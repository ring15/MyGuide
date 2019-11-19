package com.ring.myguide.utils;

import android.content.Context;
import android.util.Log;

import com.ring.myguide.entity.User;

/**
 * Created by ring on 2019/11/18.
 */
public class CacheUtils {
    private final static String TAG = "CacheUtils";

    private final static String CACHE_USER = "user";                //最近登录user

    private static ACache mAppCache;

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        mAppCache = ACache.get(applicationContext);
    }

    public static void putUser(User user) {
        if (mAppCache != null) {
            mAppCache.put(CACHE_USER, user);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    public static User getUser() {
        User user = null;
        if (mAppCache != null) {
            user = (User) mAppCache.getAsObject(CACHE_USER);
        } else {
            Log.e(TAG, "Cache is null");
        }
        return user;
    }

}
