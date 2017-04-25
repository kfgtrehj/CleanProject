package com.norton.mvppro.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.norton.mvppro.mylibrary.SunfoApiClient;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SunfoApiClient client = new SunfoApiClient(this,"http://www.weather.com.cn/");
        AppClient.ApiStores apiStores = client.create(AppClient.ApiStores.class);
        Observable<WeatherJson> observable = apiStores.getWeather("101280601");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherJson value) {
                        Toast.makeText(MainActivity.this,value.getWeatherinfo().getCity(),Toast.LENGTH_SHORT).show();
                        Log.i("wxl", "getWeatherinfo=" + value.getWeatherinfo().getCity());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("wxl", "onError=" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
