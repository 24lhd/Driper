package com.haui.activity;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

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
//    initView();
        LatLng latLng=new LatLng(21.05400482,105.73306561);
        LatLng latLng2=new LatLng(21.05296351,105.73463202);
        Location locationa=new Location("a");
        locationa.setLatitude(latLng.latitude);
        locationa.setLongitude(latLng.longitude);
        Location locationb=new Location("b");
        locationb.setLatitude(latLng2.latitude);
        locationb.setLongitude(latLng2.longitude);
        Log.e("faker",""+locationa.distanceTo(locationb));
    }
    private void initView() {
//        EditText editText = (EditText) findViewById(R.id.search);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                boolean handled = false;
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
////                    sendMessage();
//                    handled = true;
//                }
//                return handled;
//            }
//        });
    }
}
