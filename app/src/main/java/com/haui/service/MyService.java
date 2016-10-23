package com.haui.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Duong on 10/23/2016.
 */

public class MyService extends Service implements LocationListener{
    private DatabaseReference database;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String maSV;
    private LocationManager mLocationManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("faker","chay");
        database = FirebaseDatabase.getInstance().getReference();
        mLocationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400,10000, this);
        return START_STICKY;
    }

    public void upDateUser(final String item, final String valuse) {
        database.child("users").child(maSV).child(item).setValue(valuse, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {

                    } else {

                    }
            }
        });
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==111) {
                Location location= (Location) msg.obj;
                Log.e("faker","chay"+location.getLatitude());
                Intent intent = new Intent("my.location");
                // add data
                intent.putExtra("lat",""+location.getLatitude());
                intent.putExtra("lng",""+location.getLongitude());
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400,10000,MyService.this);
            }
        }
    };
    @Override
    public void onLocationChanged(Location location) {
        Message message=new Message();
        message.what=111;
        message.obj=location;
        handler.sendMessage(message);

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
    }

