package com.ufc.tbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.ufc.tbot.controller.AppController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @Value("${botToken}")
    private String botToken;

    private TelegramBot telegramBot;

    // Принимает ли бот входящие сообщения?
    private boolean listening = false;

    // Находится ли бот в режиме выключения? (выключится через X секунд после команды)
    private boolean shuttingDown = false;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(AppController.class.getName(), true);
        LOGGER.info("Telegram Bot initialised");
        telegramBot = new TelegramBot(botToken);
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
                            + "(" + update.message().from().id() + ")" + " says: " + update.message().text());

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
