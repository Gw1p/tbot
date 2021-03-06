package com.gw1p.tbot.DAO;

import com.gw1p.tbot.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
public class UserDAO extends AbstractDAO<User> {

    public UserDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(User.class);
    }

    /**
     * Возвращает первого пользователя из БД с username = username
     *
     * @param username пользователя, которого найти в БД
     * @return User с соответствующим username
     */
    public User findByUsername(String username) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = null;

        Query q = session.createQuery("from User where username = :username");
        q.setParameter("username", username);
        List<Object> results = q.list();

        if (results.size() > 0) {
            user = (User) results.get(0);
            logger.info("Found User: " + user);
        } else {
            logger.info("Could not find user with username: " + username);
        }
        return user;
    }
}
