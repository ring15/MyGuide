package com.ring.myguide.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ring.myguide.entity.MessageList;
import com.ring.myguide.entity.User;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ring on 2019/11/18.
 */
public class CacheUtils {
    private final static String TAG = "CacheUtils";

    private final static String CACHE_USER = "user";                //最近登录user
    private final static String QUERY_USER = "query_user";                //用户查询信息
    private final static String QUERY_POST = "query_post";                //帖子查询信息
    private final static String LOCATION = "location";                //用户位置信息

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
            Log.e(TAG, "getQueryUser解析失败");
        }
        return username;
    }

    /**
     * 缓存帖子查询信息
     *
     * @param keywords
     */
    public static void putQueryPost(LinkedList<String> keywords) {
        String jsonStr = JSON.toJSONString(keywords);
        if (mAppCache != null) {
            mAppCache.put(QUERY_POST, jsonStr);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    /**
     * 查询缓存的帖子查询信息
     *
     * @return
     */
    public static LinkedList<String> getQueryPost() {
        LinkedList<String> keywords = null;
        String jsonStr;
        try {
            jsonStr = mAppCache.getAsString(QUERY_POST);
            List<String> list = JSON.parseArray(jsonStr, String.class);
            if (list != null) {
                keywords = new LinkedList<>(list);
            }
        } catch (Exception e) {
            Log.e(TAG, "getQueryPost解析失败");
        }
        return keywords;
    }

    /**
     * 缓存消息列表
     *
     * @param messageLists
     * @param userName
     */
    public static void putMessageLists(LinkedList<MessageList> messageLists, String userName) {
        String jsonStr = JSON.toJSONString(messageLists);
        if (mAppCache != null) {
            mAppCache.put(userName, jsonStr);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    /**
     * 获取消息列表信息
     *
     * @param userName
     * @return
     */
    public static LinkedList<MessageList> getMessageLists(String userName) {
        LinkedList<MessageList> messageLists = null;
        String jsonStr;
        try {
            jsonStr = mAppCache.getAsString(userName);
            List<MessageList> list = JSON.parseArray(jsonStr, MessageList.class);
            if (list != null) {
                messageLists = new LinkedList<>(list);
            }
        } catch (Exception e) {
            Log.e(TAG, "getMessageLists解析失败");
        }
        return messageLists;
    }

    /**
     * 缓存位置信息
     *
     * @param city
     */
    public static void putLocation(String city) {
        if (mAppCache != null) {
            mAppCache.put(LOCATION, city);
        } else {
            Log.e(TAG, "Cache is null");
        }
    }

    /**
     * 获取位置信息
     *
     * @return
     */
    public static String getLocation() {
        String city = null;
        if (mAppCache != null) {
            city = mAppCache.getAsString(LOCATION);
        } else {
            Log.e(TAG, "Cache is null");
        }
        return city;
    }
}
