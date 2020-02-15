package com.ufc.tbot.service;

import com.ufc.tbot.DAO.UserDAO;
import com.ufc.tbot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * Ищет и возвращает пользователей, которым можно отправлять сообщения
     *
     * @return список подтвержденных пользователей
     */
    public List<User> getApprovedUsers() {
        return userDAO.getApprovedUsers();
    }

}
