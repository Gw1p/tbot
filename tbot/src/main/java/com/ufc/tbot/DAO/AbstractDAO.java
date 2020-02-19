package com.ufc.tbot.DAO;

import com.ufc.tbot.service.LoggerService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Базовый класс для всех DAO классов
 *
 * @param <T> model класс, DAO которого наследующего у AbstractDAO
 */
@Transactional
public abstract class AbstractDAO<T extends Serializable> {

    // Класс model, DAO которого наследует у AbstractDAO
    private Class<T> clazz;

    @Autowired
    protected SessionFactory sessionFactory;

    @Autowired
    private LoggerService loggerService;

    protected static Logger logger;

    /**
     * Конструктор, инициалазирующий sessionFactory
     *
     * @param factory
     */
    public AbstractDAO(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    @PostConstruct
    public void init() { logger = loggerService.getLogger(clazz.getName(), true); }

    public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    protected final Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Этот метод должен быть вызван в constructor наследующего класса
     *
     * @param clazzToSet model класс, DAO которого наследует у AbstractDAO
     */
    public void setClazz(Class <T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findById(long id) {
        try {
            return (T) getCurrentSession().get(clazz, id);
        } catch (Exception ex) {
            logger.warning("Could not find by id: " + id);
            return null;
        }
    }

    /**
     * Ищет и возвращает все объекты класса T из БД
     *
     * @return все объекты класса T из БД (как List<Object>)
     */
    public List<T> findAll() {
        try {
            return getCurrentSession().createQuery("from " + clazz.getName()).list();
        } catch (Exception ex) {
            logger.warning("Could not findAll: " + clazz.getName());
            return new ArrayList<>();
        }
    }

    /**
     * Сохраняет объект в БД
     *
     * @param entity которую надо сохранить в БД
     */
    public void save(T entity) {
        try{
            getCurrentSession().persist(entity);
            logger.info("Saved " + clazz.getName() + ": " + entity);
        } catch (Exception ex) {
            logger.warning("Could not save " + clazz.getName() + ": " + entity);
        }
    }

    /**
     * Обновляет запись в БД
     *
     * @param entity которую надо обновить в БД
     * @return обновленную запись в БД
     */
    public T update(T entity) {
        try {
            return (T) getCurrentSession().merge(entity);
        } catch (Exception ex) {
            logger.warning("Could not update " + clazz.getName() + ": " + entity);
            return null;
        }
    }

    /**
     * Удаляет объект из БД
     *
     * @param entity которую надо удалить из БД
     */
    public void delete (T entity) {
        try {
            getCurrentSession().delete(entity);
            logger.info("Deleted " + clazz.getName() + ": " + entity);
        } catch (Exception ex) {
            logger.warning("Could not delete " + clazz.getName() + ": " + entity);
        }
    }

    /**
     * Находит объект с id и удаляет
     *
     * @param id объекта, который надо удалить из БД
     */
    public void deleteById(long id) {
        try {
            final T entity = findById(id);
            delete(entity);
            logger.info("Deleted " + clazz.getName() + " with id " + id);
        } catch (Exception ex) {
            logger.warning("Could not delete " + clazz.getName());
        }
    }

}
