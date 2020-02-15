package com.ufc.tbot.service;

import com.ufc.tbot.DAO.UserDAO;
import com.ufc.tbot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(UserService.class.getName(), true);
    }

    /**
     * Сохраняет пользователя в ДБ
     *
     * @param user которого надо сохранить в ДБ
     */
    public void save(User user) {
        LOGGER.info("Saving  " + user + " in DB");
        userDAO.save(user);
    }

    public List<User> findAll() {
        LOGGER.info("Retrieving all users");
        return userDAO.findAll();
    }
}
