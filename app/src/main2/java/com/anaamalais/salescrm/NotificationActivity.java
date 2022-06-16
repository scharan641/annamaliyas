package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.ContactTargetAdapter;
import com.anaamalais.salescrm.Adapter.EnquiryTargetAdapter;
import com.anaamalais.salescrm.Adapter.NotificationAdapter;
import com.anaamalais.salescrm.List.Mode;
import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.List.NotificationList;
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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    RecyclerView rv_notification , rv_enquiry_mode;
    String users_notifications_url = BASE_URL + "users/getnotifications";
    List<NotificationList> notificationLists;
    List<Model> models;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView txt_no_notification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = NotificationActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(NotificationActivity.this, R.color.white));
        rv_notification = findViewById(R.id.rv_notification);
        //rv_enquiry_mode  = findViewById(R.id.rv_enquiry_mode);
        requestQueue = Volley.newRequestQueue(NotificationActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(NotificationActivity.this, Constants.TOKEN, "");
        txt_no_notification = findViewById(R.id.txt_no_notification);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_notification.setHasFixedSize(true);
        rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationLists = new ArrayList<>();

        Get_Notification();
    }

    public void back(View view) {
        startActivity(new Intent(NotificationActivity.this,HomeActivity.class));
        finish();
    }

    public void Get_Notification(){
        StringRequest requests = new StringRequest(Request.Method.POST, users_notifications_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rv_notification.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONArray notification = jsonObj.getJSONArray("data");
                            for (int i = 0 ; i < notification.length() ; i++){
                                JSONObject jnotification = notification.getJSONObject(i);
                                NotificationList notificationList = new NotificationList();
                                notificationList.setStatus(jnotification.getString("status"));
                                notificationList.setMessage(jnotification.getString("message"));
                                notificationLists.add(notificationList);
                            }
                            NotificationAdapter adapter = new NotificationAdapter(NotificationActivity.this, notificationLists);
                            rv_notification.setAdapter(adapter);

                        }else if (status_code.equals("0")){
                            txt_no_notification.setVisibility(View.VISIBLE);
                            rv_notification.setVisibility(View.GONE);
                            msg = (String) jsonObj.getString("msg");

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NotificationActivity.this);
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
