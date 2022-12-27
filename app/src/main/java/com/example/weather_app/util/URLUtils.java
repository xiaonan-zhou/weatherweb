package com.example.weather_app.util;

public class URLUtils {

    public static final  String KEY = "f2752380d662c5acc08591f58f9a6d9e";
    public static String temp_url = "http://apis.juhe.cn/simpleWeather/query";

    public static String index_url = "http://apis.juhe.cn/simpleWeather/life";

    public static String ak = "cURwPV10ZgcsPFleSuOrGhlgqW6Fc1mv";

    public static String location_url1 = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=";
    public static String location_url2 = "&output=json&coordtype=wgs84ll&location=";

    public static String getTemp_url(String city){
        String url = temp_url+"?city="+city+"&key="+KEY;
        return url;
    }

    public static String getIndex_url(String city){
        String url = index_url+"?city="+city+"&key="+KEY;
        return url;
    }

    public static String getlocation_url(String location){
        String url = location_url1+ak+location_url2+location;
        return url;
    }
}
