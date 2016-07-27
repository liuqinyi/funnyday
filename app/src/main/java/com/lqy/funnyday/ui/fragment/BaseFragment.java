package com.lqy.funnyday.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lqy.com.funnyday.R;

/**
 * Created by mrliu on 16-7-18.
 */
public class BaseFragment extends Fragment{
    private String label;

    public BaseFragment(){}

    public BaseFragment(String label) {
        this.label = label;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_base,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        txt_content.setText(label);
        return view;
    }
}
