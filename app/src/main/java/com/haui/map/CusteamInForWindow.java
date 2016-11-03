package com.haui.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.haui.activity.R;
import com.haui.object.NguoiTimXe;
import com.haui.object.XeTimNguoi;

/**
 * Created by Duong on 11/3/2016.
 */

public class CusteamInForWindow implements GoogleMap.InfoWindowAdapter {
    private LayoutInflater inflater;
    private Context context;

    public CusteamInForWindow(Context context) {
        inflater = LayoutInflater.from(context);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        View view ;
        if (marker.getTag() instanceof XeTimNguoi){
            view = inflater.inflate(R.layout.infor_window_xe,null);
            XeTimNguoi xeTimNguoi= (XeTimNguoi) marker.getTag();
            TextView tvThongDiep = (TextView) view.findViewById(R.id.tv_infor_wd_xe_thong_diep);
            TextView tvMaSV= (TextView) view.findViewById(R.id.tv_infor_wd_xe_msv);
            TextView tvBSX= (TextView) view.findViewById(R.id.tv_infor_wd_xe_bsx);
            TextView tvViTri = (TextView) view.findViewById(R.id.tv_infor_wd_xe_vi_tri);
            tvThongDiep.setText(xeTimNguoi.getThongDiep());
            tvMaSV.setText(xeTimNguoi.getMaSV());
            tvBSX.setText(xeTimNguoi.getBsx());
            tvViTri.setText(xeTimNguoi.getViTri());
            return view;
        }else if (marker.getTag() instanceof NguoiTimXe){
            view = inflater.inflate(R.layout.infor_window_nguoi,null);
            NguoiTimXe nguoiTimXe= (NguoiTimXe) marker.getTag();
            TextView tvThongDiep = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_thong_diep);
            TextView tvMaSV = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_msv);
            TextView tvDiemDen = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_diem_den);
            TextView tvViTri = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_vi_tri);
            TextView tvGiaTien = (TextView) view.findViewById(R.id.tv_infor_wd_nguoi_gia_tien);
            tvThongDiep.setText(nguoiTimXe.getThongDiep());
            tvMaSV.setText(nguoiTimXe.getMaSV());
            tvDiemDen.setText(nguoiTimXe.getDiemDen());
            tvViTri.setText(nguoiTimXe.getViTri());
            tvGiaTien.setText(nguoiTimXe.getGiaTien());
            return view;
        }else{
            return null;
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = inflater.inflate(R.layout.infor_window_nguoi,null);

        return null;
    }
}
