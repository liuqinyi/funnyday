package com.lqy.funnyday.model.weather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lqy.com.funnyday.R;
import com.lqy.funnyday.ui.fragment.BaseFragment;

/**
 * Created by mrliu on 16-7-18.
 */
public class WeatherFragment extends BaseFragment{

    private Toolbar toolbar;

    private Button goChoiceWeatherActivity;

    private View view;

    public WeatherFragment(){
    }

    public WeatherFragment(String content){
        super(content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_weather,null);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        /*toolbar = (Toolbar)this.view.findViewById(R.id.toolbar);
        goChoiceWeatherActivity = (Button)view.findViewById(R.id.toolbar_btn);
        Intent intent = new Intent(MyApplication.getContext(), ChoiceCityActivity.class);
        startActivity(intent);*/
    }
}
