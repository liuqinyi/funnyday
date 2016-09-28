package com.lqy.funnyday.model.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqy.funnyday.R;
import com.lqy.funnyday.model.map.BaiduMapActivity;
import com.lqy.funnyday.util.PreferenceUtil;

/**
 * Created by mrliu on 16-7-18.
 */
public class WeatherFragment extends Fragment  {

    private static WeatherFragment weatherFragment;
    private static final String TAG = "WeatherFragment";
    private Context context;

    //UI组件
    private View view;
    private TextView tvCurrentTime, tvPublishTime, tvWeatherDesp, tvTemp;
    private TextView tvMap;
    private ImageView imgWeather;
    private Button toolbarBtn;

    //当前城市代码
    private String currentCityCode;
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
        this.context = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_weather, null);

        /**
         * 实例化UI组件
         * */
        toolbarBtn = (Button) view.findViewById(R.id.toolbar_btn);
        tvPublishTime = (TextView) view.findViewById(R.id.tv_publish_time);
        tvCurrentTime = (TextView) view.findViewById(R.id.tv_current_time);
        imgWeather = (ImageView) view.findViewById(R.id.imgv_weather);
        tvWeatherDesp = (TextView) view.findViewById(R.id.tv_weather_desp);
        tvTemp = (TextView) view.findViewById(R.id.tv_temp);
        tvMap = (TextView) view.findViewById(R.id.tv_map);

        initView();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView() {
        tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaiduMapActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        sharedPreferences = context.getSharedPreferences(PreferenceUtil.NAME_WEATHER, Context.MODE_APPEND);
        currentCityCode = sharedPreferences.getString("cityCode", PreferenceUtil.cityCodeDefault);
        showWeather();
    }


    private void showWeather() {
        tvPublishTime.setText(sharedPreferences.getString("publish_time", ""));
        tvCurrentTime.setText(sharedPreferences.getString("current_time", ""));
        tvWeatherDesp.setText(sharedPreferences.getString("weather_desp", ""));
        tvTemp.setText(sharedPreferences.getString("temp1", "") + " ~ " + sharedPreferences.getString("temp2", ""));
    }

}
