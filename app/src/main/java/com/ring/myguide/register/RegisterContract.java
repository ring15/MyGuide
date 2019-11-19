package com.ring.myguide.register;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

/**
 * Created by ring on 2019/11/19.
 */
public interface RegisterContract {

    interface Model extends BaseModel {
        void requestRegister(String userName, String password, CallbackListener<User> listener);
    }

    interface View extends BaseView {
        void registerSuccess();

        void registerFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void doRegister(String userName, String password, String passwordAgain);
    }

}
