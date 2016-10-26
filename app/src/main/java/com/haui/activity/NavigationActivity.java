package com.haui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.haui.fragment.CuaToiFragment;
import com.haui.fragment.MyInforFragment;
import com.haui.fragment.NullDataFragment;
import com.haui.fragment.TimNguoiFragment;
import com.haui.fragment.TimXeFragment;
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
    private StorageReference mStorageRef;
    private ViewPager viewPageYeuCau;
    private TabLayout tabLayoutYeuCau;
    private Dialog dialogYeuCau;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent=new Intent(this, MyService.class);
            stopService(intent);
        }catch (Exception e){

        }
        initDialogAndShow("Đang khởi tạo....");
        creatView();
        checkLogin("","");
    }

    /**
     * khỏi tạo dialog thông báo
     * @param s thông điệp muốn hiển thị
     */
    private void initDialogAndShow(String s) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(s);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    /**
     * khỏi tạo các view cần thiết trong ứng dụng
     */
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
    /**
     * kiểm tra đăng nhập
     *
     * @param maSV mã sinh viên
     * @param pass mật khẩu đăng nhập
     *
     */
    public void checkLogin(String maSV, String pass) {
        if (isOnline()) {
            try {
                if (!log.getID().isEmpty()||!log.getPass().isEmpty()) {
                    login(log.getID(), log.getPass());
                } else if (!pass.isEmpty()||!maSV.isEmpty()){
                    login(maSV, pass);
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
            showSnackbar();
        }
    }
    private void showSnackbar() {
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

    public void signOut() {
        log.remove();
        startLogin();
    }



    /**
     * kết quả trả về khi bật 1 activity mới và yêu cầu 1 kết quả trả về
     * @param requestCode mã code yêu cầu kết quả trả về
     * @param resultCode mã code kết quả trả về
     * @param data kết quả được trả về
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isOnline()){
            if (requestCode == 1001) {
                if (resultCode == RESULT_OK) {
                    initDialogAndShow("Đang khởi tạo dữ liệu....");
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

    /**
     * gửi ảnh lên stroge firebase
     * @param file là uri của hình ảnh được chọn trong máy
     */
    private void upAndGetUrlImageProfile(Uri file) {
        myInforFragment.showProgress();
        StorageReference riversRef = mStorageRef.child("images/"+"image_"+maSV);// tạo đường dẫn của file ảnh trên database cùng với tên của file
        riversRef.putFile(file) // gưi file lên
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    /**
                     * kết quả trẻ về nếu thành công
                     * @param taskSnapshot
                     * taskSnapshot.getDownloadUrl() lấy đường dẫn url của file ảnh trên firebase
                     */
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        // khi thành công sẽ gọi phương thức update về ảnh đại diện cho user và set ảnh cho view
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

    /**
     * cập nhật lại thông tin người dùng
     * @param item là tên node (đường dẫn cha)
     * @param valuse và giá trị của node con
     */
    public void upDateUser(final String item, final String valuse) {
        database.child("users").child(maSV).child(item).setValue(valuse, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (item.equals("imgProfile")){
                    if (databaseError != null) {
                        myInforFragment.hideProgress();
                    } else {
                        myInforFragment.setProImage(valuse);
                        Glide.with(NavigationActivity.this).load(valuse).fitCenter().into(imHeadNavigation); // load ảnh online và hiển thị lên 1 view dùng thư viện Glide
                    }
                }

            }
        });
    }



    /**
     * kiểm tra online và bật activity đăng nhập
     *  yêu cầu kết quả trả về với mã yêu cầu két quả trả về 1001
     */
    public void startLogin() {
        if (isOnline()) {
            Intent intent = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1001);
        } else {
            setViewOffLine();
        }
    }

    /**
     * kiểm tra trạng thai on hay offline của máy
     * @return true nếu đang online và false nếu đang offline
     */
    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {return false;}
    }

    /**
     * kiểm tra online và
     * khỏi tạo đữ liệu đầu tiên sau khi đăng nhập thành công
     *
     */
    private TimXeFragment timXeFragment;
    private TimNguoiFragment timNguoiFragment;
    private CuaToiFragment  cuaToiFragment;
    private void viewHome() {
        if (isOnline()){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_navigation_drawer);
            viewFlipper.setDisplayedChild(2);
            toolbar.setTitle("Lời gửi");
            if (viewPageYeuCau==null){
                timXeFragment=new TimXeFragment();
                timNguoiFragment=new TimNguoiFragment();
                cuaToiFragment=new CuaToiFragment();
                viewPageYeuCau= (ViewPager) findViewById(R.id.viewpager_yeucau);
                tabLayoutYeuCau = (TabLayout) findViewById(R.id.tab_yeucau);
            }
            if (tabLayoutYeuCau != null) {
                tabLayoutYeuCau.setTabMode(TabLayout.MODE_FIXED);
                tabLayoutYeuCau.setBackgroundColor(Color.WHITE);
                tabLayoutYeuCau.setTabTextColors( getResources().getColor(R.color.md_blue_grey_300),getResources().getColor(R.color.md_blue_500));
                tabLayoutYeuCau.setSelectedTabIndicatorHeight(0);
                tabLayoutYeuCau.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
//                tabLayoutYeuCau.getTabAt(0).setText("Tìm xe");
//                tabLayoutYeuCau.getTabAt(1).setText("Tìm người");
//                tabLayoutYeuCau.getTabAt(2).setText("Của tôi");

            }
            viewPageYeuCau.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                                          @Override
                                          public Fragment getItem(int position) {
                                              switch (position){
                                                  case 0:

                                                      return timXeFragment;
                                                  case 1:

                                                      return timNguoiFragment;
                                                  case 2:

                                                  default:
                                                      return cuaToiFragment;
                                              }
                                          }
                                          @Override
                                          public int getCount() {
                                              return 3;
                                          }

                @Override
                public CharSequence getPageTitle(int position) {
                    switch (position){
                        case 0:
                            return "Tìm xe";
                        case 1:
                            return "Tìm người";
                        case 2:
                        default:
                            return "Của tôi";
                    }
                }
            });
            tabLayoutYeuCau.setupWithViewPager(viewPageYeuCau);
