package com.ring.myguide.friends_list.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.User;
import com.ring.myguide.friends_list.FriendsContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/20.
 */
public class FriendsListModel implements FriendsContract.Model {

    private static final String TAG = "FriendsListModel";

    @Override
    public void requestFriends(CallbackListener<List<User>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        service.getFriendsList(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<User> userList = new ArrayList<>();
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject data = JsonParse.parseString(result);
                    JSONArray friendsList = data.getJSONArray("list");
                    for (int i = 0; i < friendsList.size(); i++) {
                        String string = friendsList.getString(i);
                        JSONObject object = JSON.parseObject(string);
                        User user = JSON.toJavaObject(object, User.class);
                        userList.add(user);
                    }
                    return userList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> userList) {
                        listener.onSuccess(userList);
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
