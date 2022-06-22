package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DeliveryActivity extends AppCompatActivity {
    TextView edt_time,edt__add_time, txt_telephone , txtdDirectvisit,txt_update_status,txt_edit_status;
    EditText edt_date,edt__add_date,edt_remark,edt_add_remark;
    TextInputEditText edt_cus_name , edt_cus_one_number;
    SwitchCompat swt_pre_delivery_checklist;
    int mYear, mDay ,mMonth,mMinute,mHour;
    Boolean isCallPressed = false, isDirectPlace = false;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String DATE , TIME , REFERRAL_NAME , REFERRAL_CONTACT_NO , FOLLOW_UP_DATE , FOLLOW_UP_TIME;
    String  status_code , msg ,UPDATE_ENQUIRY_ID,scheduled_status, delivery_completed_id , follow_up_type , API_TOKEN , contact_id , contact_status,COMMENTS;
    String deliverycompleted_url = BASE_URL + "deliverycompleted/deliverycompleted";
    String getdeliverycompleted_url = BASE_URL + "deliverycompleted/getdeliverycompleted";
    String updatedeliverycompleted_url = BASE_URL + "deliverycompleted/updatedeliverycompleted";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = DeliveryActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(DeliveryActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(DeliveryActivity.this, Constants.TOKEN,"");
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        edt__add_date  = findViewById(R.id.edt__add_date);
        edt__add_time  = findViewById(R.id.edt__add_time);
        swt_pre_delivery_checklist = findViewById(R.id.swt_pre_delivery_checklist);
        edt_remark = findViewById(R.id.edt_remark);
        edt_add_remark = findViewById(R.id.edt_add_remark);
        edt_cus_name = findViewById(R.id.edt_cus_name);
        edt_cus_one_number = findViewById(R.id.edt_cus_one_number);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        scheduled_status =  getIntent().getStringExtra("SCHEDULED_STATUS");

        txt_update_status = findViewById(R.id.txt_update_status);
        txt_edit_status = findViewById(R.id.txt_edit_status);

        if (scheduled_status.equals("NEW SCHEDULED")){
            txt_update_status.setVisibility(View.VISIBLE);
            txt_edit_status.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
            String strtime = spf.format(calendar.getTime());
            edt__add_time.setText(strtime);
            edt_time.setText(strtime);
            SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
            String strDates = dateformat.format(calendar.getTime());
            edt__add_date.setText(strDates);
            edt_date.setText(strDates);

        }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){
            txt_update_status.setVisibility(View.GONE);
            txt_edit_status.setVisibility(View.VISIBLE);
            Get_Delivery_Completed();

        }


        swt_pre_delivery_checklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                  //  lin_follow_up.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                   // lin_follow_up.setVisibility(View.VISIBLE);
                    swt_pre_delivery_checklist.setChecked(true);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(DeliveryActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveryActivity.this,
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

        edt__add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(DeliveryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edt__add_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        edt__add_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edt__add_date.getRight() - edt__add_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveryActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                        edt__add_date.setText(strDate);
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


        txt_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    follow_up_type = "1";
                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                    isCallPressed = true;

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
                    follow_up_type = "2";
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                    isDirectPlace = true;
                    if (isCallPressed) {
                        txt_telephone.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                        isCallPressed = false;
                    }
            }
        });

    }

    public void back(View view) {
        startActivity(new Intent(DeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status","Delivery Completed"));
         finish();
    }

    public void updatestatus(View view) {

        if (isCallPressed || isDirectPlace){
            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    DATE = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String followuptime = edt_time.getText().toString().trim()+":"+"00";
            if (followuptime.equals("") || followuptime.isEmpty()) {
                edt_time.setError("Please Selecte Time");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                try {

                    Date newDate = spf.parse(followuptime);
                    spf = new SimpleDateFormat("HH:mm");
                    TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            String followupdates = edt__add_date.getText().toString().trim();
            if (followupdates.equals("") || followupdates.isEmpty()) {
                edt__add_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdates);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    FOLLOW_UP_DATE = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String followuptimes = edt__add_time.getText().toString().trim()+":"+"00";
            if (followuptimes.equals("") || followuptimes.isEmpty()) {
                edt__add_time.setError("Please Selecte Time");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                try {

                    Date newDate = spf.parse(followuptimes);
                    spf = new SimpleDateFormat("HH:mm");
                    FOLLOW_UP_TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            COMMENTS = edt_remark.getText().toString().trim();
            if (COMMENTS.equals("")||COMMENTS.isEmpty()){
                edt_remark.setError("Please Fill the Comments");
            }

            REFERRAL_NAME = edt_cus_name.getText().toString().trim();
            if (REFERRAL_NAME.equals("")||COMMENTS.isEmpty()){
              //  edt_cus_name.setError("Please Fill the Comments");
            }

            REFERRAL_CONTACT_NO = edt_cus_one_number.getText().toString().trim();
            if (REFERRAL_NAME.equals("")||COMMENTS.isEmpty()){
               // edt_cus_one_number.setError("Please Fill the Comments");
            }

            if (DATE.equals("")||TIME.equals("")||FOLLOW_UP_DATE.equals("")||FOLLOW_UP_TIME.equals("")
            ||COMMENTS.equals("")||follow_up_type.equals("")){
                Toast.makeText(DeliveryActivity.this, "Fill the Details", Toast.LENGTH_SHORT).show();
            }else {
                Delivery_Completed();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(DeliveryActivity.this);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                anim.setDuration(15000);
                anim.setInterpolator(new DecelerateInterpolator());
                progressDialog.show();
            }
        }else {
            Toast.makeText(DeliveryActivity.this, "Fill all the Fileds", Toast.LENGTH_SHORT).show();

        }
    }


    public void editstatus(View view) {

        if (!isCallPressed && !isDirectPlace){
            Toast.makeText(DeliveryActivity.this, "Fill all the Fileds", Toast.LENGTH_SHORT).show();
        }else {
            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    DATE = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String followuptime = edt_time.getText().toString().trim()+":"+"00";
            if (followuptime.equals("") || followuptime.isEmpty()) {
                edt_time.setError("Please Selecte Time");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                try {

                    Date newDate = spf.parse(followuptime);
                    spf = new SimpleDateFormat("HH:mm");
                    TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String followupdates = edt__add_date.getText().toString().trim();
            if (followupdates.equals("") || followupdates.isEmpty()) {
                edt__add_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdates);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    FOLLOW_UP_DATE = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String followuptimes = edt__add_time.getText().toString().trim()+":"+"00";
            if (followuptimes.equals("") || followuptimes.isEmpty()) {
                edt__add_time.setError("Please Selecte Time");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                try {

                    Date newDate = spf.parse(followuptimes);
                    spf = new SimpleDateFormat("HH:mm");
                    FOLLOW_UP_TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            COMMENTS = edt_remark.getText().toString().trim();
            if (COMMENTS.equals("")||COMMENTS.isEmpty()){
                edt_remark.setError("Please Fill the Comments");
            }

            REFERRAL_NAME = edt_cus_name.getText().toString().trim();
            if (REFERRAL_NAME.equals("")||COMMENTS.isEmpty()){
                //  edt_cus_name.setError("Please Fill the Comments");
            }

            REFERRAL_CONTACT_NO = edt_cus_one_number.getText().toString().trim();
            if (REFERRAL_NAME.equals("")||COMMENTS.isEmpty()){
                // edt_cus_one_number.setError("Please Fill the Comments");
            }

            if (DATE.equals("")||TIME.equals("")||FOLLOW_UP_DATE.equals("")||FOLLOW_UP_TIME.equals("")
                    ||COMMENTS.equals("")||follow_up_type.equals("")){
                Toast.makeText(DeliveryActivity.this, "Fill the Details", Toast.LENGTH_SHORT).show();
            }else {
                Update_Delivery_Completed();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(DeliveryActivity.this);
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
    }

    //Api Delivery_Completed;
    public void Delivery_Completed(){
        StringRequest requests = new StringRequest(Request.Method.POST, deliverycompleted_url, new Response.Listener<String>() {
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
                            String followupdate = jsonObject.getString("followup_date");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("followup_time");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("followup_type");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            if(jsonObject.isNull("followup_remarks")){
                            }else{
                                String followup_remarks = jsonObject.getString("followup_remarks");
                                MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUP_REMARK,followup_remarks);
                            }

                            String delivery_completed_id = jsonObject.getString("delivery_completed_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.DELIVERY_COMPLETED_ID,delivery_completed_id);
                            contact_id = jsonObject.getString("customer_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String time = (String)jsonObject.get("time");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.TIME,time);
                            String date = (String)jsonObject.get("date");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.DATE,date);
                            String comments = (String)jsonObject.get("comments");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.COMMENT,comments);
                            String refName = (String)jsonObject.get("refName");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.REFNAME,refName);
                            String refPhone = (String)jsonObject.get("refPhone");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.REFPHONE,refPhone);
                            String followupid = jsonObject.getString("followup_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPID,followupid);

                            startActivity(new Intent(DeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);//
                params.put("DATE", DATE);
                params.put("TIME", TIME);
                params.put("COMMENTS", COMMENTS);
                params.put("REFERRAL_NAME",REFERRAL_NAME);
                params.put("REFERRAL_CONTACT_NO", REFERRAL_CONTACT_NO);
                params.put("ADD_FOLLOWUP", "1");
                params.put("FOLLOW_UP_DATE", FOLLOW_UP_DATE);
                params.put("FOLLOW_UP_TIME", FOLLOW_UP_TIME);
                params.put("FOLLOW_UP_TYPE", follow_up_type);
                System.out.println("VECHICAL"+params);
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

    //Api Get_Delivery_Completed;
    public void Get_Delivery_Completed(){
        StringRequest requests = new StringRequest(Request.Method.POST, getdeliverycompleted_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("data");
                            String customer_id = jsonObject.getString("customer_id");
                            delivery_completed_id = jsonObject.getString("delivery_completed_id");

                            if (jsonObject.isNull("followup_date")){
                               // BOOKINGDATE = "";
                            }else {
                                String date = jsonObject.getString("followup_date");
                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(date);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt__add_date.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (jsonObject.isNull("time")){
                               // BOOKINGTIME = "";
                            }else {
                                String time = jsonObject.getString("time");
                                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date newDate = spf.parse(time);
                                    spf = new SimpleDateFormat("HH:mm");
                                    String followuptime = spf.format(newDate);
                                    edt_time.setText(followuptime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            String comments = jsonObject.getString("comments");
                            edt_remark.setText(comments);

                            if (jsonObject.isNull("refName")){

                            }else {
                                String refName = jsonObject.getString("refName");
                                edt_cus_name.setText(refName);
                            }

                            if (jsonObject.isNull("refPhone")){

                            }else {
                                String refPhone = jsonObject.getString("refPhone");
                                edt_cus_one_number.setText(refPhone);
                            }

                            if (jsonObject.isNull("date")){
                                // BOOKINGDATE = "";
                            }else {
                                String add_followup = jsonObject.getString("date");
                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(add_followup);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt_date.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (jsonObject.isNull("followup_time")){
                                // BOOKINGTIME = "";
                            }else {
                                String time = jsonObject.getString("followup_time");
                                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date newDate = spf.parse(time);
                                    spf = new SimpleDateFormat("HH:mm");
                                    String followuptime = spf.format(newDate);
                                    edt__add_time.setText(followuptime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            String followup_type = jsonObject.getString("followup_type");
                            if (followup_type.equals("1")){
                                txt_telephone.setBackgroundResource(R.drawable.btn_line);
                                txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                                isCallPressed = true;
                                follow_up_type = "1";

                            }else if (followup_type.equals("2")){
                                txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                                txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                                isDirectPlace = true;
                                follow_up_type = "2";

                            }


                            if (jsonObject.isNull("followup_remarks")){

                            }else {
                                String followup_remarks = jsonObject.getString("followup_remarks");
                                edt_add_remark.setText(followup_remarks);
                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);//
                params.put("DELIVERY_COMPLETED_ID", MyFunctions.getSharedPrefs(DeliveryActivity.this,Constants.DELIVERY_COMPLETED_ID,""));
                System.out.println("VECHICAL"+params);
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

    //Api Delivery_Completed;
    public void Update_Delivery_Completed(){
        StringRequest requests = new StringRequest(Request.Method.POST, updatedeliverycompleted_url, new Response.Listener<String>() {
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
                            String followupdate = jsonObject.getString("followup_date");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("followup_time");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("followup_type");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            if(jsonObject.isNull("followup_remarks")){
                            }else{
                                String followup_remarks = jsonObject.getString("followup_remarks");
                                MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUP_REMARK,followup_remarks);
                            }

                            String delivery_completed_id = jsonObject.getString("delivery_completed_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.DELIVERY_COMPLETED_ID,delivery_completed_id);
                            contact_id = jsonObject.getString("customer_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String time = (String)jsonObject.get("time");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.TIME,time);
                            String date = (String)jsonObject.get("date");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.DATE,date);
                            String comments = (String)jsonObject.get("comments");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.COMMENT,comments);
                            String refName = (String)jsonObject.get("refName");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.REFNAME,refName);
                            String refPhone = (String)jsonObject.get("refPhone");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.REFPHONE,refPhone);
                            String followupid = jsonObject.getString("followup_id");
                            MyFunctions.setSharedPrefs(DeliveryActivity.this,Constants.FOLLOWUPID,followupid);

                            startActivity(new Intent(DeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);//
                params.put("DATE", DATE);
                params.put("TIME", TIME);
                params.put("COMMENTS", COMMENTS);
                params.put("REFERRAL_NAME",REFERRAL_NAME);
                params.put("DELIVERY_COMPLETED_ID",delivery_completed_id);
                params.put("REFERRAL_CONTACT_NO", REFERRAL_CONTACT_NO);
                params.put("ADD_FOLLOWUP", "1");
                params.put("FOLLOW_UP_DATE", FOLLOW_UP_DATE);
                params.put("FOLLOW_UP_TIME", FOLLOW_UP_TIME);
                params.put("FOLLOW_UP_TYPE", follow_up_type);
                System.out.println("VECHICAL"+params);
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status","Delivery Completed"));
        finish();
    }
}
