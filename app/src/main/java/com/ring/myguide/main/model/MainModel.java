package com.ring.myguide.main.model;

import com.ring.myguide.entity.User;
import com.ring.myguide.main.MainContract;
import com.ring.myguide.utils.CacheUtils;

/**
 * Created by ring on 2019/12/5.
 */
public class MainModel implements MainContract.Model {
    @Override
    public User getUserFromCache() {
        return CacheUtils.getUser();
    }

    @Override
    public String getCityFromCache() {
        return CacheUtils.getLocation();
    }

    @Override
    public void putCityIntoCache(String city) {
        CacheUtils.putLocation(city);
    }
}
