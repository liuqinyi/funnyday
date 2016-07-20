package application.lqy.com.funnyday.model.weather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import application.lqy.com.funnyday.R;
import application.lqy.com.funnyday.ui.fragment.BaseFragment;

/**
 * Created by mrliu on 16-7-18.
 */
public class WeatherFragment extends BaseFragment{

    public WeatherFragment(){
    }

    public WeatherFragment(String content){
        super(content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_layout_weather,null);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
