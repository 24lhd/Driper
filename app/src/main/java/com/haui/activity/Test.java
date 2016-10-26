package com.haui.activity;

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
        setContentView(R.layout.test);
//        editText= (EditText) findViewById(R.id.tv);
//        toolbar= (Toolbar) findViewById(R.id.test_tb);
//        toolbar.inflateMenu(R.menu.test);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
    initView();

    }

    private void initView() {
    }
}
