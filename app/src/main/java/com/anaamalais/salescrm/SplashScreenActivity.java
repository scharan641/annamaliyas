package com.anaamalais.salescrm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    String device_id;

    //After completion of 2000 ms, the next activity will get started.
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //This method is used so that your splash activity
            //can cover the entire screen.

            setContentView(R.layout.activity_splash_screen);
            //this will bind your MainActivity.class file with activity_main.

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Getting the device id
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        device_id = telephonyManager.getImei();
                       // MyFunctions.setSharedPrefs(SplashScreenActivity.this, Constants.DEVICEID,device_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                        device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                      //  MyFunctions.getSharedPrefs(SplashScreenActivity.this, Constants.DEVICE_ID,device_id);
                    }
                }
            } else {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    device_id = telephonyManager.getDeviceId();
                   // MyFunctions.setSharedPrefs(SplashScreenActivity.this, Constants.DEVICE_ID,device_id);
                }
            } else {
                ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1010);
            }
        }
device_id = "3c85dbe138e267f1";
        if (device_id!=null && !device_id.equals("")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(SplashScreenActivity.this,
                            LoginActivity.class);
                    //Intent is used to switch from one activity to another.

                    i.putExtra("DEVICEID",device_id);

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();
                    //the current activity will get finished.
                }
            }, SPLASH_SCREEN_TIME_OUT);
        }

    }

}
