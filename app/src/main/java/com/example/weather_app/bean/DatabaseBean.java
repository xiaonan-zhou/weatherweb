package com.example.weather_app.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DatabaseBean {
    private int _id;
    private String city;
//    存储城市相关信息内容
    private String content;
    private String lifeindex;

    public DatabaseBean() {
    }

    public DatabaseBean(int _id, String city, String content,String lifeindex) {
        this._id = _id;
        this.city = city;
        this.content = content;
        this.lifeindex=lifeindex;
    }

//    public int get_id() {
//        return _id;
//    }
//
//    public void set_id(int _id) {
//        this._id = _id;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
}
