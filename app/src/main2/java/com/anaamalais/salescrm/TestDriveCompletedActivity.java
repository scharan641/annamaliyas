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
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anaamalais.salescrm.Adapter.ContactStatusAdapter;
import com.anaamalais.salescrm.List.ContactStatusList;
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
import com.google.android.material.textfield.TextInputEditText;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestDriveCompletedActivity extends AppCompatActivity implements ImageChooserListener {
    Boolean isCallPressed = false, isDirectPlace = false;
    TextView edt_time,txt_status_follow_up_text,txt_telephone,txtdDirectvisit,txt_update_status;
    EditText edt_date , edt_remark;
    int mYear, mDay ,mMonth,mMinute,mHour;
    AppCompatRatingBar ratbar_overall_id,ratbar_overall_condition_of_vehicle,ratebar_sales_consultant,ratbar_overall_sales;
    SwitchCompat swt_status_follow_up;
    LinearLayout lin_follow_up,lin_top_info_title,lin_experience_lincense,lin_top,lin_top_second;
    ImageView img_personal_down , img_personal_up,img_camera,Img_dl_photo,img_license_details_down,img_license_details_up;
    RelativeLayout lin_feedback_details;
    View viewlineFeedbackup,viewlineFeedbackbelow;
    Animation animation;
    String driving_licence= "";
    String TEST_DRIVE_DETAILS_ID , driving_licence_number , LICENCE_NUMBER , SCHEDULED_ON  ,  TD_COMPLETED ,  OVERALL_TD_EXPERIANCE , OVERALL_CONDITION_OF_VEHICLE ,
            SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT , OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE ,DRIVING_LICNECE
            ,TEST_DRIVE_ID , BOOKING_FOLLOWUP , CUSTOMER_STATUS_ID , UPDATE_ENQUIRY_ID, FOLLOW_UP_DATE , FOLLOW_UP_TIME , FOLLOW_UP_TYPE , FOLLOW_UP_REMARK;
    String uploadImagePath, uploadImageFile = "", selectedCity = "", selectedCityName = "";
    private final int CAMERA_REQUEST = 20;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageChooserManager imageChooserManager;
    private int chooserType;
    String filePath ;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    TextInputEditText edt_license_no;
    private static final int REQUEST_CAMERA = 101;
    String  status_code , msg ,token, API_TOKEN , contact_id , contact_status;
    String customer_getcontactallstatus_url = BASE_URL + "enquiry/getenquirytype";
    String testdrive_completetestdrive_url = BASE_URL + "testdrive/completetestdrive";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_drive_completed);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = TestDriveCompletedActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(TestDriveCompletedActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(TestDriveCompletedActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(TestDriveCompletedActivity.this, Constants.TOKEN,"");
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        ratbar_overall_id = findViewById(R.id.ratbar_overall_id);
        ratbar_overall_condition_of_vehicle = findViewById(R.id.ratbar_overall_condition_of_vehicle);
        ratebar_sales_consultant = findViewById(R.id.ratebar_sales_consultant);
        ratbar_overall_sales = findViewById(R.id.ratbar_overall_sales);
        swt_status_follow_up = findViewById(R.id.swt_next_follow_up);
        lin_follow_up = findViewById(R.id.lin_follow_up);
        txt_status_follow_up_text = findViewById(R.id.txt_status_follow_up_text);
        img_personal_down = findViewById(R.id.img_personal_down);
        img_personal_up = findViewById(R.id.img_personal_up);
        img_license_details_down = findViewById(R.id.img_license_details_down);
        img_license_details_up = findViewById(R.id.img_license_details_up);
        lin_top = findViewById(R.id.lin_top);
        lin_top_info_title = findViewById(R.id.lin_experience_info);
        viewlineFeedbackup = findViewById(R.id.viewlineFeedbackup);
        viewlineFeedbackbelow = findViewById(R.id.viewlineFeedbackbelow);
        edt_remark = findViewById(R.id.edt_remark);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        txt_update_status  = findViewById(R.id.txt_update_status);
        img_camera = findViewById(R.id.img_camera);
        Img_dl_photo  = findViewById(R.id.Img_dl_photo);
        lin_experience_lincense = findViewById(R.id.lin_experience_lincense);
        lin_top_second = findViewById(R.id.lin_top_second);
        edt_license_no = findViewById(R.id.edt_license_no);
        TEST_DRIVE_ID = getIntent().getStringExtra("CONTACTID");
              //  MyFunctions.getSharedPrefs(TestDriveCompletedActivity.this, Constants.TESTDRIVEID,"");
                //"9";
                //MyFunctions.getSharedPrefs(TestDriveCompletedActivity.this, Constants.TESTDRIVEID,"");
                //MyFunctions.getSharedPrefs(TestDriveCompletedActivity.this, Constants.TESTDRIVEID,"");
               // MyFunctions.getSharedPrefs(TestDriveCompletedActivity.this, Constants.TESTDRIVEID,"");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
        String strtime = spf.format(calendar.getTime());
        edt_time.setText(strtime);
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        edt_date.setText(strDates);
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        ratbar_overall_id.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Float rate = ratingBar.getRating();
                String OVERALLTDEXPERIANCE = String.valueOf(rate);
                String[] out = OVERALLTDEXPERIANCE.split(".0");
                OVERALL_TD_EXPERIANCE = out[0];
            }
        });

        ratbar_overall_condition_of_vehicle.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Float rate = ratingBar.getRating();
                String OVERALLCONDITIONOFVEHICLE = String.valueOf(rate);
                String[] out = OVERALLCONDITIONOFVEHICLE.split(".0");
                OVERALL_CONDITION_OF_VEHICLE = out[0];
            }
        });

        ratebar_sales_consultant.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Float rate = ratingBar.getRating();
                String SALES_CONSULTANTKNOWLEDGEOFTHEPRODUCT = String.valueOf(rate);
                String[] out = SALES_CONSULTANTKNOWLEDGEOFTHEPRODUCT.split(".0");
                SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT = out[0];
            }
        });

        ratbar_overall_sales.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Float rate = ratingBar.getRating();
                String OVERALLSALESCONSULTANTKNOWLEDGE_EXPERIENCE = String.valueOf(rate);
                String[] out = OVERALLSALESCONSULTANTKNOWLEDGE_EXPERIENCE.split(".0");
                OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE = out[0];
            }
        });

        swt_status_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    txt_status_follow_up_text.setText("Add Follow Up");
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    swt_status_follow_up.setChecked(true);
                   // lin_follow_up.setVisibility(View.GONE);
                   // txt_status_follow_up_text.setText("Add Follow Up");
                }
            }
        });


        lin_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_personal_down.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.GONE);
                    img_personal_up.setVisibility(View.VISIBLE);
                    lin_top.setBackgroundResource(R.color.intersted_color);
                    lin_top_info_title.setVisibility(View.VISIBLE);
                    viewlineFeedbackup.setVisibility(View.VISIBLE);
                  //  lin_experience_info.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_top.startAnimation(animation);

                }else if (img_personal_up.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.VISIBLE);
                    img_personal_up.setVisibility(View.GONE);
                    lin_top.setBackgroundResource(0);
                    lin_top_info_title.setVisibility(View.GONE);
                  //  lin_experience_info.setVisibility(View.GONE);
                    viewlineFeedbackup.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_top.startAnimation(animation);
                }
            }
        });

        lin_top_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_license_details_down.getVisibility()==View.VISIBLE){
                    img_license_details_down.setVisibility(View.GONE);
                    img_license_details_up.setVisibility(View.VISIBLE);
                    lin_top_second.setBackgroundResource(R.color.intersted_color);
                    lin_experience_lincense.setVisibility(View.VISIBLE);
                    viewlineFeedbackbelow.setVisibility(View.VISIBLE);
                    //  lin_experience_info.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_top_second.startAnimation(animation);

                }else if (img_license_details_up.getVisibility()==View.VISIBLE){
                    img_license_details_down.setVisibility(View.VISIBLE);
                    img_license_details_up.setVisibility(View.GONE);
                    lin_top_second.setBackgroundResource(0);
                    lin_experience_lincense.setVisibility(View.GONE);
                    //  lin_experience_info.setVisibility(View.GONE);
                    viewlineFeedbackbelow.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_top_second.startAnimation(animation);
                }
            }
        });

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(TestDriveCompletedActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(TestDriveCompletedActivity.this,
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
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

        txt_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (img_personal_up.getVisibility()==View.VISIBLE){
                    FOLLOW_UP_TYPE = "1";
                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                    isCallPressed = true;

                        txt_update_status.setClickable(true);
                        txt_update_status.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_status.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               if (ratbar_overall_id.getRating()==0.0||ratbar_overall_sales.getRating()==0.0||
                                       ratebar_sales_consultant.getRating()==0.0||ratbar_overall_condition_of_vehicle.getRating()==0.0){
                                   AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
                                   builder.setTitle("USER MESSAGE");
                                   builder.setMessage("Please rate the filels");
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
                               }else {
                                   updatestatus();
                               }
                            }

                        });

                    }else {
                        txt_update_status.setClickable(false);
                        txt_update_status.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_status.setTextColor(Color.parseColor("#58595B"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
                        builder.setTitle("USER MESSAGE");
                        builder.setMessage("Fill all the required field first!!");
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
                    if (isDirectPlace) {
                        txtdDirectvisit.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txtdDirectvisit.setTextColor(Color.parseColor("#A7A7A7"));
                        isDirectPlace = false;
                    }
            }
        });

        txtdDirectvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (img_personal_up.getVisibility()==View.VISIBLE){
                    FOLLOW_UP_TYPE = "2";
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                    isDirectPlace = true;
                    if (!TEST_DRIVE_ID.equals("") || !OVERALL_TD_EXPERIANCE.equals("") || !OVERALL_CONDITION_OF_VEHICLE.equals("") && !OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE.equals("")
                            || !SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT.equals("") || !FOLLOW_UP_DATE.equals("") || !FOLLOW_UP_TIME.equals("")
                            || !FOLLOW_UP_TYPE.equals("")) {
                        txt_update_status.setClickable(true);
                        txt_update_status.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_status.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_status.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updatestatus();
                            }

                        });

                    }else {
                        txt_update_status.setClickable(false);
                        txt_update_status.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_status.setTextColor(Color.parseColor("#58595B"));
                        AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
                        builder.setTitle("USER MESSAGE");
                        builder.setMessage("Fill all the required field first!!");
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
                }if (isCallPressed) {
                    txt_telephone.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                    isDirectPlace = false;
                }
            }
        });


        img_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(TestDriveCompletedActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(TestDriveCompletedActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(TestDriveCompletedActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)&&
                            ContextCompat.checkSelfPermission(TestDriveCompletedActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(TestDriveCompletedActivity.this,
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

    }

    public void back(View view) {
        startActivity(new Intent(TestDriveCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status","Test Drive Completed"));
         finish();
    }

    private void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(TestDriveCompletedActivity.this);
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

        if (resultCode == REQUEST_CAMERA &&
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
                MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.DLPHOTO,driving_licence);
                //  file = new File(driving_licence);

                System.out.println("fhfhf"+driving_licence);
                Img_dl_photo.setVisibility(View.VISIBLE);
                Glide.with(TestDriveCompletedActivity.this).load(driving_licence)
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


    public void updatestatus() {

       driving_licence_number = edt_license_no.getText().toString().trim();
       if (driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)){
           edt_license_no.setError("Please Fill the Correct Driving Licence Number");
       }

        String followupdate = edt_date.getText().toString().trim();
        if (followupdate.equals("") || followupdate.isEmpty()) {
            edt_date.setError("Please Selecte Date");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date newDate = spf.parse(followupdate);
                spf = new SimpleDateFormat("yyyy-MM-dd");
                FOLLOW_UP_DATE = spf.format(newDate);

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
                FOLLOW_UP_TIME = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FOLLOW_UP_REMARK = edt_remark.getText().toString().trim();
        if (FOLLOW_UP_REMARK.equals("")){
            FOLLOW_UP_REMARK = "";
        }

        if (OVERALL_TD_EXPERIANCE.equals("")||OVERALL_CONDITION_OF_VEHICLE.equals("")||OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE.equals("")
        ||SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT.equals("")||FOLLOW_UP_DATE.equals("")||FOLLOW_UP_TIME.equals("")
        ||FOLLOW_UP_TYPE.equals("")||driving_licence_number.equals("")||driving_licence_number.isEmpty()||(driving_licence_number.toString().trim().length() < 15)){
            Toast.makeText(TestDriveCompletedActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
        }else {
            Test_Drive_Completed();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(TestDriveCompletedActivity.this);
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

    // Api call Test_Drive_Details
    public void Test_Drive_Details() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcontactallstatus_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                     progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        JSONArray jsonArray_titles = jsonObj.getJSONArray("contact_status");
                        if (status_code.equals("1")) {
                            for (int i = 0; i < jsonArray_titles.length(); i++) {

                            }

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
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
                params.put("CONTACT_ID", "205");
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

    // Api call Test_Drive_Completed
    public void Test_Drive_Completed() {
        StringRequest requests = new StringRequest(Request.Method.POST, testdrive_completetestdrive_url, new Response.Listener<String>() {
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
                            contact_id = jsonObject.getString("enquiry_id");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String overall_td_exp = (String)jsonObject.get("overall_td_exp");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.OVERALLTDEXPERIANCE,overall_td_exp);
                            String overall_condition_of_vehicle = (String)jsonObject.get("overall_condition_of_vehicle");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.OVERALLCONDITIONOFVEHICLE,overall_condition_of_vehicle);
                            String sales_consultant_knowledge_of_the_product = (String)jsonObject.get("sales_consultant_knowledge_of_the_product");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.SALES_CONSULTANTKNOWLEDGEOFTHEPRODUCT,sales_consultant_knowledge_of_the_product);
                            String overall_sales_consultant_knowledge_experiance = (String)jsonObject.get("overall_sales_consultant_knowledge_experiance");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.OVERALLSALESCONSULTANTKNOWLEDGE_EXPERIENCE,overall_sales_consultant_knowledge_experiance);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.FOLLOWUPID,followupid);
                            String dl_number = jsonObject.getString("dl_number");
                            MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.DLNUMBER,dl_number);
                           // String test_drive_id = jsonObject.getString("test_drive_id");
                          //  MyFunctions.setSharedPrefs(TestDriveCompletedActivity.this,Constants.TESTDRIVEID,test_drive_id);
                           startActivity(new Intent(TestDriveCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                           finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(TestDriveCompletedActivity.this);
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
                params.put("TEST_DRIVE_ID", UPDATE_ENQUIRY_ID);
                params.put("DRIVING_LICENCE_NUMBER", driving_licence_number);
                params.put("DRIVING_LICENCE", driving_licence);
                params.put("OVERALL_TD_EXPERIANCE", OVERALL_TD_EXPERIANCE);
                params.put("OVERALL_CONDITION_OF_VEHICLE", OVERALL_CONDITION_OF_VEHICLE);
                params.put("SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT", SALES_CONSULTANT_KNOWLEDGE_OF_THE_PRODUCT);
                params.put("OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE", OVERALL_SALES_CONSULTANT_KNOWLEDGE_EXPERIENCE);
                params.put("BOOKING_FOLLOWUP", "1");
                params.put("FOLLOW_UP_DATE", FOLLOW_UP_DATE);
                params.put("FOLLOW_UP_TIME", FOLLOW_UP_TIME);
                params.put("FOLLOW_UP_TYPE", FOLLOW_UP_TYPE);
                params.put("FOLLOW_UP_REMARK", FOLLOW_UP_REMARK);
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

}
