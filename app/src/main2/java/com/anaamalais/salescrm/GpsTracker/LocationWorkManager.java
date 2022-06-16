package com.anaamalais.salescrm.GpsTracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.anaamalais.salescrm.LoginActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.UserProfileActivity;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

public class LocationWorkManager extends Worker {

    private static final String DEFAULT_START_TIME = "08:00";
    private static final String DEFAULT_END_TIME = "19:00";
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    String status_code, msg, token, API_TOKEN;
    RequestQueue requestQueue;


    private static final String TAG = "LocationWorkManager";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The current location.
     */
    private Location mLocation;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    private Context mContext;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    public LocationWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Done");

        Log.d(TAG, "onStartJob: STARTING JOB..");

        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String formattedDate = dateFormat.format(date);

        try {
            Date currentDate = dateFormat.parse(formattedDate);
            Date startDate = dateFormat.parse(DEFAULT_START_TIME);
            Date endDate = dateFormat.parse(DEFAULT_END_TIME);

//            if (currentDate.after(startDate) && currentDate.before(endDate)) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                    }
                };

                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                try {
                    mFusedLocationClient
                            .getLastLocation()
                            .addOnCompleteListener(new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        mLocation = task.getResult();
                                        Log.d(TAG, "Location : " + mLocation);
                                        updateActivity(mLocation);

                                        // Create the NotificationChannel, but only on API 26+ because
                                        // the NotificationChannel class is new and not in the support library
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = mContext.getString(R.string.app_name);
                                            String description = mContext.getString(R.string.app_name);
                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                            NotificationChannel channel = new NotificationChannel(mContext.getString(R.string.app_name), name, importance);
                                            channel.setDescription(description);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }

                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.app_name))
                                                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                                                .setContentTitle("New Location Update")
                                                .setContentText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude()))
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setStyle(new NotificationCompat.BigTextStyle().bigText("You are at " + getCompleteAddressString(mLocation.getLatitude(), mLocation.getLongitude())));

                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

                                        // notificationId is a unique int for each notification that you must define
                                        notificationManager.notify(1001, builder.build());

                                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                                    } else {
                                        Log.w(TAG, "Failed to get location.");
                                    }
                                }
                            });
                } catch (SecurityException unlikely) {
                    Log.e(TAG, "Lost location permission." + unlikely);
                }

                try {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, null);
                } catch (SecurityException unlikely) {
                    //Utils.setRequestingLocationUpdates(this, false);
                    Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
                }
           /* } else {
                Log.d(TAG, "Time up to get location. Your time is : " + DEFAULT_START_TIME + " to " + DEFAULT_END_TIME);
            }*/
        } catch (ParseException ignored) {

        }

        return Result.success();
    }

    public void updateActivity(Location mLocation) {
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
//                            txt_end_activity.setVisibility(View.GONE);
//                            txt_start_acticity.setVisibility(View.VISIBLE);
//                            txt_user_active_status.setVisibility(View.GONE);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                    if (msg.equals("Invalid API_TOKEN!")) {
                                        MyFunctions.setSharedPrefs(getApplicationContext(), Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(getApplicationContext(), Constants.USER_ID, "");
                                        getApplicationContext().startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(getApplicationContext(), Constants.DEVICEID, "")));
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        }
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
                params.put("LATITUDE", String.valueOf(mLocation.getLatitude()));
                params.put("LONGITUDE", String.valueOf(mLocation.getLongitude()));
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


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
