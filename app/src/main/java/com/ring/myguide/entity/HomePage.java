package com.ring.myguide.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ring on 2019/12/2.
 */
public class HomePage implements Serializable {

    private static final long serialVersionUID = -3788460434154220699L;

    //滚动栏
    @JSONField(name = "banners")
    private List<Banner> banners;
    //美食推荐
    @JSONField(name = "food_post")
    private List<Post> foodPost;
    //景点推荐
    @JSONField(name = "place_post")
    private List<Post> placePost;
    //用户推荐
    @JSONField(name = "user_post")
    private List<Post> userPost;

    public List<Banner> getBanners() {
        return banners;
    }

    public List<Post> getFoodPost() {
        return foodPost;
    }

    public List<Post> getPlacePost() {
        return placePost;
    }

    public List<Post> getUserPost() {
        return userPost;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }

    public void setFoodPost(List<Post> foodPost) {
        this.foodPost = foodPost;
    }

    public void setPlacePost(List<Post> placePost) {
        this.placePost = placePost;
    }

    public void setUserPost(List<Post> userPost) {
        this.userPost = userPost;
    }
}
