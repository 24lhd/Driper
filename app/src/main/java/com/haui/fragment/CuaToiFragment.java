package com.haui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.object.NguoiTimXe;
import com.haui.object.XeTimNguoi;

/**
 * Created by Duong on 10/25/2016.
 */

public class CuaToiFragment extends Fragment implements View.OnClickListener{
    private View view;
    private NavigationActivity navigationActivity;
    private TextView tvThongDiepTimNguoi;
    private TextView tvMaSVTimNguoi;
    private TextView tvBSXTimNguoi;
    private TextView tvViTriTimNguoi;
    private TextView tvThongDiepTimXe;
    private TextView tvMaSVTimXe;
    private TextView tvDiemDenTimXe;
    private TextView tvViTriTimXe;
    private TextView tvGiaTienTimXe;
    private com.haui.log.Log log;
    private CardView card1,card2;
    private XeTimNguoi xeTimNguoi;
    private NguoiTimXe nguoiTimXe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.yeu_cau_cua_toi_fragment_layout,container,false);
        initView();
        return view;
    }

    private void initView() {
        card1= (CardView) view.findViewById(R.id.cv_1);
        card2= (CardView) view.findViewById(R.id.cv_2);
        this.tvThongDiepTimNguoi = (TextView) view.findViewById(R.id.tv_item_tim_nguoi_thong_diep);
        this.tvMaSVTimNguoi = (TextView) view.findViewById(R.id.tv_item_tim_nguoi_msv);
        this.tvBSXTimNguoi = (TextView) view.findViewById(R.id.tv_item_tim_nguoi_bsx);
        this.tvViTriTimNguoi = (TextView) view.findViewById(R.id.tv_item_tim_nguoi_vi_tri);
        navigationActivity= (NavigationActivity) getActivity();
        log=navigationActivity.getLog();
        navigationActivity.getDatabase().child("XeTimNguoi").child(log.getID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                         xeTimNguoi =dataSnapshot.getValue(XeTimNguoi.class);
                        if (xeTimNguoi !=null){

                            tvThongDiepTimNguoi.setText(xeTimNguoi.getThongDiep());
                            tvMaSVTimNguoi.setText(xeTimNguoi.getMaSV());
                            tvBSXTimNguoi.setText(xeTimNguoi.getBsx());
                            tvViTriTimNguoi.setText(xeTimNguoi.getViTri());
                            navigationActivity.setViewHeaderViTri(xeTimNguoi.getViTri());
                        }else {
                            card1.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        navigationActivity.getDatabase().child("XeTimNguoi").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                 xeTimNguoi = (XeTimNguoi) dataSnapshot.getValue(XeTimNguoi.class);
                if (xeTimNguoi.getMaSV().equals(log.getID())){
                    tvThongDiepTimNguoi.setText(xeTimNguoi.getThongDiep());
                    tvMaSVTimNguoi.setText(xeTimNguoi.getMaSV());
                    tvBSXTimNguoi.setText(xeTimNguoi.getBsx());
                    tvViTriTimNguoi.setText(xeTimNguoi.getViTri());
                    navigationActivity.setViewHeaderViTri(xeTimNguoi.getViTri());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                XeTimNguoi xeTimNguoi = (XeTimNguoi) dataSnapshot.getValue(XeTimNguoi.class);
                if (xeTimNguoi.getMaSV().equals(log.getID())){
                    card1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.tvThongDiepTimXe = (TextView) view.findViewById(R.id.tv_item_tim_xe_thong_diep);
        this.tvMaSVTimXe = (TextView) view.findViewById(R.id.tv_item_tim_xe_msv);
        this.tvDiemDenTimXe = (TextView) view.findViewById(R.id.tv_item_tim_xe_diem_den);
        this.tvViTriTimXe = (TextView) view.findViewById(R.id.tv_item_tim_xe_vi_tri);
        this.tvGiaTienTimXe = (TextView) view.findViewById(R.id.tv_item_tim_xe_gia_tien);
        navigationActivity.getDatabase().child("NguoiTimXe").child(log.getID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                         nguoiTimXe =dataSnapshot.getValue(NguoiTimXe.class);
                        if (nguoiTimXe !=null){
                            tvThongDiepTimXe.setText(nguoiTimXe.getThongDiep());
                            tvMaSVTimXe.setText(nguoiTimXe.getMaSV());
                            tvDiemDenTimXe.setText(nguoiTimXe.getDiemDen());
                            tvViTriTimXe.setText(nguoiTimXe.getViTri());
                            tvGiaTienTimXe.setText(nguoiTimXe.getGiaTien());
                            navigationActivity.setViewHeaderViTri(nguoiTimXe.getViTri());
                        }else {
                            card2.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("faker","onCancelled");
                    }
                });

        navigationActivity.getDatabase().child("NguoiTimXe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                 nguoiTimXe =dataSnapshot.getValue(NguoiTimXe.class);
                if (nguoiTimXe.getMaSV().equals(log.getID())){
                    tvThongDiepTimXe.setText(nguoiTimXe.getThongDiep());
                    tvMaSVTimXe.setText(nguoiTimXe.getMaSV());
                    tvDiemDenTimXe.setText(nguoiTimXe.getDiemDen());
                    tvViTriTimXe.setText(nguoiTimXe.getViTri());
                    tvGiaTienTimXe.setText(nguoiTimXe.getGiaTien());
                    navigationActivity.setViewHeaderViTri(nguoiTimXe.getViTri());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                NguoiTimXe nguoiTimXe =dataSnapshot.getValue(NguoiTimXe.class);
                if (nguoiTimXe.getMaSV().equals(log.getID())){
                    card2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_1:
                navigationActivity.createDialogTimNguoi(xeTimNguoi);
                break;
            case R.id.cv_2:
                navigationActivity.createDialogTimXe(nguoiTimXe);
                break;
        }
    }
}
