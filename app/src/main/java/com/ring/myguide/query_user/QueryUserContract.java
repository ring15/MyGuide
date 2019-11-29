package com.ring.myguide.query_user;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/20.
 */
public interface QueryUserContract {

    interface Model extends BaseModel {
        void putUserNameIntoCache(LinkedList<String> username);

        LinkedList<String> getUserNameFromCache();

        void requestUser(String username, CallbackListener<User> listener);
    }

    interface View extends BaseView {
        void setUserName(LinkedList<String> userName);

        void setNoUser();

        void setUser(User user);

        void getUserFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getUser(String username, String savePath);

        public abstract void getUserName();

        public abstract void clearHistory();
    }

}
