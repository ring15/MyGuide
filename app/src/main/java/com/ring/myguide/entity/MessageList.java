package com.ring.myguide.entity;

import java.io.Serializable;

/**
 * Created by ring on 2019/11/22.
 */
public class MessageList implements Serializable {

    private User mUser;

    private String content;

    private long time;

    public MessageList(User user, String content, long time) {
        mUser = user;
        this.content = content;
        this.time = time;
    }

    public User getUser() {
        return mUser;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
