package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PostSalesFollowupActivity extends AppCompatActivity {
    EditText edt_remark;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String  status_code , msg ,UPDATE_ENQUIRY_ID, API_TOKEN , contact_id , contact_status,COMMENTS;
    String postsalesfollowup_url = BASE_URL + "postsalesfollowup/postsalesfollowup";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_postsales_followup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = PostSalesFollowupActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(PostSalesFollowupActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(PostSalesFollowupActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(PostSalesFollowupActivity.this, Constants.TOKEN,"");
        edt_remark = findViewById(R.id.edt_remark);
        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");
    }

    public void back(View view) {
        startActivity(new Intent(PostSalesFollowupActivity.this,CustomerDetailsActivity.class).putExtra("Status","PSF (Post Sales Followup)"));
         finish();
    }

    public void updatestatus(View view) {
        COMMENTS = edt_remark.getText().toString().trim();
        if (COMMENTS.equals("")||COMMENTS.isEmpty()){
            edt_remark.setError("Please Fill the Comments");
        }

        if (COMMENTS.equals("")){
            Toast.makeText(PostSalesFollowupActivity.this, "Fill the Details", Toast.LENGTH_SHORT).show();
        }else {
            Post_Sales_Followup();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(PostSalesFollowupActivity.this);
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

    // Api call Test_Drive_Completed
    public void Post_Sales_Followup() {
        StringRequest requests = new StringRequest(Request.Method.POST, postsalesfollowup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            JSONObject jsonObject = jsonObj.getJSONObject("data");
                            contact_id = jsonObject.getString("customer_id");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String status_id = jsonObject.getString("status_id");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.STATUSID,status_id);
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String comment = (String)jsonObject.get("comment");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.COMMENT,comment);
                            String fifteenth_day_followup_id = jsonObject.getString("post_sales_followup_id");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.FIFTEEN_dAY_FOLLOWUP_ID,fifteenth_day_followup_id);
                            String cus_address = (String)jsonObject.get("model_and_suffix");
                            MyFunctions.setSharedPrefs(PostSalesFollowupActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            startActivity(new Intent(PostSalesFollowupActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostSalesFollowupActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(PostSalesFollowupActivity.this);
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
                params.put("ENQUIRY_ID", UPDATE_ENQUIRY_ID);
                params.put("COMMENTS", COMMENTS);
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
