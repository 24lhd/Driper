package com.haui.map;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.haui.activity.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duong on 11/1/2016.
 */

public class CustemMaps implements GoogleMap.OnMyLocationChangeListener{
    private GoogleMap googleMap;
    private Context context;
    private Geocoder geocoder;
    private Activity activity;
    private Location myLocation;
    private String diemDau;
    private String diemCuoi;
    private String driving="driving";
    private String walking="walking";
    private String transit="transit";
    private String en="en";
    private String vi="vi";
    private String link="https://maps.googleapis.com/maps/api/directions/json?origin="+diemDau+"&destination="+diemCuoi+"&avoid=tolls|highways|ferries&mode="+driving+"&language="+vi;
    public CustemMaps(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
        checkLocationIsEnable();
        activity= (Activity) context;
        geocoder = new Geocoder(activity, Locale.getDefault());
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);
    }
    public CustemMaps(Context context) {
        this.context = context;
        checkLocationIsEnable();
        geocoder = new Geocoder(context, Locale.getDefault());
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

                    activity.finish();
                }
            });
            dialog.show();
        }
    }
    public Marker drawMarker(double lat, double lng, int hue, String title, String snippet){
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

    public Activity getActivity() {
        return activity;

    }

    public GoogleMap getGoogleMap() {

        return googleMap;
    }

    public void setMapVeTinh() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
    public void setMapGiaoThong() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

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
    }
    public Location getMyLocation() {
        return myLocation;
    }

    public void moveToMyLocation() {
        checkLocationIsEnable();
        if (myLocation!=null){
            LatLng latLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            googleMap.animateCamera(cameraUpdate);
        }
    }
}
