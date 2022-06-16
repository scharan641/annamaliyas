package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.anaamalais.salescrm.Utils.PreferenceManager;
import com.anaamalais.salescrm.Utils.RequestPermissionHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleForTheDriveActivity extends AppCompatActivity implements ImageChooserListener {
    TextView edt_time , txt_update_status , txt_edit_schedule;
    EditText edt_date;
    File file;
    int mYear, mDay ,mMonth,mMinute,mHour;
    ImageView img_camera , Img_dl_photo;
    ImageChooserManager imageChooserManager;
    private int chooserType;
    String driving_licence= "";
    String filePath ;
    String TEST_DRIVE_DETAILS_ID , LICENCE_NUMBER , SCHEDULED_ON , DRIVING_LICNECE;
    String uploadImagePath, uploadImageFile = "", selectedCity = "", selectedCityName = "";
    private final int CAMERA_REQUEST = 20;
    private static int RESULT_LOAD_IMAGE = 1;
    TextInputEditText edt_license_no;
    SwitchCompat swt_add_to_task;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String UPDATE_ENQUIRY_ID ,  driving_licence_number  , scheduled_date , scheduloed_time , contact_id , contact_status;
    String  status_code , msg ,token, API_TOKEN,UPDATE_CONTACT_ID,scheduled_status;
    String testdrive_scheduletestdrive_url = BASE_URL + "testdrive/scheduletestdrive";
    String testdrive_updatetestdrive_url = BASE_URL + "testdrive/updatetestdrive";
    String testdrive_gettestdrivedetails_url = BASE_URL + "testdrive/gettestdrivedetails";
    PreferenceManager preferenceManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_for_test_drive);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = ScheduleForTheDriveActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ScheduleForTheDriveActivity.this, R.color.white));
        preferenceManager = new PreferenceManager(this);
        requestQueue = Volley.newRequestQueue(ScheduleForTheDriveActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(ScheduleForTheDriveActivity.this, Constants.TOKEN,"");
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        img_camera = findViewById(R.id.img_camera);
        Img_dl_photo  = findViewById(R.id.Img_dl_photo);
        edt_license_no = findViewById(R.id.edt_license_no);
        swt_add_to_task = findViewById(R.id.swt_add_to_task);
        txt_update_status = findViewById(R.id.txt_update_status);
        txt_edit_schedule = findViewById(R.id.txt_edit_schedule);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        edt_date.setText(strDates);
        scheduled_status = getIntent().getStringExtra("SCHEDULED_STATUS");
              //
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
       if (scheduled_status.equals("NEW SCHEDULED")){
           txt_update_status.setVisibility(View.VISIBLE);
           txt_edit_schedule.setVisibility(View.GONE);

       }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){
           txt_update_status.setVisibility(View.GONE);
           txt_edit_schedule.setVisibility(View.VISIBLE);
           Get_Test_Drive_Details();

       }

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleForTheDriveActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edt_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        edt_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edt_date.getRight() - edt_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        
                        DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleForTheDriveActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                        edt_date.setText(strDate);
                                        //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                        // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ScheduleForTheDriveActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(ScheduleForTheDriveActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ScheduleForTheDriveActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
                    ContextCompat.checkSelfPermission(ScheduleForTheDriveActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(ScheduleForTheDriveActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    takePicture();
                }

            }
        });


        swt_add_to_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }else {

                }
            }
        });
    }



    public void updatestatus(View view) {
    /*   driving_licence_number = edt_license_no.getText().toString().trim();
       if (driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)){
           edt_license_no.setError("Please Fill the Correct Driving Licence Number");
       }*/

        String followupdate = edt_date.getText().toString().trim();
        if (followupdate.equals("") || followupdate.isEmpty()) {
            edt_date.setError("Please Selecte Dob");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date newDate = spf.parse(followupdate);
                spf = new SimpleDateFormat("yyyy-MM-dd");
                scheduled_date = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String scheduloedtime = edt_time.getText().toString().trim()+":"+"00";
        if (scheduloedtime.equals("") || scheduloedtime.isEmpty()) {
            edt_time.setError("Please Selecte Time");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spf.parse(scheduloedtime);
                spf = new SimpleDateFormat("HH:mm");
                scheduloed_time = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)
        if (scheduled_date.equals("")||scheduloed_time.equals("")){
            Toast.makeText(ScheduleForTheDriveActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
        }else {
            Schedule_Test_Drive();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(ScheduleForTheDriveActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();
        }
    }


    public void editschedule(View view) {
       /* driving_licence_number = edt_license_no.getText().toString().trim();
        if (driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)){
            edt_license_no.setError("Please Fill the Correct Driving Licence Number");
        }*/

        String followupdate = edt_date.getText().toString().trim();
        if (followupdate.equals("") || followupdate.isEmpty()) {
            edt_date.setError("Please Selecte Dob");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date newDate = spf.parse(followupdate);
                spf = new SimpleDateFormat("yyyy-MM-dd");
                scheduled_date = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (driving_licence.equals("")||driving_licence==null){
            driving_licence = "";
        }

        String scheduloedtime = edt_time.getText().toString().trim()+":"+"00";
        if (scheduloedtime.equals("") || scheduloedtime.isEmpty()) {
            edt_time.setError("Please Selecte Time");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spf.parse(scheduloedtime);
                spf = new SimpleDateFormat("HH:mm");
                scheduloed_time = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)||

        if (scheduled_date.equals("")||scheduloed_time.equals("")){
            Toast.makeText(ScheduleForTheDriveActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
        }else {
            Edit_Schedule_Test_Drive();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(ScheduleForTheDriveActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();
        }
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(ScheduleForTheDriveActivity.this);
        try {

           filePath = imageChooserManager.choose();
                   //Environment.getExternalStorageDirectory().getPath();
                  //  =
            //  Toast.makeText(ScheduleForTheDriveActivity.this, filePath, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK &&
                (requestCode == ChooserType.REQUEST_PICK_PICTURE||
                        requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            switch (requestCode) {
                case ChooserType.REQUEST_PICK_PICTURE:
                case ChooserType.REQUEST_CAPTURE_PICTURE:
                    if (imageChooserManager == null) {
                        reinitializeImageChooser();
                    }
                    imageChooserManager.submit(requestCode, data);
                    break;
            }
        }
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String TAG = "ICA";
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());


                driving_licence = image.getFilePathOriginal();
                MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.DLPHOTO,driving_licence);
              //  file = new File(driving_licence);

                System.out.println("fhfhf"+driving_licence);
                    Img_dl_photo.setVisibility(View.VISIBLE);
                    Glide.with(ScheduleForTheDriveActivity.this).load(driving_licence)
                           // .circleCrop()
                            //  .thumbnail(0.5f)
                            //  .bitmapTransform(new Circul(ActivityHome.this))
                            //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(Img_dl_photo);

               /* Glide.with(ActivityAccountList.this).load(uploadImagePath)
                        .circleCrop()
                        //  .thumbnail(0.5f)
                        //  .bitmapTransform(new Circul(ActivityHome.this))
                        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imyg_profile);*/

                //  uploadImagePath = image.getFileThumbnailSmall();
                // uploadImagePath = MyFunctions.compressImage(ProfileFragment.this,image.getFilePathOriginal(),80);
                // Bitmap bitmap = BitmapFactory.decodeFile(tt);

                //encode image to base64 string


                // uploadImagePath = bitmap.toString();

                if (driving_licence != null) {
                    // uploadImagePath = MyFunctions.convertImageToString(bitmap);
                    // loadImage(updateprofileIMG, image.getFileThumbnail());
                    img_camera.setVisibility(View.GONE);
                    Img_dl_photo.setVisibility(View.VISIBLE);
                    Log.i(TAG, "Chosen Image: Is not null");
                    //Upload();

                }
                //  if (uploadImagePath != null) {

                //loadImage(updateprofileIMG, image.getFileThumbnail());
                // }
                else {
                    img_camera.setVisibility(View.VISIBLE);
                    Img_dl_photo.setVisibility(View.GONE);
                    Log.i(TAG, "Chosen Image: Is null");
                    driving_licence = "";

                }
            }
        });
    }

    @Override
    public void onError(String s) {
        //  MyFunctions.toastShort(this, s);
        Toast.makeText(getApplicationContext(), "Image Upload Not Done", Toast.LENGTH_SHORT).show();
    }


    public void Schedule_Test_Drive(){
        StringRequest requests = new StringRequest(Request.Method.POST, testdrive_scheduletestdrive_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("data");
                            contact_id = jsonObject.getString("contact_id");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPID,followupid);
                          //  String dl_number = jsonObject.getString("dl_number");
                          //  MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.DLNUMBER,dl_number);
                            String test_drive_id = jsonObject.getString("test_drive_id");
                            preferenceManager.setTestdriveid(test_drive_id);
//                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.TESTDRIVEID,test_drive_id);
                          /* if (jsonObject.isNull("dl_photo")){
                               System.out.println("x"+"dl_photo");
                           }else {
                               String dl_photo = jsonObject.getString("dl_photo");
                               MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.DLPHOTO,dl_photo);
                           }*/
                            String td_scheduled_on = jsonObject.getString("td_scheduled_on");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.TDSCHEDULEDON,td_scheduled_on);
                            startActivity(new Intent(ScheduleForTheDriveActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);
             //   params.put("DRIVING_LICENCE_NUMBER", driving_licence_number);
             //   params.put("DRIVING_LICENCE", driving_licence);
                params.put("SCHEDULED_DATE", scheduled_date);
                params.put("SCHEDULED_TIME", scheduloed_time);
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


    public void Get_Test_Drive_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, testdrive_gettestdrivedetails_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                           JSONObject jsonObject = jsonObj.getJSONObject("test_drive_details");
                            TEST_DRIVE_DETAILS_ID = jsonObject.getString("TEST_DRIVE_DETAILS_ID");
                         //   LICENCE_NUMBER = jsonObject.getString("LICENCE_NUMBER");
                          //  edt_license_no.setText(LICENCE_NUMBER);
                            SCHEDULED_ON = jsonObject.getString("SCHEDULED_ON");
                            DRIVING_LICNECE = jsonObject.getString("DRIVING_LICNECE");
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = spf.parse(SCHEDULED_ON);
                                spf = new SimpleDateFormat("dd/MM/yyyy");
                                String followupdate = spf.format(newDate);
                                edt_date.setText(followupdate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date newDate = spfe.parse(SCHEDULED_ON);
                                spfe = new SimpleDateFormat("HH:mm");
                                String followupdate = spfe.format(newDate);
                                edt_time.setText(followupdate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);
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



    public void Edit_Schedule_Test_Drive(){
        StringRequest requests = new StringRequest(Request.Method.POST, testdrive_updatetestdrive_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("data");
                            contact_id = jsonObject.getString("contact_id");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.FOLLOWUPID,followupid);
                            //String dl_number = jsonObject.getString("dl_number");
                           // MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.DLNUMBER,dl_number);
                            String test_drive_id = jsonObject.getString("test_drive_id");
//                            preferenceManager.setTestdriveid();
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.TESTDRIVEID,test_drive_id);
                            startActivity(new Intent(ScheduleForTheDriveActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                            if (jsonObject.isNull("dl_photo")){
                                System.out.println("x"+"dl_photo");
                            }else {
                                String dl_photo = jsonObject.getString("dl_photo");
                                MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.DLPHOTO,dl_photo);
                            }
                            String td_scheduled_on = jsonObject.getString("td_scheduled_on");
                            MyFunctions.setSharedPrefs(ScheduleForTheDriveActivity.this,Constants.TDSCHEDULEDON,td_scheduled_on);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleForTheDriveActivity.this);
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
                params.put("CONTACT_ID",UPDATE_ENQUIRY_ID);
            //    params.put("DRIVING_LICENCE_NUMBER", driving_licence_number);
              //  params.put("DRIVING_LICENCE", driving_licence);
                params.put("SCHEDULED_DATE", scheduled_date);
                params.put("SCHEDULED_TIME", scheduloed_time);
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

    public void back(View view) {
        startActivity(new Intent(ScheduleForTheDriveActivity.this,CustomerDetailsActivity.class).putExtra("Status","Scheduled For Test Drive"));
        finish();
    }

}
