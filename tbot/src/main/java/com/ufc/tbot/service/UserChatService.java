package com.ufc.tbot.service;

import com.ufc.tbot.DAO.UserChatDAO;
import com.ufc.tbot.model.UserChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class UserChatService {

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @Autowired
    private UserChatDAO userChatDAO;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(UserChatService.class.getName(), true);
    }

    /**
     * Сохраняет UserChat в БД
     *
     * @param userChat которое надо сохранить
     */
    public void save(UserChat userChat) {
        LOGGER.info("Saving: " + userChat);
        userChatDAO.save(userChat);
    }

    /**
     * Сохраняет UserChat в БД.
     * Если он уже существует, обновляет
     *
     * @param userChat который надо сохранить/обновить в БД
     */
    public void saveOrUpdate(UserChat userChat) {
        UserChat existingUserChat = userChatDAO.findUserChat(userChat.getUserId(), userChat.getChatId());
        if (existingUserChat == null) {
            userChatDAO.save(userChat);
        } else {
            userChatDAO.update(userChat);
        }
    }

    /**
     * Сохраняет UserChat в БД, если он не существует.
     *
     * @param userChat который надо сохранить в БД
     */
    public void saveIfNotExists(UserChat userChat) {
        UserChat existingUserChat = userChatDAO.findUserChat(userChat.getUserId(), userChat.getChatId());
        if (existingUserChat == null) {
            userChatDAO.save(userChat);
        }
    }

    /**
     * Обновляет UserChat в БД
     *
     * @param userChat объект, который надо обновить в БД
     * @return обновленный UserChat
     */
    public UserChat update(UserChat userChat) {
        LOGGER.info("Updating: " + userChat);
        return userChatDAO.update(userChat);
    }

}
