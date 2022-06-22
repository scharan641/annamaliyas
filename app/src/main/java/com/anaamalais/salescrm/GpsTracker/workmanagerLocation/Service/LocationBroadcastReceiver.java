package com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Service;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Utility.ClsGlobal;
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

public class LocationBroadcastReceiver extends BroadcastReceiver {
    double latitude = 0.0;
    double longitude = 0.0;
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    String status_code, msg, token, API_TOKEN;
    RequestQueue requestQueue;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Receiver", "onReceive call");

        String str = "";
        String LocationStatus = "";

        if (ClsGlobal.checkPermission(context)) {
            LocationStatus = LocationStatus.concat("Location Permission Granted ");
        } else {
            LocationStatus = LocationStatus.concat("Location Permission Not Granted ");
        }

        GPSTrackerWM gps = new GPSTrackerWM(context);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            new Handler().postDelayed(() -> updateActivity(context),2000);


//            str = "Your Location is - \nLat: " + latitude
//                    + "\nLog: " + longitude;

            str = "Your Location is Updating";

            LocationStatus = LocationStatus.concat("and Location is Enable. ");


            sendNotification("Location", "onStartJob: " + str, "", context);
        } else {
            LocationStatus = LocationStatus.concat("and Location is Not Enable. ");
        }

        Log.e("LocationStatus", LocationStatus);

/*
        Repository repository = new Repository(context);
        repository.Insert(new LogsEntity(latitude, longitude,
                getEntryDateFormat(getCurruntDateTime()), LocationStatus));
*/


        gps.stopUsingGPS();

    }

    public void updateActivity(Context context) {
//        fetchLocation();
        API_TOKEN = MyFunctions.getSharedPrefs(context, Constants.TOKEN, "");
        requestQueue = Volley.newRequestQueue(context);
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
                                        MyFunctions.setSharedPrefs(context, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(context, Constants.USER_ID, "");
                                        context.startActivity(new Intent(context, LoginActivity.class)
                                                .putExtra("DEVICEID", MyFunctions.getSharedPrefs(context, Constants.DEVICEID, "")));
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

    public static void sendNotification(String title, String message, String mode, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
