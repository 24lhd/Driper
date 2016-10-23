package com.haui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Duong on 10/19/2016.
 */

public class Test extends ActionBarActivity {
    private EditText editText;
    private Toolbar toolbar;
    private Button bt;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yeu_cau_layout);
//        editText= (EditText) findViewById(R.id.tv);
//        toolbar= (Toolbar) findViewById(R.id.test_tb);
//        toolbar.inflateMenu(R.menu.test);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
    initView();

    }
    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tab_yeucau);
        if (tabLayout != null) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setBackgroundColor(Color.WHITE);
            tabLayout.setTabTextColors( getResources().getColor(R.color.md_blue_grey_300),getResources().getColor(R.color.md_blue_500));
            tabLayout.setSelectedTabIndicatorHeight(0);
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
            tabLayout.getTabAt(0).setText("Tìm xe");
            tabLayout.getTabAt(1).setText("Tìm người");
            tabLayout.getTabAt(2).setText("Của bạn");
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
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
