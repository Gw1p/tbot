package com.ufc.tbot.service;

import com.ufc.tbot.DAO.UserDAO;
import com.ufc.tbot.model.Permission;
import com.ufc.tbot.model.PermissionType;
import com.ufc.tbot.model.User;
import com.ufc.tbot.model.UserPermission;
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

    /**
     * Обновляет запись пользователя в БД
     *
     * @param user которого надо обновить
     */
    public User update(User user) {
        LOGGER.info("Updating: " + user);
        return userDAO.update(user);
    }

    public void removePermission(User user, PermissionType permissionType) {
        int index = -1;
        for (int i = 0; i < user.getUserPermissions().size(); i++) {
            if (user.getUserPermissions().get(i).getPermission().getPermission().equals(permissionType)) {
                index = i;
            }
        }
        if (index != -1) {
            userPermissionService.delete(user.getUserPermissions().get(index));
            user.getUserPermissions().remove(index);
        }
    }

    public void addPermission(User user, PermissionType permissionType) {
        UserPermission userPermission = new UserPermission(user, new Permission(permissionType));
        user.getUserPermissions().add(userPermission);
        userPermissionService.save(userPermission);
    }
}
