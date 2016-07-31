package com.lqy.funnyday.ui.fragment;

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
public class BaseFragment extends Fragment{
    private String label;
    private BaseFragment baseFragment;

    public BaseFragment(String label){
        this.label = label;
    }

    public BaseFragment(){}


    public BaseFragment getInstance(String label) {
        baseFragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_base,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_content);
        Bundle args = getArguments();
        if (args != null){
            this.label = args.getString("label");
        }
        txt_content.setText(label);
        return view;
    }
}
