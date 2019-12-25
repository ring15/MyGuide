package com.ring.myguide.home.model;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.JsonParse;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;
import com.ring.myguide.entity.HomePage;
import com.ring.myguide.home.HomeContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/12/2.
 */
public class HomeModel implements HomeContract.Model {

    private static final String TAG = "HomeModel";

    @Override
    public void requestHomePage(String province, CallbackListener<HomePage> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        if (CacheUtils.getUser() != null && CacheUtils.getUser().getUserName() != null) {
            params.put("username", CacheUtils.getUser().getUserName());
        }
        params.put("province", province);
        service.getHomePage(params)
                .subscribeOn(Schedulers.newThread())
                .map(responseBody -> {
                    HomePage homePage = null;
                    String result = responseBody.string();
                    Log.i(TAG, result);
                    JSONObject data = JsonParse.parseString(result);
                    homePage = JSON.toJavaObject(data, HomePage.class);
                    return homePage;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomePage>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HomePage homePage) {
                        listener.onSuccess(homePage);
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
    public String getCityFromCache() {
        return CacheUtils.getLocation();
    }
}
