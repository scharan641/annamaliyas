package com.anaamalais.salescrm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.BookingCompletedActivity;
import com.anaamalais.salescrm.BookingFollowUpActivity;
import com.anaamalais.salescrm.CustomerDetailsActivity;
import com.anaamalais.salescrm.DeliveryActivity;
import com.anaamalais.salescrm.DroppedActivity;
import com.anaamalais.salescrm.FifteenthDayFollowupActivity;
import com.anaamalais.salescrm.InvoiceCompletedActivity;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.ContactStatusList;
import com.anaamalais.salescrm.OnekActivity;
import com.anaamalais.salescrm.PostSalesFollowupActivity;
import com.anaamalais.salescrm.PreDeliveryActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.ScheduleForTheDriveActivity;
import com.anaamalais.salescrm.TestDriveCompletedActivity;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import java.util.List;

public class ContactStatusAdapter extends RecyclerView.Adapter<ContactStatusAdapter.ContactStatusViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<ContactStatusList> contactStatusLists;

    //getting the context and product list with constructor
    public ContactStatusAdapter(Context mCtx, List<ContactStatusList> contactStatusLists) {
        this.mCtx = mCtx;
        this.contactStatusLists = contactStatusLists;
    }

    @Override
    public ContactStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_customer_status, null);

        return new ContactStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ContactStatusViewHolder holder, int position){
        //getting the product of the specified position
        final ContactStatusList allContactsList = contactStatusLists.get(position);
        holder.txt_contact_status.setText(allContactsList.getStatus());

        holder.txt_contact_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_id = allContactsList.getStatus_id();
                if (status_id.equals("5")) {
                    mCtx.startActivity(new Intent(mCtx, ScheduleForTheDriveActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                } else if (status_id.equals("6")) {

                    mCtx.startActivity(new Intent(mCtx, TestDriveCompletedActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(mCtx,Constants.TESTDRIVEID,"")));
                    ((Activity) mCtx).finish();
                } else if (status_id.equals("7")) {
                    mCtx.startActivity(new Intent(mCtx, BookingCompletedActivity.class).putExtra("SCHEDULED_STATUS","NEW BOOKING")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(mCtx,Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                } else if (status_id.equals("8")) {
                    mCtx.startActivity(new Intent(mCtx, BookingFollowUpActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID",MyFunctions.getSharedPrefs(mCtx,Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                } else if (status_id.equals("9")) {
                    mCtx.startActivity(new Intent(mCtx, DroppedActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                } else if (status_id.equals("10")) {
                    mCtx.startActivity(new Intent(mCtx, PreDeliveryActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }else if (status_id.equals("11")) {
                    mCtx.startActivity(new Intent(mCtx, InvoiceCompletedActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }else if (status_id.equals("12")) {
                    mCtx.startActivity(new Intent(mCtx, DeliveryActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }else if (status_id.equals("13")) {
                    mCtx.startActivity(new Intent(mCtx, PostSalesFollowupActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }else if (status_id.equals("14")) {
                    mCtx.startActivity(new Intent(mCtx, FifteenthDayFollowupActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }else if (status_id.equals("15")) {
                    mCtx.startActivity(new Intent(mCtx, OnekActivity.class).putExtra("SCHEDULED_STATUS","NEW SCHEDULED")
                            .putExtra("CONTACTID", MyFunctions.getSharedPrefs(mCtx, Constants.CONTACT_ID,"")));
                    ((Activity) mCtx).finish();
                }


            }
        });
    }


    @Override
    public int getItemCount () {
        return contactStatusLists.size();
    }


    class ContactStatusViewHolder extends RecyclerView.ViewHolder {

        TextView txt_contact_status, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;

        public ContactStatusViewHolder(View itemView) {
            super(itemView);
            txt_contact_status = itemView.findViewById(R.id.txt_contact_status);

        }
    }
}
