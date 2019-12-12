package com.ring.myguide.data;

import com.ring.myguide.config.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by ring on 2019/11/1.
 * 创建Retrofit方法类
 */
public class RetrofitUtil {

    private static RetrofitUtil retrofitUtil;
    private Retrofit mRetrofit;

    private RetrofitUtil() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new NetInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .callTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.mHost)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        mRetrofit = builder.client(httpClient.build()).build();
    }

    /**
     * 获取RetrofitUtil单一实例
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (retrofitUtil == null) {
            synchronized (RetrofitUtil.class) {
                if (retrofitUtil == null) {
                    retrofitUtil = new RetrofitUtil();
                }
            }
        }
        return retrofitUtil;
    }

    public <S> S createService(Class<S> sClass) {
        return mRetrofit.create(sClass);
    }

}
