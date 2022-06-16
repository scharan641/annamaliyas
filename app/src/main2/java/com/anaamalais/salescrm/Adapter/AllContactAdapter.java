package com.anaamalais.salescrm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.AddContactActivity;
import com.anaamalais.salescrm.AddEnquiryActivity;
import com.anaamalais.salescrm.BookingCompletedActivity;
import com.anaamalais.salescrm.BookingFollowUpActivity;
import com.anaamalais.salescrm.CustomerDetailsActivity;
import com.anaamalais.salescrm.DeliveryActivity;
import com.anaamalais.salescrm.DroppedActivity;
import com.anaamalais.salescrm.FifteenthDayFollowupActivity;
import com.anaamalais.salescrm.InvoiceCompletedActivity;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.OnekActivity;
import com.anaamalais.salescrm.PostSalesFollowupActivity;
import com.anaamalais.salescrm.PreDeliveryActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.ScheduleForTheDriveActivity;
import com.anaamalais.salescrm.TestDriveCompletedActivity;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AllContactAdapter extends RecyclerView.Adapter<AllContactAdapter.AllContactViewHolder> {
    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<AllContactsList> allContactsLists;

    String follow_up_date , follow_up_time;

    //getting the context and product list with constructor
    public AllContactAdapter(Context mCtx, List<AllContactsList> allContactsLists) {
        this.mCtx = mCtx;
        this.allContactsLists = allContactsLists;
    }

    @Override
    public AllContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_all_contact, null);

        return new AllContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder ( final AllContactViewHolder holder, int position){
        //getting the product of the specified position
        final AllContactsList allContactsList = allContactsLists.get(position);
        holder.txt_cus_name.setText(allContactsList.getName());

       if (allContactsList.getStatus().equals("New Contact") || allContactsList.getStatus().equals("Contact Followup") || allContactsList.getStatus().equals("Enquiry Followup")
       || allContactsList.getStatus().equals("Booking Followup") ||allContactsList.getStatus().equals("PSF (Post Sales Followup")||
       allContactsList.getStatus().equals("1K Followup")||allContactsList.getStatus().equals("Not Interested")){
           holder.txt_cus_location.setText(allContactsList.getAddress());
       }else {
           holder.txt_cus_location.setText(allContactsList.getModel_and_suffix());
       }
        String date =  allContactsList.getFollow_up_date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd MMM");
            follow_up_date = spf.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String time =  allContactsList.getFollow_up_time();
        SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss");
        try {
            Date newDate = spfe.parse(time);
            spfe = new SimpleDateFormat("hh:mm a");
            follow_up_time = spfe.format(newDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txt_model.setText(follow_up_date +  "\t" + "-" + "\t" + follow_up_time);
        holder.txt_enquiry_follow_up.setText(allContactsList.getStatus());
        if (allContactsList.getFollow_up_type()!=null){
            if (allContactsList.getFollow_up_type().equals("1")){
                holder.txt_new.setText("Telephone");
            }else if(allContactsList.getFollow_up_type().equals("2")){
                holder.txt_new.setText("Direct Visit");
            }
        }

        holder.lin_status_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = allContactsList.getStatus();
                if (status.equals("New Contact")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_ADDRESS, allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx, CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Enquiry Followup")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_ADDRESS, allContactsList.getAddress());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                } else if (status.equals("Scheduled For Test Drive")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.DLNUMBER,allContactsList.getDl_number());
                    MyFunctions.setSharedPrefs(mCtx,Constants.TESTDRIVEID,allContactsList.getTest_drive_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.DLPHOTO,allContactsList.getDl_photo());
                    MyFunctions.setSharedPrefs(mCtx,Constants.TDSCHEDULEDON,allContactsList.getTd_scheduled_on());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Test Drive Completed")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.OVERALLTDEXPERIANCE,allContactsList.getOverall_td_exp());
                    MyFunctions.setSharedPrefs(mCtx,Constants.OVERALLCONDITIONOFVEHICLE,allContactsList.getOverall_condition_of_vehicle());
                    MyFunctions.setSharedPrefs(mCtx,Constants.SALES_CONSULTANTKNOWLEDGEOFTHEPRODUCT,allContactsList.getSales_consultant_knowledge_of_the_product());
                    MyFunctions.setSharedPrefs(mCtx,Constants.OVERALLSALESCONSULTANTKNOWLEDGE_EXPERIENCE,allContactsList.getOverall_sales_consultant_knowledge_experiance());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Booking Completed")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGCOMPLETEDID,allContactsList.getBooking_completed_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGMODEANDSUFFIX,allContactsList.getBooking_model_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.INTERIORCOLOR,allContactsList.getInterior_color());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EXTERIORCOLOR,allContactsList.getExterior_color());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGAMOUNT,allContactsList.getBooking_amount());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGDATE,allContactsList.getBooking_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGTIME, allContactsList.getBooking_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.PAYMENTMODE, allContactsList.getPayment_mode());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Booking Followup")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKING_FOLLOWUP_DETAILS_ID,allContactsList.getBooking_followup_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.VEHICLE_ALLOTTED,allContactsList.getVehicle_alloted());
                    MyFunctions.setSharedPrefs(mCtx,Constants.VEHICLE_NOT_ALLOTTED_REASON,allContactsList.getVehicle_not_alloted_reason());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EX_SHOWROOM_PRICE,allContactsList.getEx_showroom_price());
                    MyFunctions.setSharedPrefs(mCtx,Constants.TCS,allContactsList.getTcs());
                    MyFunctions.setSharedPrefs(mCtx,Constants.TCS_AMOUNT,allContactsList.getTcs_amount());
                    MyFunctions.setSharedPrefs(mCtx,Constants.ROAD_TAX,allContactsList.getRoad_tax());
                    MyFunctions.setSharedPrefs(mCtx,Constants.INSURANCE,allContactsList.getInsurance());
                    MyFunctions.setSharedPrefs(mCtx,Constants.REGISTRATION,allContactsList.getRegistration());
                    MyFunctions.setSharedPrefs(mCtx,Constants.ACCESSORIES,allContactsList.getAccessories());
                    MyFunctions.setSharedPrefs(mCtx,Constants.WARRANTY,allContactsList.getWarranty());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FASTAGG,allContactsList.getFastag());
                    MyFunctions.setSharedPrefs(mCtx,Constants.OFFERS,allContactsList.getOffers());
                    MyFunctions.setSharedPrefs(mCtx,Constants.NET_AMOUNT,allContactsList.getNet_amount());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EXPECTED_DOWNPAYMENT_AMOUNT,allContactsList.getExpected_downpayment_amount());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EXPECTED_DOWNPAYMENT_DATE,allContactsList.getExpected_downpayment_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EXPECTED_INVOICE_DATE,allContactsList.getExpected_invoice_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.PAPER_DOCUMENTS_COLLECTED,allContactsList.getPaper_documents_collected());
                    MyFunctions.setSharedPrefs(mCtx,Constants.LOGIN_COMPLETED,allContactsList.getLogin_completed());
                    MyFunctions.setSharedPrefs(mCtx,Constants.QUERY_CLEARANCE,allContactsList.getQuery_clearance());
                    MyFunctions.setSharedPrefs(mCtx,Constants.LOAN_AMOUNT,allContactsList.getLoan_amount());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Pre Delivery")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTIME,allContactsList.getFollow_up_time());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPTYPE,allContactsList.getFollow_up_type());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.ACCESSORIES_FITMENT_STATUS,allContactsList.getAccessories_fitment_status());
                    MyFunctions.setSharedPrefs(mCtx,Constants.AF_WAITING_REASON,allContactsList.getAf_waiting_reason());
                    MyFunctions.setSharedPrefs(mCtx,Constants.EXTENDED_WARRANTY,allContactsList.getExtended_warranty());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FASTAG,allContactsList.getFastag());
                    MyFunctions.setSharedPrefs(mCtx,Constants.RTO_PROCESS,allContactsList.getRto_process());
                    MyFunctions.setSharedPrefs(mCtx,Constants.RTO_WAITING_REASON,allContactsList.getRto_waiting_reason());
                    MyFunctions.setSharedPrefs(mCtx,Constants.ALLOCATION_PDI,allContactsList.getAllocation_pdi());
                    MyFunctions.setSharedPrefs(mCtx,Constants.PREFERRED_RTO_DATE,allContactsList.getPreferred_rto_date());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Invoice Completed")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    //MyFunctions.setSharedPrefs(mCtx,Constants.INVOICE_COMPLETED_ID,allContactsList.getIn);
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGDATE,allContactsList.getBooking_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.BOOKINGTIME,allContactsList.getExpected_invoice_date());
                   // MyFunctions.setSharedPrefs(mCtx,Constants.INVOICE_COMPLETED_COMMENTS,allContactsList.getInC);
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Delivery Completed")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPDATE,allContactsList.getFollow_up_date());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUP_REMARK,allContactsList.getComments());
                    MyFunctions.setSharedPrefs(mCtx,Constants.TIME,allContactsList.getTime());
                    MyFunctions.setSharedPrefs(mCtx,Constants.DATE,allContactsList.getDate());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                    MyFunctions.setSharedPrefs(mCtx,Constants.REFNAME,allContactsList.getRefName());
                    MyFunctions.setSharedPrefs(mCtx,Constants.REFPHONE,allContactsList.getRefPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FOLLOWUPID,allContactsList.getFollow_up_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.DELIVERY_COMPLETED_ID,allContactsList.getDelivery_completed_id());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("PSF (Post Sales Followup)")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getModel_and_suffix());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FIFTEEN_dAY_FOLLOWUP_ID,allContactsList.getPost_sales_followup_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.DELIVERY_COMPLETED_ID,allContactsList.getDelivery_completed_id());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("15Th Day Followup")){
                    MyFunctions.setSharedPrefs(mCtx,Constants.CONTACT_ID,allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FIFTEEN_dAY_FOLLOWUP_ID,allContactsList.getFifteenth_day_followup_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_NAME,allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_PHONE,allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                } else if (status.equals("1K Followup")){
                    MyFunctions.setSharedPrefs(mCtx,Constants.CONTACT_ID,allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FIFTEEN_dAY_FOLLOWUP_ID,allContactsList.getOne_k_followup_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_NAME,allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_PHONE,allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Drop")){
                    MyFunctions.setSharedPrefs(mCtx,Constants.CONTACT_ID,allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_NAME,allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_PHONE,allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                   MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Not Interested")){
                    MyFunctions.setSharedPrefs(mCtx, Constants.CONTACT_ID, allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_NAME, allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_PHONE, allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx, Constants.CUSTOMER_ADDRESS, allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }else if (status.equals("Contact Followup")) {
                    MyFunctions.setSharedPrefs(mCtx,Constants.CONTACT_ID,allContactsList.getContact_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.FIFTEEN_dAY_FOLLOWUP_ID,allContactsList.getOne_k_followup_id());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_NAME,allContactsList.getName());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_PHONE,allContactsList.getPhone());
                    MyFunctions.setSharedPrefs(mCtx,Constants.COMMENT,allContactsList.getComment());
                    MyFunctions.setSharedPrefs(mCtx,Constants.CUSTOMER_ADDRESS,allContactsList.getAddress());
                    mCtx.startActivity(new Intent(mCtx,CustomerDetailsActivity.class).putExtra("Status", allContactsList.getStatus()));
                    ((Activity)mCtx).finish();
                }
            }
        });
    }


    @Override
    public int getItemCount () {
        return allContactsLists.size();
    }


    class AllContactViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cus_name, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;
        LinearLayout lin_status_contact;
        public AllContactViewHolder(View itemView) {
            super(itemView);
            txt_cus_name = itemView.findViewById(R.id.txt_cus_name);
            txt_cus_location = itemView.findViewById(R.id.txt_cus_location);
            txt_model  = itemView.findViewById(R.id.txt_model);
            txt_enquiry_follow_up  = itemView.findViewById(R.id.txt_enquiry_follow_up);
            txt_new  = itemView.findViewById(R.id.txt_new);
            lin_status_contact =  itemView.findViewById(R.id.lin_status_contact);
        }
    }
}
