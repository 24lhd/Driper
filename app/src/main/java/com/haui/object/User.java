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
    public String imgProfile;
    public Location location;
    public String viTri;
    public User(String maSV, String passWord, String tenSV, String tenLopDL, String soDT, String imgProfile, Location location, String viTri) {
        this.maSV = maSV;
        this.passWord = passWord;
        this.tenSV = tenSV;
        this.tenLopDL = tenLopDL;
        this.soDT = soDT;
        this.imgProfile = imgProfile;
        this.location = location;
        this.viTri = viTri;
    }

    @Override
    public String toString() {
        return "User{" +
                "maSV='" + maSV + '\'' +
                ", passWord='" + passWord + '\'' +
                ", tenSV='" + tenSV + '\'' +
                ", tenLopDL='" + tenLopDL + '\'' +
                ", soDT='" + soDT + '\'' +
                ", imgProfile='" + imgProfile + '\'' +
                ", location=" + location +
                ", viTri='" + viTri + '\'' +
                '}';
    }

    public String getMaSV() {
        return maSV;
    }

    public String getPassWord() {
        return passWord;
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

    public String getImgProfile() {
        return imgProfile;
    }

    public Location getLocation() {
        return location;
    }

    public String getViTri() {
        return viTri;
    }

    public User() {


    }

}
