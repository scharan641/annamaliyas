package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.material.textfield.TextInputEditText;

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

public class CompleteDetailsActivity extends AppCompatActivity {
    TextView txt_individual , txt_corporate,edt_time , txt_first , txt_addition ,txt_replace,txt_plus ,txt_minus,
            txt_count, txt_add_contact , txt_telephone , txtdDirectvisit,txt_title,txt_update_enquiry;

    TextInputEditText edt_cus_name , edt_cus_one_number , edt_cus_two_number , edt_cus_email ,
            edt_cus_sub_occupation , edt_address_line_one , edt_address_line_two , edt_address_line_three
            ,edt_cop_cus_name,edt_cop_contact_person_name,edt_cop_designation,edt_cop_mobile_number,
            edt_cop_address_address,edt_cop_address_line_two,edt_cop_address_line_three,edt_cop_sec_mobile_number
            ,edt_current_car_name,edt_model,edt_year_month,edt_req_number,edt_replace_current_car_name,
            edt_replace_model,edt_replace_year_month,edt_replace_req_number,edt_replace_expected_value
    ,edt_cus_models  ,edt_insurance_pinch_text,edt_finance_pinch_text,edt_cop_address_address_cus,edt_cus_annual_income;

    TextInputEditText edt_cus_contact_one , edt_cus_cop_email_one ,edt_cop_address ,
            edt_cop_address_lines_two , edt_cop_address_lines_three ,edt_cop_mobiles_number ,
            edt_cus_cop_email_two , edt_cop_address_line_two_cus,edt_cus_quantity
            ,edt_cus_title , edt_cus_model,edt_cus_source,edt_cus_profession,
            edt_fuel_types,edt_fuel_type,edt_mode_suffix_name,edt_cus_interior_color_name,edt_cus_exterior_color_name
    ,edt_cus_cop_company_email , edt_cus_cop_model ,edt_cus_cop_title ,edt_cus_cop_source, edt_cop_annual_Income,  edt_insurance_pinch_text_spinner , edt_finance_pinch_text_spinner;

    LinearLayout lin_individual , lin_corporate,lin_demand_structure,lin_intrested_vehicle,lin_value_chain,lin_follow_up
            ,lin_additional , lin_replace , lin_finance_pinch , lin_insurance_pinch;
    RelativeLayout rel_personal_details ,rel_cop_details_title, rel_demand_structure , rel_intrested_vehicle , rel_value_chain ,rel_next_follow_up;
    ImageView img_personal_downs , img_personal_up ,Img_personal_details_cop_up,Img_personal_details_cop_down,
            img_demand_structure_down , img_demand_structure_up , img_intrested_vehicle_down,img_intrested_vehicle_up,img_value_chain_down ,img_value_chain_up;
    SwitchCompat swt_next_follow_up;
    EditText edt_date,edt_cus_date,edt_remark;
    SwitchCompat swt_address_proof_attached,swt_intrested_in_utrust,swt_product_demo,swt_brouchure_given,swt_quotation_given,swt_extended_warranty,swt_smiles,
            swt_accessories_pitch , swt_notify_customer;
    String dob ,designation , additionalmobilenumber , mobilenumber , address;
    String  status_code , msg ,token, UPDATE_ENQUIRY_ID, API_TOKEN , finance_other , insurance_other,follow_up_type;
    String title_id , enquiry_id , mode_id , profession_id ,source_id , annual_income_id , company_type_id , vehicle_id
            , interior_color_id , exterior_color_id,contact_status,contact_id,finance_pitch_id,insurance_master_id;
    String demand_structure_type,current_car_maker,current_car_model,year_month,current_car_reg_number,current_car_expected_value;

    String enquirytype , statusid , contactid , modeid , sourceid , titleid , customername , contactnumberone,
            upcontactnumberone , contactnumbertwo , getemail , getdob ,professionid , suboccupation
            , getaddress , annualincomeid , companyname , companytypeid , contactpersonname , getmobilenumber
            , getadditionalmobilenumber , getdesignation , intrestedvehicleid , modesuffixid
            , interiorcolorid ,  exteriorcolorid , getquantity ,  type , current_car_make , models , fuel_type ,
            make_year , registration_number ,expected_value , prospect_category ,utrust_intrested;

