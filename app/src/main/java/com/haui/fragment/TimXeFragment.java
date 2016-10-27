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
import com.haui.object.TimXe;

import java.util.ArrayList;

/**
 * Created by Duong on 10/25/2016.
 */

public class TimXeFragment extends Fragment{
    private View view;
    private RecyclerView recyclerView;
    private NavigationActivity navigationActivity;
    private ArrayList<TimXe> arrTimXes;
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
        arrTimXes=new ArrayList<>();
        log=navigationActivity.getLog();
        adapterTimXe = new AdapterTimXe(arrTimXes);
        recyclerView.setAdapter(adapterTimXe);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        navigationActivity.getDatabase().child("TimXe").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TimXe timXe= (TimXe) dataSnapshot.getValue(TimXe.class);
                if (!timXe.getMaSV().equals(log.getID())){
                    arrTimXes.add(timXe);
                    navigationActivity.setArrTimXes(arrTimXes);
                    adapterTimXe.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                android.util.Log.e("faker TimXe","s= "+dataSnapshot.getValue());
                TimXe timXe= (TimXe) dataSnapshot.getValue(TimXe.class);
                if (!timXe.getMaSV().equals(log.getID())){
                    int i=0;
                    for (int j = 0; j <arrTimXes.size() ; j++) {
                        if (arrTimXes.get(j).getMaSV().equals(timXe.getMaSV())){
                            i=j;
                            break;
                        }
                    }
                    arrTimXes.set(i,timXe);
                    navigationActivity.setArrTimXes(arrTimXes);
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
    private class AdapterTimXe extends RecyclerView.Adapter<TimXeFragment.ItemTimXe>
            implements RecyclerView.OnClickListener {
        private ArrayList<TimXe> data;
        public AdapterTimXe( ArrayList<TimXe> data) {
            this.data = data;
        }
        @Override
        public TimXeFragment.ItemTimXe onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_list_tim_xe, parent, false);
            view.setOnClickListener(this);
            TimXeFragment.ItemTimXe holder = new TimXeFragment.ItemTimXe(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(TimXeFragment.ItemTimXe holder, int position) {
            TimXe timXe=data.get(position);
            holder.tvThongDiep.setText(timXe.getThongDiep());
            holder.tvMaSV.setText(timXe.getMaSV());
            holder.tvDiemDen.setText(timXe.getDiemDen());
            holder.tvViTri.setText(timXe.getViTri());
            holder.tvGiaTien.setText(timXe.getGiaTien());
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
