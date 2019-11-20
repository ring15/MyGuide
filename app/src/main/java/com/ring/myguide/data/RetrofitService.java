package com.ring.myguide.data;

import com.ring.myguide.config.Constants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by ring on 2019/11/1.
 * 数据请求接口
 */
public interface RetrofitService {

    /**
     * 用户注册接口
     *
     * @param map query数据
     * @return okhttp返回结果
     */
    @GET(Constants.REGISTER_USER)
    Observable<ResponseBody> doRegisterUser(@QueryMap Map<String, String> map);

    /**
     * 用户登录接口
     *
     * @param map query数据
     * @return okhttp返回结果
     */
    @GET(Constants.USER_LOGIN)
    Observable<ResponseBody> doLogin(@QueryMap Map<String, String> map);

    /**
     * 获取好友列表
     *
     * @param map query数据
     * @return okhttp返回结果
     */
    @GET(Constants.GET_FRIENDS)
    Observable<ResponseBody> getFriendsList(@QueryMap Map<String, String> map);

}
