package com.ring.myguide.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ring on 2019/11/21.
 */
public class OtherUser implements Serializable {
    private static final long serialVersionUID = 4749867187090300974L;
    //用户UID
    @JSONField(name = "uuid")
    private String uid;
    //用户名
    @JSONField(name = "username")
    private String userName;
    //用户昵称
    @JSONField(name = "nickname")
    private String nickname;
    //身份铭牌: 0:普通用户 1:官方管理员
    @JSONField(name = "badge")
    private int badge;
    //用户头像地址
    @JSONField(name = "user_img")
    private String userImg;
    //性别
    @JSONField(name = "sex")
    private int sex;
    //用户生日
    @JSONField(name = "birthday")
    private String birthday;
    //用户自我介绍
    @JSONField(name = "introduce")
    private String introduce;
    //用户注册时间
    @JSONField(name = "register_time")
    private String registerTime;
    //是否为好友关系，1是，0不是
    @JSONField(name = "is_friend")
    private int isFriend;
    //是否在黑名单中，1是，0不是
    @JSONField(name = "is_black")
    private int isBlack;
    //头像保存在本地的位置
    private String userImgPaht;

    public String getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBadge() {
        return badge;
    }

    public String getUserImg() {
        return userImg;
    }

    public int getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public int getIsBlack() {
        return isBlack;
    }

    public String getUserImgPaht() {
        return userImgPaht;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public void setIsBlack(int isBlack) {
        this.isBlack = isBlack;
    }

    public void setUserImgPaht(String userImgPaht) {
        this.userImgPaht = userImgPaht;
    }
}
