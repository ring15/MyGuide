package com.ring.myguide.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by ring on 2019/12/2.
 */
public class Banner implements Serializable {

    private static final long serialVersionUID = 7631801031331201591L;

    //标题
    @JSONField(name = "title")
    private String title;
    //图片地址
    @JSONField(name = "img")
    private String img;
    //点击触发
    @JSONField(name = "click_url")
    private String clickUrl;
    @JSONField(name = "img_path")
    private String imgPath;

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
