package com.ufc.tbot.model;

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
    private String message;
    private Date messageDate;
    private long chatId;
    private long userId;

    public MessageIn() {}

    public MessageIn(long id, String message, Date messageDate, long chatId, long userId) {
        this.id = id;
        this.message = message;
        this.messageDate = messageDate;
        this.chatId = chatId;
        this.userId = userId;
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

}
