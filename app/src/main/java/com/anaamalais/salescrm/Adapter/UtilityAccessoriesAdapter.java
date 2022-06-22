package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.ExterioraccessoriesList;
import com.anaamalais.salescrm.List.UtilityaccessoriesList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;

import java.util.Arrays;
import java.util.List;

public class UtilityAccessoriesAdapter extends RecyclerView.Adapter<UtilityAccessoriesAdapter.UtilityAccessoriesViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<UtilityaccessoriesList> utilityaccessoriesLists;

    private OnItemsutilityaccessoriesClickListener listener;

    //getting the context and product list with constructor
    //(Context mCtx, List<InterioraccessoriesList> interioraccessoriesLists,OnItemsInterioraccessoriesClickListener listener
    public UtilityAccessoriesAdapter(Context mCtx, List<UtilityaccessoriesList> utilityaccessoriesLists,OnItemsutilityaccessoriesClickListener listener) {
        this.mCtx = mCtx;
        this.utilityaccessoriesLists = utilityaccessoriesLists;
        this.listener = listener;

    }

    public void setWhenClickListener(OnItemsutilityaccessoriesClickListener listener){
        this.listener = listener;
    }

    @Override
    public UtilityAccessoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_interioraccessories, null);

        return new UtilityAccessoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final UtilityAccessoriesViewHolder holder, int position){
        //getting the product of the specified position
        final UtilityaccessoriesList mode = utilityaccessoriesLists.get(position);
        holder.setIsRecyclable(false);
        holder.txt_cus_name.setText(mode.getUtility_accessory_name());
        holder.txt_cus_number.setText(mode.getUtility_accessory_price());


        if(mode.isOn()){
            holder.swt_notify_customer.setChecked(true);
        }else{
            holder.swt_notify_customer.setChecked(false);
        }

        holder.swt_notify_customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.swt_notify_customer.setChecked(b);
                listener.onItemUnchecked(position,b,Constants.UTILITY);
            }
        });


     /*   holder.lin_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    holder.swt_notify_customer.setChecked(true);
                    listener.onItemClick(mode);
                    String [] codes = new String[utilityaccessoriesLists.size()];
                    for (int i = 0 ; i < utilityaccessoriesLists.size() ; i++){
                        codes[i] = utilityaccessoriesLists.get(i).getUtility_accessory_id();
                        MyFunctions.setSharedPrefs(mCtx, Constants.ACCESSORIESLIST, Arrays.toString(codes));
                        System.out.println("sahhh"+ Arrays.toString(codes));
                    }
                }else {
                    holder.swt_notify_customer.setChecked(false);
                }
            }
        });*/
    }

    @Override
    public int getItemCount () {
        return utilityaccessoriesLists.size();
    }


    class UtilityAccessoriesViewHolder extends RecyclerView.ViewHolder {
        TextView txt_cus_name , txt_cus_number;
        ImageView img_catalogue;
        LinearLayout lin_accessories;
        SwitchCompat swt_notify_customer;

        public UtilityAccessoriesViewHolder(View itemView) {
            super(itemView);
            txt_cus_name = itemView.findViewById(R.id.txt_cus_name);
            txt_cus_number = itemView.findViewById(R.id.txt_cus_number);
            lin_accessories = itemView.findViewById(R.id.lin_accessories);
            swt_notify_customer = itemView.findViewById(R.id.swt_notify_customer);
        }

    }

    public interface OnItemsutilityaccessoriesClickListener{
        void onItemClick(UtilityaccessoriesList utilityaccessoriesList);
        void onItemUnchecked(int position, boolean isTrue, String screen);

    }
}
