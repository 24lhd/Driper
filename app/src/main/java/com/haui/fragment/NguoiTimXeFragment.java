package com.haui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.activity.ViewUser;
import com.haui.log.Log;
import com.haui.object.NguoiTimXe;

import java.util.ArrayList;

/**
 * Created by Duong on 10/25/2016.
 */

public class NguoiTimXeFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private NavigationActivity navigationActivity;
    private ArrayList<NguoiTimXe> arrNguoiTimXes;
    private Log log;
    private AdapterTimXe adapterTimXe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.yeu_cau_fragment_layout,container,false);
        initView();
        return view;
    }
    private void initView() {
        navigationActivity= (NavigationActivity) getActivity();
        recyclerView= (RecyclerView) view.findViewById(R.id.rcv_yeucau_fragment);
        recyclerView.setHasFixedSize(true);
        arrNguoiTimXes =new ArrayList<>();
        log=navigationActivity.getLog();
        adapterTimXe = new AdapterTimXe(arrNguoiTimXes);
        recyclerView.setAdapter(adapterTimXe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        navigationActivity.getDatabase().child("NguoiTimXe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                NguoiTimXe nguoiTimXe = (NguoiTimXe) dataSnapshot.getValue(NguoiTimXe.class);
                if (!nguoiTimXe.getMaSV().equals(log.getID())){
                    arrNguoiTimXes.add(nguoiTimXe);
                    navigationActivity.setArrNguoiTimXes(arrNguoiTimXes);
                    adapterTimXe.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                NguoiTimXe nguoiTimXe = (NguoiTimXe) dataSnapshot.getValue(NguoiTimXe.class);
                if (arrNguoiTimXes.size()>0){
                    int i=0;
                    for (int j = 0; j < arrNguoiTimXes.size() ; j++) {
                        if (arrNguoiTimXes.get(j).getMaSV().equals(nguoiTimXe.getMaSV())){
                            i=j;
                            break;
                        }
                    }
                    arrNguoiTimXes.set(i, nguoiTimXe);
                    navigationActivity.setArrNguoiTimXes(arrNguoiTimXes);
                    adapterTimXe.notifyDataSetChanged();
                }
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
    class ItemTimXe extends RecyclerView.ViewHolder{ // tao mot đói tượng
        ImageView imUser;
        TextView tvThongDiep;
        TextView tvMaSV;
        TextView tvViTri;
        TextView tvDiemDen;
        TextView tvGiaTien;
        public ItemTimXe(View itemView) {
            super(itemView);
            this.tvThongDiep = (TextView) itemView.findViewById(R.id.tv_item_tim_xe_thong_diep);
            this.tvMaSV = (TextView) itemView.findViewById(R.id.tv_item_tim_xe_msv);
            this.tvDiemDen = (TextView) itemView.findViewById(R.id.tv_item_tim_xe_diem_den);
            this.tvViTri = (TextView) itemView.findViewById(R.id.tv_item_tim_xe_vi_tri);
            this.imUser = (ImageView) itemView.findViewById(R.id.im_item_tim_xe);
            this.tvGiaTien = (TextView) itemView.findViewById(R.id.tv_item_tim_xe_gia_tien);
        }
    }
    private class AdapterTimXe extends RecyclerView.Adapter<NguoiTimXeFragment.ItemTimXe>
            implements RecyclerView.OnClickListener {
        private ArrayList<NguoiTimXe> data;
        public AdapterTimXe( ArrayList<NguoiTimXe> data) {
            this.data = data;
        }
        @Override
        public NguoiTimXeFragment.ItemTimXe onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_list_tim_xe, parent, false);
            view.setOnClickListener(this);
            NguoiTimXeFragment.ItemTimXe holder = new NguoiTimXeFragment.ItemTimXe(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(NguoiTimXeFragment.ItemTimXe holder, int position) {
            NguoiTimXe nguoiTimXe =data.get(position);
            holder.tvThongDiep.setText(nguoiTimXe.getThongDiep());
            holder.tvMaSV.setText(nguoiTimXe.getMaSV());
            holder.tvDiemDen.setText(nguoiTimXe.getDiemDen());
            holder.tvViTri.setText(nguoiTimXe.getViTri());
            holder.tvGiaTien.setText(nguoiTimXe.getGiaTien());
//            holder.imUser.setText(itemKetQuaThiLop.getGhiChu());
        }
        @Override
        public int getItemCount() {
            return data.size();
        }
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            Intent intent=new Intent(getActivity(), ViewUser.class);
            intent.putExtra(ViewUser.KEY_ID,data.get(itemPosition).getMaSV());
            navigationActivity.startActivity(intent);
        }
    }

}
