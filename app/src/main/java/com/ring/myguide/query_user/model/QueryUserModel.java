package com.ring.myguide.query_user.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.User;
import com.ring.myguide.query_user.QueryUserContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/21.
 */
public class QueryUserModel implements QueryUserContract.Model {

    private static final String TAG = "QueryUserModel";

    @Override
    public void putUserNameIntoCache(LinkedList<String> username) {
        CacheUtils.putQueryUser(username);
    }

    @Override
    public LinkedList<String> getUserNameFromCache() {
        return CacheUtils.getQueryUser();
    }

    @Override
    public void requestUser(String username, CallbackListener<User> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", username);
        service.getUser(params)
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
}
