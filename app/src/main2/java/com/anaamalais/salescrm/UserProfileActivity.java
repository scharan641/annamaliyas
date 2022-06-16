package com.anaamalais.salescrm;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.anaamalais.salescrm.GpsTracker.GpsTracker;
import com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Service.GPSTrackerWM;
import com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Utility.ClsCheckLocation;
import com.anaamalais.salescrm.GpsTracker.workmanagerLocation.Utility.ClsGlobal;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class UserProfileActivity extends AppCompatActivity {
    ImageView img_user_profile;
    String authentication_url = BASE_URL + "authentication/logout";
    String users_profiledetails_url = BASE_URL + "users/profiledetails";
    String users_startactivity_url = BASE_URL + "users/startactivity";
    String users_endactivity_url = BASE_URL + "users/endactivity";
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    private static final int PICK_IMAGE_REQUEST_GALLERY = 102;
    private static final int REQUEST_CAMERA = 101;
    private boolean mHasRequestedLocationPermission = false;
    private String[] locationPermission = {ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION};
    private String[] backgroundPermission = {Manifest.permission.ACCESS_BACKGROUND_LOCATION};
    private static final int LOCATION = 12;
    private static int BACKGROUND_LOCATION = 13;
    private static int PERMISSION_REQUEST_CODE = 15;
    private static final String TAG = "LocationUpdate";

    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    TextView txt_emp_name, txt_emp_id, txt_emp_email, txt_emp_mobile, txt_emp_age, txt_my_activity_value,
            txt_emp_dob, txt_start_acticity, txt_end_activity, txt_user_active_status;
    String name, emp_id, email, profile_image, dob, address, user_id, activity_status;
    int phone, user_type;
    private static final int pic_id = 123;
    String photoresult;
    private GpsTracker gpsTracker;
    double latitude, longitude;

    @Override
    protected void onStart() {
        super.onStart();
        checkBackgroundLocation();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = UserProfileActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(UserProfileActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(UserProfileActivity.this);
        img_user_profile = findViewById(R.id.img_user_profile);
        txt_emp_name = findViewById(R.id.txt_emp_name);
        txt_emp_id = findViewById(R.id.txt_emp_id);
        txt_emp_email = findViewById(R.id.txt_emp_email);
        txt_emp_mobile = findViewById(R.id.txt_emp_mobile);
        txt_emp_dob = findViewById(R.id.txt_emp_dob);
        txt_start_acticity = findViewById(R.id.txt_start_acticity);
        txt_end_activity = findViewById(R.id.txt_end_activity);
        txt_emp_age = findViewById(R.id.txt_emp_age);
        txt_my_activity_value = findViewById(R.id.txt_my_activity_value);
        txt_user_active_status = findViewById(R.id.txt_user_active_status);
        API_TOKEN = MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.TOKEN, "");

        img_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        initUI();
        Users_Profiledetails();

    }

    private void initUI() {
            if (isWorkScheduledByTag(TAG)){
                txt_start_acticity.setVisibility(View.GONE);
                txt_end_activity.setVisibility(View.VISIBLE);
                txt_user_active_status.setVisibility(View.VISIBLE);
            }else{
                txt_start_acticity.setVisibility(View.VISIBLE);
                txt_end_activity.setVisibility(View.GONE);
                txt_user_active_status.setVisibility(View.GONE);
            }

    }

    private boolean isWorkScheduledByTag(String tag) {
        WorkManager instance = WorkManager.getInstance();
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void endtactivity(View view) {
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
   /*     stopService(new Intent(getApplicationContext(),
                ForegroundService.class));
   */     getlatlon();
        Users_End_Activity();
    }

    private void checkBackgroundLocation() {
        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

        }*/
    }

    public void back(View view) {
        startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
        finish();
    }

    private boolean checkLocationPermission() {
        int result3 = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int result4 = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        return result3 == PackageManager.PERMISSION_GRANTED &&
                result4 == PackageManager.PERMISSION_GRANTED;
    }

    public void startactivity(View view) {
        if (!checkLocationPermission()) {
            checkBackgroundLocation();
        } else {
            Start_Activity();

//            startLocationTracking();
         /*   getlatlon();

            Handler handler;
            handler = new Handler();
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            handler.post(new Runnable() {
                                             public void run() {
                                                 updateActivity();
                                             }
                                         }
                            );
                            TimeUnit.MINUTES.sleep(1);
                        } catch (Exception ex) {

                        }
                    }
                }
            }).start();*/
        }
    }

    private void startLocationTracking() {
        ClsCheckLocation.checkLocationServiceNew(UserProfileActivity.this);
        ClsGlobal.ScheduleWorker(TAG, 15);

    /*    PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(LocationWorkManager.class, 1, TimeUnit.MINUTES)
                .addTag(TAG)
                .build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.REPLACE, periodicWork);

        Toast.makeText(UserProfileActivity.this, "Location Worker Started : " + periodicWork.getId(), Toast.LENGTH_SHORT).show();
*/
        try {
            if (isWorkScheduled(WorkManager.getInstance().getWorkInfosByTag(TAG).get())) {
                txt_end_activity.setVisibility(View.VISIBLE);
            } else {
                txt_start_acticity.setVisibility(View.VISIBLE);
         /*       mainBinding.appCompatButtonStart.setText(getString(R.string.button_text_start));
                mainBinding.message.setText(getString(R.string.message_worker_stopped));
                mainBinding.logs.setText(getString(R.string.log_for_stopped));*/
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private boolean isWorkScheduled(List<WorkInfo> workInfos) {
        boolean running = false;
        if (workInfos == null || workInfos.size() == 0) return false;
        for (WorkInfo workStatus : workInfos) {
            running = workStatus.getState() == WorkInfo.State.RUNNING | workStatus.getState() == WorkInfo.State.ENQUEUED;
        }
        return running;
    }


    private void getlatlon() {
        GPSTrackerWM gps = new GPSTrackerWM(UserProfileActivity.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

      /*      gpsTracker = new GpsTracker(UserProfileActivity.this);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }*/
    }


    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && data != null && data.getExtras() != null) {

            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            // byte[] byteArray = stream.toByteArray();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            photoresult = Base64.encodeToString(b, Base64.DEFAULT);
            //System.out.println("xhxdhhd"+photoresult);
            Glide.with(UserProfileActivity.this)
                    .load(photo)
                    .circleCrop()
                    .into(img_user_profile);
            // photoresult = Base64.encodeToString(byteArray, Base64.DEFAULT);
            //  Toast.makeText(UserProfileActivity.this, photoresult, Toast.LENGTH_SHORT).show();

            // img_user_profile.setImageBitmap(photo);
            return;

        } else if (resultCode != pic_id) {
            //Here you can handle,do anything you want
            finish();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission is required", Toast.LENGTH_SHORT).show();
                checkBackgroundLocation();
            }


        }
    }

    public void logout(View view) {
        final Dialog dialog = new Dialog(UserProfileActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_logout);

        Button yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button no = (Button) dialog.findViewById(R.id.btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MyFunctions.setSharedPrefs(ProfileActivity.this, Constants.CUSTOMERID, "");
             /*   stopService(new Intent(getApplicationContext(),
                        ForegroundService.class));
             */
                WorkManager.getInstance().cancelAllWorkByTag(TAG);
                Users_End_Activity();
                Authentication_Logout();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // Api call Users_Profiledetails
    public void Users_Profiledetails() {
        StringRequest requests = new StringRequest(Request.Method.POST, users_profiledetails_url, new Response.Listener<String>() {
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

                            JSONObject profile_details = jsonObj.getJSONObject("profile_details");

                            emp_id = (String) profile_details.get("emp_id");
                            System.out.println("The emp_id" + emp_id);
                            txt_emp_id.setText(emp_id);

                            name = (String) profile_details.get("name");
                            txt_emp_name.setText(name);

                            email = (String) profile_details.get("email");
                            txt_emp_email.setText(email);

                            profile_image = (String) profile_details.get("profile_image");

                            // Glide.with(UserProfileActivity.this)
                            //     .load(profile_image)
                            //   .circleCrop()
                            //    .into(img_user_profile);

                            phone = (Integer) profile_details.getInt("phone");
                            String userphone = Integer.toString(phone);
                            txt_emp_mobile.setText(userphone);

                            dob = (String) profile_details.get("dob");

                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                            try {

                                Date newDate = spf.parse(dob);
                                spf = new SimpleDateFormat("dd MMM yyyy");
                                String newDateString = spf.format(newDate);
                                txt_emp_dob.setText(newDateString);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            address = (String) profile_details.get("address");

                            user_id = (String) profile_details.get("user_id");

                            user_type = (Integer) profile_details.getInt("user_type");

                            int age = (Integer) profile_details.getInt("age");
                            txt_emp_age.setText(String.valueOf(age));

                            String my_activity = (String) profile_details.get("my_activity");
                            txt_my_activity_value.setText(my_activity);

                            int activitystatus = (Integer) profile_details.getInt("activity_status");
                            activity_status = String.valueOf(activitystatus);
                           /* if (activity_status.equals("1")) {
                                txt_end_activity.setVisibility(View.VISIBLE);
                                txt_start_acticity.setVisibility(View.GONE);
                                txt_user_active_status.setVisibility(View.VISIBLE);
                            } else {
                                txt_start_acticity.setVisibility(View.VISIBLE);
                                txt_end_activity.setVisibility(View.GONE);
                                txt_user_active_status.setVisibility(View.GONE);
                            }*/

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    // Api call Start_Activity
    public void Start_Activity() {
        StringRequest requests = new StringRequest(Request.Method.POST, users_startactivity_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               /* Intent service = new Intent(getApplicationContext(),
                        ForegroundService.class);
                startService(service);*/

                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            startLocationTracking();
                            activity_status = "1";
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            txt_end_activity.setVisibility(View.VISIBLE);
                            txt_start_acticity.setVisibility(View.GONE);
                            txt_user_active_status.setVisibility(View.VISIBLE);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    public void Users_End_Activity() {
        StringRequest requests = new StringRequest(Request.Method.POST, users_endactivity_url, new Response.Listener<String>() {
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
                            txt_end_activity.setVisibility(View.GONE);
                            txt_start_acticity.setVisibility(View.VISIBLE);
                            txt_user_active_status.setVisibility(View.GONE);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    public void updateActivity() {
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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


    // Api call Authentication_Logout
    public void Authentication_Logout() {

        StringRequest request = new StringRequest(Request.Method.POST, authentication_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            System.out.println("Check statusMessage of Login Activity:" + token);
                            MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                            MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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
                                    MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.IS_LOGIN, false);
                                    MyFunctions.setSharedPrefs(UserProfileActivity.this, Constants.USER_ID, "");
                                    startActivity(new Intent(UserProfileActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(UserProfileActivity.this, Constants.DEVICEID, "")));
                                    finish();
                                }
                            }, 3000); // the timer will count 5 seconds....


                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
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

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("API_TOKEN", API_TOKEN);

                return params;
            }

        };
        requestQueue.add(request);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getlatlon();
    }
}
