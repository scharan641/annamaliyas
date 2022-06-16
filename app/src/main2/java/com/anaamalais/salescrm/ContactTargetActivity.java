package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.Adapter.ContactTargetAdapter;
import com.anaamalais.salescrm.List.Mode;
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

public class ContactTargetActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    RecyclerView rv_contact_target;
    String targets_gettargets_url = BASE_URL + "targets/gettargets";
    List<Mode>modes;
    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_target);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = ContactTargetActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ContactTargetActivity.this, R.color.white));
        rv_contact_target = findViewById(R.id.rv_contact_target);
        requestQueue = Volley.newRequestQueue(ContactTargetActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(ContactTargetActivity.this, Constants.TOKEN, "");
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_contact_target.setHasFixedSize(true);
        rv_contact_target.setLayoutManager(new LinearLayoutManager(ContactTargetActivity.this));
        modes = new ArrayList<>();
        Get_Targets();
    }

    public void back(View view) {
        startActivity(new Intent(ContactTargetActivity.this,HomeActivity.class));
        finish();
    }


    // Api call Start_Activity
    public void Get_Targets(){
        StringRequest requests = new StringRequest(Request.Method.POST, targets_gettargets_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rv_contact_target.setVisibility(View.VISIBLE);
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
                                if (name.equals("Contact Target")){
                                    String data = (String)jsonObject.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);
                                     JSONArray jsonArray = jsonObject1.getJSONArray("mode");
                                     for (int j = 0 ; j < jsonArray.length() ; j++){
                                         Mode mode = new Mode();
                                         JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                                         System.out.println("jgfgffggf" + jsonObject2.toString());
                                         mode.setMode_name(jsonObject2.getString("mode_name"));
                                         mode.setTotal_target(jsonObject2.getString("total_target"));
                                         mode.setAchieved_target(jsonObject2.getString("achieved_target"));
                                         modes.add(mode);
                                     }
                                    ContactTargetAdapter adapter = new ContactTargetAdapter(ContactTargetActivity.this, modes);
                                    rv_contact_target.setAdapter(adapter);
                                 }

                            }


                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ContactTargetActivity.this);
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
                                        MyFunctions.setSharedPrefs(ContactTargetActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(ContactTargetActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(ContactTargetActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ContactTargetActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ContactTargetActivity.this);
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
