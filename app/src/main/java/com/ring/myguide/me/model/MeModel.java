package com.ring.myguide.me.model;

import com.ring.myguide.entity.User;
import com.ring.myguide.me.MeContract;
import com.ring.myguide.utils.CacheUtils;

/**
 * Created by ring on 2019/11/19.
 */
public class MeModel implements MeContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }
}
