package com.gw1p.tbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * Этот сервис используется для инициализации всех Логов
 */
@Service
public class LoggerService {

    /**
     * Возвращает базовый логгер объект
     *
     * @param loggerName имя логгера (обычно ClassName.class.getName())
     * @return объект логгера
     */
    public Logger getLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }
}
