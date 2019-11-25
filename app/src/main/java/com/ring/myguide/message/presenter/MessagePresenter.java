package com.ring.myguide.message.presenter;

import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.message.model.MessageModel;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/19.
 */
public class MessagePresenter extends MessageContract.Presenter {

    private MessageContract.Model mModel;

    public MessagePresenter() {
        mModel = new MessageModel();
    }

    @Override
    public void init() {
        User user = mModel.getUserFromCache();
        if (isViewAttached()) {
            if (user != null && user.getUid() != null) {
                mView.get().setUser(user);
            } else {
                mView.get().setNoUser();
            }
        }
    }

    @Override
    public void getMessageList() {
        LinkedList<MessageList> messageLists = mModel.getMessageList();
        if (isViewAttached()) {
            if (messageLists != null && messageLists.size() > 0) {
                mView.get().setMessageList(messageLists);
            } else {
                mView.get().setNoMessageList();
            }
        }
    }

}
