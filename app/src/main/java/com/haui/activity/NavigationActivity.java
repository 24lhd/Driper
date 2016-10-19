package com.haui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haui.fragment.MyInforFragment;
import com.haui.fragment.NullDataFragment;
import com.haui.log.Log;
import com.haui.map.MapManager;
import com.haui.object.User;

import static android.support.design.widget.Snackbar.make;
import static com.haui.activity.R.id.vf;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private com.haui.log.Log log;
    private DatabaseReference database;
    private String passWord;
    private String maSV;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        log=new Log(this);
        database = FirebaseDatabase.getInstance().getReference();
        reQuestPermistion();
        creatView();
        checkLogin(log.getID(), log.getPass());
    }
    private void reQuestPermistion() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);
            }
        }
    }
    private void checkLogin(String extra, String stringExtra) {
        try {
            if (stringExtra.isEmpty()||extra.isEmpty()){
                startLogin();
            }else{
                login(extra,stringExtra);
            }
        }catch (NullPointerException e){
            android.util.Log.e("faker","checkLogin");
            startLogin();
        }


    }
    public void signOut() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        log.remove();
       startLogin();
    }
    public void startLogin() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2){
            if (resultCode==RESULT_OK){
                checkLogin(data.getStringExtra(Log.LOG_ID),data.getStringExtra(Log.LOG_PASS));
            }else{
                log.remove();
                setViewNullData();
            }
        }
    }
    private void creatData() {

        navigationView.setCheckedItem(R.id.map);
        setMap();
    }
    private void login(final String extra, final String stringExtra) {
         final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Đang khởi tạo dữ liệu...");
        dialog.setCancelable(false);
        dialog.show();
        try {
            database.child("users").child(extra).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            try {
                                if (user.getPassWord().equals(stringExtra)){
                                    android.util.Log.e("faker",user.toString());
                                    passWord=stringExtra;
                                    maSV = extra;
                                    dialog.dismiss();
                                    creatData();
                                }else {
                                    startLogin();
                                }
                            }catch (NullPointerException e){
                                android.util.Log.e("faker","login");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            android.util.Log.e("faker","onCancelled");
                        }
                    });
        }catch (NullPointerException e){}
    }
    private  NavigationView navigationView;
    private  ViewFlipper viewFlipper;
    private  DrawerLayout drawer;
    private  Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private void creatView() {
         toolbar = (Toolbar) findViewById(R.id.toolbarMain);
         viewFlipper = (ViewFlipper)findViewById(vf);
        toolbar.setTitle("Driper");
        setSupportActionBar(toolbar);
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void setViewNullData() {
        viewFlipper.setDisplayedChild(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment=new NullDataFragment();
        ft.replace(R.id.fragment, fragment).commit();

    }
    private  MyInforFragment fragmen;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.mn_nguoi_tim_xe:
                toolbar.setTitle("Thông tin cá nhân");
                setSupportActionBar(toolbar);
                setMap();
                break;
            case R.id.mn_xe_tim_nguoi:
                setMap();
                break;
            case R.id.mn_user:
                viewFlipper.setDisplayedChild(0);
                toolbar.setTitle("Thông tin cá nhân");
                setSupportActionBar(toolbar);
                fragmen=new MyInforFragment();
                 final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Đang khởi tạo dữ liệu...");
                dialog.setCancelable(false);
                dialog.show();
                database.child("users").child(maSV).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                try {
                                    if (user.getPassWord().equals(passWord)){
                                        fragmen.setTextInfor(user.getTenSV(),user.getMaSV(),user.getTenLopDL(),user.getSoDT(),user.getTenViTri(),user.getBienSoXe());
                                        dialog.dismiss();
                                    }
                                }catch (NullPointerException e){
                                    android.util.Log.e("faker","MyInforFragment");
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                android.util.Log.e("faker","onCancelled");
                            }
                        });
                ft.replace(R.id.fragment, fragmen).commit();
                break;

            case R.id.mn_yeucau:

                break;

            case R.id.mn_error:

                break;

            case R.id.mn_help:

                break;

            case R.id.mn_logout:
                signOut();
                break;
            case R.id.mn_exit:
                finish();
                break;
            case R.id.mn_share:

                break;
            case R.id.mn_dev:
//                ft.replace(R.id.fragment, fragment).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void callPhone(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        startActivity(callIntent);
        make(getCurrentFocus(), "mn_share", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    private void setMap() {
        viewFlipper.setDisplayedChild(1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       new MapManager(googleMap,this);
    }

}
