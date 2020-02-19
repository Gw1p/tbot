package com.ufc.tbot.DAO;

import com.ufc.tbot.model.UserPermission;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Repository
@Transactional
public class UserPermissionDAO extends AbstractDAO<UserPermission> {

    public UserPermissionDAO(EntityManagerFactory factory) {
        super(factory);
        setClazz(UserPermission.class);
    }

}
