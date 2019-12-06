package com.ring.myguide.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ring on 2019/12/2.
 */
public class Post implements Serializable {

    private static final long serialVersionUID = -1043322360397261129L;

    //城市
    private String city;
    //帖子id
    private int threadID;
    //用户简略信息
    private User author;
    //帖子标题
    private String title;
    //帖子内容
    private String content;
    //帖子类型：0用户推荐，1景点推荐，2美食推荐
    private int type;
    //图片地址
    private List<String> imgs;
    //回复次数
    private int replyNum;
    //点赞次数
    private int goodNum;
    //帖子发布时间 时间戳
    private long time;
    //是否赞过 true,赞过 false,未赞过
    private boolean isGood;
    //是否收藏 true,收藏 false,取消收藏
    private boolean isFavorite;
    //是否被删除 true,被删除 false,未被删除
    private boolean isDelete;
    //是否精品 true,是  false,否
    private boolean isBoutique;

    public String getCity() {
        return city;
    }

    public int getThreadID() {
        return threadID;
    }

    public User getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public long getTime() {
        return time;
    }

    public boolean isGood() {
        return isGood;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isBoutique() {
        return isBoutique;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setBoutique(boolean boutique) {
        isBoutique = boutique;
    }
}
