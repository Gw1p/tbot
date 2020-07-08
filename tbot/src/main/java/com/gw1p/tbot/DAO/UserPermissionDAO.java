package com.gw1p.tbot.DAO;

import com.gw1p.tbot.model.UserPermission;
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
