package com.ring.myguide.query_post.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.Post;
import com.ring.myguide.query_post.QueryPostContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/12/17.
 */
public class QueryPostModel implements QueryPostContract.Model {

    private static final String TAG = "QueryPostModel";

    @Override
    public void putKeywordsIntoCache(LinkedList<String> keywords) {
        CacheUtils.putQueryPost(keywords);
    }

    @Override
    public LinkedList<String> getKeywordsFromCache() {
        return CacheUtils.getQueryPost();
    }

    @Override
    public void requestPost(String keyword, CallbackListener<List<Post>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        if (CacheUtils.getUser() != null && CacheUtils.getUser().getUserName() != null) {
            params.put("username", CacheUtils.getUser().getUserName());
        }
        params.put("keyword", keyword);
        service.searchPost(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<Post> posts = new ArrayList<>();
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
