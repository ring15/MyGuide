package com.ring.myguide.register.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ring.myguide.R;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;
import com.ring.myguide.register.RegisterContract;
import com.ring.myguide.register.model.RegisterModel;

/**
 * Created by ring on 2019/11/19.
 */
public class RegisterPresenter extends RegisterContract.Presenter {

    private static final String TAG = "RegisterPresenter";

    private RegisterContract.Model mModel;

    public RegisterPresenter() {
        mModel = new RegisterModel();
    }

    @Override
    public void doRegister(String userName, String password, String passwordAgain) {
        if (TextUtils.isEmpty(userName)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.register_username_not_null);
            }
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordAgain)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.register_password_not_null);
            }
            return;
        }
        if (!password.equals(passwordAgain)) {
            if (isViewAttached()) {
                mView.get().showToast(R.string.register_password_not_match);
            }
            return;
        }
        mModel.requestRegister(userName, password, new CallbackListener<User>() {
            @Override
            public void onSuccess(User data) {
                if (isViewAttached()) {
                    if (data != null) {
                        mView.get().registerSuccess();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    mView.get().registerFailed();
                    mView.get().showToast(throwable.getMessage());
                    Log.i(TAG, throwable.getMessage());
                }
            }
        });
    }
}
