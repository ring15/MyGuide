package com.ring.myguide.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by ring on 2019/11/19.
 */
public abstract class BaseFragment<P extends BasePresenter, V extends BaseView> extends Fragment {

    //获取fragment资源
    protected abstract int getIdResource();

    //绑定资源的公有方法
    protected abstract void findView(View view);

    //初始操作的公有方法
    protected abstract void init();

    //抽象方法声明presenter接口
    protected abstract P getPresenter();

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在onCreate中绑定presenter
        if (getPresenter() != null) {
            mPresenter = getPresenter();
            //给presenter绑定view
            mPresenter.attachView((V) this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getIdResource(), container, false);
        findView(view);
        init();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在onDestroy中解除presenter绑定
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
