package com.ufc.tbot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MessagesOut")
public class MessageOut implements Serializable {

    @Id
    private long id;

    @Column(name = "chatId")
    private long chatId;

    @Column(name = "message")
    private String message;

    @Column(name = "messageDate")
    private Date messageDate;

    @Column(name = "sent")
    private boolean sent;

    public MessageOut() {}

    public MessageOut(long id, long chatId, String message, Date messageDate, boolean sent) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.messageDate = messageDate;
        this.sent = sent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() { return chatId; }

    public void setChatId(long chatId) {
        this.chatId = chatId;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
