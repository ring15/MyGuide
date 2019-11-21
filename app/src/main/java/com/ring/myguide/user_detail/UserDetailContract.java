package com.ring.myguide.user_detail;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.OtherUser;
import com.ring.myguide.entity.User;

/**
 * Created by ring on 2019/11/21.
 */
public interface UserDetailContract {

    interface Model extends BaseModel {
        User getUserFromCache();

        void queryUser(String queryUserName, CallbackListener<OtherUser> listener);

        void addFriend(String friendUserName, CallbackListener<String> listener);

        void deleteFriend(String friendUserName, CallbackListener<String> listener);

        void addBlack(String blackUserName, CallbackListener<String> listener);

        void deleteBlack(String blackUserName, CallbackListener<String> listener);

    }

    interface View extends BaseView {
        void setStranger();

        void setFriends();

        void setFriendsAndBlacks();

        void setBlacks();

        void setOthers();

        void onQueryFailed();

        void onAddFriendSuccess();

        void onDeleteFriendSuccess();

        void onAddBlackSuccess();

        void onDeleteBlackSuccess();

        void onFailed();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init(String queryUserName);

        public abstract void addFriend(String friendUserName);

        public abstract void deleteFriend(String friendUserName);

        public abstract void addBlack(String blackUserName);

        public abstract void deleteBlack(String blackUserName);
    }
}
