package com.example.weather_app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//SQLiteOpenHelper：继承该类，重写数据库创建以及更新的方法， 我们还可以通过该类的对象获得数据库实例，或者关闭数据库！
public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context){
        super(context,"forecast.db",null,1);
    }

    /**
     * 数据库第一次被创建时执行的方法
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
//        创建表的操作
        String sql = "create table info(_id integer primary key autoincrement,city varchar(20) unique not null,content text not null,lifeindex text not null)";
        db.execSQL(sql);
    }

    /**
     * 数据库版本更新时执行的方法
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
