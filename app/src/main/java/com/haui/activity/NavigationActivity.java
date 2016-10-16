package com.haui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.haui.log.Log;
import com.haui.map.MapManager;

public class NavigationActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private com.haui.log.Log log;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);
            }
        }
        checkLogin();

    }

    private void checkLogin() {
//        log=new Log(this);
        creatView();

//        if (log.getID().isEmpty()){
//            startLogin();
//        }else if (login(log.getID(),log.getPass())){
//           creatView();
//        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                String id=data.getStringExtra(Log.LOG_ID);
                String pass=data.getStringExtra(Log.LOG_PASS);
                log.putID(id);
                log.putPass(pass);
                checkLogin();
            }else if (resultCode==Activity.RESULT_CANCELED){
                finish();
            }
        }
    }
    private boolean login(String id, String pass) {

        return false;
    }

    public void startLogin() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivityForResult(intent,0);
    }

    private void creatView() {
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
//        vf.setDisplayedChild(1);
        FloatingActionButton fab = (FloatingActionButton) vf.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        (AppCompatActivity) setSupportActionBar(toolbar);
//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.mn_user) {
             Snackbar.make(getCurrentFocus(), "mn_user", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }else if (id == R.id.mn_history) {
             Snackbar.make(getCurrentFocus(), "mn_history", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }else if (id == R.id.mn_nguoi_tim_xe) {
            ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
            vf.setDisplayedChild(1);
            FloatingActionButton fab = (FloatingActionButton) vf.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else if (id == R.id.mn_xe_tim_nguoi) {
             Snackbar.make(getCurrentFocus(), "mn_nguoi_tim_xe", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
             setMap();
        }else if (id == R.id.mn_error) {
             Snackbar.make(getCurrentFocus(), "mn_cai_dat", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }
         else if (id == R.id.mn_help) {
             Snackbar.make(getCurrentFocus(), "mn_cai_dat", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
         }else if (id == R.id.mn_logout) {
             Snackbar.make(getCurrentFocus(), "mn_login", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }else if (id == R.id.mn_exit) {
             Snackbar.make(getCurrentFocus(), "mn_dev", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
         }else if (id == R.id.mn_dev) {
             Snackbar.make(getCurrentFocus(), "mn_dev", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }else if (id == R.id.mn_share) {
             Intent callIntent = new Intent(Intent.ACTION_CALL);
             callIntent.setData(Uri.parse("tel:" + "0986052482"));
             startActivity(callIntent);
             Snackbar.make(getCurrentFocus(), "mn_share", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setMap() {
        ViewFlipper vf = (ViewFlipper)findViewById(R.id.vf);
        vf.setDisplayedChild(0);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       new MapManager(googleMap,this);
    }
}
