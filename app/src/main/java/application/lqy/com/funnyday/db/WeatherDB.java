package application.lqy.com.funnyday.db;

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
    public void saveCounty(Country country){
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
                province.setProvince_name(cursor.getString(cursor.getColumnIndex(ProvinceDao.Properties.Province_name.columnName)));
                province.setProvince_code(cursor.getString(cursor.getColumnIndex(ProvinceDao.Properties.Province_code.columnName)));
                list.add(province);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public String getCodeFormName(String name, int code, String higherCode){
        switch(code){
            case 0:
                List<Province> provinceList = loadProvince();
                for (Province province : provinceList){
                    if (name == province.getProvince_name()){
                        return province.getProvince_code();
                    }
                }
                break;
            case 1:
                List<City> cityList = loadCity(Integer.parseInt(higherCode));
                for(City city : cityList){
                    if(name == city.getCity_name()){
                        return city.getCity_code();
                    }
                }
                break;
            case 2:
                List<Country> coutryList = loadCountry(Integer.parseInt(higherCode));

                break;
        }
        return name;
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
    public List<Country> loadCountry(int cityId){
        List<Country> list =  new ArrayList<>();
        CountryDao countryDao = getDaoSession().getCountryDao();
        Cursor cursor = getDb().query(countryDao.getTablename(), countryDao.getAllColumns(),null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Country country = new Country();
                country.setCounty_name(cursor.getString(cursor.getColumnIndex(CountryDao.Properties.County_name.columnName)));
                country.setCounty_code(cursor.getString(cursor.getColumnIndex(CountryDao.Properties.County_code.columnName)));
                country.setCity_id(cityId);
                list.add(country);
            }while(cursor.moveToNext());
        }
        return list;
    }

}
