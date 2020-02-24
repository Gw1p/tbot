package com.ufc.tbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufc.tbot.conversation.ActionType;
import com.ufc.tbot.conversation.Conversation;
import com.ufc.tbot.conversation.Response;
import com.ufc.tbot.conversation.ResponseType;
import com.ufc.tbot.conversation.commands.*;
import com.ufc.tbot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.logging.Logger;

/**
 * Этот класс отвечает на все интеракции бота с Telegram.
 * Содержит методы для получения/отправления сообщений.
 */
@Service
public class BotService {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private MessageOutService messageOutService;

    @Autowired
    private MessageInService messageInService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserChatService userChatService;

    @Autowired
    private UserPermissionService userPermissionService;

    @Autowired
    private AutowireCapableBeanFactory autowiredCapableBeanFactory;

    @Value("${botToken}")
    private String botToken;

    @Value("${botId}")
    private int botId; // 1 для production бота, 2 для test бота

    private TelegramBot telegramBot;

    // Принимает ли бот входящие сообщения?
    private boolean listening = false;

    // Следит ли бот за БД чтобы отправлять сообщения?
    private boolean polling = false;

    // Находится ли бот в режиме выключения? (выключится через X секунд после команды)
    private boolean shuttingDown = false;

    // Пользователи приложения
    private HashMap<Long, User> users = new HashMap<Long, User>();

    private List<Conversation> availableCommands = new ArrayList<>();

    // Отдельный чат для Админов
    private int adminChatId = -265907783;

    private List<Conversation> editingNewUser = new ArrayList<>();

    // Если пользователь с Id начал Conversation, то следующий ответ автоматически пойдет в этот Conversation
    private HashMap<UserChat, Conversation> userInteractions = new HashMap<>();

    // Как часто (в миллисекундах) Бот запрашивает сообщения в БД
    private static int POLLING_FREQUENCY = 2000;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(BotService.class.getName(), true);
        LOGGER.info("Telegram Bot initialised");

        availableCommands.add(new RequestPhoneCommand());
        availableCommands.add(new ListCommandsCommand());
        availableCommands.add(new StopBotCommand());
        availableCommands.add(new EditUserCommand());
        availableCommands.add(new EditAdminCommand());
        availableCommands.add(new StatusCommand());

