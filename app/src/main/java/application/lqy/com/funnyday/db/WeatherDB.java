package application.lqy.com.funnyday.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lqy.greendao.City;
import com.lqy.greendao.CityDao;
import com.lqy.greendao.County;
import com.lqy.greendao.CountyDao;
import com.lqy.greendao.DaoMaster;
import com.lqy.greendao.DaoSession;
import com.lqy.greendao.Province;
import com.lqy.greendao.ProvinceDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrliu on 16-7-20.
 */
public class WeatherDB {

    private static WeatherDB weatherDB;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daomaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;


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

    public WeatherDB(Context context){
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
    /*public synchronized static WeatherDB getInstance(Context context){
        if(weatherDB != null){
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }*/

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

    //保存县对象至数据库funnyday,db 表COUNTY
    public void saveCounty(County county){
        if(county != null){
            CountyDao countyDao = getDaoSession().getCountyDao();
            countyDao.insert(county);
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
        Cursor cursor = getDb().query(cityDao.getTablename(),cityDao.getAllColumns(),null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                City city = new City();
                city.setCity_name(cursor.getString(cursor.getColumnIndex(CityDao.Properties.City_name.columnName)));
                city.setCity_code(cursor.getString(cursor.getColumnIndex(CityDao.Properties.City_code.columnName)));
                city.setProvince_id(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
        }
        return list;
    }

    //遍历县表，返回County泛型集合
    public List<County> loadCounty(int cityId){
        List<County> list =  new ArrayList<>();
        CountyDao countyDao = getDaoSession().getCountyDao();
        Cursor cursor = getDb().query(countyDao.getTablename(),countyDao.getAllColumns(),null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                County county = new County();
                county.setCounty_name(cursor.getString(cursor.getColumnIndex(CountyDao.Properties.County_name.columnName)));
                county.setCounty_code(cursor.getString(cursor.getColumnIndex(CountyDao.Properties.County_code.columnName)));
                county.setCity_id(cityId);
                list.add(county);
            }while(cursor.moveToNext());
        }
        return list;
    }

}
