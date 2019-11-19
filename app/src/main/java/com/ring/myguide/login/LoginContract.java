package com.ring.myguide.login;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

/**
 * Created by ring on 2019/11/19.
 */
public interface LoginContract {

    interface Model extends BaseModel {
        void requestLogin(String userName, String password, CallbackListener<User> listener);
    }

    interface View extends BaseView {
        void loginSuccess(User user);

        void loginFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void doLogin(String userName, String password);
    }

}
