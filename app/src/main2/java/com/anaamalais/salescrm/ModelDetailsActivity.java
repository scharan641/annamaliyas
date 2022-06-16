package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.CatalogueAdapter;
import com.anaamalais.salescrm.Adapter.VariantColorTypeAdapter;
import com.anaamalais.salescrm.Adapter.VariantTypeAdapter;
import com.anaamalais.salescrm.List.CatelogeList;
import com.anaamalais.salescrm.List.ColorDataList;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ModelDetailsActivity extends AppCompatActivity {
    TextView txt_model_price , txt_interior_accessories_amount , txt_exterior_accessories_amount
            , txt_utility_accessories_amount , txt_generate_quotation , txt_model_name;
    ImageView image_model_car;
    String MODELID , MODEL_IMAGE , Variant_id,COLOR_ID;
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN , vehcledetail , vehcleimage;
    RecyclerView rv_variant_list , rv_variant_color;
    String accessoriesmaster_getvehicledetails_url = BASE_URL + "accessoriesmaster/getvehicledetails";
    List<VariantsAccessList> variantsAccessLists;
    List<ColorDataList> colorDataLists;
    VariantTypeAdapter varianttypeadapter;
    VariantColorTypeAdapter variantColorTypeAdapter;
    RelativeLayout lin_interior_accessories , lin_exterior_accessories , lin_utility_accessories;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_details);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = ModelDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(ModelDetailsActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(ModelDetailsActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(ModelDetailsActivity.this, Constants.TOKEN, "");
        MODELID = getIntent().getStringExtra("ID");
        MODEL_IMAGE = getIntent().getStringExtra("MODEL_IMAGE");
        txt_model_price = findViewById(R.id.txt_model_price);
        image_model_car = findViewById(R.id.image_model_car);
        txt_interior_accessories_amount = findViewById(R.id.txt_interior_accessories_amount);
        txt_exterior_accessories_amount = findViewById(R.id.txt_exterior_accessories_amount);
        txt_utility_accessories_amount = findViewById(R.id.txt_utility_accessories_amount);
        lin_interior_accessories = findViewById(R.id.lin_interior_accessories);
        lin_exterior_accessories = findViewById(R.id.lin_exterior_accessories);
        lin_utility_accessories  = findViewById(R.id.lin_utility_accessories);
        txt_generate_quotation = findViewById(R.id.txt_generate_quotation);
        txt_model_name = findViewById(R.id.txt_model_name);
        rv_variant_list = findViewById(R.id.rv_variant_list);
        rv_variant_list.setHasFixedSize(true);
        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv_variant_list.setLayoutManager(manager);


        rv_variant_color = findViewById(R.id.rv_variant_color);
        rv_variant_color.setHasFixedSize(true);
        /**
         Simple GridLayoutManager that spans two columns
         **/
        GridLayoutManager manager1 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv_variant_color.setLayoutManager(manager1);
        variantsAccessLists = new ArrayList<>();
        colorDataLists = new ArrayList<>();

        if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEINT,"")!=null){
            txt_interior_accessories_amount.setText("₹"+MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEINT,""));
        }else {
            txt_interior_accessories_amount.setText("₹ 00.00");
        }

        if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEEXT,"")!=null){
            txt_exterior_accessories_amount.setText("₹"+MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEEXT,""));
        }else {
            txt_exterior_accessories_amount.setText("₹ 00.00");
        }

        if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEUTL,"")!=null){
            txt_utility_accessories_amount.setText("₹"+MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.PRICEUTL,""));
        }else {
            txt_utility_accessories_amount.setText("₹ 00.00");
        }

        lin_interior_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.VARIANTID,"")!=null){
                    startActivity(new Intent(ModelDetailsActivity.this,AccessoriesList.class).putExtra("ACCESSLIST",Variant_id).putExtra("STATUS","Interior Accessories"));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
                    builder.setTitle("USER MESSAGE");
                    builder.setMessage("Please Choose Variant!!");
                    builder.setCancelable(true);
                    final AlertDialog closedialog= builder.create();
                    closedialog.show();

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            closedialog.dismiss();
                            timer2.cancel(); //this will cancel the timer of the system
                            if (msg.equals("Invalid API_TOKEN!")){
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                finish();
                            }
                        }
                    }, 3000); // the timer will count 5 seconds....
                }

            }
        });

        lin_exterior_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.VARIANTID,"")!=null){
                    startActivity(new Intent(ModelDetailsActivity.this,AccessoriesList.class).putExtra("ACCESSLIST",Variant_id).putExtra("STATUS","Exterior Accessories"));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
                    builder.setTitle("USER MESSAGE");
                    builder.setMessage("Please Choose Variant!!");
                    builder.setCancelable(true);
                    final AlertDialog closedialog= builder.create();
                    closedialog.show();

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            closedialog.dismiss();
                            timer2.cancel(); //this will cancel the timer of the system
                            if (msg.equals("Invalid API_TOKEN!")){
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                finish();
                            }
                        }
                    }, 3000); // the timer will count 5 seconds....
                }

            }
        });


        lin_utility_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.VARIANTID,"")!=null){
                    startActivity(new Intent(ModelDetailsActivity.this,AccessoriesList.class).putExtra("ACCESSLIST",Variant_id).putExtra("STATUS","Utility Accessories"));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
                    builder.setTitle("USER MESSAGE");
                    builder.setMessage("Please Choose Variant!!");
                    builder.setCancelable(true);
                    final AlertDialog closedialog= builder.create();
                    closedialog.show();

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            closedialog.dismiss();
                            timer2.cancel(); //this will cancel the timer of the system
                            if (msg.equals("Invalid API_TOKEN!")){
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                finish();
                            }
                        }
                    }, 3000); // the timer will count 5 seconds....
                }
            }
        });

        txt_generate_quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.VARIANTID,"")!=null && MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.COLORID,"")!=null){
                    startActivity(new Intent(ModelDetailsActivity.this,GenerateQuotationfor.class));
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
                    builder.setTitle("USER MESSAGE");
                    builder.setMessage("Missing VARIANT OR COLORID");
                    builder.setCancelable(true);
                    final AlertDialog closedialog= builder.create();
                    closedialog.show();

                    final Timer timer2 = new Timer();
                    timer2.schedule(new TimerTask() {
                        public void run() {
                            closedialog.dismiss();
                            timer2.cancel(); //this will cancel the timer of the system
                            if (msg.equals("Invalid API_TOKEN!")){
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                finish();
                            }
                        }
                    }, 3000); // the timer will count 5 seconds....
                }
            }
        });


        Accessoriesmaster_Get_Vehicle_Details();

    }

    public void back(View view) {
        startActivity(new Intent(ModelDetailsActivity.this,CatalogueActivity.class));
       // finish();
    }

    public void sharequotation(View view) {
       /* File outputFile = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOCUMENTS), "quotation_format.pdf");
        Uri uri = Uri.fromFile(outputFile);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(share);*/
       // startActivity(new Intent(ModelDetailsActivity.this,GenerateQuotationfor.class));
    }

    public void Accessoriesmaster_Get_Vehicle_Details(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_getvehicledetails_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                   // shimmerFrameLayout.stopShimmer();
                  //  shimmerFrameLayout.setVisibility(View.GONE);
                    rv_variant_list.setVisibility(View.VISIBLE);
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){
                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);
                            JSONObject data = jsonObj.getJSONObject("data");
                            JSONObject vehcle_detail = jsonObj.getJSONObject("vehcle_detail");
                            vehcledetail =  vehcle_detail.getString("name");
                            txt_model_name.setText(vehcledetail);
                            vehcleimage = vehcle_detail.getString("image");
                            Glide.with(ModelDetailsActivity.this).load(vehcleimage).into(image_model_car);
                            JSONArray catalogue = data.getJSONArray("variants");
                            for (int k = 0 ; k < catalogue.length() ; k++){
                                VariantsAccessList model = new VariantsAccessList();
                                JSONObject jsonObject2 = catalogue.getJSONObject(k);
                                System.out.println("jgfgffggf" + jsonObject2.toString());
                                model.setVariant_id(jsonObject2.getString("variant_id"));
                                model.setVariant(jsonObject2.getString("variant"));
                                model.setFuel_type(jsonObject2.getString("fuel_type"));
                                variantsAccessLists.add(model);
                            }
                            varianttypeadapter = new VariantTypeAdapter(ModelDetailsActivity.this, variantsAccessLists);
                            rv_variant_list.setAdapter(varianttypeadapter);

                            // Setting up the events that will occur on each Main-List item click
                            varianttypeadapter.setWhenClickListener(new VariantTypeAdapter.OnItemsClickListener(){
                                @Override
                                public void onItemClick(VariantsAccessList variantsAccessList) {
                                    Variant_id = variantsAccessList.getVariant_id();
                                    MyFunctions.setSharedPrefs(ModelDetailsActivity.this,Constants.VARIANTID,Variant_id);
                                    //setRVTwoList(rvOneModel.getNum());
                                    Accessoriesmaster_Get_Vehicle_Details_Color();
                                }
                            });



                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
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
                                        MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
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
                params.put("MODEL_ID", MODELID);
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

    public void Accessoriesmaster_Get_Vehicle_Details_Color(){
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_getvehicledetails_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // shimmerFrameLayout.stopShimmer();
                    //  shimmerFrameLayout.setVisibility(View.GONE);
                    rv_variant_color.setVisibility(View.VISIBLE);
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
                                   JSONArray color_data = jsonObject2.getJSONArray("color_data");
                                   colorDataLists.clear();
                                   if (color_data.length() != 0){
                                       for (int km = 0 ; km < color_data.length() ; km++){
                                           ColorDataList model = new ColorDataList();
                                           JSONObject ColorData = color_data.getJSONObject(km);
                                           System.out.println("jgfgffggf" + ColorData.toString());
                                           model.setColor(ColorData.getString("color"));
                                           model.setColor_id(ColorData.getString("color_id"));
                                           model.setImage(ColorData.getString("image"));
                                           model.setPrice(ColorData.getString("price"));
                                           colorDataLists.add(model);
                                       }
                                       variantColorTypeAdapter = new VariantColorTypeAdapter(ModelDetailsActivity.this, colorDataLists);
                                       rv_variant_color.setAdapter(variantColorTypeAdapter);
                                       variantColorTypeAdapter.notifyDataSetChanged();
                                       // Setting up the events that will occur on each Main-List item click
                                       variantColorTypeAdapter.setWhenClickListener(new VariantColorTypeAdapter.OnItemsColorClickListener(){
                                           @Override
                                           public void onItemClick(ColorDataList colorDataList) {
                                               COLOR_ID = colorDataList.getColor_id();
                                               MyFunctions.setSharedPrefs(ModelDetailsActivity.this,Constants.COLORID,COLOR_ID);
                                               txt_model_price.setText(colorDataList.getPrice());
                                               Glide.with(ModelDetailsActivity.this).load(colorDataList.getImage()).into(image_model_car);
                                           }
                                       });
                                   }else {
                                       AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
                                       builder.setTitle("USER MESSAGE");
                                       builder.setMessage("NO COLOR IS AVALIABLE FOR THIS VAIANT ID");
                                       builder.setCancelable(true);
                                       final AlertDialog closedialog= builder.create();
                                       closedialog.show();

                                       final Timer timer2 = new Timer();
                                       timer2.schedule(new TimerTask() {
                                           public void run() {
                                               closedialog.dismiss();
                                               timer2.cancel(); //this will cancel the timer of the system
                                               if (msg.equals("Invalid API_TOKEN!")){
                                                   MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                                   MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                                   startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                                   finish();
                                               }
                                           }
                                       }, 3000); // the timer will count 5 seconds....

                                   }


                               }
                            }

                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
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
                                        MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(ModelDetailsActivity.this, Constants.USER_ID, "");
                                        startActivity(new Intent(ModelDetailsActivity.this, LoginActivity.class).putExtra("DEVICEID",MyFunctions.getSharedPrefs(ModelDetailsActivity.this,Constants.DEVICEID,"")));
                                        finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else{
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModelDetailsActivity.this);
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
                params.put("MODEL_ID", MODELID);
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
