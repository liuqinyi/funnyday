package com.lqy.funnyday.model.weather.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lqy.funnyday.R;

/**
 * Created by mrliu on 16-7-18.
 */
public class WeatherFragment extends Fragment {

    private View view;

    private static WeatherFragment weatherFragment;

    public static WeatherFragment getInstance(String label) {
        weatherFragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        weatherFragment.setArguments(bundle);
        return weatherFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_weather, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_label);
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("label"));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
