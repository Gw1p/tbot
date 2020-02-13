package com.ufc.tbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="username", unique=true)
    private String username;

    @Column(name="first_message")
    private Date firstMessage;

    @Column(name="approved")
    private boolean approved;

    public User() {

    }

    public User(long id,
                String firstName,
                String lastName,
                String username,
                Date firstMessage,
                boolean approved) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.firstMessage = firstMessage;
        this.approved = approved;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
