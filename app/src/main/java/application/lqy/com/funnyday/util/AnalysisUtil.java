package application.lqy.com.funnyday.util;

import android.text.TextUtils;
import android.util.Log;

import com.lqy.greendao.Province;

import application.lqy.com.funnyday.db.WeatherDB;

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
}
