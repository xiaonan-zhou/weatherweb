package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import com.example.weather_app.bean.LocationDataBean;
import com.example.weather_app.city_manager.CityManagerActivity;
import com.example.weather_app.city_manager.SearchCityActivity;
import com.example.weather_app.db.DBManager;
import com.example.weather_app.util.LocationUtils;
import com.example.weather_app.util.URLUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView addCityIv,moreIv;
    TextView locationCity;
    LinearLayout pointLayout;
    RelativeLayout outLayout;
    ViewPager mainVp;
    //    ViewPager的数据源
    List<Fragment>fragmentList;
    //    表示需要显示的城市的集合
    List<String>cityList;
    //    表示ViewPager的页数指数器显示集合
    List<ImageView>imgList;
    private CityFragmentPagerAdapter adapter;
    private SharedPreferences pref;
    private int bgNum;
  //  private static Context context;
    //定位都要通过LocationManager这个类实现
    private static String provider;
    private LocationManager locationManager;

    private void getLocation() {
        final LocationListener locationListener = new LocationListener() {
            //位置发生改变后调用
            public void onLocationChanged(Location location) {
                //更新当前设备的新位置信息
                // showLocation(location);
            }
            //provider 被用户关闭后调用
            public void onProviderDisabled(String provider) {
            }
            //provider 被用户开启后调用
            public void onProviderEnabled(String provider) {
            }
            //provider 状态变化时调用
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        String string ="";
        //获取位置信息
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
            Toast.makeText(this, "请检查网络或GPS是否打开",
                    Toast.LENGTH_LONG).show();
        }
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            while(lastKnownLocation  == null)
            {
                locationManager.requestLocationUpdates("gps", 60000, 1, locationListener);
            }
            //获取当前位置，这里只用到了经纬度
            string = lastKnownLocation.getLatitude() + ","
                    + lastKnownLocation.getLongitude();
        }
//        获取当前所在城市
        LocationDataBean.ResultBean.AddressComponentBean locationBean;
        RequestParams params = new RequestParams(URLUtils.getlocation_url(string));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LocationDataBean locationBean = new Gson().fromJson(result, LocationDataBean.class);
                LocationDataBean.ResultBean resultBean = locationBean.getResult();
                if (resultBean != null) {
                locationCity.setText(resultBean.getFormatted_address());
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        locationCity =findViewById(R.id.location_tv);
        pointLayout = findViewById(R.id.main_layout_point);
        outLayout = findViewById(R.id.main_out_layout);
        exchangeBg();
        mainVp = findViewById(R.id.main_vp);
//        添加点击事件
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        locationCity.setOnClickListener(this);
        fragmentList = new ArrayList<>();
        cityList = DBManager.queryAllCityName();  //获取数据库包含的城市信息列表
        imgList = new ArrayList<>();
        if(cityList.size()==0){
            cityList.add("北京");
        }
        /* 因为可能搜索界面点击跳转此界面，会传值，所以此处获取一下*/
        try {
            Intent intent = getIntent();
            String city = intent.getStringExtra("city");
            if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
                cityList.add(city);
            }
        }catch (Exception e){
            Log.i("animee","程序出现问题了！！");
        }
//        初始化ViewPager页面的方法
        initPager();
        adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        mainVp.setAdapter(adapter);
//        创建小圆点指示器
        initPoint();
//        设置最后一个城市信息
        mainVp.setCurrentItem(0);
//        设置ViewPager页面监听
        setPagerListener();
        getLocation();
    }


    //        换壁纸的函数
    public void exchangeBg(){
        pref = getSharedPreferences("bg_pref", MODE_PRIVATE);
        bgNum = pref.getInt("bg", 2);
        switch (bgNum) {
            case 0:
                outLayout.setBackgroundResource(R.mipmap.night);
                break;
            case 1:
                outLayout.setBackgroundResource(R.mipmap.sky);
                break;
            case 2:
                outLayout.setBackgroundResource(R.mipmap.tree);
                break;
        }

    }
    private void setPagerListener() {
        /* 设置监听事件*/
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.mipmap.a1);
                }
                imgList.get(position).setImageResource(R.mipmap.a2);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initPoint() {
//        创建小圆点 ViewPager页面指示器的函数
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv = new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        imgList.get(0).setImageResource(R.mipmap.a2);


    }

    private void initPager() {
        /* 创建Fragment对象，添加到ViewPager数据源当中*/
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cwFrag = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cwFrag.setArguments(bundle);
            fragmentList.add(cwFrag);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.main_iv_add:
                intent.setClass(this,CityManagerActivity.class);
                break;
            case R.id.main_iv_more:
                intent.setClass(this,MoreActivity.class);
                break;
            case R.id.location_tv:
                Bundle bundle = new Bundle();
                bundle.putString("locationcity",(String) locationCity.getText());
                intent.putExtras(bundle);
                intent.setClass(this, SearchCityActivity.class);
                break;
        }
        startActivity(intent);
    }

    /* 当页面重写加载时会调用的函数，这个函数在页面获取焦点之前进行调用，此处完成ViewPager页数的更新*/
    @Override
    protected void onRestart() {
        super.onRestart();
//        获取数据库当中还剩下的城市集合
        List<String> list = DBManager.queryAllCityName();
        if (list.size()==0) {
            list.add("北京");
        }
        cityList.clear();    //重写加载之前，清空原本数据源
        cityList.addAll(list);
//        剩余城市也要创建对应的fragment页面
        fragmentList.clear();
        initPager();
        adapter.notifyDataSetChanged();
//        页面数量发生改变，指示器的数量也会发生变化，重写设置添加指示器
        imgList.clear();
        pointLayout.removeAllViews();   //将布局当中所有元素全部移除
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
    }

}
