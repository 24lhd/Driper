package com.haui.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

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

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationChangeListener {
    private  LocationManager locationManager;
    private GoogleMap googleMap;

    private Context context;
    private NavigationActivity navigationActivity;
//    private Geocoder geocoder;//đối tượng quản lý vị trí địa lý vùng mình đang đứng
    public MapManager(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        checkLocationIsEnable();
        navigationActivity= (NavigationActivity) context;
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        googleMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) navigationActivity.getSystemService(Context.LOCATION_SERVICE);
        initMap();
    }
    private void initMap() {
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
//            mMarker=drawMarker(latLng.latitude,latLng.longitude,R.drawable.ic_my_location,"My Location","");
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
            googleMap.animateCamera(cameraUpdate);
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
    public void setTimNguoi() {
//        googleMap.clear();
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
//        initMap();
    }

    public void setTimXe() {
//        googleMap.clear();
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
//        initMap();
    }
    public void moveToMyLocation() {
        myLocation=null;
    }
}
