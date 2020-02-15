package com.ufc.tbot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserChats")
public class UserChat implements Serializable {

    @Id
    private long id;
    private long userId;
    private long chatId;
    private String chatType;
    private Date discoveredDate;

    public UserChat() {}

    public UserChat(long id, long userId, long chatId, String chatType, Date discoveredDate) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
        this.chatType = chatType;
        this.discoveredDate = discoveredDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public Date getDiscoveredDate() {
        return discoveredDate;
    }

    public void setDiscoveredDate(Date discoveredDate) {
        this.discoveredDate = discoveredDate;
    }
}
