package com.ring.myguide.post.presenter;

import android.util.Log;

import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.base.RequestImgListener;
import com.ring.myguide.post.PostContract;

/**
 * Created by ring on 2019/12/13.
 */
public class PostPresenter extends PostContract.Presenter {

    private static final String TAG = "PostPresenter";

    @Override
    public void requestImg(String img, String name, String savePath, RequestImgListener listener) {
        RequestImgModel model = new RequestImgModel();
        model.requestImg(img, savePath, name, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                listener.onSuccess();
            }

            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, throwable.getMessage());
                listener.onFailed();
            }
        });
    }
}
