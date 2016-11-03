package com.haui.map;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.object.NguoiTimXe;
import com.haui.object.XeTimNguoi;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager extends CustemMaps implements GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener {
    private NavigationActivity navigationActivity;
    public MapManager(GoogleMap googleMap, Context context) {
        super(googleMap, context);
        navigationActivity= (NavigationActivity) context;
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new CusteamInForWindow());
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.e("faker", (String) marker.getTag());
        Log.e("faker","qua");
        return true;
    }
//        mMarker.setPosition(latLng);
//        PolylineOptions options = new PolylineOptions();
//        options.color(Color.GREEN);;
//        options.width(10);
//        options.add(new LatLng(21.0579267,105.73167086));
//        options.add(new LatLng(21.04677555,105.74839711));
//        googleMap.addPolyline(options);
    public void setHienXe() {
        getGoogleMap().clear();
        for (XeTimNguoi xeTimNguoi :navigationActivity.getArrXeTimNguois()) {
            double a=Double.parseDouble(xeTimNguoi.getLocation().getLat());
            double b=Double.parseDouble(xeTimNguoi.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                Marker marker=drawMarker(a,b,R.drawable.ic_location_driver, xeTimNguoi.getThongDiep()+"\n"+ xeTimNguoi.getMaSV(),
                        xeTimNguoi.getViTri());
                marker.setTag(xeTimNguoi.getMaSV());

            }
        }
    }
    public void setHienNguoi() {
        getGoogleMap().clear();
        for (NguoiTimXe nguoiTimXe :navigationActivity.getArrNguoiTimXes()) {
            double a=Double.parseDouble(nguoiTimXe.getLocation().getLat());
            double b=Double.parseDouble(nguoiTimXe.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                drawMarker(a,b,R.drawable.ic_student, nguoiTimXe.getThongDiep()+"\n"+
                        nguoiTimXe.getMaSV()+"\n"+ nguoiTimXe.getDiemDen(), nguoiTimXe.getViTri());
            }

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
