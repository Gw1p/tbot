package com.gw1p.tbot.model;

import javax.persistence.*;

@Entity
@Table(name = "Permissions")
public class Permission {

    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private PermissionType permission;

    public Permission() {}

    public Permission(PermissionType permission) {
        if (permission.equals(PermissionType.ADMIN)) {
            this.id = 1;
        } else if (permission.equals(PermissionType.USER)) {
            this.id = 2;
        }
        this.permission = permission;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PermissionType getPermission() {
        return permission;
    }

    public void setPermission(PermissionType permission) {
        this.permission = permission;
    }
}
