package com.lqy.funnyday.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mrliu on 16-9-26.
 */
public class PreferenceUtil {

    private static Context context;

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static String cityCodeDefault = "110000";
    public static String KEY_CITY_CODE ="cityCode";
    public static String weatherPreferenceName = "weather";

    public PreferenceUtil(Context context){
        this.context = context;

    }

    public void saveToPerference(String preferenceName,String key, String values){
        sharedPreferences = context.getSharedPreferences(preferenceName,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key,values);
    }

}
