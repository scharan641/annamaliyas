package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.List.NotificationList;
import com.anaamalais.salescrm.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<NotificationList> notificationLists;

    String followupdate , followuptime;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus , progressBarStatusretail = 0;

    //getting the context and product list with constructor
    public NotificationAdapter(Context mCtx, List<NotificationList> notificationLists) {
        this.mCtx = mCtx;
        this.notificationLists = notificationLists;
    }

    @Override
    public NotificationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_notification, null);

        return new NotificationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final NotificationAdapterViewHolder holder, int position){
        //getting the product of the specified position
        final NotificationList notificationList = notificationLists.get(position);
        if (notificationList.getStatus().equals("1")){
            holder.txt_notification_message.setText(notificationList.getMessage());
            holder.txt_notification_message.setTextColor(Color.parseColor("#000000"));
            holder.txt_read_status.setText("Unseen");
            holder.txt_read_status.setTextColor(Color.parseColor("#23CB63"));
        }else if (notificationList.getStatus().equals("0")){
            holder.txt_notification_message.setText(notificationList.getMessage());
            holder.txt_notification_message.setTextColor(Color.parseColor("#CDCDCD"));
            holder.txt_read_status.setText("Seen");
            holder.txt_read_status.setTextColor(Color.parseColor("#EB0A1E"));
        }

    }

    @Override
    public int getItemCount () {
        return notificationLists.size();
    }


    class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView txt_notification_message, txt_read_status, txt_cus_achive;
        ProgressBar progressBar;

        public NotificationAdapterViewHolder(View itemView) {
            super(itemView);
            txt_notification_message = itemView.findViewById(R.id.txt_notification_message);
            txt_read_status = itemView.findViewById(R.id.txt_read_status);

        }
    }
}
