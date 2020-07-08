package com.gw1p.tbot.service;

import com.gw1p.tbot.DAO.MessageOutDAO;
import com.gw1p.tbot.model.MessageOut;
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
        LOGGER = loggerService.getLogger(MessageOutService.class.getName());
    }

    /**
     * Получает все MessageOut записи из БД где sent = 0
     *
     * @return List<MessageOut> список с MessageOut, которые надо отправить
     */
    public List<MessageOut> getAwaitingMessages() {
        LOGGER.info("Retrieving awaiting messages");
        List<MessageOut> myList = messageOutDAO.getAwaitingMessages();
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
