package com.lqy.funnyday.model.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lqy.funnyday.R;
import com.lqy.funnyday.http.HttpUtil;
import com.lqy.funnyday.model.map.BaiduMapActivity;
import com.lqy.funnyday.util.OkHttpResponseUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mrliu on 16-7-18.
 */
public class WeatherFragment extends Fragment {

    private static WeatherFragment weatherFragment;
    private Context context;

    //UI组件
    private View view;
    private TextView tvCurrentTime, tvPublishTime, tvWeatherDesp, tvTemp;
    private TextView tvMap;
    private ImageView imgWeather;
    private Button toolbarBtn;

    //当前城市代码
    private String countryCode;
    //数据处理
    private SharedPreferences sharedPreferences;

    public static WeatherFragment getInstance(String label) {
        weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_weather, null);
        context = this.getActivity();
        /**
         * 实例化UI组件
         * */
        toolbarBtn = (Button)view.findViewById(R.id.toolbar_btn);
        tvPublishTime = (TextView)view.findViewById(R.id.tv_publish_time);
        tvCurrentTime = (TextView)view.findViewById(R.id.tv_current_time);
        imgWeather = (ImageView)view.findViewById(R.id.imgv_weather);
        tvWeatherDesp = (TextView)view.findViewById(R.id.tv_weather_desp);
        tvTemp = (TextView)view.findViewById(R.id.tv_temp);
        tvMap = (TextView)view.findViewById(R.id.tv_map);
        tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaiduMapActivity.class);
                startActivity(intent);
            }
        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        countryCode = sharedPreferences.getString("country_code","");
        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView() {
        if (!TextUtils.isEmpty(countryCode)){
            queryWeatherCode(countryCode);
        }else{
            showWeather();
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /**
     * 根据城市代码查找天气代码
     * @param countryCode
     */
    private void queryWeatherCode(String countryCode) {
        String address = "http://www.weather.com.cn/data/list3/city"+ countryCode +".xml";
        queryFromServer(address, "countryCode");
    }

    /**
     * 根据天气代码查找天气信息
     * @param weatherCode
     */
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address, "weatherCode");
    }

    /**
     * 从服务器查找数据并解析
     * @param address
     * @param type
     */
    private void queryFromServer(final String address, final String type) {
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
                if ("countryCode".equals(type)){
                    if (!TextUtils.isEmpty(result)){
                        String[] array = result.split("\\|");
                        if (array != null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
                    OkHttpResponseUtil.handleWeatherResponse(context,result);
                    showWeather();
                }
            }
        });
    }

    private void showWeather(){
        tvPublishTime.setText(sharedPreferences.getString("publish_time",""));
        tvCurrentTime.setText(sharedPreferences.getString("current_time",""));
        tvWeatherDesp.setText(sharedPreferences.getString("weather_desp",""));
        tvTemp.setText(sharedPreferences.getString("temp1","") + " ~ " + sharedPreferences.getString("temp2",""));
    }

}
