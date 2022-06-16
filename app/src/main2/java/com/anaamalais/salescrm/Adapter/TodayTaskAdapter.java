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

import com.anaamalais.salescrm.CompleteDetailsActivity;
import com.anaamalais.salescrm.CustomerDetailsActivity;
import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.TodayTaskList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.ToadayTaskViewHolder> {
    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<TodayTaskList> todayTaskLists;

    String follow_up_date , follow_up_time;

    //getting the context and product list with constructor
    public TodayTaskAdapter(Context mCtx, List<TodayTaskList> todayTaskLists) {
        this.mCtx = mCtx;
        this.todayTaskLists = todayTaskLists;
    }

    @Override
    public ToadayTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_today_task, null);

        return new ToadayTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ToadayTaskViewHolder holder, int position){
        //getting the product of the specified position
        final TodayTaskList allContactsList = todayTaskLists.get(position);
        holder.txt_cus_name.setText(allContactsList.getName());

        if (allContactsList.getStatus().equals("New Contact") ||allContactsList.getStatus().equals("Contact Followup")|| allContactsList.getStatus().equals("Enquiry Followup")
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

                    mCtx.startActivity(new Intent(mCtx, CompleteDetailsActivity.class).putExtra("CONTACTID",allContactsList.getContact_id()));
                    ((Activity)mCtx).finish();
            }
        });
    }


    @Override
    public int getItemCount () {
        return todayTaskLists.size();
    }


    class ToadayTaskViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cus_name, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;
        LinearLayout lin_status_contact;
        public ToadayTaskViewHolder(View itemView) {
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
