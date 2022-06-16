package com.anaamalais.salescrm.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anaamalais.salescrm.List.AnnualIncomesList;
import com.anaamalais.salescrm.List.FuelList;
import com.anaamalais.salescrm.R;

import java.util.List;

public class FuelTypeArrayAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    final Context mContext;
    private final List<FuelList> items;
    private final int mResource;
    TextView offTypeTv;

    public FuelTypeArrayAdapter(Context context, int resource,
                                        List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position){
        if(position == 0)
        {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        }
        else
        {
            return true;
        }
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.layout_fuel_type, parent, false);

        TextView offTypeTv = (TextView) view.findViewById(R.id.offer_fuel_txt);

        FuelList offerData = items.get(position);
        if(position == 0){
            // Set the hint text color gray
            offTypeTv.setTextColor(Color.GRAY);
            //  tv.setTextColor(Color.parseColor("#A7A7A7"));
            //  tv.setTextSize(12);

        }
        else {
            offTypeTv.setTextColor(Color.parseColor("#131313"));
            offTypeTv.setText(offerData.getFuel_type());

        }


        return view;
    }

}
