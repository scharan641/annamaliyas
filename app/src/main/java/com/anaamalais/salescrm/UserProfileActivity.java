package com.anaamalais.salescrm;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;
import static com.anaamalais.salescrm.Utils.Constants.TIME;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.anaamalais.salescrm.data.Annamalaiapiserveice;
import com.anaamalais.salescrm.data.Client;
import com.anaamalais.salescrm.data.ImageUpload;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

public class UserProfileActivity extends AppCompatActivity {
    CircleImageView img_user_profile;
    String authentication_url = BASE_URL + "authentication/logout";
    String users_profiledetails_url = BASE_URL + "users/profiledetails";
    String users_startactivity_url = BASE_URL + "users/startactivity";
    String users_endactivity_url = BASE_URL + "users/endactivity";
    String users_updateactivity_url = BASE_URL + "users/updateuserlocation";
    String updateUser = BASE_URL + "users/updateuserlocation";
    private String permissionsCAMERA[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private static final int REQUEST_CAMERA = 101;
    private boolean mHasRequestedLocationPermission = false;
    private String[] locationPermission = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    private String[] backgroundPermission = {ACCESS_BACKGROUND_LOCATION};
    private android.app.AlertDialog.Builder alertDialog2;
    private static int BACKGROUND_LOCATION = 13;
    private static int LOCATION_PERMISSION_REQUEST_CODE = 15;
    private static int BATTERY_OPTIMIZATION = 16;
    private static final String TAG = "LocationUpdate";
    private static int PICK_IMAGE_REQUEST_GALLERY = 102;
    private static int REQUEST_CAMERA_PERMISSION = 1002;
    private static int REQUEST_GALLERY_PERMISSION = 1003;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    TextView txt_emp_name, txt_emp_id, txt_emp_email, txt_emp_mobile, txt_emp_age, txt_my_activity_value,
            txt_emp_dob, txt_start_acticity, txt_end_activity, txt_user_active_status;
    String name, emp_id, email, profile_image, dob, address, user_id, activity_status;
    int phone, user_type;
    Long phone2;
    private static final int pic_id = 123;
    String photoresult;
    private GpsTracker gpsTracker;
    double latitude, longitude;
    private PowerManager pm;
    private File imageToUpload;
    public Annamalaiapiserveice apiservice ;



    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

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

        apiservice = Client.getRetrofitInstance().create(Annamalaiapiserveice.class);

        img_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the camera_intent ACTION_IMAGE_CAPTURE
                // it will open the camera for capture the image
                showImageDialog();

            }
        });
        requestLocationPermission();

        initUI();

        Users_Profiledetails();

    }

    private void showImageDialog(){
        alertDialog2 = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View add_menu_layout = inflater.inflate(R.layout.image_mode_upload, null);
        LinearLayout layout_gallery = add_menu_layout.findViewById(R.id.layout_gallery);
        LinearLayout layout_camera = add_menu_layout.findViewById(R.id.layout_camera);
        alertDialog2.setView(add_menu_layout);
        alertDialog2.setCancelable(true);
        final android.app.AlertDialog testDialog = alertDialog2.create();
        final TextView tv_camera = add_menu_layout.findViewById(R.id.tv_camera);
        final TextView tv_gallery = add_menu_layout.findViewById(R.id.tv_gallery);
        layout_camera.setOnClickListener(view2 -> {
            openCamera();
//            if (imageToUpload != null){
//                updateImageurl();
//            }
//            Handler handler;
//            handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    updateImageurl();
//                }
//            },2000);
            testDialog.dismiss();


        });
        layout_gallery.setOnClickListener(view2 -> {
            choosephoto();
//            Handler handler;
//            handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    updateImageurl();
//                }
//            },2000);
            testDialog.dismiss();


        });
        testDialog.show();
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }


    private void updateImageurl() {

        MultipartBody.Part multipartImage = null;

        if (imageToUpload != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), imageToUpload);
            multipartImage =
                    MultipartBody.Part.createFormData("PROFILE_IMAGE", imageToUpload.getName(), requestFile);
        }

        if (API_TOKEN != null) {

            apiservice.updateImageurl(API_TOKEN, multipartImage).enqueue(new Callback<ImageUpload>() {
                @Override
                public void onResponse(Call<ImageUpload> call, retrofit2.Response<ImageUpload> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 0) {
                            Toast.makeText(UserProfileActivity.this, "API_TOKEN is missing!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserProfileActivity.this, "uploaded", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ImageUpload> call, Throwable t) {

                }
            });
        }

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

    private void requestBackgroundLocation(){
        if (ContextCompat.checkSelfPermission(this, backgroundPermission[0]) == PackageManager.PERMISSION_GRANTED) {
        }else{
            ActivityCompat.requestPermissions(this,backgroundPermission,BACKGROUND_LOCATION);
        }
    }


    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

        }
    }

    private void batteryOptimisation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = "com.anaamalais.salescrm";
            // pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);

            }
        }
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

    private boolean checkBatteryIsOptimized(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (pm.isIgnoringBatteryOptimizations("com.anaamalais.salescrm")) {
              return true;
            } else {
                return false;
            }
        }else{
            return true;
        }
    }

    private boolean checkIsBackgroundGranted() {
        if (ContextCompat.checkSelfPermission(this, backgroundPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            return  true;
        }else{
            requestBackgroundLocation();
            return false;
        }
    }

    public void startactivity(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!checkLocationPermission()) {
                requestLocationPermission();
            }else if(!checkIsBackgroundGranted()){
                requestBackgroundLocation();
            }else if(!checkBatteryIsOptimized()){
                batteryOptimisation();
            } else {
                WorkManager.getInstance().cancelAllWorkByTag(TAG);
                Start_Activity();
            }
        }else {
            if (!checkLocationPermission()) {
                requestLocationPermission();
            } else {
                WorkManager.getInstance().cancelAllWorkByTag(TAG);
                Start_Activity();
            }

        }



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


    private void openCamera() {
        if (EasyPermissions.hasPermissions(this, permissionsCAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);

        } else {
            EasyPermissions.requestPermissions(this, null,
                    REQUEST_CAMERA_PERMISSION, permissionsCAMERA);
        }
    }

    private void choosephoto() {
        if (EasyPermissions.hasPermissions(this, permissionsCAMERA)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), PICK_IMAGE_REQUEST_GALLERY);

        } else {
            EasyPermissions.requestPermissions(this, null,
                    REQUEST_GALLERY_PERMISSION, permissionsCAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA && data != null && data.getExtras() != null) {
            try {
                Bundle bundle = data.getExtras();
                Bitmap bmp = (Bitmap) bundle.get("data");
                Uri tempUri = getImageUri(getApplicationContext(), bmp);

                imageToUpload = new File(getRealPathFromURI(tempUri));
                updateImageurl();

                Glide.with(this).load(imageToUpload).transform(new CircleCrop()).error(R.drawable.ic_add_image)
                        .placeholder(R.drawable.ic_add_image).apply(RequestOptions.circleCropTransform()).into(img_user_profile);

            } catch (Exception e) {
                e.getMessage();
            }


        }
        if (requestCode == PICK_IMAGE_REQUEST_GALLERY && resultCode == RESULT_OK
                && null != data) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            imagesEncodedList = new ArrayList<String>();
//            if (data.getClipData() == null) {
            Uri tempUri = data.getData();
            imageToUpload = new File(getPath(tempUri));
            updateImageurl();
            Glide.with(this).load(imageToUpload).transform(new CircleCrop()).error(R.drawable.ic_add_image)
                    .placeholder(R.drawable.ic_add_image).apply(RequestOptions.circleCropTransform()).into(img_user_profile);

//            }
        }

    }
    // This method will help to retrieve the image


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == BACKGROUND_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                batteryOptimisation();
            } else {
//                Toast.makeText(this, "Permission is required", Toast.LENGTH_SHORT).show();
                requestLocationPermission();
            }


        }else if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Location Permission is required", Toast.LENGTH_SHORT).show();
            }else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestBackgroundLocation();
            }else{
                Toast.makeText(this, "Background Location Permission is required", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == BATTERY_OPTIMIZATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//                Toast.makeText(this, "Permission is required", Toast.LENGTH_SHORT).show();
            }else{
                batteryOptimisation();
            }

        }

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {

            }
        }
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosephoto();
            } else {

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

//                            profile_image = (String) profile_details.get("profile_image");

                            // Glide.with(UserProfileActivity.this)
                            //     .load(profile_image)
                            //   .circleCrop()
                            //    .into(img_user_profile);

//                            phone = (Integer) profile_details.getInt("phone");
                            phone2 = profile_details.getLong("phone");
                            String userphone = String.valueOf(phone2);
                            txt_emp_mobile.setText(userphone);

                            profile_image = profile_details.getString("profile_image");
                            String userImage =profile_image;
//                            try {
//                                img_user_profile.setImageResource(Integer.parseInt(userImage));
//                            } catch (Exception e){
//                                img_user_profile.setImageResource(Integer.parseInt(userImage));
//
//                            }
                            Glide.with(UserProfileActivity.this).load(profile_details.getString("profile_image")).error(R.drawable.profile).into(img_user_profile);


//                            Glide.with(UserProfileActivity.this).load(user.getProfilePath()).error(R.drawable.profile_user_icon).into(profilePicture);


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
//                            if (activity_status.equals("0")) {
//                                try {
//                                    if (isWorkScheduledByTag(TAG)) {
//                                        txt_start_acticity.setVisibility(View.GONE);
//                                        txt_end_activity.setVisibility(View.VISIBLE);
//                                        txt_user_active_status.setVisibility(View.VISIBLE);
//                                    } else {
//                                        txt_start_acticity.setVisibility(View.VISIBLE);
//                                        txt_end_activity.setVisibility(View.GONE);
//                                        txt_user_active_status.setVisibility(View.GONE);
//                                    }
//                                } catch (Exception e) {
//
//                                }
//
//                            }
//                            else {
////                                WorkManager.getInstance().cancelAllWorkByTag(TAG);
//
//                            }


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
        finish();
    }
}
