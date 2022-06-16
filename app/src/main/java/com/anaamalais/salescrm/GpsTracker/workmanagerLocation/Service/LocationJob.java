package com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Service;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LocationJob extends JobService {

    private static final String TAG = "Location Updating...";
    double latitude;
    double longitude;
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    String status_code, msg, token, API_TOKEN;
    RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Service created");

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "onStartJob");

        String str = "";
        GPSTrackerWM gps = new GPSTrackerWM(this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            new Handler().postDelayed(() -> updateActivity(),2000);

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            str ="Your Location is - \nLat: " + latitude
                    + "\nLog: " + longitude;

//            Repository repository = new Repository(this);
//            repository.Insert(new LogsEntity(latitude,longitude,getEntryDateFormat(getCurruntDateTime())));

        }

        sendNotification("LocationJob","onStartJob: " + str,"",this);

        gps.stopUsingGPS();

        jobFinished(params,true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobFinished(params,true);
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Service destroyed");
    }

    public void updateActivity() {
//        fetchLocation();
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
                params.put("LATITUDE", String.valueOf(latitude));
                params.put("LONGITUDE", String.valueOf(longitude));
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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return START_NOT_STICKY;
    }


    public static void sendNotification(String title, String message, String mode, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "default")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_toyota_europe_logo);

        if (mode.equalsIgnoreCase("Send PDF")) {
            notification.setOngoing(true);
        }

        notificationManager.notify(1, notification.build());
    }




}
