package com.groep4.mindfulness.model;

import java.util.Date;

/**
 * Simpele Class voor chat
 */
public class Message {

    private String content;
    private String messageUser;
    private long messageTime;
    private boolean gelezen;

    /**
     * Chat list heeft no-args constructor nodig.
     */
    public  Message(){

    }

    public Message(String content, String messageUser) {

        this.content = content;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
        this.gelezen = false;
    }

    public boolean isGelezen() {
        return gelezen;
    }

    public void setGelezen(boolean gelezen) {
        this.gelezen = gelezen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

}