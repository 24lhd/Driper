package com.haui.activity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by Duong on 10/19/2016.
 */

public class Test extends AppCompatActivity{
    private DatabaseReference database;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_infor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        geocoder = new Geocoder(this, Locale.getDefault());
        toolbar.inflateMenu(R.menu.test);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                finish();
                return false;
            }
        });

//        AsynGetLatLng asynGetLatLng = new AsynGetLatLng();
//        asynGetLatLng.execute();


        searchLocationByName("Nam sach");
    }
    public void searchLocationByName(String start) {
        Location locationStart = null;
        try {
            List<Address> addressesStart = geocoder.getFromLocationName(start,5);
            if (addressesStart.size()>0){
                for (int i = 0; i <addressesStart.size() ; i++) {
                    Address addStart = addressesStart.get(i);
                    Log.e("faker",addStart.toString());
//                    locationStart = new Location("");
//                    locationStart.setLatitude(addStart.getLatitude());
//                    locationStart.setLongitude(addStart.getLongitude());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (locationStart==null){
            Toast.makeText(this,"Vị trí không thoả mã",Toast.LENGTH_LONG).show();
            return;
        }else {
            Toast.makeText(this,"Vị trí  thoả mã",Toast.LENGTH_LONG).show();
        }
    }
    public void searchLocationByName(String start, String end) {
        Location locationStart = null,locationEnd = null;
        try {
            List<Address> addressesStart = geocoder.getFromLocationName(start,1);
            List<Address> addressesEnd = geocoder.getFromLocationName(end,1);
            if (addressesStart.size()>0){
                Address addStart = addressesStart.get(0);
                locationStart = new Location("");
                locationStart.setLatitude(addStart.getLatitude());
                locationStart.setLongitude(addStart.getLongitude());
            }
            if (addressesEnd.size()>0){
                Address addEnd = addressesEnd.get(0);
                locationEnd = new Location("");
                locationEnd.setLatitude(addEnd.getLatitude());
                locationEnd.setLongitude(addEnd.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (locationStart==null||locationEnd==null){
            Toast.makeText(this,"Vị trí không thoả mã",Toast.LENGTH_LONG).show();
            return;
        }else {
            Toast.makeText(this,"Vị trí  thoả mã",Toast.LENGTH_LONG).show();
        }
    }
    private class AsynGetLatLng extends AsyncTask<Location, Void, ArrayList<LatLng>> {
        @Override
        protected ArrayList<LatLng> doInBackground(Location... params) {
            ArrayList<LatLng> latLngs = null;

            String diemDau = "cau giay";
            diemDau = diemDau.replace(" ", "+");
            String diemCuoi = "nhon";
            diemCuoi = diemCuoi.replace(" ", "+");
            String driving = "driving";
            String vi = "vi";
            String link = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + diemDau + "&destination="
                    + diemCuoi + "&avoid=tolls|highways|ferries&mode="
                    + driving + "&language="
                    + vi;
            String result = "";
            try {
                // Create a URL for the desired page
//                Log.e("faker","Rẽ \u003cb\u003etrái\u003c/b\u003e tại Công Ty Tnhh Nano An Phát vào \u003cb\u003eNgô Quyền\u003c/b\u003e");
                URL url = new URL(link);
                Log.e("faker", link);
                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = null;
                while ((str = in.readLine()) != null) {
                    // str is one line of text; readLine() strips the newline character(s)
                    result = result + str;

                }
                in.close();
                JSONObject json = new JSONObject(result);
                String status = json.getString("status");
                if (status.equals("OK")) {
                    JSONArray object = json.getJSONArray("routes");
                    JSONObject item = object.getJSONObject(0);
                    String copyrights = item.getString("copyrights");

                    String summary = item.getString("summary");
                    Log.e("faker", summary);
                    JSONArray legs = item.getJSONArray("legs");

                    JSONObject objectLegs = legs.getJSONObject(0);

                    JSONObject distance = objectLegs.getJSONObject("distance");
                    String distanceText = distance.getString("text");
                    String distanceValue = distance.getString("value");
                    Log.e("faker", distanceText + " " + distanceValue);

                    JSONObject duration = objectLegs.getJSONObject("duration");
                    String durationText = duration.getString("text");
                    String durationValue = duration.getString("value");
                    Log.e("faker", durationText + " " + durationValue);

                    String end_address = objectLegs.getString("end_address");
                    String start_address = objectLegs.getString("start_address");
                    Log.e("faker", end_address + " " + start_address);

                    JSONObject start_location = objectLegs.getJSONObject("start_location");
                    String start_locationLat = start_location.getString("lat");
                    String start_locationLng = start_location.getString("lng");
                    Log.e("faker", start_locationLat + " " + start_locationLng);

                    JSONObject end_location = objectLegs.getJSONObject("end_location");
                    String end_locationLat = end_location.getString("lat");
                    String end_locationLng = end_location.getString("lng");
                    Log.e("faker", end_locationLat + " " + end_locationLng);

                    JSONArray steps = objectLegs.getJSONArray("steps");

                    for (int i = 0; i < steps.length(); i++) {
                        JSONObject itemSteps=steps.getJSONObject(i);
                        JSONObject distanceSteps = itemSteps.getJSONObject("distance");
                        String distanceTextSteps = distanceSteps.getString("text");
                        String distanceValueSteps = distanceSteps.getString("value");
                        Log.e("faker", distanceTextSteps + " " + distanceValueSteps);

                        JSONObject durationSteps = itemSteps.getJSONObject("duration");
                        String durationTextSteps = durationSteps.getString("text");
                        String durationValueSteps = durationSteps.getString("value");
                        Log.e("faker", durationTextSteps + " " + durationValueSteps);

                        JSONObject start_locationSteps = itemSteps.getJSONObject("start_location");
                        String start_locationLatSteps = start_locationSteps.getString("lat");
                        String start_locationLngSteps = start_locationSteps.getString("lng");
                        Log.e("faker", start_locationLatSteps + " " + start_locationLngSteps);

                        JSONObject end_locationSteps = itemSteps.getJSONObject("end_location");
                        String end_locationLatSteps = end_locationSteps.getString("lat");
                        String end_locationLngSteps = end_locationSteps.getString("lng");
                        Log.e("faker", end_locationLatSteps + " " + end_locationLngSteps);

                        String html_instructions=Html.fromHtml((String) itemSteps.getString("html_instructions")).toString();
                        html_instructions=html_instructions.replace("\n"," ");
                        Log.e("faker", html_instructions);

                        String travel_mode=itemSteps.getString("travel_mode");
                        Log.e("faker", travel_mode);
                    }
                } else {

                }


                Log.e("faker", status);
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                Log.e("faker", "NullPointerException");
            }
            return latLngs;
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> latLngs) {
            super.onPostExecute(latLngs);

//            PolylineOptions options = new PolylineOptions();
//            options.color(Color.RED);;
//            options.width(10);
//            options.addAll(latLngs);
//            if (poLyLine!=null){
//                poLyLine.remove();
//            }
//            poLyLine = mGoogleMap.addPolyline(options);
        }
    }

}
