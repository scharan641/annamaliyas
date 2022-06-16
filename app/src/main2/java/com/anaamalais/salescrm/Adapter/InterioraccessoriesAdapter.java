package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.InterioraccessoriesList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class InterioraccessoriesAdapter extends RecyclerView.Adapter<InterioraccessoriesAdapter.InterioraccessoriesViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<InterioraccessoriesList> interioraccessoriesLists;

    private OnItemsInterioraccessoriesClickListener listener;

    //getting the context and product list with constructor
    public InterioraccessoriesAdapter(Context mCtx, List<InterioraccessoriesList> interioraccessoriesLists) {
        this.mCtx = mCtx;
        this.interioraccessoriesLists = interioraccessoriesLists;
    }

    public void setWhenClickListener(OnItemsInterioraccessoriesClickListener listener ){
        this.listener = listener;
    }

    @Override
    public InterioraccessoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_interioraccessories, null);

        return new InterioraccessoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final InterioraccessoriesViewHolder holder, int position){
        //getting the product of the specified position
        final InterioraccessoriesList mode = interioraccessoriesLists.get(position);
        holder.txt_cus_name.setText(mode.getInterior_accessory_name());
        holder.txt_cus_number.setText(mode.getInterior_accessory_price());

        holder.lin_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    holder.swt_notify_customer.setChecked(true);
                    listener.onItemClick(mode);
                    String [] codes = new String[interioraccessoriesLists.size()];
                    codes[position] = interioraccessoriesLists.get(position).getInterior_accessory_id();
                    MyFunctions.setSharedPrefs(mCtx, Constants.ACCESSORIESLIST,Arrays.toString(codes));
                    System.out.println("sahhh"+ Arrays.toString(codes));

                }else {
                    holder.swt_notify_customer.setChecked(false);
                }
            }
        });

    }

    @Override
    public int getItemCount () {
        return interioraccessoriesLists.size();
    }


    class InterioraccessoriesViewHolder extends RecyclerView.ViewHolder {
        TextView txt_cus_name , txt_cus_number;
        ImageView img_catalogue;
        SwitchCompat swt_notify_customer;
        LinearLayout lin_accessories;


        public InterioraccessoriesViewHolder(View itemView) {
            super(itemView);
            txt_cus_name = itemView.findViewById(R.id.txt_cus_name);
            txt_cus_number = itemView.findViewById(R.id.txt_cus_number);
            lin_accessories = itemView.findViewById(R.id.lin_accessories);
            swt_notify_customer = itemView.findViewById(R.id.swt_notify_customer);

        }

    }

    public interface OnItemsInterioraccessoriesClickListener{
        void onItemClick(InterioraccessoriesList interioraccessoriesList);
    }
}
