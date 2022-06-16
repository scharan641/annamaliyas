package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.ColorDataList;
import com.anaamalais.salescrm.List.VariantsAccessList;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.OnItemsColorClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class VariantColorTypeAdapter extends RecyclerView.Adapter<VariantColorTypeAdapter.VariantColorTypeViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<ColorDataList> colorDataLists;

    public static int lastClickedPosition = -1;

    private OnItemsColorClickListener listener;

    //getting the context and product list with constructor
    public VariantColorTypeAdapter(Context mCtx, List<ColorDataList> colorDataLists) {
        this.mCtx = mCtx;
        this.colorDataLists = colorDataLists;
    }

    public void setWhenClickListener(OnItemsColorClickListener listener){
        this.listener = listener;
    }

    @Override
    public VariantColorTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_variant_color, null);

        return new VariantColorTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final VariantColorTypeViewHolder holder, int position){
        //getting the product of the specified position
        final ColorDataList mode = colorDataLists.get(position);
        holder.txt_catalogue_name.setText(mode.getColor());
        Glide.with(mCtx).load(mode.getImage()).into(holder.img_catalogue);

       if (lastClickedPosition==position){
           holder.lin_catalogue_view.setBackgroundResource(R.drawable.btn_line);
       }else {
           holder.lin_catalogue_view.setBackgroundResource(R.drawable.shape_shadow_search);
       }
        holder.lin_catalogue_view.setOnClickListener(new View.OnClickListener() {
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
        return colorDataLists.size();
    }


    class VariantColorTypeViewHolder extends RecyclerView.ViewHolder {
        TextView txt_catalogue_name , txt_variant_fuel_type;
        ImageView img_catalogue;
        LinearLayout lin_catalogue_view;


        public VariantColorTypeViewHolder(View itemView) {
            super(itemView);
            txt_catalogue_name = itemView.findViewById(R.id.txt_catalogue_name);
            img_catalogue = itemView.findViewById(R.id.img_catalogue);
            lin_catalogue_view = itemView.findViewById(R.id.lin_catalogue_view);

        }

    }

    public interface OnItemsColorClickListener{
        void onItemClick(ColorDataList colorDataList);
    }
}
