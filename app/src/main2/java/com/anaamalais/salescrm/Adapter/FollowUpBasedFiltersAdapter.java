package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.FiltersList;
import com.anaamalais.salescrm.R;

import java.util.List;

public class FollowUpBasedFiltersAdapter extends RecyclerView.Adapter<FollowUpBasedFiltersAdapter.FollowUpBasedViewHolder> {


    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<FiltersList> filtersLists;
    int selectedItemPosition = -1;

    //getting the context and product list with constructor
    public FollowUpBasedFiltersAdapter(Context mCtx, List<FiltersList> filtersLists) {
        this.mCtx = mCtx;
        this.filtersLists = filtersLists;
    }

    @Override
    public FollowUpBasedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_checkbox_followup_type, null);

        return new FollowUpBasedViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final FollowUpBasedViewHolder holder, int position){
        //getting the product of the specified position
        final FiltersList allContactsList = filtersLists.get(position);
        holder.txt_drop_reason.setText(allContactsList.getFollowup_type_value());

     /*    holder.txt_drop_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItemPosition = position;
                notifyDataSetChanged();

            }
        });*/

      /*  if(selectedItemPosition == position){
            holder.txt_drop_reason.setTextColor(Color.parseColor("#2ABB00"));
            String dropreasonid = allContactsList.getDrop_id();
            MyFunctions.setSharedPrefs(mCtx, Constants.DROP_REASON_ID,dropreasonid);
        }else {
            holder.txt_drop_reason.setTextColor(Color.parseColor("#2F3044"));
        }*/


    }


    @Override
    public int getItemCount () {
        return filtersLists.size();
    }


    class FollowUpBasedViewHolder extends RecyclerView.ViewHolder {

        TextView txt_drop_reason, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;
        AppCompatCheckBox checkbox_status;
        public FollowUpBasedViewHolder(View itemView) {
            super(itemView);
            txt_drop_reason = itemView.findViewById(R.id.txt_cus_name);
            checkbox_status = itemView.findViewById(R.id.checkbox_status);
            // txt_model  = itemView.findViewById(R.id.txt_model);
            // txt_enquiry_follow_up  = itemView.findViewById(R.id.txt_enquiry_follow_up);
            // txt_new  = itemView.findViewById(R.id.txt_new);
        }
    }
}
