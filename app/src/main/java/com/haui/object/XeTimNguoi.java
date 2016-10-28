package com.haui.object;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Duong on 10/26/2016.
 */
@IgnoreExtraProperties
public class XeTimNguoi {
    public Location location;
    public String viTri;
    public String maSV;

    @Override
    public String toString() {
        return "XeTimNguoi{" +
                "location=" + location +
                ", viTri='" + viTri + '\'' +
                ", maSV='" + maSV + '\'' +
                ", bsx='" + bsx + '\'' +
                ", thongDiep='" + thongDiep + '\'' +
                '}';
    }

    public Location getLocation() {
        return location;
    }

    public String getViTri() {
        return viTri;
    }

    public String getMaSV() {
        return maSV;
    }

    public String getBsx() {
        return bsx;
    }

    public String getThongDiep() {
        return thongDiep;
    }

    public XeTimNguoi() {

    }

    public XeTimNguoi(Location location, String viTri, String maSV, String bsx, String thongDiep) {

        this.location = location;
        this.viTri = viTri;
        this.maSV = maSV;
        this.bsx = bsx;
        this.thongDiep = thongDiep;
    }

    public String bsx;
    public String thongDiep;
}
