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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anaamalais.salescrm.Adapter.EnquiryTitleTypeArrayAdapter;
import com.anaamalais.salescrm.List.EnquiryTitle;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class AddFollowUpTaskActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String  status_code , msg ,token, API_TOKEN,UPDATE_CONTACT_ID,contact_id,contact_status;
    EditText edt_remark , edt_date;
    TextView edt_time , txt_telephone , txtdDirectvisit , txt_add_follow_up_task;
    int mYear, mDay ,mMonth,mMinute,mHour;
    Boolean isCallPressed = false, isDirectPlace = false;
    String follow_up_type , TODAY_REMARK , FOLLOW_UP_DATE , FOLLOW_UP_TIME;
    String enquiry_complete_by_follow_up_date = BASE_URL + "enquiry/completefollowup";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_follow_up_task);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = AddFollowUpTaskActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(AddFollowUpTaskActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(AddFollowUpTaskActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(AddFollowUpTaskActivity.this, Constants.TOKEN, "");
        edt_remark = findViewById(R.id.edt_remark);
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        txt_add_follow_up_task = findViewById(R.id.txt_add_follow_up_task);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        edt_date.setText(strDates);
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
        String strtime = spf.format(calendar.getTime());
        edt_time.setText(strtime);

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                try{
                    c.set(Calendar.DATE,Integer.parseInt(String.valueOf(edt_date.getText())));
                } catch(NumberFormatException ex){ // handle your exception

                }
                c.set(Calendar.HOUR_OF_DAY,mHour);
                c.set(Calendar.MINUTE,mMinute);


                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddFollowUpTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if (hourOfDay >= mHour) {
                                    edt_time.setText(hourOfDay + ":" + minute);
                                } else {
                                    edt_time.setText("Invalid Time");
                                }
                            }


                        },mHour, mMinute, false);

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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddFollowUpTaskActivity.this,
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
                        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
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


        if (!isCallPressed || !isDirectPlace){
            follow_up_type = "1";
        }


        txt_add_follow_up_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!isDirectPlace && !isCallPressed){
                   Toast.makeText(AddFollowUpTaskActivity.this, "Selected Follow up Type", Toast.LENGTH_SHORT).show();
               }else {
                   TODAY_REMARK = edt_remark.getText().toString().trim();
                   if (TODAY_REMARK.equals("")||TODAY_REMARK.isEmpty()){
                       edt_remark.setError("Please Fill the Remark");
                       TODAY_REMARK = "";
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

                   String  followuptime = edt_time.getText().toString().trim()+":"+"00";
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



                   if (TODAY_REMARK.equals("")||FOLLOW_UP_DATE.equals("")||FOLLOW_UP_TIME.equals("")||follow_up_type.equals("")){
                       Toast.makeText(AddFollowUpTaskActivity.this, "Fill all the details", Toast.LENGTH_SHORT).show();
                   }else {
                       Enquiry_Complete_By_Follow_Up_Date();
                       //initialize the progress dialog and show it
                       progressDialog = new ProgressDialog(AddFollowUpTaskActivity.this);
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
        });
    }

    public void back(View view) {
        startActivity(new Intent(AddFollowUpTaskActivity.this,HomeActivity.class));
        finish();
    }

    public void Enquiry_Complete_By_Follow_Up_Date(){

        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_complete_by_follow_up_date, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                                JSONObject jsonObject = jsonObj.getJSONObject("followup_details");
                            contact_id = jsonObject.getString("contact_id");
                            MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("contact_status");
                                String cus_name = (String)jsonObject.get("name");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.CUSTOMER_NAME,cus_name);
                                String cus_phone = (String)jsonObject.get("phone").toString();
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                                String cus_address = (String)jsonObject.get("address");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                                String followupdate = jsonObject.getString("follow_up_date");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.FOLLOWUPDATE,followupdate);
                                String followuptime = jsonObject.getString("follow_up_time");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.FOLLOWUPTIME,followuptime);
                                String followuptype = jsonObject.getString("follow_up_type");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                                String followupid = jsonObject.getString("follow_up_id");
                                MyFunctions.setSharedPrefs(AddFollowUpTaskActivity.this,Constants.FOLLOWUPID,followupid);
                                startActivity(new Intent(AddFollowUpTaskActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                                finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFollowUpTaskActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddFollowUpTaskActivity.this);
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
                params.put("CONTACT_ID", MyFunctions.getSharedPrefs(AddFollowUpTaskActivity.this,Constants.CONTACT_ID,""));
                params.put("FOLLOWUP_ID", MyFunctions.getSharedPrefs(AddFollowUpTaskActivity.this,Constants.FOLLOWUPID,""));
                params.put("TODAY_REMARK", TODAY_REMARK);
                params.put("NEXT_FOLLOWUP_ADDED", "1");
                params.put("FOLLOW_UP_DATE", FOLLOW_UP_DATE);
                params.put("FOLLOW_UP_TIME", FOLLOW_UP_TIME);
                params.put("FOLLOW_UP_TYPE", follow_up_type);
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
