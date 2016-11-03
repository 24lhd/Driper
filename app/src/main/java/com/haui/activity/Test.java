package com.haui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Duong on 10/19/2016.
 */

public class Test extends AppCompatActivity{
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_infor);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.test);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });


    }

}
