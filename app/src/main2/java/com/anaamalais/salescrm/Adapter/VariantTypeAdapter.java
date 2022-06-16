package com.anaamalais.salescrm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.CatelogeList;
import com.anaamalais.salescrm.List.VariantsAccessList;
import com.anaamalais.salescrm.ModelDetailsActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.OnItemsClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class VariantTypeAdapter extends RecyclerView.Adapter<VariantTypeAdapter.VariantTypeViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<VariantsAccessList> variantsAccessLists;

    private OnItemsClickListener listener;

    public static int lastClickedPosition = -1;
    //getting the context and product list with constructor
    public VariantTypeAdapter(Context mCtx, List<VariantsAccessList> variantsAccessLists) {
        this.mCtx = mCtx;
        this.variantsAccessLists = variantsAccessLists;
    }

    public void setWhenClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }

    @Override
    public VariantTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_variant, null);

        return new VariantTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final VariantTypeViewHolder holder, int position){
        //getting the product of the specified position
        final VariantsAccessList mode = variantsAccessLists.get(position);
        holder.txt_variant_name.setText(mode.getVariant());
        holder.txt_variant_fuel_type.setText(mode.getVariant());

        if (lastClickedPosition==position){
            holder.lin_bottom_view.setBackgroundResource(R.drawable.btn_line);
        }else {
            holder.lin_bottom_view.setBackgroundResource(R.drawable.shape_shadow_search);
        }

        holder.lin_bottom_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    lastClickedPosition = position;
                    notifyDataSetChanged();
                    listener.onItemClick(mode);
                }
            }
        });

    }

    @Override
    public int getItemCount () {
        return variantsAccessLists.size();
    }


    class VariantTypeViewHolder extends RecyclerView.ViewHolder {
        TextView txt_variant_name , txt_variant_fuel_type;
        LinearLayout lin_bottom_view;


        public VariantTypeViewHolder(View itemView) {
            super(itemView);
            txt_variant_name = itemView.findViewById(R.id.txt_variant_name);
            txt_variant_fuel_type = itemView.findViewById(R.id.txt_variant_fuel_type);
            lin_bottom_view = itemView.findViewById(R.id.lin_bottom_view);

        }

    }

    public interface OnItemsClickListener{
        void onItemClick(VariantsAccessList variantsAccessList);
    }
}
