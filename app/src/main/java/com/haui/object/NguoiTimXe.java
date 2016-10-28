package com.haui.object;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Duong on 10/26/2016.
 */
@IgnoreExtraProperties
public class NguoiTimXe {
    public Location location;
    public String viTri;
    public String maSV;
    public String diemDen;
    public String giaTien;
    public String thongDiep;
    public NguoiTimXe() {
    }

    @Override
    public String toString() {
        return "NguoiTimXe{" +
                "location=" + location +
                ", viTri='" + viTri + '\'' +
                ", maSV='" + maSV + '\'' +
                ", diemDen='" + diemDen + '\'' +
                ", giaTien='" + giaTien + '\'' +
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

    public String getDiemDen() {
        return diemDen;
    }

    public String getGiaTien() {
        return giaTien;
    }


    public String getThongDiep() {
        return thongDiep;
    }

    public NguoiTimXe(Location location, String viTri, String maSV, String diemDen, String giaTien, String thongDiep) {

        this.location = location;
        this.viTri = viTri;
        this.maSV = maSV;
        this.diemDen = diemDen;
        this.giaTien = giaTien;
        this.thongDiep = thongDiep;
    }
}
