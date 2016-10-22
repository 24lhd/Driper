package com.haui.object;

/**
 * Created by Duong on 10/18/2016.
 */

public class Location  {
    public String lat;
    public String lng;

    public Location() {
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Location(String lat, String lng) {

        this.lat = lat;
        this.lng = lng;
    }
}
