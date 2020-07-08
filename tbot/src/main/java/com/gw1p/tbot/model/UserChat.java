package com.gw1p.tbot.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "UserChats")
public class UserChat implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name= "id")
    private long id;

    @Column(name = "userId")
    private long userId;

    @Column(name = "chatId")
    private long chatId;

    @Column(name = "chatType")
    private String chatType;

    @Column(name = "discoveredDate")
    private Date discoveredDate;

    public UserChat() {}

    public UserChat(long userId, long chatId, String chatType, Date discoveredDate) {
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

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof UserChat) {
            UserChat userChat = (UserChat) v;
            retVal = userChat.userId == this.userId && userChat.chatId == this.chatId;
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        return (int) (this.userId ^ this.chatId);
    }
}
