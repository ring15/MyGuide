package com.ring.myguide.post_list;

import android.util.Log;

import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.base.RequestImgListener;

/**
 * Created by ring on 2019/12/12.
 */
public class PostListPresenter extends PostListContract.Presenter {

    private static final String TAG = "PostListPresenter";

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
