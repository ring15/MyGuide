package com.ring.myguide.me.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.Post;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;
import com.ring.myguide.me.MeContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/19.
 */
public class MeModel implements MeContract.Model {

    private static final String TAG = "MeModel";

    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public void requestMyPost(CallbackListener<List<Post>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        service.requestMyPost(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<Post> posts = new ArrayList<>();
                    try {
                        String result = responseBody.string();
                        Log.i(TAG, result);
                        JSONObject data = JsonParse.parseString(result);
                        JSONArray replyArray = data.getJSONArray("list");
                        for (int i = 0; i < replyArray.size(); i++) {
                            String string = replyArray.getString(i);
                            JSONObject object = JSON.parseObject(string);
                            Post post = JSON.toJavaObject(object, Post.class);
                            posts.add(post);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return posts;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        listener.onSuccess(posts);
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
    public void requestMyFavorite(CallbackListener<List<Post>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        service.requestFavotitePost(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<Post> posts = new ArrayList<>();
                    try {
                        String result = responseBody.string();
                        Log.i(TAG, result);
                        JSONObject data = JsonParse.parseString(result);
                        JSONArray replyArray = data.getJSONArray("list");
                        for (int i = 0; i < replyArray.size(); i++) {
                            String string = replyArray.getString(i);
                            JSONObject object = JSON.parseObject(string);
                            Post post = JSON.toJavaObject(object, Post.class);
                            posts.add(post);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return posts;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        listener.onSuccess(posts);
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
    public void requestMyLike(CallbackListener<List<Post>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        service.requestLikePost(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<Post> posts = new ArrayList<>();
                    try {
                        String result = responseBody.string();
                        Log.i(TAG, result);
                        JSONObject data = JsonParse.parseString(result);
                        JSONArray replyArray = data.getJSONArray("list");
                        for (int i = 0; i < replyArray.size(); i++) {
                            String string = replyArray.getString(i);
                            JSONObject object = JSON.parseObject(string);
                            Post post = JSON.toJavaObject(object, Post.class);
                            posts.add(post);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return posts;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Post>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        listener.onSuccess(posts);
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
