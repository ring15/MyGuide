package com.ring.myguide.friends_list;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.User;

import java.util.List;

/**
 * Created by ring on 2019/11/20.
 */
public interface FriendsContract {

    interface Model extends BaseModel {
        void requestFriends(CallbackListener<List<User>> listener);
    }

    interface View extends BaseView {
        void setFriendsList(List<User> friendsList);

        void getFriendsFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getFriends();
    }

}
