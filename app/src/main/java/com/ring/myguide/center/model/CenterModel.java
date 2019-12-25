package com.ring.myguide.center.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.center.CenterContract;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.data.TransToRequestBody;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.CacheUtils;

import java.io.File;
import java.util.LinkedHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ring on 2019/11/26.
 */
public class CenterModel implements CenterContract.Model {

    private static final String TAG = "CenterModel";

    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public void updateUserAvatar(File file, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, RequestBody> params = new LinkedHashMap<>();
        params.put("username", TransToRequestBody.toRequestBody(CacheUtils.getUser().getUserName()));

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        String descriptionString = "photo";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        service.updateAvatar(params, description, body)
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
                    public void onNext(String url) {
                        listener.onSuccess(url);
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
    public void updateUser(User oldUser, CallbackListener<User> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("sex", oldUser.getSex() + "");
        if (oldUser.getBirthday() != null) {
            params.put("birthday", oldUser.getBirthday());
        }
        if (oldUser.getNickname() != null) {
            params.put("nickname", oldUser.getNickname());
        }
        if (oldUser.getIntroduce() != null) {
            params.put("introduction", oldUser.getIntroduce());
        }
        if (oldUser.getUserImg() != null) {
            params.put("user_img", oldUser.getUserImg());
        }
        params.put("username", oldUser.getUserName());
        service.updateUser(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    User user = null;
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject data = JsonParse.parseString(result);
                    user = JSON.toJavaObject(data, User.class);
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
    public void putUser(User user) {
        CacheUtils.putUser(user);
    }
}
