package com.haui.map;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.haui.activity.NavigationActivity;
import com.haui.activity.R;
import com.haui.object.NguoiTimXe;
import com.haui.object.XeTimNguoi;

/**
 * Created by Duong on 10/16/2016.
 */

public class MapManager extends CustemMaps implements GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener,PlaceSelectionListener {
    private NavigationActivity navigationActivity;
    private Marker markerSearch;

    public MapManager(GoogleMap googleMap, Context context) {
        super(googleMap, context);
        navigationActivity= (NavigationActivity) context;
        googleMap.setOnMarkerClickListener(this);
        navigationActivity.registerForContextMenu(navigationActivity.getFloatingActionButton());
        googleMap.setInfoWindowAdapter(new CusteamInForWindow(context));
        googleMap.setOnInfoWindowClickListener(this);
        PlaceAutocompleteFragment autocompleteFragment =
                (PlaceAutocompleteFragment)navigationActivity.getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }
//        mMarker.setPosition(latLng);
//        PolylineOptions options = new PolylineOptions();
//        options.color(Color.GREEN);;
//        options.width(10);
//        options.add(new LatLng(21.0579267,105.73167086));
//        options.add(new LatLng(21.04677555,105.74839711));
//        googleMap.addPolyline(options);
    public void setHienXe() {
        getGoogleMap().clear();
        for (XeTimNguoi xeTimNguoi :navigationActivity.getArrXeTimNguois()) {
            double a=Double.parseDouble(xeTimNguoi.getLocation().getLat());
            double b=Double.parseDouble(xeTimNguoi.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                drawMarker(a,b,BitmapDescriptorFactory.fromResource(R.drawable.ic_driver), xeTimNguoi.getThongDiep()+"\n"+ xeTimNguoi.getMaSV(),
                        xeTimNguoi.getViTri()).setTag(xeTimNguoi);

            }
        }
    }

    public void setHienNguoi() {
        getGoogleMap().clear();
        for (NguoiTimXe nguoiTimXe :navigationActivity.getArrNguoiTimXes()) {
            double a=Double.parseDouble(nguoiTimXe.getLocation().getLat());
            double b=Double.parseDouble(nguoiTimXe.getLocation().getLng());
            if (a!=0.0||b!=0.0){
                drawMarker(a,b,BitmapDescriptorFactory.fromResource(R.drawable.ic_user), nguoiTimXe.getThongDiep()+"\n"+
                        nguoiTimXe.getMaSV()+"\n"+ nguoiTimXe.getDiemDen(), nguoiTimXe.getViTri()).setTag(nguoiTimXe);

            }

        }
    }
    private Marker markerCick;

    public Marker getMarkerCick() {
        return markerCick;
    }

    public void setMarkerCick(Marker markerCick) {
        this.markerCick = markerCick;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!(marker.getTag() instanceof XeTimNguoi)&&!(marker.getTag() instanceof NguoiTimXe)){

        }else {
            navigationActivity.openContextMenu(navigationActivity.getFloatingActionButton());
            setMarkerCick(marker);
        }

    }

    @Override
    public void onPlaceSelected(Place place) {
        if (place.getLatLng()!=null){
            markerSearch=drawMarker(place.getLatLng().latitude,place.getLatLng().longitude, BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),place.getName().toString(),place.getAddress().toString());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerSearch.getPosition(), 12);
            getGoogleMap().animateCamera(cameraUpdate);
        }

    }

    @Override
    public void onError(Status status) {
        Log.e("faker", "onError");
    }
}
