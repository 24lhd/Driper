package com.haui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Duong on 10/23/2016.
 */

public class MyReceiver extends BroadcastReceiver implements GoogleMap.OnMyLocationChangeListener{

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
