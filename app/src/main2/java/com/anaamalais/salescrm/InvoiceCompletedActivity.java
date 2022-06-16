package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;
import static com.anaamalais.salescrm.Utils.Constants.INVOICE_COMPLETED_ID;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class InvoiceCompletedActivity extends AppCompatActivity {
    TextView edt_time;
    EditText edt_date;
    int mYear, mDay ,mMonth,mMinute,mHour;
    EditText edt_remark;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String invoice_completed_comments , invoicecompletedtime , invoicecompleteddate , scheduled_status;
    String  status_code , msg ,UPDATE_ENQUIRY_ID, API_TOKEN , contact_id , contact_status,
            COMMENTS, INVOICE_COMPLETED_DATE , INVOICE_COMPLETED_TIME , INVOICE_COMPLETED_COMMENTS;;
    String invoicecompleted_url = BASE_URL + "invoicecompleted/invoicecompleted";
    String get_invoice_completed_url = BASE_URL + "invoicecompleted/getinvoicecompleted";
    String updateinvoicecompletedurl = BASE_URL + "invoicecompleted/updateinvoicecompleted";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_completed);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = InvoiceCompletedActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(InvoiceCompletedActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(InvoiceCompletedActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(InvoiceCompletedActivity.this, Constants.TOKEN,"");
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        edt_remark = findViewById(R.id.edt_remark);
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
        scheduled_status = getIntent().getStringExtra("SCHEDULED_STATUS");
                //"EDIT NEW BOOKING";
                //
                //

        if (scheduled_status.equals("NEW SCHEDULED")){
           // txt_update_status.setVisibility(View.VISIBLE);
           // txt_edit_booking.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
            String strtime = spf.format(calendar.getTime());
            edt_time.setText(strtime);
            SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
            String strDates = dateformat.format(calendar.getTime());
            edt_date.setText(strDates);

        }else if (scheduled_status.equals("EDIT NEW BOOKING")){
           // txt_update_status.setVisibility(View.GONE);
            //txt_edit_booking.setVisibility(View.VISIBLE);
            Invoice_Get_Details();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(InvoiceCompletedActivity.this);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();


        }

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(InvoiceCompletedActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(InvoiceCompletedActivity.this,
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

    }

    public void back(View view) {
        startActivity(new Intent(InvoiceCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status","Invoice Completed"));
         finish();
    }

    public void updatestatus(View view) {

        if (scheduled_status.equals("NEW SCHEDULED")){
            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    INVOICE_COMPLETED_DATE = spf.format(newDate);

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
                    INVOICE_COMPLETED_TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            INVOICE_COMPLETED_COMMENTS = edt_remark.getText().toString();
            if (INVOICE_COMPLETED_COMMENTS.equals("")){
                edt_remark.setError("Fill the Remarks");
            }

            if (INVOICE_COMPLETED_COMMENTS.equals("")||INVOICE_COMPLETED_TIME.equals("")||INVOICE_COMPLETED_DATE.equals("")){
                Toast.makeText(InvoiceCompletedActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
            }else {
                Invoice_Completed();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(InvoiceCompletedActivity.this);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                anim.setDuration(15000);
                anim.setInterpolator(new DecelerateInterpolator());
                progressDialog.show();
            }
        }else if (scheduled_status.equals("EDIT NEW BOOKING")){
            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    INVOICE_COMPLETED_DATE = spf.format(newDate);

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
                    INVOICE_COMPLETED_TIME = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            INVOICE_COMPLETED_COMMENTS = edt_remark.getText().toString();
            if (INVOICE_COMPLETED_COMMENTS.equals("")){
                edt_remark.setError("Fill the Remarks");
            }

            if (INVOICE_COMPLETED_COMMENTS.equals("")||INVOICE_COMPLETED_TIME.equals("")||INVOICE_COMPLETED_DATE.equals("")){
                Toast.makeText(InvoiceCompletedActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
            }else {
                Update_Invoice_Completed();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(InvoiceCompletedActivity.this);
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


    // Api call Booking_Complete
    public void Invoice_Completed() {
        StringRequest requests = new StringRequest(Request.Method.POST, invoicecompleted_url, new Response.Listener<String>() {
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
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String invoice_completed_id = (String)jsonObject.get("invoice_completed_id").toString();
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_ID,invoice_completed_id);
                            String booking_date = jsonObject.getString("invoice_completed_date");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.BOOKINGDATE,booking_date);
                            String booking_time = jsonObject.getString("invoice_completed_time");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.BOOKINGTIME,booking_time);
                            String invoice_completed_comments = jsonObject.getString("invoice_completed_comments");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_COMMENTS,invoice_completed_comments);

                            startActivity(new Intent(InvoiceCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                params.put("INVOICE_COMPLETED_COMMENTS", INVOICE_COMPLETED_COMMENTS);
                params.put("INVOICE_COMPLETED_DATE", INVOICE_COMPLETED_DATE);
                params.put("INVOICE_COMPLETED_TIME",INVOICE_COMPLETED_TIME );
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


    //Api call Invoice_Get_Details

    public void Invoice_Get_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, get_invoice_completed_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject =  jsonObj.getJSONObject("data");

                            if (jsonObject.isNull("invoice_completed_date")){
                                invoicecompleteddate = "";
                            }else {
                                invoicecompleteddate = jsonObject.getString("invoice_completed_date");

                                SimpleDateFormat spf = new SimpleDateFormat("dd-mm-yyyy");
                                try {
                                    Date newDate = spf.parse(invoicecompleteddate);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    String followupdate = spf.format(newDate);
                                    edt_date.setText(followupdate);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            if (jsonObject.isNull("invoice_completed_time")){
                                invoicecompletedtime = "";
                            }else {
                                invoicecompletedtime = jsonObject.getString("invoice_completed_time");
                                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss a");
                                try {
                                    Date newDate = spf.parse(invoicecompletedtime);
                                    spf = new SimpleDateFormat("HH:mm");
                                    String followuptime = spf.format(newDate);
                                    edt_time.setText(followuptime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            INVOICE_COMPLETED_ID= (String)jsonObject.get("invoice_completed_id").toString();
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_ID,INVOICE_COMPLETED_ID);

                            invoice_completed_comments = jsonObject.getString("invoice_completed_comments");
                            edt_remark.setText(invoice_completed_comments);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                params.put("INVOICE_COMPLETED_ID",MyFunctions.getSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_ID,""));
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


    //Api call Invoice_Get_Details

    public void Update_Invoice_Completed() {
        StringRequest requests = new StringRequest(Request.Method.POST, updateinvoicecompletedurl, new Response.Listener<String>() {
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
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id  = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.STATUS_ID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String invoice_completed_id = (String)jsonObject.get("invoice_completed_id").toString();
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_ID,invoice_completed_id);
                            String booking_date = jsonObject.getString("invoice_completed_date");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.BOOKINGDATE,booking_date);
                            String booking_time = jsonObject.getString("invoice_completed_time");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.BOOKINGTIME,booking_time);
                            String invoice_completed_comments = jsonObject.getString("invoice_completed_comments");
                            MyFunctions.setSharedPrefs(InvoiceCompletedActivity.this,Constants.INVOICE_COMPLETED_COMMENTS,invoice_completed_comments);

                            startActivity(new Intent(InvoiceCompletedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceCompletedActivity.this);
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
                params.put("INVOICE_COMPLETED_ID", INVOICE_COMPLETED_ID);
                params.put("INVOICE_COMPLETED_COMMENTS", INVOICE_COMPLETED_COMMENTS);
                params.put("INVOICE_COMPLETED_DATE", INVOICE_COMPLETED_DATE);
                params.put("INVOICE_COMPLETED_TIME",INVOICE_COMPLETED_TIME );
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
