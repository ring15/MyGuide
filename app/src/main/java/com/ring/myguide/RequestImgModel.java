package com.ring.myguide;

import android.util.Log;

import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ring on 2019/11/28.
 */
public class RequestImgModel {

    private static final String TAG = "RequestImgModel";

    public void requestImg(String photoPath, String savePath, String photoName, CallbackListener<String> listener) {
        RetrofitService service = RetrofitUtil.getInstance().createService(RetrofitService.class);
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put("photo_path", photoPath);
        service.getImg(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(responseBody -> {
                    File file = new File(savePath, photoName);
                    byte[] bytes = responseBody.bytes();
                    Log.e(TAG, responseBody.contentLength() + "");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.close();
                    return file.getPath();
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
                        Log.i(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
