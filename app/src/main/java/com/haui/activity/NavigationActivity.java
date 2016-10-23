package com.haui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.haui.fragment.MyInforFragment;
import com.haui.fragment.NullDataFragment;
import com.haui.fragment.YeuCauFragment;
import com.haui.log.Log;
import com.haui.map.MapManager;
import com.haui.object.User;
import com.haui.service.MyService;

import static android.support.design.widget.Snackbar.make;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,View.OnClickListener {
    private com.haui.log.Log log;
    private DatabaseReference database;
    private String passWord;
    private String maSV;
    private int contenView;
    private NullDataFragment nullDataFragment;
    private MyInforFragment myInforFragment;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private ProgressDialog progressDialog;
    private ImageView imHeadNavigation;
    private TextView tvTenHeadNavigation;
    private TextView tvViTriHeadNavigation;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Đang khởi tạo....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        creatView();
        checkLogin("","");
    }



    private void creatView() {
        setContentView(R.layout.activity_navigation);
        log = new Log(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        database = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
        toolbar.setTitle("Driper");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view=navigationView.getHeaderView(0);
        imHeadNavigation= (ImageView) view.findViewById(R.id.im_hd_img_profile);
        tvTenHeadNavigation= (TextView) view.findViewById(R.id.hd_tv_tensv);
        tvViTriHeadNavigation= (TextView) view.findViewById(R.id.hd_tv_vitri);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private StorageReference mStorageRef;
    public void checkLogin(String extra, String stringExtra) {
        if (isOnline()) {
            try {
                if (!log.getID().isEmpty()||!log.getPass().isEmpty()) {
                    login(log.getID(), log.getPass());
                } else if (!stringExtra.isEmpty()||!extra.isEmpty()){
                    login(extra, stringExtra);
                }else{
                    startLogin();
                }

            } catch (NullPointerException e) {
                android.util.Log.e("faker", "checkLogin");
                startLogin();
            }
        } else {
            progressDialog.dismiss();
            setViewOffLine();
            final Snackbar snackbar = Snackbar.make(navigationView, "Vui lòng bật kết nối internet!", Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.setAction("Bật wifi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    }else {
                        Intent intent=new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                        startActivity(intent);
                    }
                    snackbar.dismiss();
                    checkLogin(log.getID(), log.getPass());
                }
            });
            snackbar.show();

        }

    }

    public void signOut() {
        log.remove();
        startLogin();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (contenView){
            case R.id.mn_user:
                getMenuInflater().inflate(R.menu.menu_select_image,menu);
                break;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isOnline()){
            if (requestCode == 1001) {
                if (resultCode == RESULT_OK) {
                    checkLogin(data.getStringExtra(Log.LOG_ID), data.getStringExtra(Log.LOG_PASS));
                } else {
                    log.remove();
                    finish();
                }
            }else if (resultCode == RESULT_OK && requestCode == 1011) {
                Uri file = data.getData();
                upAndGetUrlImageProfile(file);
            } else if (resultCode == RESULT_OK && requestCode == 1010) {
                Uri file = data.getData();
                upAndGetUrlImageProfile(file);
            }
        }else {
            setViewOffLine();
        }
    }
    private void upAndGetUrlImageProfile(Uri file) {
        myInforFragment.showProgress();
        StorageReference riversRef = mStorageRef.child("images/"+"image_"+maSV);
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        upDateUser("imgProfile",downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        myInforFragment.hideProgress();
                    }
                });
    }

    public void upDateUser(final String item, final String valuse) {
        database.child("users").child(maSV).child(item).setValue(valuse, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (item.equals("imgProfile")){
                    if (databaseError != null) {
                        myInforFragment.hideProgress();
                    } else {
                        myInforFragment.setProImage(valuse);
                        Glide.with(NavigationActivity.this).load(valuse).fitCenter().into(imHeadNavigation);
                    }
                }

            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        item.getItemId();
        switch (item.getItemId()){
            case R.id.mn_stroge:
                myInforFragment.from_gallery();
                return true;
            case R.id.mn_camera:
                myInforFragment.from_camera();
                return true;
            case R.id.mn_update:
                myInforFragment.updateInfor();
                return true;
            case R.id.mn_update_pass:
                myInforFragment.updatePass();
                return true;
            case R.id.mn_delete_user:
                myInforFragment.deleteAcc();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void startLogin() {
        if (isOnline()) {
            Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1001);
        } else {
            setViewOffLine();
        }
    }
    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {return false;}
    }

    private void creatData() {
        if (isOnline()){
            toolbar.setTitle("Lời gửi");
            yeuCauFragment=new YeuCauFragment();
            fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment, yeuCauFragment).commitAllowingStateLoss();
            navigationView.setCheckedItem(R.id.mn_home);
            progressDialog.dismiss();
        }else {
            setViewOffLine();
        }
    }
    private void login(final String extra, final String stringExtra) {
        if (isOnline()) {
            try {
                database.child("users").child(extra).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                try {
                                    if (user.getPassWord().equals(stringExtra)) {
                                       tvTenHeadNavigation.setText(user.getTenSV());
                                        tvViTriHeadNavigation.setText(user.getViTri());
                                        Glide.with(NavigationActivity.this).load(user.getImgProfile()).fitCenter().into(imHeadNavigation);
                                        passWord = stringExtra;
                                        maSV = extra;
                                        creatData();
                                    } else {
                                        progressDialog.dismiss();
                                        startLogin();
                                    }
                                } catch (NullPointerException e) {
                                    progressDialog.dismiss();
                                    startLogin();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                android.util.Log.e("faker", "onCancelled");
                            }
                        });
            } catch (NullPointerException e) {
            }
        } else {
            progressDialog.dismiss();
        }

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
    private FragmentTransaction fragmentTransaction;
    private YeuCauFragment yeuCauFragment;
    private FloatingActionButton floatingActionButton;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (isOnline()) {
            contenView=item.getItemId();
            navigationView.setCheckedItem(item.getItemId());
            switch (item.getItemId()) {
                case R.id.mn_nguoi_tim_xe:
                    toolbar.setTitle("Tìm xe");
                    viewFlipper.setDisplayedChild(1);
                    mapManager.setTimXe();
                    break;
                case R.id.mn_xe_tim_nguoi:
                    toolbar.setTitle("Tìm người");
                    viewFlipper.setDisplayedChild(1);
                    mapManager.setTimNguoi();
                    break;
                case R.id.mn_user:
                    toolbar.setTitle("Thông tin cá nhân");
                    viewFlipper.setDisplayedChild(0);
                    myInforFragment = new MyInforFragment();
                    database.child("users").child(maSV).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    try {
                                        if (user.getPassWord().equals(passWord)) {
                                            myInforFragment.setTextInfor(user.getTenSV(), user.getMaSV(), user.getTenLopDL(), user.getSoDT(), user.getViTri());
                                            myInforFragment.setProImage(user.getImgProfile());
                                            tvTenHeadNavigation.setText(user.getTenSV());
                                            tvViTriHeadNavigation.setText(user.getViTri());
                                        } else {
                                            startLogin();
                                        }
                                    } catch (NullPointerException e) {
                                        startLogin();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    android.util.Log.e("faker", "onCancelled");
                                }
                            });
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, myInforFragment).commitAllowingStateLoss();
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

                    break;
                case R.id.mn_home:
                    viewFlipper.setDisplayedChild(0);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_navigation_drawer);
                    toolbar.setTitle("Lời gửi");
                    yeuCauFragment=new YeuCauFragment();
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, yeuCauFragment).commitAllowingStateLoss();
                    break;
                case R.id.mn_map:
                    viewFlipper.setDisplayedChild(1);
                    navigationView.getMenu().clear();
                     navigationView.inflateMenu(R.menu.menu_map);
                    setMap();
                    break;
                case R.id.mn_map_ve_tinh:
                    viewFlipper.setDisplayedChild(1);
                    mapManager.setMapVeTinh();
                    break;
                case R.id.mn_map_giao_thong:
                    viewFlipper.setDisplayedChild(1);
                    mapManager.setMapGiaoThong();
                    break;
                case R.id.mn_map_tim_kiem:

                    break;
            }

            drawer.closeDrawer(GravityCompat.START);
        } else {
            setViewOffLine();
        }

        return true;
    }
    private void setViewOffLine() {
         viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
        viewFlipper.setDisplayedChild(0);
        nullDataFragment = new NullDataFragment();
        fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, nullDataFragment).commit();
    }
    private void callPhone(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
        make(getCurrentFocus(), "mn_share", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    private void setMap() {
        progressDialog.show();
        viewFlipper.setDisplayedChild(1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab_map_my_location);
        floatingActionButton.setOnClickListener(this);
//        toolbar= (Toolbar) findViewById(R.id.test_tb);
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.test);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
        mapFragment.getMapAsync(this);
    }
    private MapManager mapManager;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager= new MapManager(googleMap,this);
        Intent intent=new Intent(this, MyService.class);
        startService(intent);
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_map_my_location:
                mapManager.moveToMyLocation();
                break;
        }
    }
}
