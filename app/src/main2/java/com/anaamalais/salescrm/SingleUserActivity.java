package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.Adapter.FollowUpBasedFiltersAdapter;
import com.anaamalais.salescrm.Adapter.ModelBasedFiltersAdapter;
import com.anaamalais.salescrm.Adapter.SourceTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.StatusBasedFiltersAdapter;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.ExteriorColorsList;
import com.anaamalais.salescrm.List.FiltersList;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SingleUserActivity extends AppCompatActivity {
    String status_based = "[status_based][\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\"\"8\"\"9\"\"10\"\"11\"\"12\"\"13\"\"14\"]";
    TextView txt_clear , txt_apply;
    EditText searchView;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView img_group_fliter;
    String dashboard_getallcontact = BASE_URL + "dashboard/getalldetails";
    String dashboard_getallfilters = BASE_URL + "dashboard/getallfilters";
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    LinearLayout lin_user_all_list , lin_filters_status;
    List<AllContactsList> allContactsLists;
    List<FiltersList> filtersLists;
    String  status_code , msg ,token, API_TOKEN;
    Boolean isallcontactPressed = true, iscontactPlace = false , isenquiryPlace = false;
    TextView txt_all , txt_contact , txt_enquiry;
    RecyclerView rv_allcontact , rv_filters , rv_enquiry ,  rv_model_based , rv_followup_type;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = SingleUserActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(SingleUserActivity.this, R.color.white));
        API_TOKEN = MyFunctions.getSharedPrefs(SingleUserActivity.this, Constants.TOKEN, "");
        requestQueue = Volley.newRequestQueue(SingleUserActivity.this);
        txt_all = findViewById(R.id.txt_all);
        txt_contact = findViewById(R.id.txt_contact);
        txt_enquiry = findViewById(R.id.txt_enquiry);
        txt_clear = findViewById(R.id.txt_clear);
        txt_apply = findViewById(R.id.txt_apply);
        searchView = findViewById(R.id.ET_city);
        img_group_fliter = findViewById(R.id.img_group_fliter);
        lin_user_all_list = findViewById(R.id.lin_user_all_list);
        lin_filters_status = findViewById(R.id.lin_filters_status);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_allcontact = findViewById(R.id.rv_allcontact);
        rv_filters = findViewById(R.id.rv_filters);
        rv_model_based = findViewById(R.id.rv_model_based);
        rv_followup_type = findViewById(R.id.rv_followup_type);
        rv_allcontact.setHasFixedSize(true);
        rv_allcontact.setLayoutManager(new LinearLayoutManager(SingleUserActivity.this));
        rv_filters.setHasFixedSize(true);
        rv_filters.setLayoutManager(new LinearLayoutManager(SingleUserActivity.this));
        rv_model_based.setHasFixedSize(true);
        rv_model_based.setLayoutManager(new LinearLayoutManager(SingleUserActivity.this));
        rv_followup_type.setHasFixedSize(true);
        rv_followup_type.setLayoutManager(new LinearLayoutManager(SingleUserActivity.this));
        allContactsLists = new ArrayList<>();
        filtersLists = new ArrayList<>();

        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                txt_all.setBackgroundResource(R.drawable.shape_red_all);
                txt_all.setTextColor(Color.parseColor("#FFFFFF"));
                rv_allcontact.setVisibility(View.VISIBLE);
                rv_enquiry.setVisibility(View.GONE);
                //  prideliverystatus = txt_follow_up.getText().toString().trim();
                Customer_Get_All_Contact();
                isallcontactPressed = true;
                if (iscontactPlace) {
                    txt_contact.setBackgroundResource(R.drawable.shape_follow);
                    txt_contact.setTextColor(Color.parseColor("#2F3044"));
                    iscontactPlace = false;
                } else if (isenquiryPlace) {
                    txt_enquiry.setBackgroundResource(R.drawable.shape_follow);
                    txt_enquiry.setTextColor(Color.parseColor("#2F3044"));
                    isenquiryPlace = false;
                }
            }
        });
       // searchView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());

            }


        });



        img_group_fliter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_user_all_list.setVisibility(View.GONE);
                Dashboard_Get_All_Filters_status();
                Dashboard_Get_All_Filters_Model();
                Dashboard_Get_All_Filters_FollowUp_Type();
            }
        });

        txt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer_Get_All_Contact_Filters();
            }
        });

        Customer_Get_All_Contact();

    }

    public void back(View view) {
        startActivity(new Intent(SingleUserActivity.this,HomeActivity.class));
        finish();
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        List<AllContactsList>filterdNames = new ArrayList<>();

        //looping through existing elements
        for (AllContactsList s : allContactsLists) {
            //if the existing elements contains the search input
            if (s.getName().contains(text)||s.getStatus().contains(text)) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        rv_allcontact.setAdapter(new AllContactAdapter(SingleUserActivity.this, filterdNames));


    }



    public void custview(View view) {
        startActivity(new Intent(SingleUserActivity.this,CustomerViewActivity.class));
        finish();
    }

    public void addcontact(View view) {
        startActivity(new Intent(SingleUserActivity.this,AddContactActivity.class));
      //  finish();
    }

    public void addenquiry(View view) {
        startActivity(new Intent(SingleUserActivity.this,AddContactActivity.class));
       // finish();
    }

    // Api call Customer_cop_Getsources
    public void Customer_Get_All_Contact () {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getallcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // on below line we are making the
                    // recycler view visibility visible.
                    rv_allcontact.setVisibility(View.VISIBLE);


                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("data");
                           // JSONArray jsonArray_intrested_vehicle= jsonObj.getJSONArray("intrested_vehicle");
                            allContactsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                AllContactsList modeList = new AllContactsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);

                                if (jsonObject_titles.isNull("name")){
                                   // System.out.println("NAME"+"xnxnx");

                                }else {
                                    modeList.setName(jsonObject_titles.getString("name"));
                                }

                                if (jsonObject_titles.isNull("phone")){
                                 //   System.out.println("NAME"+jsonObject_titles.getString("phone"));

                                }else {
                                    modeList.setPhone(jsonObject_titles.getString("phone"));
                                }

                                if (jsonObject_titles.isNull("address")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("address"));

                                }else {
                                    modeList.setAddress(jsonObject_titles.getString("address"));
                                }

                                if (jsonObject_titles.isNull("model_and_suffix")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("model_and_suffix"));

                                }else {
                                    modeList.setModel_and_suffix(jsonObject_titles.getString("model_and_suffix"));
                                }

                                if (jsonObject_titles.isNull("pre_delivery_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("pre_delivery_id"));

                                }else {
                                    modeList.setPre_delivery_id(jsonObject_titles.getString("pre_delivery_id"));
                                }

                                if (jsonObject_titles.isNull("accessories_fitment_status")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("accessories_fitment_status"));

                                }else {
                                    modeList.setAccessories_fitment_status(jsonObject_titles.getString("accessories_fitment_status"));
                                }

                                if (jsonObject_titles.isNull("af_waiting_reason")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("af_waiting_reason"));

                                }else {
                                    modeList.setAf_waiting_reason(jsonObject_titles.getString("af_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("extended_warranty")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("extended_warranty"));

                                }else {
                                    modeList.setExtended_warranty(jsonObject_titles.getString("extended_warranty"));
                                }

                                if (jsonObject_titles.isNull("fastag")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("fastag"));

                                }else {
                                    modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("rto_waiting_reason")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("rto_waiting_reason"));

                                }else {
                                    modeList.setRto_waiting_reason(jsonObject_titles.getString("rto_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("preferred_rto_date")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("preferred_rto_date"));

                                }else {
                                    modeList.setPreferred_rto_date(jsonObject_titles.getString("preferred_rto_date"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                    modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                    modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
                                }

                                if (jsonObject_titles.isNull("contact_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("contact_id"));

                                }else {
                                    modeList.setContact_id(jsonObject_titles.getString("contact_id"));
                                }

                                if (jsonObject_titles.isNull("status_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("status_id"));

                                }else {
                                    modeList.setStatus_id(jsonObject_titles.getString("status_id"));
                                }

                                if (jsonObject_titles.isNull("status")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("status"));

                                }else {
                                    modeList.setStatus(jsonObject_titles.getString("status"));
                                }

                                if (jsonObject_titles.isNull("follow_up_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("follow_up_id"));

                                }else {
                                    modeList.setFollow_up_id(jsonObject_titles.getString("follow_up_id"));
                                }

                                if (jsonObject_titles.isNull("follow_up_date")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("follow_up_date"));

                                }else {
                                    modeList.setFollow_up_date(jsonObject_titles.getString("follow_up_date"));
                                }

                                if (jsonObject_titles.isNull("follow_up_time")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("follow_up_time"));

                                }else {
                                    modeList.setFollow_up_time(jsonObject_titles.getString("follow_up_time"));
                                }

                                if (jsonObject_titles.isNull("follow_up_type")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("follow_up_type"));

                                }else {
                                    modeList.setFollow_up_type(jsonObject_titles.getString("follow_up_type"));
                                }

                                if (jsonObject_titles.isNull("vehicle_alloted")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("vehicle_alloted"));

                                }else {
                                    modeList.setVehicle_alloted(jsonObject_titles.getString("vehicle_alloted"));
                                }

                                if (jsonObject_titles.isNull("vehicle_not_alloted_reason")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("vehicle_not_alloted_reason"));

                                }else {
                                    modeList.setVehicle_not_alloted_reason(jsonObject_titles.getString("vehicle_not_alloted_reason"));
                                }

                                if (jsonObject_titles.isNull("paper_documents_collected")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("paper_documents_collected"));

                                }else {
                                    modeList.setPaper_documents_collected(jsonObject_titles.getString("paper_documents_collected"));
                                }

                                if (jsonObject_titles.isNull("login_completed")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("login_completed"));

                                }else {
                                    modeList.setLogin_completed(jsonObject_titles.getString("login_completed"));
                                }

                                if (jsonObject_titles.isNull("login_completed_date")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("login_completed_date"));

                                }else {
                                    modeList.setLogin_completed_date(jsonObject_titles.getString("login_completed_date"));
                                }

                                if (jsonObject_titles.isNull("query_clearance")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("query_clearance"));

                                }else {
                                    modeList.setQuery_clearance(jsonObject_titles.getString("query_clearance"));
                                }

                                if (jsonObject_titles.isNull("loan_amount")){
                                 //   System.out.println("NAME"+jsonObject_titles.getString("loan_amount"));

                                }else {
                                    modeList.setLoan_amount(jsonObject_titles.getString("loan_amount"));
                                }

                                if (jsonObject_titles.isNull("ex_showroom_price")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("ex_showroom_price"));

                                }else {
                                    modeList.setEx_showroom_price(jsonObject_titles.getString("ex_showroom_price"));
                                }

                                if (jsonObject_titles.isNull("tcs")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("tcs"));

                                }else {
                                    modeList.setTcs(jsonObject_titles.getString("tcs"));
                                }

                                if (jsonObject_titles.isNull("tcs_amount")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("tcs_amount"));

                                }else {
                                    modeList.setTcs_amount(jsonObject_titles.getString("tcs_amount"));
                                }

                                if (jsonObject_titles.isNull("road_tax")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("road_tax"));

                                }else {
                                    modeList.setTcs_amount(jsonObject_titles.getString("road_tax"));
                                }

                                if (jsonObject_titles.isNull("insurance")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("insurance"));

                                }else {
                                    modeList.setInsurance(jsonObject_titles.getString("insurance"));
                                }

                                if (jsonObject_titles.isNull("insurance")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("registration"));

                                }else {
                                    modeList.setRegistration(jsonObject_titles.getString("registration"));
                                }

                                if (jsonObject_titles.isNull("accessories")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("accessories"));

                                }else {
                                    modeList.setAccessories(jsonObject_titles.getString("accessories"));
                                }

                                if (jsonObject_titles.isNull("warranty")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("warranty"));

                                }else {
                                    modeList.setWarranty(jsonObject_titles.getString("warranty"));
                                }

                                if (jsonObject_titles.isNull("fastag")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("fastag"));

                                }else {
                                    modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("offers")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("offers"));

                                }else {
                                    modeList.setOffers(jsonObject_titles.getString("offers"));
                                }

                                if (jsonObject_titles.isNull("net_amount")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("net_amount"));

                                }else {
                                    modeList.setNet_amount(jsonObject_titles.getString("net_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                    modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                    modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_date")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_date"));

                                }else {
                                    modeList.setExpected_downpayment_date(jsonObject_titles.getString("expected_downpayment_date"));
                                }

                                if (jsonObject_titles.isNull("expected_invoice_date")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("expected_invoice_date"));

                                }else {
                                    modeList.setExpected_invoice_date(jsonObject_titles.getString("expected_invoice_date"));
                                }

                                if (jsonObject_titles.isNull("booking_followup_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("booking_followup_id"));

                                }else {
                                    modeList.setBooking_followup_id(jsonObject_titles.getString("booking_followup_id"));
                                }

                                if (jsonObject_titles.isNull("fifteenth_day_followup_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("fifteenth_day_followup_id"));

                                }else {
                                    modeList.setFifteenth_day_followup_id(jsonObject_titles.getString("fifteenth_day_followup_id"));
                                }

                                if (jsonObject_titles.isNull("comment")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("comment"));

                                }else {
                                    modeList.setComment(jsonObject_titles.getString("comment"));
                                }

                                if (jsonObject_titles.isNull("booking_completed_id")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("booking_completed_id"));

                                }else {
                                    modeList.setComment(jsonObject_titles.getString("booking_completed_id"));
                                }

                                if (jsonObject_titles.isNull("booking_completed_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("booking_completed_id"));

                                }else {
                                    modeList.setBooking_completed_id(jsonObject_titles.getString("booking_completed_id"));
                                }

                                if (jsonObject_titles.isNull("booking_model_and_suffix")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("booking_model_and_suffix"));

                                }else {
                                    modeList.setBooking_model_and_suffix(jsonObject_titles.getString("booking_model_and_suffix"));
                                }

                                if (jsonObject_titles.isNull("interior_color")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("interior_color"));

                                }else {
                                    modeList.setInterior_color(jsonObject_titles.getString("interior_color"));
                                }

                                if (jsonObject_titles.isNull("interior_color")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("exterior_color"));

                                }else {
                                    modeList.setExterior_color(jsonObject_titles.getString("exterior_color"));
                                }

                                if (jsonObject_titles.isNull("booking_amount")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("booking_amount"));

                                }else {
                                    modeList.setBooking_amount(jsonObject_titles.getString("booking_amount"));
                                }

                                if (jsonObject_titles.isNull("booking_date")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("booking_date"));

                                }else {
                                    modeList.setBooking_date(jsonObject_titles.getString("booking_date"));
                                }

                                if (jsonObject_titles.isNull("booking_time")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("booking_time"));

                                }else {
                                    modeList.setBooking_time(jsonObject_titles.getString("booking_time"));
                                }


                                if (jsonObject_titles.isNull("payment_mode")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("payment_mode"));

                                }else {
                                    modeList.setPayment_mode(jsonObject_titles.getString("payment_mode"));
                                }


                                if (jsonObject_titles.isNull("reason")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("reason"));

                                }else {
                                    modeList.setReason(jsonObject_titles.getString("reason"));
                                }

                                if (jsonObject_titles.isNull("post_sales_followup_id")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("post_sales_followup_id"));

                                }else {
                                    modeList.setPost_sales_followup_id(jsonObject_titles.getString("post_sales_followup_id"));
                                }

                                if (jsonObject_titles.isNull("one_k_followup_id")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("one_k_followup_id"));

                                }else {
                                    modeList.setOne_k_followup_id(jsonObject_titles.getString("one_k_followup_id"));
                                }

                                if (jsonObject_titles.isNull("test_drive_id")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("test_drive_id"));

                                }else {
                                    modeList.setTest_drive_id(jsonObject_titles.getString("test_drive_id"));
                                }

                                if (jsonObject_titles.isNull("dl_number")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("dl_number"));

                                }else {
                                    modeList.setDl_number(jsonObject_titles.getString("dl_number"));
                                }

                                if (jsonObject_titles.isNull("dl_photo")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("dl_photo"));

                                }else {
                                    modeList.setDl_photo(jsonObject_titles.getString("dl_photo"));
                                }

                                if (jsonObject_titles.isNull("td_scheduled_on")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("td_scheduled_on"));

                                }else {
                                    modeList.setTd_scheduled_on(jsonObject_titles.getString("td_scheduled_on"));
                                }

                                if (jsonObject_titles.isNull("overall_td_exp")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("overall_td_exp"));

                                }else {
                                    modeList.setOverall_td_exp(jsonObject_titles.getString("overall_td_exp"));
                                }

                                if (jsonObject_titles.isNull("overall_condition_of_vehicle")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("overall_condition_of_vehicle"));

                                }else {
                                    modeList.setOverall_condition_of_vehicle(jsonObject_titles.getString("overall_condition_of_vehicle"));
                                }

                                if (jsonObject_titles.isNull("sales_consultant_knowledge_of_the_product")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("sales_consultant_knowledge_of_the_product"));

                                }else {
                                    modeList.setSales_consultant_knowledge_of_the_product(jsonObject_titles.getString("sales_consultant_knowledge_of_the_product"));
                                }

                                if (jsonObject_titles.isNull("overall_sales_consultant_knowledge_experiance")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));

                                }else {
                                    modeList.setOverall_sales_consultant_knowledge_experiance(jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));
                                }

                                if (jsonObject_titles.isNull("overall_sales_consultant_knowledge_experiance")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));

                                }else {
                                    modeList.setOverall_sales_consultant_knowledge_experiance(jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));
                                }

                                if (jsonObject_titles.isNull("delivery_completed_id")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("delivery_completed_id"));

                                }else {
                                    modeList.setDelivery_completed_id(jsonObject_titles.getString("delivery_completed_id"));
                                }

                                if (jsonObject_titles.isNull("date")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("date"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("date"));
                                }

                                if (jsonObject_titles.isNull("time")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("time"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("time"));
                                }

                                if (jsonObject_titles.isNull("comments")){
                                  //  System.out.println("NAME"+jsonObject_titles.getString("comments"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("comments"));
                                }

                                if (jsonObject_titles.isNull("refName")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("refName"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("refName"));
                                }

                                if (jsonObject_titles.isNull("refPhone")){
                                   // System.out.println("NAME"+jsonObject_titles.getString("refPhone"));

                                }else {
                                    modeList.setRefPhone(jsonObject_titles.getString("refPhone"));
                                }

                                allContactsLists.add(modeList);
                            }

                            AllContactAdapter adapter = new AllContactAdapter(SingleUserActivity.this, allContactsLists);
                            rv_allcontact.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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



    // Api call Dashboard_Get_All_Filters_status
    public void Dashboard_Get_All_Filters_status() {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getallfilters, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            lin_filters_status.setVisibility(View.VISIBLE);
                            JSONObject filters = jsonObj.getJSONObject("filters");
                            filtersLists.clear();
                            JSONArray status_based = filters.getJSONArray("status_based");
                            for (int i = 0 ; i < status_based.length() ; i++){
                                FiltersList filtersList = new FiltersList();
                                JSONObject jsonObject = status_based.getJSONObject(i);
                                filtersList.setStatus_based_id(jsonObject.getString("status_based_id"));
                                filtersList.setStatus_based_value(jsonObject.getString("status_based_value"));
                                filtersLists.add(filtersList);
                            }
                            StatusBasedFiltersAdapter adapter = new StatusBasedFiltersAdapter(SingleUserActivity.this, filtersLists);
                            rv_filters.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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


    // Api call Dashboard_Get_All_Filters_Model
    public void Dashboard_Get_All_Filters_Model() {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getallfilters, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {

                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            lin_filters_status.setVisibility(View.VISIBLE);
                            JSONObject filters = jsonObj.getJSONObject("filters");
                            //allContactsLists.clear();
                            JSONArray status_based = filters.getJSONArray("model_based");
                            filtersLists.clear();
                            for (int i = 0 ; i < status_based.length() ; i++){
                                FiltersList filtersList = new FiltersList();
                                JSONObject jsonObject = status_based.getJSONObject(i);
                                filtersList.setModel_based_id(jsonObject.getString("model_based_id"));
                                filtersList.setModel_based_value(jsonObject.getString("model_based_value"));
                                filtersLists.add(filtersList);
                            }
                            ModelBasedFiltersAdapter adapter = new ModelBasedFiltersAdapter(SingleUserActivity.this, filtersLists);
                            rv_model_based.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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


    // Api call Dashboard_Get_All_Filters_FollowUp_Type
    public void Dashboard_Get_All_Filters_FollowUp_Type() {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getallfilters, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            lin_filters_status.setVisibility(View.VISIBLE);
                            JSONObject filters = jsonObj.getJSONObject("filters");
                            //allContactsLists.clear();
                            JSONArray status_based = filters.getJSONArray("followup_type");
                            filtersLists.clear();
                            for (int i = 0 ; i < status_based.length() ; i++){
                                FiltersList filtersList = new FiltersList();
                                JSONObject jsonObject = status_based.getJSONObject(i);
                                filtersList.setFollowup_type_id(jsonObject.getString("followup_type_id"));
                                filtersList.setFollowup_type_value(jsonObject.getString("followup_type_value"));
                                filtersLists.add(filtersList);
                            }
                            FollowUpBasedFiltersAdapter adapter = new FollowUpBasedFiltersAdapter(SingleUserActivity.this, filtersLists);
                            rv_followup_type.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
    public void Customer_Get_All_Contact_Filters () {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getallcontact, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    lin_user_all_list.setVisibility(View.VISIBLE);
                    // on below line we are making the
                    // recycler view visibility visible.
                    rv_allcontact.setVisibility(View.VISIBLE);
                    lin_filters_status.setVisibility(View.GONE);

                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("data");
                            // JSONArray jsonArray_intrested_vehicle= jsonObj.getJSONArray("intrested_vehicle");
                            allContactsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                AllContactsList modeList = new AllContactsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);

                                if (jsonObject_titles.isNull("name")){
                                    // System.out.println("NAME"+"xnxnx");

                                }else {
                                    modeList.setName(jsonObject_titles.getString("name"));
                                }

                                if (jsonObject_titles.isNull("phone")){
                                    //   System.out.println("NAME"+jsonObject_titles.getString("phone"));

                                }else {
                                    modeList.setPhone(jsonObject_titles.getString("phone"));
                                }

                                if (jsonObject_titles.isNull("address")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("address"));

                                }else {
                                    modeList.setAddress(jsonObject_titles.getString("address"));
                                }

                                if (jsonObject_titles.isNull("model_and_suffix")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("model_and_suffix"));

                                }else {
                                    modeList.setModel_and_suffix(jsonObject_titles.getString("model_and_suffix"));
                                }

                                if (jsonObject_titles.isNull("pre_delivery_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("pre_delivery_id"));

                                }else {
                                    modeList.setPre_delivery_id(jsonObject_titles.getString("pre_delivery_id"));
                                }

                                if (jsonObject_titles.isNull("accessories_fitment_status")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("accessories_fitment_status"));

                                }else {
                                    modeList.setAccessories_fitment_status(jsonObject_titles.getString("accessories_fitment_status"));
                                }

                                if (jsonObject_titles.isNull("af_waiting_reason")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("af_waiting_reason"));

                                }else {
                                    modeList.setAf_waiting_reason(jsonObject_titles.getString("af_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("extended_warranty")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("extended_warranty"));

                                }else {
                                    modeList.setExtended_warranty(jsonObject_titles.getString("extended_warranty"));
                                }

                                if (jsonObject_titles.isNull("fastag")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("fastag"));

                                }else {
                                    modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("rto_waiting_reason")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("rto_waiting_reason"));

                                }else {
                                    modeList.setRto_waiting_reason(jsonObject_titles.getString("rto_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("preferred_rto_date")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("preferred_rto_date"));

                                }else {
                                    modeList.setPreferred_rto_date(jsonObject_titles.getString("preferred_rto_date"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                    modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                    modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
                                }

                                if (jsonObject_titles.isNull("contact_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("contact_id"));

                                }else {
                                    modeList.setContact_id(jsonObject_titles.getString("contact_id"));
                                }

                                if (jsonObject_titles.isNull("status_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("status_id"));

                                }else {
                                    modeList.setStatus_id(jsonObject_titles.getString("status_id"));
                                }

                                if (jsonObject_titles.isNull("status")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("status"));

                                }else {
                                    modeList.setStatus(jsonObject_titles.getString("status"));
                                }

                                if (jsonObject_titles.isNull("follow_up_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("follow_up_id"));

                                }else {
                                    modeList.setFollow_up_id(jsonObject_titles.getString("follow_up_id"));
                                }

                                if (jsonObject_titles.isNull("follow_up_date")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("follow_up_date"));

                                }else {
                                    modeList.setFollow_up_date(jsonObject_titles.getString("follow_up_date"));
                                }

                                if (jsonObject_titles.isNull("follow_up_time")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("follow_up_time"));

                                }else {
                                    modeList.setFollow_up_time(jsonObject_titles.getString("follow_up_time"));
                                }

                                if (jsonObject_titles.isNull("follow_up_type")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("follow_up_type"));

                                }else {
                                    modeList.setFollow_up_type(jsonObject_titles.getString("follow_up_type"));
                                }

                                if (jsonObject_titles.isNull("vehicle_alloted")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("vehicle_alloted"));

                                }else {
                                    modeList.setVehicle_alloted(jsonObject_titles.getString("vehicle_alloted"));
                                }

                                if (jsonObject_titles.isNull("vehicle_not_alloted_reason")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("vehicle_not_alloted_reason"));

                                }else {
                                    modeList.setVehicle_not_alloted_reason(jsonObject_titles.getString("vehicle_not_alloted_reason"));
                                }

                                if (jsonObject_titles.isNull("paper_documents_collected")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("paper_documents_collected"));

                                }else {
                                    modeList.setPaper_documents_collected(jsonObject_titles.getString("paper_documents_collected"));
                                }

                                if (jsonObject_titles.isNull("login_completed")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("login_completed"));

                                }else {
                                    modeList.setLogin_completed(jsonObject_titles.getString("login_completed"));
                                }

                                if (jsonObject_titles.isNull("login_completed_date")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("login_completed_date"));

                                }else {
                                    modeList.setLogin_completed_date(jsonObject_titles.getString("login_completed_date"));
                                }

                                if (jsonObject_titles.isNull("query_clearance")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("query_clearance"));

                                }else {
                                    modeList.setQuery_clearance(jsonObject_titles.getString("query_clearance"));
                                }

                                if (jsonObject_titles.isNull("loan_amount")){
                                    //   System.out.println("NAME"+jsonObject_titles.getString("loan_amount"));

                                }else {
                                    modeList.setLoan_amount(jsonObject_titles.getString("loan_amount"));
                                }

                                if (jsonObject_titles.isNull("ex_showroom_price")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("ex_showroom_price"));

                                }else {
                                    modeList.setEx_showroom_price(jsonObject_titles.getString("ex_showroom_price"));
                                }

                                if (jsonObject_titles.isNull("tcs")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("tcs"));

                                }else {
                                    modeList.setTcs(jsonObject_titles.getString("tcs"));
                                }

                                if (jsonObject_titles.isNull("tcs_amount")){
                                    //System.out.println("NAME"+jsonObject_titles.getString("tcs_amount"));

                                }else {
                                    modeList.setTcs_amount(jsonObject_titles.getString("tcs_amount"));
                                }

                                if (jsonObject_titles.isNull("road_tax")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("road_tax"));

                                }else {
                                    modeList.setTcs_amount(jsonObject_titles.getString("road_tax"));
                                }

                                if (jsonObject_titles.isNull("insurance")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("insurance"));

                                }else {
                                    modeList.setInsurance(jsonObject_titles.getString("insurance"));
                                }

                                if (jsonObject_titles.isNull("insurance")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("registration"));

                                }else {
                                    modeList.setRegistration(jsonObject_titles.getString("registration"));
                                }

                                if (jsonObject_titles.isNull("accessories")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("accessories"));

                                }else {
                                    modeList.setAccessories(jsonObject_titles.getString("accessories"));
                                }

                                if (jsonObject_titles.isNull("warranty")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("warranty"));

                                }else {
                                    modeList.setWarranty(jsonObject_titles.getString("warranty"));
                                }

                                if (jsonObject_titles.isNull("fastag")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("fastag"));

                                }else {
                                    modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("offers")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("offers"));

                                }else {
                                    modeList.setOffers(jsonObject_titles.getString("offers"));
                                }

                                if (jsonObject_titles.isNull("net_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("net_amount"));

                                }else {
                                    modeList.setNet_amount(jsonObject_titles.getString("net_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                    modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                    modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_date")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_date"));

                                }else {
                                    modeList.setExpected_downpayment_date(jsonObject_titles.getString("expected_downpayment_date"));
                                }

                                if (jsonObject_titles.isNull("expected_invoice_date")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_invoice_date"));

                                }else {
                                    modeList.setExpected_invoice_date(jsonObject_titles.getString("expected_invoice_date"));
                                }

                                if (jsonObject_titles.isNull("booking_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("booking_followup_id"));

                                }else {
                                    modeList.setBooking_followup_id(jsonObject_titles.getString("booking_followup_id"));
                                }

                                if (jsonObject_titles.isNull("fifteenth_day_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("fifteenth_day_followup_id"));

                                }else {
                                    modeList.setFifteenth_day_followup_id(jsonObject_titles.getString("fifteenth_day_followup_id"));
                                }

                                if (jsonObject_titles.isNull("comment")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("comment"));

                                }else {
                                    modeList.setComment(jsonObject_titles.getString("comment"));
                                }

                                if (jsonObject_titles.isNull("booking_completed_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("booking_completed_id"));

                                }else {
                                    modeList.setComment(jsonObject_titles.getString("booking_completed_id"));
                                }

                                if (jsonObject_titles.isNull("booking_completed_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("booking_completed_id"));

                                }else {
                                    modeList.setBooking_completed_id(jsonObject_titles.getString("booking_completed_id"));
                                }

                                if (jsonObject_titles.isNull("booking_model_and_suffix")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("booking_model_and_suffix"));

                                }else {
                                    modeList.setBooking_model_and_suffix(jsonObject_titles.getString("booking_model_and_suffix"));
                                }

                                if (jsonObject_titles.isNull("interior_color")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("interior_color"));

                                }else {
                                    modeList.setInterior_color(jsonObject_titles.getString("interior_color"));
                                }

                                if (jsonObject_titles.isNull("interior_color")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("exterior_color"));

                                }else {
                                    modeList.setExterior_color(jsonObject_titles.getString("exterior_color"));
                                }

                                if (jsonObject_titles.isNull("booking_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("booking_amount"));

                                }else {
                                    modeList.setBooking_amount(jsonObject_titles.getString("booking_amount"));
                                }

                                if (jsonObject_titles.isNull("booking_date")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("booking_date"));

                                }else {
                                    modeList.setBooking_date(jsonObject_titles.getString("booking_date"));
                                }

                                if (jsonObject_titles.isNull("booking_time")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("booking_time"));

                                }else {
                                    modeList.setBooking_time(jsonObject_titles.getString("booking_time"));
                                }


                                if (jsonObject_titles.isNull("payment_mode")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("payment_mode"));

                                }else {
                                    modeList.setPayment_mode(jsonObject_titles.getString("payment_mode"));
                                }


                                if (jsonObject_titles.isNull("reason")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("reason"));

                                }else {
                                    modeList.setReason(jsonObject_titles.getString("reason"));
                                }

                                if (jsonObject_titles.isNull("post_sales_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("post_sales_followup_id"));

                                }else {
                                    modeList.setPost_sales_followup_id(jsonObject_titles.getString("post_sales_followup_id"));
                                }

                                if (jsonObject_titles.isNull("one_k_followup_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("one_k_followup_id"));

                                }else {
                                    modeList.setOne_k_followup_id(jsonObject_titles.getString("one_k_followup_id"));
                                }

                                if (jsonObject_titles.isNull("test_drive_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("test_drive_id"));

                                }else {
                                    modeList.setTest_drive_id(jsonObject_titles.getString("test_drive_id"));
                                }

                                if (jsonObject_titles.isNull("dl_number")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("dl_number"));

                                }else {
                                    modeList.setDl_number(jsonObject_titles.getString("dl_number"));
                                }

                                if (jsonObject_titles.isNull("dl_photo")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("dl_photo"));

                                }else {
                                    modeList.setDl_photo(jsonObject_titles.getString("dl_photo"));
                                }

                                if (jsonObject_titles.isNull("td_scheduled_on")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("td_scheduled_on"));

                                }else {
                                    modeList.setTd_scheduled_on(jsonObject_titles.getString("td_scheduled_on"));
                                }

                                if (jsonObject_titles.isNull("overall_td_exp")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("overall_td_exp"));

                                }else {
                                    modeList.setOverall_td_exp(jsonObject_titles.getString("overall_td_exp"));
                                }

                                if (jsonObject_titles.isNull("overall_condition_of_vehicle")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("overall_condition_of_vehicle"));

                                }else {
                                    modeList.setOverall_condition_of_vehicle(jsonObject_titles.getString("overall_condition_of_vehicle"));
                                }

                                if (jsonObject_titles.isNull("sales_consultant_knowledge_of_the_product")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("sales_consultant_knowledge_of_the_product"));

                                }else {
                                    modeList.setSales_consultant_knowledge_of_the_product(jsonObject_titles.getString("sales_consultant_knowledge_of_the_product"));
                                }

                                if (jsonObject_titles.isNull("overall_sales_consultant_knowledge_experiance")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));

                                }else {
                                    modeList.setOverall_sales_consultant_knowledge_experiance(jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));
                                }

                                if (jsonObject_titles.isNull("overall_sales_consultant_knowledge_experiance")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));

                                }else {
                                    modeList.setOverall_sales_consultant_knowledge_experiance(jsonObject_titles.getString("overall_sales_consultant_knowledge_experiance"));
                                }

                                if (jsonObject_titles.isNull("delivery_completed_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("delivery_completed_id"));

                                }else {
                                    modeList.setDelivery_completed_id(jsonObject_titles.getString("delivery_completed_id"));
                                }

                                if (jsonObject_titles.isNull("date")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("date"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("date"));
                                }

                                if (jsonObject_titles.isNull("time")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("time"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("time"));
                                }

                                if (jsonObject_titles.isNull("comments")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("comments"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("comments"));
                                }

                                if (jsonObject_titles.isNull("refName")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("refName"));

                                }else {
                                    modeList.setDate(jsonObject_titles.getString("refName"));
                                }

                                if (jsonObject_titles.isNull("refPhone")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("refPhone"));

                                }else {
                                    modeList.setRefPhone(jsonObject_titles.getString("refPhone"));
                                }

                                allContactsLists.add(modeList);
                            }

                            AllContactAdapter adapter = new AllContactAdapter(SingleUserActivity.this, allContactsLists);
                            rv_allcontact.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(SingleUserActivity.this);
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
               // params.put("FILTERS", UPDATE_ENQUIRY_ID);
                System.out.println("PRINTF" + params);
                return params;
            }



            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("API_TOKEN", API_TOKEN);
                params.put("FILTERS", status_based);

                return params;
            }

        };
        requestQueue.add(requests);

    }

}
