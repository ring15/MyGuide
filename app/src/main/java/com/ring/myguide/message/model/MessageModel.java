package com.ring.myguide.message.model;

import com.ring.myguide.entity.User;
import com.ring.myguide.message.MessageContract;
import com.ring.myguide.utils.CacheUtils;

/**
 * Created by ring on 2019/11/19.
 */
public class MessageModel implements MessageContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }
}
