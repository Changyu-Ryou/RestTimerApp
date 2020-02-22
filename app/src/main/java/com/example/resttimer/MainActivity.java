package com.example.resttimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements Frag1_Timer.onButtonClickListener {
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    public static FragmentManager fm;
    private FragmentTransaction ft;

    static Activity activity;
    static int min=0;
    static int sec=0;


    public static Frag1_Timer frag1Timer;
    private Frag3_Setting frag3Setting;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity=this;
        frag1Timer = new Frag1_Timer();
        frag3Setting = new Frag3_Setting();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_timer:
                        setFrag(0);
                        break;

                    case R.id.action_setting:

                        if(MyService.widFlag==0){//!isServiceRunningCheck()) {      //위젯 작동중
                            System.out.println("if문");
                            setFrag(1);
                        }else{
                            System.out.println("else문");
                            setFrag(0);
                            Toast.makeText(activity, "Change permission setting. You have to turn on the screen overlay from Setting > Apps",
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });


        setFrag(0); // 첫 프래그먼트 화면 지정

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        } else {
            startService(new Intent(MainActivity.this, MyService.class));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // TODO 동의를 얻지 못했을 경우의 처리


            } else {
                startService(new Intent(MainActivity.this, MyService.class));
            }
        }
    }


    // 프레그먼트 교체
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_layout, frag1Timer);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.main_layout, frag3Setting);
                ft.commit();
                break;


        }
    }

    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("MyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    @Override
    public void onButtonClick(int Button) {
        if(Button==1){
            //ft.replace(R.id.main_coordinatorlayout, widtimer);
            if(!isServiceRunningCheck()){
                MyService.mHandler.removeMessages(0);
                MyService.widFlag=1;
                stopService(new Intent(MainActivity.this, MyService.class));
            }
            checkPermission();
            MyService.widFlag=0;
            //MyService.widFlag=0;
        }else if(Button==2){
            MyService.widFlag=1;
            MyService.mHandler.removeMessages(0);
            stopService(new Intent(MainActivity.this, MyService.class));
            setFrag(0);
        }
    }


}