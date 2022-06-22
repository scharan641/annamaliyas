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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.CatelogeList;
import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.ModelDetailsActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.anaamalais.salescrm.Utils.PreferenceManager;
import com.anaamalais.salescrm.common.CommonData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<CatelogeList> catelogeLists;

    String followupdate , followuptime;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus , progressBarStatusretail = 0;
    PreferenceManager preferenceManager;
    private ProgressBar progressBar;

    //getting the context and product list with constructor
    public CatalogueAdapter(Context mCtx, List<CatelogeList> catelogeLists) {
        this.mCtx = mCtx;
        this.catelogeLists = catelogeLists;
    }

    @Override
    public CatalogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_catalogue, null);

        return new CatalogueViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final CatalogueViewHolder holder, int position){
        //getting the product of the specified position
        preferenceManager = new PreferenceManager(mCtx);
        final CatelogeList mode = catelogeLists.get(position);
        holder.txt_catalogue_name.setText(mode.getModel());
        Glide.with(mCtx).load(mode.getVehicle_image()).into(holder.img_car_icon);
        holder.lin_catalogue_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.interioraccessoriesLists.clear();
                CommonData.exterioraccessoriesLists.clear();
                CommonData.utilityaccessoriesLists.clear();
                MyFunctions.setSharedPrefs(mCtx,Constants.PRICEINT,"");
                MyFunctions.setSharedPrefs(mCtx,Constants.PRICEEXT,"");
                MyFunctions.setSharedPrefs(mCtx,Constants.PRICEUTL,"");
                VariantColorTypeAdapter.lastClickedPosition = -1;
                VariantTypeAdapter.lastClickedPosition = -1;
                preferenceManager.cleanAccessoriesData();
            /*    preferenceManager.setModelList(Constants.EXTEREIOR,new ArrayList<>());
                preferenceManager.setModelList(Constants.INTERIOR,new ArrayList<>());
                preferenceManager.setModelList(Constants.UTILITY,new ArrayList<>());
*/
                MyFunctions.setSharedPrefs(mCtx, Constants.MODEL_ID,mode.getId());
                preferenceManager.setModelId(mode.getId());
                MyFunctions.setSharedPrefs(mCtx, Constants.MODELIMAGE,mode.getVehicle_image());
                mCtx.startActivity(new Intent(mCtx, ModelDetailsActivity.class).putExtra("ID",mode.getId()).putExtra("MODEL_IMAGE",mode.getVehicle_image()));
                ((Activity)mCtx).finish();

            }
        });

    }

    @Override
    public int getItemCount () {
        return catelogeLists.size();
    }


    class CatalogueViewHolder extends RecyclerView.ViewHolder {
        TextView txt_catalogue_name;
        ImageView img_car_icon;
        LinearLayout lin_catalogue_view;

        public CatalogueViewHolder(View itemView) {
            super(itemView);
            img_car_icon  = itemView.findViewById(R.id.img_catalogue);
            txt_catalogue_name = itemView.findViewById(R.id.txt_catalogue_name);
            lin_catalogue_view = itemView.findViewById(R.id.lin_catalogue_view);
        }
    }
}
