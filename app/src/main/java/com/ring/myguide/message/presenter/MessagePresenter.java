package com.ring.myguide.message.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.ring.myguide.RequestImgModel;
import com.ring.myguide.base.CallbackListener;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.message.model.MessageModel;
import com.ring.myguide.utils.FileUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/11/19.
 */
public class MessagePresenter extends MessageContract.Presenter {

    private MessageContract.Model mModel;
    private String mSavePath;

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

    @Override
    public void updateMessageList(List<EMMessage> messages, String savePath) {
        mSavePath = savePath;
        if (messages != null && messages.size() > 0) {
            EMMessage message = messages.get(messages.size() - 1);
            mModel.queryUser(message.getFrom(), new CallbackListener<User>() {
                @Override
                public void onSuccess(User data) {
                    requestImg(message, data);
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }
    }

    private void requestImg(EMMessage message, User user) {
        if (user.getUserImg() != null) {
            if (user.getUserImgPath() == null || !FileUtils.fileIsExists(user.getUserImgPath())) {

                RequestImgModel model = new RequestImgModel();
                model.requestImg(user.getUserImg(), mSavePath, user.getUserImgPath() + ".jpg", new CallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        user.setUserImgPath(data);
                        setUser(message, user);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        setUser(message, user);
                    }
                });
            } else {
                setUser(message, user);
            }
        } else {
            setUser(message, user);
        }
    }

    private void setUser(EMMessage message, User user) {
        if (isViewAttached()) {
            MessageList messageList = new MessageList();
            messageList.setUser(user);
            messageList.setContent(((EMTextMessageBody) message.getBody()).getMessage());
            messageList.setTime(message.getMsgTime());
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
            getMessageList();
        }
    }

    @Override
    public void deleteMessageList(LinkedList<MessageList> messageLists) {
        if (messageLists != null && messageLists.size() > 0) {
            for (MessageList messageList : messageLists) {
                EMClient.getInstance().chatManager().deleteConversation(messageList.getUser().getUserName(), false);
            }
        }
        mModel.putMessageList(new LinkedList<>());
    }

    @Override
    public void deleteMessage(String username) {
        EMClient.getInstance().chatManager().deleteConversation(username, false);
        LinkedList<MessageList> messageLists = mModel.getMessageList();
        if (messageLists != null) {
            LinkedList<MessageList> newMessageLists = new LinkedList<>(messageLists);
            for (MessageList list : newMessageLists) {
                if (list.getUser().getUserName().equals(username)) {
                    messageLists.remove(list);
                }
            }
            mModel.putMessageList(messageLists);
        }
    }
}
