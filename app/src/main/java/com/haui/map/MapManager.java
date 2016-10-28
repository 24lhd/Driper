package com.haui.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.object.NguoiTimXe;
import com.haui.object.XeTimNguoi;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener {
    private  LocationManager locationManager;
    private GoogleMap googleMap;

    private Context context;
    private NavigationActivity navigationActivity;
    private Geocoder geocoder;

    //    private Geocoder geocoder;//đối tượng quản lý vị trí địa lý vùng mình đang đứng
    public MapManager(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        checkLocationIsEnable();
        initMapView();
    }
    public String getNameByLocation(double lat,double lng){
        //tìm kiếm vị trí
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,lng,1);// getfromlocation trả vể list nên cần tạo 1 list
            if (addresses.size()==0){
                return "";
            }
            String name = addresses.get(0).getAddressLine(0);
            name +=" - " +addresses.get(0).getAddressLine(1);
            name +=" - " +addresses.get(0).getAddressLine(2);
            return name;
        } catch (IOException e) {
            return "";
        }
    }
    private void initMapView() {
        navigationActivity= (NavigationActivity) context;
        geocoder = new Geocoder(navigationActivity, Locale.getDefault());
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        googleMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) navigationActivity.getSystemService(Context.LOCATION_SERVICE);
        googleMap.setOnMyLocationChangeListener(this);
    }
    private void checkLocationIsEnable() {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    navigationActivity.finish();
                }
            });
            dialog.show();
        }
    }
    private Marker drawMarker(double lat,double lng,int hue,String title,String snippet){
        //định nghĩa điểm ảnh
        // mỗi maker chỉ hiện thị một điểm ảnh
        LatLng latLng = new LatLng(lat,lng);//tạo kinh vĩ
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource( hue));
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        return googleMap.addMarker(markerOptions);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    private Location myLocation;
    @Override
    public void onMyLocationChange(Location location) {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        if (myLocation==null){
            myLocation=location;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            googleMap.animateCamera(cameraUpdate);
        }else {
            myLocation=location;
        }
//        mMarker.setPosition(latLng);
//        PolylineOptions options = new PolylineOptions();
//        options.color(Color.GREEN);;
//        options.width(10);
//        options.add(new LatLng(21.0579267,105.73167086));
//        options.add(new LatLng(21.04677555,105.74839711));
//        googleMap.addPolyline(options);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==111){
                LatLng latLng= (LatLng) msg.obj;
            }
        }
    };

    public void setHienXe() {
        googleMap.clear();
        for (XeTimNguoi xeTimNguoi :navigationActivity.getArrXeTimNguois()) {
            double a=Double.parseDouble(xeTimNguoi.getLocation().getLat());
            double b=Double.parseDouble(xeTimNguoi.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                drawMarker(a,b,R.drawable.ic_location_driver, xeTimNguoi.getThongDiep()+"\n"+ xeTimNguoi.getMaSV(),
                        xeTimNguoi.getViTri());
            }
        }
    }

    public void setHienNguoi() {
        googleMap.clear();
        for (NguoiTimXe nguoiTimXe :navigationActivity.getArrNguoiTimXes()) {
            double a=Double.parseDouble(nguoiTimXe.getLocation().getLat());
            double b=Double.parseDouble(nguoiTimXe.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                drawMarker(a,b,R.drawable.ic_student, nguoiTimXe.getThongDiep()+"\n"+
                        nguoiTimXe.getMaSV()+"\n"+ nguoiTimXe.getDiemDen(), nguoiTimXe.getViTri());
            }

        }
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void moveToMyLocation() {
        checkLocationIsEnable();
        LatLng latLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
        googleMap.animateCamera(cameraUpdate);
    }
    public void setMapVeTinh() {
        Log.e("faker","setMapVeTinh");
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
    public void setMapGiaoThong() {
        Log.e("faker","setMapGiaoThong");
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
