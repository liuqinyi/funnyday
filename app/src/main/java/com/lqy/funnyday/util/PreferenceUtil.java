package com.lqy.funnyday.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.lqy.funnyday.model.weather.WeatherInfo;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mrliu on 16-9-26.
 */
public class PreferenceUtil {

    private Context context;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    //定义SharedPreference文件名
    public static String NAME_WEATHER = "weather";


    public static String cityCodeDefault = "010101";  //默认的城市代码是北京市

    //SharedPreference字段索引的key值
    public static String KEY_CITY_CODE = "cityCode";
    public static String KEY_WEATHER_CODE="weatherCode";
    public static String KEY_TEMP_1 = "temp1";
    public static String KEY_TMEP_2 = "temp2";
    public static String KEY_WEATHER_DESP = "weatherDesc";


    public PreferenceUtil(Context context) {
        this.context = context;

    }

    public void saveToPreference(String preferenceName, String key, String values) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
        editor.putString(key, values);
        editor.commit();
    }

    /**
     * 讲今夕玩的天禧信息存至Sharedpreference文件中
     *
     * @param context
     * @param weatherInfo
     */
    public static void saveWeatherInfoToSharePreferences(Context context, WeatherInfo weatherInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy年M月d日", Locale.CANADA);
        SharedPreferences sharedPreferences = context.getSharedPreferences("weather", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

    }

}
