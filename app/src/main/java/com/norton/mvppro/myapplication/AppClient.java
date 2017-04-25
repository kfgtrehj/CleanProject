package com.norton.mvppro.myapplication;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author Norton
 * @date 2017/4/5.
 * @description
 */

public class AppClient {
    static Retrofit mRetrofit;
    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl("http://www.weather.com.cn/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }
    public interface ApiStores {
        @GET("adat/sk/{cityId}.html")
        Observable<WeatherJson> getWeather(@Path("cityId") String cityId);
    }
}