package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotInterestedContactActivity extends AppCompatActivity {
    LinearLayout lin_individual , lin_corporate;
    ImageView img_personal_down , img_personal_up;
    RelativeLayout lin_personal_details;
    TextInputEditText edt_cus_title , edt_cus_model,edt_cus_source , edt_cus_name , edt_cus_one_number ,
            edt_cus_two_number , edt_cus_email , edt_cus_profession , edt_cus_sub_occupation , edt_address_line_one , edt_address_line_two
            ,edt_address_line_three , edt_cus_annual_income , edt_cus_cop_title , edt_cus_cop_model, edt_cus_cop_source , edt_cop_cus_name ,
            edt_cus_cop_company_type , edt_cop_contact_person_name, edt_cop_designation ,
            edt_cop_mobile_number , edt_cop_sec_mobile_number , edt_cop_address_address, edt_cop_address_line_two , edt_cop_address_line_three , edt_cop_annual_Income ;
    EditText edt_cus_date , edt_non_interested;
    TextView txt_personal;
    Animation animation;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String  status_code , msg ,token, API_TOKEN,UPDATE_CONTACT_ID,non_interested_remark;
    String customer_getcontact = BASE_URL + "customer/getcontact";
    String customer_notintrested = BASE_URL + "customer/notintrested";
    String statusid , contactid , modeid , sourceid , titleid , customername , contactnumberone,
            upcontactnumberone , contactnumbertwo , getemail , getdob ,professionid , suboccupation
            , getaddress , annualincomeid , companyname , companytypeid , contactpersonname , getmobilenumber
            , getadditionalmobilenumber , getdesignation , intrestedvehicleid , modesuffixid
   , mobilenumber , designation , dob , contact_status ,  reason , getquantity,prospect_category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_interested_contact);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = NotInterestedContactActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(NotInterestedContactActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(NotInterestedContactActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(NotInterestedContactActivity.this, Constants.TOKEN,"");
        lin_individual = findViewById(R.id.lin_individual);
        lin_corporate = findViewById(R.id.lin_corporate);
        img_personal_down = findViewById(R.id.img_personal_down);
        img_personal_up = findViewById(R.id.img_personal_up);
        lin_personal_details = findViewById(R.id.lin_personal_details);
        edt_cus_title = findViewById(R.id.edt_cus_title);
        edt_cus_model = findViewById(R.id.edt_cus_model);
        edt_cus_source = findViewById(R.id.edt_cus_source);
        edt_cus_name = findViewById(R.id.edt_cus_name);
        edt_cus_one_number = findViewById(R.id.edt_cus_one_number);
        edt_cus_two_number = findViewById(R.id.edt_cus_two_number);
        edt_cus_email = findViewById(R.id.edt_cus_email);
        edt_cus_date = findViewById(R.id.edt_cus_date);
        edt_cus_profession = findViewById(R.id.edt_cus_profession);
        edt_cus_sub_occupation  = findViewById(R.id.edt_cus_sub_occupation);
        edt_address_line_one = findViewById(R.id.edt_address_line_one);
        edt_address_line_two = findViewById(R.id.edt_address_line_two);
        edt_address_line_three = findViewById(R.id.edt_address_line_three);
        edt_cus_annual_income = findViewById(R.id.edt_cus_annual_income);
        edt_cus_cop_title = findViewById(R.id.edt_cus_cop_title);
        edt_cus_cop_model = findViewById(R.id.edt_cus_cop_model);
        edt_cop_cus_name = findViewById(R.id.edt_cop_cus_name);
        edt_cus_cop_source = findViewById(R.id.edt_cus_cop_source);
        edt_cus_cop_company_type = findViewById(R.id.edt_cus_cop_company_type);
        edt_cop_contact_person_name = findViewById(R.id.edt_cop_contact_person_name);
        edt_cop_designation = findViewById(R.id.edt_cop_designation);
        edt_cop_mobile_number = findViewById(R.id.edt_cop_mobile_number);
        edt_cop_sec_mobile_number = findViewById(R.id.edt_cop_sec_mobile_number);
        edt_cop_address_address = findViewById(R.id.edt_cop_address_address);
        edt_cop_address_line_two = findViewById(R.id.edt_cop_address_line_two);
        edt_cop_address_line_three = findViewById(R.id.edt_cop_address_line_three);
        edt_cop_annual_Income = findViewById(R.id.edt_cop_annual_Income);
        txt_personal = findViewById(R.id.txt_personal);
        edt_non_interested = findViewById(R.id.edt_non_interested);
        UPDATE_CONTACT_ID = getIntent().getStringExtra("CONTACTID");

        lin_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_personal_down.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.GONE);
                    img_personal_up.setVisibility(View.VISIBLE);
                    lin_individual.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_individual.startAnimation(animation);
                    Customer_Get_Contact();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(NotInterestedContactActivity.this);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                    ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                    anim.setDuration(15000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    progressDialog.show();
                }else if (img_personal_up.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.VISIBLE);
                    img_personal_up.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_individual.startAnimation(animation);
                }
            }
        });



    }

    public void back(View view) {
        startActivity(new Intent(NotInterestedContactActivity.this,CustomerDetailsActivity.class).putExtra("Status","Not Interested"));
         finish();
    }

    public void updatestatus(View view) {
        non_interested_remark = edt_non_interested.getText().toString().trim();
        if (non_interested_remark.equals("")||non_interested_remark.isEmpty()|| (non_interested_remark.toString().trim().length() < 5 || non_interested_remark.toString().trim().length() > 320)){
            edt_non_interested.setError("Remarks should not be four or less than four characters");
        }else {
            Customer_Not_Intrested();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(NotInterestedContactActivity.this);
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


    // Api call Customer_Get_Contact
    public void Customer_Get_Contact(){
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                      progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonArray_titles = jsonObj.getJSONObject("contact");

                            JSONObject jsonObject = jsonArray_titles.getJSONObject("personal_details");

                            if (jsonObject.isNull("STATUS_ID")){
                                statusid = "";
                            }else {
                                statusid = jsonObject.getString("STATUS_ID");
                            }

                            if (jsonObject.isNull("CONTACT_ID")){
                                contactid = "";
                            }else {
                                contactid = jsonObject.getString("CONTACT_ID");
                            }

                            if (jsonObject.isNull("PROSPECT_CATEGORY")){
                                prospect_category = "";
                            }else {
                                prospect_category = jsonObject.getString("PROSPECT_CATEGORY");
                                if (prospect_category.equals("1")){
                                    txt_personal.setText("Personal Details");
                                    lin_individual.setVisibility(View.VISIBLE);
                                    lin_corporate.setVisibility(View.GONE);
                                }else if (prospect_category.equals("2")){
                                    txt_personal.setText("Company Details");
                                    lin_individual.setVisibility(View.GONE);
                                    lin_corporate.setVisibility(View.VISIBLE);
                                }
                            }

                            if (jsonObject.isNull("MODE_NAME")){
                                modeid = "";
                            }else {
                                modeid = jsonObject.getString("MODE_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_model.setText(modeid);
                                }else if (prospect_category.equals("2")){
                                    edt_cus_cop_model.setText(modeid);
                                }

                            }

                            if (jsonObject.isNull("SOURCE_NAME")){
                                sourceid = "";
                            }else {
                                sourceid = jsonObject.getString("SOURCE_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_source.setText(sourceid);
                                }else if (prospect_category.equals("2")){
                                    edt_cus_cop_source.setText(sourceid);
                                }

                            }

                            if (jsonObject.isNull("TITLE_NAME")){
                                titleid = "";
                            }else {
                                titleid = jsonObject.getString("TITLE_NAME");
                              //  MyFunctions.setSharedPrefs(NotInterestedContactActivity.this,Constants.TITLEID,titleid);
                                if (prospect_category.equals("1")){
                                    edt_cus_title.setText(titleid);
                                }else if (prospect_category.equals("2")){
                                    edt_cus_cop_title.setText(titleid);
                                }
                            }
                            customername = jsonObject.getString("CUSTOMER_NAME");
                            edt_cus_name.setText(customername);

                            contactnumberone = jsonObject.getString("CONTACT_NUMBER_1");

                            edt_cus_one_number.setText(contactnumberone);

                            if (jsonObject.isNull("CONTACT_NUMBER_2")){
                                contactnumbertwo = "";
                            }else {
                                contactnumbertwo = jsonObject.getString("CONTACT_NUMBER_2");
                                edt_cus_two_number.setText(contactnumbertwo);
                            }

                            if (jsonObject.isNull("EMAIL")){
                                getemail = "";
                            }else {
                                getemail = jsonObject.getString("EMAIL");
                                edt_cus_email.setText(getemail);
                            }

                            if (jsonObject.isNull("DOB")){
                                getdob = "";
                            }else {
                                getdob = jsonObject.getString("DOB");

                                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date newDate = spf.parse(getdob);
                                    spf = new SimpleDateFormat("dd/MM/yyyy");
                                    dob = spf.format(newDate);
                                    edt_cus_date.setText(dob);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (jsonObject.isNull("PROFESSION_NAME")){
                                professionid = "";
                            }else {
                                professionid = jsonObject.getString("PROFESSION_NAME");
                                edt_cus_profession.setText(professionid);
                               // spinner_profession.setSelection(Integer.parseInt(professionid));
                            }

                            if (jsonObject.isNull("SUB_OCCUPATION")){
                                suboccupation = "";
                            }else {
                                suboccupation = jsonObject.getString("SUB_OCCUPATION");
                                edt_cus_sub_occupation.setText(suboccupation);

                            }

                            if (jsonObject.isNull("ADDRESS")){
                                getaddress = "";
                            }else {
                                if (prospect_category.equals("1")){
                                    getaddress = jsonObject.getString("ADDRESS");
                                    String a[]=getaddress.split("\t");
                                    String addressone = a[0];
                                    edt_address_line_one.setText(addressone);
                                    String addresstwo = a[1];
                                    edt_address_line_two.setText(addresstwo);
                                    String addressthree = a[2];
                                    edt_address_line_three.setText(addressthree);
                                }else if (prospect_category.equals("2")){
                                    getaddress = jsonObject.getString("ADDRESS");
                                    String a[]=getaddress.split("\t");
                                    String addressone = a[0];
                                    edt_cop_address_address.setText(addressone);
                                    String addresstwo = a[1];
                                    edt_cop_address_line_two.setText(addresstwo);
                                    String addressthree = a[2];
                                    edt_cop_address_line_three.setText(addressthree);
                                }

                            }

                            if (jsonObject.isNull("ANNUAL_INCOME_NAME")){
                                annualincomeid = "";
                            }else {
                                annualincomeid = jsonObject.getString("ANNUAL_INCOME_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_annual_income.setText(annualincomeid);
                                }else if (prospect_category.equals("2")){
                                    edt_cop_annual_Income.setText(annualincomeid);
                                }

                            }

                            if (jsonObject.isNull("COMPANY_NAME")){
                                companyname = "";
                            }else {
                                companyname = jsonObject.getString("COMPANY_NAME");
                                edt_cop_cus_name.setText(companyname);
                            }

                            if (jsonObject.isNull("COMPANY_TYPE_NAME")){
                                companytypeid = "";
                            }else {
                                companytypeid = jsonObject.getString("COMPANY_TYPE_NAME");
                                edt_cus_cop_company_type.setText(companytypeid);
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_NAME")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_NAME");
                                edt_cop_contact_person_name.setText(contactpersonname);
                            }

                            if (jsonObject.isNull("MOBILE_NUMBER")){
                                mobilenumber = "";
                            }else {
                                mobilenumber = jsonObject.getString("MOBILE_NUMBER");
                                edt_cop_mobile_number.setText(mobilenumber);
                            }

                            if (jsonObject.isNull("ADDITIONAL_MOBILE_NUMBER")){
                                getadditionalmobilenumber = "";
                            }else {
                                getadditionalmobilenumber = jsonObject.getString("ADDITIONAL_MOBILE_NUMBER");
                                edt_cop_sec_mobile_number.setText(getadditionalmobilenumber);
                            }

                            if (jsonObject.isNull("DESIGNATION")){
                                designation = "";
                            }else {
                                getdesignation = jsonObject.getString("DESIGNATION");
                                edt_cop_designation.setText(getdesignation);
                            }

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotInterestedContactActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    //exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotInterestedContactActivity.this);
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
                params.put("CONTACT_ID", UPDATE_CONTACT_ID);
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

    public void Customer_Not_Intrested(){

        StringRequest requests = new StringRequest(Request.Method.POST, customer_notintrested, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                        //JSONObject jsonArray_titles = jsonObj.getJSONObject("contact");
                       // JSONObject jsonObject = jsonArray_titles.getJSONObject("personal_details");
                        contact_status = jsonObj.getString("contact_status");
                        reason = jsonObj.getString("reason");
                        startActivity(new Intent(NotInterestedContactActivity.this,CustomerDetailsActivity.class)
                                .putExtra("Status",contact_status).putExtra("REASON",reason));
                        finish();


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotInterestedContactActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    //exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotInterestedContactActivity.this);
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
                params.put("CONTACT_ID", UPDATE_CONTACT_ID);
                params.put("REASON", non_interested_remark);
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
