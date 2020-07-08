package com.gw1p.tbot.DAO;

import com.gw1p.tbot.model.MessageIn;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Repository
@Transactional
public class MessageInDAO extends AbstractDAO<MessageIn> {

    public MessageInDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(MessageIn.class);
    }
}
