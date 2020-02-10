package com.ufc.tbot.service;

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

    // Указывает на файл, в который записывать логи
    @Value("${logPath}")
    private String logFileName;

    /**
     * Возвращает базовый логгер объект
     *
     * @param loggerName имя логгера (обычно ClassName.class.getName())
     * @return объект логгера
     */
    public Logger getLogger(String loggerName) {
        return Logger.getLogger(loggerName);
    }

    /**
     * Возвращает базовый логгер объект
     * Если fileLog - true, то параллельно записывает логи в файл 'LOG_FILE + дата.log'
     *
     * @param loggerName итя объекта логгера
     * @param fileLog записывать логи в файлы?
     * @return объект логгера
     */
    public Logger getLogger(String loggerName, boolean fileLog) {
        Logger logger = Logger.getLogger(loggerName);

        if (fileLog) {
            FileHandler fh;
            try {
                // Хотим добавить дату к концу лог файла
                // TODO динамично обновлять с новой датой
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();

                String loggerFileName = this.logFileName + dateFormatter.format(date) + ".log";
                logger.info("Writing logs to " + loggerFileName);

                File logFile = new File(loggerFileName);
                if (logFile.exists()) {
                    fh = new FileHandler(loggerFileName, true);
                } else {
                    fh = new FileHandler(loggerFileName);
                }

                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // Ставим максимальный уровень логов. Можно поменять позже
                for (Handler handler : logger.getHandlers()) {
                    handler.setLevel(Level.ALL);
                }
                logger.setLevel(Level.ALL);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return logger;
    }
}