        telegramBot = new TelegramBot(botToken);
    }

    /**
     * Отправляет сообщение msg в чат chatId
     *
     * @param chatId id чата, в который отправить сообщение
     * @param msg сообщение, которое надо отправить
     * @return true/false отправилось ли сообщение успешно?
     */
    private boolean sendMessage(long chatId, String msg) {
        SendMessage request = new SendMessage(chatId, msg)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(false)
                .replyMarkup(new ReplyKeyboardRemove());

        SendResponse sendResponse = telegramBot.execute(request);
        LOGGER.info("Sending message (" + chatId + "): " + sendResponse.message());
        return sendResponse.isOk();
    }

    /**
     * Отправляет сообщение msg в чат chatId
     *
     * @param chatId id чата, в который отправить сообщение
     * @param msg сообщение, которое надо отправить
     * @param messageId id сообщения, на которое надо ответить
     * @return true/false отправилось ли сообщение успешно?
     */
    private boolean sendMessage(long chatId, String msg, int messageId) {
        SendMessage request = new SendMessage(chatId, msg)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(false)
                .replyToMessageId(messageId)
                .replyMarkup(new ReplyKeyboardRemove());

        SendResponse sendResponse = telegramBot.execute(request);
        LOGGER.info("Sending message (" + chatId + "): " + sendResponse.message());
        return sendResponse.isOk();
    }

    /**
     * Отправляет сообщение msg в чат chatId
     *
     * @param chatId id чата, в который отправить сообщение
     * @param msg сообщение, которое надо отправить
     * @param keyboard кастомная клавиатура, с которой ответить пользователю
     * @return true/false отправилось ли сообщение успешно?
     */
    private boolean sendMessage(long chatId, String msg , Keyboard keyboard) {
        SendMessage request = new SendMessage(chatId, msg)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(false)
                .disableNotification(false)
                .replyMarkup(keyboard);
        LOGGER.info("Sending message with keyboard (" + chatId + "): " + msg);
        SendResponse sendResponse = telegramBot.execute(request);
        LOGGER.info("Response message: " + sendResponse.message());
        return sendResponse.isOk();
    }

    /**
     * Отправляет сообщение msg в чат chatId
     *
     * @param chatId id чата, в который отправить сообщение
     * @param msg сообщение, которое надо отправить
     * @param keyboard кастомная клавиатура, с которой ответить пользователю
     * @param replyToMessageId id сообщения, на которое ответит Бот
     * @return true/false отправилось ли сообщение успешно?
     */
    private boolean sendMessage(long chatId, String msg , Keyboard keyboard, int replyToMessageId) {
        SendMessage request = new SendMessage(chatId, msg)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(false)
                .disableNotification(false)
                .replyToMessageId(replyToMessageId)
                .replyMarkup(keyboard);
        LOGGER.info("Sending message with keyboard (" + chatId + "): " + msg);
        SendResponse sendResponse = telegramBot.execute(request);
        LOGGER.info("Response message: " + sendResponse.message());
        return sendResponse.isOk();
    }

    /**
     * Обновляет MessageOut и подтверждает, что сообщение было отправлено
     *
     * @param messageOut которое надо обновить
     */
    private void messageOutSuccess(MessageOut messageOut) {
        messageOut.setSent(true);
        messageOut.setMessageDate(new Date());
        messageOutService.updateMessageOut(messageOut);
    }

    /**
     * Инициализирует бота и получает список пользователей, которым можно отправлять сообщения
     */
    public void botInit() {
        users = new HashMap<>();
        List<User> retrievedUsers = userService.findAll();
        for (User user : retrievedUsers) {
            users.put(user.getId(), user);
        }
    }

    /**
     * Начинает поллить ДБ на сообщения, которые надо отправить
     */
    public void botStartPolling() {
        polling = true;
        LOGGER.info("Bot Start Polling DB for MessagesOut");
        messageOutService.getAwaitingMessages();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (polling) {
                    try {
                        LOGGER.info("Polling db for awaiting messages...");
                        List<MessageOut> awaitingMessages = messageOutService.getAwaitingMessages();

                        if (awaitingMessages.size() == 0) {
                            LOGGER.info("No awaiting messages found");
                        }

                        for (MessageOut messageOut : awaitingMessages) {
                            int maxTries = 3;
                            for (int i = 0; i < maxTries; i++) {
                                if (messageOut.getMessageFor() == botId) {
                                    LOGGER.info("Attempt (" + i + ") to send message " +
                                            messageOut.getMessage() + " to chat " + messageOut.getChatId());
                                    boolean messageSuccess = sendMessage(
                                            messageOut.getChatId(),
                                            messageOut.getMessage()
                                    );

                                    if (messageSuccess) {
                                        LOGGER.info("Message sent successfully");
                                        messageOutSuccess(messageOut);
                                        break;
                                    } else if (i < maxTries - 1) {
                                        LOGGER.warning("Unsuccessful, retrying");
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(POLLING_FREQUENCY);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception ex) {
                        LOGGER.warning("Something went terribly wrong. Polling not working.");
                    }
                }

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Прекращает поллить ДБ на сообщения, которые надо отправить
     */
    public void botStopPolling() {
        polling = false;
        LOGGER.info("Bot Stop Polling DB for MessagesOut");
    }

    /**
     * Создает listener для получения сообщений из Telegram
     * (подписывается на updates)
     */
    public void botStartListening() {
        listening = true;
        shuttingDown = false;

        telegramBot.setUpdatesListener(new UpdatesListener() {
            @Override
            public int process(List<Update> updates) {
                // Если Бот выключается - считаем, что Updates не получены
                if (shuttingDown) {
                    return UpdatesListener.CONFIRMED_UPDATES_NONE;
                }

                try {
                    if (users.isEmpty()) {
                        LOGGER.warning("Users empty. Reinitialising");
                        botInit();
                    }

                    LOGGER.info("TelegramBot: got " + updates.size() + " updates");
                    for (Update update : updates) {

                        LOGGER.info("Update summary: " + update.toString());
                        LOGGER.info(update.message().from().firstName() + " " + update.message().from().lastName()
                                + "(" + update.message().from().id() + ")" + " in chat " +
                                update.message().chat().id() + " says: " + update.message().text());

                        // Нужно убедиться, что пользователь был подтвержден для использования Бота
                        long userId = Integer.toUnsignedLong(update.message().from().id());
                        Chat chat = new Chat(update.message().chat().id(),
                                update.message().chat().type().toString(),
                                update.message().chat().firstName(),
                                update.message().chat().lastName(),
                                update.message().chat().username(),
                                update.message().chat().title(),
                                update.message().chat().inviteLink());
                        try {
                            chatService.saveOrUpdate(chat);
                        } catch (Exception ex) {
                            LOGGER.warning("Cannot save chat: " + ex.getMessage());
                        }

                        // Новый пользователь
                        if (!users.containsKey(userId)) {
                            LOGGER.info("New user: " + userId);
                            User user = new User(update.message().from().id(),
                                    update.message().from().firstName(),
                                    update.message().from().lastName(),
                                    update.message().from().username(),
                                    new Date());
                            userService.save(user);
                            users.put(userId, user);

                            Keyboard keyboard = new ReplyKeyboardMarkup(
                                    new String[]{ "Дать " + user.getFirstName() + " Права Пользователя" },
                                    new String[]{ "Дать " + user.getFirstName() + " Права Админа (+ пользователя)" },
                                    new String[]{ "Ничего" }
                            ).oneTimeKeyboard(false)
                                    .resizeKeyboard(true)
                                    .selective(false);
                            sendMessage(adminChatId, "Новый пользователь: " + user +
                                    "\nЧто с ним делать?", keyboard);

                            EditNewUserCommand editNewUser = new EditNewUserCommand();
                            editNewUser.reset();
                            autowiredCapableBeanFactory.autowireBean(editNewUser);

                            List<User> newUserList = new ArrayList<>();
                            newUserList.add(user);
                            editingNewUser.add(editNewUser);
                            editNewUser.step(null, users.get(userId), newUserList);
                        }

                        MessageIn messageIn = new MessageIn(
                                update.message().messageId(),
                                update.message().text(),
                                new Date(),
                                update.message().chat().id(),
                                update.message().from().id(),
                                botId
                        );
                        try {
                            messageInService.saveOrUpdate(messageIn);
                        } catch (Exception ex) {
                            LOGGER.warning("Cannot save message: " + ex.getMessage());
                        }

                        UserChat userChat = new UserChat(update.message().from().id(),
                                update.message().chat().id(),
                                update.message().chat().type().toString(),
                                new Date());
                        try {
                            userChatService.saveIfNotExists(userChat);
                        } catch (Exception ex) {
                            LOGGER.warning("Cannot save userchat: " + ex.getMessage());
                        }

                        // Проверка Прав
                        if (!users.get(userId).hasPermission(PermissionType.USER)) {
                            LOGGER.warning("Unapproved User (" + update.message().from().id() + ")!");
                            break;
                        }

                        boolean foundCommand = false;
                        // Есть ли новые пользователей, которых админы должны подтвердить?
                        if (users.get(userId).hasPermission(PermissionType.ADMIN)
                                && editingNewUser.size() > 0
                                && update.message().chat().id() == adminChatId) {
                            foundCommand = true;
                            Conversation lastEditNewUser = editingNewUser.get(editingNewUser.size() - 1);
                            Response response = lastEditNewUser.step(
                                    update.message(),
                                    users.get(userId),
                                    new ArrayList<>()
                            );
                            parseResponse(response, users.get(userId), adminChatId);
                            if (lastEditNewUser.finished()) {
                                editingNewUser.remove(editingNewUser.size() - 1);
                            }
                        }

                        // Проверяем комманды, которые пользователи уже начали
                        if (userInteractions.containsKey(userChat) && !foundCommand) {
                            Conversation conversation = userInteractions.get(userChat);
                            foundCommand = true;
                            Response response = conversation.step(
                                    update.message(),
                                    users.get(userId),
                                    new ArrayList<>(users.values())
                            );
                            parseResponse(response, users.get(userId), update.message().chat().id());
                            if (conversation.finished()) {
                                userInteractions.remove(userChat);
                            }
                        } else if (!foundCommand) {

                            // Проверяем существующие команды
                            for (Conversation command : availableCommands) {
                                LOGGER.info("Cmd " + command.getClass().getName() +
                                        " can start: " + command.canStart(
                                                update.message().text(),
                                                users.get(userId),
                                                chat)
                                );
                                if (command.canStart(update.message().text(), users.get(userId), chat)) {
                                    try {
                                        foundCommand = true;
                                        Conversation newCommand = (Conversation) command.clone();
                                        autowiredCapableBeanFactory.autowireBean(newCommand);

                                        Response response = newCommand.step(
                                                update.message(),
                                                users.get(userId),
                                                new ArrayList<>(users.values())
                                        );
                                        parseResponse(response, users.get(userId), update.message().chat().id());
                                        if (!newCommand.finished()) {
                                            userInteractions.put(userChat, newCommand);
                                        }
                                        break;
                                    } catch (CloneNotSupportedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        if (!foundCommand && update.message().text().charAt(0) == '/') {
                            sendMessage(update.message().chat().id(), "Не понял комманду. " +
                                    "Чтобы узнать список комманд, напишите /помощь.");
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.warning ("Something went terribly wrong. Cannot receive messages: " + ex.getMessage());
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    /**
     * Обрабатывает Response, полученный из Conversation
     *
     * @param response полученный из Conversation
     * @param user который отправил сообщение, которое вызвало response
     * @param chatId id чата, в котором идет переписка
     */
    private void parseResponse(Response response, User user, long chatId) {
        LOGGER.info("Parsing response: " + response.toString());
        parseAction(response.getAction(), response.getActionObject(), user, chatId, response.getResponseText());

        if (response.getResponseType().equals(ResponseType.TEXT)) {
            sendMessage(chatId, response.getResponseText());
        } else if (response.getResponseType().equals(ResponseType.KEYBOARD)) {
            sendMessage(chatId, response.getResponseText(), response.getResponseKeyboard());
        } else if (response.getResponseType().equals(ResponseType.TEXT_REPLY)) {
            sendMessage(chatId, response.getResponseText(), response.getMessageId());
        } else if (response.getResponseType().equals(ResponseType.KEYBOARD_REPLY)) {
            sendMessage(
                    chatId,
                    response.getResponseText(),
                    response.getResponseKeyboard(),
                    response.getMessageId()
            );
        }
    }

    /**
     * Обрабатывает действие, содержащееся в Response
     *
     * @param actionType тип Action, связанного с Response
     * @param actionObject дополнительный объект для выполнения Action
     * @param user пользователь, отправивший изначальный запрос
     * @param chatId id чата, где написал user
     */
    private void parseAction(ActionType actionType, Object actionObject, User user, long chatId, String text) {
        if (actionType.equals(ActionType.UPDATE_USER)) {
            User updatedUser = (User) actionObject;
            LOGGER.info("Was size: " + users.get(updatedUser.getId()).getUserPermissions().size() + " became: " + updatedUser.getUserPermissions().size());
            userService.update(updatedUser);
            this.users.put(updatedUser.getId(), updatedUser);
            LOGGER.info("Updated user:  " + updatedUser);
        } else if (actionType.equals(ActionType.STOP_BOT)) {
            stopBot();
        } else if (actionType.equals(ActionType.EXTRA_ACTION)) {
            Collection<Response> extraActions = (Collection<Response>) actionObject;
            for (Response extraResponse : extraActions) {
                parseResponse(extraResponse, user, chatId);
            }
        } else if (actionType.equals(ActionType.MESSAGE_USER)) {
            sendMessage(((User)actionObject).getId(), text);
        }
    }

    /**
     * Останавливает Бота после 3 секунд
     */
    private void stopBot() {
        LOGGER.info("Stopping bot...");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    shuttingDown = true;
                    polling = false;
                    LOGGER.info("Stopping bot in 3...");
                    Thread.sleep(1000);
                    LOGGER.info("Stopping bot in 2...");
                    Thread.sleep(1000);
                    LOGGER.info("Stopping bot in 1...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listening = false;
                botStopPolling();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Убирает listeners для получения updates
     */
    public void botStopListening() {
        telegramBot.removeGetUpdatesListener();
    }

    /**
     * Принимает ли бот сообщения? Поллит ли бот ДБ?
     *
     * @return true/false в зависимости от того, работает ли Бот
     */
    public boolean isBotWorking() {
        return this.listening || this.polling;
    }

}
