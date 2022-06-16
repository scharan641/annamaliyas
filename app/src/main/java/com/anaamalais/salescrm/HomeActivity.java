package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.Adapter.TodayTaskAdapter;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.TestDriveCompletedList;
import com.anaamalais.salescrm.List.TodayTaskList;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
    TextView user_name,txt_time,txt_today,txt_complete,txt_no_today_task,txt_cus_name,txt_model,txt_cus_location,txt_new,txt_follow,txt_cus_name_follow,txt_cus_location_follow,
            txt_new_follow,txt_cus_name_closed,txt_todays_contact,txt_notificationcount,txt_model_closed,txt_cus_location_closed,txt_cus_date_time_closed,txt_new_closed;
    ImageView image_bottom_dailog , single_user , task_list , catalogue,image_car;
    ProgressBar circularProgressbarbooking , circularProgressbarRetail;
    TextView tvprogressbarbooking , tvprogressbarretail;
    TextView txt_booking , txt_retail, txt_contact,txt_enquiry,txt_Test_Drive,txt_finance,txt_finance_total
            ,txt_insurance,txt_insurance_total,txt_ewarranty,txt_ewarranty_total,txt_accessories,txt_accessories_total;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus , progressBarStatusretail = 0;
    LinearLayout lin_contacts , lin_Enquiry , lin_Test_Drive , lin_retail_target ,
            lin_booking_target , lin_target;
    RelativeLayout rel_finance , rel_insurance , rel_ewarranty , rel_accessories,lin_top_status;
    Animation animZoomIn;
    String emp_name;
    String  status_code , msg ,token, API_TOKEN;
    ShimmerFrameLayout shimmerFrameLayout , shimmerFrameLayoutDashboard;
    RecyclerView rv_today_task;
    String dashboard_todaytask_url = BASE_URL + "dashboard/todaytask";
    String targets_gettargets_url = BASE_URL + "targets/gettargets";
    String users_notificationcount_url = BASE_URL + "users/getnotificationcount";
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    List<TodayTaskList> todayTaskLists;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = HomeActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.white));
        API_TOKEN = MyFunctions.getSharedPrefs(HomeActivity.this, Constants.TOKEN, "");
        requestQueue = Volley.newRequestQueue(HomeActivity.this);
        image_bottom_dailog = findViewById(R.id.image_bottom_dailog);
        single_user = findViewById(R.id.single_user);
        task_list = findViewById(R.id.task_list);
        catalogue = findViewById(R.id.catalogue);
    //    image_car = findViewById(R.id.image_car);
        image_car = findViewById(R.id.image_animation);
        user_name = findViewById(R.id.user_name);
        txt_time = findViewById(R.id.txt_time);
        emp_name = MyFunctions.getSharedPrefs(HomeActivity.this, Constants.EMP_NAME,"");
        user_name.setText(emp_name);
        circularProgressbarbooking  = findViewById(R.id.circularProgressbarbooking);
        circularProgressbarRetail  = findViewById(R.id.circularProgressbarRetail);
        tvprogressbarbooking  = findViewById(R.id.tvprogressbarbooking);
        tvprogressbarretail  = findViewById(R.id.tvprogressbarretail);
        txt_booking  = findViewById(R.id.txt_booking);
        txt_retail  = findViewById(R.id.txt_retail);
        txt_contact  = findViewById(R.id.txt_contact);
        txt_enquiry  = findViewById(R.id.txt_enquiry);
        txt_finance  = findViewById(R.id.txt_finance);
        txt_Test_Drive  = findViewById(R.id.txt_Test_Drive);
        txt_finance_total  = findViewById(R.id.txt_finance_total);
        txt_insurance  = findViewById(R.id.txt_insurance);
        txt_insurance_total  = findViewById(R.id.txt_insurance_total);
        txt_ewarranty  = findViewById(R.id.txt_ewarranty);
        txt_ewarranty_total  = findViewById(R.id.txt_ewarranty_total);
        txt_accessories  = findViewById(R.id.txt_accessories);
        txt_accessories_total  = findViewById(R.id.txt_accessories_total);
        lin_contacts = findViewById(R.id.lin_contacts);
        lin_Enquiry  = findViewById(R.id.lin_Enquiry);
        lin_Test_Drive = findViewById(R.id.lin_Test_Drive);
        rel_finance  = findViewById(R.id.rel_finance);
        rel_insurance = findViewById(R.id.rel_insurance);
        rel_ewarranty  = findViewById(R.id.rel_ewarranty);
        rel_accessories = findViewById(R.id.rel_accessories);
        lin_retail_target = findViewById(R.id.lin_retail_target);
        lin_booking_target = findViewById(R.id.lin_booking_target);
        txt_notificationcount = findViewById(R.id.txt_notificationcount);
        txt_no_today_task = findViewById(R.id.txt_no_today_task);
        lin_top_status = findViewById(R.id.lin_top_status);
       // txt_todays_contact = findViewById(R.id.circularProgressbarbooking);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new  SimpleDateFormat("hh:mm a");
        String strDate = mdformat.format(calendar.getTime());
        txt_time.setText(strDate);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_today_task = findViewById(R.id.rv_today_task);
        rv_today_task.setHasFixedSize(true);
        rv_today_task.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        todayTaskLists = new ArrayList<>();

        // txt_todays_contact.setText(Html.fromHtml("<html><body><font size=5>5 </font> <font size=2>/10 </font></body><html>"));

      /*  CustomBottomNavigationView customBottomNavigationView1 = findViewById(R.id.customBottomBar);
        customBottomNavigationView1.inflateMenu(R.menu.bottom_menu);
        customBottomNavigationView1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                      startActivity(new Intent(HomeActivity.this,SingleUserActivity.class));
                      finish();
                        return true;

                    case R.id.action_schedules:
                        // do something here
                        startActivity(new Intent(HomeActivity.this,TaskListActivity.class));
                        finish();
                        return true;
                    case R.id.action_music:
                        // do something here
                        startActivity(new Intent(HomeActivity.this,CatalogueActivity.class));
                        finish();
                        return true;

                }
                return true;
            }
        });*/

        single_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SingleUserActivity.class));
               // finish();
            }
        });

        task_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,TaskListActivity.class));
               // finish();
            }
        });

        catalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CatalogueActivity.class));
               // finish();
            }
        });


        image_bottom_dailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.layout_bottom_dailog);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
              //  layoutParams.x = 100; // right margin
                layoutParams.y = 130; // top margin
                dialog.getWindow().setAttributes(layoutParams);
                TextView txt_add_contact = (TextView) dialog.findViewById(R.id.txt_add_contact);
                TextView txt_add_enquiry = (TextView) dialog.findViewById(R.id.txt_add_enquiry);
             //   TextView txt_add_meeting = (TextView) dialog.findViewById(R.id.txt_add_meeting);
                ImageView image_bottom = (ImageView) dialog.findViewById(R.id.image_bottom);

                txt_add_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // MyFunctions.setSharedPrefs(ProfileActivity.this, Constants.IS_LOGIN, false);
                        // MyFunctions.setSharedPrefs(ProfileActivity.this, Constants.USER_ID, "");
                        // MyFunctions.setSharedPrefs(ProfileActivity.this, Constants.CUSTOMERID, "");
                        startActivity(new Intent(HomeActivity.this, AddContactActivity.class));
                        finish();
                        dialog.dismiss();


                    }
                });
                txt_add_enquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,AddEnquiryActivity.class));
                        dialog.dismiss();
                    }
                });

              /*  txt_add_meeting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(HomeActivity.this,AddMeetingActivity.class));
                        dialog.dismiss();
                    }
                });*/

                image_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  startActivity(new Intent(HomeActivity.this,AddMeetingActivity.class));
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

        });


        lin_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ContactTargetActivity.class).putExtra("home","HOME"));
                 finish();
            }
        });

        lin_Enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,EnquiryTargetActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        lin_Test_Drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,TestDriveActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        rel_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,FinanceActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        rel_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,InsuranceActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        rel_ewarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,EwarrantyActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        rel_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,AccessoriesActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        lin_booking_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,BookingTargetActivity.class).putExtra("home","HOME"));
                finish();
            }
        });


        lin_retail_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,RetailActivity.class).putExtra("home","HOME"));
                finish();
            }
        });

        DashboardTodaytask();
        Get_Targets();
        Get_Notification_Count();

    }

    public void newlead(View view) {
        startActivity(new Intent(HomeActivity.this,TaskDetailsActivity.class).putExtra("home","HOME"));
       // finish();
    }

    public void closed(View view) {
        startActivity(new Intent(HomeActivity.this,TaskDetailsActivity.class).putExtra("home","HOME"));
       // finish();
    }

    public void lead(View view) {
        startActivity(new Intent(HomeActivity.this,TaskDetailsActivity.class).putExtra("home","HOME"));
      //  finish();
    }

    public void notification(View view) {
        startActivity(new Intent(HomeActivity.this,NotificationActivity.class));
      //  finish();
    }

    public void userprofile(View view) {
        startActivity(new Intent(HomeActivity.this,UserProfileActivity.class));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void DashboardTodaytask(){
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_todaytask_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // on below line we are making the
                    // recycler view visibility visible.
                    rv_today_task.setVisibility(View.VISIBLE);


                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("data");
                            // JSONArray jsonArray_intrested_vehicle= jsonObj.getJSONArray("intrested_vehicle");
                            todayTaskLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                TodayTaskList modeList = new TodayTaskList();
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
                                  //  modeList.setPre_delivery_id(jsonObject_titles.getString("pre_delivery_id"));
                                }

                                if (jsonObject_titles.isNull("accessories_fitment_status")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("accessories_fitment_status"));

                                }else {
                                  //  modeList.setAccessories_fitment_status(jsonObject_titles.getString("accessories_fitment_status"));
                                }

                                if (jsonObject_titles.isNull("af_waiting_reason")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("af_waiting_reason"));

                                }else {
                                  //  modeList.setAf_waiting_reason(jsonObject_titles.getString("af_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("extended_warranty")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("extended_warranty"));

                                }else {
                                   // modeList.setExtended_warranty(jsonObject_titles.getString("extended_warranty"));
                                }

                                if (jsonObject_titles.isNull("fastag")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("fastag"));

                                }else {
                                  //  modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("rto_waiting_reason")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("rto_waiting_reason"));

                                }else {
                                  //  modeList.setRto_waiting_reason(jsonObject_titles.getString("rto_waiting_reason"));
                                }

                                if (jsonObject_titles.isNull("preferred_rto_date")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("preferred_rto_date"));

                                }else {
                                  //  modeList.setPreferred_rto_date(jsonObject_titles.getString("preferred_rto_date"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                  //  modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
                                }

                                if (jsonObject_titles.isNull("allocation_pdi")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("allocation_pdi"));

                                }else {
                                 //   modeList.setAllocation_pdi(jsonObject_titles.getString("allocation_pdi"));
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
                                   // modeList.setFastag(jsonObject_titles.getString("fastag"));
                                }

                                if (jsonObject_titles.isNull("offers")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("offers"));

                                }else {
                                  //  modeList.setOffers(jsonObject_titles.getString("offers"));
                                }

                                if (jsonObject_titles.isNull("net_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("net_amount"));

                                }else {
                                   // modeList.setNet_amount(jsonObject_titles.getString("net_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                  //  modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_amount")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_amount"));

                                }else {
                                   // modeList.setExpected_downpayment_amount(jsonObject_titles.getString("expected_downpayment_amount"));
                                }

                                if (jsonObject_titles.isNull("expected_downpayment_date")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("expected_downpayment_date"));

                                }else {
                                   // modeList.setExpected_downpayment_date(jsonObject_titles.getString("expected_downpayment_date"));
                                }

                                if (jsonObject_titles.isNull("expected_invoice_date")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("expected_invoice_date"));

                                }else {
                                  //  modeList.setExpected_invoice_date(jsonObject_titles.getString("expected_invoice_date"));
                                }

                                if (jsonObject_titles.isNull("booking_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("booking_followup_id"));

                                }else {
                                   // modeList.setBooking_followup_id(jsonObject_titles.getString("booking_followup_id"));
                                }

                                if (jsonObject_titles.isNull("fifteenth_day_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("fifteenth_day_followup_id"));

                                }else {
                                   // modeList.setFifteenth_day_followup_id(jsonObject_titles.getString("fifteenth_day_followup_id"));
                                }

                                if (jsonObject_titles.isNull("comment")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("comment"));

                                }else {
                                 //   modeList.setComment(jsonObject_titles.getString("comment"));
                                }

                                if (jsonObject_titles.isNull("booking_completed_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("booking_completed_id"));

                                }else {
                                   // modeList.setComment(jsonObject_titles.getString("booking_completed_id"));
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
                                  //  modeList.setReason(jsonObject_titles.getString("reason"));
                                }

                                if (jsonObject_titles.isNull("post_sales_followup_id")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("post_sales_followup_id"));

                                }else {
                                    //modeList.setPost_sales_followup_id(jsonObject_titles.getString("post_sales_followup_id"));
                                }

                                if (jsonObject_titles.isNull("one_k_followup_id")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("one_k_followup_id"));

                                }else {
                                   // modeList.setOne_k_followup_id(jsonObject_titles.getString("one_k_followup_id"));
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
                                 //   modeList.setDelivery_completed_id(jsonObject_titles.getString("delivery_completed_id"));
                                }

                                if (jsonObject_titles.isNull("date")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("date"));

                                }else {
                                   // modeList.setDate(jsonObject_titles.getString("date"));
                                }

                                if (jsonObject_titles.isNull("time")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("time"));

                                }else {
                                  //  modeList.setDate(jsonObject_titles.getString("time"));
                                }

                                if (jsonObject_titles.isNull("comments")){
                                    //  System.out.println("NAME"+jsonObject_titles.getString("comments"));

                                }else {
                                 //   modeList.setDate(jsonObject_titles.getString("comments"));
                                }

                                if (jsonObject_titles.isNull("refName")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("refName"));

                                }else {
                                   // modeList.setDate(jsonObject_titles.getString("refName"));
                                }

                                if (jsonObject_titles.isNull("refPhone")){
                                    // System.out.println("NAME"+jsonObject_titles.getString("refPhone"));

                                }else {
                                  //  modeList.setRefPhone(jsonObject_titles.getString("refPhone"));
                                }

                                todayTaskLists.add(modeList);
                            }

                            TodayTaskAdapter adapter = new TodayTaskAdapter(HomeActivity.this, todayTaskLists);
                            rv_today_task.setAdapter(adapter);

                        } else if (status_code.equals("0")) {
                            txt_no_today_task.setVisibility(View.VISIBLE);
                            rv_today_task.setVisibility(View.GONE);

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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

    // Api call Start_Activity
    public void Get_Targets(){
        StringRequest requests = new StringRequest(Request.Method.POST, targets_gettargets_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONArray targets = jsonObj.getJSONArray("targets");
                                 for(int i = 0 ; i < targets.length() ; i++){
                                     JSONObject jsonObject = targets.getJSONObject(i);
                                     int user_id = (Integer)jsonObject.getInt("user_id");
                                     int target_id = (Integer)jsonObject.getInt("target_id");
                                     String name = (String)jsonObject.getString("name");
                                     int total_target = (Integer)jsonObject.getInt("total_target");
                                     int achieved_target = (Integer)jsonObject.getInt("achieved_target");
                                     if (name.equals("Booking Target")){
                                         txt_booking.setText(String.valueOf(achieved_target+"/"+total_target));
                                         try{
                                             progressBarStatus = (achieved_target * 100/ total_target);
                                             //  progressBarStatus *= 100;
                                             tvprogressbarbooking.setText(progressBarStatus+"%");
                                         }
                                         catch (ArithmeticException e) {
                                             System.out.println ("Can't be divided by Zero " + e);
                                         }
                                         new Thread(new Runnable() {
                                             public void run() {
                                                 while (progressBarStatus < 100) {
                                                    // progressBarStatus += 1;
                                                     // Update the progress bar and display the
                                                     //current value in the text view
                                                     progressBarHandler.post(new Runnable() {
                                                         public void run() {
                                                             circularProgressbarbooking.setProgress(progressBarStatus);

                                                         }
                                                     });
                                                     try {
                                                         // Sleep for 200 milliseconds.
                                                         Thread.sleep(200);
                                                     } catch (InterruptedException e) {
                                                         e.printStackTrace();
                                                     }
                                                 }
                                             }
                                         }).start();

                                     }else if (name.equals("Retail Target")){
                                         txt_retail.setText(String.valueOf(achieved_target+"/"+total_target));
                                         try{
                                             progressBarStatusretail = (achieved_target * 100/ total_target);
                                             tvprogressbarretail.setText(progressBarStatusretail+"%");
                                         }
                                         catch (ArithmeticException e) {
                                             System.out.println ("Can't be divided by Zero " + e);
                                         }

                                         new Thread(new Runnable() {
                                             public void run() {
                                                 while (progressBarStatus < 100) {
                                                     // progressBarStatus += 1;
                                                     // Update the progress bar and display the
                                                     //current value in the text view
                                                     progressBarHandler.post(new Runnable() {
                                                         public void run() {
                                                             circularProgressbarRetail.setProgress(progressBarStatusretail);

                                                         }
                                                     });
                                                     try {
                                                         // Sleep for 200 milliseconds.
                                                         Thread.sleep(200);
                                                     } catch (InterruptedException e) {
                                                         e.printStackTrace();
                                                     }
                                                 }
                                             }
                                         }).start();
                                     }else if (name.equals("Contact Target")){
                                         txt_contact.setText(String.valueOf(achieved_target+"/"+total_target));
                                     }else if (name.equals("Enquiry Target")){
                                         txt_enquiry.setText(String.valueOf(achieved_target+"/"+total_target));
                                     }else if (name.equals("TD Completed")){
                                         txt_Test_Drive.setText(String.valueOf(achieved_target+"/"+total_target));
                                     }else if (name.equals("Finance Target")){
                                         txt_finance.setText(String.valueOf(achieved_target));
                                         txt_finance_total.setText(String.valueOf(total_target));
                                     }else if (name.equals("Insurance Target")){
                                         txt_insurance.setText(String.valueOf(achieved_target));
                                         txt_insurance_total.setText(String.valueOf(total_target));
                                     }else if (name.equals("E-Warranty")){
                                         txt_ewarranty.setText(String.valueOf(achieved_target));
                                         txt_ewarranty_total.setText(String.valueOf(total_target));
                                     }else if (name.equals("Accessories")){
                                         txt_accessories.setText(String.valueOf(achieved_target));
                                         txt_accessories_total.setText(String.valueOf(total_target));
                                     }
                                 }


                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog= builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                    if (msg.equals("Invalid API_TOKEN!")){
                                        MyFunctions.setSharedPrefs(HomeActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(HomeActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(HomeActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(HomeActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
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

    // Api call Start_Activity
    public void Get_Notification_Count(){

        StringRequest requests = new StringRequest(Request.Method.POST, users_notificationcount_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            String count  = (String) jsonObj.getString("count");
                            txt_notificationcount.setText(count);
                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog= builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                    if (msg.equals("Invalid API_TOKEN!")){
                                        MyFunctions.setSharedPrefs(HomeActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(HomeActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(HomeActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(HomeActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
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
}
