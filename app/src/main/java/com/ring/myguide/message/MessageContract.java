package com.ring.myguide.message;

import com.hyphenate.chat.EMMessage;
import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/11/19.
 */
public interface MessageContract {
    interface Model extends BaseModel {

        void queryUser(String queryUserName, CallbackListener<User> listener);

        User getUserFromCache();

        LinkedList<MessageList> getMessageList();

        void putMessageList(LinkedList<MessageList> messageLists);
    }

    interface View extends BaseView {
        void setUser(User user);

        void setNoUser();

        void setMessageList(LinkedList<MessageList> messageList);

        void setNoMessageList();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init();

        public abstract void getMessageList();

        public abstract void updateMessageList(List<EMMessage> messages);

        public abstract void deleteMessageList();
    }
}
