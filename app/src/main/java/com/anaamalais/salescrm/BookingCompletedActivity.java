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

import com.anaamalais.salescrm.Adapter.ExteriorColorArrayAdapter;
import com.anaamalais.salescrm.Adapter.InteriorColorTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelVariantTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelsTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.PaymentModesAdapter;
import com.anaamalais.salescrm.List.ExteriorColorsList;
import com.anaamalais.salescrm.List.InteriorColorsList;
import com.anaamalais.salescrm.List.ModelsList;
import com.anaamalais.salescrm.List.PaymentModesList;
import com.anaamalais.salescrm.List.VariantsLIst;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BookingCompletedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView edt_time , edt_time_booking , txt_telephone , txtdDirectvisit , txt_update_status , txt_edit_booking ;
    EditText edt_date , edt_date_booking , edt_remark;
    int mYear, mDay ,mMonth,mMinute,mHour;
    Boolean isCallPressed = false, isDirectPlace = false;
    TextInputEditText edt_cop_booking_amount;
    SwitchCompat swt_notify_customer , swt_status_follow_up;
    LinearLayout lin_follow_up ;
    Spinner spinner_model_suffix_cop , spinner_model_variant , spinner_exterEior_color, spinner_interior_color,spinner_payment_mode;
    String vehicle_id , model_variant, interior_color_id , exterior_color_id , spinner_payment_id,scheduled_status;
    String NOTIFYCUSTOMER , PAYMENTMODE , AMOUNTPAID , BOOKINGTIME , BOOKINGDATE , EXTERIOR_COLOR_ID , INTERIOR_COLOR_ID , MODEL_SUFFIX_ID;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String NOTIFY_CUSTOMER = "0";
    String  status_code , msg ,token, UPDATE_ENQUIRY_ID ,API_TOKEN , BOOKING_DATE , BOOKING_TIME ,contact_id , contact_status ,
            AMOUNT_PAID , PAYMENT_MODE  , BOOKING_FOLLOWUP , CUSTOMER_STATUS_ID , FOLLOW_UP_DATE ,
            FOLLOW_UP_TIME , FOLLOW_UP_TYPE , FOLLOW_UP_REMARK;
    String vehicles_getallvehicle_url = BASE_URL + "customer/getmodels";
    String customer_getmodelvariants_url = BASE_URL + "customer/getmodelvariants";
    String vehiclecolormaster_getallinteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String vehiclecolormaster_getallexteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String booking_getpaymentmodes_url = BASE_URL + "booking/getpaymentmodes";
    String booking_completebooking_url = BASE_URL + "booking/completebooking";
    String booking_getbooking_details_url = BASE_URL + "booking/getbookingdetails";
    String booking_updatebooking_details_url = BASE_URL + "booking/updatebookingdetails";
    List<ModelsList> modelsLists;
    List<VariantsLIst> variantsLIsts;
    List<InteriorColorsList>interiorColorsLists;
    List<ExteriorColorsList>exteriorColorsLists;
    List<PaymentModesList>paymentModesLists;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_completed);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = BookingCompletedActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(BookingCompletedActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(BookingCompletedActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(BookingCompletedActivity.this, Constants.TOKEN,"");
        edt_date_booking = findViewById(R.id.edt_date_booking);
        edt_time_booking = findViewById(R.id.edt_time_booking);
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        spinner_payment_mode = findViewById(R.id.spinner_payment_mode);
        spinner_model_suffix_cop = (Spinner) findViewById(R.id.spinner_model_suffix_cop);
        spinner_model_variant = (Spinner) findViewById(R.id.spinner_model_variant);
        spinner_exterEior_color = (Spinner) findViewById(R.id.spinner_exterEior_color);
        spinner_interior_color = (Spinner) findViewById(R.id.spinner_interior_color);
        edt_cop_booking_amount = findViewById(R.id.edt_cop_booking_amount);
        swt_notify_customer = findViewById(R.id.swt_notify_customer);
        swt_status_follow_up = findViewById(R.id.swt_status_follow_up);
        edt_remark = findViewById(R.id.edt_remark);
        lin_follow_up  = findViewById(R.id.lin_follow_up);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        txt_update_status = findViewById(R.id.txt_update_status);
        txt_edit_booking = findViewById(R.id.txt_edit_booking);
        modelsLists = new ArrayList<>();
        variantsLIsts = new ArrayList<>();
        interiorColorsLists = new ArrayList<>();
        exteriorColorsLists = new ArrayList<>();
        paymentModesLists = new ArrayList<>();
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        scheduled_status = getIntent().getStringExtra("SCHEDULED_STATUS");
                //getIntent().getStringExtra("SCHEDULED_STATUS");
              //

        edt_time_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingCompletedActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edt_time_booking.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        edt_date_booking.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edt_date_booking.getRight() - edt_date_booking.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingCompletedActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                        edt_date_booking.setText(strDate);
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

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingCompletedActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(BookingCompletedActivity.this,
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
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

        swt_notify_customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    NOTIFY_CUSTOMER  = "1";
                } else {
                    // The toggle is disabled
                    NOTIFY_CUSTOMER =  "0";
                }
            }
        });

        swt_status_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    swt_status_follow_up.setChecked(true);
                }
            }
        });

        txt_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    isCallPressed = true;
                    FOLLOW_UP_TYPE = "1";
                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));

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
                    isDirectPlace = true;
                    FOLLOW_UP_TYPE = "2";
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                    if (isCallPressed) {
                        txt_telephone.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                        isCallPressed = false;
                    }
            }
        });



        spinner_model_suffix_cop.setOnItemSelectedListener(this);

        spinner_model_variant.setOnItemSelectedListener(this);

        spinner_interior_color.setOnItemSelectedListener(this);

        spinner_exterEior_color.setOnItemSelectedListener(this);

        spinner_payment_mode.setOnItemSelectedListener(this);

        if (scheduled_status.equals("NEW BOOKING")){
            txt_update_status.setVisibility(View.VISIBLE);
            txt_edit_booking.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
            String strtime = spf.format(calendar.getTime());
            edt_time_booking.setText(strtime);
            edt_time.setText(strtime);
            SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
            String strDates = dateformat.format(calendar.getTime());
            edt_date_booking.setText(strDates);
            edt_date.setText(strDates);
            Vehicles_Getallvehicle();

            Booking_Getpaymentmodes();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(BookingCompletedActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();

        }else if (scheduled_status.equals("EDIT NEW BOOKING")){
            txt_update_status.setVisibility(View.GONE);
            txt_edit_booking.setVisibility(View.VISIBLE);
            Booking_Getbooking_Details();
            Vehicles_Getallvehicle();
            Booking_Getpaymentmodes();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(BookingCompletedActivity.this);
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

    public void back(View view) {
        startActivity(new Intent(BookingCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status","Booking Completed"));
         finish();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // TODO Auto-generated method stub
        switch (parent.getId()) {

            case R.id.spinner_model_suffix_cop:
                if (position == 0) {
                    vehicle_id = "1";
                   // Customer_Get_Model_Variants();
                    //Vehiclecolormaster_Getallinteriorcolors();
                    // Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    vehicle_id = modelsLists.get(position).getId();
                    MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.VEHICLEID,vehicle_id);
                    if (vehicle_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
                        builder.setTitle("USER MESSAGE");
                        builder.setMessage("Please Select Mode First!!");
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
                        Customer_Get_Model_Variants();

                        //  Vehiclecolormaster_Getallinteriorcolors();
                        //  Vehiclecolormaster_Getallexteriorcolors();
                    }
                }
                break;

            case R.id.spinner_model_variant:
                if (position == 0) {
                    model_variant = "1";
                   // Vehiclecolormaster_Getallinteriorcolors();
                    //Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    model_variant = variantsLIsts.get(position).getId();
                    MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.MODEL_VARIANT,String.valueOf(position));
                    if (model_variant == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
                        builder.setTitle("USER MESSAGE");
                        builder.setMessage("Please Select Mode First!!");
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
                        Vehiclecolormaster_Getallinteriorcolors();
                        Vehiclecolormaster_Getallexteriorcolors();
                    }
                }
                break;

            case R.id.spinner_interior_color:
                // interior_color_id = interiorColorsLists.get(position).getInterior_color_id();
                interior_color_id = interiorColorsLists.get(position).getInterior_color_id();
                MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.INTERIOR,String.valueOf(position));
                break;

            case R.id.spinner_exterEior_color:
                exterior_color_id = exteriorColorsLists.get(position).getExterior_color_id();
                MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.EXTEREIOR,String.valueOf(position));
                break;

            case R.id.spinner_payment_mode:
                PAYMENT_MODE = paymentModesLists.get(position).getPayment_mode_id();
                MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.PAYMENTSMODE,String.valueOf(position));
                break;    
                
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void updatestatus(View view) {

        if (vehicle_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_model_suffix_cop.getSelectedView().findViewById(R.id.offer_model_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        if (interior_color_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_interior_color.getSelectedView().findViewById(R.id.offer_interior_color_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }


        if (exterior_color_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_exterEior_color.getSelectedView().findViewById(R.id.offer_exterior_color_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        if (PAYMENT_MODE.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_payment_mode.getSelectedView().findViewById(R.id.offer_paymentmode_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        String followupdate = edt_date.getText().toString().trim();
        if (followupdate.equals("") || followupdate.isEmpty()) {
            edt_date.setError("Please Selecte Dob");
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

        String followuptime = edt_time.getText().toString().trim()+":"+"00";
        if (followuptime.equals("") || followuptime.isEmpty()) {
            edt_time.setError("Please Selecte Time");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spf.parse(followuptime);
                spf = new SimpleDateFormat("HH:mm");
                FOLLOW_UP_TIME = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AMOUNT_PAID = edt_cop_booking_amount.getText().toString().toString();

        if (AMOUNT_PAID.equals("")){
            edt_cop_booking_amount.setError("Please Enter Booking Amount");
        }

        FOLLOW_UP_REMARK = edt_remark.getText().toString().toString();

        if (FOLLOW_UP_REMARK.equals("")){
          //  edt_remark.setError("Please Enter Booking Amount");
            FOLLOW_UP_REMARK = "";
        }


        String bookingdate = edt_date_booking.getText().toString().trim();
        if (bookingdate.equals("") || bookingdate.isEmpty()) {
            edt_date_booking.setError("Please Selecte Booking date");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date newDate = spf.parse(bookingdate);
                spf = new SimpleDateFormat("yyyy-MM-dd");
                BOOKING_DATE = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String bookingtime = edt_time_booking.getText().toString().trim()+":"+"00";
        if (bookingtime.equals("") || bookingtime.isEmpty()) {
            edt_time_booking.setError("Please Booking Time");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spf.parse(bookingtime);
                spf = new SimpleDateFormat("HH:mm");
                BOOKING_TIME = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (vehicle_id.equals("")||interior_color_id.equals("")||exterior_color_id.equals("")
                ||PAYMENT_MODE.equals("") || BOOKING_DATE.equals("")||BOOKING_TIME.equals("")||AMOUNT_PAID.equals("")){
            Toast.makeText(BookingCompletedActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();

        }else {
            Booking_Complete();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(BookingCompletedActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();
            //Toast.makeText(BookingCompletedActivity.this, "Fill all the", Toast.LENGTH_SHORT).show();
        }

    }

    // Api call Vehicles_Getallvehicle
    public void Vehicles_Getallvehicle () {
        StringRequest requests = new StringRequest(Request.Method.POST, vehicles_getallvehicle_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("data");
                            modelsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ModelsList modeList = new ModelsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setModel(jsonObject_titles.getString("model"));
                                modeList.setId(jsonObject_titles.getString("id"));
                               // modeList.setModel_suffix(jsonObject_titles.getString("model_suffix"));
                                modelsLists.add(modeList);
                            }
                            ModelsTypeArrayAdapter adapter = new ModelsTypeArrayAdapter(BookingCompletedActivity.this,
                                    R.layout.layout_model_type, modelsLists);
                            spinner_model_suffix_cop.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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

    // Api call Customer_Get_Model_Variants
    public void Customer_Get_Model_Variants(){
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getmodelvariants_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("data");
                            variantsLIsts.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                VariantsLIst modeList = new VariantsLIst();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setMode(jsonObject_titles.getString("model"));
                                modeList.setId(jsonObject_titles.getString("id"));
                                variantsLIsts.add(modeList);
                            }
                            ModelVariantTypeArrayAdapter adapter = new ModelVariantTypeArrayAdapter(BookingCompletedActivity.this,
                                    R.layout.layout_variant_type, variantsLIsts);

                            spinner_model_variant.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                System.out.println("VECHICAL" + params);
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

    // Api call Vehiclecolormaster_Getallinteriorcolors
    public void Vehiclecolormaster_Getallinteriorcolors () {

        StringRequest requests = new StringRequest(Request.Method.POST, vehiclecolormaster_getallinteriorcolors_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObjcolor = jsonObj.getJSONObject("colors");
                            System.out.println("COLOR" + jsonObjcolor.toString());
                            JSONArray jsonArray_titles = jsonObjcolor.getJSONArray("interior_colors");
                            interiorColorsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                InteriorColorsList modeList = new InteriorColorsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setInterior_color_id(jsonObject_titles.getString("interior_color_id"));
                                modeList.setInterior_color(jsonObject_titles.getString("interior_color"));
                                interiorColorsLists.add(modeList);
                            }
                           InteriorColorTypeArrayAdapter adapter = new InteriorColorTypeArrayAdapter(BookingCompletedActivity.this,
                                    R.layout.layout_model_type, interiorColorsLists);

                            spinner_interior_color.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            // interiorColorsLists.clear();
                            // adapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
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

    // Api call Vehiclecolormaster_Getallexteriorcolors
    public void Vehiclecolormaster_Getallexteriorcolors () {
        StringRequest requests = new StringRequest(Request.Method.POST, vehiclecolormaster_getallexteriorcolors_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObjcolor = jsonObj.getJSONObject("colors");
                            JSONArray jsonArray_titles = jsonObjcolor.getJSONArray("exterior_colors");
                            exteriorColorsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ExteriorColorsList modeList = new ExteriorColorsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setExterior_color_id(jsonObject_titles.getString("exterior_color_id"));
                                modeList.setExterior_color(jsonObject_titles.getString("exterior_color"));
                                exteriorColorsLists.add(modeList);
                            }
                           ExteriorColorArrayAdapter exadapter = new ExteriorColorArrayAdapter(BookingCompletedActivity.this,
                                    R.layout.layout_exteriorcolor_type, exteriorColorsLists);

                            spinner_exterEior_color.setAdapter(exadapter);

                        } else if (status_code.equals("0")) {
                            //exteriorColorsLists.clear();
                            //exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
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

    // Api call Booking_Getpaymentmodes
    public void Booking_Getpaymentmodes () {
        StringRequest requests = new StringRequest(Request.Method.POST, booking_getpaymentmodes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                           // JSONObject jsonObjcolor = jsonObj.getJSONObject("colors");
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("payment_modes");
                            paymentModesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                PaymentModesList modeList = new PaymentModesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setPayment_mode_id(jsonObject_titles.getString("payment_mode_id"));
                                modeList.setPayment_mode(jsonObject_titles.getString("payment_mode"));
                                paymentModesLists.add(modeList);
                            }
                            PaymentModesAdapter exadapter = new PaymentModesAdapter(BookingCompletedActivity.this,
                                    R.layout.layout_payment_mode_type, paymentModesLists);

                            spinner_payment_mode.setAdapter(exadapter);

                        } else if (status_code.equals("0")) {
                            //exteriorColorsLists.clear();
                            //exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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


    // Api call Booking_Complete
    public void Booking_Complete() {
        StringRequest requests = new StringRequest(Request.Method.POST, booking_completebooking_url, new Response.Listener<String>() {
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
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String booking_completed_id = (String)jsonObject.get("booking_completed_id").toString();
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGCOMPLETEDID,booking_completed_id);
                            String booking_mode_and_suffix = (String)jsonObject.get("booking_model_and_suffix");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGMODEANDSUFFIX,booking_mode_and_suffix);
                            String interior_color = (String)jsonObject.get("interior_color");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.INTERIORCOLOR,interior_color);
                            String exterior_color = (String)jsonObject.get("exterior_color");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.EXTERIORCOLOR,exterior_color);
                            String booking_amount = jsonObject.getString("booking_amount");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGAMOUNT,booking_amount);
                            String booking_date = jsonObject.getString("booking_date");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGDATE,booking_date);
                            String booking_time = jsonObject.getString("booking_time");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGTIME,booking_time);
                            String payment_mode = jsonObject.getString("payment_mode");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.PAYMENTMODE,payment_mode);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPID,followupid);
                            startActivity(new Intent(BookingCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("BOOKING_DATE", BOOKING_DATE);
                params.put("BOOKING_TIME", BOOKING_TIME);
                params.put("AMOUNT_PAID", AMOUNT_PAID);
                params.put("PAYMENT_MODE", PAYMENT_MODE);
                params.put("NOTIFY_CUSTOMER", NOTIFY_CUSTOMER);
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


    public void Booking_Getbooking_Details(){

        StringRequest requests = new StringRequest(Request.Method.POST, booking_getbooking_details_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                          JSONObject jsonObject =  jsonObj.getJSONObject("booking_details");
                            if (jsonObject.isNull("MODEL")){
                                MODEL_SUFFIX_ID = "";
                            }else {
                                MODEL_SUFFIX_ID = jsonObject.getString("MODEL");
                                spinner_model_suffix_cop.setSelection(Integer.parseInt(MODEL_SUFFIX_ID));
                            }

                            if (jsonObject.isNull("INTERIOR_COLOR_ID")){
                                INTERIOR_COLOR_ID = "";
                            }else {
                                INTERIOR_COLOR_ID = jsonObject.getString("INTERIOR_COLOR_ID");
                                spinner_interior_color.setSelection(Integer.parseInt(INTERIOR_COLOR_ID));
                            }

                            if (jsonObject.isNull("EXTERIOR_COLOR_ID")){
                                EXTERIOR_COLOR_ID = "";
                            }else {
                                EXTERIOR_COLOR_ID = jsonObject.getString("EXTERIOR_COLOR_ID");
                                spinner_exterEior_color.setSelection(Integer.parseInt(EXTERIOR_COLOR_ID));
                            }

                            if (jsonObject.isNull("BOOKING_DATE")){
                                BOOKINGDATE = "";
                            }else {
                                BOOKINGDATE = jsonObject.getString("BOOKING_DATE");

                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(BOOKINGDATE);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt_date_booking.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (jsonObject.isNull("BOOKING_TIME")){
                                BOOKINGTIME = "";
                            }else {
                                BOOKINGTIME = jsonObject.getString("BOOKING_TIME");
                                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                                try {
                                    Date newDate = spf.parse(BOOKINGTIME);
                                    spf = new SimpleDateFormat("HH:mm");
                                    String followuptime = spf.format(newDate);
                                    edt_time_booking.setText(followuptime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (jsonObject.isNull("AMOUNT_PAID")){
                                AMOUNTPAID = "";
                            }else {
                                AMOUNTPAID = jsonObject.getString("AMOUNT_PAID");
                                edt_cop_booking_amount.setText(AMOUNTPAID);
                            }

                            if (jsonObject.isNull("PAYMENT_MODE")){
                                PAYMENTMODE = "";
                            }else {
                                PAYMENTMODE = jsonObject.getString("PAYMENT_MODE");
                                spinner_payment_mode.setSelection(Integer.parseInt(PAYMENTMODE));
                            }
                            if (jsonObject.isNull("NOTIFY_CUSTOMER")){
                                NOTIFYCUSTOMER = "";
                            }else {
                                NOTIFYCUSTOMER = jsonObject.getString("NOTIFY_CUSTOMER");
                               if (NOTIFYCUSTOMER.equals("1")){
                                   swt_notify_customer.setChecked(true);
                                   NOTIFYCUSTOMER = "1";
                               }else if (NOTIFYCUSTOMER.equals("0")){
                                   swt_notify_customer.setChecked(false);
                                   NOTIFY_CUSTOMER = "0";
                               }
                            }

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("ENQUIRY_ID", UPDATE_ENQUIRY_ID);
                params.put("BOOKING_COMPLETED_ID", MyFunctions.getSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGCOMPLETEDID,""));
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

    public void editupdatestatus(View view) {

        if (vehicle_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_model_suffix_cop.getSelectedView().findViewById(R.id.offer_model_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        if (interior_color_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_interior_color.getSelectedView().findViewById(R.id.offer_interior_color_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }


        if (exterior_color_id.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_exterEior_color.getSelectedView().findViewById(R.id.offer_exterior_color_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        if (PAYMENT_MODE.equals("1")) {
            // get selected item value
            TextView viewById =  spinner_payment_mode.getSelectedView().findViewById(R.id.offer_paymentmode_txt);
            viewById.setTextColor(Color.RED);
            viewById.setError("Field Required");
            viewById.setText("Field Required");//changes the selected item text to this
            //errorTextview.spinner_profession.getSelectedView();
            // errorTextview.setError("Your Error Message here");
        }

        String followupdate = edt_date.getText().toString().trim();
        if (followupdate.equals("") || followupdate.isEmpty()) {
            edt_date.setError("Please Selecte Dob");
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

        String followuptime = edt_time.getText().toString().trim()+":"+"00";
        if (followuptime.equals("") || followuptime.isEmpty()) {
            edt_time.setError("Please Selecte Time");
        } else {
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spf.parse(followuptime);
                spf = new SimpleDateFormat("HH:mm");
                FOLLOW_UP_TIME = spf.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AMOUNT_PAID = edt_cop_booking_amount.getText().toString().toString();

        if (AMOUNT_PAID.equals("")){
            edt_cop_booking_amount.setError("Please Enter Booking Amount");
        }

        FOLLOW_UP_REMARK = edt_remark.getText().toString().toString();

        if (FOLLOW_UP_REMARK.equals("")){
            //  edt_remark.setError("Please Enter Booking Amount");
            FOLLOW_UP_REMARK = "";
        }


        String bookingdate = edt_date_booking.getText().toString().trim();
        if (bookingdate.equals("") || bookingdate.isEmpty()) {
            edt_date_booking.setError("Please Selecte Booking date");
        } else {
            SimpleDateFormat spfs = new SimpleDateFormat("dd/MM/yyyy");
            try {

                Date newDateone = spfs.parse(bookingdate);
                spfs = new SimpleDateFormat("yyyy-MM-dd");
                BOOKING_DATE = spfs.format(newDateone);
                        //

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String bookingtimes = edt_time_booking.getText().toString().trim()+":"+"00";
        if (bookingtimes.equals("") || bookingtimes.isEmpty()) {
            edt_time_booking.setError("Please Booking Time");
        } else {
            SimpleDateFormat spfw = new SimpleDateFormat("HH:mm:ss");
            try {

                Date newDate = spfw.parse(bookingtimes);
                spfw = new SimpleDateFormat("HH:mm");
                BOOKING_TIME = spfw.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (vehicle_id.equals("")||interior_color_id.equals("")||exterior_color_id.equals("")
                ||PAYMENT_MODE.equals("")||BOOKING_DATE.equals("")||BOOKING_TIME.equals("")||AMOUNT_PAID.equals("")){
            Toast.makeText(BookingCompletedActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();

        }else {
            Booking_Updatebooking_Details();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(BookingCompletedActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();
            //Toast.makeText(BookingCompletedActivity.this, "Fill all the", Toast.LENGTH_SHORT).show();
        }


    }

    public void Booking_Updatebooking_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, booking_updatebooking_details_url, new Response.Listener<String>() {
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
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String booking_completed_id = (String)jsonObject.get("booking_completed_id").toString();
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGCOMPLETEDID,booking_completed_id);
                            String booking_mode_and_suffix = (String)jsonObject.get("booking_model_and_suffix");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGMODEANDSUFFIX,booking_mode_and_suffix);
                            String interior_color = (String)jsonObject.get("interior_color");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.INTERIORCOLOR,interior_color);
                            String exterior_color = (String)jsonObject.get("exterior_color");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.EXTERIORCOLOR,exterior_color);
                            String booking_amount = jsonObject.getString("booking_amount");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGAMOUNT,booking_amount);
                            String booking_date = jsonObject.getString("booking_date");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGDATE,booking_date);
                            String booking_time = jsonObject.getString("booking_time");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGTIME,booking_time);
                            String payment_mode = jsonObject.getString("payment_mode");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.PAYMENTMODE,payment_mode);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(BookingCompletedActivity.this,Constants.FOLLOWUPID,followupid);
                            startActivity(new Intent(BookingCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingCompletedActivity.this);
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
                params.put("BOOKING_COMPLETED_ID", MyFunctions.getSharedPrefs(BookingCompletedActivity.this,Constants.BOOKINGCOMPLETEDID,""));
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("BOOKING_DATE",BOOKING_DATE);
                params.put("BOOKING_TIME", BOOKING_TIME);
                params.put("AMOUNT_PAID", AMOUNT_PAID);
                params.put("PAYMENT_MODE", PAYMENT_MODE);
                params.put("NOTIFY_CUSTOMER", NOTIFY_CUSTOMER);
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



}
