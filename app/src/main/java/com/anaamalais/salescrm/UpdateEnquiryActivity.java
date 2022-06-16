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
import androidx.core.widget.NestedScrollView;

import com.anaamalais.salescrm.Adapter.AnnualIncomeTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.CompanyTypesArrayAdapter;
import com.anaamalais.salescrm.Adapter.EnquiryTitleTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.EnquiryTypesArrayAdapter;
import com.anaamalais.salescrm.Adapter.ExteriorColorArrayAdapter;
import com.anaamalais.salescrm.Adapter.FinancePitchTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.InsurancePitchTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.InteriorColorTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModeTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelVariantTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelsTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ProfessionTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.SourceTypeArrayAdapter;
import com.anaamalais.salescrm.List.AnnualIncomesList;
import com.anaamalais.salescrm.List.CompanyTypesList;
import com.anaamalais.salescrm.List.EnquiryTitle;
import com.anaamalais.salescrm.List.EnquiryTypesList;
import com.anaamalais.salescrm.List.ExteriorColorsList;
import com.anaamalais.salescrm.List.FinancePitchList;
import com.anaamalais.salescrm.List.FuelList;
import com.anaamalais.salescrm.List.InsuranceList;
import com.anaamalais.salescrm.List.InteriorColorsList;
import com.anaamalais.salescrm.List.ModeList;
import com.anaamalais.salescrm.List.ModelsList;
import com.anaamalais.salescrm.List.ProfessionsList;
import com.anaamalais.salescrm.List.SourcesList;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEnquiryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    InteriorColorTypeArrayAdapter adapter;
    ExteriorColorArrayAdapter exadapter;
    TextView txt_individual , txt_corporate,edt_time , txt_first , txt_addition ,txt_replace,txt_plus ,txt_minus,
            txt_count, txt_add_contact , txt_telephone , txtdDirectvisit,txt_title,txt_update_enquiry;
    Boolean isOnePressed = false, isSecondPlace = false;
    Boolean isCallPressed = false, isDirectPlace = false;
    int quantity = 0;
    String QUANTITY="1";
    NestedScrollView nest_scroll_view;
    String MONTHYEAR,thisYear,followuptime,add_update,UPDATE_ENQUIRY_ID;
    TextInputEditText edt_cus_name , edt_cus_one_number , edt_cus_two_number , edt_cus_email ,
            edt_cus_sub_occupation , edt_address_line_one , edt_address_line_two , edt_address_line_three
            ,edt_cop_cus_name,edt_cop_contact_person_name,edt_cop_designation,edt_cop_mobile_number,
            edt_cop_address_address,edt_cop_address_line_two,edt_cop_address_line_three,edt_cop_sec_mobile_number
            ,edt_current_car_name,edt_model,edt_year_month,edt_req_number,edt_replace_current_car_name,
            edt_replace_model,edt_replace_year_month,edt_replace_req_number,edt_replace_expected_value
            ,edt_insurance_pinch_text,edt_finance_pinch_text,edt_cop_address_address_cus;

    TextInputEditText edt_cus_contact_one , edt_cus_cop_email_one ,edt_cop_address ,
            edt_cop_address_lines_two , edt_cop_address_lines_three ,edt_cop_mobiles_number ,
            edt_cus_cop_email_two , edt_cop_address_line_two_cus;

    SwitchCompat swt_address_proof_attached,swt_intrested_in_utrust,swt_product_demo,swt_brouchure_given,swt_quotation_given,swt_extended_warranty,swt_smiles,
            swt_accessories_pitch , swt_notify_customer;
    LinearLayout lin_individual , lin_corporate,lin_demand_structure,lin_intrested_vehicle,lin_value_chain,lin_follow_up
            ,lin_additional , lin_replace , lin_finance_pinch , lin_insurance_pinch;
    RelativeLayout rel_personal_details ,rel_cop_details_title, rel_demand_structure , rel_intrested_vehicle , rel_value_chain ,rel_next_follow_up;
    ImageView img_personal_downs , img_personal_up ,Img_personal_details_cop_up,Img_personal_details_cop_down,
            img_demand_structure_down , img_demand_structure_up , img_intrested_vehicle_down,img_intrested_vehicle_up,img_value_chain_down ,img_value_chain_up;
    SwitchCompat swt_next_follow_up;
    EditText edt_date,edt_cus_date,edt_remark;
    int mYear, mDay ,mMonth,mMinute,mHour;
    Boolean isfirstPressed = true, isadditionPlace = false , isreplacePlace = false;
    Animation animation;
    List<EnquiryTitle> titlesList;
    List<ModeList>modeLists;
    List<SourcesList>sourcesLists;
    List<ProfessionsList>professionsLists;
    List<AnnualIncomesList>annualIncomesLists;
    List<CompanyTypesList>companyTypesLists;
    List<ModelsList>modelsLists;
    List<VariantsLIst>variantsLIsts;
    List<InteriorColorsList>interiorColorsLists;
    List<ExteriorColorsList>exteriorColorsLists;
    List<EnquiryTypesList>enquiryTypesLists;
    List<FinancePitchList>financePitchLists;
    List<InsuranceList>insuranceLists;
    List<FuelList>fuelLists;
    String  status_code , msg ,token, API_TOKEN , finance_other , insurance_other,follow_up_type;
    String title_id , enquiry_id , mode_id , profession_id ,source_id , annual_income_id , company_type_id , vehicle_id
    , model_variant   , interior_color_id , exterior_color_id,contact_status,contact_id,finance_pitch_id,insurance_master_id;
    String demand_structure_type,current_car_maker,current_car_model,year_month,current_car_reg_number,current_car_expected_value;
    Spinner spinner_title , spinner_mode , spinner_enquiry , spinner_source , spinner_profession , spinner_cop
            , spinner_mode_cop , spinner_source_cop , spinner_company_type_cop ,
            spinner_annual_income , spinner_annual_income_cop , spinner_model_suffix_cop ,spinner_model_variant , spinner_exterEior_color,
            spinner_interior_color,spinner_fuel_type,spinner_replace_fuel_type,spinner_finance_pitch
            ,spinner_insurance_pitch,spinner_enquiry_type_cop;
    String customer_gettitles_url = BASE_URL + "customer/gettitles";
    String enquiry_createenquiry_url = BASE_URL + "enquiry/getenquirytype";
    String customer_getmodes_url = BASE_URL + "customer/getmodes";
    String customer_getsources_url = BASE_URL + "customer/getsources";
    String customer_getprofessions_url = BASE_URL + "customer/getprofessions";
    String customer_getanualincome_url = BASE_URL + "customer/getanualincome";
    String customer_getcompanytypes_url = BASE_URL + "customer/getcompanytypes";
    String vehicles_getallvehicle_url = BASE_URL + "customer/getmodels";
    String customer_getmodelvariants_url = BASE_URL + "customer/getmodelvariants";
    String vehiclecolormaster_getallinteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String vehiclecolormaster_getallexteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String enquiry_getfinancepitch_url = BASE_URL + "enquiry/getfinancepitch";
    String enquiry_getinsurancepitch_url = BASE_URL + "enquiry/getinsurancepitch";
    String createenquiry_url = BASE_URL + "enquiry/createenquiry";
    String enquiry_getenquiry = BASE_URL + "enquiry/getenquiry";
    String enquiry_updateenquiry = BASE_URL + "enquiry/updateenquiry";
    String prospect_category , customer_name , contact_number_one , contact_number_two , sub_occupation ,
            address , email , dob , company_name , contact_person_name,designation , mobilenumber,additionalmobilenumber
            ,current_car_fuel_type,follow_up_date,follow_up_time,follow_up_remark;
    String address_proof_attached_status , intrested_in_utrust_status , product_demo_given , brochure_given , quotation_given , extended_warranty , smiles
            , accessories_pitch , notify_customer ;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String enquirytype , statusid , contactid , modeid , sourceid , titleid , customername , contactnumberone,
            upcontactnumberone , contactnumbertwo , getemail , getdob ,professionid , suboccupation
            , getaddress , annualincomeid , companyname , companytypeid , contactpersonname , getmobilenumber
            , getadditionalmobilenumber , getdesignation , intrestedvehicleid , modesuffixid
            , interiorcolorid ,  exteriorcolorid , getquantity ,  type , current_car_make , models , fuel_type ,
            make_year , registration_number ,expected_value ,utrust_intrested;

    String productdemogiven , brochuregiven , quotationgiven , extended_warrantys , smiless
            , accessoriespitch , notifycustomer , financepitch , insurancemasterid , financeother
            , insuranceothers , date , time , types , remark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_enquiry);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = UpdateEnquiryActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(UpdateEnquiryActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(UpdateEnquiryActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this, Constants.TOKEN,"");
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
        nest_scroll_view = findViewById(R.id.nest_scroll_view);
        txt_update_enquiry = findViewById(R.id.txt_update_enquiry);
        //spinner individual
        spinner_title = (Spinner) findViewById(R.id.spinner_title);
        spinner_enquiry = (Spinner) findViewById(R.id.spinner_enquiry_type);
        spinner_mode = (Spinner) findViewById(R.id.spinner_mode);
        spinner_source = (Spinner) findViewById(R.id.spinner_source);
        spinner_profession = (Spinner) findViewById(R.id.spinner_profession);
        spinner_annual_income = (Spinner) findViewById(R.id.spinner_annual_income);
        spinner_model_suffix_cop = (Spinner) findViewById(R.id.spinner_model_suffix_cop);
        spinner_model_variant = (Spinner) findViewById(R.id.spinner_model_variant);
        spinner_exterEior_color = (Spinner) findViewById(R.id.spinner_exterEior_color);
        spinner_interior_color = (Spinner) findViewById(R.id.spinner_interior_color);
        spinner_fuel_type = (Spinner) findViewById(R.id.spinner_fuel_type);
        spinner_replace_fuel_type = (Spinner) findViewById(R.id.spinner_replace_fuel_type);
        spinner_finance_pitch = (Spinner) findViewById(R.id.spinner_finance_pitch);
        spinner_insurance_pitch = (Spinner) findViewById(R.id.spinner_insurance_pitch);
        spinner_cop = (Spinner) findViewById(R.id.spinner_cop);
        spinner_mode_cop = (Spinner) findViewById(R.id.spinner_mode_cop);
        spinner_source_cop = (Spinner) findViewById(R.id.spinner_source_cop);
        spinner_company_type_cop = (Spinner) findViewById(R.id.spinner_company_type_cop);
        spinner_annual_income_cop = (Spinner) findViewById(R.id.spinner_annual_income_cop);
        spinner_enquiry_type_cop = (Spinner) findViewById(R.id.spinner_enquiry_type_cop);

      //  txt_add_contact = findViewById(R.id.txt_add_contact);

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
        Calendar calendar = Calendar.getInstance();

        //coper tetx
        edt_cop_cus_name = findViewById(R.id.edt_cop_cus_name);
        edt_cop_mobile_number = findViewById(R.id.edt_cus_contact_one);
        edt_cus_cop_email_one = findViewById(R.id.edt_cus_cop_email_one);
        edt_cop_address = findViewById(R.id.edt_cop_address);
        edt_cop_address_lines_two  = findViewById(R.id.edt_cop_address_lines_two);
        edt_cop_address_lines_three  = findViewById(R.id.edt_cop_address_lines_three);
        edt_cus_contact_one = findViewById(R.id.edt_cop_mobiles_number);
        edt_cop_sec_mobile_number = findViewById(R.id.edt_cop_sec_mobile_number);
        edt_cus_cop_email_two = findViewById(R.id.edt_cus_cop_email_two);
        edt_cop_address_address_cus = findViewById(R.id.edt_cop_address_address_cus);
        edt_cop_address_line_two_cus =  findViewById(R.id.edt_cop_address_line_two_cus);
        edt_cop_address_lines_three =  findViewById(R.id.edt_cop_address_lines_three);
        edt_cop_contact_person_name = findViewById(R.id.edt_cop_contact_person_name);
        edt_cop_designation = findViewById(R.id.edt_cop_designation);

        edt_current_car_name = findViewById(R.id.edt_current_car_name);
        edt_model = findViewById(R.id.edt_model);
        edt_year_month = findViewById(R.id.edt_year_month);
        edt_req_number = findViewById(R.id.edt_req_number);

        edt_replace_current_car_name = findViewById(R.id.edt_replace_current_car_name);
        edt_replace_model = findViewById(R.id.edt_replace_model);
        edt_replace_year_month = findViewById(R.id.edt_replace_year_month);
        edt_replace_req_number = findViewById(R.id.edt_replace_req_number);
        edt_replace_expected_value = findViewById(R.id.edt_replace_expected_value);

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
        titlesList = new ArrayList<>();
        enquiryTypesLists = new ArrayList<>();
        modeLists = new ArrayList<>();
        sourcesLists = new ArrayList<>();
        professionsLists = new ArrayList<>();
        annualIncomesLists = new ArrayList<>();
        companyTypesLists = new ArrayList<>();
        modelsLists = new ArrayList<>();
        variantsLIsts = new ArrayList<>();
        interiorColorsLists = new ArrayList<>();
        exteriorColorsLists = new ArrayList<>();
        financePitchLists = new ArrayList<>();
        insuranceLists = new ArrayList<>();
        fuelLists = new ArrayList<>();
        vehicle_id =  MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.VEHICLEID,"");
        mode_id =  MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.MODE,"");


        if (MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.PROSPECTCATEGORY,"").equals("1")){
            txt_individual.setBackgroundResource(R.drawable.btn_line);
            txt_individual.setTextColor(Color.parseColor("#EB0A1E"));
            rel_personal_details.setVisibility(View.VISIBLE);
            rel_cop_details_title.setVisibility(View.GONE);
            isOnePressed = true;
        }else if (MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.PROSPECTCATEGORY,"").equals("2")){
            txt_corporate.setBackgroundResource(R.drawable.btn_line);
            txt_corporate.setTextColor(Color.parseColor("#EB0A1E"));
            rel_cop_details_title.setVisibility(View.VISIBLE);
            rel_personal_details.setVisibility(View.GONE);
            isSecondPlace = true;
        }

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
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdateEnquiryActivity.this, android.R.layout.simple_spinner_item, categories){
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
                            Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
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
                            Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
                            ((TextView) view).setTypeface(typeface);
                            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,10,0,0);
                            ((TextView) view).setLayoutParams(params);
                            ((TextView) view).setPadding(10,10,0,0);

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
                ArrayAdapter<String> dataAdapterone = new ArrayAdapter<String>(UpdateEnquiryActivity.this, android.R.layout.simple_spinner_item, categories){
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
                            Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
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
                            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                            Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
                            ((TextView) view).setTypeface(typeface);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(0,10,0,0);
                            ((TextView) view).setLayoutParams(params);
                            ((TextView) view).setPadding(10,10,0,0);
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

        edt_cus_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edt_cus_date.getRight() - edt_cus_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEnquiryActivity.this,
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
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });


        txt_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               if (!isOnePressed){
                     AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
                      builder.setTitle("USER MESSAGE");
                      builder.setMessage("The Prospect Category is not Individual");
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
               }else {
                    isOnePressed = true;
                txt_individual.setBackgroundResource(R.drawable.btn_line);
                txt_individual.setTextColor(Color.parseColor("#EB0A1E"));
                lin_individual.setVisibility(View.VISIBLE);
                rel_cop_details_title.setVisibility(View.GONE);
                img_personal_downs.setVisibility(View.GONE);
                img_personal_up.setVisibility(View.VISIBLE);
                rel_personal_details.setVisibility(View.VISIBLE);
                rel_personal_details.setBackgroundResource(R.color.intersted_color);
                prospect_category = "1";
                Customer_Gettitles();
                Enquiry_Create_Enquiry();
                Customer_Getmodes();
                Customer_Getprofessions();
                Customer_Getanualincome();
                   Vehicles_Getallvehicle();
                   Getfinancepitch();
                   Getinsurancepitch();
                   Customer_Get_Enquiry();

                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(UpdateEnquiryActivity.this);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                anim.setDuration(15000);
                anim.setInterpolator(new DecelerateInterpolator());
                progressDialog.show();
                if (isSecondPlace) {
                    txt_corporate.setBackgroundResource(R.drawable.btn_line_grey);
                    txt_corporate.setTextColor(Color.parseColor("#A7A7A7"));
                    lin_corporate.setVisibility(View.GONE);
                    rel_cop_details_title.setVisibility(View.GONE);
                    isSecondPlace = false;
                }
               }
            }
        });

        txt_corporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              if (!isSecondPlace){
                   AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
                       builder.setTitle("USER MESSAGE");
                       builder.setMessage("The Prospect Category is not Corporate");
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
              }else {
                txt_corporate.setBackgroundResource(R.drawable.btn_line);
                txt_corporate.setTextColor(Color.parseColor("#EB0A1E"));
                lin_corporate.setVisibility(View.VISIBLE);
                lin_individual.setVisibility(View.GONE);
                rel_cop_details_title.setVisibility(View.VISIBLE);
                rel_personal_details.setVisibility(View.GONE);
                Img_personal_details_cop_up.setVisibility(View.VISIBLE);
                Img_personal_details_cop_down.setVisibility(View.GONE);
                prospect_category = "2";
                Customer_Cop_Gettitles();
                Customer_Cop_Getmodes();
                Customer_Cop_Getanualincome();
                Customer_Cop_Getcompanytypes();
                Enquiry__Cop_Create_Enquiry();
                Customer_Get_Enquiry();
                  Vehicles_Getallvehicle();
                  Getfinancepitch();
                  Getinsurancepitch();
                isSecondPlace = true;
                if (isOnePressed) {
                    txt_individual.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_individual.setTextColor(Color.parseColor("#A7A7A7"));
                    lin_individual.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    rel_personal_details.setVisibility(View.GONE);
                    isOnePressed = false;
                }
              }
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
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_enquiry.setClickable(true);
                        txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_enquiry.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_enquiry.setClickable(true);
                        txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_enquiry.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_enquiry.setClickable(true);
                        txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_enquiry.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                        txt_update_enquiry.setClickable(true);
                        txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                        txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                validate_add_enquiery();
                            }
                        });
                    }else {
                        txt_update_enquiry.setBackgroundResource(R.drawable.shape_button_gray);
                        txt_update_enquiry.setClickable(true);
                        // userMessage = (String) jsonObj.get("userMessage");
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                    Customer_Gettitles();
                    Enquiry_Create_Enquiry();
                    Customer_Getmodes();
                    Customer_Getprofessions();
                    Customer_Getanualincome();
                    Vehicles_Getallvehicle();
                    Getfinancepitch();
                    Getinsurancepitch();
                    Customer_Get_Enquiry();

                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(UpdateEnquiryActivity.this);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                    ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                    anim.setDuration(15000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    progressDialog.show();

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
                    lin_corporate.startAnimation(animation);
                    Customer_Cop_Gettitles();
                    Customer_Cop_Getmodes();
                    Customer_Cop_Getanualincome();
                    Customer_Cop_Getcompanytypes();
                    Enquiry__Cop_Create_Enquiry();
                    Vehicles_Getallvehicle();
                    Getfinancepitch();
                    Getinsurancepitch();
                    Customer_Get_Enquiry();
                    demand_structure_type = "1";
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
                    lin_intrested_vehicle.startAnimation(animation);
                   /* nest_scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            nest_scroll_view.fullScroll(View.FOCUS_DOWN);
                        }
                    });*/
                   // Vehicles_Getallvehicle();
                    //Customer_Get_Enquiry();
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
                  /*  nest_scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            nest_scroll_view.fullScroll(View.FOCUS_DOWN);
                        }
                    });*/
                   // Getfinancepitch();
                  //  Getinsurancepitch();
                  //  Customer_Get_Enquiry();
                    if (swt_smiles.isChecked()){
                        smiles = "1";
                        //  Toast.makeText(UpdateEnquiryActivity.this, "1", Toast.LENGTH_SHORT).show();
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

        swt_address_proof_attached.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    address_proof_attached_status  = "1";
                } else {
                    // The toggle is disabled
                    address_proof_attached_status =  "0";
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateEnquiryActivity.this,
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


                        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEnquiryActivity.this,
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

        txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(String.valueOf(txt_count.getText()));
                quantity++;
                //  quantity + 1;
                QUANTITY =  Integer.toString(quantity);
                txt_count.setText(QUANTITY);
            }
        });

        txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(String.valueOf(txt_count.getText()));
                if (quantity == 1){
                    txt_count.setText("1");
                }else {
                    quantity-=1;
                    QUANTITY = Integer.toString(quantity);
                    txt_count.setText(QUANTITY);
                }
            }
        });


        edt_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Spinner click listener
        spinner_title.setOnItemSelectedListener(this);

        spinner_enquiry.setOnItemSelectedListener(this);

        spinner_mode.setOnItemSelectedListener(this);

        spinner_source.setOnItemSelectedListener(this);

        spinner_profession.setOnItemSelectedListener(this);

        spinner_annual_income.setOnItemSelectedListener(this);

        spinner_model_suffix_cop.setOnItemSelectedListener(this);

        spinner_model_variant.setOnItemSelectedListener(this);

        spinner_interior_color.setOnItemSelectedListener(this);

        spinner_exterEior_color.setOnItemSelectedListener(this);

        spinner_fuel_type.setOnItemSelectedListener(this);

        spinner_replace_fuel_type.setOnItemSelectedListener(this);

        spinner_finance_pitch.setOnItemSelectedListener(this);

        spinner_insurance_pitch.setOnItemSelectedListener(this);

        spinner_cop.setOnItemSelectedListener(this);

        spinner_mode_cop.setOnItemSelectedListener(this);

        spinner_source_cop.setOnItemSelectedListener(this);

        spinner_company_type_cop.setOnItemSelectedListener(this);

        spinner_annual_income_cop.setOnItemSelectedListener(this);

        spinner_enquiry_type_cop.setOnItemSelectedListener(this);

        thisYear = new SimpleDateFormat("yyyy").format(new Date());


        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");


    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValiddate(String year_month){
        String regEx = "^d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(year_month);
        return matcher.matches();
    }

    public void back(View view) {
        startActivity(new Intent(UpdateEnquiryActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // TODO Auto-generated method stub
        switch (parent.getId()) {

            case R.id.spinner_title:
                String selectedItemText = Integer.toString(position);
                //(String) parent.getItemAtPosition(position)+1;
                //Your Action Here.
                title_id = titlesList.get(position).getTitle_id();
                break;

            case R.id.spinner_enquiry_type:
                //  String selectedItemText = Integer.toString(position);
                //(String) parent.getItemAtPosition(position)+1;
                //Your Action Here.
                enquiry_id = enquiryTypesLists.get(position).getEnquiry_type_id();
                break;
            case R.id.spinner_mode:
                //Your Another Action Here.
                if (position == 0) {
                    mode_id = "1";
                    Customer_Getsources();
                } else {
                    mode_id = modeLists.get(position).getMode_id();
                    if (mode_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                        Customer_Getsources();
                    }
                }

                break;
            case R.id.spinner_source:
                //Your Another Action Here.
                source_id = sourcesLists.get(position).getSource_id();

                break;
            case R.id.spinner_profession:
                profession_id = professionsLists.get(position).getProfession_id();
                break;
            case R.id.spinner_annual_income:
                annual_income_id = annualIncomesLists.get(position).getAnnual_income_id();
                break;

            case R.id.spinner_model_suffix_cop:
                if (position == 0) {
                    vehicle_id = "1";
                    Customer_Get_Model_Variants();
                    //Vehiclecolormaster_Getallinteriorcolors();
                    // Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    vehicle_id = modelsLists.get(position).getId();
                    MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.VEHICLEID,vehicle_id);
                    if (vehicle_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                    Vehiclecolormaster_Getallinteriorcolors();
                    Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    model_variant = variantsLIsts.get(position).getId();
                    MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.MODEL_VARIANT,String.valueOf(position));
                    if (model_variant == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
             //   if (interior_color_id==null||interior_color_id.equals("")){
                    interior_color_id = interiorColorsLists.get(position).getInterior_color_id();
              //  }else {
                  //  spinner_interior_color.setSelection(Integer.parseInt(annualincomeid));
               // }
                break;

            case R.id.spinner_exterEior_color:
               // if (exteriorcolorid==null||exteriorcolorid.equals("")){
                    exterior_color_id = exteriorColorsLists.get(position).getExterior_color_id();
               // }else {
                   // spinner_exterEior_color.setSelection(Integer.parseInt(exteriorcolorid));
              //  }
                break;

            case R.id.spinner_fuel_type:
                current_car_fuel_type = Integer.toString(position);
                //Toast.makeText(UpdateEnquiryActivity.this, current_car_fuel_type, Toast.LENGTH_SHORT).show();
                break;

            case R.id.spinner_replace_fuel_type:
                current_car_fuel_type = Integer.toString(position);
                break;

            case R.id.spinner_finance_pitch:
                //if (financepitch==null||financepitch.equals("")){
                    finance_pitch_id = financePitchLists.get(position).getFinance_id();
                    if (finance_pitch_id.equals("2")){
                        lin_finance_pinch.setVisibility(View.VISIBLE);
                        finance_other = edt_finance_pinch_text.getText().toString();
                    }else {
                        lin_finance_pinch.setVisibility(View.GONE);
                        finance_other = "";
                    }
              //  }else {
                   // spinner_finance_pitch.setSelection(Integer.parseInt(financepitch));
                   // if (financeother.equals("2")){
                        //edt_finance_pinch_text.setText(financeother);
                       // lin_finance_pinch.setVisibility(View.VISIBLE);
                   // }
               // }

                break;

            case R.id.spinner_insurance_pitch:

               // if (insurancemasterid==null||insurancemasterid.equals("")){
                    insurance_master_id = insuranceLists.get(position).getInsurance_id();
                    if (insurance_master_id.equals("2")){
                        lin_insurance_pinch.setVisibility(View.VISIBLE);
                        insurance_other = edt_insurance_pinch_text.getText().toString();
                        //edt_insurance_pinch_text.getText().toString();
                    }else {
                        lin_insurance_pinch.setVisibility(View.GONE);
                        insurance_other = "";
                    }
              //  }else {
                   // spinner_insurance_pitch.setSelection(Integer.parseInt(insurancemasterid));
                   // if (insurancemasterid.equals("2")){
                    //    edt_insurance_pinch_text.setText(insuranceothers);
                     //   lin_insurance_pinch.setVisibility(View.VISIBLE);
                    //}
               // }

                break;

            case R.id.spinner_cop:
                //Your Action Here.
                title_id = titlesList.get(position).getTitle_id();
                break;

            case R.id.spinner_enquiry_type_cop:
                enquiry_id = enquiryTypesLists.get(position).getEnquiry_type_id();
                break;
            case R.id.spinner_mode_cop:
                //Your Another Action Here.
                if (position == 0){
                    mode_id = "1";
                    Customer_Cop_Getsources();
                }else {
                    mode_id = modeLists.get(position).getMode_id();
                    if (mode_id == null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
                        builder.setTitle("USER MESSAGE");
                        builder.setMessage("Please Select Mode First!!");
                        builder.setCancelable(true);
                        final AlertDialog closedialog= builder.create();
                        closedialog.show();

                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                closedialog.dismiss();
                                timer2.cancel(); //this will cancel the timer of the system
                            }
                        }, 3000); // the timer will count 5 seconds....
                    }else {
                        Customer_Cop_Getsources();
                    }
                }

                break;
            case R.id.spinner_source_cop:
                //Your Another Action Here.
                source_id = sourcesLists.get(position).getSource_id();

                break;
            case R.id.spinner_company_type_cop:
                company_type_id = companyTypesLists.get(position).getCompany_type_id();
                break;

            case R.id.spinner_annual_income_cop:
                annual_income_id = annualIncomesLists.get(position).getAnnual_income_id();
                break;

        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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
                Toast.makeText(UpdateEnquiryActivity.this, "", Toast.LENGTH_SHORT).show();
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

            if ((customer_name.equals("")||contact_number_one.equals(""))||(contact_number_one.toString().trim().length() < 10) || contact_number_one.isEmpty()) {
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            }  else if (mode_id.equals("") || vehicle_id.equals("")||demand_structure_type.equals("")|| Objects.equals(address, "")||(follow_up_time.equals(""))) {
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (current_car_maker.equals("")||current_car_model.equals("")||year_month.equals("")||(current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)
                    ||current_car_fuel_type.equals("")||thisYear.compareTo(year_month) < 0 || "2000".compareTo(year_month) > 0
                    ||(addressthree.toString().trim().length() < 6)){
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            }else {
                Customer_Update_Equiery();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(UpdateEnquiryActivity.this);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                anim.setDuration(15000);
                anim.setInterpolator(new DecelerateInterpolator());
                progressDialog.show();
            }
        }else if (prospect_category.equals("2")){

            company_name = edt_cop_cus_name.getText().toString();
            if (company_name.equals("") || company_name.isEmpty()) {
                edt_cop_cus_name.setError("Please Enter Company Name");
            }

            mobilenumber = edt_cop_mobile_number.getText().toString().trim();
            if (mobilenumber.isEmpty() || (mobilenumber.equals("")) || (mobilenumber.toString().trim().length() < 10)) {
                edt_cop_mobile_number.setError("Please Enter the Correct Phone Number");
            }

            additionalmobilenumber = edt_cus_contact_one.getText().toString().trim();
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


            String addressone = edt_cop_address.getText().toString().trim();
            if (addressone.equals("") || addressone.isEmpty()) {
                edt_cop_address.setError("Please Enter the Correct Address");
            }

            String addresstwo = edt_cop_address_lines_two.getText().toString().trim();
            if (addresstwo.equals("") || addresstwo.isEmpty()) {
                edt_cop_address_lines_two.setError("Please Enter the Correct Address");
            }

            String addressthree = edt_cop_address_lines_three.getText().toString().trim();
            if (addressthree.equals("") || addressthree.isEmpty()||addressthree.toString().trim().length() < 6) {
                edt_cop_address_lines_three.setError("Please Enter the Correct Address");
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
                    edt_req_number.setError("Reg Number should not less than 15 char ");
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

            email = edt_cus_cop_email_one.getText().toString().trim();

            if (!isValidEmail(email)) {
                edt_cus_cop_email_one.setError("Please Enter the Correct Email Id");
            }

            if ((company_name.equals("")||mobilenumber.toString().trim().length() < 10) || mobilenumber.isEmpty()||(!isValidEmail(email))) {
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (mode_id.equals("") || vehicle_id.equals("")||demand_structure_type.equals("")|| address.equals("")||(addressthree.toString().trim().length() < 6)) {
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            } else if (current_car_maker.equals("")||current_car_model.equals("")||year_month.equals("")||(current_car_reg_number.toString().trim().length() < 5 || current_car_reg_number.toString().trim().length() > 10)
                    ||current_car_fuel_type.equals("")||thisYear.compareTo(year_month) < 0 || "2000".compareTo(year_month) > 0 ||(follow_up_time.equals(""))){
                Toast.makeText(UpdateEnquiryActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
            }else {
                Customer_Cop_Update_Equiery();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(UpdateEnquiryActivity.this);
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


        // Api call Customer_Gettitles
    public void Customer_Gettitles () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_gettitles_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        titlesList.clear();
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("titles");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                EnquiryTitle titles = new EnquiryTitle();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setTitle_id(jsonObject_titles.getString("title_id"));
                                titles.setTitle(jsonObject_titles.getString("title"));
                                titlesList.add(titles);

                                // Populate spinner with country names
                              //  worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            EnquiryTitleTypeArrayAdapter adapter = new EnquiryTitleTypeArrayAdapter(UpdateEnquiryActivity.this, R.layout.layout_enquiry_type,
                                    titlesList);
                            spinner_title.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
    public void Enquiry_Create_Enquiry() {
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_createenquiry_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            enquiryTypesLists.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("enquiry_types");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                EnquiryTypesList titles = new EnquiryTypesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setEnquiry_type_id(jsonObject_titles.getString("enquiry_type_id"));
                                titles.setEnquiry_type(jsonObject_titles.getString("enquiry_type"));
                                enquiryTypesLists.add(titles);

                                // Populate spinner with country names
                                //   worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            EnquiryTypesArrayAdapter adapter = new EnquiryTypesArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_enquiry, enquiryTypesLists);
                            spinner_enquiry.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_Getmodes
    public void Customer_Getmodes () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getmodes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // progressDialog.dismiss();
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("mode");
                            modeLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ModeList modeList = new ModeList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setMode_id(jsonObject_titles.getString("mode_id"));
                                modeList.setMode(jsonObject_titles.getString("mode"));

                                modeLists.add(modeList);
                            }

                            ModeTypeArrayAdapter adapter = new ModeTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_mode_type, modeLists);
                            spinner_mode.setAdapter(adapter);
                            try {
                            spinner_mode.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.MODE,"")));
                            } catch(NumberFormatException ex){ // handle your exception

                            }



                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_Getsources
    public void Customer_Getsources () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getsources_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("sources");
                            sourcesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                SourcesList modeList = new SourcesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setSource_id(jsonObject_titles.getString("source_id"));
                                modeList.setSource(jsonObject_titles.getString("source"));

                                sourcesLists.add(modeList);
                            }

                            SourceTypeArrayAdapter adapter = new SourceTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_source_type, sourcesLists);
                            spinner_source.setAdapter(adapter);
                            spinner_source.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.SPINNERSOURCEID,"")));


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                params.put("MODE_ID", mode_id);
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

    // Api call Customer_Getprofessions
    public void Customer_Getprofessions () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getprofessions_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // progressDialog.dismiss();
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("professions");
                            professionsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ProfessionsList modeList = new ProfessionsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setProfession_id(jsonObject_titles.getString("profession_id"));
                                modeList.setProfession(jsonObject_titles.getString("profession"));

                                professionsLists.add(modeList);
                            }

                            ProfessionTypeArrayAdapter adapter = new ProfessionTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_profession_type, professionsLists);

                            spinner_profession.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_Getanualincome
    public void Customer_Getanualincome () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getanualincome_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                 //   progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("annual_incomes");
                            annualIncomesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                AnnualIncomesList modeList = new AnnualIncomesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setAnnual_income_id(jsonObject_titles.getString("annual_income_id"));
                                modeList.setAnnual_income(jsonObject_titles.getString("annual_income"));

                                annualIncomesLists.add(modeList);
                            }

                            AnnualIncomeTypeArrayAdapter adapter = new AnnualIncomeTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_annualincome_type, annualIncomesLists);

                            spinner_annual_income.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Vehicles_Getallvehicle
    public void Vehicles_Getallvehicle () {
        StringRequest requests = new StringRequest(Request.Method.POST, vehicles_getallvehicle_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
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
                                //modeList.setModel_suffix(jsonObject_titles.getString("model_suffix"));
                                modelsLists.add(modeList);
                            }
                            ModelsTypeArrayAdapter adapter = new ModelsTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_model_type, modelsLists);
                            spinner_model_suffix_cop.setAdapter(adapter);
                            spinner_model_suffix_cop.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.VEHICLEID,"")));

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            ModelVariantTypeArrayAdapter adapter = new ModelVariantTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_variant_type, variantsLIsts);
                            spinner_model_variant.setAdapter(adapter);
                            try{
                                spinner_model_variant.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.MODEL_VARIANT,"")));

                            } catch(NumberFormatException ex){ // handle your exception

                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            adapter = new InteriorColorTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_model_type, interiorColorsLists);

                            spinner_interior_color.setAdapter(adapter);
                            try{
                                spinner_interior_color.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.INTERIOR,"")));
                            } catch(NumberFormatException ex){ // handle your exception

                            }

                        } else if (status_code.equals("0")) {
                           // interiorColorsLists.clear();
                           // adapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            exadapter = new ExteriorColorArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_exteriorcolor_type, exteriorColorsLists);

                            spinner_exterEior_color.setAdapter(exadapter);
                            try{
                                spinner_exterEior_color.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.EXTEREIOR,"")));
                            } catch(NumberFormatException ex){ // handle your exception

                            }

                        } else if (status_code.equals("0")) {
                            //exteriorColorsLists.clear();
                            //exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            FinancePitchTypeArrayAdapter adapter = new FinancePitchTypeArrayAdapter(UpdateEnquiryActivity.this, R.layout.layout_title_type,
                                    financePitchLists);
                            spinner_finance_pitch.setAdapter(adapter);
                           // spinner_finance_pitch.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.FINANCEPITCHID,"")));

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            InsurancePitchTypeArrayAdapter adapter = new InsurancePitchTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_enquiry, insuranceLists);
                            spinner_insurance_pitch.setAdapter(adapter);
                          //  spinner_insurance_pitch.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.INSURANCEPITCHID,"")));

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_cop_Gettitles
    public void Customer_Cop_Gettitles () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_gettitles_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            titlesList.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("titles");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                EnquiryTitle titles = new EnquiryTitle();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setTitle_id(jsonObject_titles.getString("title_id"));
                                titles.setTitle(jsonObject_titles.getString("title"));
                                titlesList.add(titles);
                            }

                            EnquiryTitleTypeArrayAdapter adapter = new EnquiryTitleTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_title_type, titlesList);
                            spinner_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    public void Enquiry__Cop_Create_Enquiry(){
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_createenquiry_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            enquiryTypesLists.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("enquiry_types");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                EnquiryTypesList titles = new EnquiryTypesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setEnquiry_type_id(jsonObject_titles.getString("enquiry_type_id"));
                                titles.setEnquiry_type(jsonObject_titles.getString("enquiry_type"));
                                enquiryTypesLists.add(titles);

                                // Populate spinner with country names
                                //   worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            EnquiryTypesArrayAdapter adapter = new EnquiryTypesArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_enquiry, enquiryTypesLists);
                            spinner_enquiry_type_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_cop_Getmodes
    public void Customer_Cop_Getmodes () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getmodes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("mode");
                            modeLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ModeList modeList = new ModeList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setMode_id(jsonObject_titles.getString("mode_id"));
                                modeList.setMode(jsonObject_titles.getString("mode"));

                                modeLists.add(modeList);
                            }

                            ModeTypeArrayAdapter adapter = new ModeTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_mode_type, modeLists);
                            spinner_mode_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_cop_Getsources
    public void Customer_Cop_Getsources () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getsources_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("sources");
                            sourcesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                SourcesList modeList = new SourcesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setSource_id(jsonObject_titles.getString("source_id"));
                                modeList.setSource(jsonObject_titles.getString("source"));

                                sourcesLists.add(modeList);
                            }

                            SourceTypeArrayAdapter adapter = new SourceTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_source_type, sourcesLists);
                            spinner_source_cop.setAdapter(adapter);
                            try {
                                spinner_source_cop.setSelection(Integer.parseInt(MyFunctions.getSharedPrefs(UpdateEnquiryActivity.this,Constants.SPINNERSOURCEID,"")));
                           } catch(NumberFormatException ex){ // handle your exception

                        }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                params.put("MODE_ID", mode_id);
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

    // Api call Customer_Getanualincome
    public void Customer_Cop_Getanualincome () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getanualincome_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("annual_incomes");
                            annualIncomesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                AnnualIncomesList modeList = new AnnualIncomesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setAnnual_income_id(jsonObject_titles.getString("annual_income_id"));
                                modeList.setAnnual_income(jsonObject_titles.getString("annual_income"));

                                annualIncomesLists.add(modeList);
                            }

                            AnnualIncomeTypeArrayAdapter adapter = new AnnualIncomeTypeArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_annualincome_type, annualIncomesLists);

                            spinner_annual_income_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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

    // Api call Customer_Getcompanytypes
    public void Customer_Cop_Getcompanytypes () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcompanytypes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
