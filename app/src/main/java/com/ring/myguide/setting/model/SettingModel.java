package com.ring.myguide.setting.model;

import com.ring.myguide.entity.User;
import com.ring.myguide.setting.SettingContract;
import com.ring.myguide.utils.CacheUtils;

/**
 * Created by ring on 2019/11/19.
 */
public class SettingModel implements SettingContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }
}
