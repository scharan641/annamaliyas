package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.AllContactsList;
import com.anaamalais.salescrm.List.DropReasonsList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import java.util.List;

public class DropReasonsAdapter extends RecyclerView.Adapter<DropReasonsAdapter.DropReasonsViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<DropReasonsList> dropReasonsLists;
     int selectedItemPosition = -1;

    //getting the context and product list with constructor
    public DropReasonsAdapter(Context mCtx, List<DropReasonsList> dropReasonsLists) {
        this.mCtx = mCtx;
        this.dropReasonsLists = dropReasonsLists;
    }

    @Override
    public DropReasonsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_dropreasons, null);

        return new DropReasonsViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final DropReasonsViewHolder holder, int position){
        //getting the product of the specified position
        final DropReasonsList allContactsList = dropReasonsLists.get(position);
        holder.txt_drop_reason.setText(allContactsList.getDrop_reason());
     holder.txt_drop_reason.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             selectedItemPosition = position;
             notifyDataSetChanged();

         }
     });

        if(selectedItemPosition == position){
            holder.txt_drop_reason.setTextColor(Color.parseColor("#2ABB00"));
            String dropreasonid = allContactsList.getDrop_id();
            MyFunctions.setSharedPrefs(mCtx, Constants.DROP_REASON_ID,dropreasonid);
        }else {
            holder.txt_drop_reason.setTextColor(Color.parseColor("#2F3044"));
        }


    }


    @Override
    public int getItemCount () {
        return dropReasonsLists.size();
    }


    class DropReasonsViewHolder extends RecyclerView.ViewHolder {

        TextView txt_drop_reason, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;

        public DropReasonsViewHolder(View itemView) {
            super(itemView);
            txt_drop_reason = itemView.findViewById(R.id.txt_drop_reason);
            //txt_cus_location = itemView.findViewById(R.id.txt_cus_location);
           // txt_model  = itemView.findViewById(R.id.txt_model);
           // txt_enquiry_follow_up  = itemView.findViewById(R.id.txt_enquiry_follow_up);
           // txt_new  = itemView.findViewById(R.id.txt_new);
        }
    }
}
