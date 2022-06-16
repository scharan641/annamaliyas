package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;
import static com.anaamalais.salescrm.Utils.Constants.BOOKING_FOLLOWUP_DETAILS_ID;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.anaamalais.salescrm.Adapter.SourceTypeArrayAdapter;
import com.anaamalais.salescrm.List.SourcesList;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BookingFollowUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] extendedwarranty={" Vehicle Allocated","Yes","No"};
    Spinner spinner_vechical;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN, UPDATE_ENQUIRY_ID,TCSRATE,scheduled_status;
    TextInputEditText edt_ex_showroom_price , edt_tcs , edt_tcs_rate , edt_road_tax, edt_insurance ,edt_registration
            ,edt_accessories , edt_warranty , edt_fastag , edt_offers , edt_expected_downpayment_amount;
    TextView edt_cus_date , edt_expected_invoice_date , txt_net_amount , txt_login_date_time , txt_login_completed
            , txt_query_clearance , txt_loan_amount,txt_paper_documents_collected;
    String contact_id , contact_status;
    int mYear, mDay ,mMonth,mMinute,mHour;
    int EX_SHPOW_ROOM_PRICE , NETAMOUNT , ROADNETAMOUNT , INSURANCEAMOUNT , REGISTRATIONS ,
            ACCESSORIESS , WARRANTYS , FATGS , OFFERSs;

    String BOOKINGFOLLOWUPDETAILSID , VEHICLEALLOTTED , VEHICLENOTALLOTTEDREASON , EXSHOWROOMPRICE , TCCS ,
            TCSAMOUNT , ROADTAX, INSURANCES , REGISTR ,  ACCESSORI, WARRANT ,  FASTG , OFERS , NEAMOUNT ,
            EXPECTED_DOWNPAYMENT_AMOUNT , EXPECTED_DOWNPAYMENT_DATE , EXPECTED_INVOICE_DATE ,  PAPER_DOCUMENTS_COLLECTED
             , LOGIN_COMPLETED_DATE , VEHICLEALLOTED , LOGIN_COMPLETED , QUERY_CLEARANCE , LOAN_AMOUNT , FOLLOW_UP_DATE  , FOLLOW_UP_TIME , FOLLOW_UP_TYPE , FOLLOW_UP_REMARK;


    String ENQUIRY_ID , VEHICLE_ALLOTED , VEHICLE_NOT_ALLOTED_REASON , EX_SHOWROOM_PRICE , TCS ,
            TCS_AMOUNT , ROAD_TAX , INSURANCE , REGISTRATION , ACCESSORIES , WARRANTY , FASTAG ,
            OFFERS , NET_AMOUNT , EXP_DOWN_PAYMENT , EXP_DOWN_PAYMENT_DATE , EXP_INVOICE_DATE ;
    String bookingfollowupinfo_url = BASE_URL + "bookingfollowup/bookingfollowupinfo";
    String bookingfollowup_addbookingfollowup_url = BASE_URL + "bookingfollowup/addbookingfollowup";
    String getbookingfollowupdetails_url = BASE_URL + "bookingfollowup/getbookingfollowupdetails";
    String updatebookingfollowup_url = BASE_URL + "bookingfollowup/updatebookingfollowup";
    String bookingfollowup_vehicleprice_url = BASE_URL + "bookingfollowup/getbookedvehicleprice";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_follow_up);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = BookingFollowUpActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(BookingFollowUpActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(BookingFollowUpActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(BookingFollowUpActivity.this, Constants.TOKEN, "");
        edt_ex_showroom_price = findViewById(R.id.edt_ex_showroom_price);
        edt_tcs = findViewById(R.id.edt_tcs);
        edt_tcs_rate = findViewById(R.id.edt_tcs_rate);
        edt_road_tax = findViewById(R.id.edt_road_tax);
        edt_insurance = findViewById(R.id.edt_insurance);
        edt_registration = findViewById(R.id.edt_registration);
        edt_accessories = findViewById(R.id.edt_accessories);
        edt_warranty = findViewById(R.id.edt_warranty);
        edt_fastag = findViewById(R.id.edt_fastag);
        edt_offers = findViewById(R.id.edt_offers);
        edt_expected_downpayment_amount = findViewById(R.id.edt_expected_downpayment_amount);
        edt_cus_date = findViewById(R.id.edt_date);
        edt_expected_invoice_date = findViewById(R.id.edt_expected_invoice_date);
        txt_net_amount = findViewById(R.id.txt_net_amount);
        txt_login_date_time = findViewById(R.id.txt_login_date_time);
        txt_login_completed = findViewById(R.id.txt_login_completed);
        txt_query_clearance = findViewById(R.id.txt_query_clearance);
        txt_loan_amount  = findViewById(R.id.txt_loan_amount);
        txt_paper_documents_collected = findViewById(R.id.txt_paper_documents_collected);
        spinner_vechical = findViewById(R.id.spinner_vechical);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        edt_expected_invoice_date.setText(strDates);
        edt_cus_date.setText(strDates);
        scheduled_status = getIntent().getStringExtra("SCHEDULED_STATUS");
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        if (scheduled_status.equals("NEW SCHEDULED")){
          //  txt_update_status.setVisibility(View.VISIBLE);
          //  txt_edit_schedule.setVisibility(View.GONE);
            Booking_Followup_Info();
            Get_Booked_Vehicle_Price();
        }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){
           // txt_update_status.setVisibility(View.GONE);
          //  txt_edit_schedule.setVisibility(View.VISIBLE);
            Get_Booking_Follow_Up_Details();
        }


        edt_ex_showroom_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    EX_SHPOW_ROOM_PRICE = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }


            }
        });

        edt_tcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    float rCtWt = Float.parseFloat(s.toString());
                    float mk = Float.parseFloat(edt_ex_showroom_price.getText().toString());
                    TCSRATE = ((rCtWt * mk) / 100) + "";
                    edt_tcs_rate.setText(TCSRATE);
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_tcs_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
              //  NETAMOUNT = EX_SHPOW_ROOM_PRICE + Integer.parseInt(edt_tcs_rate.getText().toString());
               // System.out.println("cashauj"+ NETAMOUNT );
                // txt_net_amount.setText(String.valueOf(NETAMOUNT));

            }
        });

        edt_road_tax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    ROADNETAMOUNT = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_insurance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    INSURANCEAMOUNT = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_registration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try{
                    REGISTRATIONS = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT + REGISTRATIONS;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_accessories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    ACCESSORIESS = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT + REGISTRATIONS + ACCESSORIESS;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_warranty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try{
                    WARRANTYS = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT + REGISTRATIONS + ACCESSORIESS + WARRANTYS;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_fastag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    FATGS = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT + REGISTRATIONS + ACCESSORIESS + FATGS;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edt_offers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    OFFERSs = Integer.parseInt(s.toString());
                    NETAMOUNT = EX_SHPOW_ROOM_PRICE + ROADNETAMOUNT + INSURANCEAMOUNT + REGISTRATIONS + ACCESSORIESS + FATGS + WARRANTYS + OFFERSs;
                    txt_net_amount.setText(String.valueOf(NETAMOUNT));
                } catch(NumberFormatException ex){ // handle your exception
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        edt_cus_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // your action here
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingFollowUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                CharSequence strDate = null;
                                android.text.format.Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);
                                long dtDob = chosenDate.toMillis(true);

                                strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                edt_cus_date.setText(strDate);
                                //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                //  datePickerDialog.show();

            }

        });

        edt_expected_invoice_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // your action here
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingFollowUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                CharSequence strDate = null;
                                android.text.format.Time chosenDate = new Time();
                                chosenDate.set(dayOfMonth, monthOfYear, year);
                                long dtDob = chosenDate.toMillis(true);

                                strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                edt_expected_invoice_date.setText(strDate);
                                //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                //  datePickerDialog.show();

            }

        });

        // Spinner click listener
        spinner_vechical.setOnItemSelectedListener(this);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(BookingFollowUpActivity.this, android.R.layout.simple_spinner_item, extendedwarranty){
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
                    Typeface typeface = ResourcesCompat.getFont(BookingFollowUpActivity.this, R.font.poppins);
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
                    Typeface typeface = ResourcesCompat.getFont(BookingFollowUpActivity.this, R.font.poppins);
                    ((TextView) view).setTypeface(typeface);
                    ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
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
        spinner_vechical.setAdapter(dataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.spinner_vechical:
                VEHICLEALLOTED = Integer.toString(position);
                if (VEHICLEALLOTED.equals("1")){
                    VEHICLE_ALLOTED = "1";
                }else if (VEHICLEALLOTED.equals("2")){
                    VEHICLE_ALLOTED = "0";
                }
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void back(View view) {
        startActivity(new Intent(BookingFollowUpActivity.this,CustomerDetailsActivity.class).putExtra("Status","Booking Followup"));
         finish();
    }

    public void updatestatus(View view) {

       if (VEHICLE_ALLOTED != null){
           if (scheduled_status.equals("NEW SCHEDULED")){

               EX_SHOWROOM_PRICE = edt_ex_showroom_price.getText().toString();
               if (EX_SHOWROOM_PRICE.equals("")||EX_SHOWROOM_PRICE.isEmpty()){
                   edt_ex_showroom_price.setError("Fill the EX SHOWROOM PRICE");
               }

               TCS = edt_tcs.getText().toString();
               if (TCS.equals("")||TCS.isEmpty()){
                   edt_tcs.setError("Fill the TCS");
               }

               TCS_AMOUNT = edt_tcs_rate.getText().toString();
               if (TCS_AMOUNT.equals("")||TCS_AMOUNT.isEmpty()){
                   edt_tcs.setError("Fill the TCS AMOUNT");
               }

               ROAD_TAX = edt_road_tax.getText().toString();
               if (ROAD_TAX.equals("")||ROAD_TAX.isEmpty()){
                   edt_road_tax.setError("Fill the ROAD TAX");
               }

               INSURANCE = edt_insurance.getText().toString();
               if (INSURANCE.equals("")||INSURANCE.isEmpty()){
                   edt_insurance.setError("Fill the INSURANCE");
               }

               REGISTRATION = edt_registration.getText().toString();
               if (REGISTRATION.equals("")||REGISTRATION.isEmpty()){
                   edt_registration.setError("Fill the REGISTRATION");
               }

               ACCESSORIES = edt_accessories.getText().toString();
               if (ACCESSORIES.equals("")||ACCESSORIES.isEmpty()){
                   edt_accessories.setError("Fill the ACCESSORIES");
               }

               WARRANTY = edt_warranty.getText().toString();
               if (WARRANTY.equals("")||WARRANTY.isEmpty()){
                   edt_accessories.setError("Fill the WARRANTY");
               }

               FASTAG = edt_fastag.getText().toString();
               if (FASTAG.equals("")||FASTAG.isEmpty()){
                   edt_fastag.setError("Fill the FASTAG");
               }

               OFFERS = edt_offers.getText().toString();
               if (OFFERS.equals("")||OFFERS.isEmpty()){
                   edt_offers.setError("Fill the OFFERS");
               }

               NET_AMOUNT = txt_net_amount.getText().toString();
               if (NET_AMOUNT.equals("")||NET_AMOUNT.isEmpty()){
                   txt_net_amount.setError("Fill the NET_AMOUNT");
               }

               EXP_DOWN_PAYMENT = edt_expected_downpayment_amount.getText().toString();
               if (EXP_DOWN_PAYMENT.equals("")||EXP_DOWN_PAYMENT.isEmpty()){
                   edt_expected_downpayment_amount.setError("Fill the EXP_DOWN_PAYMENT");
               }

               String EXPDOWNPAYMENTDATE = edt_cus_date.getText().toString();
               if (EXPDOWNPAYMENTDATE.equals("") || EXPDOWNPAYMENTDATE.isEmpty()) {
                   edt_expected_invoice_date.setError("Please Selecte Dob");
               } else {
                   SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                   try {

                       Date newDate = spf.parse(EXPDOWNPAYMENTDATE);
                       spf = new SimpleDateFormat("yyyy-MM-dd");
                       EXP_DOWN_PAYMENT_DATE = spf.format(newDate);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               String EXPINVOICEDATE = edt_expected_invoice_date.getText().toString();
               if (EXPINVOICEDATE.equals("") || EXPINVOICEDATE.isEmpty()) {
                   edt_expected_invoice_date.setError("Please Selecte Dob");
               } else {
                   SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                   try {

                       Date newDate = spf.parse(EXPINVOICEDATE);
                       spf = new SimpleDateFormat("yyyy-MM-dd");
                       EXP_INVOICE_DATE = spf.format(newDate);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               if (VEHICLE_ALLOTED.equals("1")){
                   VEHICLE_NOT_ALLOTED_REASON = "";
               }else if (VEHICLE_ALLOTED.equals("0")){
                   VEHICLE_NOT_ALLOTED_REASON = "";
               }

               if (EX_SHOWROOM_PRICE.equals("")||TCS.equals("")||TCS_AMOUNT.equals("")||ROAD_TAX.equals("")
                       ||INSURANCE.equals("")||REGISTRATION.equals("")||ACCESSORIES.equals("")||WARRANTY.equals("")
                       ||FASTAG.equals("")||OFFERS.equals("")||NET_AMOUNT.equals("")||EXP_DOWN_PAYMENT.equals("")||
                       EXP_DOWN_PAYMENT_DATE.equals("")||EXP_INVOICE_DATE.equals("") ){
                   Toast.makeText(BookingFollowUpActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();

               }else {
                   Booking_Followup();
                   //initialize the progress dialog and show it
                   progressDialog = new ProgressDialog(BookingFollowUpActivity.this);
                   progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                   progressDialog.setIndeterminate(true);
                   progressDialog.setCancelable(false);
                   progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                   ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                   anim.setDuration(15000);
                   anim.setInterpolator(new DecelerateInterpolator());
                   progressDialog.show();
               }
           }else if (scheduled_status.equals("EDIT NEW SCHEDULED")){

               EX_SHOWROOM_PRICE = edt_ex_showroom_price.getText().toString();
               if (EX_SHOWROOM_PRICE.equals("")||EX_SHOWROOM_PRICE.isEmpty()){
                   edt_ex_showroom_price.setError("Fill the EX SHOWROOM PRICE");
               }

               TCS = edt_tcs.getText().toString();
               if (TCS.equals("")||TCS.isEmpty()){
                   edt_tcs.setError("Fill the TCS");
               }

               TCS_AMOUNT = edt_tcs_rate.getText().toString();
               if (TCS_AMOUNT.equals("")||TCS_AMOUNT.isEmpty()){
                   edt_tcs_rate.setError("Fill the TCS AMOUNT");
               }

               ROAD_TAX = edt_road_tax.getText().toString();
               if (ROAD_TAX.equals("")||ROAD_TAX.isEmpty()){
                   edt_road_tax.setError("Fill the ROAD TAX");
               }

               INSURANCE = edt_insurance.getText().toString();
               if (INSURANCE.equals("")||INSURANCE.isEmpty()){
                   edt_insurance.setError("Fill the INSURANCE");
               }

               REGISTRATION = edt_registration.getText().toString();
               if (REGISTRATION.equals("")||REGISTRATION.isEmpty()){
                   edt_registration.setError("Fill the REGISTRATION");
               }

               ACCESSORIES = edt_accessories.getText().toString();
               if (ACCESSORIES.equals("")||ACCESSORIES.isEmpty()){
                   edt_accessories.setError("Fill the ACCESSORIES");
               }

               WARRANTY = edt_warranty.getText().toString();
               if (WARRANTY.equals("")||WARRANTY.isEmpty()){
                   edt_warranty.setError("Fill the WARRANTY");
               }

               FASTAG = edt_fastag.getText().toString();
               if (FASTAG.equals("")||FASTAG.isEmpty()){
                   edt_fastag.setError("Fill the FASTAG");
               }

               OFFERS = edt_offers.getText().toString();
               if (OFFERS.equals("")||OFFERS.isEmpty()){
                   edt_offers.setError("Fill the OFFERS");
               }

               NET_AMOUNT = txt_net_amount.getText().toString();
               if (NET_AMOUNT.equals("")||NET_AMOUNT.isEmpty()){
                   txt_net_amount.setError("Fill the NET_AMOUNT");
               }

               EXP_DOWN_PAYMENT = edt_expected_downpayment_amount.getText().toString();
               if (EXP_DOWN_PAYMENT.equals("")||EXP_DOWN_PAYMENT.isEmpty()){
                   edt_expected_downpayment_amount.setError("Fill the EXP_DOWN_PAYMENT");
               }

               String EXPDOWNPAYMENTDATE = edt_cus_date.getText().toString();
               if (EXPDOWNPAYMENTDATE.equals("") || EXPDOWNPAYMENTDATE.isEmpty()) {
                   edt_expected_invoice_date.setError("Please Selecte Dob");
               } else {
                   SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                   try {

                       Date newDate = spf.parse(EXPDOWNPAYMENTDATE);
                       spf = new SimpleDateFormat("yyyy-MM-dd");
                       EXP_DOWN_PAYMENT_DATE = spf.format(newDate);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               String EXPINVOICEDATE = edt_expected_invoice_date.getText().toString();
               if (EXPINVOICEDATE.equals("") || EXPINVOICEDATE.isEmpty()) {
                   edt_expected_invoice_date.setError("Please Selecte Dob");
               } else {
                   SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                   try {

                       Date newDate = spf.parse(EXPINVOICEDATE);
                       spf = new SimpleDateFormat("yyyy-MM-dd");
                       EXP_INVOICE_DATE = spf.format(newDate);

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }

               if (VEHICLE_ALLOTED.equals("1")){
                   VEHICLE_NOT_ALLOTED_REASON = "";
               }else if (VEHICLE_ALLOTED.equals("0")){
                   VEHICLE_NOT_ALLOTED_REASON = "";
               }

               if (EX_SHOWROOM_PRICE.equals("")||TCS.equals("")||TCS_AMOUNT.equals("")||ROAD_TAX.equals("")
                       ||INSURANCE.equals("")||REGISTRATION.equals("")||ACCESSORIES.equals("")||WARRANTY.equals("")
                       ||FASTAG.equals("")||OFFERS.equals("")||NET_AMOUNT.equals("")||EXP_DOWN_PAYMENT.equals("")||
                       EXP_DOWN_PAYMENT_DATE.equals("")||EXP_INVOICE_DATE.equals("") || VEHICLE_ALLOTED.equals("") ){
                   Toast.makeText(BookingFollowUpActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();

               }else {
                   Update_Booking_Followup();
                   //initialize the progress dialog and show it
                   progressDialog = new ProgressDialog(BookingFollowUpActivity.this);
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

       }else {
           // userMessage = (String) jsonObj.get("userMessage");
           AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
           builder.setTitle("USER MESSAGE");
           builder.setMessage("Choose Vechical Allocated");
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

    }

    // Api call Customer_Getsources
    public void Booking_Followup_Info () {
        StringRequest requests = new StringRequest(Request.Method.POST, bookingfollowupinfo_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject =  jsonObj.getJSONObject("data");
                            if (jsonObject.isNull("paper_documents_collected")){
                               String paper_documents_collected = "";
                            }else {
                               String paper_documents_collected = jsonObject.getString("paper_documents_collected");
                                    txt_paper_documents_collected.setText(paper_documents_collected);
                                    txt_paper_documents_collected.setTextColor(Color.parseColor("#2ABB00"));
                                   // txt_paper_documents_collected.setText(paper_documents_collected);
                                   // txt_paper_documents_collected.setTextColor(Color.parseColor("#EB0A1E"));
                            }

                            if (jsonObject.isNull("login_completed")){

                            }else {
                               String login_completed = jsonObject.getString("login_completed");
                                    txt_login_completed.setText(login_completed);
                                    txt_login_completed.setTextColor(Color.parseColor("#2ABB00"));
                                    txt_login_completed.setText(login_completed);
                                    txt_login_date_time.setVisibility(View.GONE);
                                  //  txt_login_completed.setTextColor(Color.parseColor("#EB0A1E"));
                            }

                            if (jsonObject.isNull("login_completed_date")){
                              //  login_completed_date = "";
                            }else {
                               String login_completed_date = jsonObject.getString("login_completed_date");
                                txt_login_date_time.setText(login_completed_date);
                            }

                            if (jsonObject.isNull("query_clearance")){
                                //  login_completed_date = "";
                            }else {
                                String query_clearance = jsonObject.getString("query_clearance");
                                txt_query_clearance.setText(query_clearance);
                            }

                            if (jsonObject.isNull("loan_amount")){
                                //  login_completed_date = "";
                            }else {
                                String loan_amount = jsonObject.getString("loan_amount");
                                txt_loan_amount.setText(loan_amount);
                            }



                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                params.put("ENQUIRY_ID", "205");
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

    //Api call Get_Booking_Follow_Up_Details
    public void Get_Booking_Follow_Up_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, getbookingfollowupdetails_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("booking_followup_details");
                            BOOKINGFOLLOWUPDETAILSID = jsonObject.getString("BOOKING_FOLLOWUP_DETAILS_ID");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.BOOKING_FOLLOWUP_DETAILS_ID,BOOKINGFOLLOWUPDETAILSID);

                           if (jsonObject.isNull("VEHICLE_ALLOTTED")){

                           }else {
                               VEHICLEALLOTTED =  jsonObject.getString("VEHICLE_ALLOTTED").toString();
                               MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_ALLOTTED,VEHICLEALLOTTED);

                               if (VEHICLEALLOTTED.equals("0")){
                                   spinner_vechical.setSelection(Integer.parseInt("2"));
                               }else {
                                   spinner_vechical.setSelection(Integer.parseInt(VEHICLEALLOTTED));
                               }
                           }
                            VEHICLENOTALLOTTEDREASON = jsonObject.getString("VEHICLE_NOT_ALLOTTED_REASON");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_NOT_ALLOTTED_REASON,VEHICLENOTALLOTTEDREASON);
                            EXSHOWROOMPRICE =  jsonObject.getString("EX_SHOWROOM_PRICE");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EX_SHOWROOM_PRICE,EXSHOWROOMPRICE);
                            edt_ex_showroom_price.setText(EXSHOWROOMPRICE);
                            TCCS = jsonObject.getString("TCS");
                            edt_tcs.setText(TCCS);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS,TCCS);
                            TCSAMOUNT =  jsonObject.getString("TCS_AMOUNT");
                            edt_tcs_rate.setText(EXSHOWROOMPRICE);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS_AMOUNT,TCSAMOUNT);

                            ROADTAX = jsonObject.getString("ROAD_TAX");
                            edt_road_tax.setText(ROADTAX);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ROAD_TAX,ROADTAX);

                            INSURANCES =  jsonObject.getString("INSURANCE");
                            edt_insurance.setText(INSURANCES);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.INSURANCE,INSURANCES);

                            REGISTR = jsonObject.getString("REGISTRATION");
                            edt_registration.setText(REGISTR);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ROAD_TAX,REGISTR);

                            ACCESSORI =  jsonObject.getString("ACCESSORIES");
                            edt_accessories.setText(ACCESSORI);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ACCESSORIES,ACCESSORI);

                            WARRANT = jsonObject.getString("REGISTRATION");
                            edt_warranty.setText(WARRANT);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.WARRANTY,WARRANT);

                            FASTG =  jsonObject.getString("FASTAG");
                            edt_fastag.setText(FASTG);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FASTAGG,FASTG);

                            OFERS = jsonObject.getString("OFFERS");
                            edt_offers.setText(OFERS);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.OFFERS,OFERS);

                            NEAMOUNT =  jsonObject.getString("NET_AMOUNT");
                            txt_net_amount.setText(NEAMOUNT);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.NET_AMOUNT,NEAMOUNT);

                            EXPECTED_DOWNPAYMENT_AMOUNT = jsonObject.getString("EXPECTED_DOWNPAYMENT_AMOUNT");
                            edt_expected_downpayment_amount.setText(EXPECTED_DOWNPAYMENT_AMOUNT);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_AMOUNT,EXPECTED_DOWNPAYMENT_AMOUNT);

                            EXPECTED_DOWNPAYMENT_DATE =  jsonObject.getString("EXPECTED_DOWNPAYMENT_DATE");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_DATE,EXPECTED_DOWNPAYMENT_DATE);
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date newDate = spf.parse(EXPECTED_DOWNPAYMENT_DATE);
                                spf = new SimpleDateFormat("dd/MM/yyyy");
                                String followupdate = spf.format(newDate);
                                edt_cus_date.setText(followupdate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            EXPECTED_INVOICE_DATE = jsonObject.getString("EXPECTED_INVOICE_DATE");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_INVOICE_DATE,EXPECTED_INVOICE_DATE);
                            SimpleDateFormat spfs = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date newDate = spfs.parse(EXPECTED_INVOICE_DATE);
                                spfs = new SimpleDateFormat("dd/MM/yyyy");
                                String followupdate = spfs.format(newDate);
                                edt_expected_invoice_date.setText(followupdate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            PAPER_DOCUMENTS_COLLECTED =  jsonObject.getString("PAPER_DOCUMENTS_COLLECTED");
                            txt_paper_documents_collected.setText(PAPER_DOCUMENTS_COLLECTED);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.PAPER_DOCUMENTS_COLLECTED,PAPER_DOCUMENTS_COLLECTED);

                            LOGIN_COMPLETED = jsonObject.getString("LOGIN_COMPLETED");
                            txt_login_completed.setText(LOGIN_COMPLETED);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOGIN_COMPLETED,LOGIN_COMPLETED);

                            QUERY_CLEARANCE =  jsonObject.getString("QUERY_CLEARANCE");
                            txt_query_clearance.setText(QUERY_CLEARANCE);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.QUERY_CLEARANCE,QUERY_CLEARANCE);


                            LOAN_AMOUNT = jsonObject.getString("LOAN_AMOUNT");
                            txt_loan_amount.setText(LOAN_AMOUNT);
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOAN_AMOUNT,LOAN_AMOUNT);

                            FOLLOW_UP_DATE =  jsonObject.getString("FOLLOW_UP_DATE");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_DATE,FOLLOW_UP_DATE);

                            FOLLOW_UP_TIME = jsonObject.getString("FOLLOW_UP_TIME");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_TIME,FOLLOW_UP_TIME);

                            FOLLOW_UP_TYPE =  jsonObject.getString("FOLLOW_UP_TYPE");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_TYPE,FOLLOW_UP_TYPE);

                            FOLLOW_UP_REMARK =  jsonObject.getString("FOLLOW_UP_REMARK");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_REMARK,FOLLOW_UP_REMARK);;


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                params.put("ENQUIRY_ID", UPDATE_ENQUIRY_ID);//
                //params.put("PRE_DELIVERY_ID", "1");
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

    //Api call Booking_Followup
    public void Booking_Followup(){
        StringRequest requests = new StringRequest(Request.Method.POST, bookingfollowup_addbookingfollowup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("booking_followup_details");
                            contact_id = jsonObject.getString("enquiry_id");
                            contact_status = jsonObject.getString("status");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CONTACT_ID,contact_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_&_suffix");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            BOOKINGFOLLOWUPDETAILSID = jsonObject.getString("booking_followup_id");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.BOOKING_FOLLOWUP_DETAILS_ID,BOOKINGFOLLOWUPDETAILSID);

                            VEHICLEALLOTTED =  jsonObject.getString("vehicle_alloted");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_ALLOTTED,VEHICLEALLOTTED);

                            VEHICLENOTALLOTTEDREASON = jsonObject.getString("vehicle_not_alloted_reason");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_NOT_ALLOTTED_REASON,VEHICLENOTALLOTTEDREASON);

                            EXSHOWROOMPRICE =  jsonObject.getString("ex_showroom_price");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EX_SHOWROOM_PRICE,EXSHOWROOMPRICE);

                            TCCS = jsonObject.getString("tcs");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS,TCCS);
                            TCSAMOUNT =  jsonObject.getString("tcs_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS_AMOUNT,TCSAMOUNT);
                            ROADTAX = jsonObject.getString("road_tax");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ROAD_TAX,ROADTAX);
                            INSURANCES =  jsonObject.getString("insurance");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.INSURANCE,INSURANCES);
                            REGISTR = jsonObject.getString("registration");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.REGISTRATION,REGISTR);
                            ACCESSORI =  jsonObject.getString("accessories");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ACCESSORIES,ACCESSORI);
                            WARRANT = jsonObject.getString("warranty");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.WARRANTY,WARRANT);
                            FASTG =  jsonObject.getString("fastag");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FASTAGG,FASTG);
                            OFERS = jsonObject.getString("offers");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.OFFERS,OFERS);
                            NEAMOUNT =  jsonObject.getString("net_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.NET_AMOUNT,NEAMOUNT);
                            EXPECTED_DOWNPAYMENT_AMOUNT = jsonObject.getString("expected_downpayment_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_AMOUNT,EXPECTED_DOWNPAYMENT_AMOUNT);
                            EXPECTED_DOWNPAYMENT_DATE =  jsonObject.getString("expected_downpayment_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_DATE,EXPECTED_DOWNPAYMENT_DATE);
                            EXPECTED_INVOICE_DATE = jsonObject.getString("expected_invoice_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_INVOICE_DATE,EXPECTED_INVOICE_DATE);
                            PAPER_DOCUMENTS_COLLECTED =  jsonObject.getString("paper_documents_collected");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.PAPER_DOCUMENTS_COLLECTED,PAPER_DOCUMENTS_COLLECTED);
                            LOGIN_COMPLETED = jsonObject.getString("login_completed");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOGIN_COMPLETED,LOGIN_COMPLETED);
                            QUERY_CLEARANCE =  jsonObject.getString("query_clearance");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.QUERY_CLEARANCE,QUERY_CLEARANCE);
                            LOAN_AMOUNT = jsonObject.getString("loan_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOAN_AMOUNT,LOAN_AMOUNT);
                            FOLLOW_UP_DATE =  jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPDATE,FOLLOW_UP_DATE);
                            FOLLOW_UP_TIME = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTIME,FOLLOW_UP_TIME);
                            FOLLOW_UP_TYPE =  jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_TYPE,FOLLOW_UP_TYPE);
                           // FOLLOW_UP_REMARK =  jsonObject.getString("FOLLOW_UP_REMARK");
                           // MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_REMARK,FOLLOW_UP_REMARK);
                            startActivity(new Intent(BookingFollowUpActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                params.put("ENQUIRY_ID",UPDATE_ENQUIRY_ID);
                params.put("VEHICLE_ALLOTED", VEHICLE_ALLOTED);
                params.put("VEHICLE_NOT_ALLOTED_REASON", "VEHICLE_NOT_ALLOTED_REASON");
                params.put("EX_SHOWROOM_PRICE", EX_SHOWROOM_PRICE);
                params.put("TCS", TCS);
                params.put("TCS_AMOUNT", TCS_AMOUNT);
                params.put("ROAD_TAX", ROAD_TAX);
                params.put("INSURANCE", INSURANCE);
                params.put("REGISTRATION", REGISTRATION);
                params.put("ACCESSORIES", ACCESSORIES);
                params.put("WARRANTY", WARRANTY);
                params.put("FASTAG", FASTAG);
                params.put("OFFERS", OFFERS);
                params.put("NET_AMOUNT", NET_AMOUNT);
                params.put("EXP_DOWN_PAYMENT", EXP_DOWN_PAYMENT);
                params.put("EXP_DOWN_PAYMENT_DATE", EXP_DOWN_PAYMENT_DATE);//current_car_maker
                params.put("EXP_INVOICE_DATE", EXP_INVOICE_DATE);//current_car_model
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

    // Api call Customer_Getsources
    public void Update_Booking_Followup() {
        StringRequest requests = new StringRequest(Request.Method.POST, updatebookingfollowup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("booking_followup_details");
                            contact_id = jsonObject.getString("enquiry_id");
                            contact_status = jsonObject.getString("status");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CONTACT_ID,contact_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_&_suffix");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            BOOKINGFOLLOWUPDETAILSID = jsonObject.getString("booking_followup_id");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.BOOKING_FOLLOWUP_DETAILS_ID,BOOKINGFOLLOWUPDETAILSID);

                            VEHICLEALLOTTED =  jsonObject.getString("vehicle_alloted");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_ALLOTTED,VEHICLEALLOTTED);

                            VEHICLENOTALLOTTEDREASON = jsonObject.getString("vehicle_not_alloted_reason");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.VEHICLE_NOT_ALLOTTED_REASON,VEHICLENOTALLOTTEDREASON);

                            EXSHOWROOMPRICE =  jsonObject.getString("ex_showroom_price");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EX_SHOWROOM_PRICE,EXSHOWROOMPRICE);

                            TCCS = jsonObject.getString("tcs");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS,TCCS);
                            TCSAMOUNT =  jsonObject.getString("tcs_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.TCS_AMOUNT,TCSAMOUNT);
                            ROADTAX = jsonObject.getString("road_tax");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ROAD_TAX,ROADTAX);
                            INSURANCES =  jsonObject.getString("insurance");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.INSURANCE,INSURANCES);
                            REGISTR = jsonObject.getString("registration");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.REGISTRATION,REGISTR);
                            ACCESSORI =  jsonObject.getString("accessories");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.ACCESSORIES,ACCESSORI);
                            WARRANT = jsonObject.getString("warranty");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.WARRANTY,WARRANT);
                            FASTG =  jsonObject.getString("fastag");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FASTAGG,FASTG);
                            OFERS = jsonObject.getString("offers");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.OFFERS,OFERS);
                            NEAMOUNT =  jsonObject.getString("net_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.NET_AMOUNT,NEAMOUNT);
                            EXPECTED_DOWNPAYMENT_AMOUNT = jsonObject.getString("expected_downpayment_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_AMOUNT,EXPECTED_DOWNPAYMENT_AMOUNT);
                            EXPECTED_DOWNPAYMENT_DATE =  jsonObject.getString("expected_downpayment_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_DOWNPAYMENT_DATE,EXPECTED_DOWNPAYMENT_DATE);
                            EXPECTED_INVOICE_DATE = jsonObject.getString("expected_invoice_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.EXPECTED_INVOICE_DATE,EXPECTED_INVOICE_DATE);
                            PAPER_DOCUMENTS_COLLECTED =  jsonObject.getString("paper_documents_collected");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.PAPER_DOCUMENTS_COLLECTED,PAPER_DOCUMENTS_COLLECTED);
                            LOGIN_COMPLETED = jsonObject.getString("login_completed");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOGIN_COMPLETED,LOGIN_COMPLETED);
                            QUERY_CLEARANCE =  jsonObject.getString("query_clearance");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.QUERY_CLEARANCE,QUERY_CLEARANCE);
                            LOAN_AMOUNT = jsonObject.getString("loan_amount");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.LOAN_AMOUNT,LOAN_AMOUNT);
                            FOLLOW_UP_DATE =  jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPDATE,FOLLOW_UP_DATE);
                            FOLLOW_UP_TIME = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOWUPTIME,FOLLOW_UP_TIME);
                            FOLLOW_UP_TYPE =  jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_TYPE,FOLLOW_UP_TYPE);
                            // FOLLOW_UP_REMARK =  jsonObject.getString("FOLLOW_UP_REMARK");
                            // MyFunctions.setSharedPrefs(BookingFollowUpActivity.this,Constants.FOLLOW_UP_REMARK,FOLLOW_UP_REMARK);
                            startActivity(new Intent(BookingFollowUpActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                params.put("BOOKING_FOLLOWUP_ID", BOOKINGFOLLOWUPDETAILSID);
                params.put("VEHICLE_ALLOTED", VEHICLE_ALLOTED);
                params.put("VEHICLE_NOT_ALLOTED_REASON", "VEHICLE_NOT_ALLOTED_REASON");
                params.put("EX_SHOWROOM_PRICE", EX_SHOWROOM_PRICE);
                params.put("TCS", TCS);
                params.put("TCS_AMOUNT", TCS_AMOUNT);
                params.put("ROAD_TAX", ROAD_TAX);
                params.put("INSURANCE", INSURANCE);
                params.put("REGISTRATION", REGISTRATION);
                params.put("ACCESSORIES", ACCESSORIES);
                params.put("WARRANTY", WARRANTY);
                params.put("FASTAG", FASTAG);
                params.put("OFFERS", OFFERS);
                params.put("NET_AMOUNT", NET_AMOUNT);
                params.put("EXP_DOWN_PAYMENT", EXP_DOWN_PAYMENT);
                params.put("EXP_DOWN_PAYMENT_DATE", EXP_DOWN_PAYMENT_DATE);//current_car_maker
                params.put("EXP_INVOICE_DATE", EXP_INVOICE_DATE);//current_car_model
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

    // Api call Get_Booked_Vehicle_Price
    public void Get_Booked_Vehicle_Price(){
        StringRequest requests = new StringRequest(Request.Method.POST, bookingfollowup_vehicleprice_url, new Response.Listener<String>() {
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
                            String EX_SHOWROOM_PRICE = jsonObject.getString("EX_SHOWROOM_PRICE");
                            edt_ex_showroom_price.setText(EX_SHOWROOM_PRICE);
                            String TCS = jsonObject.getString("TCS");
                            edt_tcs.setText(TCS);
                            String TCS_AMOUNT = jsonObject.getString("TCS_AMOUNT");
                            edt_tcs_rate.setText(TCS_AMOUNT);
                            String ROAD_TAX = jsonObject.getString("ROAD_TAX");
                            edt_road_tax.setText(ROAD_TAX);
                            String INSURANCE = jsonObject.getString("INSURANCE");
                            edt_insurance.setText(INSURANCE);
                            String REGISTRATION = jsonObject.getString("REGISTRATION");
                            edt_registration.setText(REGISTRATION);
                            String ACCESSORIES = jsonObject.getString("ACCESSORIES");
                            edt_accessories.setText(ACCESSORIES);
                            String WARRANTY = jsonObject.getString("WARRANTY");
                            edt_warranty.setText(WARRANTY);
                            String FASTAG = jsonObject.getString("FASTAG");
                             edt_fastag.setText(FASTAG);
                            String OFFERS = jsonObject.getString("OFFERS");
                            edt_offers.setText(OFFERS);
                            String NET_AMOUNT = jsonObject.getString("NET_AMOUNT");
                            txt_net_amount.setText(NET_AMOUNT);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(BookingFollowUpActivity.this);
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
                //params.put("PRE_DELIVERY_ID", "1");
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
