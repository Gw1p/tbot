package com.gw1p.tbot.DAO;

import com.gw1p.tbot.model.UserChat;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
public class UserChatDAO extends AbstractDAO<UserChat> {

    public UserChatDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(UserChat.class);
    }

    /**
     * Находит уникальный чат с userId и chatId в БД
     *
     * @param userId пользователя чата
     * @param chatId чата, в котором есть пользователь с userId
     * @return уникальный UserChat
     */
    public UserChat findUserChat(long userId, long chatId) {
        Session session = this.sessionFactory.getCurrentSession();
        UserChat userChat = null;

        Query q = session.createQuery("from UserChat where userId = :userId and chatId = :chatId");
        q.setParameter("userId", userId);
        q.setParameter("chatId", chatId);
        List<Object> results = q.list();

        if (results.size() > 0) {
            userChat = (UserChat) results.get(0);
            logger.info("Found UserChat: " + userChat);
        } else {
            logger.info("Could not find userChat with userId: " + userId + " и chatId:" + chatId);
        }
        return userChat;
    }
}
