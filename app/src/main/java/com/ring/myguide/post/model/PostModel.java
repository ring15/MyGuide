package com.ring.myguide.post.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.Reply;
import com.ring.myguide.entity.User;
import com.ring.myguide.post.PostContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/12/13.
 */
public class PostModel implements PostContract.Model {

    private static final String TAG = "PostModel";

    @Override
    public void doSendReply(String reply, int threadId, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("reply", reply);
        params.put("thread_id", threadId + "");
        service.uploadReply(params)
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
    public void requestReply(int threadId, CallbackListener<List<Reply>> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("thread_id", threadId + "");
        service.getReply(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    List<Reply> replies = new ArrayList<>();
                    try {
                        String result = responseBody.string();
                        Log.i(TAG, result);
                        JSONObject data = JsonParse.parseString(result);
                        JSONArray replyArray = data.getJSONArray("list");
                        for (int i = 0; i < replyArray.size(); i++) {
                            String string = replyArray.getString(i);
                            JSONObject object = JSON.parseObject(string);
                            Reply reply = JSON.toJavaObject(object, Reply.class);
                            replies.add(reply);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return replies;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Reply>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Reply> replies) {
                        listener.onSuccess(replies);
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
    public void doFavorite(int threadId, CallbackListener<Boolean> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("thread_id", threadId + "");
        service.doFavorite(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getBoolean("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
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
    public void doGood(int threadId, CallbackListener<Boolean> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("username", CacheUtils.getUser().getUserName());
        params.put("thread_id", threadId + "");
        service.doGood(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject root = JSON.parseObject(result);
                    String status = root.getString("status");
                    if (status.equals("failed")) {
                        throw new Exception(root.getString("data"));
                    } else {
                        return root.getBoolean("data");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean s) {
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
    public void doChangeBoutique(int threadId, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("thread_id", threadId + "");
        service.doChangeBoutique(params)
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
    public void doChangeType(int threadId, int type, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("thread_id", threadId + "");
        params.put("type", type + "");
        service.doChangeType(params)
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
    public void doChangeDelete(int threadId, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("thread_id", threadId + "");
        service.doChangeDelete(params)
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
    public void deleteReply(int threadId, int floor, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("thread_id", threadId + "");
        params.put("floor", floor + "");
        service.doDeleteReply(params)
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
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }
}
