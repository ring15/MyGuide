package com.ring.myguide.message.model;

import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.utils.CacheUtils;

import java.util.LinkedList;

/**
 * Created by ring on 2019/11/19.
 */
public class MessageModel implements MessageContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public LinkedList<MessageList> getMessageList() {
        return CacheUtils.getMessageLists();
    }
}
