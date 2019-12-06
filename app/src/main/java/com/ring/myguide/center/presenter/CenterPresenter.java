package com.ring.myguide.center.presenter;

import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.center.CenterContract;
import com.ring.myguide.center.model.CenterModel;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.FileUtils;

import java.io.File;

/**
 * Created by ring on 2019/11/26.
 */
public class CenterPresenter extends CenterContract.Presenter {

    private static final String TAG = "CenterPresenter";

    private CenterContract.Model mModel;
    private File mFile;
    private String mSavePath;

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
    public void changeInfo(User user, String path, String savePath) {
        if (user == null || user.getUserName() == null) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.center_error);
            }
            return;
        }
        mSavePath = savePath;
        CallbackListener<User> listener = new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {
                    getImg(data);
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

    public void getImg(User user) {
        if (user.getUserImg() != null) {
            if (user.getUserImgPaht() == null || !FileUtils.fileIsExists(user.getUserImgPaht())) {
                RequestImgModel model = new RequestImgModel();
                model.requestImg(user.getUserImg(), mSavePath, user.getUserName() + ".jpg", new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        user.setUserImgPaht(data);
                        mModel.putUser(user);
                        if (isViewAttached()) {
                            mView.get().updateSuccess();
                            mView.get().showToast(R.string.center_success);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mModel.putUser(user);
                        if (isViewAttached()) {
                            mView.get().updateSuccess();
                            mView.get().showToast(R.string.center_success);
                        }
                    }
                });
            } else {
                mModel.putUser(user);
                if (isViewAttached()) {
                    mView.get().updateSuccess();
                    mView.get().showToast(R.string.center_success);
                }
            }
        } else {
            mModel.putUser(user);
            if (isViewAttached()) {
                mView.get().updateSuccess();
                mView.get().showToast(R.string.center_success);
            }
        }
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
