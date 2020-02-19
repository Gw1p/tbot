package com.ufc.tbot.service;

import com.ufc.tbot.DAO.ChatDAO;
import com.ufc.tbot.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class ChatService {

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(ChatService.class.getName(), true);
    }

    @Autowired
    private ChatDAO chatDAO;

    /**
     * Сохраняет чат в БД
     *
     * @param chat который надо сохранить
     */
    public void save(Chat chat) {
        LOGGER.info("Saving chat: " + chat);
        chatDAO.save(chat);
    }

    /**
     * Сохраняет чат в БД, если его там нет.
     * В противном случае - обновляет.
     *
     * @param chat который надо сохранить/обновить в БД
     */
    public void saveOrUpdate(Chat chat) {
        Chat existingChat = chatDAO.findById(chat.getId());
        if (existingChat == null) {
            this.save(chat);
        } else {
            this.update(chat);
        }
    }

    /**
     * Обновляет существующий чат в БД
     *
     * @param chat который надо обновить в БД
     * @return обновленный чат
     */
    public Chat update(Chat chat) {
        LOGGER.info("Updating chat: " + chat);
        return chatDAO.update(chat);
    }

}
