package com.anaamalais.salescrm;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.LogPrinter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;
import static com.anaamalais.salescrm.Utils.Constants.DEVICE_ID;

import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText edt_employee_id , edt_password;
    TextView signup;
    ImageView markup;
    String AUTH_KEY , header_data , LOGOUT_DEVICEID ;
    String authentication_url = BASE_URL + "authentication/login";
    RequestQueue requestQueue;
    String employee_id , password , device_type , device_id , status_code , msg ,token ,emp_id , name ,
    email , profile_image , dob , address , user_id  , json_as_string;
    int user_type , phone;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    boolean isEmailValid, isPasswordValid, isPasswordVisible;
    ProgressBar pbar_login;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Window window = LoginActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.red));
        requestQueue = Volley.newRequestQueue(LoginActivity.this);
        signup = findViewById(R.id.txt_login);
        edt_employee_id = findViewById(R.id.edt_employee_id);
        edt_password = findViewById(R.id.edt_password);
        markup = findViewById(R.id.ima_second);
        pref = getSharedPreferences("LoginActivity", 0);
        editor = pref.edit();
        device_id =  getIntent().getStringExtra("DEVICEID");
        MyFunctions.setSharedPrefs(LoginActivity.this,Constants.DEVICEID,device_id);

        if(MyFunctions.getSharedPrefs(LoginActivity.this, Constants.IS_LOGIN,false)) {
            if (MyFunctions.getSharedPrefs(LoginActivity.this, "userid", "").length() > 0) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        edt_employee_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
               // employee_id = s.toString();
               if (isValidMobileNumber(s.toString())){
                   edt_employee_id.setFilters(new InputFilter[]{
                           new InputFilter.LengthFilter(10)
                   });
               }else if (!isValidIdentifier(s.toString())){
                  // edt_employee_id.setError("Please Enter the Correct Employee Id");
               }

            }
        });

        edt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[RIGHT].getBounds().width())) {
                        int selection = edt_password.getSelectionEnd();
                        if (isPasswordVisible) {
                            // set drawable image
                            edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                            // hide Password
                            edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            isPasswordVisible = false;
                        } else  {
                            // set drawable image
                            edt_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                            // show Password
                            edt_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            isPasswordVisible = true;
                        }
                        edt_password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid_signup();
            }
        });

    }

    // validating email id
    private boolean isValidPassword(String identifier) {
        String regex= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        Pattern p = Pattern.compile(regex);

        if (identifier == null) {
            return false;
        }
        Matcher m = p.matcher(identifier);
        return m.matches();
    }

    // Function to employee_id the identifier.
    public static boolean isValidIdentifier(String identifier)
    {
        String regex = "^([a-zA-Z_$][a-zA-Z\\d_$]*)$";

        Pattern p = Pattern.compile(regex);

        if (identifier == null) {
            return false;
        }
        Matcher m = p.matcher(identifier);
        return m.matches();
    }

    // Function to employee_id the identifier.
    public static boolean isValidMobileNumber(String identifier)
    {
        String regex = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

        Pattern p = Pattern.compile(regex);

        if (identifier == null) {
            return false;
        }
        Matcher m = p.matcher(identifier);
        return m.matches();
    }

    @SuppressLint("HardwareIds")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void valid_signup() {

        employee_id = edt_employee_id.getText().toString();
        if (!isValidIdentifier(employee_id)||employee_id.isEmpty()||(edt_employee_id.equals(""))||(edt_employee_id.toString().trim().length() < 10)) {
            Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_set_error);
            customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
           // edt_employee_id.setError("Please Enter the Phone Number",customErrorDrawable);
        }

        password = edt_password.getText().toString();
        if (!isValidPassword(password)){
            Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_set_error);
            customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
            edt_password.setError("Please Enter the Correct Password",customErrorDrawable);

        }

        header_data = "#%ClOuD@NoWtECh#NoLoGy1@#)({@p!-$@lT-kEy}+)##({!@ToYoT@})%#"+device_id+employee_id;
        try
        { MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(header_data.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuilder buffer = new StringBuilder();
            for (byte b : bytes)
            {
                buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            AUTH_KEY = buffer.toString();
            System.out.println("AUTH" + AUTH_KEY);
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }

      //  ||!isValidPassword(password)

        if (employee_id.equals("")||password.equals("")){
            Toast.makeText(LoginActivity.this, "Please Fill all the Details", Toast.LENGTH_SHORT).show();
        }else {

            Authentication_Login();
            //initialize the progress dialog and show it
            progressDialog = new ProgressDialog(LoginActivity.this);
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


    // Api call Authentication_Login

    public void Authentication_Login(){
        System.out.println("Auth_url"+ authentication_url);

        StringRequest request = new StringRequest(Request.Method.POST, authentication_url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if(status_code.equals("1")){

                            msg = (String) jsonObj.getString("msg");
                            System.out.println("Check statusMessage of Login Activity:" + msg);

                            token = (String) jsonObj.getString("token");
                            System.out.println("Check statusMessage of Login Activity:" + token);

                            JSONObject user_details = jsonObj.getJSONObject("user_details");

                            emp_id = (String) user_details.get("emp_id");

                            name = (String) user_details.get("name");

                            email = (String) user_details.get("email");

                            profile_image = (String) user_details.get("profile_image");

                            phone = (Integer) user_details.getInt("phone");

                            dob = (String) user_details.get("dob");

                            address = (String) user_details.get("address");

                            user_id = (String) user_details.get("user_id");

                            user_type = (Integer) user_details.getInt("user_type");

                            String usertype = Integer.toString(user_type);
                            editor.putString("userid",user_id);
                            editor.putString("emp_id",emp_id);
                            editor.putString("token",token);
                            editor.putString("name",name);
                            editor.putString("user_type",usertype);
                            editor.commit();
                            MyFunctions.setSharedPrefs(LoginActivity.this, Constants.IS_LOGIN, true);
                            MyFunctions.setSharedPrefs(LoginActivity.this, Constants.USER_ID,user_id);
                            MyFunctions.setSharedPrefs(LoginActivity.this,Constants.EMP_ID,emp_id);
                            MyFunctions.setSharedPrefs(LoginActivity.this,Constants.TOKEN,token);
                            MyFunctions.setSharedPrefs(LoginActivity.this,Constants.EMP_NAME,name);
                            MyFunctions.setSharedPrefs(LoginActivity.this, Constants.USER_TYPE,usertype);
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage("Login Success Full");
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }, 2000); // the timer will count 5 seconds....

                        }else if (status_code.equals("0")){
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

                        } else{
                           // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("USERNAME",employee_id);
                params.put("PASSWORD",password);
                params.put("DEVICE_TYPE","1");
                params.put("DEVICE_ID",device_id);
                System.out.println("PRINTF" + params);
                return params;
            }

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("AUTH_KEY", AUTH_KEY);

                return params;
            }
        };
        requestQueue.add(request);
    }
}
