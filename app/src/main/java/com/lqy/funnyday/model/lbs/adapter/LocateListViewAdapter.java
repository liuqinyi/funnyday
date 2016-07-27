package com.lqy.funnyday.model.lbs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by mrliu on 16-7-24.
 */
public class LocateListViewAdapter extends ArrayAdapter {

    private int resourceId;
    public LocateListViewAdapter(Context context, int resource, int textViewResourceId, List objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public LocateListViewAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        return super.getView(position, convertView, parent);

    }
}
