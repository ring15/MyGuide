package com.ring.myguide.center.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.center.CenterContract;
import com.ring.myguide.center.model.CenterModel;
import com.ring.myguide.entity.User;

import java.io.File;

/**
 * Created by ring on 2019/11/26.
 */
public class CenterPresenter extends CenterContract.Presenter {

    private static final String TAG = "CenterPresenter";

    private CenterContract.Model mModel;
    private File mFile;

    public CenterPresenter() {
        mModel = new CenterModel();
    }

    @Override
    public void getUser() {
        User user = mModel.getUserFromCache();
        if (isViewAttached()) {
            if (user != null) {
                mView.get().setUser(user);
            } else {
                mView.get().showToast(R.string.center_not_login);
                mView.get().setNoUser();
            }
        }
    }

    @Override
    public void changeInfo(User user, String path) {
        if (user == null || user.getUserName() == null) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.center_error);
            }
            return;
        }

        CallbackListener<User> listener = new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (isViewAttached()) {
                    mView.get().updateSuccess();
                    mView.get().showToast(R.string.center_success);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().updateFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.e(TAG, throwable.getMessage());
                }
            }
        };
        if (fileIsExists(path)) {
            mModel.updateUserAvatar(mFile, new CallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    user.setUserImg(data);
                    mModel.updateUser(user, listener);
                }

                @Override
                public void onError(Throwable throwable) {
                    if (isViewAttached()) {
                        mView.get().updateFailed();
                        mView.get().showToast(throwable.getMessage());
                        Log.e(TAG, throwable.getMessage());
                    }
                }
            });
        } else {
            mModel.updateUser(user, listener);
        }
    }

    @Override
    public void getImg(String photoPath, String savePath, String photoName) {
        if (TextUtils.isEmpty(photoPath)) {
            mView.get().updateFailed();
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
                    mView.get().updateFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.e(TAG, throwable.getMessage());
                }
            }
        });
    }

    //判断文件是否存在
    private boolean fileIsExists(String strFile) {
        try {
            mFile = new File(strFile);
            if (!mFile.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
