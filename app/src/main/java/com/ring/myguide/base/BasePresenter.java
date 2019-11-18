package com.ring.myguide.base;

import java.lang.ref.WeakReference;

/**
 * Created by ring on 2019/11/18.
 */
public abstract class BasePresenter<V extends BaseView> {

    //弱引用持有activity，避免造成内存泄漏
    protected WeakReference<V> mView;

    public void attachView(V view) {
        mView = new WeakReference<>(view);
    }

    public void detachView() {
        mView = null;
    }

    public boolean isViewAttached() {
        return mView.get() != null;
    }
}
