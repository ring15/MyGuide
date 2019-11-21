package com.ring.myguide.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ring.myguide.entity.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/11/18.
 */
public class CacheUtils {
    private final static String TAG = "CacheUtils";

    private final static String CACHE_USER = "user";                //最近登录user
    private final static String QUERY_USER = "query_user";                //最近登录user

    private static ACache mAppCache;

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        mAppCache = ACache.get(applicationContext);
    }

    /**
     * 缓存用户信息
     *
     * @param user
     */
    public static void putUser(User user) {
        if (mAppCache != null) {
            mAppCache.put(CACHE_USER, user);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    /**
     * 获取缓存的用户信息
     *
     * @return
     */
    public static User getUser() {
        User user = null;
        if (mAppCache != null) {
            user = (User) mAppCache.getAsObject(CACHE_USER);
        } else {
            Log.e(TAG, "Cache is null");
        }
        return user;
    }

    /**
     * 缓存用户查询信息
     *
     * @param username
     */
    public static void putQueryUser(LinkedList<String> username) {
        String jsonStr = JSON.toJSONString(username);
        if (mAppCache != null) {
            mAppCache.put(QUERY_USER, jsonStr);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    /**
     * 查询缓存的用户查询信息
     *
     * @return
     */
    public static LinkedList<String> getQueryUser() {
        LinkedList<String> username = null;
        String jsonStr;
        try {
            jsonStr = mAppCache.getAsString(QUERY_USER);
            List<String> list = JSON.parseArray(jsonStr, String.class);
            if (list != null) {
                username = new LinkedList<>(list);
            }
        } catch (Exception e) {
            Log.e(TAG, "getUserList解析失败");
        }
        return username;
    }
}
