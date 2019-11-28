package com.ring.myguide.me.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.me.MeContract;
import com.ring.myguide.me.model.MeModel;

/**
 * Created by ring on 2019/11/19.
 */
public class MePresenter extends MeContract.Presenter {

    private static final String TAG = "MePresenter";

    private MeContract.Model mModel;

    public MePresenter() {
        mModel = new MeModel();
    }

    @Override
    public void init() {
        User user = mModel.getUserFromCache();
        if (isViewAttached()) {
            if (user != null && user.getUid() != null) {
                mView.get().setUser(user);
            } else {
                mView.get().setNoUser();
            }
        }
    }

    @Override
    public void getImg(String photoPath, String savePath, String photoName) {
        if (TextUtils.isEmpty(photoPath)) {
            mView.get().getImgFailed();
            return;
        }
        RequestImgModel model = new RequestImgModel();
        model.requestImg(photoPath, savePath, photoName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                if (isViewAttached()) {
                    mView.get().getImgSuccess(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().getImgFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.e(TAG, throwable.getMessage());
                }
            }
        });
    }
}
