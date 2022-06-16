package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.ExteriorAccessoriesAdapter;
import com.anaamalais.salescrm.Adapter.InterioraccessoriesAdapter;
import com.anaamalais.salescrm.Adapter.UtilityAccessoriesAdapter;
import com.anaamalais.salescrm.Adapter.VariantColorTypeAdapter;
import com.anaamalais.salescrm.Adapter.VariantTypeAdapter;
import com.anaamalais.salescrm.List.ColorDataList;
import com.anaamalais.salescrm.List.ExterioraccessoriesList;
import com.anaamalais.salescrm.List.InterioraccessoriesList;
import com.anaamalais.salescrm.List.UtilityaccessoriesList;
import com.anaamalais.salescrm.List.VariantsAccessList;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.anaamalais.salescrm.common.CommonData;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AccessoriesList extends AppCompatActivity implements InterioraccessoriesAdapter.OnItemsInterioraccessoriesClickListener
        ,ExteriorAccessoriesAdapter.OnItemsExterioraccessoriesClickListener ,UtilityAccessoriesAdapter.OnItemsutilityaccessoriesClickListener {
    TextView txt_model_price,txt_accessories_list_title , txt_start_acticity,extacticity,utlacticity,ex , txt_net_amount;
    ImageView image_model_car;
    String MODELID , STATUS , Variant_id;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN;
    RecyclerView rv_interior_accessories , rv_exterior_accessories , rv_utility_accessories;
    String accessoriesmaster_getvehicledetails_url = BASE_URL + "accessoriesmaster/getvehicledetails";
//    List<ExterioraccessoriesList> exterioraccessoriesLists;
//    List<UtilityaccessoriesList> utilityaccessoriesLists;
    VariantTypeAdapter varianttypeadapter;
    InterioraccessoriesAdapter interioraccessoriesAdapter;
    ExteriorAccessoriesAdapter exterioraccessoriesAdapter;
    UtilityAccessoriesAdapter utilityAccessoriesAdapter;
    ArrayList<String>interioraccessories;
  //  String[]interioraccessories;
    String[]exterioraccessories;
    String[]utilityAccessories;
    int price;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_list);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = AccessoriesList.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(AccessoriesList.this, R.color.white));
        requestQueue = Volley.newRequestQueue(AccessoriesList.this);
        API_TOKEN = MyFunctions.getSharedPrefs(AccessoriesList.this, Constants.TOKEN, "");
        Variant_id = MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.VARIANTID,"");
                //getIntent().getStringExtra("ACCESSLIST");
        STATUS = getIntent().getStringExtra("STATUS");
        txt_accessories_list_title = findViewById(R.id.txt_accessories_list_title);
        rv_interior_accessories = findViewById(R.id.rv_interior_accessories);
        rv_exterior_accessories = findViewById(R.id.rv_exterior_accessories);
        rv_utility_accessories  = findViewById(R.id.rv_utility_accessories);
        txt_start_acticity = findViewById(R.id.txt_start_acticity);
        extacticity = findViewById(R.id.extacticity);
        utlacticity = findViewById(R.id.utlacticity);
        txt_net_amount = findViewById(R.id.txt_net_amount);
        if (STATUS.equals("Interior Accessories")){
            txt_accessories_list_title.setText(STATUS);
            rv_interior_accessories.setVisibility(View.VISIBLE);
            txt_start_acticity.setVisibility(View.VISIBLE);
            Accessoriesmaster_Get_Vehicle_Details();
        }else if (STATUS.equals("Exterior Accessories")){
            txt_accessories_list_title.setText(STATUS);
            rv_exterior_accessories.setVisibility(View.VISIBLE);
            extacticity.setVisibility(View.VISIBLE);
            Accessories_Get_Vehicle_Details();
        }else if (STATUS.equals("Utility Accessories")){
            txt_accessories_list_title.setText(STATUS);
            rv_utility_accessories.setVisibility(View.VISIBLE);
            utlacticity.setVisibility(View.VISIBLE);
            Accessoriesmaster_Vehicle_Details();
        }
        rv_interior_accessories.setHasFixedSize(true);
        rv_interior_accessories.setLayoutManager(new LinearLayoutManager(AccessoriesList.this));

        rv_exterior_accessories.setHasFixedSize(true);
        rv_exterior_accessories.setLayoutManager(new LinearLayoutManager(AccessoriesList.this));

        rv_utility_accessories.setHasFixedSize(true);
        rv_utility_accessories.setLayoutManager(new LinearLayoutManager(AccessoriesList.this));

