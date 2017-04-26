package com.norton.mvppro.myapplication;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 *
 * @author Norton
 * @date 2017/4/26.
 * @description
 */

public interface ApiStores {
     String BASEURL = "http://www.weather.com.cn/";

    @GET("adat/sk/{cityId}.html")
    Observable<WeatherJson> getWeather(@Path("cityId") String cityId);

    @GET
    Observable<ResponseBody> loadString(@Url String url);

    @GET
    @Streaming
    Observable<ResponseBody> download(@Url String url);
}
