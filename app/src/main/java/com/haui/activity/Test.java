package com.haui.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class Test extends ActionBarActivity {
    private SQLiteDatabase database;
    public static final String CREATE_TABLE_SV= "CREATE TABLE IF NOT EXISTS `sinhvien` (" +
            "`stt`INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`maSV`TEXT ," +
            "`tenSV`TEXT);";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDatabases();
        insertSV("ssssssssssss","sađuawđă");
        updateSV("ssssssssssss","123");
        getSV("ssssssssssss");


    }

    private void openDatabases() {
        database=openOrCreateDatabase("datas.sqlite", Context.MODE_APPEND,null);
        database.execSQL(CREATE_TABLE_SV);
    }

    private void closeDatabases() {
        database.close();
    }
    public long insertSV(String maSV,String tenSV){
        long id = 0;
            ContentValues contentValues=new ContentValues();
            contentValues.put("maSV",maSV);
            contentValues.put("tenSV",tenSV);
            openDatabases();
            id=database.insert("sinhvien", null, contentValues);
            closeDatabases();
        return id;
    }
    public void getSV(String masv) {
        try {
            openDatabases();
            String [] s={masv};
            Cursor cursor=database.query("sinhvien",null,"maSV=?",s,null,null,null);
            cursor.getCount();// tra ve so luong ban ghi no ghi dc
            cursor.getColumnNames();// 1 mang cac cot
            cursor.moveToFirst(); // di chuyển con trỏ đến dòng đầu tiền trong bảng
            int tenSV=cursor.getColumnIndex("tenSV");
            int maSV=cursor.getColumnIndex("maSV");
                    String s1=cursor.getString(tenSV);
            String s2= cursor.getString(maSV);
            Log.e("demo",s1+s2);
            closeDatabases();

        }catch (CursorIndexOutOfBoundsException e){
            Log.e("faker","CursorIndexOutOfBoundsException");
        }
    }
    public long updateSV(String maSV,String tenSV){
        ContentValues contentValues=new ContentValues();
        String []s={maSV};
        contentValues.put("maSV",maSV);
        contentValues.put("tenSV",tenSV);
        openDatabases();
        long id=database.update("sinhvien", contentValues,"maSV=?",s);
        closeDatabases();
        return id;
    }
}
