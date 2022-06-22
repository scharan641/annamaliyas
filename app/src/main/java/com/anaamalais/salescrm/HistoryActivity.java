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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.Adapter.BookingCompletedAdapter;
import com.anaamalais.salescrm.Adapter.BookingFollowupAdapter;
import com.anaamalais.salescrm.Adapter.ContactFollowupAdapter;
import com.anaamalais.salescrm.Adapter.ContactHistoryAdapter;
import com.anaamalais.salescrm.Adapter.DeliveryCompletedAdapter;
import com.anaamalais.salescrm.Adapter.DropAdapter;
import com.anaamalais.salescrm.Adapter.EnquiryFollowupAdapter;
import com.anaamalais.salescrm.Adapter.FifteenDayFollowupAdapter;
import com.anaamalais.salescrm.Adapter.InvoiceCompletedAdapter;
import com.anaamalais.salescrm.Adapter.NotInterestedAdapter;
import com.anaamalais.salescrm.Adapter.OneKFollowupAdapter;
import com.anaamalais.salescrm.Adapter.PostSalesFollowupAdapter;
import com.anaamalais.salescrm.Adapter.PreDeliveryAdapter;
import com.anaamalais.salescrm.Adapter.ScheduleddrivefollowupAdapter;
import com.anaamalais.salescrm.Adapter.StatusBasedFiltersAdapter;
import com.anaamalais.salescrm.Adapter.TestDriveCompleteAdapter;
import com.anaamalais.salescrm.List.BookingCompletedList;
import com.anaamalais.salescrm.List.BookingFollowupList;
import com.anaamalais.salescrm.List.ContactHistoryList;
import com.anaamalais.salescrm.List.DeliveryCompletedList;
import com.anaamalais.salescrm.List.DropList;
import com.anaamalais.salescrm.List.FifteenDayFollowupList;
import com.anaamalais.salescrm.List.Followup_details_List;
import com.anaamalais.salescrm.List.InvoiceCompletedList;
import com.anaamalais.salescrm.List.NotInterestedList;
import com.anaamalais.salescrm.List.OneKFollowupList;
import com.anaamalais.salescrm.List.PostSalesFollowupList;
import com.anaamalais.salescrm.List.PreDeliveryList;
import com.anaamalais.salescrm.List.ScheduleddrivefollowupList;
import com.anaamalais.salescrm.List.TestDriveCompletedList;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HistoryActivity extends AppCompatActivity {
    TextView txt_enquired_on , txt_created_title , txt_contact_followup_change_status , txt_enquiry_followup_change_status
    ,txt_newcontact_followup_change_status , txt_not_interested_followup_change_status , txt_scheduled_drive_followup_change_status
    ,txt_test_drive_complete_followup_change_status , txt_booking_completed_followup_change_status
   ,txt_booking_follow_up_followup_change_status , txt_pre_delivery_followup_change_status
    ,txt_invoice_completed_followup_change_status , txt_delivery_completed_change_status,txt_psf_change_status
    ,txt_fifteenth_day_change_status, txt_oneK_change_status , txt_drop_change_status;

    TextView  txt_contact_empty_message , txt_enquiry_empty_message , txt_newcontact_empty_message
    ,txt_not_interested_empty_message,txt_scheduled_drive_empty_message,txt_test_drive_complete_empty_message
    ,txt_booking_completed_empty_message , txt_booking_follow_up_empty_message,txt_pre_delivery_empty_message
    ,txt_invoice_completed_empty_message , txt_delivery_completed_empty_message,txt_psf_empty_message
    ,txt_fifteenth_day_empty_message , txt_oneK_empty_message , txt_drop_empty_message;

    ImageView  img_enquiry_followup_downs , img_enquiry_followup_ups , img_contact_followup_down , img_contact_followup_up , img_scheduled_for_test_drive_down , img_scheduled_for_test_drive_up ,
            img_test_drive_completed_down , img_test_drive_completed_up , img_booking_completed_down , img_booking_completed_up , img_booking_follow_up_down , img_booking_followup_up , img_psf_down ,
            img_psf_up , img_fifteenth_day_follow_up_down , img_fifteenth_day_follow_up_up , img_oneK_follow_up_down ,
            img_oneK_follow_up_up , img_drop_down , img_drop_up , img_contact_followup_downs ,
            img_contact_followup_ups ,  img_not_interested_down , img_not_interested_up,img_pre_delivery_down ,  img_pre_delivery_up
            ,img_invoice_completed_follow_up_down , img_invoice_completed_followup_up
            ,img_delivery_completed_follow_up_down ,  img_delivery_completed_followup_up;

    RelativeLayout rel_contact_followup , rel_enquiry_followup , rel_contacts_followup , rel_not_interested,
            rel_scheduled_for_test_drive , rel_test_drive_completed , rel_pre_delivery_follow_up,
            rel_booking_completed , rel_booking_follow_up  , rel_invoice_completed_follow_up , rel_delivery_completed_follow_up , rel_psf_followup,
            rel_fifteenth_day_follow_up ,rel_oneK_follow_up , rel_drop   ;

    LinearLayout lin_individual , lin_contact_follow_up , lin_enquiry_follow_up , lin_newcontact_follow_up
            ,lin_not_interested_follow_up,lin_scheduled_drive_follow_up,lin_test_drive_complete_follow_up,
            lin_booking_completed_follow_up,lin_booking_follow_up_follow_up,lin_pre_delivery_follow_up
            ,lin_invoice_completed_follow_up , lin_delivery_completed_follow_up,lin_psf_follow_up
            ,lin_fifteenth_day_follow_up,lin_oneK_follow_up,lin_drop_follow_up;

    RecyclerView rv_contact_follow_up , rv_enquiry_follow_up , rv_newcontact_follow_up , rv_not_interested_follow_up,
            rv_scheduled_drive_follow_up,rv_test_drive_complete_follow_up,rv_booking_completed_follow_up , rv_pre_delivery_follow_up
            ,rv_booking_follow_up_follow_up , rv_invoice_completed_follow_up,rv_delivery_completed_follow_up,
            rv_psf_follow_up , rv_fifteenth_day_follow_up,rv_oneK_follow_up,rv_drop_follow_up;

    Animation animation;
    String followupdate , followuptime;
    String  status_code , msg ,token, UPDATE_ENQUIRY_ID, API_TOKEN , finance_other , insurance_other,follow_up_type;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String enquiry_gethistory_url = BASE_URL + "enquiry/gethistory";
    List<Followup_details_List>followup_details_lists;
    List<TestDriveCompletedList>testDriveCompletedLists;
    List<BookingCompletedList>bookingCompletedLists;
    List<BookingFollowupList>bookingFollowupLists;
    List<PreDeliveryList>preDeliveryLists;
    List<InvoiceCompletedList>invoiceCompletedLists;
    List<DeliveryCompletedList>deliveryCompletedLists;
    List<PostSalesFollowupList>postSalesFollowupLists;
    List<FifteenDayFollowupList>fifteenDayFollowupLists;
    List<OneKFollowupList>oneKFollowupLists;
    List<DropList>dropLists;
    List<ScheduleddrivefollowupList>scheduleddrivefollowupLists;
    List<NotInterestedList>notInterestedLists;
    List<ContactHistoryList>contactHistoryLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Window window = HistoryActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(HistoryActivity.this, R.color.white));
        requestQueue = Volley.newRequestQueue(HistoryActivity.this);
        API_TOKEN = MyFunctions.getSharedPrefs(HistoryActivity.this, Constants.TOKEN,"");
        img_enquiry_followup_downs = findViewById(R.id.img_enquiry_followup_downs);
        img_enquiry_followup_ups = findViewById(R.id.img_enquiry_followup_ups);
        img_contact_followup_down = findViewById(R.id.img_contact_followup_down);
        img_contact_followup_up = findViewById(R.id.img_contact_followup_up);
        img_scheduled_for_test_drive_down = findViewById(R.id.img_scheduled_for_test_drive_down);
        img_scheduled_for_test_drive_up = findViewById(R.id.img_scheduled_for_test_drive_up);
        img_test_drive_completed_down = findViewById(R.id.img_test_drive_completed_down);
        img_test_drive_completed_up = findViewById(R.id.img_test_drive_completed_up);
        img_booking_completed_down = findViewById(R.id.img_booking_completed_down);
        img_booking_completed_up = findViewById(R.id.img_booking_completed_up);
        img_booking_follow_up_down = findViewById(R.id.img_booking_follow_up_down);
        img_booking_followup_up = findViewById(R.id.img_booking_followup_up);
        img_psf_down = findViewById(R.id.img_psf_down);
        img_psf_up = findViewById(R.id.img_psf_up);
        img_fifteenth_day_follow_up_down = findViewById(R.id.img_fifteenth_day_follow_up_down);
        img_fifteenth_day_follow_up_up = findViewById(R.id.img_fifteenth_day_follow_up_up);
        img_oneK_follow_up_down = findViewById(R.id.img_oneK_follow_up_down);
        img_oneK_follow_up_up = findViewById(R.id.img_oneK_follow_up_up);
        img_drop_up = findViewById(R.id.img_drop_up);
        img_drop_down = findViewById(R.id.img_drop_down);
        img_contact_followup_downs = findViewById(R.id.img_contact_followup_downs);
        img_contact_followup_ups = findViewById(R.id.img_contact_followup_ups);
        img_not_interested_down = findViewById(R.id.img_not_interested_down);
        img_not_interested_up = findViewById(R.id.img_not_interested_up);
        img_pre_delivery_down = findViewById(R.id.img_pre_delivery_down);
        img_pre_delivery_up = findViewById(R.id.img_pre_delivery_up);
        img_invoice_completed_follow_up_down = findViewById(R.id.img_invoice_completed_follow_up_down);
        img_invoice_completed_followup_up = findViewById(R.id.img_invoice_completed_followup_up);
        img_delivery_completed_follow_up_down = findViewById(R.id.img_delivery_completed_follow_up_down);
        img_delivery_completed_followup_up = findViewById(R.id.img_delivery_completed_followup_up);
        rel_contact_followup = findViewById(R.id.rel_contact_followup);
        rel_enquiry_followup = findViewById(R.id.rel_enquiry_followup);
        rel_contacts_followup = findViewById(R.id.rel_contacts_followup);
        rel_not_interested = findViewById(R.id.rel_not_interested);
        rel_scheduled_for_test_drive = findViewById(R.id.rel_scheduled_for_test_drive);
        rel_test_drive_completed = findViewById(R.id.rel_test_drive_completed);
        rel_booking_completed = findViewById(R.id.rel_booking_completed);
        rel_pre_delivery_follow_up = findViewById(R.id.rel_pre_delivery_follow_up);
        rel_invoice_completed_follow_up = findViewById(R.id.rel_invoice_completed_follow_up);
        rel_delivery_completed_follow_up = findViewById(R.id.rel_delivery_completed_follow_up);
        rel_psf_followup  = findViewById(R.id.rel_psf_followup);
        rel_booking_follow_up = findViewById(R.id.rel_booking_follow_up);
        rel_fifteenth_day_follow_up = findViewById(R.id.rel_fifteenth_day_follow_up);
        rel_oneK_follow_up = findViewById(R.id.rel_oneK_follow_up);
        rel_drop = findViewById(R.id.rel_drop);
        txt_contact_followup_change_status = findViewById(R.id.txt_contact_followup_change_status);
        //linerlayout hide & show view finder
        lin_newcontact_follow_up = findViewById(R.id.lin_newcontact_follow_up);
        lin_enquiry_follow_up = findViewById(R.id.lin_enquiry_follow_up);
        lin_contact_follow_up = findViewById(R.id.lin_contact_follow_up);
        lin_not_interested_follow_up = findViewById(R.id.lin_not_interested_follow_up);
        lin_scheduled_drive_follow_up = findViewById(R.id.lin_scheduled_drive_follow_up);
        lin_test_drive_complete_follow_up = findViewById(R.id.lin_test_drive_complete_follow_up);
        lin_booking_completed_follow_up = findViewById(R.id.lin_booking_completed_follow_up);
        lin_booking_follow_up_follow_up = findViewById(R.id.lin_booking_follow_up_follow_up);
        lin_pre_delivery_follow_up = findViewById(R.id.lin_pre_delivery_follow_up);
        lin_invoice_completed_follow_up = findViewById(R.id.lin_invoice_completed_follow_up);
        lin_delivery_completed_follow_up = findViewById(R.id.lin_delivery_completed_follow_up);
        lin_psf_follow_up = findViewById(R.id.lin_psf_follow_up);
        lin_fifteenth_day_follow_up = findViewById(R.id.lin_fifteenth_day_follow_up);
        lin_oneK_follow_up = findViewById(R.id.lin_oneK_follow_up);
        lin_drop_follow_up = findViewById(R.id.lin_drop_follow_up);

        txt_created_title = findViewById(R.id.txt_created_title);
        txt_enquired_on  = findViewById(R.id.txt_enquired_on);
        //txtview no view & status change view finder
        txt_contact_empty_message  = findViewById(R.id.txt_contact_empty_message);
        txt_enquiry_empty_message = findViewById(R.id.txt_enquiry_empty_message);
        txt_enquiry_followup_change_status = findViewById(R.id.txt_enquiry_followup_change_status);
        txt_newcontact_followup_change_status = findViewById(R.id.txt_newcontact_followup_change_status);
        txt_newcontact_empty_message = findViewById(R.id.txt_newcontact_empty_message);
        txt_not_interested_followup_change_status = findViewById(R.id.txt_not_interested_followup_change_status);
        txt_not_interested_empty_message = findViewById(R.id.txt_not_interested_empty_message);
        txt_scheduled_drive_empty_message = findViewById(R.id.txt_scheduled_drive_empty_message);
        txt_scheduled_drive_followup_change_status = findViewById(R.id.txt_scheduled_drive_followup_change_status);
        txt_test_drive_complete_followup_change_status  = findViewById(R.id.txt_test_drive_complete_followup_change_status);
        txt_test_drive_complete_empty_message = findViewById(R.id.txt_test_drive_complete_empty_message);
        txt_booking_completed_followup_change_status = findViewById(R.id.txt_booking_completed_followup_change_status);
        txt_booking_completed_empty_message = findViewById(R.id.txt_booking_completed_empty_message);
        txt_booking_follow_up_followup_change_status = findViewById(R.id.txt_booking_follow_up_followup_change_status);
        txt_booking_follow_up_empty_message = findViewById(R.id.txt_booking_follow_up_empty_message);
        txt_pre_delivery_empty_message = findViewById(R.id.txt_pre_delivery_empty_message);
        txt_pre_delivery_followup_change_status = findViewById(R.id.txt_pre_delivery_followup_change_status);
        txt_invoice_completed_empty_message = findViewById(R.id.txt_invoice_completed_empty_message);
        txt_invoice_completed_followup_change_status = findViewById(R.id.txt_invoice_completed_followup_change_status);
        txt_delivery_completed_change_status = findViewById(R.id.txt_delivery_completed_change_status);
        txt_delivery_completed_empty_message = findViewById(R.id.txt_delivery_completed_empty_message);
        txt_psf_empty_message  = findViewById(R.id.txt_psf_empty_message);
        txt_psf_change_status = findViewById(R.id.txt_psf_change_status);
        txt_fifteenth_day_empty_message = findViewById(R.id.txt_fifteenth_day_empty_message);
        txt_fifteenth_day_change_status = findViewById(R.id.txt_fifteenth_day_change_status);
        txt_oneK_change_status = findViewById(R.id.txt_oneK_change_status);
        txt_oneK_empty_message = findViewById(R.id.txt_oneK_empty_message);
        txt_drop_change_status = findViewById(R.id.txt_drop_change_status);
        txt_drop_empty_message = findViewById(R.id.txt_drop_empty_message);
        //recycleview view finder
        rv_contact_follow_up = findViewById(R.id.rv_contact_follow_up);
        rv_contact_follow_up.setHasFixedSize(true);
        rv_contact_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_enquiry_follow_up = findViewById(R.id.rv_enquiry_follow_up);
        rv_enquiry_follow_up.setHasFixedSize(true);
        rv_enquiry_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_newcontact_follow_up = findViewById(R.id.rv_newcontact_follow_up);
        rv_newcontact_follow_up.setHasFixedSize(true);
        rv_newcontact_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_not_interested_follow_up = findViewById(R.id.rv_not_interested_follow_up);
        rv_not_interested_follow_up.setHasFixedSize(true);
        rv_not_interested_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_scheduled_drive_follow_up = findViewById(R.id.rv_scheduled_drive_follow_up);
        rv_scheduled_drive_follow_up.setHasFixedSize(true);
        rv_scheduled_drive_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_test_drive_complete_follow_up = findViewById(R.id.rv_test_drive_complete_follow_up);
        rv_test_drive_complete_follow_up.setHasFixedSize(true);
        rv_test_drive_complete_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_booking_completed_follow_up = findViewById(R.id.rv_booking_completed_follow_up);
        rv_booking_completed_follow_up.setHasFixedSize(true);
        rv_booking_completed_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_booking_follow_up_follow_up = findViewById(R.id.rv_booking_follow_up_follow_up);
        rv_booking_completed_follow_up.setHasFixedSize(true);
        rv_booking_completed_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_pre_delivery_follow_up = findViewById(R.id.rv_pre_delivery_follow_up);
        rv_pre_delivery_follow_up.setHasFixedSize(true);
        rv_pre_delivery_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_invoice_completed_follow_up = findViewById(R.id.rv_invoice_completed_follow_up);
        rv_invoice_completed_follow_up.setHasFixedSize(true);
        rv_invoice_completed_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_delivery_completed_follow_up  = findViewById(R.id.rv_delivery_completed_follow_up);
        rv_delivery_completed_follow_up.setHasFixedSize(true);
        rv_delivery_completed_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_psf_follow_up = findViewById(R.id.rv_psf_follow_up);
        rv_delivery_completed_follow_up.setHasFixedSize(true);
        rv_delivery_completed_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_fifteenth_day_follow_up = findViewById(R.id.rv_fifteenth_day_follow_up);
        rv_fifteenth_day_follow_up.setHasFixedSize(true);
        rv_fifteenth_day_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_oneK_follow_up = findViewById(R.id.rv_oneK_follow_up);
        rv_oneK_follow_up.setHasFixedSize(true);
        rv_oneK_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        rv_drop_follow_up = findViewById(R.id.rv_drop_follow_up);
        rv_drop_follow_up.setHasFixedSize(true);
        rv_drop_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        followup_details_lists = new ArrayList<>();
        testDriveCompletedLists = new ArrayList<>();
        bookingCompletedLists = new ArrayList<>();
        bookingFollowupLists = new ArrayList<>();
        preDeliveryLists = new ArrayList<>();
        invoiceCompletedLists = new ArrayList<>();
        deliveryCompletedLists = new ArrayList<>();
        postSalesFollowupLists = new ArrayList<>();
        fifteenDayFollowupLists = new ArrayList<>();
        oneKFollowupLists = new ArrayList<>();
        dropLists = new ArrayList<>();
        scheduleddrivefollowupLists  = new ArrayList<>();
        notInterestedLists = new ArrayList<>();
        contactHistoryLists  = new ArrayList<>();

        UPDATE_ENQUIRY_ID = getIntent().getStringExtra("CONTACTID");


        rel_contact_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_contact_followup_downs.getVisibility()==View.VISIBLE){
                    img_contact_followup_downs.setVisibility(View.GONE);
                    img_contact_followup_ups.setVisibility(View.VISIBLE);
                    rel_contact_followup.setBackgroundResource(R.color.intersted_color);
                    lin_newcontact_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_contact_followup.startAnimation(animation);

                }else if (lin_newcontact_follow_up.getVisibility()==View.VISIBLE){
                    img_contact_followup_downs.setVisibility(View.VISIBLE);
                    img_contact_followup_ups.setVisibility(View.GONE);
                    lin_newcontact_follow_up.setVisibility(View.GONE);
                    rel_contact_followup.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_contact_followup.startAnimation(animation);
                }
            }
        });

        rel_enquiry_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_enquiry_followup_downs.getVisibility()==View.VISIBLE){
                    img_enquiry_followup_downs.setVisibility(View.GONE);
                    img_enquiry_followup_ups.setVisibility(View.VISIBLE);
                    rel_enquiry_followup.setBackgroundResource(R.color.intersted_color);
                    lin_enquiry_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_enquiry_followup.startAnimation(animation);

                }else if (img_enquiry_followup_ups.getVisibility()==View.VISIBLE){
                    img_enquiry_followup_downs.setVisibility(View.VISIBLE);
                    img_enquiry_followup_ups.setVisibility(View.GONE);
                    lin_enquiry_follow_up.setVisibility(View.GONE);
                    rel_enquiry_followup.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_enquiry_followup.startAnimation(animation);
                }
            }
        });

        rel_contacts_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_contact_followup_down.getVisibility()==View.VISIBLE){
                    img_contact_followup_down.setVisibility(View.GONE);
                    img_contact_followup_up.setVisibility(View.VISIBLE);
                    rel_contacts_followup.setBackgroundResource(R.color.intersted_color);
                    lin_contact_follow_up.setVisibility(View.VISIBLE);
                    rv_contact_follow_up.setHasFixedSize(true);
                    rv_contact_follow_up.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_contacts_followup.startAnimation(animation);

                }else if (img_contact_followup_up.getVisibility()==View.VISIBLE){
                    img_contact_followup_down.setVisibility(View.VISIBLE);
                    img_contact_followup_up.setVisibility(View.GONE);
                    lin_contact_follow_up.setVisibility(View.GONE);
                    rel_contacts_followup.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_contacts_followup.startAnimation(animation);
                }
            }
        });

        rel_not_interested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_not_interested_down.getVisibility()==View.VISIBLE){
                    img_not_interested_down.setVisibility(View.GONE);
                    img_not_interested_up.setVisibility(View.VISIBLE);
                    rel_not_interested.setBackgroundResource(R.color.intersted_color);
                    lin_not_interested_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_not_interested.startAnimation(animation);
                }else if (img_not_interested_up.getVisibility()==View.VISIBLE){
                    img_not_interested_down.setVisibility(View.VISIBLE);
                    img_not_interested_up.setVisibility(View.GONE);
                    lin_not_interested_follow_up.setVisibility(View.GONE);
                    rel_not_interested.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_not_interested.startAnimation(animation);
                }
            }
        });

        rel_scheduled_for_test_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_scheduled_for_test_drive_down.getVisibility()==View.VISIBLE){
                    img_scheduled_for_test_drive_down.setVisibility(View.GONE);
                    img_scheduled_for_test_drive_up.setVisibility(View.VISIBLE);
                    rel_scheduled_for_test_drive.setBackgroundResource(R.color.intersted_color);
                    lin_scheduled_drive_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_scheduled_for_test_drive.startAnimation(animation);

                }else if (img_scheduled_for_test_drive_up.getVisibility()==View.VISIBLE){
                    img_scheduled_for_test_drive_down.setVisibility(View.VISIBLE);
                    img_scheduled_for_test_drive_up.setVisibility(View.GONE);
                    lin_scheduled_drive_follow_up.setVisibility(View.GONE);
                    rel_scheduled_for_test_drive.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_scheduled_for_test_drive.startAnimation(animation);
                }
            }
        });

        rel_test_drive_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_test_drive_completed_down.getVisibility()==View.VISIBLE){
                    img_test_drive_completed_down.setVisibility(View.GONE);
                    img_test_drive_completed_up.setVisibility(View.VISIBLE);
                    rel_test_drive_completed.setBackgroundResource(R.color.intersted_color);
                    lin_test_drive_complete_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_test_drive_completed.startAnimation(animation);

                }else if (img_test_drive_completed_up.getVisibility()==View.VISIBLE){
                    img_test_drive_completed_down.setVisibility(View.VISIBLE);
                    img_test_drive_completed_up.setVisibility(View.GONE);
                    lin_test_drive_complete_follow_up.setVisibility(View.GONE);
                    rel_test_drive_completed.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_test_drive_completed.startAnimation(animation);
                }
            }
        });

        rel_booking_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_booking_completed_down.getVisibility()==View.VISIBLE){
                    img_booking_completed_down.setVisibility(View.GONE);
                    img_booking_completed_up.setVisibility(View.VISIBLE);
                    rel_booking_completed.setBackgroundResource(R.color.intersted_color);
                    lin_booking_completed_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_booking_completed.startAnimation(animation);

                }else if (img_booking_completed_up.getVisibility()==View.VISIBLE){
                    img_booking_completed_down.setVisibility(View.VISIBLE);
                    img_booking_completed_up.setVisibility(View.GONE);
                    lin_booking_completed_follow_up.setVisibility(View.GONE);
                    rel_booking_completed.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_booking_completed.startAnimation(animation);
                }
            }
        });

        rel_booking_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_booking_follow_up_down.getVisibility()==View.VISIBLE){
                    img_booking_follow_up_down.setVisibility(View.GONE);
                    img_booking_followup_up.setVisibility(View.VISIBLE);
                    rel_booking_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_booking_follow_up_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_booking_follow_up.startAnimation(animation);

                }else if (img_booking_followup_up.getVisibility()==View.VISIBLE){
                    img_booking_follow_up_down.setVisibility(View.VISIBLE);
                    img_booking_followup_up.setVisibility(View.GONE);
                    lin_booking_follow_up_follow_up.setVisibility(View.GONE);
                    rel_booking_follow_up.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_booking_follow_up.startAnimation(animation);
                }
            }
        });

        rel_pre_delivery_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_pre_delivery_down.getVisibility()==View.VISIBLE){
                    img_pre_delivery_down.setVisibility(View.GONE);
                    img_pre_delivery_up.setVisibility(View.VISIBLE);
                    rel_pre_delivery_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_pre_delivery_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_pre_delivery_follow_up.startAnimation(animation);

                }else if (img_pre_delivery_up.getVisibility()==View.VISIBLE){
                    img_pre_delivery_down.setVisibility(View.VISIBLE);
                    img_pre_delivery_up.setVisibility(View.GONE);
                    rel_pre_delivery_follow_up.setBackgroundResource(0);
                    lin_pre_delivery_follow_up.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_pre_delivery_follow_up.startAnimation(animation);
                }
            }
        });

        rel_invoice_completed_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_invoice_completed_follow_up_down.getVisibility()==View.VISIBLE){
                    img_invoice_completed_follow_up_down.setVisibility(View.GONE);
                    img_invoice_completed_followup_up.setVisibility(View.VISIBLE);
                    rel_invoice_completed_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_invoice_completed_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_invoice_completed_follow_up.startAnimation(animation);

                }else if (img_invoice_completed_followup_up.getVisibility()==View.VISIBLE){
                    img_invoice_completed_follow_up_down.setVisibility(View.VISIBLE);
                    img_invoice_completed_followup_up.setVisibility(View.GONE);
                    rel_invoice_completed_follow_up.setBackgroundResource(0);
                    lin_invoice_completed_follow_up.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_invoice_completed_follow_up.startAnimation(animation);
                }
            }
        });

        rel_delivery_completed_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_delivery_completed_follow_up_down.getVisibility()==View.VISIBLE){
                    img_delivery_completed_follow_up_down.setVisibility(View.GONE);
                    img_delivery_completed_followup_up.setVisibility(View.VISIBLE);
                    rel_delivery_completed_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_delivery_completed_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_delivery_completed_follow_up.startAnimation(animation);

                }else if (img_delivery_completed_followup_up.getVisibility()==View.VISIBLE){
                    img_delivery_completed_follow_up_down.setVisibility(View.VISIBLE);
                    img_delivery_completed_followup_up.setVisibility(View.GONE);
                    rel_delivery_completed_follow_up.setBackgroundResource(0);
                    lin_delivery_completed_follow_up.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_delivery_completed_follow_up.startAnimation(animation);
                }
            }
        });

        rel_psf_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_psf_down.getVisibility()==View.VISIBLE){
                    img_psf_down.setVisibility(View.GONE);
                    img_psf_up.setVisibility(View.VISIBLE);
                    rel_psf_followup.setBackgroundResource(R.color.intersted_color);
                    lin_psf_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_psf_followup.startAnimation(animation);

                }else if (img_psf_up.getVisibility()==View.VISIBLE){
                    img_psf_down.setVisibility(View.VISIBLE);
                    img_psf_up.setVisibility(View.GONE);
                    lin_psf_follow_up.setVisibility(View.GONE);
                    rel_psf_followup.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_psf_followup.startAnimation(animation);
                }
            }
        });

        rel_fifteenth_day_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_fifteenth_day_follow_up_down.getVisibility()==View.VISIBLE){
                    img_fifteenth_day_follow_up_down.setVisibility(View.GONE);
                    img_fifteenth_day_follow_up_up.setVisibility(View.VISIBLE);
                    rel_fifteenth_day_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_fifteenth_day_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_fifteenth_day_follow_up.startAnimation(animation);

                }else if (img_fifteenth_day_follow_up_up.getVisibility()==View.VISIBLE){
                    img_fifteenth_day_follow_up_down.setVisibility(View.VISIBLE);
                    img_fifteenth_day_follow_up_up.setVisibility(View.GONE);
                    lin_fifteenth_day_follow_up.setVisibility(View.GONE);
                    rel_fifteenth_day_follow_up.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_fifteenth_day_follow_up.startAnimation(animation);
                }
            }
        });

        rel_oneK_follow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_oneK_follow_up_down.getVisibility()==View.VISIBLE){
                    img_oneK_follow_up_down.setVisibility(View.GONE);
                    img_oneK_follow_up_up.setVisibility(View.VISIBLE);
                    rel_oneK_follow_up.setBackgroundResource(R.color.intersted_color);
                    lin_oneK_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_oneK_follow_up.startAnimation(animation);

                }else if (img_oneK_follow_up_up.getVisibility()==View.VISIBLE){
                    img_oneK_follow_up_down.setVisibility(View.VISIBLE);
                    img_oneK_follow_up_up.setVisibility(View.GONE);
                    lin_oneK_follow_up.setVisibility(View.GONE);
                    rel_oneK_follow_up.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_oneK_follow_up.startAnimation(animation);
                }
            }
        });

        rel_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_drop_down.getVisibility()==View.VISIBLE){
                    img_drop_down.setVisibility(View.GONE);
                    img_drop_up.setVisibility(View.VISIBLE);
                    rel_drop.setBackgroundResource(R.color.intersted_color);
                    lin_drop_follow_up.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slipe_open_down);
                    rel_drop.startAnimation(animation);

                }else if (img_drop_up.getVisibility()==View.VISIBLE){
                    img_drop_down.setVisibility(View.VISIBLE);
                    img_drop_up.setVisibility(View.GONE);
                    lin_drop_follow_up.setVisibility(View.GONE);
                    rel_drop.setBackgroundResource(0);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_close_up);
                    rel_drop.startAnimation(animation);
                }
            }
        });

        Enquiry_Get_History();
    }

    public void back(View view) {
        startActivity(new Intent(HistoryActivity.this,CustomerDetailsActivity.class).putExtra("Status","BACK"));
         finish();
    }


    public void Enquiry_Get_History(){
        StringRequest requests = new StringRequest(Request.Method.POST, enquiry_gethistory_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    // progressDialog.dismiss();
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        String flag = jsonObj.getString("flag").toString();
                        if (flag.equals("1")){
                            txt_created_title.setText("Contact created");
                        }else if (flag.equals("2")){
                            txt_created_title.setText("Enquired On");
                        }
                        String date = jsonObj.getString("date");
                        SimpleDateFormat spfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = spfd.parse(date);
                            spfd = new SimpleDateFormat("dd MMM yyyy");
                            followupdate = spfd.format(newDate);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat spfse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date newDate = spfse.parse(date);
                            spfse = new SimpleDateFormat("HH:mm a");
                            followuptime = spfse.format(newDate);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        txt_enquired_on.setText(followupdate +"-"+"\t"+followuptime);
                        if (status_code.equals("1")) {
                            JSONArray contact_status = jsonObj.getJSONArray("contact_status");
                           for (int j = 0 ; j < contact_status.length(); j++ ){
                               JSONObject jsonObject = contact_status.getJSONObject(j);

                               String status_id  = jsonObject.getString("status_id");
                               String status  = jsonObject.getString("status");

                               if (status.equals("Enquiry Followup")){
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_enquiry_empty_message.setVisibility(View.VISIBLE);
                                       rv_enquiry_follow_up.setVisibility(View.GONE);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           Followup_details_List followup_details_list = new Followup_details_List();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MMM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }
                                           txt_enquiry_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           followup_details_lists.add(followup_details_list);
                                       }

                                       EnquiryFollowupAdapter adapter = new EnquiryFollowupAdapter(HistoryActivity.this, followup_details_lists);
                                       rv_enquiry_follow_up.setAdapter(adapter);

                                   }
                               } else if (status.equals("Contact Followup")) {
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                       if (jsonArray.isNull(0)) {
                                           txt_contact_empty_message.setVisibility(View.VISIBLE);
                                           rv_contact_follow_up.setVisibility(View.GONE);
                                       } else {
                                           for (int i = 0; i < jsonArray.length(); i++) {
                                               ContactHistoryList followup_details_list = new ContactHistoryList();
                                               JSONObject followup_details = jsonArray.getJSONObject(i);
                                               followup_details_list.setDate(followup_details.getString("date"));
                                               followup_details_list.setTime(followup_details.getString("time"));

                                               if (followup_details.isNull("remark")) {
                                                   System.out.println("Remark" + "remark null");

                                               } else {
                                                   followup_details_list.setRemark(followup_details.getString("remark"));
                                               }

                                               if (followup_details.isNull("today_remark")) {
                                                   System.out.println("Today Remark" + " Today remark");
                                               } else {
                                                   followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                               }

                                               if (followup_details.isNull("type")) {

                                               } else {
                                                   followup_details_list.setType(followup_details.getString("type"));

                                               }

                                               String created_dates = jsonObject.getString("created_date");
                                               SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                               try {
                                                   Date newDate = spf.parse(created_dates);
                                                   spf = new SimpleDateFormat("dd MMM yyyy");
                                                   followupdate = spf.format(newDate);


                                               } catch (Exception e) {
                                                   e.printStackTrace();
                                               }

                                               SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                               try {
                                                   Date newDate = spfe.parse(created_dates);
                                                   spfe = new SimpleDateFormat("HH:mm a");
                                                   followuptime = spfe.format(newDate);

                                               } catch (Exception e) {
                                                   e.printStackTrace();
                                               }
                                               txt_contact_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                               contactHistoryLists.add(followup_details_list);
                                           }

                                           ContactHistoryAdapter adapter = new ContactHistoryAdapter(HistoryActivity.this, contactHistoryLists);
                                           rv_contact_follow_up.setAdapter(adapter);

                                       }
                                   }else if (status.equals("New Contact")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_newcontact_empty_message.setVisibility(View.VISIBLE);
                                       rv_newcontact_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_newcontact_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           Followup_details_List followup_details_list = new Followup_details_List();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_newcontact_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           followup_details_lists.add(followup_details_list);
                                       }

                                       // ContactFollowupAdapter adapter = new ContactFollowupAdapter(HistoryActivity.this, followup_details_lists);
                                       // rv_contact_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Not Interested")){
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_not_interested_empty_message.setVisibility(View.VISIBLE);
                                       rv_not_interested_follow_up.setVisibility(View.GONE);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           NotInterestedList followup_details_list = new NotInterestedList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MMM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }
                                           txt_not_interested_empty_message.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           notInterestedLists.add(followup_details_list);
                                       }

                                       NotInterestedAdapter adapter = new NotInterestedAdapter(HistoryActivity.this, notInterestedLists);
                                       rv_not_interested_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Scheduled For Test Drive")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_scheduled_drive_empty_message.setVisibility(View.VISIBLE);
                                       rv_scheduled_drive_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_not_interested_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           ScheduleddrivefollowupList followup_details_list = new ScheduleddrivefollowupList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_scheduled_drive_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           scheduleddrivefollowupLists.add(followup_details_list);
                                       }

                                        ScheduleddrivefollowupAdapter adapter = new ScheduleddrivefollowupAdapter(HistoryActivity.this, scheduleddrivefollowupLists);
                                        rv_scheduled_drive_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Test Drive Completed")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_test_drive_complete_empty_message.setVisibility(View.VISIBLE);
                                       rv_test_drive_complete_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_test_drive_complete_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           TestDriveCompletedList followup_details_list = new TestDriveCompletedList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_test_drive_complete_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           testDriveCompletedLists.add(followup_details_list);
                                       }

                                        TestDriveCompleteAdapter adapter = new TestDriveCompleteAdapter(HistoryActivity.this, testDriveCompletedLists);
                                        rv_test_drive_complete_follow_up.setAdapter(adapter);

                                   }

                               }else if (status.equals("Booking Completed")){
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_booking_completed_empty_message.setVisibility(View.VISIBLE);
                                       rv_booking_completed_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_booking_completed_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           BookingCompletedList followup_details_list = new BookingCompletedList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_booking_completed_followup_change_status.setText("Status Changed :"+followupdate +"-"+"\t"+followuptime);
                                           bookingCompletedLists.add(followup_details_list);
                                       }

                                       BookingCompletedAdapter adapter = new BookingCompletedAdapter(HistoryActivity.this, bookingCompletedLists);
                                       rv_booking_completed_follow_up.setAdapter(adapter);

                                   }

                               }else if (status.equals("Booking Followup")) {
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_booking_follow_up_empty_message.setVisibility(View.VISIBLE);
                                       rv_booking_follow_up_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_booking_follow_up_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           BookingFollowupList followup_details_list = new BookingFollowupList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_booking_follow_up_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           bookingFollowupLists.add(followup_details_list);
                                       }

                                       BookingFollowupAdapter adapter = new BookingFollowupAdapter(HistoryActivity.this, bookingFollowupLists);
                                       rv_booking_follow_up_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Pre Delivery")){
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_pre_delivery_empty_message.setVisibility(View.VISIBLE);
                                       rv_pre_delivery_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_booking_follow_up_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           PreDeliveryList followup_details_list = new PreDeliveryList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_pre_delivery_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           preDeliveryLists.add(followup_details_list);
                                       }

                                       PreDeliveryAdapter adapter = new PreDeliveryAdapter(HistoryActivity.this, preDeliveryLists);
                                       rv_pre_delivery_follow_up.setAdapter(adapter);

                                   }

                               }else if (status.equals("Invoice Completed")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_invoice_completed_empty_message.setVisibility(View.VISIBLE);
                                       rv_invoice_completed_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_invoice_completed_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           InvoiceCompletedList followup_details_list = new InvoiceCompletedList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_invoice_completed_followup_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           invoiceCompletedLists.add(followup_details_list);
                                       }

                                       InvoiceCompletedAdapter adapter = new InvoiceCompletedAdapter(HistoryActivity.this, invoiceCompletedLists);
                                       rv_invoice_completed_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Delivery Completed")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_delivery_completed_empty_message.setVisibility(View.VISIBLE);
                                       rv_delivery_completed_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_delivery_completed_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           DeliveryCompletedList followup_details_list = new DeliveryCompletedList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_delivery_completed_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           deliveryCompletedLists.add(followup_details_list);
                                       }

                                       DeliveryCompletedAdapter adapter = new DeliveryCompletedAdapter(HistoryActivity.this, deliveryCompletedLists);
                                       rv_delivery_completed_follow_up.setAdapter(adapter);

                                   }

                               }else if (status.equals("PSF (Post Sales Followup)")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_psf_empty_message.setVisibility(View.VISIBLE);
                                       rv_psf_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_psf_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           PostSalesFollowupList followup_details_list = new PostSalesFollowupList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_psf_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           postSalesFollowupLists.add(followup_details_list);
                                       }

                                       PostSalesFollowupAdapter adapter = new PostSalesFollowupAdapter(HistoryActivity.this, postSalesFollowupLists);
                                       rv_psf_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("15Th Day Followup")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_fifteenth_day_empty_message.setVisibility(View.VISIBLE);
                                       rv_fifteenth_day_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_fifteenth_day_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           FifteenDayFollowupList followup_details_list = new FifteenDayFollowupList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_fifteenth_day_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           fifteenDayFollowupLists.add(followup_details_list);
                                       }

                                       FifteenDayFollowupAdapter adapter = new FifteenDayFollowupAdapter(HistoryActivity.this, fifteenDayFollowupLists);
                                       rv_fifteenth_day_follow_up.setAdapter(adapter);

                                   }

                               }else if (status.equals("1K Followup")){

                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_oneK_empty_message.setVisibility(View.VISIBLE);
                                       rv_oneK_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                       SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spf.parse(created_dates);
                                           spf = new SimpleDateFormat("dd MMM yyyy");
                                           followupdate = spf.format(newDate);


                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                       SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       try {
                                           Date newDate = spfe.parse(created_dates);
                                           spfe = new SimpleDateFormat("HH:mm a");
                                           followuptime = spfe.format(newDate);

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                       txt_oneK_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           OneKFollowupList followup_details_list = new OneKFollowupList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_oneK_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           oneKFollowupLists.add(followup_details_list);
                                       }

                                       OneKFollowupAdapter adapter = new OneKFollowupAdapter(HistoryActivity.this, oneKFollowupLists);
                                       rv_oneK_follow_up.setAdapter(adapter);

                                   }
                               }else if (status.equals("Drop")){
                                   JSONArray jsonArray = jsonObject.getJSONArray("followup_details");
                                   if (jsonArray.isNull(0)) {
                                       txt_drop_empty_message.setVisibility(View.VISIBLE);
                                       rv_drop_follow_up.setVisibility(View.GONE);
                                       String created_dates = jsonObject.getString("created_date");
                                      if (created_dates.equals(null)||created_dates.equals("")){
                                          txt_oneK_change_status.setText("Status Changed :" + "\t" + "NA");
                                      }else {
                                          SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                          try {
                                              Date newDate = spf.parse(created_dates);
                                              spf = new SimpleDateFormat("dd MMM yyyy");
                                              followupdate = spf.format(newDate);


                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }

                                          SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                          try {
                                              Date newDate = spfe.parse(created_dates);
                                              spfe = new SimpleDateFormat("HH:mm a");
                                              followuptime = spfe.format(newDate);

                                          } catch (Exception e) {
                                              e.printStackTrace();
                                          }

                                          txt_drop_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                      }

                                   } else {
                                       for (int i = 0; i < jsonArray.length(); i++) {
                                           DropList followup_details_list = new DropList();
                                           JSONObject followup_details = jsonArray.getJSONObject(i);
                                           followup_details_list.setDate(followup_details.getString("date"));
                                           followup_details_list.setTime(followup_details.getString("time"));

                                           if (followup_details.isNull("remark")) {
                                               System.out.println("Remark" + "remark null");

                                           } else {
                                               followup_details_list.setRemark(followup_details.getString("remark"));
                                           }

                                           if (followup_details.isNull("today_remark")) {
                                               System.out.println("Today Remark" + " Today remark");
                                           } else {
                                               followup_details_list.setToday_remark(followup_details.getString("today_remark"));

                                           }

                                           if (followup_details.isNull("type")) {

                                           } else {
                                               followup_details_list.setType(followup_details.getString("type"));

                                           }

                                           String created_dates = jsonObject.getString("created_date");
                                           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spf.parse(created_dates);
                                               spf = new SimpleDateFormat("dd MM yyyy");
                                               followupdate = spf.format(newDate);


                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           SimpleDateFormat spfe = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                           try {
                                               Date newDate = spfe.parse(created_dates);
                                               spfe = new SimpleDateFormat("HH:mm a");
                                               followuptime = spfe.format(newDate);

                                           } catch (Exception e) {
                                               e.printStackTrace();
                                           }

                                           txt_oneK_change_status.setText("Status Changed :" + followupdate + "-" + "\t" + followuptime);
                                           dropLists.add(followup_details_list);
                                       }

                                       DropAdapter adapter = new DropAdapter(HistoryActivity.this, dropLists);
                                       rv_drop_follow_up.setAdapter(adapter);

                                   }
                               }
                           }

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");
                            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
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
                params.put("CONTACT_ID", UPDATE_ENQUIRY_ID);
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
        startActivity(new Intent(HistoryActivity.this,CustomerDetailsActivity.class).putExtra("Status","BACK"));
        finish();
    }
}
