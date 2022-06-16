package com.anaamalais.salescrm.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.anaamalais.salescrm.R;

public class MyFocusChangeListener implements View.OnFocusChangeListener {

    public void onFocusChange(View v, boolean hasFocus){

        if(v.getId() == R.id.edt_address_line_three && !hasFocus) {

           // InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        }
    }
}
