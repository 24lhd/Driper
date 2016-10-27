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
import com.haui.object.TimNguoi;
import java.util.ArrayList;

/**
 * Created by Duong on 10/25/2016.
 */

public class TimNguoiFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private NavigationActivity navigationActivity;
    private ArrayList<TimNguoi> arrTimNguois;
    private Log log;
    private AdapterTimNguoi adapterTimNguoi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.yeu_cau_fragment_layout,container,false);
        initView();
        android.util.Log.e("faker TimNguoiFragment","onCreateView");
        return view;
    }

    private void initView() {
        navigationActivity= (NavigationActivity) getActivity();
        recyclerView= (RecyclerView) view.findViewById(R.id.rcv_yeucau_fragment);
        recyclerView.setHasFixedSize(true);
        arrTimNguois=new ArrayList<>();
        log=navigationActivity.getLog();
        adapterTimNguoi = new AdapterTimNguoi(arrTimNguois);
        recyclerView.setAdapter(adapterTimNguoi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        navigationActivity.getDatabase().child("TimNguoi").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TimNguoi timNguoi= (TimNguoi) dataSnapshot.getValue(TimNguoi.class);
                if (!timNguoi.getMaSV().equals(log.getID())){
                    arrTimNguois.add(timNguoi);
                     adapterTimNguoi.notifyDataSetChanged();
                }



            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        android.util.Log.e("faker TimNguoi","onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        android.util.Log.e("faker TimNguoi","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
            android.util.Log.e("faker TimNguoi","onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        android.util.Log.e("faker TimNguoi","onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        android.util.Log.e("faker TimNguoi","onDestroy");
        super.onDestroy();
    }

    class ItemTimNguoi extends RecyclerView.ViewHolder{ // tao mot đói tượng
        ImageView imUser;
        TextView tvThongDiep;
        TextView tvMaSV;
        TextView tvBSX;
        TextView tvViTri;
        public ItemTimNguoi(View itemView) {
            super(itemView);
            this.tvThongDiep = (TextView) itemView.findViewById(R.id.tv_item_tim_nguoi_thong_diep);
            this.tvMaSV = (TextView) itemView.findViewById(R.id.tv_item_tim_nguoi_msv);
            this.tvBSX = (TextView) itemView.findViewById(R.id.tv_item_tim_nguoi_bsx);
            this.tvViTri = (TextView) itemView.findViewById(R.id.tv_item_tim_nguoi_vi_tri);
            this.imUser = (ImageView) itemView.findViewById(R.id.im_item_tim_nguoi);
        }
    }
    private class AdapterTimNguoi extends RecyclerView.Adapter<TimNguoiFragment.ItemTimNguoi>
            implements RecyclerView.OnClickListener {
        private ArrayList<TimNguoi> data;
        public AdapterTimNguoi( ArrayList<TimNguoi> data) {
            this.data = data;
        }
        @Override
        public TimNguoiFragment.ItemTimNguoi onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_list_tim_nguoi, parent, false);
            view.setOnClickListener(this);
            TimNguoiFragment.ItemTimNguoi holder = new TimNguoiFragment.ItemTimNguoi(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(TimNguoiFragment.ItemTimNguoi holder, int position) {
            TimNguoi timNguoi=data.get(position);
            holder.tvThongDiep.setText(timNguoi.getThongDiep());
            holder.tvMaSV.setText(timNguoi.getMaSV());
            holder.tvBSX.setText(timNguoi.getBsx());
            holder.tvViTri.setText(timNguoi.getViTri());
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
//
//    public void createList(ArrayList<TimNguoi> data) {
//        android.util.Log.e("faker timNguoi",""+data.size());
//        if (data.size()>0){
//            AdapterTimNguoi adapterTimNguoi = new AdapterTimNguoi(data);
//            recyclerView.setAdapter(adapterTimNguoi);
//        }
//    }

//    public void addItemTimNguoi(TimNguoi timNguoi) {
//        data.add(timNguoi);
//        adapterTimNguoi.notifyDataSetChanged();
//
//        recyclerView.setAdapter(adapterTimNguoi);
//    }

}
