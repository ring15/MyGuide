package com.ring.myguide.login.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.login.LoginContract;
import com.ring.myguide.login.model.LoginModel;

/**
 * Created by ring on 2019/11/19.
 */
public class LoginPresenter extends LoginContract.Presenter {

    private static final String TAG = "LoginPresenter";

    private LoginContract.Model mModel;

    public LoginPresenter() {
        mModel = new LoginModel();
    }

    @Override
    public void doLogin(String userName, String password) {
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
        mModel.requestLogin(userName, password, new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (isViewAttached()) {
                    if (data != null) {
                        mView.get().loginSuccess(data);
                        if (data.getBadge() == 1) {
                            mView.get().showToast(R.string.login_manager);
                        }
                    }
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
}
