package com.anaamalais.salescrm.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PreferenceManager {
    Context context;
    @SuppressLint("StaticFieldLeak")
    private static PreferenceManager instance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public PreferenceManager(Context context) {
        this.context =context;
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
            sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return instance;
    }

    public void cleanAllData(){
        sharedPreferences  = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setModelId(String modelId){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("setmodel_id",modelId).apply();
    }
    public String getModelId(){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("setmodel_id","");
    }

    public void setContactId(String contactId){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("contactId",contactId).apply();
    }
    public String getContactId(){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("contactId","");
    }

    public void setTestdriveid(String testdriveid){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("testdriveid",testdriveid).apply();
    }
    public String getTestdriveid(){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("testdriveid","");
    }

    public void setLicencedriveimage(String testdriveid){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("licencedriveimage",testdriveid).apply();
    }
    public String getLicencedriveimage(){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("licencedriveimage","");
    }

    public void setLicencedriveimageurl(String testdriveid){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("licencedriveimageurl",testdriveid).apply();
    }
    public String getLicencedriveimageurl(){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("licencedriveimageurl","");
    }

    public void setIsFirstTime(boolean isFirstTime){
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime",isFirstTime).apply();
    }
}
