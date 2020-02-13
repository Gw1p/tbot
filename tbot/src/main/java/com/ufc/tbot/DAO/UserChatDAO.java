package com.ufc.tbot.DAO;

import com.ufc.tbot.model.UserChat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Repository
@Transactional
public class UserChatDAO extends AbstractDAO<UserChat> {

    public UserChatDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(UserChat.class);
    }
}
