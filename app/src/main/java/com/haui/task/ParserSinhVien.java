package com.haui.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.haui.object.SinhVien;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by Faker on 8/29/2016.
 */

public class ParserSinhVien extends AsyncTask<String,Void,SinhVien> {
    private Handler handler;

    public ParserSinhVien( Handler handler) {
        this.handler = handler;
    }

    @Override
    protected SinhVien doInBackground( String... strings) {
        try {
            // mã sinh viên
            SinhVien sinhVien;
            Document doc= Jsoup.connect("http://qlcl.edu.vn/examre/ket-qua-hoc-tap.htm?code="+strings[0]).get();
            Elements parents=doc.select("div#_ctl8_viewResult");
            Elements tbodys=parents.get(0).select("tbody");
            Elements ttSV=tbodys.get(0).select("tr");
            String  tenSv=ttSV.get(0).select("td").get(1).select("strong").text();
            String maSV=ttSV.get(1).select("td").get(1).select("strong").text();
            String lopDL=ttSV.get(2).select("td").get(1).select("strong").text();
            sinhVien=new SinhVien(tenSv,maSV,lopDL);
            return sinhVien;
        } catch (Exception e) {
            return null;
        }

    }
    @Override
    protected void onPostExecute(SinhVien ketQuaHocTap) {
        try {
                Message message=new Message();
                message.what=0;
                message.obj=ketQuaHocTap;
                handler.sendMessage(message);

        }catch (NullPointerException e){
            Log.e("faker","loi");
        }

    }
}