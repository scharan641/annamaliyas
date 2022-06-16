package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.DropReasonsList;
import com.anaamalais.salescrm.List.FiltersList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusBasedFiltersAdapter extends RecyclerView.Adapter<StatusBasedFiltersAdapter.StatusBasedFiltersViewHolderView> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<FiltersList> filtersLists;
    int selectedItemPosition = -1;
    String[] namesArr;
    FiltersList [] filtersList;

    //getting the context and product list with constructor
    public StatusBasedFiltersAdapter(Context mCtx, List<FiltersList> filtersLists) {
        this.mCtx = mCtx;
        this.filtersLists = filtersLists;
    }

    @Override
    public StatusBasedFiltersViewHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_filters_status_based, null);

        return new StatusBasedFiltersViewHolderView(view);
    }

    @Override
    public void onBindViewHolder (final StatusBasedFiltersViewHolderView holder, int position){
        //getting the product of the specified position
        final FiltersList allContactsList = filtersLists.get(position);
        holder.txt_drop_reason.setText(allContactsList.getStatus_based_value());

       ;

        holder.checkbox_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String[] fs = new String[allContactsList.getStatus_based_id().length()];
                  List<String> ttt = new ArrayList<>();
                   StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0 ; i < allContactsList.getStatus_based_id().length() ; i++){
                        String tttt=  allContactsList.getStatus_based_id();
                        ttt.add(tttt);
                        System.out.println("sahacah"+ttt.toString());

                    }


            }
        });

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


    class StatusBasedFiltersViewHolderView extends RecyclerView.ViewHolder {

        TextView txt_drop_reason, txt_cus_location, txt_model, txt_enquiry_follow_up , txt_new;
        AppCompatCheckBox checkbox_status;
        public StatusBasedFiltersViewHolderView(View itemView) {
            super(itemView);
            txt_drop_reason = itemView.findViewById(R.id.txt_cus_name);
            checkbox_status = itemView.findViewById(R.id.checkbox_status);
            // txt_model  = itemView.findViewById(R.id.txt_model);
            // txt_enquiry_follow_up  = itemView.findViewById(R.id.txt_enquiry_follow_up);
            // txt_new  = itemView.findViewById(R.id.txt_new);
        }
    }
}
