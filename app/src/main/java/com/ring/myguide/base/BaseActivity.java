package com.ring.myguide.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ring on 2019/11/18.
 * 所有activity需要继承的方法
 */
public abstract class BaseActivity<P extends BasePresenter, V extends BaseView> extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化presenter
        if (getPresenter() != null) {
            mPresenter = getPresenter();
            //给presenter绑定view
            mPresenter.attachView((V) this);
        }
        setContentView(getIdResource());
        //下面两个方法不能换位置，会出错
        findView();
        init();
    }

    //获取activity资源
    protected abstract int getIdResource();

    protected P mPresenter;

    //抽象方法，需要在子类中实现
    protected abstract P getPresenter();

    //绑定资源的公有方法
    protected abstract void findView();

    //初始操作的公有方法
    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity结束时解除presenter和view的绑定
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
