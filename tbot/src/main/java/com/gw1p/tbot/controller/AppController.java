package com.gw1p.tbot.controller;

import com.gw1p.tbot.service.BotService;
import com.gw1p.tbot.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Controller
public class AppController {

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private BotService botService;

    private boolean startedBot = false;

    @PostConstruct
    public void init() {
        Logger logger = loggerService.getLogger(AppController.class.getName());
        startBot();
    }

    /**
     * Запускает работу Бота.
     * Бот начинает принимать сообщения
     */
    public void startBot() {
        botService.botInit();
        botService.botStartPolling();
        botService.botStartListening();
        startedBot = true;

        while (botService.isBotWorking()) {}

        botService.botStopListening();
    }
}
