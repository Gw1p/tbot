package com.gw1p.tbot.service;

import com.gw1p.tbot.DAO.UserDAO;
import com.gw1p.tbot.model.User;
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

    @Autowired
    private UserPermissionService userPermissionService;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(UserService.class.getName());
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

    /**
     * Обновляет запись пользователя в БД
     *
     * @param user которого надо обновить
     */
    public User update(User user) {
        LOGGER.info("Updating: " + user);
        return userDAO.update(user);
    }
}
