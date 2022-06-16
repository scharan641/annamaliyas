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

import com.anaamalais.salescrm.List.ExterioraccessoriesList;
import com.anaamalais.salescrm.List.InterioraccessoriesList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.anaamalais.salescrm.Utils.OnItemsExterioraccessoriesClickListener;

import java.util.Arrays;
import java.util.List;

public class ExteriorAccessoriesAdapter extends RecyclerView.Adapter<ExteriorAccessoriesAdapter.ExteriorAccessoriesViewHolder> {


    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<ExterioraccessoriesList> exterioraccessoriesLists;

    private OnItemsExterioraccessoriesClickListener listener;

    //getting the context and product list with constructor
    public ExteriorAccessoriesAdapter(Context mCtx, List<ExterioraccessoriesList> exterioraccessoriesLists) {
        this.mCtx = mCtx;
        this.exterioraccessoriesLists = exterioraccessoriesLists;
    }

    public void setWhenClickListener(OnItemsExterioraccessoriesClickListener listener){
        this.listener = listener;
    }

    @Override
    public ExteriorAccessoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_interioraccessories, null);

        return new ExteriorAccessoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final ExteriorAccessoriesViewHolder holder, int position){
        //getting the product of the specified position
        final ExterioraccessoriesList mode = exterioraccessoriesLists.get(position);
        holder.txt_cus_name.setText(mode.getExterior_acessory_name());
        holder.txt_cus_number.setText(mode.getExterior_acessory_price());


        holder.lin_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    holder.swt_notify_customer.setChecked(true);
                    listener.onItemClick(mode);
                    String [] codes = new String[exterioraccessoriesLists.size()];
                    for (int i = 0 ; i < exterioraccessoriesLists.size() ; i++){
                        codes[i] = exterioraccessoriesLists.get(i).getExterior_acessory_id();
                        MyFunctions.setSharedPrefs(mCtx, Constants.ACCESSORIESLIST, Arrays.toString(codes));
                        System.out.println("sahhh"+ Arrays.toString(codes));
                    }
                }else {
                    holder.swt_notify_customer.setChecked(false);
                }
            }
        });

    }

    @Override
    public int getItemCount () {
        return exterioraccessoriesLists.size();
    }


    class ExteriorAccessoriesViewHolder extends RecyclerView.ViewHolder {
        TextView txt_cus_name , txt_cus_number;
        ImageView img_catalogue;
        LinearLayout lin_accessories;
        SwitchCompat swt_notify_customer;

        public ExteriorAccessoriesViewHolder(View itemView) {
            super(itemView);
            txt_cus_name = itemView.findViewById(R.id.txt_cus_name);
            txt_cus_number = itemView.findViewById(R.id.txt_cus_number);
            lin_accessories = itemView.findViewById(R.id.lin_accessories);
            swt_notify_customer = itemView.findViewById(R.id.swt_notify_customer);
        }

    }

    public interface OnItemsExterioraccessoriesClickListener{
        void onItemClick(ExterioraccessoriesList exterioraccessoriesList);
    }
}
