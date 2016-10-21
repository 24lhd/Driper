package com.haui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haui.activity.NavigationActivity;
import com.haui.activity.R;

/**
 * Created by Duong on 10/19/2016.
 */

public class MyInforFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ImageView imageView;
    private FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.layout_my_infor,container,false);
        initView();
        return view;
    }
    private NavigationActivity navigationActivity;
    private CollapsingToolbarLayout collapsingToolbar;
    private void initView( ) {
        floatingActionButton= (FloatingActionButton) view.findViewById(R.id.fbt_my_infor);
         collapsingToolbar =(CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
         imageView = (ImageView) view.findViewById(R.id.im_backdrop);
//        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
        navigationActivity= (NavigationActivity) getActivity();
        navigationActivity.registerForContextMenu(floatingActionButton);
//        floatingActionButton.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Chọn ảnh bằng");
        navigationActivity.getMenuInflater().inflate(R.menu.menu_select_image,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
            item.getItemId();
        switch (item.getItemId()){
            case R.id.mn_stroge:
                from_gallery();
                break;
            case R.id.mn_camera:
                from_camera();
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void from_gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 222);
    }
    public void from_camera() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 111);
    }
    public void setTextInfor(final String tv_infor_ten, String tv_infor_masv, String tv_infor_lop, final String tv_infor_sdt, String img) {
        ((TextView) view.findViewById(R.id.tv_infor_ten)).setText(tv_infor_ten);
        ((TextView) view.findViewById(R.id.tv_infor_masv)).setText(tv_infor_masv);
        ((TextView) view.findViewById(R.id.tv_infor_lop)).setText(tv_infor_lop);
        ((TextView) view.findViewById(R.id.tv_infor_sdt)).setText(tv_infor_sdt);
        ((TextView) view.findViewById(R.id.tv_infor_sdt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),tv_infor_sdt,Toast.LENGTH_SHORT).show();
            }
        });
        collapsingToolbar.setTitle(tv_infor_ten);
    }
    public void setProImage(String proImage) {
//        imageView.setImageURI(proImage);
        imageView = (ImageView) view.findViewById(R.id.im_backdrop);
        imageView.setImageResource(R.drawable.im_test);
//        imageView.setImageBitmap(BitmapFactory.decodeFile(proImage));
//        Picasso.with(getActivity()).load(proImage).into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fbt_my_infor:
                navigationActivity.openContextMenu(view);
            break;
            case R.id.im_backdrop:

                break;
        }
    }
}
