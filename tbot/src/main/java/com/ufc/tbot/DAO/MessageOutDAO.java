package com.ufc.tbot.DAO;

import com.ufc.tbot.model.MessageOut;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Repository
@Transactional
public class MessageOutDAO extends AbstractDAO<MessageOut> {

    public MessageOutDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(MessageOut.class);
    }
}
