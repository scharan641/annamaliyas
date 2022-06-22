package com.anaamalais.salescrm.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.anaamalais.salescrm.List.ExterioraccessoriesList;
import com.anaamalais.salescrm.List.InterioraccessoriesList;
import com.anaamalais.salescrm.List.UtilityaccessoriesList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PreferenceManager {
    Context context;
    @SuppressLint("StaticFieldLeak")
    private static PreferenceManager instance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        this.context = context;
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context);
            sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return instance;
    }

    public void cleanAllData() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setModelId(String modelId) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("setmodel_id", modelId).apply();
    }

    public String getModelId() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("setmodel_id", "");
    }

    public void setContactId(String contactId) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("contactId", contactId).apply();
    }

    public String getContactId() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("contactId", "");
    }

    public void setTestdriveid(String testdriveid) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("testdriveid", testdriveid).apply();
    }

    public String getTestdriveid() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("testdriveid", "");
    }

    public void setLicencedriveimage(String testdriveid) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("licencedriveimage", testdriveid).apply();
    }

    public String getLicencedriveimage() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("licencedriveimage", "");
    }

    public void setLicencedriveimageurl(String testdriveid) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("licencedriveimageurl", testdriveid).apply();
    }

    public String getLicencedriveimageurl() {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        return sharedPreferences.getString("licencedriveimageurl", "");
    }

    public void setIsFirstTime(boolean isFirstTime) {
        sharedPreferences = context.getSharedPreferences("annamalaya", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", isFirstTime).apply();
    }

    public void cleanAccessoriesData() {
        sharedPreferences = context.getSharedPreferences("ACCESSORIES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    public <T> void setModelList(String key, List<T> list) {
        sharedPreferences = context.getSharedPreferences("ACCESSORIES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public static void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();

    }

    public List<InterioraccessoriesList> getInteriorList() {
        sharedPreferences = context.getSharedPreferences("ACCESSORIES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        List<InterioraccessoriesList> arrayItems = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(Constants.INTERIOR, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<InterioraccessoriesList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public List<ExterioraccessoriesList> getExteriorList() {
        sharedPreferences = context.getSharedPreferences("ACCESSORIES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        List<ExterioraccessoriesList> arrayItems = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(Constants.EXTEREIOR, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ExterioraccessoriesList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    public List<UtilityaccessoriesList> getUtilityList() {
        sharedPreferences = context.getSharedPreferences("ACCESSORIES", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        List<UtilityaccessoriesList> arrayItems = new ArrayList<>();
        String serializedObject = sharedPreferences.getString(Constants.UTILITY, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UtilityaccessoriesList>>() {
            }.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

   /* public void set(SignUpModel signUpModel){
        Gson gson = new Gson();
        String json = gson.toJson(signUpModel);
        editor.putString("userData", json);
        editor.commit();
    }
    public SignUpModel getUserModel(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("userData", "");
        SignUpModel obj = gson.fromJson(json, SignUpModel.class);
        return obj;
    }*/

}
