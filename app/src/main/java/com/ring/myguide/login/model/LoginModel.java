package com.ring.myguide.login.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by ring on 2019/11/19.
 */
public class LoginModel implements LoginContract.Model {

    private static final String TAG = "LoginModel";

    @Override
    public void requestLogin(String userName, String password, CallbackListener<User> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("user_name", userName);
        params.put("password", password);
        service.doLogin(params)
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
                .observeOn(Schedulers.io())
                .doOnNext(CacheUtils::putUser)
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
