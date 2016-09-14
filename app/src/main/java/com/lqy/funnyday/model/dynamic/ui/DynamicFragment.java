package com.lqy.funnyday.model.dynamic.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lqy.funnyday.R;

/**
 * Created by mrliu on 16-7-18.
 */
public class DynamicFragment extends Fragment {
    private View view;
    private static DynamicFragment dynamicFragment;
    private String label;

    public static DynamicFragment getInstance(String label) {
        dynamicFragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        dynamicFragment.setArguments(bundle);
        return dynamicFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_dynamic, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_label);
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("label"));
        return view;
    }
}
