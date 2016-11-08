package com.haui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.haui.map.CustemMaps;

import java.util.List;

/**
 * Created by Duong on 11/7/2016.
 */
public class AdapterSteps extends ArrayAdapter<CustemMaps.ItemStep> {
    public AdapterSteps(Context context, int resource, List<CustemMaps.ItemStep> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
