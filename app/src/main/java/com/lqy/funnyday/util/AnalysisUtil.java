package com.lqy.funnyday.util;

import android.text.TextUtils;
import android.util.Log;

import com.lqy.funnyday.db.WeatherDB;
import com.lqy.greendao.City;
import com.lqy.greendao.Country;
import com.lqy.greendao.Province;

/**
 * Created by mrliu on 16-7-21.
 *
 * 数据解析类
 */
public class AnalysisUtil {

    private static final String TAG = "AnalysisUtil";

    //解析获取的省级代码
    public static boolean handleProvincesResponse(WeatherDB weatherDB, String response){
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length>0){
                for (String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setId(null);
                    province.setProvince_code(array[0]);
                    province.setProvince_name(array[1]);
                    Log.d(TAG, "handleProvincesResponse: province: " + "name = " + province.getProvince_name()+",code = " + province.getProvince_code());
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    //解析根据省级代码获取的市级代码
    public static boolean handlerCityResponse(WeatherDB weatherDB, String response, int provinceId){
        if (!TextUtils.isEmpty(response)) {
            String[] allCites = response.split(",");
            if (allCites != null && allCites.length > 0) {
                for (String c : allCites) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setId(null);
                    city.setCity_code(array[0]);
                    city.setCity_name(array[1]);
                    city.setProvince_id(provinceId);
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    //解析根据市级代码获得县级县级代码
    public static boolean handlerCountiesResponse(WeatherDB weatherDB, String response, int cityId){
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    Country country = new Country();
                    country.setId(null);
                    country.setCountry_code(array[0]);
                    country.setCountry_name(array[1]);
                    country.setCity_id(cityId);
                    weatherDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }
}
