package com.haui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haui.activity.R;

/**
 * Created by Duong on 10/23/2016.
 */

public class YeuCauFragment extends Fragment{
    private View view;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.yeu_cau_layout,container,false);
        initView();
        return view;
    }
    private void initView() {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_yeucau);
        if (tabLayout != null) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setBackgroundColor(Color.WHITE);
            tabLayout.setTabTextColors( getResources().getColor(R.color.md_blue_grey_300),getResources().getColor(R.color.md_blue_500));
            tabLayout.setSelectedTabIndicatorHeight(0);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_wait_1).setText("Đang đợi");
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_done_0).setText("Đã xong");
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            tabLayout.getTabAt(0).setIcon(R.drawable.ic_wait_1).setText("Đang đợi");
                            tabLayout.getTabAt(1).setIcon(R.drawable.ic_done_0).setText("Đã xong");
                            break;
                        case 1:
                            tabLayout.getTabAt(0).setIcon(R.drawable.ic_wait_0).setText("Đang đợi");
                            tabLayout.getTabAt(1).setIcon(R.drawable.ic_done_1).setText("Đã xong");

                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }
}
