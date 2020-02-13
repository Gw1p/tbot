package com.ufc.tbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Chats")
public class Chat implements Serializable {

    @Id
    @Column(name="id")
    private long id;

    @Column(name="chat_type")
    private String chatType;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="username", unique=true)
    private String username;

    @Column(name="title")
    private String title;

    @Column(name="invite_link")
    private String inviteLink;

    public Chat() {}

    public Chat(long id,
                String chatType,
                String firstName,
                String lastName,
                String username,
                String title,
                String inviteLink) {
        this.id = id;
        this.chatType = chatType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.title = title;
        this.inviteLink = inviteLink;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }
}
