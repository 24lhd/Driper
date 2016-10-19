package com.haui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.haui.log.Log;
import com.haui.map.MapManager;
import com.haui.object.User;

import static android.support.design.widget.Snackbar.make;
import static com.haui.activity.R.id.vf;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {
    private com.haui.log.Log log;
    private DatabaseReference database;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        reQuestPermistion();
        checkLogin();
    }

    private void reQuestPermistion() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},0);
            }
        }
    }
    private void checkLogin() {
        log=new Log(this);
        database = FirebaseDatabase.getInstance().getReference();
        creatView();
           if (log.getID().isEmpty()){
              startLogin();
            }else{
               login(log.getID(),log.getPass());
            }
    }
    public void signOut() {
        log.remove();
       startLogin();
    }
    public void startLogin() {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivityForResult(intent,2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==2){
            if (resultCode==RESULT_OK){
                creatData();
            }else{
                log.remove();
                finish();
            }

        }
    }

    private void creatData() {
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.map);
        setMap();
    }

    private void login(final String id, final String pass) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Đang đăng nhập...");
        dialog.setCancelable(false);
        dialog.show();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                     if (user.getPassWord().equals(pass)){
                         dialog.dismiss();
                         creatData();
                    }else {
                         dialog.dismiss();
                         Toast.makeText(NavigationActivity.this, "Sai mã sinh viên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                         startLogin();
                     }
                }catch (NullPointerException e){
                    dialog.dismiss();
                    Toast.makeText(NavigationActivity.this, "Sai mã sinh viên hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    startLogin();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        database.child("users").child(id).getRef().addValueEventListener(postListener);
    }
    private  NavigationView navigationView;
    private  ViewFlipper viewFlipper;
    private void creatView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         viewFlipper = (ViewFlipper)findViewById(vf);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setViewNullData();
    }
    private void setViewNullData() {
        viewFlipper.setDisplayedChild(0);
        TextView tvNull= (TextView) viewFlipper.findViewById(R.id.data_null);
        tvNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id == R.id.mn_user) {

        }else if (id == R.id.mn_history) {

        }else if (id == R.id.mn_nguoi_tim_xe) {
            viewFlipper.setDisplayedChild(2);
            FloatingActionButton fab = (FloatingActionButton) viewFlipper.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }else if (id == R.id.mn_xe_tim_nguoi) {
             setMap();
        }else if (id == R.id.mn_error) {
        }else if (id == R.id.mn_help) {
         }else if (id == R.id.mn_logout) {
             signOut();
        }else if (id == R.id.mn_exit) {
           finish();
         }else if (id == R.id.mn_dev) {

        }else if (id == R.id.mn_share) {
             Intent callIntent = new Intent(Intent.ACTION_CALL);
             callIntent.setData(Uri.parse("tel:" + "0986052482"));
             startActivity(callIntent);
             make(getCurrentFocus(), "mn_share", Snackbar.LENGTH_LONG)
                     .setAction("Action", null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setMap() {
        viewFlipper.setDisplayedChild(1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
       new MapManager(googleMap,this);
    }
}
