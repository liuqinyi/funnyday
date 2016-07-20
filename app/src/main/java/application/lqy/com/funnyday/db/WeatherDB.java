package application.lqy.com.funnyday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lqy.greendao.City;
import com.lqy.greendao.CityDao;
import com.lqy.greendao.County;
import com.lqy.greendao.CountyDao;
import com.lqy.greendao.DaoMaster;
import com.lqy.greendao.DaoSession;
import com.lqy.greendao.Province;
import com.lqy.greendao.ProvinceDao;

/**
 * Created by mrliu on 16-7-20.
 */
public class WeatherDB {

    private static WeatherDB weatherDB;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daomaster;
    private DaoSession daoSession;
    private ProvinceDao provinceDao;
    private CityDao cityDao;
    private CountyDao countydao;

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

    private WeatherDB(Context context){
        //获取SQLiteOpenHelper对象,第二个参数位数据库名
        helper = new DaoMaster.DevOpenHelper(context,"funnyday-db",null);
        //获取SQLiteDatabase对象，创建数据库
        db = helper.getWritableDatabase();
        //获取daomaster对象
        daomaster = new DaoMaster(db);
        //通过daomaster对象实例化daoSession对象
        daoSession = daomaster.newSession();
        //通过daoSession对象实例化Province对象,curd(增删减查)操作也是通过该dao对象来操作
        /*provinceDao = daoSession.getProvinceDao();
        cityDao = daoSession.getCityDao();
        countydao = daoSession.getCountyDao();*/

    }

    //获取WeatherDB对象
    public synchronized static WeatherDB getInstance(Context context){
        if(weatherDB != null){
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }

    public void savaProvince(Province province){
        if(province != null){
            ProvinceDao provinceDao = getDaoSession().getProvinceDao();
            provinceDao.insert(province);
        }
    }

    public void savaCity(City city){
        if(city != null){
            CityDao cityDao = getDaoSession().getCityDao();
            cityDao.insert(city);
        }
    }

    public void savaCounty(County county){
        if(county != null){
            CountyDao countyDao = getDaoSession().getCountyDao();
            countyDao.insert(county);
        }
    }

}
