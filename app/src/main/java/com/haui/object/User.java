package com.haui.object;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Duong on 10/18/2016.
 * khi gửi một đối tượng lên firebase càn phải thêm
 */
@IgnoreExtraProperties
public class User {
    public String maSV;
    public String passWord;
    public String tenSV;
    public String tenLopDL;
    public String soDT;
    public String bienSoXe;
    public String tenViTri;
    public Location location;

    public User() {

    }

    public User(String maSV, String passWord, String tenSV, String tenLopDL, String soDT, String bienSoXe, String tenViTri, Location location) {
        this.maSV = maSV;
        this.passWord = passWord;
        this.tenSV = tenSV;
        this.tenLopDL = tenLopDL;
        this.soDT = soDT;
        this.bienSoXe = bienSoXe;
        this.tenViTri = tenViTri;
        this.location = location;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getMaSV() {
        return maSV;
    }

    public String getTenSV() {
        return tenSV;
    }

    public String getTenLopDL() {
        return tenLopDL;
    }

    public String getSoDT() {
        return soDT;
    }

    public String getBienSoXe() {
        return bienSoXe;
    }

    public String getTenViTri() {
        return tenViTri;
    }

    public Location getLocation() {
        return location;
    }
}
