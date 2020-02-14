package com.ufc.tbot.service;

import com.ufc.tbot.DAO.MessageOutDAO;
import com.ufc.tbot.model.MessageOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Logger;

@Service
public class MessageOutService {

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @Autowired
    private MessageOutDAO messageOutDAO;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(MessageOutService.class.getName(), true);
    }

    /**
     * Получает все MessageOut записи из БД где sent = 0
     *
     * @return List<MessageOut> список с MessageOut, которые надо отправить
     */
    public List<MessageOut> getAwaitingMessages() {
        LOGGER.info("Retrieving awaiting messages");
        LOGGER.info(messageOutDAO.toString());
        List<MessageOut> myList = messageOutDAO.getAwaitingMessages();
        LOGGER.info("Retrieved awaiting messages");
        return myList;
    }

    /**
     * Обновляет существующий MessageOut
     *
     * @param updated обновленная версия MessageOut
     * @return обновленный MessageOut
     */
    public MessageOut updateMessageOut(MessageOut updated) {
        return this.messageOutDAO.update(updated);
    }
}
