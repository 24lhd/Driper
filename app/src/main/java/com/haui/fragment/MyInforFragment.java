package com.haui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haui.activity.R;

/**
 * Created by Duong on 10/19/2016.
 */

public class MyInforFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_my_infor,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }
}
