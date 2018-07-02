package com.intern.yef.yefmobileapp.Models;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String senderId;

    public ChatMessage() {
    }

    public ChatMessage(String messageText, String messageUser, String senderId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.senderId = senderId;

        messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
