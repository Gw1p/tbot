package com.gw1p.tbot.service;

import com.gw1p.tbot.model.UserPermission;
import com.gw1p.tbot.DAO.UserPermissionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Service
public class UserPermissionService {

    @Autowired
    private UserPermissionDAO userPermissionDAO;

    @Autowired
    private LoggerService loggerService;

    private static Logger LOGGER;

    @PostConstruct
    public void init() {
        LOGGER = loggerService.getLogger(UserPermissionService.class.getName());
    }

    /**
     * Сохраняет UserPermission в ДБ
     *
     * @param userPermission которого надо сохранить в ДБ
     */
    public void save(UserPermission userPermission) {
        LOGGER.info("Saving  " + userPermission + " in DB");
        userPermissionDAO.save(userPermission);
    }

    /**
     * Обновляет UserPermission в БД
     *
     * @param userPermission которое надо обновить
     */
    public UserPermission update(UserPermission userPermission) {
        LOGGER.info("Updating: " + userPermission);
        return userPermissionDAO.update(userPermission);
    }

    /**
     * Удаляет UserPermission из БД
     *
     * @param userPermission которое надо удалить
     */
    public void delete(UserPermission userPermission) {
        LOGGER.info("Deleting: " + userPermission);
        userPermissionDAO.delete(userPermission);
    }
}
