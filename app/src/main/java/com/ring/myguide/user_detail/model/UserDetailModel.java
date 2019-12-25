package com.ring.myguide.user_detail.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.OtherUser;
import com.ring.myguide.entity.User;
import com.ring.myguide.user_detail.UserDetailContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/21.
 */
public class UserDetailModel implements UserDetailContract.Model {

    private static final String TAG = "UserDetailModel";

    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public void queryUser(String queryUserName, CallbackListener<OtherUser> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("query_username", queryUserName);
        service.queryUser(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    OtherUser user = null;
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject data = JsonParse.parseString(result);
                    user = JSON.toJavaObject(data, OtherUser.class);
                    return user;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OtherUser>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OtherUser user) {
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
    public void addFriend(String friendUserName, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("friend", friendUserName);
        service.addFriend(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getString("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        listener.onSuccess(string);
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
    public void deleteFriend(String friendUserName, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("friend", friendUserName);
        service.deleteFriend(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getString("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        listener.onSuccess(string);
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
    public void addBlack(String blackUserName, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("black", blackUserName);
        service.addBlack(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getString("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        listener.onSuccess(string);
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
    public void deleteBlack(String blackUserName, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("black", blackUserName);
        service.deleteBlack(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getString("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String string) {
                        listener.onSuccess(string);
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
