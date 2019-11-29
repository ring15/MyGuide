package com.ring.myguide.center;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

import java.io.File;

/**
 * Created by ring on 2019/11/26.
 */
public interface CenterContract {

    interface Model extends BaseModel {
        User getUserFromCache();

        void putUser(User user);

        void updateUserAvatar(File file, CallbackListener<String> listener);

        void updateUser(User user, CallbackListener<User> listener);
    }

    interface View extends BaseView {
        void setUser(User user);

        void setNoUser();

        void updateSuccess();

        void updateFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getUser();

        public abstract void changeInfo(User user, String path, String savePath);
    }
}
