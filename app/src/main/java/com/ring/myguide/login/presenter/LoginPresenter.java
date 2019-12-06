package com.ring.myguide.login.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.login.model.LoginModel;
import com.ring.myguide.utils.FileUtils;

/**
 * Created by ring on 2019/11/19.
 */
public class LoginPresenter extends LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private LoginContract.Model mModel;
    private String mSavePath;

    public LoginPresenter() {
        mModel = new LoginModel();
    }

    @Override
    public void doLogin(String userName, String password, String savePath) {
        if (TextUtils.isEmpty(userName)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.login_username_not_null);
            }
            return;
        }
        if (TextUtils.isEmpty(password)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.login_password_not_null);
            }
            return;
        }
        mSavePath = savePath;
        mModel.requestLogin(userName, password, new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (data != null) {
                    requestImg(data);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().loginFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }

    private void requestImg(User user) {
        if (user.getUserImg() != null) {
            if (user.getUserImgPaht() == null || !FileUtils.fileIsExists(user.getUserImgPaht())) {
                RequestImgModel model = new RequestImgModel();
                model.requestImg(user.getUserImg(), mSavePath, user.getUserName() + ".jpg", new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        user.setUserImgPaht(data);
                        mModel.putUser(user);
                        if (isViewAttached()) {
                            mView.get().loginSuccess(user);
                            if (user.getBadge() == 1) {
                                mView.get().showToast(R.string.login_manager);
                            } else {
                                mView.get().loginChat();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mModel.putUser(user);
                        if (isViewAttached()) {
                            mView.get().loginSuccess(user);
                            if (user.getBadge() == 1) {
                                mView.get().showToast(R.string.login_manager);
                            } else {
                                mView.get().loginChat();
                            }
                        }
                    }
                });
            } else {
                mModel.putUser(user);
                if (isViewAttached()) {
                    mView.get().loginSuccess(user);
                    if (user.getBadge() == 1) {
                        mView.get().showToast(R.string.login_manager);
                    } else {
                        mView.get().loginChat();
                    }
                }
            }
        } else {
            mModel.putUser(user);
            if (isViewAttached()) {
                mView.get().loginSuccess(user);
                if (user.getBadge() == 1) {
                    mView.get().showToast(R.string.login_manager);
                } else {
                    mView.get().loginChat();
                }
            }
        }
    }
}
