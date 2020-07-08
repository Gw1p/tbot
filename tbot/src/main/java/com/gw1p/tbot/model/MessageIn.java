package com.gw1p.tbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MessagesIn")
public class MessageIn implements Serializable {

    @Id
    private long id;

    @Column(name = "message")
    private String message;

    @Column(name = "messageDate")
    private Date messageDate;

    @Column(name = "chatId")
    private long chatId;

    @Column(name = "userId")
    private long userId;

    @Column(name = "receivedBy")
    private long receivedBy;

    public MessageIn() {}

    public MessageIn(long id, String message, Date messageDate, long chatId, long userId, long receivedBy) {
        this.id = id;
        this.message = message;
        this.messageDate = messageDate;
        this.chatId = chatId;
        this.userId = userId;
        this.receivedBy = receivedBy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getReceivedBy() { return receivedBy; }

    public void setReceivedBy(long receivedBy) { this.receivedBy = receivedBy; }
}
