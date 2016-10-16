package com.haui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haui.activity.NavigationActivity;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager implements GoogleMap.OnMarkerClickListener {
    private GoogleMap googleMap;
    private NavigationActivity navigationActivity;

    public MapManager(GoogleMap googleMap, NavigationActivity navigationActivity) {
        this.googleMap = googleMap;
        this.navigationActivity = navigationActivity;
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions marker = new MarkerOptions().position(sydney).title("Marker in Sydney");
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:9898989898"));

        navigationActivity.startActivity(callIntent);
        return false;
    }
}
