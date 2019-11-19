package com.ring.myguide.base;

/**
 * Created by ring on 2019/11/19.
 * 数据请求完成后的回调接口
 */
public interface CallbackListener<T> {

    void onSuccess(T data);

    void onError(Throwable throwable);
}
