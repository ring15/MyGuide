package com.ring.myguide.send_post.model;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.data.TransToRequestBody;
import com.ring.myguide.entity.User;
import com.ring.myguide.send_post.SendPostContract;
import com.ring.myguide.utils.CacheUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by ring on 2019/12/3.
 */
public class SendPostModel implements SendPostContract.Model {

    private static final String TAG = "SendPostModel";

    @Override
    public void addPost(String title, String content, List<String> path, String city, int isBoutique, int type, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("title", title);
        if (!TextUtils.isEmpty(content)) {
            params.put("content", content);
        }
        if (path != null && path.size() > 0) {
            params.put("imgs", JSON.toJSONString(path));
        }
        params.put("isBoutique", isBoutique + "");
        params.put("type", type + "");
        params.put("city", city);
        service.addPost(params)
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
                    public void onNext(String s) {
                        listener.onSuccess(s);
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
    public void upload(List<File> files, CallbackListener<List<String>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, RequestBody> params = new LinkedHashMap<>();
        params.put("username", TransToRequestBody.toRequestBody(CacheUtils.getUser().getUserName()));
        List<MultipartBody.Part> bodys = new ArrayList<>();
        for (File file : files) {
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
            bodys.add(body);
        }
        String descriptionString = "photo";
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        service.updatePhoto(params, description, bodys)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject data = JsonParse.parseString(result);
                    List<String> list = new ArrayList<>();
                    JSONArray paths = data.getJSONArray("list");
                    for (int i = 0; i < paths.size(); i++) {
                        String s = paths.getString(i);
                        list.add(s);
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<String> s) {
                        listener.onSuccess(s);
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
    public String getCityFromCache() {
        return CacheUtils.getLocation();
    }
}
