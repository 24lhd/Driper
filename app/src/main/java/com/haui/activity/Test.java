package com.haui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Duong on 10/19/2016.
 */

public class Test extends ActionBarActivity{
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_infor);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarUser);

        toolbar.setNavigationIcon(R.drawable.ic_exit);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

    }
}