    String productdemogiven , brochuregiven , quotationgiven , extended_warrantys , smiless
            , accessoriespitch , notifycustomer , financepitch , insurancemasterid , financeother
            , insuranceothers , date , time , types , remark;
    Animation animation;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String enquiry_getenquiry = BASE_URL + "enquiry/getenquiry";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = CompleteDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(CompleteDetailsActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(CompleteDetailsActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(CompleteDetailsActivity.this, Constants.TOKEN,"");
        txt_individual = findViewById(R.id.txt_individual);
        txt_corporate  = findViewById(R.id.txt_corporate);
        lin_individual = findViewById(R.id.lin_individual);
        lin_corporate = findViewById(R.id.lin_corporate);
        lin_demand_structure = findViewById(R.id.lin_demand_structure);
        lin_intrested_vehicle  = findViewById(R.id.lin_intrested_vehicle);
        lin_value_chain = findViewById(R.id.lin_value_chain);
        lin_follow_up = findViewById(R.id.lin_follow_up);
        rel_personal_details = findViewById(R.id.rel_personal_details);
        rel_demand_structure = findViewById(R.id.rel_demand_structure);
        rel_intrested_vehicle  = findViewById(R.id.rel_intrested_vehicle);
        rel_next_follow_up = findViewById(R.id.rel_next_follow_up);
        rel_value_chain = findViewById(R.id.rel_value_chain);
        img_personal_downs = findViewById(R.id.img_personal_downs);
        img_personal_up = findViewById(R.id.img_personal_ups);
        img_demand_structure_down  = findViewById(R.id.img_demand_structure_down);
        img_demand_structure_up = findViewById(R.id.img_demand_structure_up);
        img_intrested_vehicle_down = findViewById(R.id.img_intrested_vehicle_down);
        img_intrested_vehicle_up = findViewById(R.id.img_intrested_vehicle_up);
        img_value_chain_down = findViewById(R.id.img_value_chain_down);
        img_value_chain_up = findViewById(R.id.img_value_chain_up);
        swt_next_follow_up = findViewById(R.id.swt_next_follow_up);
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        txt_first = findViewById(R.id.txt_first);
        txt_addition = findViewById(R.id.txt_addition);
        txt_replace = findViewById(R.id.txt_replace);
        lin_additional = findViewById(R.id.lin_additional);
        lin_replace = findViewById(R.id.lin_replace);
        txt_minus = findViewById(R.id.txt_minus);
        txt_plus = findViewById(R.id.txt_plus);
        txt_count = findViewById(R.id.txt_count);
        edt_remark = findViewById(R.id.edt_remark);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        txt_update_enquiry = findViewById(R.id.txt_update_enquiry);
        lin_finance_pinch = findViewById(R.id.lin_finance_pinch);
        lin_insurance_pinch = findViewById(R.id.lin_insurance_pinch);
        Img_personal_details_cop_up = findViewById(R.id.img_cop_up);
        Img_personal_details_cop_down = findViewById(R.id.img_cop_down);
        //intivideual edittext
        edt_cus_name = findViewById(R.id.edt_cus_name);
        edt_cus_one_number = findViewById(R.id.edt_cus_one_number);
        edt_cus_two_number = findViewById(R.id.edt_cus_two_number);
        edt_cus_email = findViewById(R.id.edt_cus_email);
        edt_cus_sub_occupation = findViewById(R.id.edt_cus_sub_occupation);
        edt_cus_date = findViewById(R.id.edt_cus_date);
        edt_address_line_one = findViewById(R.id.edt_address_line_one);
        edt_address_line_two = findViewById(R.id.edt_address_line_two);
        edt_address_line_three = findViewById(R.id.edt_address_line_three);
        edt_finance_pinch_text = findViewById(R.id.edt_finance_pinch_text);
        edt_insurance_pinch_text = findViewById(R.id.edt_insurance_pinch_text);
        edt_cus_title = findViewById(R.id.edt_cus_title);
        edt_cus_models = findViewById(R.id.edt_cus_models);
      //  edt_cus_source =  findViewById(R.id.edt_cus_source);
        edt_cus_profession  =  findViewById(R.id.edt_cus_profession);
        edt_fuel_types  =  findViewById(R.id.edt_fuel_types);
        edt_insurance_pinch_text_spinner = findViewById(R.id.edt_insurance_pinch_text_spinner);
        edt_finance_pinch_text_spinner = findViewById(R.id.edt_finance_pinch_text_spinner);
        edt_cop_annual_Income = findViewById(R.id.edt_cop_annual_Income);
        edt_cus_cop_model = findViewById(R.id.edt_cus_cop_model);
        edt_cus_source = findViewById(R.id.edt_cus_source);
        edt_cus_cop_title = findViewById(R.id.edt_cus_cop_title);
        edt_cus_cop_source = findViewById(R.id.edt_cus_cop_source);
        Calendar calendar = Calendar.getInstance();

        //coper tetx
        edt_cop_cus_name = findViewById(R.id.edt_cop_cus_name);
        edt_cop_mobile_number = findViewById(R.id.edt_cop_mobile_number);
        edt_cus_cop_email_one = findViewById(R.id.edt_cus_cop_email_one);
        edt_cop_address = findViewById(R.id.edt_cop_address);
        edt_cop_address_lines_two  = findViewById(R.id.edt_cop_address_lines_two);
        edt_cop_address_lines_three  = findViewById(R.id.edt_cop_address_lines_three);
        edt_cus_contact_one = findViewById(R.id.edt_cop_mobiles_number);
        edt_cus_cop_email_two = findViewById(R.id.edt_cus_cop_email_two);
        edt_cop_address_address_cus = findViewById(R.id.edt_cop_address_address);
        edt_cop_address_line_two_cus =  findViewById(R.id.edt_cop_address_line_two);
        edt_cop_address_lines_three =  findViewById(R.id.edt_cop_address_line_three);
        edt_cop_contact_person_name = findViewById(R.id.edt_cop_contact_person_name);
        edt_cop_designation = findViewById(R.id.edt_cop_designation);
        edt_cus_quantity = findViewById(R.id.edt_cus_quantity);
        edt_current_car_name = findViewById(R.id.edt_current_car_name);
        edt_model = findViewById(R.id.edt_model);
        edt_year_month = findViewById(R.id.edt_year_month);
        edt_req_number = findViewById(R.id.edt_req_number);
        edt_fuel_type  = findViewById(R.id.edt_fuel_type);
        edt_mode_suffix_name = findViewById(R.id.edt_mode_suffix_name);
        edt_cus_interior_color_name = findViewById(R.id.edt_cus_interior_color_name);
        edt_cus_exterior_color_name = findViewById(R.id.edt_cus_exterior_color_name);
        edt_replace_current_car_name = findViewById(R.id.edt_replace_current_car_name);
        edt_replace_model = findViewById(R.id.edt_replace_model);
        edt_replace_year_month = findViewById(R.id.edt_replace_year_month);
        edt_replace_req_number = findViewById(R.id.edt_replace_req_number);
        edt_replace_expected_value = findViewById(R.id.edt_replace_expected_value);
        edt_cus_annual_income = findViewById(R.id.edt_cus_annual_income);
        edt_cus_cop_company_email  = findViewById(R.id.edt_cus_cop_company_email);
        //switch
        swt_address_proof_attached = findViewById(R.id.swt_address_proof_attached);
        swt_intrested_in_utrust = findViewById(R.id.swt_intrested_in_utrust);
        swt_product_demo = findViewById(R.id.swt_product_demo);
        swt_brouchure_given = findViewById(R.id.swt_brouchure_given);
        swt_quotation_given = findViewById(R.id.swt_quotation_given);
        swt_extended_warranty = findViewById(R.id.swt_extended_warranty);
        swt_smiles = findViewById(R.id.swt_smiles);
        swt_accessories_pitch = findViewById(R.id.swt_accessories_pitch);
        swt_notify_customer = findViewById(R.id.swt_notify_customer);
        rel_cop_details_title = findViewById(R.id.rel_cop_details_title);
        lin_finance_pinch = findViewById(R.id.lin_finance_pinch);
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");

        rel_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_personal_downs.getVisibility()==View.VISIBLE){
                    img_personal_downs.setVisibility(View.GONE);
                    img_personal_up.setVisibility(View.VISIBLE);
                    rel_personal_details.setBackgroundResource(R.color.intersted_color);
                    lin_individual.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_individual.startAnimation(animation);

                }else if (img_personal_up.getVisibility()==View.VISIBLE){
                    img_personal_downs.setVisibility(View.VISIBLE);
                    img_personal_up.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    rel_personal_details.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_individual.startAnimation(animation);
                }
            }
        });


        rel_cop_details_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img_personal_details_cop_down.getVisibility()==View.VISIBLE){
                    Img_personal_details_cop_down.setVisibility(View.GONE);
                    Img_personal_details_cop_up.setVisibility(View.VISIBLE);
                    lin_corporate.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                }else if (Img_personal_details_cop_up.getVisibility()==View.VISIBLE){
                    Img_personal_details_cop_down.setVisibility(View.VISIBLE);
                    Img_personal_details_cop_up.setVisibility(View.GONE);
                    lin_corporate.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_corporate.startAnimation(animation);
                }
            }
        });


        rel_demand_structure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_demand_structure_down.getVisibility()==View.VISIBLE){
                    img_demand_structure_down.setVisibility(View.GONE);
                    img_demand_structure_up.setVisibility(View.VISIBLE);
                    rel_demand_structure.setBackgroundResource(R.color.intersted_color);
                    lin_demand_structure.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_demand_structure.startAnimation(animation);

                }else if (img_demand_structure_up.getVisibility()==View.VISIBLE){
                    img_demand_structure_down.setVisibility(View.VISIBLE);
                    img_demand_structure_up.setVisibility(View.GONE);
                    rel_demand_structure.setBackgroundResource(0);
                    lin_demand_structure.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_demand_structure.startAnimation(animation);
                }
            }
        });


        rel_intrested_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_intrested_vehicle_down.getVisibility()==View.VISIBLE){
                    img_intrested_vehicle_down.setVisibility(View.GONE);
                    img_intrested_vehicle_up.setVisibility(View.VISIBLE);
                    rel_intrested_vehicle.setBackgroundResource(R.color.intersted_color);
                    lin_intrested_vehicle.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                }else if (img_intrested_vehicle_up.getVisibility()==View.VISIBLE){
                    img_intrested_vehicle_down.setVisibility(View.VISIBLE);
                    img_intrested_vehicle_up.setVisibility(View.GONE);
                    rel_intrested_vehicle.setBackgroundResource(0);
                    lin_intrested_vehicle.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_intrested_vehicle.startAnimation(animation);
                }
            }
        });


        rel_value_chain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_value_chain_down.getVisibility()==View.VISIBLE){
                    img_value_chain_down.setVisibility(View.GONE);
                    img_value_chain_up.setVisibility(View.VISIBLE);
                    rel_value_chain.setBackgroundResource(R.color.intersted_color);
                    lin_value_chain.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_value_chain.startAnimation(animation);

                }else if (img_value_chain_up.getVisibility()==View.VISIBLE){
                    img_value_chain_down.setVisibility(View.VISIBLE);
                    img_value_chain_up.setVisibility(View.GONE);
                    rel_value_chain.setBackgroundResource(0);
                    lin_value_chain.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_value_chain.startAnimation(animation);
                }
            }
        });

        swt_next_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    swt_next_follow_up.setChecked(true);
                }
            }
        });

        Customer_Get_Enquiry();
    }

    public void back(View view) {
        startActivity(new Intent(CompleteDetailsActivity.this,HomeActivity.class).putExtra("Status","BACK"));
         finish();
    }




    // Api call Customer_Get_Contact
    public void Customer_Get_Enquiry(){
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_getenquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonArray_titles = jsonObj.getJSONObject("enquiry_details");

                            JSONObject jsonObject = jsonArray_titles.getJSONObject("personal_details");

                            if (jsonObject.isNull("PROSPECT_CATEGORY")){
                                prospect_category = "";
                            }else {
                                prospect_category = jsonObject.getString("PROSPECT_CATEGORY");
                                if (prospect_category.equals("1")){
                                    txt_individual.setBackgroundResource(R.drawable.btn_line);
                                    txt_individual.setTextColor(Color.parseColor("#EB0A1E"));
                                    lin_corporate.setVisibility(View.GONE);
                                    // lin_individual.setVisibility(View.VISIBLE);
                                    rel_cop_details_title.setVisibility(View.GONE);
                                    rel_personal_details.setVisibility(View.VISIBLE);
                                    prospect_category = "1";
                                }else if(prospect_category.equals("2")){
                                    lin_individual.setVisibility(View.GONE);
                                    rel_personal_details.setVisibility(View.GONE);
                                    txt_corporate.setBackgroundResource(R.drawable.btn_line);
                                    txt_corporate.setTextColor(Color.parseColor("#EB0A1E"));
                                    rel_cop_details_title.setVisibility(View.VISIBLE);
                                }
                            }

                            if (jsonObject.isNull("ENQUIRY_TYPE")){
                                enquirytype = "";
                            }else {
                                enquirytype = jsonObject.getString("ENQUIRY_TYPE");

                            }

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

                            if (jsonObject.isNull("MODE_NAME")){
                                modeid = "";
                            }else {
                                modeid = jsonObject.getString("MODE_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_models.setText(modeid);
                                }else if(prospect_category.equals("2")){
                                    edt_cus_cop_model.setText(modeid);
                                }

                            }

                            if (jsonObject.isNull("SOURCE_NAME")){
                                edt_cus_source.setText("Not Selected");
                            }else {
                                sourceid = jsonObject.getString("SOURCE_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_source.setText(sourceid);
                                }else if(prospect_category.equals("2")){
                                    edt_cus_cop_source.setText(sourceid);
                                }

                            }

                            if (jsonObject.isNull("TITLE_NAME")){
                                edt_cus_title.setText("Not Selected");
                            }else {
                                titleid = jsonObject.getString("TITLE_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_title.setText(titleid);
                                }else if(prospect_category.equals("2")){
                                    edt_cus_cop_title.setText(titleid);
                                }

                            }
                            customername = jsonObject.getString("CUSTOMER_NAME");
                            edt_cus_name.setText(customername);
                            contactnumberone = jsonObject.getString("CONTACT_NUMBER_1");
                            edt_cus_one_number.setText(contactnumberone);
                            if (jsonObject.isNull("CONTACT_NUMBER_2")){
                                edt_cus_two_number.setText("Not Selected");
                            }else {
                                contactnumbertwo = jsonObject.getString("CONTACT_NUMBER_2");
                                edt_cus_two_number.setText(contactnumbertwo);
                            }

                            if (jsonObject.isNull("EMAIL")){
                                getemail = "";
                            }else {
                                getemail = jsonObject.getString("EMAIL");
                                if (prospect_category.equals("1")){
                                    edt_cus_email.setText(getemail);
                                }else if (prospect_category.equals("2")){
                                    getemail = jsonObject.getString("EMAIL");
                                    edt_cus_cop_company_email.setText(getemail);
                                }

                            }

                            if (jsonObject.isNull("DOB")){
                                edt_cus_date.setText("Not Selected");
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
                                if (prospect_category.equals("1")){
                                    edt_cus_profession.setText(professionid);
                                }else if(prospect_category.equals("2")){
                                    // spinner_cop.setSelection(Integer.parseInt(modeid));
                                }


                            }

                            if (jsonObject.isNull("SUB_OCCUPATION")){
                                edt_cus_sub_occupation.setText("Not Selected");
                            }else {
                                suboccupation = jsonObject.getString("SUB_OCCUPATION");
                                edt_cus_sub_occupation.setText(suboccupation);

                            }

                            if (jsonObject.isNull("ADDRESS")){
                                address = "";
                            }else {

                                if (prospect_category.equals("1")){
                                    address = jsonObject.getString("ADDRESS");
                                    String a[]=address.split("\t");
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
                                    edt_cop_address_address_cus.setText(addressone);
                                    String addresstwo = a[1];
                                    edt_cop_address_line_two_cus.setText(addresstwo);
                                    String addressthree = a[2];
                                    edt_cop_address_lines_three.setText(addressthree);
                                }
                            }

                            if (jsonObject.isNull("ANNUAL_INCOME_ID")){
                                edt_cus_annual_income.setText("Not Selected");
                            }else {
                                annualincomeid = jsonObject.getString("ANNAUL_INCOME_NAME");
                                if (prospect_category.equals("1")){
                                    edt_cus_annual_income.setText(annualincomeid);
                                }else if(prospect_category.equals("2")){
                                    edt_cop_annual_Income.setText(annualincomeid);
                                }

                            }

                            if (jsonObject.isNull("COMPANY_NAME")){
                                companyname = "";
                            }else {
                                companyname = jsonObject.getString("COMPANY_NAME");
                                edt_cop_cus_name.setText(companyname);
                            }

                            if (jsonObject.isNull("COMPANY_TYPE_ID")){
                                companytypeid = "";
                            }else {
                                companytypeid = jsonObject.getString("COMPANY_TYPE_ID");

                            }

                            if (jsonObject.isNull("CONTACT_PERSON_NAME")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_NAME");
                                edt_cop_contact_person_name.setText("contactpersonname");
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_EMAIL")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_EMAIL");
                                edt_cop_contact_person_name.setText("contactpersonname");
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_ADDRESS")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_EMAIL");
                                edt_cop_contact_person_name.setText("CONTACT_PERSON_ADDRESS");
                            }

                            if (jsonObject.isNull("MOBILE_NUMBER")){
                                mobilenumber = "";
                            }else {
                                mobilenumber = jsonObject.getString("MOBILE_NUMBER");
                                edt_cop_mobile_number.setText(mobilenumber);
                            }

                            if (jsonObject.isNull("ADDITIONAL_MOBILE_NUMBER")){
                                additionalmobilenumber = "";
                            }else {
                                additionalmobilenumber = jsonObject.getString("ADDITIONAL_MOBILE_NUMBER");
//                                edt_cop_sec_mobile_number.setText(additionalmobilenumber);
                            }

                            if (jsonObject.isNull("DESIGNATION")){
                                getdesignation = "";
                            }else {
                                getdesignation = jsonObject.getString("DESIGNATION");
                                edt_cop_designation.setText(designation);
                            }


                            JSONObject demand_and_structure = jsonArray_titles.getJSONObject("demand_and_structure");

                            if (demand_and_structure.isNull("TYPE")){
                                type = "";
                            }else {
                                type = demand_and_structure.getString("TYPE");
                                if (type.equals("1")){
                                    txt_first.setBackgroundResource(R.drawable.btn_line);
                                    txt_first.setTextColor(Color.parseColor("#EB0A1E"));
                                    demand_structure_type = "1";
                                    lin_additional.setVisibility(View.GONE);
                                    lin_replace.setVisibility(View.GONE);
                                }else if (type.equals("2")){
                                    txt_addition.setBackgroundResource(R.drawable.btn_line);
                                    txt_addition.setTextColor(Color.parseColor("#EB0A1E"));
                                    lin_additional.setVisibility(View.VISIBLE);
                                    lin_replace.setVisibility(View.GONE);
                                    demand_structure_type = "2";

                                }else if (type.equals("3")){
                                    txt_first.setBackgroundResource(0);
                                    txt_first.setTextColor(Color.parseColor("#58595B"));
                                    txt_replace.setBackgroundResource(R.drawable.btn_line);
                                    txt_replace.setTextColor(Color.parseColor("#EB0A1E"));
                                    lin_replace.setVisibility(View.VISIBLE);
                                    lin_additional.setVisibility(View.GONE);
                                    demand_structure_type = "3";

                                }
                            }

                            if (demand_and_structure.isNull("CURRENT_CAR_MAKE")){
                                current_car_make = "";
                            }else {
                                current_car_make = demand_and_structure.getString("CURRENT_CAR_MAKE");
                                if (type.equals("2")){
                                    edt_current_car_name.setText(current_car_make);
                                }else if (type.equals("3")){
                                    edt_replace_current_car_name.setText(registration_number);
                                }
                            }

                            if (demand_and_structure.isNull("MODEL")){
                                models = "";
                            }else {
                                models = demand_and_structure.getString("MODEL");
                                if (type.equals("2")){
                                    edt_model.setText(models);
                                }else if (type.equals("3")){
                                    edt_replace_model.setText(registration_number);
                                }
                            }

                            if (demand_and_structure.isNull("FUEL_TYPE_NAME")){
                                fuel_type = "";
                            }else {
                                fuel_type = demand_and_structure.getString("FUEL_TYPE_NAME");
                                if (type.equals("2")){
                                    edt_fuel_types.setText(fuel_type);
                                }else {

                                }

                            }

                            if (demand_and_structure.isNull("MAKE_YEAR")){
                                make_year = "";
                            }else {
                                make_year = demand_and_structure.getString("MAKE_YEAR");
                                if (type.equals("2")){
                                    edt_year_month.setText(make_year);
                                }else if (type.equals("3")){
                                    edt_replace_year_month.setText(make_year);
                                }
                            }

                            if (demand_and_structure.isNull("REGISTRATION_NUMBER")){
                                registration_number = "";
                            }else {
                                registration_number = demand_and_structure.getString("REGISTRATION_NUMBER");
                                if (type.equals("2")){
                                    edt_req_number.setText(registration_number);
                                }else if (type.equals("3")){
                                    edt_replace_req_number.setText(registration_number);

                                }
                            }

                            if (demand_and_structure.isNull("EXPECTED_VALUE")){
                                expected_value= "";
                            }else {
                                expected_value = demand_and_structure.getString("EXPECTED_VALUE");
                                if (type.equals("3")){
                                    edt_replace_expected_value.setText(expected_value);
                                }
                            }

                            if (demand_and_structure.isNull("UTRUST_INTRESTED")){
                                utrust_intrested= "";
                            }else {
                                utrust_intrested = demand_and_structure.getString("UTRUST_INTRESTED");
                                if (type.equals("3")&&utrust_intrested.equals("1")){
                                    swt_intrested_in_utrust.setChecked(true);
                                }else {
                                    swt_intrested_in_utrust.setChecked(false);
                                }
                            }

                            JSONObject intrested_vehicle = jsonArray_titles.getJSONObject("intrested_toyota_vehicle");

                            if (intrested_vehicle.isNull("VEHICLE_NAME")){
                                intrestedvehicleid = "";
                            }else {
                                intrestedvehicleid = intrested_vehicle.getString("VEHICLE_NAME");
                                edt_mode_suffix_name.setText(intrestedvehicleid);
                            }

                            if (intrested_vehicle.isNull("VEHICLE_INTERIOR_COLOR_NAME")){
                                edt_cus_interior_color_name.setText("Color not selected");
                            }else {
                                interiorcolorid = intrested_vehicle.getString("VEHICLE_INTERIOR_COLOR_NAME");
                                edt_cus_interior_color_name.setText(interiorcolorid);
                            }

                            if (intrested_vehicle.isNull("VEHICLE_EXTERIOR_COLOR_NAME")){
                                edt_cus_exterior_color_name.setText("Color not selected");
                            }else {
                                exteriorcolorid = intrested_vehicle.getString("VEHICLE_EXTERIOR_COLOR_NAME");
                                edt_cus_exterior_color_name.setText(exteriorcolorid);
                            }

                            if (intrested_vehicle.isNull("QTY")){
                                getquantity = "";
                            }else {
                                getquantity = intrested_vehicle.getString("QTY");
                                edt_cus_quantity.setText(getquantity);
                                edt_cus_quantity.setText(getquantity);
                            }

                            JSONObject value_chain = jsonArray_titles.getJSONObject("value_chain");

                            if (value_chain.isNull("PRODUCT_DEMO_GIVEN")){
                                productdemogiven = "";
                            }else {
                                productdemogiven = value_chain.getString("PRODUCT_DEMO_GIVEN");
                                if (prospect_category.equals("1")){
                                    if (productdemogiven.equals("0")){
                                        swt_product_demo.setChecked(false);
                                    }else if(productdemogiven.equals("1")){
                                        swt_product_demo.setChecked(true);
                                    }if (prospect_category.equals("2")){
                                        if (productdemogiven.equals("0")){
                                            swt_product_demo.setChecked(false);
                                        }else if(productdemogiven.equals("1")){
                                            swt_product_demo.setChecked(true);
                                        }
                                    }
                                }
                            }

                            if (value_chain.isNull("BROCHURE_GIVEN")){
                                brochuregiven = "";
                            }else {
                                brochuregiven = value_chain.getString("BROCHURE_GIVEN");
                                if (brochuregiven.equals("0")){
                                    swt_brouchure_given.setChecked(false);
                                }else if(brochuregiven.equals("1")){
                                    swt_brouchure_given.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("QUOTATION_GIVEN")){
                                quotationgiven = "";
                            }else {
                                quotationgiven = value_chain.getString("QUOTATION_GIVEN");
                                if (quotationgiven.equals("0")){
                                    swt_quotation_given.setChecked(false);
                                }else if(quotationgiven.equals("1")){
                                    swt_quotation_given.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("EXTENDED_WARRANTY")){
                                extended_warrantys = "";
                            }else {
                                extended_warrantys = value_chain.getString("EXTENDED_WARRANTY");
                                if (extended_warrantys.equals("0")){
                                    swt_extended_warranty.setChecked(false);
                                }else if(extended_warrantys.equals("1")){
                                    swt_extended_warranty.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("SMILES")){
                                smiless = "";
                            }else {
                                smiless = value_chain.getString("SMILES");
                                if (smiless.equals("0")){
                                    swt_smiles.setChecked(false);
                                }else if(smiless.equals("1")){
                                    swt_smiles.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("ACCESSORIES_PITCH")){
                                accessoriespitch = "";
                            }else {
                                accessoriespitch = value_chain.getString("ACCESSORIES_PITCH");
                                if (accessoriespitch.equals("0")){
                                    swt_accessories_pitch.setChecked(false);
                                }else if(accessoriespitch.equals("1")){
                                    swt_accessories_pitch.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("NOTIFY_CUSTOMER")){
                                notifycustomer = "";
                            }else {
                                notifycustomer = value_chain.getString("NOTIFY_CUSTOMER");
                                if (notifycustomer.equals("0")){
                                    swt_notify_customer.setChecked(false);
                                }else if(notifycustomer.equals("1")){
                                    swt_notify_customer.setChecked(true);
                                }
                            }

                            if (value_chain.isNull("FINANCE_OTHERS")){
                                financeother = "";
                            }else {
                                financeother = value_chain.getString("FINANCE_OTHERS");
                                edt_finance_pinch_text.setText(financeother);
                            }

                            if (value_chain.isNull("INSURANCE_OTHERS")){
                                insuranceothers = "";
                            }else {
                                insuranceothers = value_chain.getString("INSURANCE_OTHERS");
                                edt_insurance_pinch_text.setText(insuranceothers);
                            }

                            if (value_chain.isNull("FINANCE_PITCH")){
                                financepitch = "";
                            }else {
                                financepitch = value_chain.getString("FINANCE_PITCH");
                                if (financepitch.equals("2")){
                                    lin_finance_pinch.setVisibility(View.VISIBLE);
                                    edt_finance_pinch_text.setText(financeother);
                                    finance_other = edt_finance_pinch_text.getText().toString();
                                }else {
                                    lin_finance_pinch.setVisibility(View.GONE);
                                    finance_other = "";
                                }
                            }

                            if (value_chain.isNull("INSURANCE_MASTER_ID")){
                                insurancemasterid = "";
                            }else {
                                insurancemasterid = value_chain.getString("INSURANCE_MASTER_ID");
                                if (insurancemasterid.equals("2")){
                                    lin_insurance_pinch.setVisibility(View.VISIBLE);
                                    edt_insurance_pinch_text.setText(insuranceothers);
                                    //  insurance_other = edt_insurance_pinch_text.getText().toString();
                                    //edt_insurance_pinch_text.getText().toString();
                                }else {
                                    lin_insurance_pinch.setVisibility(View.GONE);
                                    insurance_other = "";
                                }
                            }

                            if (value_chain.isNull("FINANCE_PITCH_NAME")){
                                edt_finance_pinch_text_spinner.setText("Not Selected");
                            }else {
                                String FINANCE_PITCH_NAME = value_chain.getString("FINANCE_PITCH_NAME");
                                edt_finance_pinch_text_spinner.setText(FINANCE_PITCH_NAME);
                            }

                            if (value_chain.isNull("INSURANCE_MASTER_NAME")){
                                edt_insurance_pinch_text_spinner.setText("Not Selected");
                            }else {
                                String INSURANCE_MASTER_NAME = value_chain.getString("INSURANCE_MASTER_NAME");
                                edt_insurance_pinch_text_spinner.setText(INSURANCE_MASTER_NAME);
                            }




                            JSONObject next_followup = jsonArray_titles.getJSONObject("next_followup");
                            if (next_followup.isNull("DATE")){
                                date = "";
                            }else {
                                date = next_followup.getString("DATE");

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

                            if (next_followup.isNull("TIME")){
                                time = "";
                            }else {
                                time = next_followup.getString("TIME");
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

                            if (next_followup.isNull("TYPE")){
                                types = "";
                            }else {
                                types = next_followup.getString("TYPE");
                                if (type.equals("1")){
                                    follow_up_type = "1";
                                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));

                                }else if (type.equals("2")){
                                    follow_up_type = "2";
                                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));

                                    }
                                }
                            if (next_followup.isNull("REMARKS")){
                                edt_remark.setText("No Remarks");
                            }else {
                                remark = next_followup.getString("REMARKS");
                                edt_remark.setText(remark);
                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteDetailsActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompleteDetailsActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CompleteDetailsActivity.this,HomeActivity.class).putExtra("Status","BACK"));
        finish();
    }
}
