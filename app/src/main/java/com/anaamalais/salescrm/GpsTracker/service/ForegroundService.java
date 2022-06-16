package com.anaamalais.salescrm.GpsTracker.service;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.anaamalais.salescrm.HomeActivity;
import com.anaamalais.salescrm.Location.AppLocationService;
import com.anaamalais.salescrm.LoginActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private String TAG;
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    String status_code, msg, token, API_TOKEN;
    RequestQueue requestQueue;
    private Location gpsLocation;
    private static final int REQUEST_LOCATION = 104;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    String input = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        input = intent.getStringExtra("inputExtra");
        createNotificationChannel();

        startForeground(1, getNotification(input));

        new Handler().postDelayed(() -> updateActivity(),2000);


        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    public void updateActivity() {
        fetchLocation();
        API_TOKEN = MyFunctions.getSharedPrefs(this, Constants.TOKEN, "");
        requestQueue = Volley.newRequestQueue(this);
        StringRequest requests = new StringRequest(Request.Method.POST, users_updateactivity_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    timer2.cancel(); //this will cancel the timer of the system
                                    if (msg.equals("Invalid API_TOKEN!")) {
                                        MyFunctions.setSharedPrefs(getApplicationContext(), Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(getApplicationContext(), Constants.USER_ID, "");
                                        getApplicationContext().startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(getApplicationContext(), Constants.DEVICEID, "")));
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        }
//                        stopSelf();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("LATITUDE", String.valueOf(gpsLocation.getLatitude()));
                params.put("LONGITUDE", String.valueOf(gpsLocation.getLongitude()));
                System.out.println("PRINTF" + params);
                return params;
            }

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("API_TOKEN", API_TOKEN);

                return params;
            }

        };
        requestQueue.add(requests);

    }

    private void fetchLocation() {
        AppLocationService appLocationService = new AppLocationService(this);
        gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation == null) {
            gpsLocation = appLocationService
                    .getLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
    }


    private Notification getNotification(String input) {
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Updating Location...")
                .setContentText(input)
                .setSmallIcon(R.drawable.toyota_europe_logo)
                .setContentIntent(pendingIntent)
                .build();
        return notification;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}