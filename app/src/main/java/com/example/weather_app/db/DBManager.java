package com.example.weather_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weather_app.bean.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理
 */
public class DBManager {
//    SQLiteDatabase：数据库访问类：我们可以通过该类的对象来对数据库做一些增删改查的操作
    public static SQLiteDatabase database;

    /**
     * 初始化数据库信息
     * @param context
     */
    public static void initDB(Context context){
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 查找数据库当中城市列表
     * @return
     */
    public static List<String>queryAllCityName(){
        Cursor cursor = database.query("info", null, null, null, null, null,null);
        List<String>cityList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cityList.add(city);
        }
        return cityList;
    }

    /**
     * 根据城市名称，替换信息内容
     * @param city
     * @param content
     * @return
     */
    public static int updateInfoByCity(String city,String content){
        ContentValues values = new ContentValues();
        values.put("content",content);
        return database.update("info",values,"city=?",new String[]{city});
    }

    /**
     * 根据城市名称，替换信息内容
     * @param city
     * @param lifeindex
     * @return
     */
    public static int updatelifeindexByCity(String city,String lifeindex){
        ContentValues values = new ContentValues();
        values.put("lifeindex",lifeindex);
        return database.update("info",values,"city=?",new String[]{city});
    }
    public static int update(String city){
        ContentValues values = new ContentValues();
        values.put("lifeindex","{\n" +
                "    \"reason\": \"查询成功!\",\n" +
                "    \"result\": {\n" +
                "        \"city\": \"桂林\",\n" +
                "        \"life\": {\n" +
                "            \"kongtiao\": {/*空调开启*/\n" +
                "                \"v\": \"开启制暖空调\",/*指数*/\n" +
                "                \"des\": \"您将感到有些冷，可以适当开启制暖空调调节室内温度，以免着凉感冒。\"/*指数详情*/\n" +
                "            },\n" +
                "            \"guomin\": {/*过敏*/\n" +
                "                \"v\": \"极不易发\",\n" +
                "                \"des\": \"天气条件极不易诱发过敏，可放心外出，享受生活。\"\n" +
                "            },\n" +
                "            \"shushidu\": {/*舒适度*/\n" +
                "                \"v\": \"较不舒适\",\n" +
                "                \"des\": \"白天天气晴好，但仍会使您感觉偏冷，不很舒适，请注意适时添加衣物，以防感冒。\"\n" +
                "            },\n" +
                "            \"chuanyi\": {/*穿衣*/\n" +
                "                \"v\": \"冷\",\n" +
                "                \"des\": \"天气冷，建议着棉服、羽绒服、皮夹克加羊毛衫等冬季服装。年老体弱者宜着厚棉衣、冬大衣或厚羽绒服。\"\n" +
                "            },\n" +
                "            \"diaoyu\": {/*钓鱼*/\n" +
                "                \"v\": \"不宜\",\n" +
                "                \"des\": \"天气冷，不适合垂钓。\"\n" +
                "            },\n" +
                "            \"ganmao\": {/*感冒*/\n" +
                "                \"v\": \"较易发\",\n" +
                "                \"des\": \"昼夜温差较大，较易发生感冒，请适当增减衣服。体质较弱的朋友请注意防护。\"\n" +
                "            },\n" +
                "            \"ziwaixian\": {/*紫外线*/\n" +
                "                \"v\": \"弱\",\n" +
                "                \"des\": \"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。\"\n" +
                "            },\n" +
                "            \"xiche\": {/*洗车*/\n" +
                "                \"v\": \"较适宜\",\n" +
                "                \"des\": \"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。\"\n" +
                "            },\n" +
                "            \"yundong\": {/*运动*/\n" +
                "                \"v\": \"较不宜\",\n" +
                "                \"des\": \"天气较好，但考虑天气寒冷，推荐您进行室内运动，户外运动时请注意保暖并做好准备活动。\"\n" +
                "            },\n" +
                "            \"daisan\": {/*带伞*/\n" +
                "                \"v\": \"不带伞\",\n" +
                "                \"des\": \"天气较好，您在出门的时候无须带雨伞。\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"error_code\": 0\n" +
                "}");
        return database.update("info",values,"city=?",new String[]{city});
    }
    /**
     * 新增一条城市记录
     * @param city
     * @param content
     * @return
     */
    public static long addCityInfo(String city,String content){
        ContentValues values = new ContentValues();
        values.put("city",city);
        values.put("content",content);
        values.put("lifeindex","null");
        return database.insert("info",null,values);
    }

    /**
     *  根据城市名，查询数据库当中的内容
     * @param city
     * @return
     */
    public static String queryInfoByCity(String city){
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String content = cursor.getString(cursor.getColumnIndex("content"));
            return content;
        }
        return null;
    }

    /**
     *  根据城市名，查询数据库当中的内容
     * @param city
     * @return
     */
    public static String querylifeindexByCity(String city){
        Cursor cursor = database.query("info", null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String lifeindex = cursor.getString(cursor.getColumnIndex("lifeindex"));
            return lifeindex;
        }
        return null;
    }

    /**
     * 存储城市天气要求最多存储5个城市的信息，一旦超过5个城市就不能存储了，获取目前已经存储的数量
     * @return
     */
    public static int getCityCount(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        int count = cursor.getCount();
        return count;
    }

    /**
     * 查询数据库当中的全部信息
     * @return
     */
    public static List<DatabaseBean>queryAllInfo(){
        Cursor cursor = database.query("info", null, null, null, null, null, null);
        List<DatabaseBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String lifeindex = cursor.getString(cursor.getColumnIndex("lifeindex"));
            DatabaseBean bean = new DatabaseBean(id, city, content,lifeindex);
            list.add(bean);
        }
        return list;
    }

    /**
     * 根据城市名称，删除这个城市在数据库当中的数据
     * @param city
     * @return
     */
    public static int deleteInfoByCity(String city){
        return database.delete("info","city=?",new String[]{city});
    }

    /**
     * 删除表当中所有的数据信息
     */
    public static void deleteAllInfo(){
        String sql = "delete from info";
        database.execSQL(sql);
    }
}
