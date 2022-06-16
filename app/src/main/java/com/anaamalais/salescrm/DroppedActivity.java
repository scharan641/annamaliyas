package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.Adapter.AnnualIncomeTypeArrayAdapter;
import com.anaamalais.salescrm.Adapter.DropReasonsAdapter;
import com.anaamalais.salescrm.List.AnnualIncomesList;
import com.anaamalais.salescrm.List.DropReasonsList;
import com.anaamalais.salescrm.List.PaymentModesList;
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

import java.nio.file.ProviderMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DroppedActivity extends AppCompatActivity {
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView Img_up , Img_down;
    RelativeLayout rel_sales_reason;
    LinearLayout lin_individual;
    View view_sales_reason;
    RecyclerView rv_drop_reason;
    List<DropReasonsList>dropReasonsLists;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String status_code, msg, token, contact_id , contact_status  , API_TOKEN, UPDATE_CONTACT_ID,DROPPED_REASON_ID,CONTACT_ID;
    String booking_getdropreasons_url = BASE_URL + "booking/getdropreasons";
    String customer_dropcontact_url = BASE_URL + "customer/dropcontact";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = DroppedActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(DroppedActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(DroppedActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(DroppedActivity.this, Constants.TOKEN, "");
        rel_sales_reason = findViewById(R.id.rel_sales_reason);
        Img_up = findViewById(R.id.img_up);
        Img_down = findViewById(R.id.img_down);
        lin_individual = findViewById(R.id.lin_individual);
        view_sales_reason = findViewById(R.id.view_sales_reason);
        rv_drop_reason = findViewById(R.id.rv_drop_reason);
        rv_drop_reason.setHasFixedSize(true);
        rv_drop_reason.setLayoutManager(new LinearLayoutManager(DroppedActivity.this));
        dropReasonsLists = new ArrayList<>();
        CONTACT_ID = getIntent().getStringExtra("CONTACTID");
        rel_sales_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Img_down.getVisibility()==View.VISIBLE){
                    Img_down.setVisibility(View.GONE);
                    Img_up.setVisibility(View.VISIBLE);
                    lin_individual.setVisibility(View.VISIBLE);
                    view_sales_reason.setVisibility(View.GONE);
                    shimmerFrameLayout = findViewById(R.id.shimmerLayout);
                    shimmerFrameLayout.startShimmer();
                    Booking_Get_Drop_Reasons();

                }else if (Img_up.getVisibility()==View.VISIBLE){
                    Img_down.setVisibility(View.VISIBLE);
                    Img_up.setVisibility(View.GONE);
                    lin_individual.setVisibility(View.GONE);
                    view_sales_reason.setVisibility(View.GONE);
                }
            }
        });

    }

    public void back(View view) {
        startActivity(new Intent(DroppedActivity.this,CustomerDetailsActivity.class).putExtra("Status","Drop"));
         finish();
    }

    public void updatestatus(View view) {
        DROPPED_REASON_ID = MyFunctions.getSharedPrefs(DroppedActivity.this,Constants.DROP_REASON_ID,"");
      if (DROPPED_REASON_ID.equals("")){
          Toast.makeText(DroppedActivity.this, "Selected the Reason", Toast.LENGTH_SHORT).show();
      }else {
          if (DROPPED_REASON_ID.equals("")||DROPPED_REASON_ID.isEmpty()){
              Toast.makeText(DroppedActivity.this, "Selected the Drop Reason", Toast.LENGTH_SHORT).show();
          }else {
              Customer_Drop_Contact();
              //initialize the progress dialog and show it
              progressDialog = new ProgressDialog(DroppedActivity.this);
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

    // Api call Booking_Get_Drop_Reasons
    public void Booking_Get_Drop_Reasons () {
        StringRequest requests = new StringRequest(Request.Method.POST, booking_getdropreasons_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // on below line we are making the
                    // recycler view visibility visible.
                    rv_drop_reason.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("0")) {
                            JSONArray jsonArray_titles = jsonObj.getJSONArray("drop_reasons");
                            dropReasonsLists.clear();
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                DropReasonsList paymentModesList = new DropReasonsList();
                                JSONObject jsonObject_titles = jsonArray_titles.getJSONObject(i);
                                paymentModesList.setDrop_id(jsonObject_titles.getString("drop_id"));
                                paymentModesList.setDrop_reason(jsonObject_titles.getString("drop_reason"));

                                dropReasonsLists.add(paymentModesList);
                            }

                            DropReasonsAdapter adapter = new DropReasonsAdapter(DroppedActivity.this, dropReasonsLists);
                            rv_drop_reason.setAdapter(adapter);


                        } else if (status_code.equals("1")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(DroppedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DroppedActivity.this);
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


    // Api call Customer_Drop_Contact
    public void Customer_Drop_Contact () {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_dropcontact_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                        progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            for (int i = 0; i < jsonObj.length(); i++) {
                                JSONObject jsonObject = jsonObj.getJSONObject("data");
                                contact_id = jsonObject.getString("customer_id");
                                MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.CONTACT_ID,contact_id);
                                contact_status = jsonObject.getString("status");
                                String status_id = jsonObject.getString("status_id");
                                MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.STATUSID,status_id);
                                String cus_name = (String)jsonObject.get("name");
                                MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.CUSTOMER_NAME,cus_name);
                                String cus_phone = (String)jsonObject.get("phone").toString();
                                MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                                String comment = (String)jsonObject.get("reason");
                                MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.COMMENT,comment);
                               if (jsonObject.isNull("model_and_suffix")){

                               }else {
                                   String cus_address = (String)jsonObject.get("model_and_suffix");
                                   MyFunctions.setSharedPrefs(DroppedActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                               }
                                startActivity(new Intent(DroppedActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                                finish();

                            }

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(DroppedActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DroppedActivity.this);
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
                params.put("CONTACT_ID", CONTACT_ID);
                params.put("DROPPED_REASON_ID", DROPPED_REASON_ID);
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
