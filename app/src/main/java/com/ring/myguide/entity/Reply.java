package com.ring.myguide.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ring on 2019/12/16.
 */
public class Reply implements Serializable {

    private static final long serialVersionUID = -431147568940413660L;

    //帖子id
    @JSONField(name = "thread_id")
    private int threadID;
    //用户简略信息
    @JSONField(name = "author")
    private User author;
    //评论内容
    @JSONField(name = "reply_content")
    private String content;
    //评论楼层
    @JSONField(name = "floor")
    private int floor;

    public int getThreadID() {
        return threadID;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getFloor() {
        return floor;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
