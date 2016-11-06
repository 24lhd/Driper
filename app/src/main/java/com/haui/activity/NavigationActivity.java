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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import com.haui.fragment.NguoiTimXeFragment;
import com.haui.fragment.NullDataFragment;
import com.haui.fragment.XeTimNguoiFragment;
import com.haui.log.Log;
import com.haui.map.MapManager;
import com.haui.object.Location;
import com.haui.object.NguoiTimXe;
import com.haui.object.User;
import com.haui.object.XeTimNguoi;
import com.haui.service.MyService;

import java.util.ArrayList;

import static android.support.design.widget.Snackbar.make;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        View.OnClickListener {
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
    private CuaToiFragment cuaToiFragment;
    private XeTimNguoiFragment xeTimNguoiFragment;
    private NguoiTimXeFragment nguoiTimXeFragment;
    private FragmentTransaction fragmentTransaction;
    private FloatingActionButton floatingActionButton;
    private ArrayList<NguoiTimXe> arrNguoiTimXes;
    private ArrayList<XeTimNguoi> arrXeTimNguois;
    private View viewMapMenu;

    public View getViewMapMenu() {
        return viewMapMenu;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogAndShow("Đang khởi tạo....");
        Intent intent=new Intent(this, MyService.class);
        startService(intent);
        creatView();
        checkLogin("","");
    }

    private void createNullView() {
          myInforFragment=null;
          viewPageYeuCau=null;
          cuaToiFragment=null;
          xeTimNguoiFragment=null;
          nguoiTimXeFragment=null;
          fragmentTransaction=null;
          floatingActionButton=null;
         arrNguoiTimXes=null;
          arrXeTimNguois=null;
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
        Intent intent=new Intent(this, MyService.class);
        stopService(intent);
        createNullView();
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
    /**
     * đăng nhập hệ thống
     * @param masv mã sinh viên
     * @param pass mật khẩu
     */
    private void login(final String masv, final String pass) {
        if (isOnline()) {
            contenView=1000;
            try {
                passWord=pass;
                maSV=masv;
                //https://hauiclass.firebaseio.com/users/094120041
                database.child("users").child(maSV).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                try{
                                if (user.getPassWord().equals(passWord)) {
                                    contenView=R.id.mn_home;
                                    setViewHeader(user);
                                    viewHome();
                                    progressDialog.dismiss();
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
                            }});
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
    @Override
    public boolean onPrepareOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (isOnline()) {
            contenView=item.getItemId();
            toolbar.setVisibility(View.VISIBLE);
            switch (item.getItemId()) {
                case R.id.mn_nguoi_quanh_day:
                    toolbar.setVisibility(View.GONE);
                    toolbar.setTitle("Tìm xe");
                    mapManager.setHienNguoi();
                    break;
                case R.id.mn_xe_om_quanh_day:
                    toolbar.setVisibility(View.GONE);
                    toolbar.setTitle("Tìm người");
                    mapManager.setHienXe();
                    break;
                case R.id.mn_user:
                    toolbar.setTitle("Thông tin cá nhân");
                    viewFlipper.setDisplayedChild(0);
                    myInforFragment = new MyInforFragment();
                    fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, myInforFragment).commit();
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
                    toolbar.setVisibility(View.GONE);
                    initDialogAndShow("Đang khỏi tạo dữ liệu....");
                    viewFlipper.setDisplayedChild(1);
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.menu_map);
                    setMap();
                    break;
                case R.id.mn_map_ve_tinh:
                    toolbar.setVisibility(View.GONE);
                    mapManager.setMapVeTinh();
                    break;
                case R.id.mn_map_giao_thong:
                    toolbar.setVisibility(View.GONE);
                    mapManager.setMapGiaoThong();
                    break;
                case R.id.mn_tai_xe:
                    toolbar.setVisibility(View.GONE);
                    createDialogTimNguoi(null);
                    break;
                case R.id.mn_sinh_vien:
                  createDialogTimXe(null);
                    break;
            }
            navigationView.setCheckedItem(contenView);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setViewOffLine();
        }

        return true;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
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
            case R.id.view_menu:
                getMenuInflater().inflate(R.menu.mn_dang_yeu_cau,menu);
                break;
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    public void writeNewYeuCauTimNguoi(Location location, String viTri, String maSV, String bsx, String thongDiep) {
        database.child("XeTimNguoi").child(maSV).setValue(new XeTimNguoi(location, viTri, maSV, bsx, thongDiep), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    Toast.makeText(NavigationActivity.this, "Đăng thành công", Toast.LENGTH_SHORT).show();

                    dialogYeuCau.dismiss();
                }else{
                    Toast.makeText(NavigationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    dialogYeuCau.dismiss();
                }
            }
        }); // gửi một đối tượng lên firebase vói child là những node cha
    }
    public void writeNewYeuCauTimXe(Location location, String viTri, String maSV, String diemDen, String giaTien , String thongDiep) {
        database.child("NguoiTimXe").child(maSV).setValue(new NguoiTimXe(location, viTri, maSV, diemDen, giaTien,thongDiep), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError==null){
                    Toast.makeText(NavigationActivity.this, "Đăng thành công", Toast.LENGTH_SHORT).show();

                    dialogYeuCau.dismiss();
                }else{
                    Toast.makeText(NavigationActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    dialogYeuCau.dismiss();
                }
            }
        }); // gửi một đối tượng lên firebase vói child là những node cha
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
            case R.id.mn_xem_thongtin:
                if (mapManager.getMarkerCick().getTag() instanceof XeTimNguoi){
                    XeTimNguoi xeTimNguoi= (XeTimNguoi) mapManager.getMarkerCick().getTag();
                    Intent intent=new Intent(this, ViewUser.class);
                    intent.putExtra(ViewUser.KEY_ID,xeTimNguoi.getMaSV());
                    this.startActivity(intent);
                }else if (mapManager.getMarkerCick().getTag() instanceof NguoiTimXe){
                    NguoiTimXe nguoiTimXe= (NguoiTimXe) mapManager.getMarkerCick().getTag();
                    Intent intent=new Intent(this, ViewUser.class);
                    intent.putExtra(ViewUser.KEY_ID,nguoiTimXe.getMaSV());
                    this.startActivity(intent);
                }else{
                    Snackbar.make(navigationView,""+mapManager.getMarkerCick().getSnippet(),Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.mn_chi_duong_di_bo:
                String walking="walking";
                chiDuong(walking);
                return true;
            case R.id.mn_chi_duong_oto:
                String driving="driving";
                chiDuong(driving);
                return true;
            case R.id.mn_chi_duong_xe_bus:
                String transit="transit";
                chiDuong(transit);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void chiDuong(String walking) {
        android.location.Location location=new android.location.Location("a");
        if (mapManager.getMarkerCick().getTag() instanceof XeTimNguoi){
            XeTimNguoi xeTimNguoi= (XeTimNguoi) mapManager.getMarkerCick().getTag();
            location.setLatitude(Double.parseDouble(xeTimNguoi.getLocation().getLat()));
            location.setLongitude(Double.parseDouble(xeTimNguoi.getLocation().getLng()));
        }else if (mapManager.getMarkerCick().getTag() instanceof NguoiTimXe){
            NguoiTimXe nguoiTimXe= (NguoiTimXe) mapManager.getMarkerCick().getTag();
            location.setLatitude(Double.parseDouble(nguoiTimXe.getLocation().getLat()));
            location.setLongitude(Double.parseDouble(nguoiTimXe.getLocation().getLng()));
        }else{
            location.setLatitude( mapManager.getMarkerCick().getPosition().latitude);
            location.setLongitude( mapManager.getMarkerCick().getPosition().longitude);
        }
        mapManager.drawRoadByLocation(mapManager.getMyLocation(),location,walking,getResources().getColor(R.color.colorPrimary),5);
    }

    public void createDialogTimNguoi(XeTimNguoi xeTimNguoi) {
        dialogYeuCau=new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog_Alert);
        View v=getLayoutInflater().inflate(R.layout.dialog_tim_nguoi,null);
        final EditText etLoiNhan= (EditText) v.findViewById(R.id.et_dl_tim_nguoi_loi_nhan);
        final EditText etBSX= (EditText) v.findViewById(R.id.et_dl_tim_nguoi_bsx);
        final ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.pb_dl_tim_nguoi);
        final AppCompatButton btDang= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_nguoi_dang);
        final TextView textView= (TextView) v.findViewById(R.id.tv_notidl_tim_nguoi);
        final AppCompatButton btHuy= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_nguoi_huy);
        if (xeTimNguoi instanceof XeTimNguoi){
            etLoiNhan.setText(xeTimNguoi.getThongDiep());
            etBSX.setText(xeTimNguoi.getBsx());
        }
        btDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etBSX.getText().toString().isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("* Không được để trống...");
                }else{
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("* đang...");
                    btDang.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    btHuy.setVisibility(View.GONE);
                    writeNewYeuCauTimNguoi(new Location(""+mapManager.getMyLocation().getLatitude(),
                            ""+mapManager.getMyLocation().getLongitude()),mapManager.getNameByLocation(mapManager.getMyLocation().getLatitude(),mapManager.getMyLocation().getLongitude()),maSV,etBSX.getText().toString(),etLoiNhan.getText().toString());
                }
            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogYeuCau.dismiss();
            }
        });
        dialogYeuCau.setCanceledOnTouchOutside(false);
        dialogYeuCau.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogYeuCau.setContentView(v);
        dialogYeuCau.show();
    }
    public void createDialogTimXe(NguoiTimXe nguoiTimXe) {
        dialogYeuCau=new Dialog(this,android.R.style.Theme_DeviceDefault_Dialog_Alert);
        View v=getLayoutInflater().inflate(R.layout.dialog_tim_xe,null);
        final EditText etDiemDi= (EditText) v.findViewById(R.id.et_dl_tim_xe_diem_di);
        etDiemDi.setImeOptions(EditorInfo.IME_ACTION_DONE);
        final EditText etDiemDen= (EditText) v.findViewById(R.id.et_dl_tim_xe_diem_den);
        final EditText etGiaTien= (EditText) v.findViewById(R.id.et_dl_tim_xe_gia_tien);
        final EditText etLoiNhan= (EditText) v.findViewById(R.id.et_dl_tim_xe_loi_nhan);
        final ProgressBar progressBar= (ProgressBar) v.findViewById(R.id.pb_dl_tim_xe);
        final TextView textView= (TextView) v.findViewById(R.id.tv_notidl_tim_xe);
        final AppCompatButton btDang= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_xe_dang);
        final AppCompatButton btHuy= (AppCompatButton) v.findViewById(R.id.bt_dl_tim_xe_huy);
        if (nguoiTimXe instanceof  NguoiTimXe){
            etLoiNhan.setText(nguoiTimXe.getThongDiep());
            etDiemDen.setText(nguoiTimXe.getDiemDen());
            etGiaTien.setText(nguoiTimXe.getGiaTien());
        }
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogYeuCau.dismiss();
            }
        });
        btDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDiemDi.getText().toString().isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("* Không được để trống điểm đi...");
                }else if (etDiemDen.getText().toString().isEmpty()){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("* Không được để trống diểm đến...");
                }else{
                    btDang.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    btHuy.setVisibility(View.GONE);
                    writeNewYeuCauTimXe(new Location(""+mapManager.getMyLocation().getLatitude(),
                            ""+mapManager.getMyLocation().getLongitude()),
                            mapManager.getNameByLocation(mapManager.getMyLocation().getLatitude(),mapManager.getMyLocation().getLongitude()),maSV,etDiemDen.getText().toString(),etGiaTien.getText().toString(),etLoiNhan.getText().toString());
                }
            }
        });

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
             viewMapMenu=findViewById(R.id.view_menu);
            mapFragment.getMapAsync(this);
        }else {
            progressDialog.dismiss();
        }
    }
    private MapManager mapManager;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapManager= new MapManager(googleMap,this);

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

    public Log getLog() {
        if (!log.getID().equals(maSV)){
            startLogin();
        }
        return log;
    }

    public DatabaseReference getDatabase() {
        return database;
    }
    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }

    public void setArrXeTimNguois(ArrayList<XeTimNguoi> arrXeTimNguois) {
        this.arrXeTimNguois = arrXeTimNguois;
        if (contenView==R.id.mn_xe_om_quanh_day){
            mapManager.setHienXe();
        }
    }
    public ArrayList<XeTimNguoi> getArrXeTimNguois() {
        return arrXeTimNguois;
    }
    public ArrayList<NguoiTimXe> getArrNguoiTimXes() {
        return arrNguoiTimXes;
    }

    public void setArrNguoiTimXes(ArrayList<NguoiTimXe> arrNguoiTimXes) {
        this.arrNguoiTimXes = arrNguoiTimXes;
        if (contenView==R.id.mn_nguoi_quanh_day){
            mapManager.setHienNguoi();
        }
    }
    private void viewHome() {
        if (isOnline()){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_navigation_drawer);
            viewFlipper.setDisplayedChild(2);
            toolbar.setTitle("Lời gửi");
              cuaToiFragment=new CuaToiFragment();
              xeTimNguoiFragment =new XeTimNguoiFragment();
              nguoiTimXeFragment =new NguoiTimXeFragment();
            if (viewPageYeuCau==null){
                viewPageYeuCau= (ViewPager) findViewById(R.id.viewpager_yeucau);
                tabLayoutYeuCau = (TabLayout) findViewById(R.id.tab_yeucau);
                arrXeTimNguois =new ArrayList<>();
                arrNguoiTimXes =new ArrayList<>();
            }
            if (tabLayoutYeuCau != null) {
                tabLayoutYeuCau.setTabMode(TabLayout.MODE_FIXED);
                tabLayoutYeuCau.setBackgroundColor(Color.WHITE);
                tabLayoutYeuCau.setSelectedTabIndicatorHeight(10);
                tabLayoutYeuCau.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
                tabLayoutYeuCau.setTabTextColors( getResources().getColor(R.color.md_blue_grey_300),getResources().getColor(R.color.colorPrimary));
            }
            viewPageYeuCau.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    switch (position){
                        case 2:
                            return cuaToiFragment;
                        case 1:
                            return xeTimNguoiFragment;
                        case 0:
                        default:
                        return nguoiTimXeFragment;
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
            progressDialog.dismiss();
        }else {
            progressDialog.dismiss();
            setViewOffLine();
        }
    }
    public void setViewHeader(User viewHeader) {
        tvTenHeadNavigation.setText(viewHeader.getTenSV());
//        tvViTriHeadNavigation.setText(viewHeader.getViTri());
        if (!viewHeader.getImgProfile().toString().isEmpty()){
            Glide.with(NavigationActivity.this).load(viewHeader.getImgProfile()).fitCenter().into(imHeadNavigation);
        }
    }
    public void setViewHeaderViTri(String viewHeader) {
        tvViTriHeadNavigation.setText(viewHeader);
    }
}
