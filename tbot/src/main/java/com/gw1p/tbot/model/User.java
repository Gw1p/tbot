package com.gw1p.tbot.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
public class User implements Serializable, Cloneable {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

    @Column(name="username")
    private String username;

    @Column(name="firstMessage")
    private Date firstMessage;

    @Column(name="phone")
    private Long phone;

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "user")
    private List<UserPermission> userPermissions;

    public User() {}

    public User(long id,
                String firstName,
                String lastName,
                String username,
                Date firstMessage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.firstMessage = firstMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(Date firstMessage) {
        this.firstMessage = firstMessage;
    }

    public Long getPhone() { return phone; }

    public void setPhone(Long phone) { this.phone = phone; }

    public List<UserPermission> getUserPermissions() {
        if (this.userPermissions == null) {
            return new ArrayList<>();
        }
        return userPermissions;
    }

    public void setUserPermissions(List<UserPermission> userPermissions) { this.userPermissions = userPermissions; }

    /**
     * Есть ли у пользователя права?
     *
     * @param permissionType права, на которые проверяют пользователя
     * @return true/false есть ли у пользователя права?
     */
    public boolean hasPermission(PermissionType permissionType) {
        try {
            for (UserPermission userPermission : getUserPermissions()) {
                if (userPermission.getPermission().getPermission().equals(permissionType)) {
                    return true;
                }
            }
        } catch (NullPointerException ex) {}
        return false;
    }

    @Override
    public String toString() {
        return "User " + this.firstName + " " + this.lastName + " ("  + this.id + ") " + this.username;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        User user2 = (User) super.clone();
        user2.userPermissions = new ArrayList<>();
        for (UserPermission userPermission : getUserPermissions()) {
            user2.userPermissions.add((UserPermission) userPermission.clone());
        }
        return user2;
    }
}
