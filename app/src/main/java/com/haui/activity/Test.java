package com.haui.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Duong on 10/19/2016.
 */

public class Test extends AppCompatActivity{
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_infor);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.test);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });

        AsynGetLatLng asynGetLatLng=new AsynGetLatLng();
        asynGetLatLng.execute();



    }
    private class AsynGetLatLng extends AsyncTask<Location,Void,ArrayList<LatLng>> {
        @Override
        protected ArrayList<LatLng> doInBackground(Location... params) {
            ArrayList<LatLng> latLngs = null;

            String diemDau= "cau giay";
            diemDau=diemDau.replace(" ","+");
            String diemCuoi = "nhon";
            diemCuoi=diemCuoi.replace(" ","+");
            String driving= "driving";
            String vi= "vi";
            String link="https://maps.googleapis.com/maps/api/directions/json?origin="
                    +diemDau+"&destination="
                    + diemCuoi+"&avoid=tolls|highways|ferries&mode="
                    +driving+"&language="
                    +vi;
            String result = "";
            try {
                // Create a URL for the desired page
//                Log.e("faker","Rẽ \u003cb\u003etrái\u003c/b\u003e tại Công Ty Tnhh Nano An Phát vào \u003cb\u003eNgô Quyền\u003c/b\u003e");
                URL url = new URL(link);

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str=null;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    result=result+str;

                }
                in.close();
                JSONObject json=new JSONObject(result);
                String status= json.getString("status");
                if (status.equals("OK")){
                    JSONArray object=json.getJSONArray("routes");
                    JSONObject item=object.getJSONObject(0);
                    String copyrights=item.getString("copyrights");

                    JSONArray legs=item.getJSONArray("legs");
                    JSONObject objectLegs=legs.getJSONObject(0);

                    JSONObject distance=objectLegs.getJSONObject("distance");
                    String distanceText=distance.getString("text");
                    String distanceValue=distance.getString("value");
                    Log.e("faker", distanceText+" "+distanceValue);

                    JSONObject duration=objectLegs.getJSONObject("duration");
                    String durationText=duration.getString("text");
                    String durationValue=duration.getString("value");
                    Log.e("faker", durationText+" "+durationValue);

                    String end_address=objectLegs.getString("end_address");
                    String start_address=objectLegs.getString("start_address");
                    Log.e("faker", end_address+" "+start_address);

                    JSONObject start_location=objectLegs.getJSONObject("start_location");
                    String start_locationLat=start_location.getString("lat");
                    String start_locationLng=start_location.getString("lng");
                    Log.e("faker", start_locationLat+" "+start_locationLng);

                    JSONObject end_location=objectLegs.getJSONObject("end_location");
                    String end_locationLat=end_location.getString("lat");
                    String end_locationLng=end_location.getString("lng");
                    Log.e("faker", end_locationLat+" "+end_locationLng);
                }else{

                }


                Log.e("faker", status);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return latLngs;
        }
        @Override
        protected void onPostExecute(ArrayList<LatLng> latLngs) {
            super.onPostExecute(latLngs);

            PolylineOptions options = new PolylineOptions();
            options.color(Color.RED);;
            options.width(10);
            options.addAll(latLngs);
//            if (poLyLine!=null){
//                poLyLine.remove();
//            }
//            poLyLine = mGoogleMap.addPolyline(options);
        }
    }

}
