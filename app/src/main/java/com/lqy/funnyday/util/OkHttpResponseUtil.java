package com.lqy.funnyday.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lqy.funnyday.db.LocationDB;
import com.lqy.funnyday.model.weather.domain.WeatherInfo;
import com.lqy.greendao.City;
import com.lqy.greendao.Country;
import com.lqy.greendao.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mrliu on 16-7-21.
 *
 * okhttp框架response解析类
 */
public class OkHttpResponseUtil {

    private static final String TAG = "OkHttpResponseUtil";
    /**
     * 解析
     * response为市数据
     * @param locationDB 需要存入的数据库对象
     * @param response url返回市数据
     * */
    public synchronized static boolean handleProvincesResponse(LocationDB locationDB, String response){
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
                    locationDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析
     * response为市数据
     * @param locationDB 需要存入的数据库对象
     * @param response:url 返回市数据
     * @param provinceId:对应省代码，传入之后保存即可，不须处理逻辑
     * */
    public static boolean handlerCityResponse(LocationDB locationDB, String response, int provinceId){
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
                    locationDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析
     * response为市数据
     * @param locationDB 需要存入的数据库对象
     * @param response:url 返回县数据
     * @param cityId:对应市代码，传入之后保存即可，不须处理逻辑
     * */
    public static boolean handlerCountiesResponse(LocationDB locationDB, String response, int cityId){
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
                    locationDB.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context,String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherJson = jsonObject.getJSONObject("weatherinfo");
            Gson gson = new Gson();
            WeatherInfo weatherInfo = gson.fromJson(String.valueOf(weatherJson),WeatherInfo.class);
            saveWeatherInfoToSharePreferences(context,weatherInfo);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private static void saveWeatherInfoToSharePreferences(Context context, WeatherInfo weatherInfo) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyy年M月d日", Locale.CANADA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name" , weatherInfo.getCityName());
        editor.putString("weather_code" , weatherInfo.getWeatherCode());
        editor.putString("temp1" , weatherInfo.getTemp1());
        editor.putString("temp2" , weatherInfo.getTemp2());
        editor.putString("weather_desp" , weatherInfo.getWeatherDesp());
        editor.putString("publish_time",weatherInfo.getPtTime());
        editor.putString("current_time",sdf.format(new Date()));
        editor.commit();
    }
}
