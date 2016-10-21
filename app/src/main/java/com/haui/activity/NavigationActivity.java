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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.haui.log.Log;
import com.haui.map.MapManager;
import com.haui.object.User;

import static android.support.design.widget.Snackbar.make;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
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
    private ProgressDialog dialognoti;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialognoti = new ProgressDialog(this, android.R.style.Theme_Material_Light_Dialog);
        dialognoti.setMessage("Đang khởi tạo dữ liệu...");
        dialognoti.setCancelable(false);
        dialognoti.show();
        reQuestPermistion();
        creatView();
        checkLogin(log.getID(), log.getPass());
    }
    private void creatView() {
        setContentView(R.layout.activity_navigation);
        log = new Log(this);
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
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void reQuestPermistion() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_WIFI_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CHANGE_WIFI_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 2);
            }
        }

    }
    private StorageReference mStorageRef;
    private void checkLogin(String extra, String stringExtra) {
        if (isOnline()) {
            try {
                if (stringExtra.isEmpty() || extra.isEmpty()) {
                    startLogin();
                } else {
                    login(extra, stringExtra);
                }
            } catch (NullPointerException e) {
                android.util.Log.e("faker", "checkLogin");
                startLogin();
            }
        } else {
            setViewOffLine();
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
//                dialognoti.setTitle("Đang thay đổi");
//                dialognoti.show();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();/
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
//                Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
                upAndGetUrlImageProfile(file);
            } else if (resultCode == RESULT_OK && requestCode == 1010) {
                Uri file = data.getData();
//                dialognoti.setTitle("Đang thay đổi");
//                dialognoti.show();
//                android.util.Log.e("faker", selectedImage.toString());
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
                upAndGetUrlImageProfile(file);
            }
        }else {
            setViewOffLine();
        }


    }

    private void upAndGetUrlImageProfile(Uri file) {
        StorageReference riversRef = mStorageRef.child("images/"+"image_"+maSV);
        dialognoti.setMessage("Đang thay đổi");
        dialognoti.setCancelable(true);
        dialognoti.show();
        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        upDateUser("imgProfile",downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }

    private void upDateUser(String item, final String valuse) {
        database.child("users").child(maSV).child(item).setValue(valuse, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    dialognoti.dismiss();
                    Toast.makeText(NavigationActivity.this, "Lỗi hãy thử lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NavigationActivity.this, "Đã thay đổi", Toast.LENGTH_SHORT).show();
                    myInforFragment.setProImage(valuse);
                    dialognoti.dismiss();
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
        reQuestPermistion();
        if (isOnline()) {
            Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1001);
        } else {
            final Snackbar snackbar = Snackbar.make(nullDataFragment.getTextView(), "Vui lòng bật kết nối internet!", Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimaryDark));
            snackbar.setAction("Bật wifi", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (!wifiManager.isWifiEnabled()) {
                        wifiManager.setWifiEnabled(true);
                    } else {

                    }

                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private void creatData() {
        navigationView.setCheckedItem(R.id.mn_nguoi_tim_xe);
        setMap();
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
                                        android.util.Log.e("faker", user.toString());
                                        passWord = stringExtra;
                                        maSV = extra;

                                        creatData();
                                    } else {
                                        dialognoti.dismiss();
                                        startLogin();
                                    }
                                } catch (NullPointerException e) {
                                    dialognoti.dismiss();
                                    startLogin();
                                    android.util.Log.e("faker", "login1");
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
            dialognoti.dismiss();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (isOnline()) {
            contenView=item.getItemId();
            viewFlipper.setDisplayedChild(0);
            switch (item.getItemId()) {
                case R.id.mn_nguoi_tim_xe:
                    toolbar.setTitle("Tìm xe");
                    setMap();
                    break;
                case R.id.mn_xe_tim_nguoi:
                    toolbar.setTitle("Tìm người");
                    setMap();
                    break;
                case R.id.mn_user:
                    toolbar.setTitle("Thông tin cá nhân");
                    myInforFragment = new MyInforFragment();
                    database.child("users").child(maSV).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    try {
                                        if (user.getPassWord().equals(passWord)) {
                                            myInforFragment.setTextInfor(user.getTenSV(), user.getMaSV(), user.getTenLopDL(), user.getSoDT(), user.getImgProfile());
                                            myInforFragment.setProImage(user.getImgProfile());
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
                    ft.replace(R.id.fragment, myInforFragment).commit();
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
            }
        } else {
            setViewOffLine();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setViewOffLine() {
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
        viewFlipper.setDisplayedChild(0);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        nullDataFragment = new NullDataFragment();
        ft.replace(R.id.fragment, nullDataFragment).commit();
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
        viewFlipper.setDisplayedChild(1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        dialognoti.dismiss();
       new MapManager(googleMap,this);
    }
}
