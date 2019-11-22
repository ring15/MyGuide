package com.ring.myguide.chat;

import com.ring.myguide.base.BaseModel;
import com.ring.myguide.base.BasePresenter;
import com.ring.myguide.base.BaseView;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/21.
 */
public interface ChatContract {
    interface Model extends BaseModel {
        User getUserFromCache();

        LinkedList<MessageList> getMessageList();

        void putMessageList(LinkedList<MessageList> messageLists);
    }

    interface View extends BaseView {
        void setUser(User user);

        void setNoUser();

        void sendTextMessage(String content);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void init();

        public abstract void sendMessage(String content);

        public abstract void updateMessageList(User user, String content, long time);
    }
}
