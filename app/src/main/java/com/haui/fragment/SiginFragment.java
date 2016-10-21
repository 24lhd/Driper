package com.haui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haui.activity.LoginActivity;
import com.haui.activity.R;


/**
 * Created by Faker on 8/14/2016.
 */
public class SiginFragment extends Fragment implements View.OnClickListener{
    private EditText etId;
    private EditText etPass;
    private EditText etPassAgian;
    private EditText etSoDT;
    private AppCompatButton btRigister;
    private LoginActivity tabActivity;
    private ProgressBar progressBar;
    private TextView tvError;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         tabActivity= (LoginActivity) getActivity();
        View view= inflater.inflate(R.layout.sigin_fragment,container,false);
        progressBar= (ProgressBar) view.findViewById(R.id.pg_register);
        etPass= (EditText) view.findViewById(R.id.et_pass_register);
        etId= (EditText) view.findViewById(R.id.et_id_register);
        etPassAgian= (EditText) view.findViewById(R.id.et_pass_again_register);
        etSoDT= (EditText) view.findViewById(R.id.et_sdt);
        btRigister= (AppCompatButton) view.findViewById(R.id.bt_register);
        tvError= (TextView) view.findViewById(R.id.signin_error);
        tvError.setVisibility(View.GONE);
        btRigister.setOnClickListener(this);
        return view;
    }

    public void textError(String s) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setText("Error: "+s);
    }

    @Override
    public void onClick(View view) {
        String id,pass,passAgain,soDT;
        if(view.getId()==btRigister.getId()){
            id=etId.getText().toString();
            pass=etPass.getText().toString();
            passAgain=etPassAgian.getText().toString();
            soDT=etSoDT.getText().toString();
            if (id.isEmpty()){
                textError("Không được để trống mã sinh viên");
//                Toast.makeText(getContext(),"Không được để trống mã sinh viên",Toast.LENGTH_LONG).show();
            }else if(!pass.equals(passAgain)){
                textError("Mật khẩu không giống nhau");
//                Toast.makeText(getContext(),"Mật khẩu không giống nhau",Toast.LENGTH_LONG).show();
            }else if(soDT.length()<10){
                textError("Số điện thoại không đúng");
//                Toast.makeText(getContext(),"Số điện thoại không đúng",Toast.LENGTH_LONG).show();
            }else if(pass.length()<8){
                textError("Mật khẩu phải lớn hơn 8 ký tự");
//                Toast.makeText(getContext(),"Số điện thoại không đúng",Toast.LENGTH_LONG).show();
            }else {
                btRigister.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                tabActivity.register(id,pass,soDT,btRigister,progressBar);
            }
        }
    }
}
