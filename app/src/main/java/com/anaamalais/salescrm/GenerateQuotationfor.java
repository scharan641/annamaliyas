package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.ContactTargetAdapter;
import com.anaamalais.salescrm.Adapter.GenerateQuotationforAdapter;
import com.anaamalais.salescrm.List.CustomerList;
import com.anaamalais.salescrm.List.Mode;
import com.anaamalais.salescrm.List.VariantsAccessList;
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

public class GenerateQuotationfor extends AppCompatActivity {
    EditText searchView;
    RequestQueue requestQueue;
    RecyclerView rv_generate_quotation;
    ShimmerFrameLayout shimmerFrameLayout;
    String status_code, msg, token, API_TOKEN;
    RecyclerView rv_catalogue , rv_enquiry_mode;
    String customer_getcustomerlist_url = BASE_URL + "customer/getcustomerlist";
    List<CustomerList> customerLists;
    String SEARCH_WORD;
    private static final int REQUEST_WRITE_PERMISSION = 786;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_generatequotationfor);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = GenerateQuotationfor.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(GenerateQuotationfor.this, R.color.white));
        requestQueue = Volley.newRequestQueue(GenerateQuotationfor.this);
        API_TOKEN = MyFunctions.getSharedPrefs(GenerateQuotationfor.this, Constants.TOKEN, "");
        searchView = findViewById(R.id.ET_city);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        rv_generate_quotation = findViewById(R.id.rv_generate_quotation);
        rv_generate_quotation.setHasFixedSize(true);
        rv_generate_quotation.setLayoutManager(new LinearLayoutManager(GenerateQuotationfor.this));
        customerLists = new ArrayList<>();
        requestPermissioreadandwrite();




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

                SEARCH_WORD = editable.toString();
                if (SEARCH_WORD!=null){
                    shimmerFrameLayout.startShimmer();

                    Get_Customer_List();
                }else {
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rv_generate_quotation.setVisibility(View.GONE);

                }

            }

        });
    }

    private void requestPermissioreadandwrite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            startActivity(new Intent(GenerateQuotationfor.this,CatalogueActivity.class).putExtra("Status","BACK"));

            Toast.makeText(this, "Deny permission", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        startActivity(new Intent(GenerateQuotationfor.this,CatalogueActivity.class).putExtra("Status","BACK"));
        finish();
    }


    public void Get_Customer_List(){
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcustomerlist_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    rv_generate_quotation.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONArray targets = jsonObj.getJSONArray("data");
                            customerLists.clear();
                            for(int i = 0 ; i < targets.length() ; i++){
                                CustomerList customerList = new CustomerList();
                                JSONObject jsonObject = targets.getJSONObject(i);
                                customerList.setId(jsonObject.getString("id"));
                                customerList.setName(jsonObject.getString("name"));
                                customerList.setMobile_number(jsonObject.getString("mobile_number"));
                                customerLists.add(customerList);
                                }
                            GenerateQuotationforAdapter adapter = new GenerateQuotationforAdapter(GenerateQuotationfor.this, customerLists);
                            rv_generate_quotation.setAdapter(adapter);
                        }else if (status_code.equals("0")){
                            customerLists.clear();
                            rv_generate_quotation.setVisibility(View.GONE);
                            shimmerFrameLayout.startShimmer();
                            shimmerFrameLayout.setVisibility(View.VISIBLE);
                          /*  msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(GenerateQuotationfor.this);
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
                                        MyFunctions.setSharedPrefs(GenerateQuotationfor.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(GenerateQuotationfor.this, Constants.USER_ID, "");
                                        startActivity(new Intent(GenerateQuotationfor.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(GenerateQuotationfor.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000);*/ // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(GenerateQuotationfor.this);
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("SEARCH_WORD", SEARCH_WORD);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
}
