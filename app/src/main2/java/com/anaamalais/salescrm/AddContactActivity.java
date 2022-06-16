package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Observable;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Config;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.anaamalais.salescrm.Adapter.AnnualIncomeTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.CompanyTypesArrayAdapter;
import com.anaamalais.salescrm.Adapter.ExteriorColorArrayAdapter;
import com.anaamalais.salescrm.Adapter.InteriorColorTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModeTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelVariantTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ModelsTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.ProfessionTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.SourceTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.TitleTypeArrayAdapter;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.AnnualIncomesList;
import com.anaamalais.salescrm.List.CompanyTypesList;
import com.anaamalais.salescrm.List.ExteriorColorsList;
import com.anaamalais.salescrm.List.InteriorColorsList;
import com.anaamalais.salescrm.List.ModeList;
import com.anaamalais.salescrm.List.ModelsList;
import com.anaamalais.salescrm.List.ProfessionsList;
import com.anaamalais.salescrm.List.SourcesList;
import com.anaamalais.salescrm.List.Titles;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
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
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    InteriorColorTypeArrayAdapter adapter;
    ExteriorColorArrayAdapter exadapter;
    public static final String TAG_NAME = "Name";
    ArrayList<String> worldlist;
    TextView txt_individual, txt_corporate, txt_minus, txt_plus, txt_count, txt_add_contact, txt_update_contact;
    Boolean isOnePressed = true, isSecondPlace = false;
    TextInputEditText edt_cus_name, edt_cus_one_number, edt_cus_two_number, edt_cus_email,
            edt_cus_sub_occupation, edt_address_line_one, edt_address_line_two, edt_address_line_three, edt_cop_cus_name, edt_cop_contact_person_name, edt_cop_designation, edt_cop_mobile_number,
            edt_cop_address_address, edt_cop_address_line_two, edt_cop_address_line_three, edt_cop_sec_mobile_number;
    EditText edt_cus_date;
    NestedScrollView nest_scroll_view;
    LinearLayout lin_individual, lin_corporate, lin_intrested_vehicle;
    ImageView Img_personal_details_up, Img_personal_details_down, Img_down_intrested, Img_up_intrested,
            Img_personal_details_cop_up, Img_personal_details_cop_down;
    private Data data;
    RelativeLayout lin_personal_details,rel_add_cont, lin_personal_intrested_toyota_vehicle, rel_cop_details_title, rel_nes_test;
    Spinner spinner, spinner_mode, spinner_source, spinner_profession, spinner_cop, spinner_mode_cop, spinner_source_cop, spinner_company_type_cop,
            spinner_annual_income, spinner_annual_income_cop, spinner_model_suffix_cop, spinner_exterEior_color,
            spinner_interior_color , spinner_model_variant;
    int quantity;
    String QUANTITY = "1";
    String add_update;
    TextView txt_title;
    Animation animation;
    String title_id, mode_id, profession_id, source_id, annual_income_id, company_type_id, vehicle_id,model_variant, interior_color_id, exterior_color_id, contact_status, contact_id;
    String prospect_category, customer_name, contact_number_one, contact_number_two, sub_occupation,
            address, email, dob, company_name, contact_person_name, designation, mobilenumber, additionalmobilenumber;

    List<Titles> titlesList;
    List<ModeList> modeLists;
    List<SourcesList> sourcesLists;
    List<ProfessionsList> professionsLists;
    List<AnnualIncomesList> annualIncomesLists;  
    List<CompanyTypesList> companyTypesLists;
    List<ModelsList> modelsLists;
    List<VariantsLIst> variantsLIsts;
    List<InteriorColorsList> interiorColorsLists;
    List<ExteriorColorsList> exteriorColorsLists;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN, UPDATE_CONTACT_ID;
    String customer_gettitles_url = BASE_URL + "customer/gettitles";
    String customer_getmodes_url = BASE_URL + "customer/getmodes";
    String customer_getsources_url = BASE_URL + "customer/getsources";
    String customer_getprofessions_url = BASE_URL + "customer/getprofessions";
    String customer_getanualincome_url = BASE_URL + "customer/getanualincome";
    String customer_getcompanytypes_url = BASE_URL + "customer/getcompanytypes";
    String vehicles_getallvehicle_url = BASE_URL + "customer/getmodels";
    String customer_getmodelvariants_url = BASE_URL + "customer/getmodelvariants";
    String vehiclecolormaster_getallinteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String vehiclecolormaster_getallexteriorcolors_url = BASE_URL + "vehiclecolors/getvehiclecolors";
    String customer_createcontact = BASE_URL + "customer/createcontact";
    String customer_getcontact = BASE_URL + "customer/getcontact";
    String customer_updatecontact = BASE_URL + "customer/updatecontact";
    int mYear, mDay, mMonth, mMinute, mHour;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = AddContactActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(AddContactActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(AddContactActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(AddContactActivity.this, Constants.TOKEN, "");
        rel_add_cont = findViewById(R.id.rel_add_cont);
        txt_title = findViewById(R.id.txt_title);
        txt_individual = findViewById(R.id.txt_individual);
        txt_corporate = findViewById(R.id.txt_corporate);
        lin_intrested_vehicle = findViewById(R.id.lin_intrested_vehicle);
        rel_cop_details_title = findViewById(R.id.rel_cop_details_title);
        lin_individual = findViewById(R.id.lin_individual);
        lin_corporate = findViewById(R.id.lin_corporate);
        txt_add_contact = findViewById(R.id.txt_add_contact);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner_mode = (Spinner) findViewById(R.id.spinner_mode);
        spinner_source = (Spinner) findViewById(R.id.spinner_source);
        spinner_profession = (Spinner) findViewById(R.id.spinner_profession);
        spinner_annual_income = (Spinner) findViewById(R.id.spinner_annual_income);
        // txt_update_contact = findViewById(R.id.txt_update_contact);
        // add_update = getIntent().getStringExtra("ADD_UPDATE");

        spinner_cop = (Spinner) findViewById(R.id.spinner_cop);
        spinner_mode_cop = (Spinner) findViewById(R.id.spinner_mode_cop);
        spinner_source_cop = (Spinner) findViewById(R.id.spinner_source_cop);
        spinner_company_type_cop = (Spinner) findViewById(R.id.spinner_company_type_cop);
        spinner_annual_income_cop = (Spinner) findViewById(R.id.spinner_annual_income_cop);

        spinner_model_suffix_cop = (Spinner) findViewById(R.id.spinner_model_suffix_cop);
        spinner_model_variant = (Spinner) findViewById(R.id.spinner_model_variant);
        spinner_exterEior_color = (Spinner) findViewById(R.id.spinner_exterEior_color);
        spinner_interior_color = (Spinner) findViewById(R.id.spinner_interior_color);

        lin_personal_details = findViewById(R.id.lin_personal_details);
        lin_personal_intrested_toyota_vehicle = findViewById(R.id.lin_intrested_toyota_vehicle);
        Img_personal_details_up = findViewById(R.id.img_up);
        Img_personal_details_down = findViewById(R.id.img_down);
        Img_up_intrested = findViewById(R.id.img_up_intrested);
        Img_down_intrested = findViewById(R.id.img_down_intrested);
        rel_nes_test = findViewById(R.id.rel_nes_test);
        edt_cus_name = findViewById(R.id.edt_cus_name);
        edt_cus_one_number = findViewById(R.id.edt_cus_one_number);
        edt_cus_two_number = findViewById(R.id.edt_cus_two_number);
        edt_cus_email = findViewById(R.id.edt_cus_email);
        edt_cus_sub_occupation = findViewById(R.id.edt_cus_sub_occupation);
        edt_cus_date = findViewById(R.id.edt_cus_date);
        edt_address_line_one = findViewById(R.id.edt_address_line_one);
        edt_address_line_two = findViewById(R.id.edt_address_line_two);
        edt_address_line_three = findViewById(R.id.edt_address_line_three);

        edt_cop_cus_name = findViewById(R.id.edt_cop_cus_name);
        edt_cop_contact_person_name = findViewById(R.id.edt_cop_contact_person_name);
        edt_cop_designation = findViewById(R.id.edt_cop_designation);
        edt_cop_mobile_number = findViewById(R.id.edt_cop_mobile_number);
        edt_cop_address_address = findViewById(R.id.edt_cop_address_address);
        edt_cop_address_line_two = findViewById(R.id.edt_cop_address_line_two);
        edt_cop_address_line_three = findViewById(R.id.edt_cop_address_line_three);
        edt_cop_sec_mobile_number = findViewById(R.id.edt_cop_sec_mobile_number);
        txt_update_contact = findViewById(R.id.txt_update_contact);

        txt_minus = findViewById(R.id.txt_minus);
        txt_plus = findViewById(R.id.txt_plus);
        txt_count = findViewById(R.id.txt_count);
        nest_scroll_view = findViewById(R.id.nest_scroll_view);
        Img_personal_details_cop_up = findViewById(R.id.img_cop_up);
        Img_personal_details_cop_down = findViewById(R.id.img_cop_down);
        spinner.setPrompt("Title");
        // nest_scroll_view = findViewById(R.id.nest_scroll_view);
        titlesList = new ArrayList<>();
        modeLists = new ArrayList<>();
        sourcesLists = new ArrayList<>();
        professionsLists = new ArrayList<>();
        annualIncomesLists = new ArrayList<>();
        companyTypesLists = new ArrayList<>();
        modelsLists = new ArrayList<>();
        variantsLIsts = new ArrayList<>();
        interiorColorsLists = new ArrayList<>();
        exteriorColorsLists = new ArrayList<>();

        // Create an array to populate the spinner
        worldlist = new ArrayList<String>();
        worldlist.add("Title");

        if (isOnePressed = true){
            prospect_category = "1";
        }else if (isSecondPlace = true){
            prospect_category = "2";
        }
        lin_personal_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img_personal_details_down.getVisibility() == View.VISIBLE) {
                    Img_personal_details_down.setVisibility(View.GONE);
                    Img_personal_details_up.setVisibility(View.VISIBLE);
                    lin_individual.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slipe_open_down);
                    lin_individual.startAnimation(animation);
                    Customer_Gettitles();
                    Customer_Getmodes();
                    Customer_Getprofessions();
                    Customer_Getanualincome();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(AddContactActivity.this);
                    progressDialog.getWindow().setGravity(Gravity.CENTER);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                    ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                    anim.setDuration(15000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    progressDialog.show();
                } else if (Img_personal_details_up.getVisibility() == View.VISIBLE) {
                    Img_personal_details_down.setVisibility(View.VISIBLE);
                    Img_personal_details_up.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_close_up);
                    lin_individual.startAnimation(animation);
                }
            }
        });

        rel_cop_details_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img_personal_details_cop_down.getVisibility() == View.VISIBLE) {
                    Img_personal_details_cop_down.setVisibility(View.GONE);
                    Img_personal_details_cop_up.setVisibility(View.VISIBLE);
                    lin_corporate.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slipe_open_down);
                    lin_corporate.startAnimation(animation);

                } else if (Img_personal_details_cop_up.getVisibility() == View.VISIBLE) {
                    Img_personal_details_cop_down.setVisibility(View.VISIBLE);
                    Img_personal_details_cop_up.setVisibility(View.GONE);
                    lin_corporate.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_close_up);
                    lin_corporate.startAnimation(animation);
                }
            }
        });


        lin_personal_intrested_toyota_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img_down_intrested.getVisibility() == View.VISIBLE) {
                    Img_down_intrested.setVisibility(View.GONE);
                    Img_up_intrested.setVisibility(View.VISIBLE);
                    lin_intrested_vehicle.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slipe_open_down);
                    lin_intrested_vehicle.startAnimation(animation);
                    nest_scroll_view.post(new Runnable() {
                        @Override
                        public void run() {
                            nest_scroll_view.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    Vehicles_Getallvehicle();
                } else if (Img_up_intrested.getVisibility() == View.VISIBLE) {
                    Img_down_intrested.setVisibility(View.VISIBLE);
                    Img_up_intrested.setVisibility(View.GONE);
                    lin_intrested_vehicle.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_close_up);
                    lin_intrested_vehicle.startAnimation(animation);
                }
            }
        });


        txt_individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                if (isOnePressed = true) {
                    prospect_category = "1";
                }
                // Toast.makeText(AddContactActivity.this, prospect_category, Toast.LENGTH_SHORT).show();
                txt_individual.setBackgroundResource(R.drawable.btn_line);
                txt_individual.setTextColor(Color.parseColor("#EB0A1E"));
                lin_individual.setVisibility(View.VISIBLE);
                lin_corporate.setVisibility(View.GONE);
                lin_personal_details.setVisibility(View.VISIBLE);
                rel_cop_details_title.setVisibility(View.GONE);
                Img_personal_details_up.setVisibility(View.VISIBLE);
                Img_personal_details_down.setVisibility(View.GONE);
                Customer_Gettitles();
                Customer_Getmodes();
                Customer_Getprofessions();
                Customer_Getanualincome();
                //initialize the progress dialog and show it
                progressDialog = new ProgressDialog(AddContactActivity.this);
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
        });

        txt_corporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isSecondPlace = true;
                prospect_category = "2";

                txt_corporate.setBackgroundResource(R.drawable.btn_line);
                txt_corporate.setTextColor(Color.parseColor("#EB0A1E"));
                lin_corporate.setVisibility(View.VISIBLE);
                lin_individual.setVisibility(View.GONE);
                rel_cop_details_title.setVisibility(View.VISIBLE);
                lin_personal_details.setVisibility(View.GONE);
                Img_personal_details_cop_up.setVisibility(View.VISIBLE);
                Img_personal_details_cop_down.setVisibility(View.GONE);
                Customer_Cop_Gettitles();
                Customer_Cop_Getmodes();
                Customer_Cop_Getanualincome();
                Customer_Cop_Getcompanytypes();
                if (isOnePressed) {
                    txt_individual.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_individual.setTextColor(Color.parseColor("#A7A7A7"));
                    lin_individual.setVisibility(View.GONE);
                    lin_personal_details.setVisibility(View.GONE);
                    isOnePressed = false;
                }
            }
        });

        txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(String.valueOf(txt_count.getText()));
                quantity++;
                //  quantity + 1;
                QUANTITY = Integer.toString(quantity);
                txt_count.setText(QUANTITY);

                //display(quantity);
            }
        });

        txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = Integer.parseInt(String.valueOf(txt_count.getText()));
                if (quantity == 1) {
                    txt_count.setText("1");
                } else {
                    quantity -= 1;
                    QUANTITY = Integer.toString(quantity);
                    txt_count.setText(QUANTITY);
                }
                //quantity - 1;

                //display(quantity);
            }
        });

        edt_cus_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (edt_cus_date.getRight() - edt_cus_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddContactActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy", dtDob);
                                        edt_cus_date.setText(strDate);
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

        closeKeyBoard();
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        spinner_mode.setOnItemSelectedListener(this);

        spinner_source.setOnItemSelectedListener(this);

        spinner_profession.setOnItemSelectedListener(this);

        spinner_annual_income.setOnItemSelectedListener(this);

        spinner_cop.setOnItemSelectedListener(this);

        spinner_mode_cop.setOnItemSelectedListener(this);

        spinner_source_cop.setOnItemSelectedListener(this);

        spinner_company_type_cop.setOnItemSelectedListener(this);

        spinner_annual_income_cop.setOnItemSelectedListener(this);

        spinner_model_suffix_cop.setOnItemSelectedListener(this);

        spinner_model_variant.setOnItemSelectedListener(this);

        spinner_interior_color.setOnItemSelectedListener(this);

        spinner_exterEior_color.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        // TODO Auto-generated method stub
        switch (parent.getId()) {
            case R.id.spinner:
                String selectedItemText = Integer.toString(position);
                //(String) parent.getItemAtPosition(position)+1;
                //Your Action Here.
                title_id = titlesList.get(position).getTitle_id();
                // spinner.setSelection(Integer.parseInt("3"));
                break;
            case R.id.spinner_mode:
                //Your Another Action Here.
                if (position == 0) {
                    mode_id = "1";
                    Customer_Getsources();
                } else {
                    mode_id = modeLists.get(position).getMode_id();
                    if (mode_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.SPINNERSOURCEID,String.valueOf(position));
                break;
            case R.id.spinner_profession:

                profession_id = professionsLists.get(position).getProfession_id();
                break;
            case R.id.spinner_annual_income:
                annual_income_id = annualIncomesLists.get(position).getAnnual_income_id();
                break;

            case R.id.spinner_cop:
                //Your Action Here.
                title_id = titlesList.get(position).getTitle_id();
                break;
            case R.id.spinner_mode_cop:
                //Your Another Action Here.
                if (position == 0) {
                    mode_id = "1";
                    Customer_Cop_Getsources();
                } else {
                    mode_id = modeLists.get(position).getMode_id();
                    if (mode_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                        Customer_Cop_Getsources();
                    }
                }
                break;
            case R.id.spinner_source_cop:
                source_id = sourcesLists.get(position).getSource_id();
                MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.SPINNERSOURCEID,String.valueOf(position));
                break;
            case R.id.spinner_company_type_cop:
                company_type_id = companyTypesLists.get(position).getCompany_type_id();
                break;

            case R.id.spinner_annual_income_cop:
                annual_income_id = annualIncomesLists.get(position).getAnnual_income_id();
                break;

            case R.id.spinner_model_suffix_cop:
                if (position == 0) {
                    vehicle_id = "1";
                   // Customer_Get_Model_Variants();
                    //Vehiclecolormaster_Getallinteriorcolors();
                   // Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    vehicle_id = modelsLists.get(position).getId();
                    MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.VEHICLEID,vehicle_id);
                    if (vehicle_id == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                  //  Vehiclecolormaster_Getallinteriorcolors();
                   // Vehiclecolormaster_Getallexteriorcolors();
                } else {
                    model_variant = variantsLIsts.get(position).getId();
                    MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.MODEL_VARIANT,String.valueOf(position));
                    if (model_variant == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                interior_color_id = interiorColorsLists.get(position).getInterior_color_id();
                MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.INTERIOR,String.valueOf(position));
                break;

            case R.id.spinner_exterEior_color:
                exterior_color_id = exteriorColorsLists.get(position).getExterior_color_id();
                MyFunctions.setSharedPrefs(AddContactActivity.this,Constants.EXTEREIOR,String.valueOf(position));
                break;
        }

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void display(int number) {
        String count = Integer.toString(number);
        txt_count.setText(count);
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void back(View view) {
        startActivity(new Intent(AddContactActivity.this, HomeActivity.class));
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

    public void addcontact(View view) {
            if (Img_up_intrested.getVisibility()==View.VISIBLE){
                if (prospect_category.equals("1")) {
                MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.PROSPECTCATEGORY, prospect_category);
                customer_name = edt_cus_name.getText().toString();
                if (customer_name.equals("") || customer_name.isEmpty()) {
                    edt_cus_name.setError("Please Enter Customer Name");
                }

                contact_number_one = edt_cus_one_number.getText().toString().trim();
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

                if (QUANTITY == null) {
                    QUANTITY = "0";
                }

                String cusdob = edt_cus_date.getText().toString().trim();
                if (cusdob.equals("") || cusdob.isEmpty()) {
                    //edt_cus_email.setError("Please Selecte Dob");
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
                    //edt_cus_sub_occupation.setError("Please Enter the Correct Sub Occupation");
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
                if (addressthree.equals("") || addressthree.isEmpty() || addressthree.toString().trim().length() < 6) {
                    edt_address_line_three.setError("Please Enter the Correct Address");
                }
                address = addressone + "" + "\t" + addresstwo + "\t" + addressthree;
                if (mode_id.equals("1")) {
                    // get selected item value
                    TextView viewById = spinner_mode.getSelectedView().findViewById(R.id.offer_mode_txt);
                    viewById.setTextColor(Color.RED);
                    viewById.setError("Field Required");
                    viewById.setText("Field Required");//changes the selected item text to this
                    //errorTextview.spinner_profession.getSelectedView();
                    // errorTextview.setError("Your Error Message here");
                }

                if (profession_id.equals("")) {
                    // get selected item value
                    TextView viewById = spinner_profession.getSelectedView().findViewById(R.id.offer_profession_txt);
                    viewById.setTextColor(Color.RED);
                    viewById.setError("Field Required");
                    viewById.setText("Field Required");//changes the selected item text to this
                    //errorTextview.spinner_profession.getSelectedView();
                    // errorTextview.setError("Your Error Message here");
                }

                if (Img_up_intrested.getVisibility() == View.VISIBLE) {
                    if (vehicle_id.equals("1")) {
                        // get selected item value
                        TextView viewById1 = spinner_model_suffix_cop.getSelectedView().findViewById(R.id.offer_model_txt);
                        viewById1.setTextColor(Color.RED);
                        viewById1.setError("Field Required");
                        viewById1.setText("Field Required");//changes the selected item text to this
                        //errorTextview.spinner_profession.getSelectedView();
                        // errorTextview.setError("Your Error Message here");
                    }
                }

                if (customer_name.equals("") || (contact_number_one.equals("")) || (contact_number_one.toString().trim().length() < 10) || contact_number_one.isEmpty() || (!isValidEmail(email))) {
                    Toast.makeText(AddContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();

                } else if (mode_id.equals("") || profession_id.equals("") || vehicle_id.equals("") || address.equals("") || addressthree.toString().trim().length() < 6) {
                    Toast.makeText(AddContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
                } else {
                    Customer_Createcontact();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(AddContactActivity.this);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progress_dailog));
                    ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
                    anim.setDuration(15000);
                    anim.setInterpolator(new DecelerateInterpolator());
                    progressDialog.show();
                }
            } else if (prospect_category.equals("2")) {

                MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.PROSPECTCATEGORY, prospect_category);
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
                    //edt_cop_sec_mobile_number.setError("Please Enter the Correct Phone Number");
                }

                contact_person_name = edt_cop_contact_person_name.getText().toString().trim();
                if (contact_person_name.isEmpty() || (contact_person_name.equals(""))) {
                    // edt_cop_contact_person_name.setError("Please Enter the Correct Contact Person Name");
                }

                designation = edt_cop_designation.getText().toString().trim();
                if (designation.equals("") || designation.isEmpty()) {
                    edt_cop_designation.setError("Please Enter the Correct Designation");
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
                if (addressthree.equals("") || addressthree.isEmpty() || addressthree.toString().trim().length() < 6) {
                    edt_cop_address_line_three.setError("Please Enter the Correct Address");
                }
                address = addressone + "" + "\t" + addresstwo + "\t" + addressthree;

                if (mode_id.equals("1")) {
                    // get selected item value
                    TextView viewById = spinner_mode_cop.getSelectedView().findViewById(R.id.offer_mode_txt);
                    viewById.setTextColor(Color.RED);
                    viewById.setError("Field Required");
                    viewById.setText("Field Required");//changes the selected item text to this
                    //errorTextview.spinner_profession.getSelectedView();
                    // errorTextview.setError("Your Error Message here");
                }

                if (company_type_id.equals("")) {
                    // get selected item value
                    TextView viewById = spinner_company_type_cop.getSelectedView().findViewById(R.id.offer_company_txt);
                    viewById.setTextColor(Color.RED);
                    viewById.setError("Field Required");
                    viewById.setText("Field Required");//changes the selected item text to this
                    //errorTextview.spinner_profession.getSelectedView();
                    // errorTextview.setError("Your Error Message here");
                }


                if (Img_up_intrested.getVisibility() == View.VISIBLE) {
                    if (vehicle_id.equals("1")) {
                        // get selected item value
                        TextView viewById = spinner_model_suffix_cop.getSelectedView().findViewById(R.id.offer_model_txt);
                        viewById.setTextColor(Color.RED);
                        viewById.setError("Field Required");
                        viewById.setText("Field Required");//changes the selected item text to this
                        //errorTextview.spinner_profession.getSelectedView();
                        // errorTextview.setError("Your Error Message here");
                    }
                }


                if ((company_name.equals("") || (mobilenumber.toString().trim().length() < 10) || mobilenumber.isEmpty())) {
                    Toast.makeText(AddContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
                } else if (mode_id.equals("") || (address.equals("")) || addressthree.toString().trim().length() < 6) {
                    Toast.makeText(AddContactActivity.this, "Fill all the fields!!", Toast.LENGTH_SHORT).show();
                } else {
                    Customer_Cop_Createcontact();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(AddContactActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
                builder.setTitle("USER MESSAGE");
                builder.setMessage("Fill All The Fields");
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

    // Api call Customer_Gettitles
    public void Customer_Gettitles() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_gettitles_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            titlesList.clear();
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("titles");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                Titles titles = new Titles();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setTitle_id(jsonObject_titles.getString("title_id"));
                                titles.setTitle(jsonObject_titles.getString("title"));
                                titlesList.add(titles);

                                // Populate spinner with country names
                                worldlist.add(jsonObject_titles.getString("title"));
                                //  spinner.
                                // titlesList.add(jsonObject_titles.getString(Constants.EMP_NAME));
                            }

                            // Spinner adapter
                            TitleTypeArrayAdapter adapter = new TitleTypeArrayAdapter(AddContactActivity.this, R.layout.layout_title_type,
                                    titlesList);
                            spinner.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                                    if (msg.equals("Invalid API_TOKEN!")) {
                                        MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(AddContactActivity.this, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(AddContactActivity.this, Constants.DEVICEID, "")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Getmodes() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getmodes_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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

                            ModeTypeArrayAdapter adapter = new ModeTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_mode_type, modeLists);

                            spinner_mode.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Getsources() {
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

                            SourceTypeArrayAdapter adapter = new SourceTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_source_type, sourcesLists);
                            spinner_source.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Getprofessions() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getprofessions_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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

                            ProfessionTypeArrayAdapter adapter = new ProfessionTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_profession_type, professionsLists);

                            spinner_profession.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Getanualincome() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getanualincome_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
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

                            AnnualIncomeTypeArrayAdapter adapter = new AnnualIncomeTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_annualincome_type, annualIncomesLists);

                            spinner_annual_income.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Cop_Gettitles() {
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
                                Titles titles = new Titles();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                titles.setTitle_id(jsonObject_titles.getString("title_id"));
                                titles.setTitle(jsonObject_titles.getString("title"));
                                titlesList.add(titles);
                            }

                            TitleTypeArrayAdapter adapter = new TitleTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_title_type, titlesList);

                            spinner_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Cop_Getmodes() {
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

                            ModeTypeArrayAdapter adapter = new ModeTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_mode_type, modeLists);

                            spinner_mode_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Cop_Getsources() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getsources_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        sourcesLists.clear();
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("sources");
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                SourcesList modeList = new SourcesList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                modeList.setSource_id(jsonObject_titles.getString("source_id"));
                                modeList.setSource(jsonObject_titles.getString("source"));

                                sourcesLists.add(modeList);
                            }

                            SourceTypeArrayAdapter adapter = new SourceTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_source_type, sourcesLists);

                            spinner_source_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Cop_Getanualincome() {
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

                            AnnualIncomeTypeArrayAdapter adapter = new AnnualIncomeTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_annualincome_type, annualIncomesLists);

                            spinner_annual_income_cop.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Customer_Cop_Getcompanytypes() {
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
                            CompanyTypesArrayAdapter adapter = new CompanyTypesArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_company_type, companyTypesLists);

                            spinner_company_type_cop.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Vehicles_Getallvehicle() {
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
                            ModelsTypeArrayAdapter adapter = new ModelsTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_model_type, modelsLists);

                            spinner_model_suffix_cop.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            ModelVariantTypeArrayAdapter adapter = new ModelVariantTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_variant_type, variantsLIsts);

                            spinner_model_variant.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
    public void Vehiclecolormaster_Getallinteriorcolors() {

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
                            adapter = new InteriorColorTypeArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_model_type, interiorColorsLists);

                            spinner_interior_color.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            //  interiorColorsLists.clear();
                            //  adapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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

    // Api call Vehiclecolormaster_Getallexteriorcolors
    public void Vehiclecolormaster_Getallexteriorcolors() {
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
                            exadapter = new ExteriorColorArrayAdapter(AddContactActivity.this,
                                    R.layout.layout_exteriorcolor_type, exteriorColorsLists);
                            spinner_exterEior_color.setAdapter(exadapter);

                        } else if (status_code.equals("0")) {
                            // exteriorColorsLists.clear();
                            // exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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

    public void Customer_Createcontact() {

        StringRequest requests = new StringRequest(Request.Method.POST, customer_createcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            contact_id = jsonObj.getString("contact_id");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CONTACT_ID, contact_id);
                            contact_status = jsonObj.getString("contact_status");
                            JSONObject jsonObject = jsonObj.getJSONObject("contact_details");
                            String cus_name = (String) jsonObject.get("name");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_NAME, cus_name);
                            String cus_phone = (String) jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_PHONE, cus_phone);
                            String cus_address = (String) jsonObject.get("address");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_ADDRESS, cus_address);
                            startActivity(new Intent(AddContactActivity.this, CustomerDetailsActivity.class).putExtra("Status", contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            exteriorColorsLists.clear();
                            exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("QUANTITY", QUANTITY);

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

    public void Customer_Cop_Createcontact() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_createcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            contact_id = jsonObj.getString("contact_id");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CONTACT_ID, contact_id);
                            contact_status = jsonObj.getString("contact_status");
                            JSONObject jsonObject = jsonObj.getJSONObject("contact_details");
                            String cus_name = (String) jsonObject.get("name");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_NAME, cus_name);
                            String cus_phone = (String) jsonObject.get("phone");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_PHONE, cus_phone);
                            String cus_address = (String) jsonObject.get("address");
                            MyFunctions.setSharedPrefs(AddContactActivity.this, Constants.CUSTOMER_ADDRESS, cus_address);
                            startActivity(new Intent(AddContactActivity.this, CustomerDetailsActivity.class).putExtra("Status", contact_status));
                            finish();
                        } else if (status_code.equals("0")) {
                            exteriorColorsLists.clear();
                            exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
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
                params.put("MODEL_ID", vehicle_id);
                params.put("MODEL_VARIANT_ID", model_variant);
                params.put("INTERIOR_COLOR_ID", interior_color_id);
                params.put("EXTERIOR_COLOR_ID", exterior_color_id);
                params.put("QUANTITY", QUANTITY);

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
