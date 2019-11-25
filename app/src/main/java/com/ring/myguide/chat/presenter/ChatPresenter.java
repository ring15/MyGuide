package com.ring.myguide.chat.presenter;

import android.text.TextUtils;

import com.ring.myguide.R;
import com.ring.myguide.chat.ChatContract;
import com.ring.myguide.chat.model.ChatModel;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/21.
 */
public class ChatPresenter extends ChatContract.Presenter {

    private ChatContract.Model mModel;

    public ChatPresenter() {
        mModel = new ChatModel();
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
    public void sendMessage(String content) {
        if (isViewAttached()) {
            if (TextUtils.isEmpty(content)) {
                mView.get().showToast(R.string.chat_message_not_empty);
                return;
            }
            mView.get().sendTextMessage(content);
        }
    }

    @Override
    public void updateMessageList(User user, String content, long time) {
        if (isViewAttached()) {
            MessageList messageList = new MessageList();
            messageList.setUser(user);
            messageList.setContent(content);
            messageList.setTime(time);
            LinkedList<MessageList> messageLists = mModel.getMessageList();
            if (messageLists == null) {
                messageLists = new LinkedList<>();
                messageLists.add(messageList);
                mModel.putMessageList(messageLists);
            } else {
                LinkedList<MessageList> newMessageLists = new LinkedList<>(messageLists);
                for (MessageList list : newMessageLists) {
                    if (list.getUser().getUid().equals(messageList.getUser().getUid())) {
                        messageLists.remove(list);
                    }
                }
                messageLists.add(0, messageList);
                mModel.putMessageList(messageLists);
            }
        }
    }
}
