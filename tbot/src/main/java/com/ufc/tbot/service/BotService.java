package com.ufc.tbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufc.tbot.model.MessageOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
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

    @Value("${botToken}")
    private String botToken;

    private TelegramBot telegramBot;

    // Принимает ли бот входящие сообщения?
    private boolean listening = false;

    // Следит ли бот за БД чтобы отправлять сообщения?
    private boolean polling = false;

    // Находится ли бот в режиме выключения? (выключится через X секунд после команды)
    private boolean shuttingDown = false;

    // Как часто (в миллисекундах) Бот запрашивает сообщения в БД
    private static int POLLING_FREQUENCY = 2000;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(BotService.class.getName(), true);
        LOGGER.info("Telegram Bot initialised");
        telegramBot = new TelegramBot(botToken);
    }

    private boolean sendMessage(long chatId, String msg) {
        SendMessage request = new SendMessage(chatId, msg)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(false);

        SendResponse sendResponse = telegramBot.execute(request);
        LOGGER.info("Response message: " + sendResponse.message());
        return sendResponse.isOk();
    }

    private void messageOutSuccess(MessageOut messageOut) {
        messageOut.setSent(true);
        messageOut.setMessageDate(new Date());
        messageOutService.updateMessageOut(messageOut);
    }

    public void botStartPolling() {
        polling = true;
        LOGGER.info("Bot Start Polling DB for MessagesOut");
        messageOutService.getAwaitingMessages();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (polling) {
                    LOGGER.info("Polling db for awaiting messages...");
                    List<MessageOut> awaitingMessages = messageOutService.getAwaitingMessages();

                    if (awaitingMessages.size() == 0) {
                        LOGGER.info("No awaiting messages found");
                    }

                    for (MessageOut messageOut : awaitingMessages) {
                        int maxTries = 3;
                        for (int i = 0; i < maxTries; i++) {
                            LOGGER.info("Attempt (" + i + ") to send message " + messageOut.getMessage() + " to chat " + messageOut.getChatId());
                            boolean messageSuccess = sendMessage(messageOut.getChatId(), messageOut.getMessage());

                            if (messageSuccess) {
                                LOGGER.info("Message sent successfully");
                                messageOutSuccess(messageOut);
                                break;
                            } else if (i < maxTries - 1) {
                                LOGGER.warning("Unsuccessful, retrying");
                            }
                        }
                    }
                    try {
                        Thread.sleep(POLLING_FREQUENCY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

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

                LOGGER.info("TelegramBot: got  " + updates.size() + " updates");
                for (Update update : updates) {
                    LOGGER.info("Update summary: " + update.toString());
                    LOGGER.info(update.message().from().firstName() + " " + update.message().from().lastName()
                            + "(" + update.message().from().id() + ")" + " in chat " +
                            update.message().chat().id() + " says: " + update.message().text());

                    // При получении команды, останавливаем работу Бота
                    if (update.message().text().equals("stop")) {
                        LOGGER.info("Stopping bot...");

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    shuttingDown = true;
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
                                polling = false;
                            }
                        };
                        Thread thread = new Thread(runnable);
                        thread.start();
                    }
                }
                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        });
    }

    /**
     * Убирает listeners для получения updates
     */
    public void botStopListening() {
        telegramBot.removeGetUpdatesListener();
    }

    /**
     * Принимает ли бот сообщения?
     *
     * @return true/false в зависимости от того, принимает ли бот сообщения
     */
    public boolean isBotListening() {
        return this.listening;
    }

}