//        exterioraccessoriesLists = new ArrayList<>();
//        utilityaccessoriesLists = new ArrayList<>();
    }

    public void back(View view) {
        startActivity(new Intent(AccessoriesList.this,ModelDetailsActivity.class));
        // finish();
    }

    public void addaccessories(View view) {
        if (STATUS.equals("Interior Accessories")){
            MyFunctions.setSharedPrefs(AccessoriesList.this,Constants.PRICEINT,txt_net_amount.getText().toString());
            startActivity(new Intent(AccessoriesList.this,ModelDetailsActivity.class)
                    .putExtra("ID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""))
                    .putExtra("MODEL_IMAGE",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODELIMAGE,""))
                    .putExtra("interiorAmount", txt_net_amount.getText().toString()));
        }

    }

    public void  extaddaccessories (View view){
        if (STATUS.equals("Exterior Accessories")){
            MyFunctions.setSharedPrefs(AccessoriesList.this,Constants.PRICEEXT,txt_net_amount.getText().toString());
            startActivity(new Intent(AccessoriesList.this,ModelDetailsActivity.class)
                    .putExtra("ID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""))
                    .putExtra("MODEL_IMAGE",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODELIMAGE,""))
                    .putExtra("exteriorAmount", txt_net_amount.getText().toString()));
        }
    }

    public void utladdaccessories (View view){
        if (STATUS.equals("Utility Accessories")){
            MyFunctions.setSharedPrefs(AccessoriesList.this,Constants.PRICEUTL,txt_net_amount.getText().toString());
            startActivity(new Intent(AccessoriesList.this,ModelDetailsActivity.class)
                    .putExtra("ID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""))
                    .putExtra("MODEL_IMAGE",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODELIMAGE,""))
                    .putExtra("utlAmount", txt_net_amount.getText().toString()));
        }
    }


    public void Accessoriesmaster_Get_Vehicle_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_getvehicledetails_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // shimmerFrameLayout.stopShimmer();
                    //  shimmerFrameLayout.setVisibility(View.GONE);
                   // rv_interior_accessories.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONObject data = jsonObj.getJSONObject("data");
                            JSONArray catalogue = data.getJSONArray("data");
                            for (int k = 0 ; k < catalogue.length() ; k++){
                                JSONObject jsonObject2 = catalogue.getJSONObject(k);
                                String variant_id = jsonObject2.getString("variant_id");
                                if (Variant_id.equals(variant_id)){
                                    String accessorydata =  jsonObject2.getString("accessory_data");
                                    JSONObject accessory_data = new JSONObject(accessorydata);
                                    JSONArray color_data = accessory_data.getJSONArray("interior_accessories");
                                    CommonData.getInteriorData.interioraccessoriesLists.clear();
                                    for (int km = 0 ; km < color_data.length() ; km++){
                                        InterioraccessoriesList model = new InterioraccessoriesList();
                                        JSONObject ColorData = color_data.getJSONObject(km);
                                        System.out.println("jgfgffggf" + ColorData.toString());
                                        model.setInterior_accessory_id(ColorData.getString("interior_accessory_id"));
                                        model.setInterior_accessory_name(ColorData.getString("interior_accessory_name"));
                                        model.setInterior_accessory_price(ColorData.getString("interior_accessory_price"));
                                        CommonData.getInteriorData.interioraccessoriesLists.add(model);
                                    }
                                    interioraccessoriesAdapter = new InterioraccessoriesAdapter(AccessoriesList.this, CommonData.getInteriorData.interioraccessoriesLists, AccessoriesList.this);
                                    rv_interior_accessories.setAdapter(interioraccessoriesAdapter);

                                    // Setting up the events that will occur on each Main-List item click
/*
                                    interioraccessoriesAdapter.setWhenClickListener(new InterioraccessoriesAdapter.OnItemsInterioraccessoriesClickListener(){
                                        @Override
                                        public void onItemClick(InterioraccessoriesList variantsAccessList) {
                                           // Variant_id = ;
                                            String sprice = variantsAccessList.getInterior_accessory_price();
                                            if (sprice.contains("₹")) {
                                                sprice = sprice.replace("₹", "");
                                               // Toast.makeText(AccessoriesList.this, sprice, Toast.LENGTH_SHORT).show();
                                            }

                                            try{
                                                price += Integer.parseInt(sprice);
                                                txt_net_amount.setText(String.valueOf(price));
                                                System.out.println("tttt"+ String.valueOf(price));
                                               // Toast.makeText(AccessoriesList.this, String.valueOf(price), Toast.LENGTH_SHORT).show();
                                            } catch(NumberFormatException ex){ // handle your exception
                                                ex.printStackTrace();
                                            }
                                            interioraccessories = new ArrayList<>();
                                            interioraccessories.add(variantsAccessList.getInterior_accessory_id());
                                            String[] answer = Arrays.copyOf(
                                                    interioraccessories.toArray(), interioraccessories.size(), String[].class);
                                           String [] code = new String[interioraccessories.size()];
                                            System.out.println("rettertetr"+ code);

                                        }
                                    });
*/

                                }
                            }

                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.USER_ID, "");
                                        startActivity(new Intent(AccessoriesList.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                params.put("MODEL_ID", MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""));
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

    public void Accessories_Get_Vehicle_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_getvehicledetails_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // shimmerFrameLayout.stopShimmer();
                    //  shimmerFrameLayout.setVisibility(View.GONE);
                    // rv_interior_accessories.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONObject data = jsonObj.getJSONObject("data");
                            JSONArray catalogue = data.getJSONArray("data");
                            for (int k = 0 ; k < catalogue.length() ; k++){
                                JSONObject jsonObject2 = catalogue.getJSONObject(k);
                                String variant_id = jsonObject2.getString("variant_id");
                                if (Variant_id.equals(variant_id)){
                                    String accessorydata =  jsonObject2.getString("accessory_data");
                                    JSONObject accessory_data = new JSONObject(accessorydata);
                                    JSONArray color_data = accessory_data.getJSONArray("exterior_accessories");
                                    CommonData.getInteriorData.interioraccessoriesLists.clear();
                                    for (int km = 0 ; km < color_data.length() ; km++){
                                        ExterioraccessoriesList model = new ExterioraccessoriesList();
                                        JSONObject ColorData = color_data.getJSONObject(km);
                                        System.out.println("jgfgffggf" + ColorData.toString());
                                        model.setExterior_acessory_id(ColorData.getString("exterior_acessory_id"));
                                        model.setExterior_acessory_name(ColorData.getString("exterior_acessory_name"));
                                        model.setExterior_acessory_price(ColorData.getString("exterior_acessory_price"));
                                        CommonData.getInteriorData.exterioraccessoriesLists.add(model);
                                    }
                                    exterioraccessoriesAdapter = new ExteriorAccessoriesAdapter(AccessoriesList.this, CommonData.getInteriorData.exterioraccessoriesLists, AccessoriesList.this);
                                    rv_exterior_accessories.setAdapter(exterioraccessoriesAdapter);

                                    // Setting up the events that will occur on each Main-List item click
/*
                                    exterioraccessoriesAdapter.setWhenClickListener(new ExteriorAccessoriesAdapter.OnItemsExterioraccessoriesClickListener(){
                                        @Override
                                        public void onItemClick(ExterioraccessoriesList exterioraccessoriesList) {
                                            Variant_id = exterioraccessoriesList.getExterior_acessory_id();
                                            String sprice = exterioraccessoriesList.getExterior_acessory_price();
                                            if (sprice.contains("₹")) {
                                                sprice = sprice.replace("₹", "");
                                              //  Toast.makeText(AccessoriesList.this, sprice, Toast.LENGTH_SHORT).show();
                                            }

                                            try{
                                                price += Integer.parseInt(sprice);
                                                txt_net_amount.setText(String.valueOf(price));
                                                System.out.println("tttt"+ String.valueOf(price));
                                               // Toast.makeText(AccessoriesList.this, String.valueOf(price), Toast.LENGTH_SHORT).show();
                                            } catch(NumberFormatException ex){ // handle your exception
                                                ex.printStackTrace();
                                            }
                                        }
                                    });

*/
                                }
                            }
                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.USER_ID, "");
                                        startActivity(new Intent(AccessoriesList.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                params.put("MODEL_ID", MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""));
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

    public void Accessoriesmaster_Vehicle_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_getvehicledetails_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // shimmerFrameLayout.stopShimmer();
                    //  shimmerFrameLayout.setVisibility(View.GONE);
                    // rv_interior_accessories.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONObject data = jsonObj.getJSONObject("data");
                            JSONArray catalogue = data.getJSONArray("data");
                            for (int k = 0 ; k < catalogue.length() ; k++){
                                JSONObject jsonObject2 = catalogue.getJSONObject(k);
                                String variant_id = jsonObject2.getString("variant_id");
                                if (Variant_id.equals(variant_id)){
                                    String accessorydata =  jsonObject2.getString("accessory_data");
                                    JSONObject accessory_data = new JSONObject(accessorydata);
                                    JSONArray color_data = accessory_data.getJSONArray("utility_accessories");
                                    CommonData.getInteriorData.utilityaccessoriesLists.clear();
                                    for (int km = 0 ; km < color_data.length() ; km++){
                                        UtilityaccessoriesList model = new UtilityaccessoriesList();
                                        JSONObject ColorData = color_data.getJSONObject(km);
                                        System.out.println("jgfgffggf" + ColorData.toString());
                                        model.setUtility_accessory_id(ColorData.getString("utility_accessory_id"));
                                        model.setUtility_accessory_name(ColorData.getString("utility_accessory_name"));
                                        model.setUtility_accessory_price(ColorData.getString("utility_accessory_price"));
                                        CommonData.getInteriorData.utilityaccessoriesLists.add(model);
                                    }
                                    utilityAccessoriesAdapter = new UtilityAccessoriesAdapter(AccessoriesList.this, CommonData.getInteriorData.utilityaccessoriesLists,AccessoriesList.this);
                                    rv_utility_accessories.setAdapter(utilityAccessoriesAdapter);

                                    // Setting up the events that will occur on each Main-List item click
                          /*          utilityAccessoriesAdapter.setWhenClickListener(new UtilityAccessoriesAdapter.OnItemsutilityaccessoriesClickListener(){
                                        @Override
                                        public void onItemClick(UtilityaccessoriesList utilityaccessoriesList) {
                                            Variant_id = utilityaccessoriesList.getUtility_accessory_id();
                                            String sprice = utilityaccessoriesList.getUtility_accessory_price();
                                            if (sprice.contains("₹")) {
                                                sprice = sprice.replace("₹", "");
                                               // Toast.makeText(AccessoriesList.this, sprice, Toast.LENGTH_SHORT).show();
                                            }

                                            try{
                                                price += Integer.parseInt(sprice);
                                                txt_net_amount.setText(String.valueOf(price));
                                                System.out.println("tttt"+ String.valueOf(price));
                                              //  Toast.makeText(AccessoriesList.this, String.valueOf(price), Toast.LENGTH_SHORT).show();
                                            } catch(NumberFormatException ex){ // handle your exception
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
*/
                                }
                            }
                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(AccessoriesList.this, Constants.USER_ID, "");
                                        startActivity(new Intent(AccessoriesList.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccessoriesList.this);
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
                params.put("MODEL_ID", MyFunctions.getSharedPrefs(AccessoriesList.this,Constants.MODEL_ID,""));
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AccessoriesList.this,ModelDetailsActivity.class));

    }

    @Override
    public void onItemClick(InterioraccessoriesList interioraccessoriesList) {

    }

    @Override
    public void onItemClick(ExterioraccessoriesList exterioraccessoriesList) {

    }

    @Override
    public void onItemClick(UtilityaccessoriesList utilityaccessoriesList) {

    }

    @Override
    public void onItemUnchecked(int position, boolean isTrue) {
        String [] codes = new String[CommonData.getInteriorData.interioraccessoriesLists.size()];
        String [] extcodes = new String[CommonData.getInteriorData.exterioraccessoriesLists.size()];
        String [] ultcodes = new String[CommonData.getInteriorData.utilityaccessoriesLists.size()];

        if(CommonData.getInteriorData.interioraccessoriesLists.size()>position) {
            CommonData.getInteriorData.interioraccessoriesLists.get(position).setOn(isTrue);

            if(CommonData.getInteriorData.interioraccessoriesLists.get(position).isOn()){
                codes[position] = CommonData.getInteriorData.interioraccessoriesLists.get(position).getInterior_accessory_id();
            }else{
                codes[position] = null;
            }
            MyFunctions.setSharedPrefs(this, Constants.ACCESSORIESLIST,Arrays.toString(codes));
            calculateTotalAmount(CommonData.getInteriorData.interioraccessoriesLists);
        }

        if(CommonData.getInteriorData.exterioraccessoriesLists.size()>position) {
            CommonData.getInteriorData.exterioraccessoriesLists.get(position).setOn(isTrue);

            if(CommonData.getInteriorData.exterioraccessoriesLists.get(position).isOn()){
                extcodes[position] = CommonData.getInteriorData.exterioraccessoriesLists.get(position).getExterior_acessory_id();
            }else{
                extcodes[position] = null;
            }
            MyFunctions.setSharedPrefs(this, Constants.ACCESSORIESLIST,Arrays.toString(extcodes));
            calculateexteriorTotalAmount(CommonData.getInteriorData.exterioraccessoriesLists);
        }

        if(CommonData.getInteriorData.utilityaccessoriesLists.size()>position) {
            CommonData.getInteriorData.utilityaccessoriesLists.get(position).setOn(isTrue);

            if(CommonData.getInteriorData.utilityaccessoriesLists.get(position).isOn()){
                ultcodes[position] = CommonData.getInteriorData.utilityaccessoriesLists.get(position).getUtility_accessory_id();
            }else{
                ultcodes[position] = null;
            }
            MyFunctions.setSharedPrefs(this, Constants.ACCESSORIESLIST,Arrays.toString(ultcodes));
            calculateutlTotalAmount(CommonData.getInteriorData.utilityaccessoriesLists);
        }
    }

    private void calculateTotalAmount(List<InterioraccessoriesList> list) {
        double totalAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOn()) {
                try{
                String price = list.get(i).getInterior_accessory_price().replace("₹", "").trim();
                totalAmount += Double.parseDouble(price);
                }catch (Exception e){}
            }
        }
        txt_net_amount.setText(new DecimalFormat("##.##").format(totalAmount) + "");
    }

    private void calculateexteriorTotalAmount(List<ExterioraccessoriesList> list) {
        double totalAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOn()) {
                try{
                    String price = list.get(i).getExterior_acessory_price().replace("₹", "").trim();
                    totalAmount += Double.parseDouble(price);
                }catch (Exception e){}
            }
        }
        txt_net_amount.setText(new DecimalFormat("##.##").format(totalAmount) + "");
    }

    private void calculateutlTotalAmount(List<UtilityaccessoriesList> list) {
        double totalAmount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isOn()) {
                try{
                    String price = list.get(i).getUtility_accessory_price().replace("₹", "").trim();
                    totalAmount += Double.parseDouble(price);
                }catch (Exception e){}
            }
        }
        txt_net_amount.setText(new DecimalFormat("##.##").format(totalAmount) + "");
    }
}
