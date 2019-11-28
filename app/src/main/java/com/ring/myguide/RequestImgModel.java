package com.ring.myguide;

import android.util.Log;

import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.data.RetrofitService;
import com.ring.myguide.data.RetrofitUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                .map(responseBody -> {
                    File file = new File(savePath, photoName);
                    try {
                        InputStream inputStream = responseBody.byteStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inputStream.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                        }
                        bos.close();
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(bos.toByteArray());
                        fos.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