//                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {

                            JSONArray jsonArray_titles = jsonObj.getJSONArray("company_types");
                            companyTypesLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                CompanyTypesList modeList = new CompanyTypesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setCompany_type_id(jsonObject_titles.getString("company_type_id"));
                                modeList.setCompany_type(jsonObject_titles.getString("company_type"));
                                companyTypesLists.add(modeList);
                            }
                            CompanyTypesArrayAdapter adapter = new CompanyTypesArrayAdapter(UpdateEnquiryActivity.this,
                                    R.layout.layout_company_type, companyTypesLists);

                            spinner_company_type_cop.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                                spinner_enquiry.setSelection(Integer.parseInt(enquirytype));
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

                            if (jsonObject.isNull("MODE_ID")){
                                modeid = "";
                            }else {
                                modeid = jsonObject.getString("MODE_ID");
                                if (prospect_category.equals("1")){
                                    spinner_mode.setSelection(Integer.parseInt(modeid));
                                }else if(prospect_category.equals("2")){
                                    spinner_mode_cop.setSelection(Integer.parseInt(modeid));
                                }

                            }

                            if (jsonObject.isNull("SOURCE_ID")){
                                sourceid = "";
                            }else {
                                sourceid = jsonObject.getString("SOURCE_ID");
                                if (prospect_category.equals("1")){
                                  //  spinner_source.setSelection(Integer.parseInt(sourceid));
                                  //  spinner_source_cop.setSelection(Integer.parseInt(sourceid));
                                }else if(prospect_category.equals("2")){
                                   String wsourceid = jsonObject.getString("SOURCE_ID");
                                 //   spinner_source_cop.setSelection(Integer.parseInt(wsourceid));
                                }

                            }

                            if (jsonObject.isNull("TITLE_ID")){
                                titleid = "";
                            }else {
                                titleid = jsonObject.getString("TITLE_ID");
                                if (prospect_category.equals("1")){
                                    spinner_title.setSelection(Integer.parseInt(titleid));
                                }else if(prospect_category.equals("2")){
                                    spinner_cop.setSelection(Integer.parseInt(titleid));
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
                                if (prospect_category.equals("1")){
                                    edt_cus_email.setText(getemail);
                                }else if (prospect_category.equals("2")){
                                    getemail = jsonObject.getString("EMAIL");
                                    edt_cus_cop_email_one.setText(getemail);
                                }

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

                            if (jsonObject.isNull("PROFESSION_ID")){
                                professionid = "";
                            }else {
                                professionid = jsonObject.getString("PROFESSION_ID");
                                if (prospect_category.equals("1")){
                                    spinner_profession.setSelection(Integer.parseInt(professionid));
                                }else if(prospect_category.equals("2")){
                                   // spinner_cop.setSelection(Integer.parseInt(modeid));
                                }


                            }

                            if (jsonObject.isNull("SUB_OCCUPATION")){
                                suboccupation = "";
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
                                    edt_cop_address.setText(addressone);
                                    String addresstwo = a[1];
                                    edt_cop_address_lines_two.setText(addresstwo);
                                    String addressthree = a[2];
                                    edt_cop_address_lines_three.setText(addressthree);
                                }
                            }

                            if (jsonObject.isNull("ANNUAL_INCOME_ID")){
                                annualincomeid = "";
                            }else {
                                annualincomeid = jsonObject.getString("ANNUAL_INCOME_ID");
                                if (prospect_category.equals("1")){
                                    spinner_annual_income.setSelection(Integer.parseInt(annualincomeid));
                                }else if(prospect_category.equals("2")){
                                     spinner_annual_income_cop.setSelection(Integer.parseInt(annualincomeid));
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
                                spinner_company_type_cop.setSelection(Integer.parseInt(companytypeid));
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_NAME")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_NAME");
                                edt_cop_contact_person_name.setText(contactpersonname);
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_EMAIL")){
                                contactpersonname = "";
                            }else {
                                String cone = jsonObject.getString("CONTACT_PERSON_EMAIL");
                                edt_cop_contact_person_name.setText(cone);
                            }

                            if (jsonObject.isNull("CONTACT_PERSON_ADDRESS")){
                                contactpersonname = "";
                            }else {
                                contactpersonname = jsonObject.getString("CONTACT_PERSON_EMAIL");
                                edt_cop_contact_person_name.setText(contactpersonname);
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
                                try {
                                    edt_cop_sec_mobile_number.setText(additionalmobilenumber);
                                }catch (Exception e){

                                }
                            }

                            if (jsonObject.isNull("DESIGNATION")){
                                getdesignation = "";
                            }else {
                                designation = jsonObject.getString("DESIGNATION");
                                edt_cop_designation.setText(designation);
                            }


                            JSONObject demand_and_structure = jsonArray_titles.getJSONObject("demand_and_structure");

                            if (demand_and_structure.isNull("TYPE")){
                                type = "";
                            }else {
                                type = demand_and_structure.getString("TYPE");
                                if (type.equals("1")){
                                    isfirstPressed = true;
                                    txt_first.setBackgroundResource(R.drawable.btn_line);
                                    txt_first.setTextColor(Color.parseColor("#EB0A1E"));
                                    demand_structure_type = "1";
                                    lin_additional.setVisibility(View.GONE);
                                    lin_replace.setVisibility(View.GONE);
                                }else if (type.equals("2")){
                                    isadditionPlace = true;
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
                                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdateEnquiryActivity.this, android.R.layout.simple_spinner_item, categories){
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
                                                Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
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
                                                Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
                                                ((TextView) view).setTypeface(typeface);
                                                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                params.setMargins(0,10,0,0);
                                                ((TextView) view).setLayoutParams(params);
                                                ((TextView) view).setPadding(10,10,0,0);

                                            }
                                            return view;
                                        }
                                    };
                                    // Drop down layout style - list view with radio button
                                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // attaching data adapter to spinner
                                    spinner_fuel_type.setAdapter(dataAdapter);
                                    spinner_fuel_type.setPrompt("Fuel Type");
                                }else if (type.equals("3")){
                                    isfirstPressed = false;
                                    isreplacePlace = true;
                                   isadditionPlace = false;
                                    txt_first.setBackgroundResource(0);
                                    txt_first.setTextColor(Color.parseColor("#58595B"));
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
                                    ArrayAdapter<String> dataAdapterone = new ArrayAdapter<String>(UpdateEnquiryActivity.this, android.R.layout.simple_spinner_item, categories){
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
                                                Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
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
                                                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                                                Typeface typeface = ResourcesCompat.getFont(UpdateEnquiryActivity.this, R.font.poppins);
                                                ((TextView) view).setTypeface(typeface);
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                params.setMargins(0,10,0,0);
                                                ((TextView) view).setLayoutParams(params);
                                                ((TextView) view).setPadding(10,10,0,0);
                                            }
                                            return view;
                                        }
                                    };
                                    // Drop down layout style - list view with radio button

                                    dataAdapterone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    // attaching data adapter to spinner
                                    spinner_replace_fuel_type.setAdapter(dataAdapterone);
                                    spinner_replace_fuel_type.setPrompt("Fuel Type");
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

                            if (demand_and_structure.isNull("FUEL_TYPE")){
                                fuel_type = "";
                            }else {
                                fuel_type = demand_and_structure.getString("FUEL_TYPE");
                                if (type.equals("2")){
                                    spinner_fuel_type.setSelection(Integer.parseInt(fuel_type));
                                }else {
                                    spinner_replace_fuel_type.setSelection(Integer.parseInt(fuel_type));
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
                                    spinner_replace_fuel_type.setSelection(Integer.parseInt(fuel_type));
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

                            if (intrested_vehicle.isNull("VEHICLE_ID")){
                                intrestedvehicleid = "";
                            }else {
                                intrestedvehicleid = intrested_vehicle.getString("VEHICLE_ID");
                                spinner_model_suffix_cop.setSelection(Integer.parseInt(intrestedvehicleid));
                            }

                            if (intrested_vehicle.isNull("VEHICLE_INTERIOR_COLOR_ID")){
                                interiorcolorid = "";
                            }else {
                                interiorcolorid = intrested_vehicle.getString("VEHICLE_INTERIOR_COLOR_ID");
                            }

                            if (intrested_vehicle.isNull("VEHICLE_EXTERIOR_COLOR_ID")){
                                exteriorcolorid = "";
                            }else {
                                exteriorcolorid = intrested_vehicle.getString("VEHICLE_EXTERIOR_COLOR_ID");
                            }

                            if (intrested_vehicle.isNull("QTY")){
                                getquantity = "";
                            }else {
                                getquantity = intrested_vehicle.getString("QTY");
                                txt_count.setText(getquantity);
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
                                    spinner_finance_pitch.setSelection(Integer.parseInt(financepitch));
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
                                    spinner_insurance_pitch.setSelection(Integer.parseInt(insurancemasterid));
                                }
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
                                    isCallPressed = true;
                                    if (img_personal_up.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                                            && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ) {
                                        txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                                        txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                                        isDirectPlace = true;
                                        if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")) {
                                            txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                                            txt_update_enquiry.setClickable(true);
                                            txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                                            txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    validate_add_enquiery();
                                                }
                                            });
                                        }
                                    }  else if (rel_cop_details_title.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                                            && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ) {
                                        follow_up_type = "1";
                                        txt_telephone.setBackgroundResource(R.drawable.btn_line);
                                        txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                                        isCallPressed = true;
                                        if (!prospect_category.equals("") || !enquiry_id.equals("") || !mode_id.equals("") || !source_id.equals("") || !title_id.equals("") || !customer_name.equals("") || !contact_number_one.equals("") || !contact_number_two.equals("") || !profession_id.equals("") || !sub_occupation.equals("") || !address.equals("") || !annual_income_id.equals("") || !email.equals("") || !dob.equals("") || !demand_structure_type.equals("") || !current_car_maker.equals("") || !current_car_model.equals("") || !current_car_fuel_type.equals("") || !year_month.equals("") || !current_car_reg_number.equals("") || !current_car_expected_value.equals("") || !intrested_in_utrust_status.equals("") || !product_demo_given.equals("") || !brochure_given.equals("") || !quotation_given.equals("") || !extended_warranty.equals("") || !smiles.equals("") || !accessories_pitch.equals("") || !notify_customer.equals("") || !finance_pitch_id.equals("") || !insurance_master_id.equals("") || !finance_other.equals("") || !insurance_other.equals("") || !follow_up_date.equals("") || !follow_up_time.equals("") || !follow_up_type.equals("") || !follow_up_remark.equals("")) {
                                            txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                                            txt_update_enquiry.setClickable(true);
                                            txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                                            txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    validate_add_enquiery();
                                                }
                                            });
                                        }
                                    }
                                }else if (type.equals("2")){
                                    follow_up_type = "2";
                                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                                    txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                                    isDirectPlace = true;
                                    if (img_personal_up.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                                            && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ) {

                                        if(!prospect_category.equals("")||!enquiry_id.equals("")||!mode_id.equals("")||!source_id.equals("")||!title_id.equals("")||!customer_name.equals("")||!contact_number_one.equals("")||!contact_number_two.equals("")||!profession_id.equals("")||!sub_occupation.equals("")||!address.equals("")||!annual_income_id.equals("")||!email.equals("")||!dob.equals("")||!demand_structure_type.equals("")||!current_car_maker.equals("")||!current_car_model.equals("")||!current_car_fuel_type.equals("")||!year_month.equals("")||!current_car_reg_number.equals("")||!current_car_expected_value.equals("")||!intrested_in_utrust_status.equals("")||!product_demo_given.equals("")||!brochure_given.equals("")||!quotation_given.equals("")||!extended_warranty.equals("")||!smiles.equals("")||!accessories_pitch.equals("")||!notify_customer.equals("")||!finance_pitch_id.equals("")||!insurance_master_id.equals("")||!finance_other.equals("")||!insurance_other.equals("")||!follow_up_date.equals("")||!follow_up_time.equals("")||!follow_up_type.equals("")||!follow_up_remark.equals("")) {
                                            txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                                            txt_update_enquiry.setClickable(true);
                                            txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                                            txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    validate_add_enquiery();
                                                }
                                            });
                                        }
                                    }  else if (rel_cop_details_title.getVisibility()==View.VISIBLE && img_demand_structure_up.getVisibility()==View.VISIBLE
                                            && img_intrested_vehicle_up.getVisibility()==View.VISIBLE && img_value_chain_up.getVisibility()==View.VISIBLE ) {
                                        follow_up_type = "1";
                                        txt_telephone.setBackgroundResource(R.drawable.btn_line);
                                        txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                                        isCallPressed = true;
                                        if (!prospect_category.equals("") || !enquiry_id.equals("") || !mode_id.equals("") || !source_id.equals("") || !title_id.equals("") || !customer_name.equals("") || !contact_number_one.equals("") || !contact_number_two.equals("") || !profession_id.equals("") || !sub_occupation.equals("") || !address.equals("") || !annual_income_id.equals("") || !email.equals("") || !dob.equals("") || !demand_structure_type.equals("") || !current_car_maker.equals("") || !current_car_model.equals("") || !current_car_fuel_type.equals("") || !year_month.equals("") || !current_car_reg_number.equals("") || !current_car_expected_value.equals("") || !intrested_in_utrust_status.equals("") || !product_demo_given.equals("") || !brochure_given.equals("") || !quotation_given.equals("") || !extended_warranty.equals("") || !smiles.equals("") || !accessories_pitch.equals("") || !notify_customer.equals("") || !finance_pitch_id.equals("") || !insurance_master_id.equals("") || !finance_other.equals("") || !insurance_other.equals("") || !follow_up_date.equals("") || !follow_up_time.equals("") || !follow_up_type.equals("") || !follow_up_remark.equals("")) {
                                            txt_update_enquiry.setBackgroundResource(R.drawable.shape_text_button);
                                            txt_update_enquiry.setClickable(true);
                                            txt_update_enquiry.setTextColor(Color.parseColor("#FFFFFF"));
                                            txt_update_enquiry.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    validate_add_enquiery();
                                                }
                                            });
                                        }
                                    }

                                }
                            }

                            if (next_followup.isNull("REMARKS")){
                                remark = "";
                            }else {
                                remark = next_followup.getString("REMARKS");
                                edt_remark.setText(remark);
                            }


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObj.getString("enquiry_status");

                            JSONObject jsonObject = jsonObj.getJSONObject("enquiry_details");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            startActivity(new Intent(UpdateEnquiryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                params.put("ENQUIRY_TYPE", enquirytype);
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
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("QUANTITY", QUANTITY);
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
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CONTACT_ID,contact_id);
                            JSONObject jsonObject = jsonObj.getJSONObject("enquiry_details");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(UpdateEnquiryActivity.this,Constants.FOLLOWUPID,followupid);
                            startActivity(new Intent(UpdateEnquiryActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateEnquiryActivity.this);
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
                params.put("ENQUIRY_TYPE", enquirytype);
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
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("QUANTITY", QUANTITY);
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
}