//            yeuCauFragment=new YeuCauFragment();
//            fragmentTransaction=getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fragment, yeuCauFragment).commitAllowingStateLoss();
            progressDialog.dismiss();
        }else {
            progressDialog.dismiss();
            setViewOffLine();
         }
    }
    /**
     * đăng nhập hệ thống
     * @param masv mã sinh viên
     * @param pass mật khẩu
     */
    private void login(final String masv, final String pass) {
        if (isOnline()) {
            try {
                database.child("users").child(masv).addListenerForSingleValueEvent( // kiểm tra theo từng node con node users -> node masv rồi đến các node con của msv
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class); //láy 1 đối tượng tương ứng vs node con được trả về
                                try {// nếu k lấy được giá trị hoặc giá trị đó k lấy kết quả trả về = null
                                    /**kiểm tra mật khẩu
                                     * đúng thì khởi tạo dữ liệu đầu tiên
                                     * sai thì bật lại activity đăng nhập
                                     */
                                    if (user.getPassWord().equals(pass)) {
                                       tvTenHeadNavigation.setText(user.getTenSV());
                                        tvViTriHeadNavigation.setText(user.getViTri());
                                        Glide.with(NavigationActivity.this).load(user.getImgProfile()).fitCenter().into(imHeadNavigation);
                                        passWord = pass;
                                         maSV=masv;
                                        viewHome();
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
            setViewOffLine();
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
    private FloatingActionButton floatingActionButton;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (isOnline()) {
            contenView=item.getItemId();
            switch (item.getItemId()) {
                case R.id.mn_nguoi_tim_xe:
                    toolbar.setTitle("Tìm xe");
                    mapManager.setTimXe();
                    break;
                case R.id.mn_xe_tim_nguoi:
                    toolbar.setTitle("Tìm người");
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
                    viewHome();
                    break;
                case R.id.mn_map:
                    initDialogAndShow("Đang khỏi tạo dữ liệu....");
                    viewFlipper.setDisplayedChild(1);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_map);
                    setMap();
                    break;
                case R.id.mn_map_ve_tinh:
                    mapManager.setMapVeTinh();
                    break;
                case R.id.mn_map_giao_thong:
                    mapManager.setMapGiaoThong();
                    break;
                case R.id.mn_map_tim_kiem:

                    break;
                case R.id.mn_dang_bai:
                    registerForContextMenu(floatingActionButton);
                    openContextMenu(floatingActionButton);
                    break;
            }
            navigationView.setCheckedItem(contenView);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setViewOffLine();
        }

        return true;
    }
    /**
     * khỏi tạo dialog menu
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.fbt_my_infor:
                getMenuInflater().inflate(R.menu.menu_select_image,menu);
                break;
            case R.id.fab_map_my_location:
                getMenuInflater().inflate(R.menu.mn_dang_yeu_cau,menu);
                break;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    /**
     * sẽ được gọi khi 1 item men được click
     * @param item item menu được cick
     * @return true
     */
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
            case R.id.mn_yc_tim_xe:
                createDialogTimXe();
                return true;
            case R.id.mn_yc_tim_nguoi:
                createDialogTimNguoi();
                return true;

        }
        return super.onContextItemSelected(item);
    }

    private void createDialogTimNguoi() {
        View v=getLayoutInflater().inflate(R.layout.dialog_tim_nguoi,null);
        EditText etLoiNhan= (EditText) v.findViewById(R.id.et_dl_tim_nguoi_loi_nhan);
        EditText etBSX= (EditText) v.findViewById(R.id.et_dl_tim_nguoi_bsx);
        AppCompatButton btDang= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_nguoi_dang);
        AppCompatButton btHuy= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_nguoi_huy);
        dialogYeuCau.setCanceledOnTouchOutside(false);
        dialogYeuCau.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogYeuCau.setContentView(v);
        dialogYeuCau.show();
    }
    private void createDialogTimXe() {
        View v=getLayoutInflater().inflate(R.layout.dialog_tim_xe,null);
        EditText etDiemDi= (EditText) v.findViewById(R.id.et_dl_tim_xe_diem_di);
        EditText etDiemDen= (EditText) v.findViewById(R.id.et_dl_tim_xe_diem_den);
        EditText etGiaTien= (EditText) v.findViewById(R.id.et_dl_tim_xe_gia_tien);
        EditText etLoiNhan= (EditText) v.findViewById(R.id.et_dl_tim_xe_loi_nhan);
        AppCompatButton btDang= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_xe_dang);
        AppCompatButton btHuy= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_xe_huy);
        dialogYeuCau.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogYeuCau.setCanceledOnTouchOutside(false);
        dialogYeuCau.setContentView(v);
        dialogYeuCau.show();
    }


    /**
     * khởi tạo fragment offline
     *
     */
    private void setViewOffLine() {
         viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
        viewFlipper.setDisplayedChild(0);  // hiện lại view 0
        nullDataFragment = new NullDataFragment();
        fragmentTransaction=getSupportFragmentManager().beginTransaction(); // lấy dối tương quản lý các fragment
        fragmentTransaction.replace(R.id.fragment, nullDataFragment).commit(); // ghi đè lên fragment hiện tại
    }

    /**
     * tạo 1 cuộc gọi điện thoại
     * @param s số điện thoại
     */
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

    /**
     * khởi tạo bản đồ
     */
    private void setMap() {
        viewFlipper.setDisplayedChild(1);
        if (mapManager==null){
            dialogYeuCau=new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog_Alert);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            floatingActionButton= (FloatingActionButton) findViewById(R.id.fab_map_my_location); // FAB vị trí hiện tại
            floatingActionButton.setOnClickListener(this);
//        toolbar= (Toolbar) findViewById(R.id.test_tb);
//        toolbar.getMenu().clear();
//        toolbar.inflateMenu(R.menu.test);
//        toolbar.setNavigationIcon(android.R.drawable.ic_delete);
            mapFragment.getMapAsync(this);
        }else {
            progressDialog.dismiss();
        }
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
            case R.id.mn_dang_bai:
                openContextMenu(v);
                break;


        }
    }
}
