package com.ring.myguide.base;

/**
 * Created by ring on 2019/11/18.
 * MVP中的view层，BaseActivity继承的基类，其中可以放置一些共有方法
 */
public interface BaseView {

    //基本所有activity都可能用到的方法可以放在基类当中
    void showToast(int resId);

    void showToast(String toast);
}
