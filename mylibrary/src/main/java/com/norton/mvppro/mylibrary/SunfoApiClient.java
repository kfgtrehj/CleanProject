package com.norton.mvppro.mylibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author Norton
 * @date 2017/4/5.
 * @description
 */

public class SunfoApiClient {

    private Context mcontext;
    private String baseUrl;
    private static Converter.Factory mfactory = GsonConverterFactory.create();
    private Retrofit mRetrofit = null;

    public SunfoApiClient(Context context, String url){
        this(context,url,mfactory);
    }

    public SunfoApiClient(Context context, String url, Converter.Factory factory){
        mcontext = context;
        baseUrl = url;
        mfactory = factory;
        initRetrofit();
    }
    private void initRetrofit() {
        if (mRetrofit==null){
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(mfactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(initOkHttp())
                    .build();
        }
    }

    private OkHttpClient initOkHttp(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheFile = new File(mcontext.getExternalCacheDir(), "sunfocash");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 10);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!isNetworkConnected(mcontext)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (isNetworkConnected(mcontext)) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
                return response;
            }
        };
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
        //添加缓存，无网络时，也能显示数据
        builder.cache(cache).addInterceptor(cacheInterceptor);
        return builder.build();
    }

    public <T> T create(@NonNull Class<T> myclass){
        if (mRetrofit == null){
            throw new NullPointerException("Retrofit is null , Please initialize Retrofit");
        }
        return mRetrofit.create(myclass);
    }

    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
