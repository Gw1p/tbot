package com.ufc.tbot.DAO;

import com.ufc.tbot.model.Chat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Repository
@Transactional
public class ChatDAO extends AbstractDAO<Chat> {

    public ChatDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(Chat.class);
    }
}
