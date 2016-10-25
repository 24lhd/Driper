package com.haui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haui.activity.R;

/**
 * Created by Duong on 10/25/2016.
 */

public class TimNguoiFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.yeu_cau_fragment_layout,container,false);
        initView();
        return view;
    }

    private void initView() {

    }
}
