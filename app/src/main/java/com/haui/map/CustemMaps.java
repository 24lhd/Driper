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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.haui.activity.R;

import java.io.IOException;
import java.util.ArrayList;
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
    public Marker drawMarker(double lat, double lng, BitmapDescriptor hue, String title, String snippet){
        //định nghĩa điểm ảnh
        // mỗi maker chỉ hiện thị một điểm ảnh
        LatLng latLng = new LatLng(lat,lng);//tạo kinh vĩ

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(hue);
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
    public void moveToMyLocation() {
        checkLocationIsEnable();
        if (myLocation!=null){
            LatLng latLng = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
            googleMap.animateCamera(cameraUpdate);
        }
    }
    public Location getMyLocation() {
        return myLocation;
    }
    public void drawRoadByAddress(String addressStart,String addressEnd,int color,int width){

    }
    public void drawRoadByLocation(Location locationStart,Location locationEnd,int color,int width){

    }
    public void drawRoad(ArrayList<LatLng> latLngs,int color,int width){
        PolylineOptions options = new PolylineOptions();
        options.color(color);;
        options.width(width);
        options.addAll(latLngs);
        googleMap.addPolyline(options);
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
            Toast.makeText(context,"Vị trí không thoả mã",Toast.LENGTH_LONG).show();
            return;
        }else {
            Toast.makeText(context,"Vị trí  thoả mã",Toast.LENGTH_LONG).show();
        }
    }
     class GoogleMapAPI{
        String status;
         String place_id="place_id:";
        String copyrights;
        String summary;
        String distanceText;
        String distanceValue;
        String durationText;
        String durationValue;
        String end_address;
        String start_address;
        String start_locationLat;
        String start_locationLng;
        String end_locationLat;
        String end_locationLng;
        ArrayList<ItemStep> itemSteps;

         public ArrayList<ItemStep> getItemSteps() {
             return itemSteps;
         }

         public GoogleMapAPI(String status, String copyrights, String summary, String distanceText, String distanceValue, String durationText, String durationValue, String end_address, String start_address, String start_locationLat, String start_locationLng, String end_locationLat, String end_locationLng, ArrayList<ItemStep> itemSteps) {

             this.status = status;
             this.copyrights = copyrights;
             this.summary = summary;
             this.distanceText = distanceText;
             this.distanceValue = distanceValue;
             this.durationText = durationText;
             this.durationValue = durationValue;
             this.end_address = end_address;
             this.start_address = start_address;
             this.start_locationLat = start_locationLat;
             this.start_locationLng = start_locationLng;
             this.end_locationLat = end_locationLat;
             this.end_locationLng = end_locationLng;
             this.itemSteps = itemSteps;
         }

         public String getStatus() {
            return status;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public String getSummary() {
            return summary;
        }

        public String getDistanceText() {
            return distanceText;
        }

        public String getDistanceValue() {
            return distanceValue;
        }

        public String getDurationText() {
            return durationText;
        }

        public String getDurationValue() {
            return durationValue;
        }

        public String getEnd_address() {
            return end_address;
        }

        public String getStart_address() {
            return start_address;
        }

        public String getStart_locationLat() {
            return start_locationLat;
        }

        public String getStart_locationLng() {
            return start_locationLng;
        }

        public String getEnd_locationLat() {
            return end_locationLat;
        }

        public String getEnd_locationLng() {
            return end_locationLng;
        }
    }
     class ItemStep{
        String distanceTextSteps;
        String distanceValueSteps;
        String durationTextSteps;
        String durationValueSteps;
        String start_locationLatSteps;
        String start_locationLngSteps;
        String end_locationLatSteps;
        String end_locationLngSteps;
        String html_instructions;
        String travel_mode;

        public String getDistanceTextSteps() {
            return distanceTextSteps;
        }

        public String getDistanceValueSteps() {
            return distanceValueSteps;
        }

        public String getDurationTextSteps() {
            return durationTextSteps;
        }

        public String getDurationValueSteps() {
            return durationValueSteps;
        }

        public String getStart_locationLatSteps() {
            return start_locationLatSteps;
        }

        public String getStart_locationLngSteps() {
            return start_locationLngSteps;
        }

        public String getEnd_locationLatSteps() {
            return end_locationLatSteps;
        }

        public String getEnd_locationLngSteps() {
            return end_locationLngSteps;
        }

        public String getHtml_instructions() {
            return html_instructions;
        }

        public String getTravel_mode() {
            return travel_mode;
        }

        public ItemStep(String distanceTextSteps,
                        String distanceValueSteps,
                        String durationTextSteps,
                        String durationValueSteps,
                        String start_locationLatSteps,
                        String start_locationLngSteps,
                        String end_locationLatSteps,
                        String end_locationLngSteps,
                        String html_instructions,
                        String travel_mode) {
            this.distanceTextSteps = distanceTextSteps;
            this.distanceValueSteps = distanceValueSteps;
            this.durationTextSteps = durationTextSteps;
            this.durationValueSteps = durationValueSteps;
            this.start_locationLatSteps = start_locationLatSteps;
            this.start_locationLngSteps = start_locationLngSteps;
            this.end_locationLatSteps = end_locationLatSteps;
            this.end_locationLngSteps = end_locationLngSteps;
            this.html_instructions = html_instructions;
            this.travel_mode = travel_mode;

        }
    }

}
