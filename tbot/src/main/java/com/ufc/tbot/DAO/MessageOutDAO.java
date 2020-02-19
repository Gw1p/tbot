package com.ufc.tbot.DAO;

import com.ufc.tbot.model.MessageOut;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class MessageOutDAO extends AbstractDAO<MessageOut> {

    public MessageOutDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(MessageOut.class);
    }

    /**
     * Получает все MessageOut записи из БД где sent = 0
     *
     * @return List<MessageOut> список с MessageOut, которые надо отправить
     */
    public List<MessageOut> getAwaitingMessages() {
        Session session = this.sessionFactory.getCurrentSession();

        List<MessageOut> awaitingMessages = new ArrayList<>();
        Query q = session.createQuery("from MessageOut where sent = 0");

        for (Object obj : q.list()) {
            awaitingMessages.add((MessageOut) obj);
        }

        if (awaitingMessages.size() > 0) {
            logger.info("Retrieved " + awaitingMessages.size() + " MessageOut where sent = 0");
        }

        return awaitingMessages;
    }
}
