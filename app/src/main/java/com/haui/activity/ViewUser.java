package com.haui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haui.object.User;

/**
 * Created by Duong on 10/27/2016.
 */

public class ViewUser extends AppCompatActivity implements View.OnClickListener{
    public static final String KEY_ID = "msv";
    private ImageView imageView;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_user_infor);
        database = FirebaseDatabase.getInstance().getReference();
        initView();
    }
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
    }
    private String sdt;
    private void initView( ) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUser);
        toolbar.setTitle("Thông tin sinh viên");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_close);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });
        progressBar= (ProgressBar) findViewById(R.id.pg_profile);
        floatingActionButton= (FloatingActionButton) findViewById(R.id.fbt_my_infor);
        collapsingToolbar =(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageView = (ImageView)findViewById(R.id.im_img_profile);
        showProgress();
        floatingActionButton.setOnClickListener(this);
        Intent intent=getIntent();
        String s=intent.getStringExtra(KEY_ID);
        database.child("users").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if (user!=null){
                    sdt=user.getSoDT();
                    setTextInfor(user.getTenSV(),user.getMaSV(),user.getTenLopDL(),user.getSoDT(),user.getViTri());
                    setProImage(user.getImgProfile());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    private void callPhone(String s) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }
    public void setTextInfor(final String tv_infor_ten, String tv_infor_masv, String tv_infor_lop,
                             final String tv_infor_sdt,String tenVitTri) {
        ((TextView) findViewById(R.id.tv_infor_ten)).setText(tv_infor_ten);
        ((TextView) findViewById(R.id.tv_infor_masv)).setText(tv_infor_masv);
        ((TextView) findViewById(R.id.tv_infor_lop)).setText(tv_infor_lop);
        ((TextView) findViewById(R.id.tv_infor_sdt)).setText(tv_infor_sdt);
        ((TextView) findViewById(R.id.tv_infor_vitri)).setText(tenVitTri);
        hideProgress();
    }
    public void setProImage(String proImage) {
        if (!proImage.isEmpty()){
            Glide.with(this).load(proImage).fitCenter().into(imageView);
            hideProgress();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fbt_my_infor:
                callPhone(sdt);
                break;
        }
    }
    @Override
    public boolean onPrepareOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
