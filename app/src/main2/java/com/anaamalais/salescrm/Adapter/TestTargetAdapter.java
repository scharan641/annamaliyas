package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class TestTargetAdapter extends RecyclerView.Adapter<TestTargetAdapter.TestTargetViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<Model> modes;

    String followupdate , followuptime;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus , progressBarStatusretail = 0;

    //getting the context and product list with constructor
    public TestTargetAdapter(Context mCtx, List<Model> modes) {
        this.mCtx = mCtx;
        this.modes = modes;
    }

    @Override
    public TestTargetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_target_model, null);

        return new TestTargetViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final TestTargetViewHolder holder, int position){
        //getting the product of the specified position
        final Model mode = modes.get(position);
        holder.txt_mode_name.setText(mode.getMode_name());
        holder.txt_cus_total.setText(mode.getTotal_target());
        holder.txt_cus_achive.setText(mode.getAchieved_target()+"\t"+"Completed");
        Glide.with(mCtx).load(mode.getVehicle_image()).into(holder.img_car_icon);
        try{
            int achieved_target = Integer.parseInt(mode.getAchieved_target());
            int total_target = Integer.parseInt(mode.getTotal_target());
            progressBarStatusretail = (achieved_target * 100/ total_target);
            holder.progressBar.setProgress(progressBarStatusretail);

        }
        catch (ArithmeticException e) {
            System.out.println ("Can't be divided by Zero " + e);
        }


    }

    @Override
    public int getItemCount () {
        return modes.size();
    }


    class TestTargetViewHolder extends RecyclerView.ViewHolder {

        TextView txt_mode_name, txt_cus_total, txt_cus_achive;
        ImageView img_car_icon;
        ProgressBar progressBar;

        public TestTargetViewHolder(View itemView) {
            super(itemView);
            txt_mode_name = itemView.findViewById(R.id.txt_model_name);
            txt_cus_total = itemView.findViewById(R.id.txt_toatl);
            txt_cus_achive = itemView.findViewById(R.id.txt_complete);
            img_car_icon  = itemView.findViewById(R.id.img_car_icon);
            progressBar  = itemView.findViewById(R.id.progressBar);
        }
    }
}
