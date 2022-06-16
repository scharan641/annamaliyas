package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.FiltersList;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TaskListActivity extends AppCompatActivity {
    TextView txt_current_month , txt_no_calander_data , txt_current_day;
    String dashboard_getdatecalendardata = BASE_URL + "dashboard/getdatecalendardata";
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    LinearLayout lin_user_all_list , lin_filters_status;
    List<AllContactsList> allContactsLists;
    List<FiltersList> filtersLists;
    String  status_code , msg ,token, API_TOKEN , calendatedate;
    ShimmerFrameLayout shimmerFrameLayout , shimmerFrameLayoutDashboard;
    RecyclerView rv_calendar_data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Window window = TaskListActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(TaskListActivity.this, R.color.customerdetails));
        API_TOKEN = MyFunctions.getSharedPrefs(TaskListActivity.this, Constants.TOKEN, "");
        requestQueue = Volley.newRequestQueue(TaskListActivity.this);
        txt_current_month = findViewById(R.id.txt_current_month);
        txt_no_calander_data = findViewById(R.id.txt_no_calander_data);
        txt_current_day = findViewById(R.id.txt_current_day);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat mdformat = new  SimpleDateFormat("MMM yyyy");
        SimpleDateFormat apiformat = new  SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dayformat = new  SimpleDateFormat("EEEE dd MMM");
        calendatedate =  apiformat.format(c);
        String strDate = mdformat.format(c);
        txt_current_month.setText(strDate);
        String day = dayformat.format(c);
        txt_current_day.setText("Task for"+"\t"+day);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_calendar_data = findViewById(R.id.rv_calendar_data);
        rv_calendar_data.setHasFixedSize(true);
        rv_calendar_data.setLayoutManager(new LinearLayoutManager(TaskListActivity.this));
        allContactsLists = new ArrayList<>();
        Get_Date_Calendar_Data();
    }

    public void back(View view) {
        startActivity(new Intent(TaskListActivity.this,HomeActivity.class));
        finish();
    }

    public void task(View view) {
        startActivity(new Intent(TaskListActivity.this,TaskDetailsActivity.class).putExtra("home","TASK"));
        finish();
    }

    public void calendartask(View view) {
        startActivity(new Intent(TaskListActivity.this,CalendartaskActivity.class));
        finish();
    }


    // Api call Customer_cop_Getsources
    public void Get_Date_Calendar_Data () {
        StringRequest requests = new StringRequest(Request.Method.POST, dashboard_getdatecalendardata, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // on below line we are making the
                    // recycler view visibility visible.
                    rv_calendar_data.setVisibility(View.VISIBLE);


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

                            AllContactAdapter adapter = new AllContactAdapter(TaskListActivity.this, allContactsLists);
                            rv_calendar_data.setAdapter(adapter);


                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            txt_no_calander_data.setVisibility(View.VISIBLE);
                            rv_calendar_data.setVisibility(View.GONE);
                            txt_no_calander_data.setText(msg+"!!");


                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(TaskListActivity.this);
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
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("DATE",calendatedate);
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
