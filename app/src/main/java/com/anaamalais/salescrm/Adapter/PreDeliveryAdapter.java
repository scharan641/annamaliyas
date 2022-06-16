package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.Followup_details_List;
import com.anaamalais.salescrm.List.PreDeliveryList;
import com.anaamalais.salescrm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PreDeliveryAdapter extends RecyclerView.Adapter<PreDeliveryAdapter.PreDeliveryViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<PreDeliveryList> preDeliveryLists;

    String followupdate , followuptime;

    //getting the context and product list with constructor
    public PreDeliveryAdapter(Context mCtx, List<PreDeliveryList> preDeliveryLists) {
        this.mCtx = mCtx;
        this.preDeliveryLists = preDeliveryLists;
    }

    @Override
    public PreDeliveryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_contact_followup, null);

        return new PreDeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final PreDeliveryViewHolder holder, int position){
        //getting the product of the specified position
        final PreDeliveryList allContactsList = preDeliveryLists.get(position);
        holder.txt_count.setText(Integer.toString(position+1));

        String date = allContactsList.getDate();
        String time = allContactsList.getTime();

        SimpleDateFormat spf = new SimpleDateFormat("dd-mm-yyyy");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd MMM yyyy");
            followupdate = spf.format(newDate);

            //Toast.makeText(mCtx, allContactsList.getRemark(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleDateFormat spfe = new SimpleDateFormat("HH:mm:ss a");
        try {
            Date newDate = spfe.parse(time);
            spfe = new SimpleDateFormat("HH:mm a");
            followuptime = spfe.format(newDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.txt_call_time.setText(followupdate + "\t" +  "-" + "\t" + followuptime);

        String followuptype = allContactsList.getType();
        if (followuptype.equals("1")){
            holder.txt_type.setText("Telephone");
        }else if (followuptype.equals("2")){
            holder.txt_type.setText("Direct");
        }

        if (allContactsList.getRemark()!=null){
            holder.txt_remark.setText(allContactsList.getRemark());
        }else{
            holder.txt_remark.setText("No Remarks");
        }

        if (allContactsList.getToday_remark()!=null){
            holder.txt_today_remark.setText(allContactsList.getToday_remark());
        }else {
            holder.txt_today_remark.setText("No Today Remarks");
        }

    }

    @Override
    public int getItemCount () {
        return preDeliveryLists.size();
    }


    class PreDeliveryViewHolder extends RecyclerView.ViewHolder {

        TextView txt_count, txt_call_time, txt_type, txt_remark , txt_today_remark;

        public PreDeliveryViewHolder(View itemView) {
            super(itemView);
            txt_count = itemView.findViewById(R.id.txt_count);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_call_time = itemView.findViewById(R.id.txt_call_time);
            txt_remark = itemView.findViewById(R.id.txt_remark);
            txt_today_remark = itemView.findViewById(R.id.txt_today_remark);



        }
    }
}
