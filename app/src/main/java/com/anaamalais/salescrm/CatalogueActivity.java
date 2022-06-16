package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.CatalogueAdapter;
import com.anaamalais.salescrm.Adapter.EnquiryTargetAdapter;
import com.anaamalais.salescrm.List.CatelogeList;
import com.anaamalais.salescrm.List.Mode;
import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.anaamalais.salescrm.Utils.PreferenceManager;
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

public class CatalogueActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    RecyclerView rv_catalogue , rv_enquiry_mode;
    String accessoriesmaster_cateloge_url = BASE_URL + "accessoriesmaster/getcateloge";
    List<CatelogeList> catelogeLists;
    ShimmerFrameLayout shimmerFrameLayout;
    PreferenceManager preferenceManager;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        preferenceManager = new PreferenceManager(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = CatalogueActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(CatalogueActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(CatalogueActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(CatalogueActivity.this, Constants.TOKEN, "");
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();
        rv_catalogue = findViewById(R.id.rv_catalogue);
        rv_catalogue.setHasFixedSize(true);
        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv_catalogue.setLayoutManager(manager);
        catelogeLists =new ArrayList<>();

        Get_Cateloge();
    }

    public void back(View view) {
        startActivity(new Intent(CatalogueActivity.this,HomeActivity.class));
        finish();
    }

    public void Get_Cateloge(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_cateloge_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rv_catalogue.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONArray catalogue = jsonObj.getJSONArray("data");
                            for (int k = 0 ; k < catalogue.length() ; k++){
                                CatelogeList model = new CatelogeList();
                                JSONObject jsonObject2 = catalogue.getJSONObject(k);
                                System.out.println("jgfgffggf" + jsonObject2.toString());
                                model.setId(jsonObject2.getString("id"));

                                model.setModel(jsonObject2.getString("model"));
                                model.setVehicle_image(jsonObject2.getString("vehicle_image"));
                                catelogeLists.add(model);
                            }
                            CatalogueAdapter adapter = new CatalogueAdapter(CatalogueActivity.this, catelogeLists);
                            rv_catalogue.setAdapter(adapter);



                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(CatalogueActivity.this);
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
                                        MyFunctions.setSharedPrefs(CatalogueActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(CatalogueActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(CatalogueActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(CatalogueActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CatalogueActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(CatalogueActivity.this,HomeActivity.class));
        finish();
    }
}

