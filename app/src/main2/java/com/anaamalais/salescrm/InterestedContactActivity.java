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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.anaamalais.salescrm.Adapter.FinancePitchTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.InsurancePitchTypeArrayAdapter;
import com.anaamalais.salescrm.List.FinancePitchList;
import com.anaamalais.salescrm.List.InsuranceList;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InterestedContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Boolean isCallPressed = false, isDirectPlace = false;
    TextView edt_time,txt_telephone,txtdDirectvisit,txt_personal;
    TextView txt_individual , txt_corporate , txt_first , txt_addition ,txt_replace,txt_plus ,txt_minus,
            txt_count, txt_add_contact ,txt_title,txt_update_enquiry;
    TextInputEditText edt_cus_title , edt_cus_model,edt_cus_source , edt_cus_name , edt_cus_one_number ,
            edt_cus_two_number , edt_cus_email , edt_cus_profession , edt_cus_sub_occupation , edt_address_line_one , edt_address_line_two
            ,edt_address_line_three , edt_cus_annual_income , edt_cus_cop_title , edt_cus_cop_model, edt_cus_cop_source , edt_cop_cus_name ,
            edt_cus_cop_company_type , edt_cop_contact_person_name, edt_cop_designation ,
    dt_current_car_name,edt_model,edt_year_month,edt_req_number,edt_replace_current_car_name,
    edt_replace_model,edt_replace_year_month,edt_replace_req_number,edt_replace_expected_value,
            edt_cop_mobile_number , edt_cop_sec_mobile_number , edt_cop_address_address, edt_cop_address_line_two
            , edt_cop_address_line_three , edt_cop_annual_Income ,edt_mode_suffix_name ,edt_variant_suffix_name, edt_cus_interior_color_name
   , edt_current_car_name   , edt_cus_cop_company_email , edt_cus_exterior_color_name , edt_cus_quantity,edt_finance_pinch_text,edt_insurance_pinch_text;
    String customer_details_status , thisYear , UPDATE_ENQUIRY_ID , demand_structure_type,followuptime;
    String current_car_fuel_type , finance_pitch_id , finance_other , insurance_master_id , insurance_other;
    SwitchCompat swt_follow_up;
    LinearLayout lin_follow_up , lin_corporate,lin_intrested_vehicle , lin_value_chain
            ,lin_finance_pinch , lin_insurance_pinch;
    EditText edt_date , edt_cus_date , edt_remark;
    int mYear, mDay ,mMonth,mMinute,mHour;
    String interiorcolorid , exteriorcolorid;
    Boolean isOnePressed = true, isSecondPlace = false;
    Boolean isfirstPressed = true, isadditionPlace = false , isreplacePlace = false;
    LinearLayout lin_individual , lin_demand_structure , lin_additional , lin_replace ;
    RelativeLayout rel_personal_details , rel_cop_details_title  , rel_value_chain;
    ImageView img_personal_down , img_personal_up , img_demand_structure_down , img_demand_structure_up ,
            img_intrested_vehicle_down , img_intrested_vehicle_up , img_value_chain_down , img_value_chain_up;
    RelativeLayout lin_personal_details,rel_demand_structure,rel_intrested_vehicle;
    String statusid , contactid , modeid , sourceid , titleid , customername , contactnumberone,
            upcontactnumberone , contactnumbertwo , getemail , getdob ,professionid , suboccupation
            , getaddress , annualincomeid , companyname , companytypeid , contactpersonname , getmobilenumber
            , getadditionalmobilenumber , getdesignation , intrestedvehicleid ,   MODEL_VARIANT_NAME , MODEL_VARIANT_ID , modesuffixid
            , mobilenumber , designation , dob , contact_status ,  reason , getquantity,
    follow_up_type ,  prospect_category , MODE_SUFFIX_NAME , INTERIOR_COLOR_NAME , EXTERIOR_COLOR_NAME , QUANTITY;

    Spinner spinner_fuel_type,spinner_replace_fuel_type,spinner_finance_pitch,spinner_insurance_pitch
            ;

    SwitchCompat swt_address_proof_attached , swt_intrested_in_utrust,swt_product_demo,swt_brouchure_given,swt_quotation_given,swt_extended_warranty,swt_smiles,
            swt_accessories_pitch , swt_notify_customer;

    String customer_name , contact_number_one , contact_number_two , sub_occupation ,
            address , email , company_name , contact_person_name ,additionalmobilenumber
            ,follow_up_date,follow_up_time,follow_up_remark;

    String current_car_maker,current_car_model,year_month,current_car_reg_number,current_car_expected_value;

    String address_proof_attached_status , intrested_in_utrust_status , product_demo_given , brochure_given , quotation_given , extended_warranty , smiles
            , accessories_pitch , notify_customer ;

    String title_id , enquiry_id , mode_id , profession_id ,source_id , annual_income_id , company_type_id , vehicle_id
            , interior_color_id , exterior_color_id,contact_id;


    List<FinancePitchList>financePitchLists;
    List<InsuranceList>insuranceLists;

    Animation animation;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String  status_code , msg ,token, API_TOKEN;
    String customer_getcontact = BASE_URL + "customer/getcontact";
    String enquiry_getfinancepitch_url = BASE_URL + "enquiry/getfinancepitch";
    String enquiry_getinsurancepitch_url = BASE_URL + "enquiry/getinsurancepitch";
    String enquiry_getenquiry = BASE_URL + "enquiry/getenquiry";
    String enquiry_updateenquiry = BASE_URL + "enquiry/updateenquiry";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_contact);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = InterestedContactActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(InterestedContactActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(InterestedContactActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(InterestedContactActivity.this, Constants.TOKEN,"");
        lin_individual = findViewById(R.id.lin_individual);
        lin_demand_structure = findViewById(R.id.lin_demand_structure);
        lin_follow_up = findViewById(R.id.lin_follow_up);
        swt_follow_up = findViewById(R.id.swt_next_follow_up);
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
        String strtime = spf.format(calendar.getTime());
        edt_time.setText(strtime);
        edt_date.setText(strDates);
        txt_add_contact = findViewById(R.id.txt_update_status);
        lin_finance_pinch = findViewById(R.id.lin_finance_pinch);
        lin_insurance_pinch = findViewById(R.id.lin_insurance_pinch);

        rel_value_chain  = findViewById(R.id.rel_value_chain);

        img_personal_down = findViewById(R.id.img_personal_downs);
        img_personal_up = findViewById(R.id.img_personal_ups);

        img_demand_structure_down = findViewById(R.id.img_demand_structure_down);
        img_demand_structure_up = findViewById(R.id.img_demand_structure_up);

        img_intrested_vehicle_down = findViewById(R.id.img_intrested_vehicle_down);
        img_intrested_vehicle_up = findViewById(R.id.img_intrested_vehicle_up);

        img_value_chain_down = findViewById(R.id.img_value_chain_down);
        img_value_chain_up = findViewById(R.id.img_value_chain_up);

        spinner_finance_pitch = (Spinner) findViewById(R.id.spinner_finance_pitch);
        spinner_insurance_pitch = (Spinner) findViewById(R.id.spinner_insurance_pitch);

        lin_personal_details = findViewById(R.id.lin_personal_details);
        rel_demand_structure = findViewById(R.id.rel_demand_structure);
        lin_intrested_vehicle = findViewById(R.id.lin_intrested_vehicle);
        rel_cop_details_title = findViewById(R.id.rel_cop_details_title);
        txt_replace = findViewById(R.id.txt_replace);
        lin_additional = findViewById(R.id.lin_additional);
        lin_value_chain = findViewById(R.id.lin_value_chain);
        spinner_fuel_type = (Spinner) findViewById(R.id.spinner_fuel_type);
        spinner_replace_fuel_type = (Spinner) findViewById(R.id.spinner_replace_fuel_type);

        swt_address_proof_attached = findViewById(R.id.swt_address_proof_attached);
        swt_intrested_in_utrust = findViewById(R.id.swt_intrested_in_utrust);
        swt_product_demo = findViewById(R.id.swt_product_demo);
        swt_brouchure_given = findViewById(R.id.swt_brouchure_given);
        swt_quotation_given = findViewById(R.id.swt_quotation_given);
        swt_extended_warranty = findViewById(R.id.swt_extended_warranty);
        swt_smiles = findViewById(R.id.swt_smiles);
        swt_accessories_pitch = findViewById(R.id.swt_accessories_pitch);
        swt_notify_customer = findViewById(R.id.swt_notify_customer);

        edt_remark = findViewById(R.id.edt_remark);

        edt_current_car_name = findViewById(R.id.edt_current_car_name);
        edt_model = findViewById(R.id.edt_model);
        edt_year_month = findViewById(R.id.edt_year_month);
        edt_req_number = findViewById(R.id.edt_req_number);

        edt_replace_current_car_name = findViewById(R.id.edt_replace_current_car_name);
        edt_replace_model = findViewById(R.id.edt_replace_model);
        edt_replace_year_month = findViewById(R.id.edt_replace_year_month);
        edt_replace_req_number = findViewById(R.id.edt_replace_req_number);
        edt_replace_expected_value = findViewById(R.id.edt_replace_expected_value);

        edt_replace_current_car_name = findViewById(R.id.edt_replace_current_car_name);
        edt_replace_model = findViewById(R.id.edt_replace_model);
        edt_replace_year_month = findViewById(R.id.edt_replace_year_month);
        edt_replace_req_number = findViewById(R.id.edt_replace_req_number);
        edt_replace_expected_value = findViewById(R.id.edt_replace_expected_value);
        edt_cus_cop_company_email = findViewById(R.id.edt_cus_cop_company_email);
        rel_personal_details = findViewById(R.id.rel_personal_details);
        rel_intrested_vehicle = findViewById(R.id.rel_intrested_vehicle);
        lin_individual = findViewById(R.id.lin_individual);
        lin_corporate = findViewById(R.id.lin_corporate);
        img_personal_down = findViewById(R.id.img_personal_downs);
        img_personal_up = findViewById(R.id.img_personal_ups);
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
        edt_mode_suffix_name = findViewById(R.id.edt_mode_suffix_name);
        edt_variant_suffix_name = findViewById(R.id.edt_variant_suffix_name);
        edt_cus_interior_color_name = findViewById(R.id.edt_cus_interior_color_name);
        edt_cus_exterior_color_name = findViewById(R.id.edt_cus_exterior_color_name);
        edt_cus_quantity = findViewById(R.id.edt_cus_quantity);
        txt_personal = findViewById(R.id.txt_personal);
        txt_first = findViewById(R.id.txt_first);
        txt_addition = findViewById(R.id.txt_addition);
        txt_replace = findViewById(R.id.txt_replace);
        lin_additional = findViewById(R.id.lin_additional);
        lin_replace = findViewById(R.id.lin_replace);
        edt_finance_pinch_text = findViewById(R.id.edt_finance_pinch_text);
        edt_insurance_pinch_text = findViewById(R.id.edt_insurance_pinch_text);
        //UPDATE_CONTACT_ID = getIntent().getStringExtra("CONTACTID");

        financePitchLists = new ArrayList<>();
        insuranceLists = new ArrayList<>();

        if (isfirstPressed=true){
            demand_structure_type = "1";
        }

        rel_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_personal_down.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.GONE);
                    img_personal_up.setVisibility(View.VISIBLE);
                    lin_individual.setVisibility(View.VISIBLE);
                    rel_personal_details.setBackgroundResource(R.color.intersted_color);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    lin_individual.startAnimation(animation);
                    Customer_Get_Contact();
                    //initialize the progress dialog and show it
                    /*progressDialog = new ProgressDialog(InterestedContactActivity.this);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                    ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                    anim.setDuration(15000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    progressDialog.show();*/
                }else if (img_personal_up.getVisibility()==View.VISIBLE){
                    img_personal_down.setVisibility(View.VISIBLE);
                    img_personal_up.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    lin_corporate.setVisibility(View.GONE);
                    rel_personal_details.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    lin_individual.startAnimation(animation);
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
                    rel_intrested_vehicle.startAnimation(animation);
                    Customer_Get_Contact();
                }else if (img_intrested_vehicle_up.getVisibility()==View.VISIBLE){
                    img_intrested_vehicle_down.setVisibility(View.VISIBLE);
                    img_intrested_vehicle_up.setVisibility(View.GONE);
                    rel_intrested_vehicle.setBackgroundResource(0);
                    lin_intrested_vehicle.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_intrested_vehicle.startAnimation(animation);
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
                  /*  nest_scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            nest_scroll_view.fullScroll(View.FOCUS_DOWN);
                        }
                    });*/
                    Getfinancepitch();
                    Getinsurancepitch();
                    if (swt_smiles.isChecked()){
                        smiles = "1";
                        //  Toast.makeText(InterestedContactActivity.this, "1", Toast.LENGTH_SHORT).show();
                    } if (swt_intrested_in_utrust.isChecked()){
                        intrested_in_utrust_status = "1";
                    } if (swt_product_demo.isChecked()){
                        product_demo_given = "1";
                    } if (swt_brouchure_given.isChecked()){
                        brochure_given = "1";
                    } if (swt_quotation_given.isChecked()){
                        quotation_given = "1";
                    } if (swt_extended_warranty.isChecked()){
                        extended_warranty = "1";
                    } if (swt_accessories_pitch.isChecked()){
                        accessories_pitch = "1";
                    } if (swt_notify_customer.isChecked()){
                        notify_customer = "1";
                    }
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


        txt_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isfirstPressed = true;
                txt_first.setBackgroundResource(R.drawable.btn_line);
                txt_first.setTextColor(Color.parseColor("#EB0A1E"));
                demand_structure_type = "1";
                lin_additional.setVisibility(View.GONE);
                lin_replace.setVisibility(View.GONE);
                //  lin_reason_waiting.setVisibility(View.VISIBLE);
                //  prideliverystatus = txt_follow_up.getText().toString().trim();
                isfirstPressed = true;
                if (isadditionPlace) {
                    txt_addition.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_addition.setTextColor(Color.parseColor("#A7A7A7"));
                    isadditionPlace = false;
                }else if (isreplacePlace){
                    txt_replace.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_replace.setTextColor(Color.parseColor("#A7A7A7"));
                    isreplacePlace = false;
                }
            }
        });

        txt_addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_addition.setBackgroundResource(R.drawable.btn_line);
                txt_addition.setTextColor(Color.parseColor("#EB0A1E"));
                lin_additional.setVisibility(View.VISIBLE);
                lin_replace.setVisibility(View.GONE);
                demand_structure_type = "2";
                // Spinner Drop down elements
                List<String> categories = new ArrayList<String>();
                categories.add("Fuel Type");
                categories.add("PETROL");
                categories.add("DIESEL");
                categories.add("CNG");
                categories.add("ELECTRIC");
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(InterestedContactActivity.this, android.R.layout.simple_spinner_item, categories){
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
                            Typeface typeface = ResourcesCompat.getFont(InterestedContactActivity.this, R.font.poppins);
                            ((TextView) v).setTypeface(typeface);
                            ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
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
                            txt_replace.setTextColor(Color.parseColor("#A7A7A7"));
                            Typeface typeface = ResourcesCompat.getFont(InterestedContactActivity.this, R.font.poppins);
                            ((TextView) view).setTypeface(typeface);
                            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,0);
                            ((TextView) view).setLayoutParams(params);
                            ((TextView) view).setPadding(15,10,0,0);

                        }
                        return view;
                    }
                };
                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                spinner_fuel_type.setAdapter(dataAdapter);
                spinner_fuel_type.setPrompt("Fuel Type");

                isadditionPlace = true;
                if (isfirstPressed) {
                    txt_first.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_first.setTextColor(Color.parseColor("#A7A7A7"));
                    isfirstPressed = false;
                }else if (isreplacePlace) {
                    txt_replace.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_replace.setTextColor(Color.parseColor("#A7A7A7"));
                    isreplacePlace = false;
                }
            }
        });

        txt_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txt_replace.setBackgroundResource(R.drawable.btn_line);
                txt_replace.setTextColor(Color.parseColor("#EB0A1E"));
                lin_replace.setVisibility(View.VISIBLE);
                lin_additional.setVisibility(View.GONE);
                demand_structure_type = "3";
                // Spinner Drop down elements
                List<String> categories = new ArrayList<String>();
                categories.add("Fuel Type");
                categories.add("PETROL");
                categories.add("DIESEL");
                categories.add("CNG");
                categories.add("ELECTRIC");

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapterone = new ArrayAdapter<String>(InterestedContactActivity.this, android.R.layout.simple_spinner_item, categories){
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
                            ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                            Typeface typeface = ResourcesCompat.getFont(InterestedContactActivity.this, R.font.poppins);
                            ((TextView) v).setTypeface(typeface);
                        }
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
                            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                            Typeface typeface = ResourcesCompat.getFont(InterestedContactActivity.this, R.font.poppins);
                            ((TextView) view).setTypeface(typeface);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,0,0,0);
                            ((TextView) view).setLayoutParams(params);
                            ((TextView) view).setPadding(15,10,0,0);
                        }
                        return view;
                    }
                };
                // Drop down layout style - list view with radio button

                dataAdapterone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // attaching data adapter to spinner
                spinner_replace_fuel_type.setAdapter(dataAdapterone);
                spinner_replace_fuel_type.setPrompt("Fuel Type");
                isreplacePlace = true;
                if (isfirstPressed) {
                    txt_first.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_first.setTextColor(Color.parseColor("#A7A7A7"));
                    isfirstPressed = false;
                }else if (isadditionPlace){
                    txt_addition.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_addition.setTextColor(Color.parseColor("#A7A7A7"));
                    isadditionPlace = false;
                }
            }
        });


        swt_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    swt_follow_up.setChecked(true);
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(InterestedContactActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(InterestedContactActivity.this,
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
                if (img_personal_up.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                        && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ){
                    follow_up_type = "1";
                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                    isCallPressed = true;
                    if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")){
                        txt_add_contact.setBackgroundResource(R.drawable.shape_text_button);
                        txt_add_contact.setClickable(true);
                        txt_add_contact.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_add_contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_add_contact.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_add_contact.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                    if (isCallPressed) {
                        txtdDirectvisit.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txtdDirectvisit.setTextColor(Color.parseColor("#A7A7A7"));
                        isadditionPlace = false;
                    }

                }else if (rel_cop_details_title.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                        && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ){
                    follow_up_type = "1";
                    txt_telephone.setBackgroundResource(R.drawable.btn_line);
                    txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                    isCallPressed = true;
                    if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")){
                        txt_add_contact.setBackgroundResource(R.drawable.shape_text_button);
                        txt_add_contact.setClickable(true);
                        txt_add_contact.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_add_contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_add_contact.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_add_contact.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                    if (isCallPressed) {
                        txtdDirectvisit.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txtdDirectvisit.setTextColor(Color.parseColor("#A7A7A7"));
                        isadditionPlace = false;
                    }
                }
            }
        });

        txtdDirectvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (img_personal_up.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                        && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ) {

                    follow_up_type = "2";
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                    isDirectPlace = true;
                    if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")){
                        txt_add_contact.setBackgroundResource(R.drawable.shape_text_button);
                        txt_add_contact.setClickable(true);
                        txt_add_contact.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_add_contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_add_contact.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_add_contact.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                    if (isCallPressed) {
                        txt_telephone.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                        isCallPressed = false;
                    }

                }else if (rel_cop_details_title.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                        && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ){


                    follow_up_type = "2";
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                    isDirectPlace = true;
                    if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")){
                        txt_add_contact.setBackgroundResource(R.drawable.shape_text_button);
                        txt_add_contact.setClickable(true);
                        txt_add_contact.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_add_contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_add_contact.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_add_contact.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                    if (isCallPressed) {
                        txt_telephone.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                        txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                        isCallPressed = false;
                    }

                }
            }
        });



        swt_intrested_in_utrust.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    intrested_in_utrust_status  = "1";
                } else {
                    // The toggle is disabled
                    intrested_in_utrust_status =  "0";
                }
            }
        });

        swt_product_demo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    product_demo_given  = "1";
                } else {
                    // The toggle is disabled
                    product_demo_given =  "0";
                }
            }
        });


        swt_brouchure_given.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    brochure_given  = "1";
                } else {
                    // The toggle is disabled
                    brochure_given =  "0";
                }
            }
        });

        swt_quotation_given.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    quotation_given  = "1";
                } else {
                    // The toggle is disabled
                    quotation_given =  "0";
                }
            }
        });


        swt_extended_warranty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    extended_warranty  = "1";
                } else {
                    // The toggle is disabled
                    extended_warranty =  "0";
                }
            }
        });

        swt_smiles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    smiles  = "1";
                } else {
                    // The toggle is disabled
                    smiles =  "0";
                }
            }
        });

        swt_accessories_pitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    accessories_pitch  = "1";
                } else {
                    // The toggle is disabled
                    accessories_pitch =  "0";
                }
            }
        });

        swt_notify_customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    notify_customer  = "1";
                } else {
                    // The toggle is disabled
                    notify_customer =  "0";
                }
            }
        });

        spinner_finance_pitch.setOnItemSelectedListener(this);

        spinner_insurance_pitch.setOnItemSelectedListener(this);

        spinner_fuel_type.setOnItemSelectedListener(this);

        spinner_replace_fuel_type.setOnItemSelectedListener(this);

        thisYear = new SimpleDateFormat("yyyy").format(new Date());

        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // TODO Auto-generated method stub
        switch (parent.getId()) {

            case R.id.spinner_fuel_type:
                current_car_fuel_type = Integer.toString(position);
               // Toast.makeText(InterestedContactActivity.this, current_car_fuel_type, Toast.LENGTH_SHORT).show();
                break;

            case R.id.spinner_replace_fuel_type:
                current_car_fuel_type = Integer.toString(position);
                break;

            case R.id.spinner_finance_pitch:
                finance_pitch_id = financePitchLists.get(position).getFinance_id();
                if (finance_pitch_id.equals("2")){
                    lin_finance_pinch.setVisibility(View.VISIBLE);
                    finance_other = edt_finance_pinch_text.getText().toString();
                }else {
                    lin_finance_pinch.setVisibility(View.GONE);
                    finance_other = "";
                }

                break;

            case R.id.spinner_insurance_pitch:
                insurance_master_id = insuranceLists.get(position).getInsurance_id();
                if (insurance_master_id.equals("2")){
                    lin_insurance_pinch.setVisibility(View.VISIBLE);
                    insurance_other = edt_insurance_pinch_text.getText().toString();
                    //edt_insurance_pinch_text.getText().toString();
                }else {
                    lin_insurance_pinch.setVisibility(View.GONE);
                    insurance_other = "";
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void back(View view) {
        startActivity(new Intent(InterestedContactActivity.this,CustomerDetailsActivity.class).putExtra("Status","BACK"));
         finish();
    }

    public void updatestatus(View view) {
        startActivity(new Intent(InterestedContactActivity.this,CustomerDetailsActivity.class).putExtra("Status","Enquiry Follow Up"));
         finish();
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Api call Customer_Gettitles
    public void Getfinancepitch () {
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_getfinancepitch_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            financePitchLists.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("finance_pitch");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                FinancePitchList titles = new FinancePitchList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setFinance_id(jsonObject_titles.getString("finance_id"));
                                titles.setFinance_type(jsonObject_titles.getString("finance_type"));
                                financePitchLists.add(titles);

                                // Populate spinner with country names
                                //   worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            FinancePitchTypeArrayAdapter adapter = new FinancePitchTypeArrayAdapter(InterestedContactActivity.this, R.layout.layout_title_type,
                                    financePitchLists);
                            spinner_finance_pitch.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
    // Api call Enquiry_Create_Enquiry
    public void Getinsurancepitch () {
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_getinsurancepitch_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    //  progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            insuranceLists.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("insurance_pitch");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                InsuranceList titles = new InsuranceList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setInsurance_id(jsonObject_titles.getString("insurance_id"));
                                titles.setInsurance_type(jsonObject_titles.getString("insurance_type"));
                                insuranceLists.add(titles);

                                // Populate spinner with country names
                                //   worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            InsurancePitchTypeArrayAdapter adapter = new InsurancePitchTypeArrayAdapter(InterestedContactActivity.this,
                                    R.layout.layout_enquiry, insuranceLists);
                            spinner_insurance_pitch.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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

    // Api call Customer_Get_Contact
    public void Customer_Get_Contact(){
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
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
                                //  MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.TITLEID,titleid);
                                if (prospect_category.equals("1")){
                                    edt_cus_title.setText(titleid);
                                }else if (prospect_category.equals("2")){
                                    edt_cus_cop_title.setText(titleid);
                                }
                            }

                            if (jsonObject.isNull("MODE_ID")){
                                mode_id = "";
                            }else {
                                mode_id = jsonObject.getString("MODE_ID");


                            }

                            if (jsonObject.isNull("SOURCE_ID")){
                                source_id = "";
                            }else {
                                source_id = jsonObject.getString("SOURCE_ID");


                            }

                            if (jsonObject.isNull("TITLE_ID")){
                                title_id = "";
                            }else {
                                title_id = jsonObject.getString("TITLE_ID");


                            }


                            if (jsonObject.isNull("PROFESSION_ID")){
                                profession_id = "";
                            }else {
                                profession_id = jsonObject.getString("PROFESSION_ID");

                            }


                            if (jsonObject.isNull("ANNUAL_INCOME_ID")){
                                annual_income_id = "";
                            }else {
                                annual_income_id = jsonObject.getString("ANNUAL_INCOME_ID");

                            }

                            if (jsonObject.isNull("COMPANY_TYPE_ID")){
                                company_type_id = "";
                            }else {
                                company_type_id = jsonObject.getString("COMPANY_TYPE_ID");

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
                            JSONObject intrested_vehicle = jsonArray_titles.getJSONObject("intrested_vehicle");
                            MODE_SUFFIX_NAME = intrested_vehicle.getString("MODEL_NAME");
                            edt_mode_suffix_name.setText(MODE_SUFFIX_NAME);

                            MODEL_VARIANT_NAME = intrested_vehicle.getString("MODEL_VARIANT_NAME");
                            edt_variant_suffix_name.setText(MODEL_VARIANT_NAME);

                            if (intrested_vehicle.isNull("INTERIOR_COLOR_NAME")){
                                edt_cus_interior_color_name.setText("Color not selected");
                            }else {
                                INTERIOR_COLOR_NAME = intrested_vehicle.getString("INTERIOR_COLOR_NAME");
                                edt_cus_interior_color_name.setText(INTERIOR_COLOR_NAME);
                            }

                            if (intrested_vehicle.isNull("EXTERIOR_COLOR_NAME")){
                                edt_cus_exterior_color_name.setText("Color not selected");
                            }else {
                                EXTERIOR_COLOR_NAME = intrested_vehicle.getString("EXTERIOR_COLOR_NAME");
                                edt_cus_exterior_color_name.setText(EXTERIOR_COLOR_NAME);
                            }

                            if (intrested_vehicle.isNull("QUANTITY")){

                            }else {
                                QUANTITY = intrested_vehicle.getString("QUANTITY");
                                edt_cus_quantity.setText(QUANTITY );
                            }

                            vehicle_id = intrested_vehicle.getString("MODEL_ID");

                            MODEL_VARIANT_ID = intrested_vehicle.getString("MODEL_VARIANT_ID");

                            if (intrested_vehicle.isNull("INTERIOR_COLOR_ID")){
                                interior_color_id = "";

                            }else {
                                interior_color_id = intrested_vehicle.getString("INTERIOR_COLOR_ID");
                                edt_cus_interior_color_name.setText(INTERIOR_COLOR_NAME);
                            }

                            if (intrested_vehicle.isNull("EXTERIOR_COLOR_ID")){
                                exterior_color_id = "";

                            }else {
                                exterior_color_id = intrested_vehicle.getString("EXTERIOR_COLOR_ID");
                                edt_cus_exterior_color_name.setText(EXTERIOR_COLOR_NAME);
                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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

    public void validate_add_enquiery(){
        if (prospect_category.equals("1")){
            customer_name = Objects.requireNonNull(edt_cus_name.getText()).toString();
            if (customer_name.equals("")) {
                edt_cus_name.setError("Please Enter Customer Name");
            }

            contact_number_one = Objects.requireNonNull(edt_cus_one_number.getText()).toString().trim();
            if (contact_number_one.isEmpty() || (contact_number_one.equals("")) || (contact_number_one.toString().trim().length() < 10)) {
                edt_cus_one_number.setError("Please Enter the Correct Phone Number");
            }

            contact_number_two = edt_cus_two_number.getText().toString().trim();
            if (contact_number_two.isEmpty() || (contact_number_two.equals("")) || (contact_number_two.toString().trim().length() < 10)) {
                //  edt_cus_two_number.setError("Please Enter the Correct Phone Number");
            }

            email = edt_cus_email.getText().toString().trim();

            if (!isValidEmail(email)) {
                edt_cus_email.setError("Please Enter the Correct Email Id");
            }

            finance_other = edt_finance_pinch_text.getText().toString();
            if (finance_other.equals("")){
                edt_finance_pinch_text.setError("Please Fill the filed");
            }

            insurance_other = edt_insurance_pinch_text.getText().toString();
            if (insurance_other.equals("")) {
                edt_insurance_pinch_text.setError("Please Fill the filed");
            }



            String cusdob = edt_cus_date.getText().toString().trim();
            if (cusdob.equals("") || cusdob.isEmpty()) {
                // edt_cus_date.setError("Please Selecte Dob");
                dob = "";
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(cusdob);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    dob = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sub_occupation = edt_cus_sub_occupation.getText().toString().trim();
            if (sub_occupation.equals("") || sub_occupation.isEmpty()) {
                //  edt_cus_sub_occupation.setError("Please Enter the Correct Sub Occupation");
            }

            String addressone = edt_address_line_one.getText().toString().trim();
            if (addressone.equals("") || addressone.isEmpty()) {
                edt_address_line_one.setError("Please Enter the Correct Address");
            }

            String addresstwo = edt_address_line_two.getText().toString().trim();
            if (addresstwo.equals("") || addresstwo.isEmpty()) {
                edt_address_line_two.setError("Please Enter the Correct Address");
            }

            String addressthree = edt_address_line_three.getText().toString().trim();
            if (addressthree.equals("") || addressthree.isEmpty()||addressthree.toString().trim().length() < 6) {
                edt_address_line_three.setError("Please Enter the Correct Address");
            }
            address = addressone + "" + "\t" + addresstwo + "\t" + addressthree;

            if (demand_structure_type.equals("1")){
                current_car_maker = "No Model";
                current_car_model = "NoNeeded";
                year_month = "2000";
                current_car_fuel_type = "1";
                current_car_expected_value = "NoNeeded";
                current_car_reg_number = "not needed";
            }else if (demand_structure_type.equals("2")){
                current_car_maker = edt_current_car_name.getText().toString();
             //   Toast.makeText(InterestedContactActivity.this, current_car_maker, Toast.LENGTH_SHORT).show();
                current_car_expected_value = "";
                if (current_car_maker.equals("")||(current_car_maker.isEmpty())){
                    edt_current_car_name.setError("Please Enter the Correct Car Name");
                }

                current_car_model = edt_model.getText().toString().trim();
                if ((current_car_model.equals("")||current_car_model.isEmpty())) {
                    edt_model.setError("Please Enter the Correct Model Name");
                }

                year_month = edt_year_month.getText().toString().trim();
                if (year_month.equals("")||year_month.toString().trim().length() < 4 || thisYear.compareTo(year_month) < 0
                        || "2000".compareTo(year_month) > 0) {
                    // !isValiddate(year_month)
                    edt_year_month.setError("Please Enter the year from 2020 year");
                }

                current_car_reg_number = edt_req_number.getText().toString().trim();
                if (current_car_reg_number.isEmpty() || (current_car_reg_number.equals("")) || (current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)) {
                    edt_req_number.setError("Reg Number should not less than  char ");
                }
            }else if(demand_structure_type.equals("3")){
                current_car_maker = edt_replace_current_car_name.getText().toString();
                if (current_car_maker.isEmpty() || (current_car_maker.equals(""))) {
                    edt_replace_current_car_name.setError("Please Enter the Correct Car Name");
                }

                current_car_model = edt_replace_model.getText().toString().trim();
                if ((current_car_model.equals("")||current_car_model.isEmpty())) {
                    edt_replace_model.setError("Please Enter the Correct Model Name");
                }

                year_month= edt_replace_year_month.getText().toString().trim();
                if (year_month.equals("") || thisYear.compareTo(year_month) < 0
                        || "2000".compareTo(year_month) > 0) {
                    //!isValiddate(year_month)
                    edt_year_month.setError("Please Enter the year from 2020 year");
                }

                current_car_reg_number = edt_replace_req_number.getText().toString().trim();
                if (current_car_reg_number.isEmpty() || (current_car_reg_number.equals("")) || (current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)) {
                    edt_replace_req_number.setError("Reg Number should not less than 5 char ");
                }
                current_car_expected_value = edt_replace_expected_value.getText().toString().trim();
                if ((current_car_expected_value.equals("")||current_car_expected_value.isEmpty())) {
                    edt_replace_expected_value.setError("Please Enter the Correct Value");
                }
            }

            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    follow_up_date = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            followuptime = edt_time.getText().toString().trim()+":"+"00";
            if (followuptime.equals("") || followuptime.isEmpty()) {
                edt_time.setError("Please Selecte Time");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                try {

                    Date newDate = spf.parse(followuptime);
                    spf = new SimpleDateFormat("HH:mm");
                    follow_up_time = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mode_id.equals("") || vehicle_id.equals("")||demand_structure_type.equals("")){
                Toast.makeText(InterestedContactActivity.this, "Select Field", Toast.LENGTH_SHORT).show();
            }

            follow_up_remark= edt_remark.getText().toString().trim();
            if (follow_up_remark.equals("") || follow_up_remark.isEmpty()) {
                //edt_remark.setError("Please Enter the Correct Remark");
            }

            if (follow_up_remark.equals("")||followupdate.isEmpty()) {
                // edt_remark.setError("Please Fill the Remarks");
            }

            if (isCallPressed=false){
                txt_telephone.setError("Please select anyone");
            }else if (isDirectPlace=false){
                txtdDirectvisit.setError("Please select anyone");
            }

            QUANTITY = edt_cus_quantity.getText().toString();

            if ((customer_name.equals("")||contact_number_one.equals(""))||(contact_number_one.toString().trim().length() < 10) || contact_number_one.isEmpty()) {
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            }  else if (mode_id.equals("") || vehicle_id.equals("")||demand_structure_type.equals("")|| Objects.equals(address, "")||(follow_up_time.equals(""))) {
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (current_car_maker.equals("")||current_car_model.equals("")||year_month.equals("")||(current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)
                    ||current_car_fuel_type.equals("")||thisYear.compareTo(year_month) < 0 || "2000".compareTo(year_month) > 0
                    ||(addressthree.toString().trim().length() < 6)){
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else {
                Customer_Update_Equiery();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(InterestedContactActivity.this);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                anim.setDuration(15000);
                anim.setInterpolator(new DecelerateInterpolator());
                progressDialog.show();
                System.out.println("test api"+ "3" + current_car_expected_value );
            }
        }else if (prospect_category.equals("2")){
            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.PROSPECTCATEGORY,prospect_category);
            company_name = edt_cop_cus_name.getText().toString();
            if (company_name.equals("") || company_name.isEmpty()) {
                edt_cop_cus_name.setError("Please Enter Company Name");
            }

            mobilenumber = edt_cop_mobile_number.getText().toString().trim();
            if (mobilenumber.isEmpty() || (mobilenumber.equals("")) || (mobilenumber.toString().trim().length() < 10)) {
                edt_cop_mobile_number.setError("Please Enter the Correct Phone Number");
            }

            additionalmobilenumber = edt_cop_sec_mobile_number.getText().toString().trim();
            if (additionalmobilenumber.isEmpty() || (additionalmobilenumber.equals("")) || (additionalmobilenumber.toString().trim().length() < 10)) {
                // edt_cus_contact_one.setError("Please Enter the Correct Phone Number");
            }

            contact_person_name = edt_cop_contact_person_name.getText().toString().trim();
            if (contact_person_name.isEmpty() || (contact_person_name.equals(""))) {
                // edt_cop_contact_person_name.setError("Please Enter the Correct Contact Person Name");
            }

            designation = edt_cop_designation.getText().toString().trim();
            if (designation.equals("")||designation.isEmpty()){
                // edt_cop_designation.setError("Please Enter the Correct Designation");
            }


            String addressone = edt_cop_address_address.getText().toString().trim();
            if (addressone.equals("") || addressone.isEmpty()) {
                edt_cop_address_address.setError("Please Enter the Correct Address");
            }

            String addresstwo = edt_cop_address_line_two.getText().toString().trim();
            if (addresstwo.equals("") || addresstwo.isEmpty()) {
                edt_cop_address_line_two.setError("Please Enter the Correct Address");
            }

            String addressthree = edt_cop_address_line_three.getText().toString().trim();
            if (addressthree.equals("") || addressthree.isEmpty()||addressthree.toString().trim().length() < 6) {
                edt_cop_address_line_three.setError("Please Enter the Correct Address");
            }
            address = addressone + "" + "\t" + addresstwo + "\t" + addressthree;

            if (demand_structure_type.equals("1")){
                current_car_maker = "No Model";
                current_car_model = "NoNeeded";
                year_month = "2000";
                current_car_fuel_type = "1";
                current_car_expected_value = "NoNeeded";
                current_car_reg_number = "not needed";
            }else if (demand_structure_type.equals("2")){
                current_car_maker = edt_current_car_name.getText().toString();
                current_car_expected_value = "";
                if (current_car_maker.isEmpty() || (current_car_maker.equals(""))) {
                    edt_current_car_name.setError("Please Enter the Correct Car Name");
                }

                current_car_model = edt_model.getText().toString().trim();
                if ((current_car_model.equals("")||current_car_model.isEmpty())) {
                    edt_model.setError("Please Enter the Correct Model Name");
                }


                year_month = edt_year_month.getText().toString().trim();
                if (year_month.equals("")||year_month.toString().trim().length() < 4|| thisYear.compareTo(year_month) < 0
                        || "2000".compareTo(year_month) > 0) {
                    // !isValiddate(year_month)
                    edt_year_month.setError("Please Enter the Date format year");
                }

                current_car_reg_number = edt_req_number.getText().toString().trim();
                if (current_car_reg_number.isEmpty() || (current_car_reg_number.equals(""))|| (current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)) {
                    edt_req_number.setError("Reg Number should not less than 10 char ");
                }
            }else if(demand_structure_type.equals("3")){
                current_car_maker = edt_replace_current_car_name.getText().toString();
                if (current_car_maker.isEmpty() || (current_car_maker.equals(""))) {
                    edt_replace_current_car_name.setError("Please Enter the Correct Car Name");
                }

                current_car_model = edt_replace_model.getText().toString().trim();
                if ((current_car_model.equals("")||current_car_model.isEmpty())) {
                    edt_replace_model.setError("Please Enter the Correct Model Name");
                }

                //  String month = edt_replace_year_month.getText().toString().trim();
                year_month = edt_replace_year_month.getText().toString().trim();
                if (year_month.equals("")||year_month.toString().trim().length() < 4|| thisYear.compareTo(year_month) < 0
                        || "2000".compareTo(year_month) > 0) {
                    // !isValiddate(year_month)
                    edt_replace_year_month.setError("Please Enter the Date format year");
                }

                current_car_reg_number = edt_replace_req_number.getText().toString().trim();
                if (current_car_reg_number.isEmpty() || (current_car_reg_number.equals("")) || (current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)) {
                    edt_replace_req_number.setError("Reg Number should not less than 18 char ");
                }
                current_car_expected_value = edt_replace_expected_value.getText().toString().trim();
                if ((current_car_expected_value.equals("")||current_car_expected_value.isEmpty())) {
                    edt_replace_expected_value.setError("Please Enter the Correct Value");
                }
            }
            QUANTITY = edt_cus_quantity.getText().toString();
            finance_other = edt_finance_pinch_text.getText().toString();
            if (finance_other.equals("")){
                edt_finance_pinch_text.setError("Please Fill the filed");
            }

            insurance_other = edt_insurance_pinch_text.getText().toString();
            if (insurance_other.equals("")) {
                edt_insurance_pinch_text.setError("Please Fill the filed");
            }

            String followupdate = edt_date.getText().toString().trim();
            if (followupdate.equals("") || followupdate.isEmpty()) {
                edt_date.setError("Please Selecte Dob");
            } else {
                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                try {

                    Date newDate = spf.parse(followupdate);
                    spf = new SimpleDateFormat("yyyy-MM-dd");
                    follow_up_date = spf.format(newDate);

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
                    follow_up_time = spf.format(newDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            follow_up_remark= edt_remark.getText().toString().trim();
            if (follow_up_remark.equals("") || follow_up_remark.isEmpty()) {
                // edt_remark.setError("Please Enter the Correct Remark");
            }

            if (follow_up_remark.equals("")||followupdate.isEmpty()) {
                // edt_remark.setError("Please Fill the Remarks");
            }

            email = edt_cus_cop_company_email.getText().toString().trim();

            if (!isValidEmail(email)) {
                edt_cus_cop_company_email.setError("Please Enter the Correct Email Id");
            }

            if ((company_name.equals("")||mobilenumber.toString().trim().length() < 10) || mobilenumber.isEmpty()||(!isValidEmail(email))
                    ||(addressthree.toString().trim().length() < 6)) {
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (mode_id.equals("") || vehicle_id.equals("")||demand_structure_type.equals("")|| address.equals("")||(follow_up_time.equals(""))) {
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (current_car_maker.equals("")||current_car_model.equals("")||year_month.equals("")||(current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)
                    ||current_car_fuel_type.equals("")||thisYear.compareTo(year_month) < 0 || "2000".compareTo(year_month) > 0){
                Toast.makeText(InterestedContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            }else {
                Customer_Cop_Update_Equiery();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(InterestedContactActivity.this);
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

    public void Customer_Update_Equiery() {
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_updateenquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            contact_id = jsonObj.getString("enquiry_id");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObj.getString("enquiry_status");

                            JSONObject jsonObject = jsonObj.getJSONObject("enquiry_details");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            startActivity(new Intent(InterestedContactActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                   // exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                params.put("PROSPECT_CATEGORY", prospect_category);
                params.put("ENQUIRY_ID", UPDATE_ENQUIRY_ID);
                params.put("ENQUIRY_TYPE", "");
                params.put("MODE_ID", mode_id);
                params.put("SOURCE_ID", source_id);
                params.put("TITLE", title_id);
                params.put("CUSTOMER_NAME", customer_name);
                params.put("CONTACT_NUMBER_1", contact_number_one);
                params.put("CONTACT_NUMBER_2", contact_number_two);
                params.put("PROFESSION_ID", profession_id);
                params.put("SUB_OCCUPATION", sub_occupation);
                params.put("ADDRESS", address);
                params.put("ANNUL_INCOME_ID", annual_income_id);
                params.put("EMAIL", email);
                params.put("DOB", dob);
                params.put("DEMAND_STRUCTURE_TYPE", demand_structure_type);
                params.put("CURRENT_CAR_MAKE", current_car_maker);//current_car_maker
                params.put("CURRENT_CAR_MODEL", current_car_model);//current_car_model
                params.put("CURRENT_CAR_FUEL_TYPE", current_car_fuel_type);
                params.put("MAKE_YEAR",year_month);//year_month
                params.put("CURRENT_CAR_REG_NUMBER", current_car_reg_number);//current_car_reg_number
                params.put("CURRENT_CAR_EXPECTED_VALUE", current_car_expected_value);
                params.put("U_TRUST_INTRESTED", intrested_in_utrust_status);
                params.put("PRODUCT_DEMO_GIVEN", product_demo_given);
                params.put("BROCHURE_GIVEN", brochure_given);
                params.put("QUOTATION_GIVEN", quotation_given);
                params.put("EXTENDED_WARRANTY", extended_warranty);
                params.put("SMILES", smiles);
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", MODEL_VARIANT_ID);
                params.put("ACCESSORIES_PITCH", accessories_pitch);
                params.put("NOTIFY_CUSTOMER", notify_customer);
                params.put("FINANCE_PITCH", finance_pitch_id);
                params.put("INSURANCE_MASTER_ID",insurance_master_id);
                params.put("FINANCE_OTHERS", finance_other);
                params.put("INSURANCE_OTHERS",insurance_other);
                params.put("FOLLOW_UP_DATE", follow_up_date);
                params.put("FOLLOW_UP_TIME",follow_up_time);
                params.put("FOLLOW_UP_TYPE", follow_up_type);
                params.put("FOLLOW_UP_REMARK",follow_up_remark);
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

    public void Customer_Cop_Update_Equiery(){
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_updateenquiry, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            contact_id = jsonObj.getString("enquiry_id");
                            contact_status = jsonObj.getString("enquiry_status");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CONTACT_ID,contact_id);
                            JSONObject jsonObject = jsonObj.getJSONObject("enquiry_details");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(InterestedContactActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            startActivity(new Intent(InterestedContactActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                  //  exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(InterestedContactActivity.this);
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
                params.put("PROSPECT_CATEGORY", prospect_category);
                params.put("ENQUIRY_ID", UPDATE_ENQUIRY_ID);
                params.put("ENQUIRY_TYPE", "");
                params.put("MODE_ID", mode_id);
                params.put("SOURCE_ID", source_id);
                params.put("TITLE", title_id);
                params.put("COMPANY_NAME", company_name);
                params.put("COMPANY_TYPE_ID", company_type_id);
                params.put("CONTACT_PERSON_NAME", contact_person_name);
                params.put("DESIGNATION", designation);
                params.put("MOBILE_NUMBER", mobilenumber);
                params.put("ADDITIONAL_MOBILE_NUMBER", additionalmobilenumber);
                params.put("ADDRESS", address);
                params.put("ANNUL_INCOME_ID", annual_income_id);
                params.put("EMAIL", email);
                params.put("DEMAND_STRUCTURE_TYPE", demand_structure_type);
                params.put("CURRENT_CAR_MAKE", current_car_maker);//current_car_maker
                params.put("CURRENT_CAR_MODEL", current_car_model);//current_car_model
                params.put("CURRENT_CAR_FUEL_TYPE", current_car_fuel_type);
                params.put("MAKE_YEAR",year_month);//year_month
                params.put("CURRENT_CAR_REG_NUMBER", current_car_reg_number);//current_car_reg_number
                params.put("CURRENT_CAR_EXPECTED_VALUE", current_car_expected_value);
                params.put("U_TRUST_INTRESTED", intrested_in_utrust_status);
                params.put("PRODUCT_DEMO_GIVEN", product_demo_given);
                params.put("BROCHURE_GIVEN", brochure_given);
                params.put("QUOTATION_GIVEN", quotation_given);
                params.put("EXTENDED_WARRANTY", extended_warranty);
                params.put("SMILES", smiles);
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", MODEL_VARIANT_ID);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("ACCESSORIES_PITCH", accessories_pitch);
                params.put("QUANTITY", QUANTITY);
                params.put("NOTIFY_CUSTOMER", notify_customer);
                params.put("FINANCE_PITCH", finance_pitch_id);
                params.put("INSURANCE_MASTER_ID",insurance_master_id);
                params.put("FINANCE_OTHERS", finance_other);
                params.put("INSURANCE_OTHERS",insurance_other);
                params.put("FOLLOW_UP_DATE", follow_up_date);
                params.put("FOLLOW_UP_TIME",follow_up_time);
                params.put("FOLLOW_UP_TYPE", follow_up_type);
                params.put("FOLLOW_UP_REMARK",follow_up_remark);
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
