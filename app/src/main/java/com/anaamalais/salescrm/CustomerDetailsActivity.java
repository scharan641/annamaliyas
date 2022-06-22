package com.anaamalais.salescrm;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.AllContactAdapter;
import com.anaamalais.salescrm.Adapter.ContactStatusAdapter;
import com.anaamalais.salescrm.Adapter.FinancePitchTypeArrayAdapter;
import com.anaamalais.salescrm.List.ContactStatusList;
import com.anaamalais.salescrm.List.FinancePitchList;
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
import com.bumptech.glide.Glide;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerDetailsActivity extends AppCompatActivity {
    TextView txt_cus_name ,txt_cus_call,txt_cus_location,txt_satus_text,edt_time,txt_telephone,txtdDirectvisit,txt_status,txt_scheduled_for_test_drive,txt_test_drive_completed
            ,txt_booking_completed,txt_booking_follow_up,txt_invoice_completed,txt_pre_delivery
    ,txt_delivery_completed,txt_psf,txt_fifteenth_day_followup,txt_onek_followup,txt_cancel,txt_test_drive_date
        ,txt_dl_number , txt_cus_car_model , txt_one_k_follow_up_comments ,delivery_complete_date , delivery_complete_time , txt_delivery_complete_comments , delivery_complete_referral_name
                ,delivery_complete_referral_contact_no , drop_reason ,   txt_invoice_complete_comments , delivery_invoice_time , invoice_complete_date
    ,txt_status_follow_up_text,txt_history,txt_complete_details,txt_customer_details_title,
            txt_add_task,txt_drop,txt_date_time,txt_followup_type , txt_foverall_td_rate  ,txt_overall_condition_rate , txt_sales_consultant_rate
            ,txt_overall_sales_consultant_rate ,txt_booking_model_suffix , txt_booking_interior_colour  ,
            txt_booking_exterior_colour , txt_overall_booking_date , txt_booking_booking_amount
            , txt_booking_payment_mode , txt_dlphoto_details , txt_dlnumber_details ;
    private android.app.AlertDialog.Builder alertDialog2;
    TextView txt_accessories_fitment_reason ,  txt_accessories_fitment_status , extended_warranty , fastag , rto_process ,
            txt_accessories_fitment_reasons , allocation_pdi;

    TextView  txt_expectedinvoicedate , txt_expecteddownpaymentdate , txt_expected_downpayment_amount
            , txt_netamunt , txt_offerss , txt_fasttag , txt_warranty , txt_accessories ,
            txt_registration , txt_insurance , txt_road_tax , txt_tcs_interest , txt_tcs_rate ,
            txt_ex_showroom_price , txt_loan_amount , txt_query_clearance , txt_login_date_time , txt_login_completed , txt_paper_documents_collected ;

    String customer_details_status , REASON , getDlimage , getIntentDlimage;
    SwitchCompat swt_follow_up,swt_status_follow_up,swt_pre_delivery_checklist;
    LinearLayout lin_follow_up,lin_status_list,lin_intersted_id,lin_test_derive_details,lin_rating_details,
            lin_predelivery_option,lin_drop_option,lin_reason_interested,lin_accessories_fitment
            ,lin_accessories_fitment_witing , lin_booking_complete,lin_one_k_follow_up,lin_booking_follow_up,
            lin_delivery_complete , accessories_fitment,accessories_fitments,rel_pre_delivery_checklist;
    RelativeLayout rel_next_follow_up,rel_status_follow_up_text,rel_time_follow_up,
            rel_staus , rel_location , rel_model;
    ImageView Img_phone , Img_message , Img_whatapp ;
    EditText edt_date,edt_drop_option,edt_remark;
    int mYear, mDay ,mMonth,mMinute,mHour;
    Boolean isOnePressed = false, isSecondPlace = false;
    ImageView Img_dl_photo , img_right;
    Animation layout_animate;
    String follow_up_date , follow_up_time , follow_up_id,PHONE,contact_id,contact_status;
    String CONTACT_ID , FOLLOW_UP_DATE , FOLLOW_UP_TIME , FOLLOW_UP_TYPE , FOLLOW_UP_REMARK;
    EditText edt_reason_interested;
    String customer_getcontactallstatus_url = BASE_URL + "customer/getcontactallstatus";
    String customer_createcontactfollowup_url = BASE_URL + "customer/createcontactfollowup";
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String  status_code , msg ,token, API_TOKEN;
    List<ContactStatusList>contactStatusLists;
    RecyclerView rv_contact_status;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        Window window = CustomerDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(CustomerDetailsActivity.this, R.color.customerdetails));
        requestQueue = Volley.newRequestQueue(CustomerDetailsActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.TOKEN,"");
        txt_satus_text = findViewById(R.id.txt_satus_text);
        customer_details_status = getIntent().getStringExtra("Status");
        getDlimage = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.DLPHOTO,"");
        getIntentDlimage = getIntent().getStringExtra("dl_photo");

        txt_satus_text.setText(customer_details_status);
        txt_cus_name = findViewById(R.id.txt_cus_name);
        String CUSTOMER_NAME = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CUSTOMER_NAME,"");
        txt_cus_name.setText(CUSTOMER_NAME);
        txt_cus_call = findViewById(R.id.txt_cus_call);
        String CUSTOMER_PHONE= MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CUSTOMER_PHONE,"");
        txt_cus_call.setText(CUSTOMER_PHONE);
        txt_cus_location = findViewById(R.id.txt_cus_location);
        String CUSTOMER_ADDRESS = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CUSTOMER_ADDRESS,"");
        txt_cus_location.setText(CUSTOMER_ADDRESS);
        PHONE= MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CUSTOMER_PHONE,"");
        lin_follow_up = findViewById(R.id.lin_follow_up);
        swt_follow_up = findViewById(R.id.swt_follow_up);
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        txt_telephone = findViewById(R.id.txt_telephone);
        txtdDirectvisit = findViewById(R.id.txtdDirectvisit);
        txt_status = findViewById(R.id.txt_status);
        lin_status_list = findViewById(R.id.lin_status_list);
        txt_cancel = findViewById(R.id.txt_cancel);
        lin_intersted_id  = findViewById(R.id.lin_intersted_id);
        rel_next_follow_up = findViewById(R.id.rel_next_follow_up);
        txt_status_follow_up_text = findViewById(R.id.txt_status_follow_up_text);
        swt_status_follow_up = findViewById(R.id.swt_status_follow_up);
        rel_status_follow_up_text = findViewById(R.id.rel_status_follow_up_text);
        rel_time_follow_up = findViewById(R.id.rel_time_follow_up);
        lin_test_derive_details = findViewById(R.id.lin_test_derive_details);
        lin_rating_details = findViewById(R.id.lin_rating_details);
        swt_pre_delivery_checklist = findViewById(R.id.swt_pre_delivery_checklist);
        rel_pre_delivery_checklist = findViewById(R.id.rel_pre_delivery_checklist);
        lin_predelivery_option = findViewById(R.id.lin_predelivery_option);
        lin_drop_option = findViewById(R.id.lin_drop_option);
        drop_reason = findViewById(R.id.drop_reason);
        txt_history = findViewById(R.id.txt_history);
        txt_complete_details = findViewById(R.id.txt_complete_details);
        Img_dl_photo = findViewById(R.id.Img_dl_photo);
        txt_customer_details_title = findViewById(R.id.txt_customer_details_title);
        lin_reason_interested = findViewById(R.id.lin_reason_interested);
        txt_add_task = findViewById(R.id.txt_add_task);
        lin_accessories_fitment = findViewById(R.id.lin_accessories_fitment);
        lin_accessories_fitment_witing = findViewById(R.id.lin_accessories_fitment_witing);
        txt_date_time = findViewById(R.id.txt_date_time);
        txt_followup_type = findViewById(R.id.txt_followup_type);
        txt_cus_car_model = findViewById(R.id.txt_cus_car_model);
     //   txt_drop = findViewById(R.id.txt_drop);
        rel_staus = findViewById(R.id.rel_staus);
        edt_reason_interested = findViewById(R.id.edt_reason_interested);
        Img_phone = findViewById(R.id.Img_phone);
        Img_message = findViewById(R.id.Img_message);
        Img_whatapp = findViewById(R.id.Img_whatapp);
        edt_remark = findViewById(R.id.edt_remark);
        txt_dl_number = findViewById(R.id.txt_dl_number);
        txt_test_drive_date = findViewById(R.id.txt_test_drive_date);
        rel_location = findViewById(R.id.rel_location);
        rel_model = findViewById(R.id.rel_model);
        rv_contact_status  = findViewById(R.id.rv_contact_status);
        rv_contact_status.setHasFixedSize(true);
        rv_contact_status.setLayoutManager(new LinearLayoutManager(CustomerDetailsActivity.this));
        contactStatusLists = new ArrayList<>();
        txt_foverall_td_rate  = findViewById(R.id.txt_foverall_td_rate);
        txt_overall_condition_rate = findViewById(R.id.txt_overall_condition_rate);
        txt_sales_consultant_rate = findViewById(R.id.txt_sales_consultant_rate);
        txt_overall_sales_consultant_rate = findViewById(R.id.txt_overall_sales_consultant_rate);
        txt_booking_model_suffix  = findViewById(R.id.txt_booking_model_suffix);
        txt_booking_interior_colour = findViewById(R.id.txt_booking_interior_colour);
        txt_booking_exterior_colour = findViewById(R.id.txt_booking_exterior_colour);
        txt_overall_booking_date   = findViewById(R.id.txt_overall_booking_date);
        txt_booking_booking_amount = findViewById(R.id.txt_booking_booking_amount);
        txt_booking_payment_mode  = findViewById(R.id.txt_booking_payment_mode);
        lin_booking_complete = findViewById(R.id.lin_booking_complete);
        txt_one_k_follow_up_comments = findViewById(R.id.txt_one_k_follow_up_comments);
        lin_one_k_follow_up = findViewById(R.id.lin_one_k_follow_up);
        delivery_complete_date = findViewById(R.id.delivery_complete_date);
        delivery_complete_time = findViewById(R.id.delivery_complete_time);
        txt_delivery_complete_comments = findViewById(R.id.txt_delivery_complete_comments);
        delivery_complete_referral_name = findViewById(R.id.delivery_complete_referral_name);
        delivery_complete_referral_contact_no = findViewById(R.id.delivery_complete_referral_contact_no);
        lin_delivery_complete  = findViewById(R.id.lin_delivery_complete);
        txt_accessories_fitment_reason = findViewById(R.id.txt_accessories_fitment_reason);
        txt_accessories_fitment_status = findViewById(R.id.txt_accessories_fitment_status);
        extended_warranty = findViewById(R.id.extended_warranty);
        fastag = findViewById(R.id.fastag);
        rto_process = findViewById(R.id.rto_process);
        txt_accessories_fitment_reasons = findViewById(R.id.txt_accessories_fitment_reasons);
        allocation_pdi  = findViewById(R.id.allocation_pdi);
        accessories_fitment = findViewById(R.id.accessories_fitment);
        accessories_fitments  = findViewById(R.id.accessories_fitments);
        txt_invoice_complete_comments = findViewById(R.id.txt_invoice_complete_comments);
        delivery_invoice_time  = findViewById(R.id.delivery_invoice_time);
        invoice_complete_date = findViewById(R.id.invoice_complete_date);
        lin_booking_follow_up = findViewById(R.id.lin_booking_follow_up);
        txt_expectedinvoicedate = findViewById(R.id.txt_expectedinvoicedate);
        txt_expecteddownpaymentdate  = findViewById(R.id.txt_expecteddownpaymentdate);
        txt_expected_downpayment_amount = findViewById(R.id.txt_expected_downpayment_amount);
        txt_netamunt  = findViewById(R.id.txt_netamunt);
        txt_offerss   = findViewById(R.id.txt_offerss);
        txt_fasttag = findViewById(R.id.txt_fasttag);
        txt_warranty = findViewById(R.id.txt_warranty);
        txt_accessories = findViewById(R.id.txt_accessories);
        txt_registration = findViewById(R.id.txt_registration);
        txt_insurance   = findViewById(R.id.txt_insurance);
        txt_road_tax   = findViewById(R.id.txt_road_tax);
        txt_tcs_interest   = findViewById(R.id.txt_tcs_interest);
        txt_tcs_rate   = findViewById(R.id.txt_tcs_rate);
        txt_ex_showroom_price  = findViewById(R.id.txt_ex_showroom_price);
        txt_loan_amount   = findViewById(R.id.txt_loan_amount);
        txt_query_clearance = findViewById(R.id.txt_query_clearance);
        txt_login_date_time  = findViewById(R.id.txt_login_date_time);
        txt_login_completed  = findViewById(R.id.txt_login_completed);
        txt_paper_documents_collected  = findViewById(R.id.txt_paper_documents_collected);
        txt_dlphoto_details = findViewById(R.id.txt_dlphoto_details);
        txt_dlnumber_details = findViewById(R.id.txt_dlnumber_details);
        img_right  = findViewById(R.id.img_right);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat spf = new SimpleDateFormat("HH:mm");
        String strtime = spf.format(calendar.getTime());
        edt_time.setText(strtime);
        SimpleDateFormat dateformat = new  SimpleDateFormat("dd/MM/yyyy");
        String strDates = dateformat.format(calendar.getTime());
        edt_date.setText(strDates);

        if (customer_details_status.equals("Enquiry Followup")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }

            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + "\t" +follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }

          //  lin_reason_interested.setVisibility(View.VISIBLE);
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,UpdateEnquiryActivity.class)
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        }else if(customer_details_status.equals("New Contact")){
            rel_next_follow_up.setVisibility(View.VISIBLE);
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,ActivityUpdateContact.class).putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        } else if (customer_details_status.equals("Not Interested")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            lin_reason_interested.setVisibility(View.VISIBLE);
            REASON = getIntent().getStringExtra("REASON");
            edt_reason_interested.setText(REASON);
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,NotInterestedContactActivity.class)
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        } else if (customer_details_status.equals("Scheduled For Test Drive")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            txt_status_follow_up_text.setText("Test Drive Completed");
            Glide.with(CustomerDetailsActivity.this)
                    .load(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.DLPHOTO,"")).into(Img_dl_photo);
         //   swt_status_follow_up.setChecked(true);
            lin_test_derive_details.setVisibility(View.VISIBLE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }
            rel_status_follow_up_text.setVisibility(View.VISIBLE);
            txt_dl_number.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.DLNUMBER,""));
            txt_test_drive_date.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.TDSCHEDULEDON,""));
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,ScheduleForTheDriveActivity.class).putExtra("SCHEDULED_STATUS","EDIT NEW SCHEDULED")
                    .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        }else if (customer_details_status.equals("Test Drive Completed")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }
            lin_rating_details.setVisibility(View.VISIBLE);
            txt_dlphoto_details.setPaintFlags(txt_dlphoto_details.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            txt_dlphoto_details.setOnClickListener(view -> {
                imageDialog();
            });
            txt_dlnumber_details.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.DLNUMBER,""));
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,"")+"/5");
            txt_foverall_td_rate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.OVERALLTDEXPERIANCE,"")+"/5");
            txt_overall_condition_rate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.OVERALLCONDITIONOFVEHICLE,"")+"/5");
            txt_sales_consultant_rate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.SALES_CONSULTANTKNOWLEDGEOFTHEPRODUCT,"")+"/5");
            txt_overall_sales_consultant_rate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.OVERALLSALESCONSULTANTKNOWLEDGE_EXPERIENCE,"")+"/5");
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }

            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // startActivity(new Intent(CustomerDetailsActivity.this,TestDriveCompletedActivity.class));
                   // finish();
                }
            });
        }else if (customer_details_status.equals("Booking Completed")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            rel_time_follow_up.setVisibility(View.VISIBLE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE) {
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this, AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID", MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CONTACT_ID, "")));
                        finish();
                    }
                });
            }
            lin_booking_complete.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            txt_booking_model_suffix.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.BOOKINGMODEANDSUFFIX,""));
            txt_booking_interior_colour.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.INTERIORCOLOR,""));
            txt_booking_exterior_colour.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EXTERIORCOLOR,""));

            String BOOKINGDATE = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.BOOKINGDATE,"");
            SimpleDateFormat spfw = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date newDate = spfw.parse(BOOKINGDATE);
                spfw = new SimpleDateFormat("dd/MM/yyyy");
                String followupdate = spfw.format(newDate);
                txt_overall_booking_date.setText(followupdate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_booking_booking_amount.setText("\u20B9" + "\t" +MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.BOOKINGAMOUNT,""));
            txt_booking_payment_mode.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.PAYMENTMODE,""));
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            follow_up_time = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,"");
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,BookingCompletedActivity.class).putExtra("SCHEDULED_STATUS","EDIT NEW BOOKING")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        }else if (customer_details_status.equals("Invoice Completed")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.GONE);
            rel_pre_delivery_checklist.setVisibility(View.VISIBLE);
            invoice_complete_date.setVisibility(View.VISIBLE);
            invoice_complete_date.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.INVOICE_COMPLETED_DATE,""));
            delivery_invoice_time.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.INVOICE_COMPLETED_TIME,""));
            txt_invoice_complete_comments.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.INVOICE_COMPLETED_COMMENTS,""));
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,InvoiceCompletedActivity.class)
                            .putExtra("SCHEDULED_STATUS","EDIT NEW BOOKING")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        }else if (customer_details_status.equals("Pre Delivery")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.GONE);
            lin_predelivery_option.setVisibility(View.GONE);
            lin_accessories_fitment_witing.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            String ex_warranty =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EXTENDED_WARRANTY,"");

            if (ex_warranty.equals("1")){
                extended_warranty.setText("Yes");
                extended_warranty.setTextColor(Color.parseColor("#2ABB00"));
            }else if (ex_warranty.equals("0")){
                extended_warranty.setText("No");
                extended_warranty.setTextColor(Color.parseColor("#EB0A1E"));
            }

            String fastags = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FASTAG,"");
            if (fastags.equals("0")){
                fastag.setText("NO");
                fastag.setTextColor(Color.parseColor("#EB0A1E"));
            }else if (fastags.equals("1")){
                fastag.setText("Completed");
                fastag.setTextColor(Color.parseColor("#2ABB00"));
            }

            String deliverystatus =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.ACCESSORIES_FITMENT_STATUS,"");
            if (deliverystatus.equals("1")){
                accessories_fitment.setVisibility(View.VISIBLE);
                txt_accessories_fitment_reason.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.AF_WAITING_REASON,""));
                txt_accessories_fitment_status.setText("Waiting");
                txt_accessories_fitment_status.setTextColor(Color.parseColor("#EB0A1E"));
            }else if (deliverystatus.equals("2")){
                txt_accessories_fitment_status.setText("On Going");
                txt_accessories_fitment_status.setTextColor(Color.parseColor("#3587E6"));
            }else if (deliverystatus.equals("3")){
                txt_accessories_fitment_status.setText("Completed");
                txt_accessories_fitment_status.setTextColor(Color.parseColor("#2ABB00"));
            }

            String rto =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.RTO_PROCESS,"");
            if (rto.equals("1")){
                accessories_fitments.setVisibility(View.VISIBLE);
                txt_accessories_fitment_reasons.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.RTO_WAITING_REASON,""));
                rto_process.setText("Waiting");
                rto_process.setTextColor(Color.parseColor("#EB0A1E"));
            }else if (rto.equals("2")){
                rto_process.setText("Preferred" + "\t"+ MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.PREFERRED_RTO_DATE,""));
                rto_process.setTextColor(Color.parseColor("#3587E6"));
            }

            String allocationpdi =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.ALLOCATION_PDI,"");
            if (allocationpdi.equals("1")){
                txt_accessories_fitment_reasons.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.RTO_WAITING_REASON,""));
                allocation_pdi.setText("Completed");
                allocation_pdi.setTextColor(Color.parseColor("#2ABB00"));
            }else if (allocationpdi.equals("2")){
                allocation_pdi.setText("Under Process");
                allocation_pdi.setTextColor(Color.parseColor("#EB0A1E"));
            }

            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,PreDeliveryActivity.class)
                            .putExtra("SCHEDULED_STATUS","EDIT NEW SCHEDULED")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });

        }else if (customer_details_status.equals("Delivery Completed")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            rel_time_follow_up.setVisibility(View.VISIBLE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE) {
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this, AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID", MyFunctions.getSharedPrefs(CustomerDetailsActivity.this, Constants.CONTACT_ID, "")));
                        finish();
                    }
                });
            }
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }
            lin_delivery_complete.setVisibility(View.VISIBLE);
            delivery_complete_date.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.DATE,""));
            delivery_complete_time.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.TIME,""));
            txt_delivery_complete_comments.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.COMMENTS,""));
            delivery_complete_referral_name.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.REFNAME,""));
            delivery_complete_referral_contact_no.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.REFPHONE,""));
            txt_customer_details_title.setText("Delivery");
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,DeliveryActivity.class).putExtra("SCHEDULED_STATUS","EDIT NEW SCHEDULED")
                    .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
        }else if (customer_details_status.equals("PSF (Post Sales Followup)")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.GONE);
            txt_customer_details_title.setText("Post Sales Followup");
            lin_one_k_follow_up.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            txt_one_k_follow_up_comments.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.COMMENT,""));
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(CustomerDetailsActivity.this,PostSalesFollowupActivity.class));
                   // finish();
                }
            });
        }else if (customer_details_status.equals("15Th Day Followup")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }
            txt_customer_details_title.setText("15th Day Followup");
            lin_one_k_follow_up.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            txt_one_k_follow_up_comments.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.COMMENT,""));
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // startActivity(new Intent(CustomerDetailsActivity.this,FifteenthDayFollowupActivity.class));
                   // finish();
                }
            });
        }else if (customer_details_status.equals("1K Followup")){
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.GONE);
            txt_customer_details_title.setText("1K Followup");
            lin_one_k_follow_up.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            txt_one_k_follow_up_comments.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.COMMENT,""));
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  startActivity(new Intent(CustomerDetailsActivity.this,OnekActivity.class));
                  //  finish();
                }
            });
        }else if (customer_details_status.equals("Booking Followup")) {
            txt_status.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            rel_next_follow_up.setVisibility(View.GONE);
            lin_booking_follow_up.setVisibility(View.VISIBLE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            rel_location.setVisibility(View.GONE);
            rel_model.setVisibility(View.VISIBLE);
            txt_cus_car_model.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,""));
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
            try {
                Date newDate = spfe.parse(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""));
                spfe = new SimpleDateFormat("hh:mm a");
                follow_up_time = spfe.format(newDate);

            } catch (Exception e) {
                e.printStackTrace();
            }
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CustomerDetailsActivity.this,BookingFollowUpActivity.class)
                            .putExtra("SCHEDULED_STATUS","EDIT NEW SCHEDULED")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }
            });
            txt_expectedinvoicedate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EXPECTED_INVOICE_DATE,""));
            txt_expecteddownpaymentdate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EXPECTED_DOWNPAYMENT_DATE,""));
            txt_expected_downpayment_amount.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EXPECTED_DOWNPAYMENT_AMOUNT,""));
            txt_netamunt.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.NET_AMOUNT,""));
            txt_offerss.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.OFFERS,""));
            txt_fasttag.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FASTAGG,""));
            txt_warranty.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.WARRANTY,""));
            txt_accessories.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.ACCESSORIES,""));
            txt_registration.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.REGISTRATION,""));
            txt_insurance.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.INSURANCE,""));
            txt_road_tax.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.ROAD_TAX,""));
            txt_tcs_interest.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.TCS,""));
            txt_tcs_rate.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.TCS_AMOUNT,""));
            txt_ex_showroom_price.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.EX_SHOWROOM_PRICE,""));
            txt_loan_amount.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.LOAN_AMOUNT,""));
            txt_query_clearance.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.QUERY_CLEARANCE,""));
            txt_login_date_time.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.LOGIN_COMPLETED_DATE,""));
            txt_login_completed.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.LOGIN_COMPLETED,""));
            txt_paper_documents_collected.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.PAPER_DOCUMENTS_COLLECTED,""));
        }else if (customer_details_status.equals("Drop")) {
            txt_status.setVisibility(View.VISIBLE);
            lin_drop_option.setVisibility(View.VISIBLE);
            lin_intersted_id.setVisibility(View.GONE);
            img_right.setVisibility(View.GONE);
            drop_reason.setText(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.COMMENT,""));
            rel_next_follow_up.setVisibility(View.GONE);
            rel_staus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    startActivity(new Intent(CustomerDetailsActivity.this,DroppedActivity.class));
                }
            });
        }else if (customer_details_status.equals("BACK")){
            txt_satus_text.setText("New Contact");
        }else if (customer_details_status.equals("Contact Followup")){
            rel_time_follow_up.setVisibility(View.GONE);
            lin_follow_up.setVisibility(View.GONE);
            txt_status.setVisibility(View.VISIBLE);
            img_right.setVisibility(View.GONE);
            rel_time_follow_up.setVisibility(View.VISIBLE);
            if (rel_time_follow_up.getVisibility()==View.VISIBLE){
                rel_time_follow_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(CustomerDetailsActivity.this,AddFollowUpTaskActivity.class)
                                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                        finish();
                    }
                });
            }
            follow_up_date = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,"");
            follow_up_time = MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,"");
            txt_date_time.setText(follow_up_date + "\t" + "-" + follow_up_time);
            follow_up_id =  MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,"");
            if (follow_up_id.equals("1")){
                txt_followup_type.setText("TELEPHONE");
            }else if (follow_up_id.equals("2")){
                txt_followup_type.setText("DIRECT VISIT");
            }
        }

        txt_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img_whatapp.setEnabled(false);
                Img_message.setEnabled(false);
                Img_phone.setEnabled(false);
                Customer_Getcontactallstatus();

            }
        });

        swt_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    lin_follow_up.setVisibility(View.VISIBLE);
                    txt_add_task.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.GONE);
                    txt_add_task.setVisibility(View.GONE);
                }
            }
        });



      /*  swt_pre_delivery_checklist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startActivity(new Intent(CustomerDetailsActivity.this,PreDeliveryActivity.class));
                    finish();
                } else {
                    // The toggle is disabled
                    lin_follow_up.setVisibility(View.GONE);
                }
            }
        });*/

        swt_status_follow_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                   startActivity(new Intent(CustomerDetailsActivity.this,TestDriveCompletedActivity.class).putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.TESTDRIVEID,"")));
                    finish();
                } else {
                    // The toggle is disabled
                  //  lin_follow_up.setVisibility(View.GONE);
                }
            }
        });

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                try{
                    c.set(Calendar.DATE,Integer.parseInt(String.valueOf(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,""))));
                } catch(NumberFormatException ex){ // handle your exception

                }
                c.set(Calendar.HOUR_OF_DAY,mHour);
                c.set(Calendar.MINUTE,mMinute);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CustomerDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay >= mHour) {
                                    edt_time.setText(hourOfDay + ":" + minute);
                                }else {
                                    edt_time.setText("Invalid Time");
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        edt_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (edt_date.getRight() - edt_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        try{
                            c.set(Calendar.DATE,Integer.parseInt(String.valueOf(MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,""))));
                        } catch(NumberFormatException ex){ // handle your exception

                        }
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                        edt_date.setText(strDate);
                                        //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                        // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

        txt_telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                txt_telephone.setBackgroundResource(R.drawable.btn_line);
                txt_telephone.setTextColor(Color.parseColor("#EB0A1E"));
                FOLLOW_UP_TYPE = "1";
                if (isSecondPlace) {
                    txtdDirectvisit.setBackgroundResource(R.drawable.btn_line_grey);
                    txtdDirectvisit.setTextColor(Color.parseColor("#A7A7A7"));
                    isSecondPlace = false;
                }
            }
        });

        txtdDirectvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                txtdDirectvisit.setBackgroundResource(R.drawable.btn_line);
                txtdDirectvisit.setTextColor(Color.parseColor("#EB0A1E"));
                FOLLOW_UP_TYPE = "2";
                isSecondPlace = true;
                if (isOnePressed) {
                    txt_telephone.setBackground(getResources().getDrawable(R.drawable.btn_line_grey));
                    txt_telephone.setTextColor(Color.parseColor("#A7A7A7"));
                    isOnePressed = false;
                }
            }
        });

        txt_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(CustomerDetailsActivity.this,HistoryActivity.class)
                        .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                finish();
            }
        });


            txt_complete_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (customer_details_status.equals("New Contact")|| customer_details_status.equals("Contact Followup")){
                       // userMessage = (String) jsonObj.get("userMessage");
            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
            builder.setTitle("USER MESSAGE");
            builder.setMessage("New Contact Don't Have Complete Details");
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
                }else {
                    startActivity(new Intent(CustomerDetailsActivity.this,CompleteDetailsActivity.class)
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
                    finish();
                }

            }
        });



        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(HomeActivity.this,AddMeetingActivity.class));
                layout_animate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
                lin_status_list.startAnimation(layout_animate);
                lin_status_list.setVisibility(View.GONE);
                if (lin_status_list.getVisibility()==View.GONE){
                    Img_whatapp.setEnabled(true);
                    Img_message.setEnabled(true);
                    Img_phone.setEnabled(true);
                }

            }
        });

        txt_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOnePressed && !isSecondPlace){

                }else {
                     String followupdate = edt_date.getText().toString().trim();
                if (followupdate.equals("") || followupdate.isEmpty()) {
                    edt_date.setError("Please Selecte Date");
                } else {
                    SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                    try {

                        Date newDate = spf.parse(followupdate);
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        FOLLOW_UP_DATE = spf.format(newDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String scheduloedtime = edt_time.getText().toString().trim()+":"+"00";
                if (scheduloedtime.equals("") || scheduloedtime.isEmpty()) {
                    edt_time.setError("Please Selecte Time");
                } else {
                    SimpleDateFormat spf = new SimpleDateFormat("HH:mm:ss");
                    try {

                        Date newDate = spf.parse(scheduloedtime);
                        spf = new SimpleDateFormat("HH:mm");
                        FOLLOW_UP_TIME = spf.format(newDate);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                FOLLOW_UP_REMARK = edt_remark.getText().toString().trim();

                if (FOLLOW_UP_DATE.equals("")||FOLLOW_UP_TIME.equals("")){
                    Toast.makeText(CustomerDetailsActivity.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
                }else {
                    Customer_Create_Contact_Followup();
                    //initialize the progress dialog and show it
                    progressDialog = new ProgressDialog(CustomerDetailsActivity.this);
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
        });
    }

    private void imageDialog() {
        alertDialog2 = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View add_menu_layout = inflater.inflate(R.layout.showdlimage, null);
        ImageView layout_gallery = add_menu_layout.findViewById(R.id.uploadimage);
        alertDialog2.setView(add_menu_layout);
        alertDialog2.setCancelable(true);
        final android.app.AlertDialog testDialog = alertDialog2.create();
//        final ImageView tv_camera = add_menu_layout.findViewById(R.id.uploadimage);
        try {
//            if (getDlimage != null){
//                Glide.with(this).load(getDlimage).error(R.drawable.ic_add_image).into(layout_gallery);
//            }else
           if (getIntentDlimage != null){
                Glide.with(this).load(getIntentDlimage).error(R.drawable.ic_add_image).into(layout_gallery);
            }else if (getDlimage != null){
                Glide.with(this).load(getDlimage).error(R.drawable.ic_add_image).into(layout_gallery);
            }
            else {
                Toast.makeText(this, "Please Update Image", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception exception){

        }



        testDialog.show();
    }

    public void back(View view) {
        startActivity(new Intent(CustomerDetailsActivity.this,HomeActivity.class));
         finish();
    }

    public void whatapp(View view) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(Intent.createChooser(sendIntent, ""));
            startActivity(sendIntent);
        } catch (Exception e) {
            Toast.makeText(CustomerDetailsActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void message(View view) {
        // TODO Auto-generated method stub
        Intent eventIntentMessage =getPackageManager()
                .getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(getApplicationContext()));
        startActivity(eventIntentMessage);
    }

    public void phone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.setData(Uri.parse("tel:"+PHONE));
        startActivity(intent);
    }

    public void notinteresed(View view) {
        if (customer_details_status.equals("New Contact")){
            startActivity(new Intent(CustomerDetailsActivity.this,NotInterestedContactActivity.class)
                    .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
            finish();
        }else {
            // userMessage = (String) jsonObj.get("userMessage");
            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
            builder.setTitle("USER MESSAGE");
            builder.setMessage("Only New Contact Could be Not Interested");
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

    }

    public void interesed(View view) {
        startActivity(new Intent(CustomerDetailsActivity.this,InterestedContactActivity.class)
                .putExtra("CONTACTID",MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,"")));
         finish();
    }



    public void Customer_Getcontactallstatus() {
        StringRequest requests = new StringRequest(Request.Method.POST, customer_getcontactallstatus_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        contactStatusLists.clear();
                        JSONArray jsonArray_titles = jsonObj.getJSONArray("contact_status");
                        if (status_code.equals("1")) {
                            for (int i = 0; i < jsonArray_titles.length(); i++) {
                                ContactStatusList contactStatusList = new ContactStatusList();
                                JSONObject jsonObject = jsonArray_titles.getJSONObject(i);
                                contactStatusList.setStatus(jsonObject.getString("status"));
                                contactStatusList.setStatus_id(jsonObject.getString("status_id"));
                                contactStatusLists.add(contactStatusList);
                            }

                            ContactStatusAdapter adapter = new ContactStatusAdapter(CustomerDetailsActivity.this, contactStatusLists);
                            rv_contact_status.setAdapter(adapter);
                            lin_status_list.setVisibility(View.VISIBLE);
                            layout_animate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                            lin_status_list.startAnimation(layout_animate);
                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
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


    //Api update contact cop;
    public void Customer_Create_Contact_Followup(){
        StringRequest requests = new StringRequest(Request.Method.POST, customer_createcontactfollowup_url, new Response.Listener<String>() {
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
                            contact_id = jsonObject.getString("contact_id");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,contact_id);
                            contact_status = jsonObject.getString("status");
                            String cus_name = (String)jsonObject.get("name");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_NAME,cus_name);
                            String cus_phone = (String)jsonObject.get("phone").toString();
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_PHONE,cus_phone);
                            String cus_address = (String)jsonObject.get("address");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.CUSTOMER_ADDRESS,cus_address);
                            String followupdate = jsonObject.getString("follow_up_date");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPDATE,followupdate);
                            String followuptime = jsonObject.getString("follow_up_time");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTIME,followuptime);
                            String followuptype = jsonObject.getString("follow_up_type");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPTYPE,followuptype);
                            String followupid = jsonObject.getString("follow_up_id");
                            MyFunctions.setSharedPrefs(CustomerDetailsActivity.this,Constants.FOLLOWUPID,followupid);
                            startActivity(new Intent(CustomerDetailsActivity.this,CustomerDetailsActivity.class).putExtra("Status",contact_status));
                            finish();
                          //  startActivity(new Intent(CustomerDetailsActivity.this, CustomerDetailsActivity.class).putExtra("Status", contact_status));
                          //  finish();
                        } else if (status_code.equals("0")) {
                            //exteriorColorsLists.clear();
                            //exadapter.notifyDataSetChanged();
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    //exteriorColorsLists.clear();
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
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
                params.put("CONTACT_ID", MyFunctions.getSharedPrefs(CustomerDetailsActivity.this,Constants.CONTACT_ID,""));
                params.put("FOLLOW_UP_DATE", FOLLOW_UP_DATE);
                params.put("FOLLOW_UP_TIME", FOLLOW_UP_TIME);
                params.put("FOLLOW_UP_TYPE", FOLLOW_UP_TYPE);
                params.put("FOLLOW_UP_REMARK", FOLLOW_UP_REMARK);
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
        startActivity(new Intent(CustomerDetailsActivity.this, SingleUserActivity.class));
        finish();
    }
}
