package com.ring.myguide.message.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/19.
 */
public class MessageModel implements MessageContract.Model {

    private static final String TAG = "MessageModel";

    @Override
    public void queryUser(String queryUserName, CallbackListener<User> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("query_username", queryUserName);
        service.queryUser(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    User user = null;
                    try {
                        String result = responseBody.string();
                        Log.i(TAG, result);
                        JSONObject data = JsonParse.parseString(result);
                        user = JSON.toJavaObject(data, User.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return user;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        listener.onSuccess(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public LinkedList<MessageList> getMessageList() {
        if (CacheUtils.getUser() != null && CacheUtils.getUser().getUserName() != null) {
            return CacheUtils.getMessageLists(CacheUtils.getUser().getUserName());
        }
        return null;
    }

    @Override
    public void putMessageList(LinkedList<MessageList> messageLists) {
        if (CacheUtils.getUser() != null && CacheUtils.getUser().getUserName() != null) {
            CacheUtils.putMessageLists(messageLists, CacheUtils.getUser().getUserName());
        }
    }
}
