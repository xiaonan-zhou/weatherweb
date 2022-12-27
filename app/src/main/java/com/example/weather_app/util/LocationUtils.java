package com.example.weather_app.util;


import androidx.appcompat.app.AppCompatActivity;
import com.example.weather_app.bean.LocationDataBean;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LocationUtils extends AppCompatActivity implements Callback.CommonCallback<String> {
    LocationDataBean.ResultBean.AddressComponentBean locationBean;
    String locationCity;
//    public String getLocatonData(String url){
//        loadData(url);
//        return locationCity;
//    }
    public String getLocatonData(String url){
        RequestParams params = new RequestParams(url);
        x.http().get(params,this);
        return locationCity;
    }
    @Override
    public void onSuccess(String result) {
        LocationDataBean  locationBean = new Gson().fromJson(result, LocationDataBean.class);
        LocationDataBean.ResultBean resultBean = locationBean.getResult();
        LocationDataBean.ResultBean.AddressComponentBean addressComponentBean = resultBean.getAddressComponent();
        locationCity= addressComponentBean.getCity();
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        System.out.println("错误");
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }

}
