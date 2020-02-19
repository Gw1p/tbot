package com.ufc.tbot.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "UserPermissions")
public class UserPermission implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permissionId", nullable = false)
    private Permission permission;

    public UserPermission() {}

    public UserPermission(User user, Permission permission) {
        this.user = user;
        this.permission = permission;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof UserPermission) {
            UserPermission userPermission = (UserPermission) obj;
            try {
                retVal = userPermission.permission.getId() == this.permission.getId()
                        && userPermission.user.getId() == this.user.getId();
            } catch (NullPointerException ex) {}
        }

        return retVal;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
