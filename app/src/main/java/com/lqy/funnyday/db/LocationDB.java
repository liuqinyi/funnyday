package com.lqy.funnyday.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lqy.greendao.City;
import com.lqy.greendao.CityDao;
import com.lqy.greendao.Country;
import com.lqy.greendao.CountryDao;
import com.lqy.greendao.DaoMaster;
import com.lqy.greendao.DaoSession;
import com.lqy.greendao.Province;
import com.lqy.greendao.ProvinceDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrliu on 16-7-20.
 *
 * LocationDB 包含funnyday-db关于定位的PROVINCE,CITY,COUNTY表的存储，查询，字段添加等操作
 */
public class LocationDB {

    public static String defaultCityCode = "110000"; //北京市城市代码
    private static LocationDB locationDB;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daomaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    private Context context;


    public DaoMaster getDaomaster() {
        if(daomaster == null){
            daomaster = new DaoMaster(db);
        }
        return daomaster;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    //创建LocationDB对象同时创建数据库
    private LocationDB(Context context){
        //获取SQLiteOpenHelper对象,第二个参数位数据库名
        helper = new DaoMaster.DevOpenHelper(context,"funnyday-db",null);
        //获取SQLiteDatabase对象，创建数据库
        db = helper.getWritableDatabase();
        //获取daomaster对象
        daomaster = new DaoMaster(db);
        //通过daomaster对象实例化daoSession对象
        daoSession = daomaster.newSession();
        //通过daoSession对象实例化Province对象,curd(增删减查)操作也是通过该dao对象来操作
    }



    //获取WeatherDB对象,单例类，一次只能存在一个一个对象
    public synchronized static LocationDB getInstance(Context context){
        if(locationDB == null){
            locationDB = new LocationDB(context);
        }
        return locationDB;
    }

    //保存省对象至数据库funnyday.db 表PROVINCE
    public void saveProvince(Province province){
        if(province != null){
            ProvinceDao provinceDao = getDaoSession().getProvinceDao();
            provinceDao.insert(province);
        }
    }

    //保存市对象至数据库funnyday.db 表CITY
    public void saveCity(City city){
        if(city != null){
            CityDao cityDao = getDaoSession().getCityDao();
            cityDao.insert(city);
        }
    }

    //保存县对象至数据库funnyday.db 表country
    public void saveCountry(Country country){
        if(country != null){
            CountryDao countryDao = getDaoSession().getCountryDao();
            countryDao.insert(country);
        }
    }

    //遍历省表，返回Province泛型集合
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        ProvinceDao provinceDao = getDaoSession().getProvinceDao();
        Cursor cursor = getDb().query(provinceDao.getTablename(),provinceDao.getAllColumns(),null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getLong(cursor.getColumnIndex(ProvinceDao.Properties.Id.columnName)));
                province.setProvince_name(cursor.getString(cursor.getColumnIndex(ProvinceDao.Properties.Province_name.columnName)));
                province.setProvince_code(cursor.getString(cursor.getColumnIndex(ProvinceDao.Properties.Province_code.columnName)));
                list.add(province);
            }while(cursor.moveToNext());
        }
        return list;
    }

    //遍历市表,返回City泛型集合
    public List<City> loadCity(int provinceId){
        List<City> list =  new ArrayList<>();
        CityDao cityDao = getDaoSession().getCityDao();
        Cursor cursor = getDb().query(cityDao.getTablename(),null,CityDao.Properties.Province_id.columnName+"=?",new String[]{String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setId( cursor.getLong(cursor.getColumnIndex(CityDao.Properties.Id.columnName)));
                city.setCity_name(cursor.getString(cursor.getColumnIndex(CityDao.Properties.City_name.columnName)));
                city.setCity_code(cursor.getString(cursor.getColumnIndex(CityDao.Properties.City_code.columnName)));
                city.setProvince_id(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    //遍历县表，返回country泛型集合
    public List<Country> loadCountry(int cityId){
        List<Country> list =  new ArrayList<>();
        CountryDao countryDao = getDaoSession().getCountryDao();
        Cursor cursor = getDb().query(countryDao.getTablename(), null,CountryDao.Properties.City_id.columnName+"=?",new String[]{String.valueOf(cityId)},null,null,null);
        if(cursor.moveToFirst()){
            do{
                Country country = new Country();
                country.setId(cursor.getLong(cursor.getColumnIndex(CountryDao.Properties.Id.columnName)));
                country.setCountry_name(cursor.getString(cursor.getColumnIndex(CountryDao.Properties.Country_name.columnName)));
                country.setCountry_code(cursor.getString(cursor.getColumnIndex(CountryDao.Properties.Country_code.columnName)));
                country.setCity_id(cityId);
                list.add(country);
            }while(cursor.moveToNext());
        }
        return list;
    }
    /**
     * 清空数据库
     * */
    public void clearTable(String tableName){
        getDb().execSQL("DELETE FROM "+tableName);
        getDb().execSQL("UPDATE sqlite_sequence SET seq = 0 where name =\'"+tableName+"\'");
    }
}
