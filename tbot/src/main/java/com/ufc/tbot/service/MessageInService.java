package com.ufc.tbot.service;

import com.ufc.tbot.DAO.MessageInDAO;
import com.ufc.tbot.model.MessageIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class MessageInService {

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(MessageInService.class.getName(), true);
    }

    @Autowired
    private MessageInDAO messageInDAO;

    /**
     * Сохраняет MessageIn в БД
     *
     * @param messageIn которое надо сохранить
     */
    public void save(MessageIn messageIn) {
        LOGGER.info("Saving: " + messageIn);
        messageInDAO.save(messageIn);
    }

    /**
     * Сохраняет MessageIn в БД.
     * Если он уже существует, обновляет
     *
     * @param messageIn
     */
    public void saveOrUpdate(MessageIn messageIn) {
        MessageIn existingMessageIn = messageInDAO.findById(messageIn.getId());
        if (existingMessageIn == null) {
            messageInDAO.save(messageIn);
        } else {
            messageInDAO.update(messageIn);
        }
    }

    /**
     * Обновляет MessageIn в БД
     *
     * @param messageIn объект, который надо обновить в БД
     * @return обновленный MessageIn
     */
    public MessageIn update(MessageIn messageIn) {
        LOGGER.info("Updating: " + messageIn);
        return messageInDAO.update(messageIn);
    }
}
