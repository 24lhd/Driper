package com.haui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.object.User;

/**
 * Created by Duong on 10/19/2016.
 */

public class MyInforFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView imageView;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.layout_my_infor,container,false);
        Log.e("faker L","khoi tao");
        initView();
        return view;
    }
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
    }
    private NavigationActivity navigationActivity;
    private CollapsingToolbarLayout collapsingToolbar;
    private void initView( ) {
        progressBar= (ProgressBar) view.findViewById(R.id.pg_profile);
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.fbt_my_infor);
         collapsingToolbar =(CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
         imageView = (ImageView) view.findViewById(R.id.im_img_profile);
        navigationActivity= (NavigationActivity) getActivity();
        navigationActivity.registerForContextMenu(floatingActionButton);
        showProgress();
        floatingActionButton.setOnClickListener(this);
        navigationActivity.getDatabase().child("users").child(navigationActivity.getLog().getID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                navigationActivity.getDatabase().child("users").child(navigationActivity.getLog().getID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        navigationActivity.setViewHeader(user);
                        setProImage(user.getImgProfile());
                        setTextInfor(user.getTenSV(), user.getMaSV(), user.getTenLopDL(), user.getSoDT(), user.getViTri());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                navigationActivity.getDatabase().child("users").child(navigationActivity.getLog().getID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        navigationActivity.setViewHeader(user);
                        setProImage(user.getImgProfile());
                        setTextInfor(user.getTenSV(), user.getMaSV(), user.getTenLopDL(), user.getSoDT(), user.getViTri());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

    public void from_gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        navigationActivity.startActivityForResult(intent, 1011);
    }

    public void from_camera() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        navigationActivity.startActivityForResult(cameraIntent, 1010);
    }
    public void setTextInfor(final String tv_infor_ten, String tv_infor_masv, String tv_infor_lop, final String tv_infor_sdt,String tenVitTri) {
        ((TextView) view.findViewById(R.id.tv_infor_ten)).setText(tv_infor_ten);
        ((TextView) view.findViewById(R.id.tv_infor_masv)).setText(tv_infor_masv);
        ((TextView) view.findViewById(R.id.tv_infor_lop)).setText(tv_infor_lop);
        ((TextView) view.findViewById(R.id.tv_infor_sdt)).setText(tv_infor_sdt);
        ((TextView) view.findViewById(R.id.tv_infor_vitri)).setText(tenVitTri);
        hideProgress();
//        collapsingToolbar.setTitle(tv_infor_ten);
    }

    public void setProImage(String proImage) {
        if (!proImage.isEmpty()){
            Glide.with(navigationActivity).load(proImage).fitCenter().into(imageView);
            hideProgress();
        }else {
            imageView.setImageResource(R.drawable.add_im);
            hideProgress();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fbt_my_infor:
                navigationActivity.openContextMenu(floatingActionButton);
            break;
        }
    }

    public void updateInfor() {
    }

    public void updatePass() {
    }

    public void deleteAcc() {
    }



}
