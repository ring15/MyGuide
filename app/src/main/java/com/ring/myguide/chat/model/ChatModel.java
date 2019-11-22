package com.ring.myguide.chat.model;

import com.ring.myguide.chat.ChatContract;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/19.
 */
public class ChatModel implements ChatContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public LinkedList<MessageList> getMessageList() {
        return CacheUtils.getMessageLists();
    }

    @Override
    public void putMessageList(LinkedList<MessageList> messageLists) {
        CacheUtils.putMessageLists(messageLists);
    }
}
