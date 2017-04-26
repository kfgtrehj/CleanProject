package com.norton.mvppro.mylibrary;

import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Field;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author Norton
 * @date 2017/4/26.
 * @description
 */

public class SunfoApiService {
    private Retrofit mRetrofit = null;
    public static SunfoApiService getService() {
        return new SunfoApiService();
    }

    public <T> T creatService(@NonNull Class<T> myclass){
        String url = "";
        try {
            Field field = myclass.getField("BASEURL");
            url = (String) field.get(myclass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initOkHttp())
                .build();
        return mRetrofit.create(myclass);
    }

    private OkHttpClient initOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置cookie
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //最多4个连接，保持20秒
        builder.connectionPool(new ConnectionPool(4, 20, TimeUnit.SECONDS));
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

}
