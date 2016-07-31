package com.lqy.funnyday.model.own;

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
public class OwnFragment extends Fragment {
    private View view;
    private static OwnFragment ownFragment;
    private String label;

    public static OwnFragment getInstance(String label) {
        ownFragment = new OwnFragment();
        Bundle bundle = new Bundle();
        bundle.putString("label", label);
        ownFragment.setArguments(bundle);
        return ownFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fg_layout_news, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_label);
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("label"));
        return view;
    }
}
