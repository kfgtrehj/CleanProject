package com.norton.mvppro.myapplication;

import io.reactivex.Observable;
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
    public interface ApiStores {
        @GET("adat/sk/{cityId}.html")
        Observable<WeatherJson> getWeather(@Path("cityId") String cityId);
    }
}