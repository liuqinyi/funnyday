package com.lqy.funnyday.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.Reader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mrliu on 16-9-28.
 */
public class WeatherInfoUtil {

    private Context context;
    private static final String TAG = "WeatherInfoUtil";

    //定义查询类型
    public static String QUERY_TYPE_1 = "cityCode";
    public static String QUERY_TYPE_2 = "weatherCode";


    public WeatherInfoUtil(Context context) {
        this.context = context;
    }

    /**
     * 根据城市代码查找天气代码
     *
     * @param cityCode
     */
    public void queryWeatherCode(String cityCode) {
        String address = "http://www.weather.com.cn/data/list3/city" + cityCode + ".xml";
        Log.d(TAG, "queryWeatherCode: addressURL " + address);

        HttpUtil.doAsynGet(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("响应回调出错" + response);
                String result = response.body().string();
                Log.d(TAG, "onResponse: " + result);
                if (!TextUtils.isEmpty(result)) {
                    String[] array = result.split("\\|");
                    if (array != null && array.length == 2) {
                        String weatherCode = array[1];

                        response.close();

                        queryWeatherInfo(weatherCode);
                    }
                }
            }
        });


    }

    /**
     * 根据天气代码查找天气信息
     * @param weatherCode
     */
    public void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        Log.d(TAG, "queryWeatherInfo: address = " + address);

        HttpUtil.doAsynGet(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "加载失败！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("响应回调出错" + response);
                Log.d(TAG, "onResponse: response = " + response );
                Reader buffer = response.body().charStream();
                Log.d(TAG, "onResponse: buffer = " + buffer);
                String result = String.valueOf(buffer);
                Log.d(TAG, "onResponse: result = " + result);
                //OkHttpResponseUtil.handleWeatherResponse(context, result);
            }
        });
    }


}
