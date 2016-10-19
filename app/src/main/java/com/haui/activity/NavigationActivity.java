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
import android.widget.Toast;
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

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,ValueEventListener {
    private com.haui.log.Log log;
    private DatabaseReference database;
    private String idSV;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        log=new Log(this);
        database = FirebaseDatabase.getInstance().getReference();
        reQuestPermistion();
        creatView();
        checkLogin();
    }
    private void reQuestPermistion() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);
            }
        }
    }
    private void checkLogin() {
           if (log.getID().isEmpty()){
              startLogin();
            }else{
               login(log.getID(),log.getPass());
            }
    }
    public void signOut() {
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
                creatData();
            }else{
                log.remove();
                setViewNullData();
            }
        }
    }
    private void creatData() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.map);
        setMap();
    }
    private void login(final String id, final String pass) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Đang đăng nhập...");
        dialog.setCancelable(false);
        dialog.show();
        final ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                while (true){
                    try {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getPassWord().equals(pass)){
                            dialog.dismiss();
                            idSV=id;
                            creatData();
                            database.child("users").child(id).getRef().removeEventListener(this);
                            break;
                        }
                    }catch (NullPointerException e){
                        dialog.dismiss();
                        Toast.makeText(NavigationActivity.this, "Sai mã sinh viên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        startLogin();
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        database.child("users").child(id).getRef().addValueEventListener(postListener);
    }

    private  NavigationView navigationView;
    private  ViewFlipper viewFlipper;
    private  DrawerLayout drawer;
    private  Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private void creatView() {
         toolbar = (Toolbar) findViewById(R.id.toolbar);
         viewFlipper = (ViewFlipper)findViewById(vf);
        toolbar.setTitle("acn");
        setSupportActionBar(toolbar);
         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }
    private void setViewNullData() {
        viewFlipper.setDisplayedChild(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment=new NullDataFragment();
        ft.replace(R.id.fragment, fragment).commit();

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
                final MyInforFragment fragment=new MyInforFragment();
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        while (true){
                            try {
                                User user = dataSnapshot.getValue(User.class);
                                fragment.setTextInfor(user.getTenSV(),user.getMaSV(),user.getTenLopDL(),user.getSoDT(),user.getTenViTri(),user.getBienSoXe());
                                database.child("users").child(idSV).getRef().removeEventListener(this);
                                break;
                            }catch (NullPointerException e){
                                android.util.Log.e("faker","null");
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                database.child("users").child(idSV).getRef().addValueEventListener(postListener);
                ft.replace(R.id.fragment, fragment).commit();
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
