package com.haui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haui.activity.R;

/**
 * Created by Duong on 10/19/2016.
 */

public class MyInforFragment extends Fragment{
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.layout_my_infor,container,false);
        initView();
        return view;
    }
    private CollapsingToolbarLayout collapsingToolbar;
    private void initView( ) {
         collapsingToolbar =(CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        final ImageView imageView = (ImageView) view.findViewById(R.id.backdrop);
//        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    public void setTextInfor(String tv_infor_ten, String tv_infor_masv, String tv_infor_lop, String tv_infor_sdt,String img) {
        ((TextView) view.findViewById(R.id.tv_infor_ten)).setText(tv_infor_ten);
        ((TextView) view.findViewById(R.id.tv_infor_masv)).setText(tv_infor_masv);
        ((TextView) view.findViewById(R.id.tv_infor_lop)).setText(tv_infor_lop);
        ((TextView) view.findViewById(R.id.tv_infor_sdt)).setText(tv_infor_sdt);
        collapsingToolbar.setTitle(tv_infor_ten);
    }


}
