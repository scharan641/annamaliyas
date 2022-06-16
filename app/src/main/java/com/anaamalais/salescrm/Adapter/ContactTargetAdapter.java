package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.Followup_details_List;
import com.anaamalais.salescrm.List.Mode;
import com.anaamalais.salescrm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContactTargetAdapter extends RecyclerView.Adapter<ContactTargetAdapter.ContactTargetViewHolder> {
    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<Mode> modes;

    String followupdate , followuptime;

    //getting the context and product list with constructor
    public ContactTargetAdapter(Context mCtx, List<Mode> modes) {
        this.mCtx = mCtx;
        this.modes = modes;
    }

    @Override
    public ContactTargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_contact_target, null);

        return new ContactTargetViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ContactTargetViewHolder holder, int position){
        //getting the product of the specified position
        final Mode mode = modes.get(position);
        holder.txt_mode_name.setText(mode.getMode_name());
        holder.txt_cus_total.setText("/"+mode.getTotal_target());
        holder.txt_cus_achive.setText(mode.getAchieved_target());

    }

    @Override
    public int getItemCount () {
        return modes.size();
    }


    class ContactTargetViewHolder extends RecyclerView.ViewHolder {

        TextView txt_mode_name, txt_cus_total, txt_cus_achive;

        public ContactTargetViewHolder(View itemView) {
            super(itemView);
            txt_mode_name = itemView.findViewById(R.id.txt_mode_name);
            txt_cus_total = itemView.findViewById(R.id.txt_cus_total);
            txt_cus_achive = itemView.findViewById(R.id.txt_cus_achive);
        }
    }
}
