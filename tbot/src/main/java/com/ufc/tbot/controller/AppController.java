package com.ufc.tbot.controller;

import com.ufc.tbot.TbotApplication;
import com.ufc.tbot.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Controller
public class AppController {

    @Autowired
    private LoggerService loggerService;

    @PostConstruct
    public void init() {
        Logger logger = loggerService.getLogger(AppController.class.getName(), true);
    }

}
