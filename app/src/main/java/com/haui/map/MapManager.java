package com.haui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager implements GoogleMap.OnMarkerClickListener, LocationListener, GoogleMap.OnMyLocationChangeListener {
    private GoogleMap googleMap;
    private Context context;
    private LocationManager locationManager;
    private Geocoder geocoder;//đối tượng quản lý vị trí địa lý vùng mình đang đứng
    public MapManager(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
//        reQuestPermistion();
        geocoder = new Geocoder(context, Locale.getDefault());
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this);




    }
    private Marker drawMarker(double lat,double lng,int hue,String title,String snippet){
        //định nghĩa điểm ảnh
        // mỗi maker chỉ hiện thị một điểm ảnh
        LatLng latLng = new LatLng(lat,lng);//tạo kinh vĩ
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //lay maker
        markerOptions.icon(BitmapDescriptorFactory.fromResource( hue));
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        return googleMap.addMarker(markerOptions);
    }
    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }
    private String getNameByLocation(double lat,double lng){
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
            Log.e("faker","IOException");
            return "";
        }

    }
    private void reQuestPermistion() {
        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},20);
        ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    12);
        } else {
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
            }
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            }
        }


    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        drawMarker(latLng.latitude,latLng.longitude, R.drawable.police,"duonghaui","im here");

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
        googleMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        Log.e("faker", getNameByLocation(location.getLatitude(),location.getLongitude()));
    }
}
