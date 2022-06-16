package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PreDeliveryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String[] extendedwarranty={"Extended Warranty","Yes","No"};
    Spinner spinner_extendedwarranty;
    TextView txt_follow_up , txt_lost_lesd , txt_closed , txt_waiting , txt_preferred_rto_dates, edt_time
            ,txt_complete_pdi,txt_under_process,txt_update_status,txt_edit_schedule ;
    Boolean isOnePressed = true, isSecondPlace = false , isThirdPlace = false;
    Boolean isRtoWaitingOnePressed = false, isRtoDateSecondPlace = false;
    Boolean isPdiCompOnePressed = false, isPdiUnPocSecondPlace = false;
    LinearLayout lin_reason_waiting , lin_reason_waiting_rto,lin_preferred_rto_date;
    EditText edt_date,edt_drop_option,edt_reason_waiting_rto,edt_reason_waiting;
    TextView edt_preferred_rto_date;
    SwitchCompat swt_next_follow_up;
    int mYear, mDay ,mMonth,mMinute,mHour;
    String  ACCESSORIES_FITMENT_STATUS , AFWAITINGREASON , FTAG , RTOPROCESS , RTOWAITINGREASON
            ,  PREFERRED_RTO_DATE , ALLOCATIONPDI , PRE_DELIVERY_ID,contact_id,contact_status , pre_delivery_id;
    String prideliverystatus,UPDATE_ENQUIRY_ID,EXTENDEDWARRANTY,scheduled_status,EXTENDED_WARRANTY;
    String ACCESSORIES_FITMENT , AF_WAITING_REASON , FASTAG , RTO_PROCESS , RTO_WAITING_REASON , PREFERED_RTO_DATE , ALLOCATION_PDI;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN, UPDATE_CONTACT_ID,DROPPED_REASON_ID,CONTACT_ID;
    String predelivery_predelivery_url = BASE_URL + "predelivery/predelivery";
    String predeliverydetails_url = BASE_URL + "predelivery/getpredeliverydetails";
    String Updatepredeliverydetails_url = BASE_URL + "predelivery/updatepredeliverydetails";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_delivery);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = PreDeliveryActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PreDeliveryActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(PreDeliveryActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(PreDeliveryActivity.this, Constants.TOKEN, "");
        txt_follow_up = findViewById(R.id.txt_follow_up);
        txt_lost_lesd = findViewById(R.id.txt_lost_lesd);
        txt_closed = findViewById(R.id.txt_closed);
        lin_reason_waiting = findViewById(R.id.lin_reason_waiting);
        txt_waiting = findViewById(R.id.txt_waiting);
        txt_preferred_rto_dates = findViewById(R.id.txt_preferred_rto_dates);
        lin_reason_waiting_rto  = findViewById(R.id.lin_reason_waiting_rto);
        edt_preferred_rto_date = findViewById(R.id.edt_preferred_rto_date);
       // edt_time = findViewById(R.id.edt_time);
        edt_reason_waiting_rto = findViewById(R.id.edt_reason_waiting_rto);
        edt_reason_waiting = findViewById(R.id.edt_reason_waiting);
        txt_complete_pdi = findViewById(R.id.txt_complete_pdi);
        txt_under_process = findViewById(R.id.txt_under_process);
        swt_next_follow_up = findViewById(R.id.swt_next_follow_up);
        lin_preferred_rto_date = findViewById(R.id.lin_preferred_rto_date);
        txt_update_status = findViewById(R.id.txt_update_status);
        txt_edit_schedule = findViewById(R.id.txt_edit_status);
        spinner_extendedwarranty = findViewById(R.id.spinner_extendedwarranty);
        scheduled_status = getIntent().getStringExtra("SCHEDULED_STATUS");
                //"EDIT NEW SCHEDULED";
                //
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        if (scheduled_status.equals("NEW SCHEDULED")){
            txt_update_status.setVisibility(View.VISIBLE);
            txt_edit_schedule.setVisibility(View.GONE);

        }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){
            txt_update_status.setVisibility(View.VISIBLE);
            txt_edit_schedule.setVisibility(View.GONE);
            Pre_Delivery_Ddetails();
        }

        // Spinner click listener
        spinner_extendedwarranty.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PreDeliveryActivity.this, android.R.layout.simple_spinner_item, extendedwarranty){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint

                    return false;
                } else {
                    return true;
                }
            }
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == 0) {
                    // Set the hint text color gray
                    ((TextView) v).setTextColor(Color.GRAY);
                } else {
                    ((TextView) v).setTextColor(Color.parseColor("#131313"));
                    Typeface typeface = ResourcesCompat.getFont(PreDeliveryActivity.this, R.font.poppins);
                    ((TextView) v).setTypeface(typeface);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                }

                //((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                //((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.parseColor("#131313"));
                   // tv.setTextColor(Color.parseColor("#A7A7A7"));
                    Typeface typeface = ResourcesCompat.getFont(PreDeliveryActivity.this, R.font.poppins);
                    ((TextView) view).setTypeface(typeface);
                    ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,0,0);
                    ((TextView) view).setLayoutParams(params);
                    ((TextView) view).setPadding(20,-8,0,0);

                }
                return view;
            }
        };
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_extendedwarranty.setAdapter(dataAdapter);
        spinner_extendedwarranty.setPrompt("Fuel Type");



        txt_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                txt_follow_up.setBackgroundResource(R.drawable.btn_line);
                txt_follow_up.setTextColor(Color.parseColor("#EB0A1E"));
                lin_reason_waiting.setVisibility(View.VISIBLE);
                prideliverystatus = txt_follow_up.getText().toString().trim();
                ACCESSORIES_FITMENT = "1";
                if (isSecondPlace) {
                    txt_lost_lesd.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_lost_lesd.setTextColor(Color.parseColor("#A7A7A7"));
                    isSecondPlace = false;
                }else if (isThirdPlace){
                    txt_closed.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_closed.setTextColor(Color.parseColor("#A7A7A7"));
                    isThirdPlace = false;
                }
            }
        });


        txt_lost_lesd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_lost_lesd.setBackgroundResource(R.drawable.btn_line);
                txt_lost_lesd.setTextColor(Color.parseColor("#EB0A1E"));
                lin_reason_waiting.setVisibility(View.GONE);
                prideliverystatus = txt_lost_lesd.getText().toString().trim();
                ACCESSORIES_FITMENT = "2";
                isSecondPlace = true;
                if (isOnePressed) {
                    txt_follow_up.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_follow_up.setTextColor(Color.parseColor("#A7A7A7"));
                    isOnePressed = false;
                }else if (isThirdPlace) {
                    txt_closed.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_closed.setTextColor(Color.parseColor("#A7A7A7"));
                    isThirdPlace = false;
                }
            }
        });

        txt_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_closed.setBackgroundResource(R.drawable.btn_line);
                txt_closed.setTextColor(Color.parseColor("#EB0A1E"));
                lin_reason_waiting.setVisibility(View.GONE);
                prideliverystatus = txt_closed.getText().toString().trim();
                ACCESSORIES_FITMENT = "3";
                isThirdPlace = true;
                if (isOnePressed) {
                    txt_follow_up.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_follow_up.setTextColor(Color.parseColor("#A7A7A7"));
                    isOnePressed = false;
                }else if (isSecondPlace){
                    txt_lost_lesd.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_lost_lesd.setTextColor(Color.parseColor("#A7A7A7"));
                    isSecondPlace = false;
                }
            }
        });

        txt_waiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_waiting.setBackgroundResource(R.drawable.btn_line);
                txt_waiting.setTextColor(Color.parseColor("#EB0A1E"));
                lin_reason_waiting_rto.setVisibility(View.VISIBLE);
                lin_preferred_rto_date.setVisibility(View.GONE);
                RTO_PROCESS = "1";
                isRtoWaitingOnePressed = true;
                if (isRtoDateSecondPlace) {
                    txt_preferred_rto_dates.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_preferred_rto_dates.setTextColor(Color.parseColor("#A7A7A7"));
                    isRtoDateSecondPlace = false;
                }
            }
        });

        txt_preferred_rto_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_preferred_rto_dates.setBackgroundResource(R.drawable.btn_line);
                txt_preferred_rto_dates.setTextColor(Color.parseColor("#EB0A1E"));
                lin_preferred_rto_date.setVisibility(View.VISIBLE);
                lin_reason_waiting_rto.setVisibility(View.GONE);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
                String strDates = dateformat.format(calendar.getTime());
                edt_preferred_rto_date.setText(strDates);
                RTO_PROCESS = "2";
                isRtoDateSecondPlace = true;
                if (isRtoWaitingOnePressed) {
                    txt_waiting.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_waiting.setTextColor(Color.parseColor("#A7A7A7"));
                    isRtoWaitingOnePressed = false;
                }
            }
        });


        txt_complete_pdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_complete_pdi.setBackgroundResource(R.drawable.btn_line);
                txt_complete_pdi.setTextColor(Color.parseColor("#EB0A1E"));
                ALLOCATION_PDI = "1";
                isPdiCompOnePressed = true;
                if (isPdiUnPocSecondPlace) {
                    txt_under_process.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_under_process.setTextColor(Color.parseColor("#A7A7A7"));
                    isPdiUnPocSecondPlace = false;
                }
            }
        });

        txt_under_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_under_process.setBackgroundResource(R.drawable.btn_line);
                txt_under_process.setTextColor(Color.parseColor("#EB0A1E"));
                ALLOCATION_PDI = "2";
                isPdiUnPocSecondPlace = true;
                if (isPdiCompOnePressed) {
                    txt_complete_pdi.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_complete_pdi.setTextColor(Color.parseColor("#A7A7A7"));
                    isPdiCompOnePressed = false;
                }
            }
        });

      /*  edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(PreDeliveryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edt_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });*/

        edt_preferred_rto_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // your action here
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PreDeliveryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                CharSequence strDate = null;
                                android.text.format.Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);
                                long dtDob = chosenDate.toMillis(true);

                                strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                edt_preferred_rto_date.setText(strDate);
                                //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                //  datePickerDialog.show();

            }

        });


        swt_next_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    FASTAG = "1";
                }else {
                    FASTAG = "0";
                }
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.spinner_extendedwarranty:
                EXTENDEDWARRANTY = Integer.toString(position);
                if (EXTENDEDWARRANTY.equals("1")){
                    EXTENDED_WARRANTY = "1";
                }else if(EXTENDEDWARRANTY.equals("2")){
                    EXTENDED_WARRANTY = "0";
                }
                //Toast.makeText(AddEnquiryActivity.this, current_car_fuel_type, Toast.LENGTH_SHORT).show();
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void updatestatus(View view) {
        if(scheduled_status.equals("NEW SCHEDULED")){
            if (!isOnePressed && !isSecondPlace && !isThirdPlace || !isRtoWaitingOnePressed && !isRtoDateSecondPlace || !isPdiCompOnePressed  && !isPdiUnPocSecondPlace){
                // userMessage = (String) jsonObj.get("userMessage");
                AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
                builder.setTitle("User Message");
                builder.setMessage("Fill all the Filelds");
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
                if (ACCESSORIES_FITMENT.equals("1")){
                    AF_WAITING_REASON =  edt_reason_waiting.getText().toString().trim();
                    if (AF_WAITING_REASON.equals("")||AF_WAITING_REASON.isEmpty()){
                        edt_reason_waiting.setError("Fill the AF_WAITING_REASON");
                    }
                }else if (ACCESSORIES_FITMENT.equals("2")){
                    AF_WAITING_REASON = "";
                }else if (ACCESSORIES_FITMENT.equals("3")){
                    AF_WAITING_REASON = "";
                }

                if (RTO_PROCESS.equals("1")){
                    RTO_WAITING_REASON =  edt_reason_waiting_rto.getText().toString().trim();
                    PREFERED_RTO_DATE = "";
                    if (RTO_WAITING_REASON.equals("")||RTO_WAITING_REASON.isEmpty()){
                        edt_reason_waiting_rto.setError("Fill the AF_WAITING_REASON");
                    }
                }else if (RTO_PROCESS.equals("2")){
                    RTO_WAITING_REASON = "";
                }

                if (!swt_next_follow_up.isChecked()) {
                    FASTAG = "0";
                }

                String followupdate = edt_preferred_rto_date.getText().toString().trim();
                if (followupdate.equals("") || followupdate.isEmpty()) {
                    edt_preferred_rto_date.setError("Please Selecte Dob");
                } else {
                    SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                    try {

                        Date newDate = spf.parse(followupdate);
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        PREFERED_RTO_DATE = spf.format(newDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (ACCESSORIES_FITMENT.equals("1")&&AF_WAITING_REASON.equals("")||RTO_PROCESS.equals("1")&& RTO_WAITING_REASON.equals("")
                        ||RTO_PROCESS.equals("2")&&PREFERED_RTO_DATE.equals("")||ALLOCATION_PDI.equals("")||FASTAG.equals("")){
                    Toast.makeText(PreDeliveryActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
                }else {
                    Pre_Delivery();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(PreDeliveryActivity.this);
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

        }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){

            if (!isOnePressed && !isSecondPlace && !isThirdPlace || !isRtoWaitingOnePressed && !isRtoDateSecondPlace || !isPdiCompOnePressed  && !isPdiUnPocSecondPlace){
                // userMessage = (String) jsonObj.get("userMessage");
                AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
                builder.setTitle("User Message");
                builder.setMessage("Fill all the Filelds");
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
                if (ACCESSORIES_FITMENT.equals("1")){
                    AF_WAITING_REASON =  edt_reason_waiting.getText().toString().trim();
                    if (AF_WAITING_REASON.equals("")||AF_WAITING_REASON.isEmpty()){
                        edt_reason_waiting.setError("Fill the AF_WAITING_REASON");
                    }
                }else if (ACCESSORIES_FITMENT.equals("2")){
                    AF_WAITING_REASON = "";
                }else if (ACCESSORIES_FITMENT.equals("3")){
                    AF_WAITING_REASON = "";
                }

                if (RTO_PROCESS.equals("1")){
                    RTO_WAITING_REASON =  edt_reason_waiting_rto.getText().toString().trim();
                    PREFERED_RTO_DATE = "";
                    if (RTO_WAITING_REASON.equals("")||RTO_WAITING_REASON.isEmpty()){
                        edt_reason_waiting_rto.setError("Fill the AF_WAITING_REASON");
                    }
                }else if (RTO_PROCESS.equals("2")){
                    RTO_WAITING_REASON = "";
                }

                if (!swt_next_follow_up.isChecked()) {
                    FASTAG = "0";
                }

                String followupdate = edt_preferred_rto_date.getText().toString().trim();
                if (followupdate.equals("") || followupdate.isEmpty()) {
                    edt_preferred_rto_date.setError("Please Selecte Dob");
                } else {
                    SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                    try {

                        Date newDate = spf.parse(followupdate);
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        PREFERED_RTO_DATE = spf.format(newDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (ACCESSORIES_FITMENT.equals("1")&&AF_WAITING_REASON.equals("")||RTO_PROCESS.equals("1")&& RTO_WAITING_REASON.equals("")
                        ||RTO_PROCESS.equals("2")&&PREFERED_RTO_DATE.equals("")||ALLOCATION_PDI.equals("")||FASTAG.equals("")){
                    Toast.makeText(PreDeliveryActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
                }else {
                    Updat_Pre_Delivery_Details();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(PreDeliveryActivity.this);
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
    }

    public void editstatus(View view) {

    }


    // Api call Pre_Delivery
    public void Pre_Delivery () {
        StringRequest requests = new StringRequest(Request.Method.POST, predelivery_predelivery_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                        progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        pre_delivery_id = jsonObj.getString("pre_delivery_id");
                        MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.PRE_DELIVERY_ID,pre_delivery_id);
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("data");
                            contact_id = jsonObject.getString("customer_id");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                          if (jsonObject.isNull("address")){

                          }else {
                              String cus_address = (String)jsonObject.get("address");
                              MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                          }
                            String accessories_fitment_status =  (String)jsonObject.get("accessories_fitment_status");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.ACCESSORIES_FITMENT_STATUS,accessories_fitment_status);
                            if (jsonObject.isNull("af_waiting_reason")){

                            }else{
                                String af_waiting_reason =  (String)jsonObject.get("af_waiting_reason");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.AF_WAITING_REASON,af_waiting_reason);
                            }

                            if (jsonObject.isNull("extended_warranty")){

                            }else{
                                String extended_warranty =  (String)jsonObject.get("extended_warranty").toString();
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.EXTENDED_WARRANTY,extended_warranty);
                            }

                            if (jsonObject.isNull("fastag")){

                            }else{
                                String fastag =  (String)jsonObject.get("fastag");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.FASTAG,fastag);
                            }

                            String rto_process  =  (String)jsonObject.get("rto_process");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.RTO_PROCESS,rto_process);

                            if (jsonObject.isNull("rto_waiting_reason")){

                            }else{
                                String rto_waiting_reason =  (String)jsonObject.get("rto_waiting_reason");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.RTO_WAITING_REASON,rto_waiting_reason);
                            }
                            String allocation_pdi  =  (String)jsonObject.get("allocation_pdi");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.ALLOCATION_PDI,allocation_pdi);
                            if (jsonObject.isNull("preferred_rto_date")){

                            }else{
                                String preferred_rto_date =  (String)jsonObject.get("preferred_rto_date");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.PREFERRED_RTO_DATE,preferred_rto_date);
                            }
                            startActivity(new Intent(PreDeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                params.put("ACCESSORIES_FITMENT", ACCESSORIES_FITMENT);
                params.put("AF_WAITING_REASON", AF_WAITING_REASON);
                params.put("FASTAG", FASTAG);
                params.put("EXTENDED_WARRANTY", EXTENDED_WARRANTY);
                params.put("RTO_PROCESS", RTO_PROCESS);
                params.put("RTO_WAITING_REASON", RTO_WAITING_REASON);
                params.put("PREFERED_RTO_DATE", PREFERED_RTO_DATE);
                params.put("ALLOCATION_PDI", ALLOCATION_PDI);
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

    // Api call Get_Delivery_Ddetails
    public void Pre_Delivery_Ddetails(){

        StringRequest requests = new StringRequest(Request.Method.POST, predeliverydetails_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("pre_delivery");
                             CONTACT_ID = jsonObject.getString("CONTACT_ID");

                            PRE_DELIVERY_ID = jsonObject.getString("PRE_DELIVERY_ID");

                            ACCESSORIES_FITMENT_STATUS = jsonObject.getString("ACCESSORIES_FITMENT_STATUS");

                            if (ACCESSORIES_FITMENT_STATUS.equals("1")){
                                txt_follow_up.setBackgroundResource(R.drawable.btn_line);
                                txt_follow_up.setTextColor(Color.parseColor("#EB0A1E"));
                                lin_reason_waiting.setVisibility(View.VISIBLE);
                              //  prideliverystatus = txt_follow_up.getText().toString().trim();
                                ACCESSORIES_FITMENT = "1";

                            }else if (ACCESSORIES_FITMENT_STATUS.equals("2")){
                                txt_lost_lesd.setBackgroundResource(R.drawable.btn_line);
                                txt_lost_lesd.setTextColor(Color.parseColor("#EB0A1E"));
                                lin_reason_waiting.setVisibility(View.GONE);
                              //  prideliverystatus = txt_lost_lesd.getText().toString().trim();
                                ACCESSORIES_FITMENT = "2";
                                isSecondPlace = true;

                            }else if (ACCESSORIES_FITMENT_STATUS.equals("3")){
                                txt_closed.setBackgroundResource(R.drawable.btn_line);
                                txt_closed.setTextColor(Color.parseColor("#EB0A1E"));
                                lin_reason_waiting.setVisibility(View.GONE);
                                prideliverystatus = txt_closed.getText().toString().trim();
                                ACCESSORIES_FITMENT = "3";
                                isThirdPlace = true;
                            }

                            if (jsonObject.isNull("AF_WAITING_REASON")){

                            }else {
                                AFWAITINGREASON  = jsonObject.getString("AF_WAITING_REASON");
                                edt_reason_waiting.setText(AFWAITINGREASON);
                            }


                            FTAG = jsonObject.getString("FASTAG");
                            if (FTAG.equals("0")){
                                swt_next_follow_up.setChecked(false);
                            }else if (FTAG.equals("1")){
                                swt_next_follow_up.setChecked(true);
                            }

                            RTOPROCESS = jsonObject.getString("RTO_PROCESS");

                            if (RTOPROCESS.equals("1")){
                                txt_waiting.setBackgroundResource(R.drawable.btn_line);
                                txt_waiting.setTextColor(Color.parseColor("#EB0A1E"));
                                lin_reason_waiting_rto.setVisibility(View.VISIBLE);
                                lin_preferred_rto_date.setVisibility(View.GONE);
                                RTO_PROCESS = "1";
                                isRtoWaitingOnePressed = true;

                            }else if (RTOPROCESS.equals("2")){
                                txt_preferred_rto_dates.setBackgroundResource(R.drawable.btn_line);
                                txt_preferred_rto_dates.setTextColor(Color.parseColor("#EB0A1E"));
                                lin_preferred_rto_date.setVisibility(View.VISIBLE);
                                lin_reason_waiting_rto.setVisibility(View.GONE);
                                RTO_PROCESS = "2";
                                isRtoDateSecondPlace = true;
                            }

                            if (jsonObject.isNull("RTO_WAITING_REASON")){

                            }else {
                                RTOWAITINGREASON = jsonObject.getString("RTO_WAITING_REASON");
                                edt_reason_waiting_rto.setText(RTOWAITINGREASON);
                            }

                            if (jsonObject.isNull("PREFERRED_RTO_DATE")){

                            }else {
                                PREFERRED_RTO_DATE = jsonObject.getString("PREFERRED_RTO_DATE");
                                String date = jsonObject.getString("PREFERRED_RTO_DATE");
                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(date);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt_preferred_rto_date.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (jsonObject.isNull("EXTENDED_WARRANTY")){

                            }else{
                                String extended_warranty =  (String)jsonObject.get("EXTENDED_WARRANTY").toString();
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.EXTENDED_WARRANTY,extended_warranty);
                                if (extended_warranty.equals("0")){
                                    spinner_extendedwarranty.setSelection(Integer.parseInt("2"));
                                }else {
                                    spinner_extendedwarranty.setSelection(Integer.parseInt(extended_warranty));
                                }
                            }

                            ALLOCATIONPDI =  jsonObject.getString("ALLOCATION_PDI");

                            if (ALLOCATIONPDI.equals("1")){
                                txt_complete_pdi.setBackgroundResource(R.drawable.btn_line);
                                txt_complete_pdi.setTextColor(Color.parseColor("#EB0A1E"));
                                ALLOCATION_PDI = "1";
                                isPdiCompOnePressed = true;

                            }else if (ALLOCATIONPDI.equals("2")){
                                txt_under_process.setBackgroundResource(R.drawable.btn_line);
                                txt_under_process.setTextColor(Color.parseColor("#EB0A1E"));
                                ALLOCATION_PDI = "2";
                                isPdiUnPocSecondPlace = true;
                            }

                            if (jsonObject.isNull("date")){
                                // BOOKINGDATE = "";
                            }else {
                                String date = jsonObject.getString("date");
                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(date);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt_date.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                params.put("PRE_DELIVERY_ID", MyFunctions.getSharedPrefs(PreDeliveryActivity.this,Constants.PRE_DELIVERY_ID,""));
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

    // Api call Pre_Delivery
    public void Updat_Pre_Delivery_Details () {
        StringRequest requests = new StringRequest(Request.Method.POST, Updatepredeliverydetails_url, new Response.Listener<String>() {
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
                            contact_id = jsonObject.getString("customer_id");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String accessories_fitment_status =  (String)jsonObject.get("accessories_fitment_status");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.ACCESSORIES_FITMENT_STATUS,accessories_fitment_status);
                            if (jsonObject.isNull("af_waiting_reason")){

                            }else{
                                String af_waiting_reason =  (String)jsonObject.get("af_waiting_reason");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.AF_WAITING_REASON,af_waiting_reason);
                            }

                            if (jsonObject.isNull("EXTENDED_WARRANTY")){

                            }else{
                                String extended_warranty =  (String)jsonObject.get("EXTENDED_WARRANTY").toString();
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.EXTENDED_WARRANTY,extended_warranty);
                                spinner_extendedwarranty.setSelection(Integer.parseInt(extended_warranty));
                            }

                            if (jsonObject.isNull("fastag")){

                            }else{
                                String fastag =  (String)jsonObject.get("fastag");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.FASTAG,fastag);
                            }

                            String rto_process  =  (String)jsonObject.get("rto_process");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.RTO_PROCESS,rto_process);

                            if (jsonObject.isNull("rto_waiting_reason")){

                            }else{
                                String rto_waiting_reason =  (String)jsonObject.get("rto_waiting_reason");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.RTO_WAITING_REASON,rto_waiting_reason);
                            }
                            String allocation_pdi  =  (String)jsonObject.get("allocation_pdi");
                            MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.ALLOCATION_PDI,allocation_pdi);
                            if (jsonObject.isNull("preferred_rto_date")){

                            }else{
                                String preferred_rto_date =  (String)jsonObject.get("preferred_rto_date");
                                MyFunctions.setSharedPrefs(PreDeliveryActivity.this,Constants.PREFERRED_RTO_DATE,preferred_rto_date);
                            }
                               startActivity(new Intent(PreDeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                              finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(PreDeliveryActivity.this);
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
                params.put("PRE_DELIVERY_ID", PRE_DELIVERY_ID);
                params.put("ACCESSORIES_FITMENT", ACCESSORIES_FITMENT);
                params.put("AF_WAITING_REASON", AF_WAITING_REASON);
                params.put("FASTAG", FASTAG);
                params.put("EXTENDED_WARRANTY", EXTENDED_WARRANTY);
                params.put("RTO_PROCESS", RTO_PROCESS);
                params.put("RTO_WAITING_REASON", RTO_WAITING_REASON);
                params.put("PREFERED_RTO_DATE", PREFERED_RTO_DATE);
                params.put("ALLOCATION_PDI", ALLOCATION_PDI);
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
        startActivity(new Intent(PreDeliveryActivity.this,CustomerDetailsActivity.class).putExtra("Status","Pre Delivery"));
        finish();
    }
}
