package com.anaamalais.salescrm.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anaamalais.salescrm.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFunctions {
    static  String app_name="SalesCrm";
    public static Boolean getSharedPrefs(Context c, String key,
                                         Boolean default_value) {
        if (c == null) {
            return default_value;
        } else {
            SharedPreferences prefs = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE);
            return prefs.getBoolean(key, default_value);
        }
    }
    public static String getSharedPrefs(Context c, String key,
                                        String default_value) {
        if (c == null) {
            return default_value;
        } else {
            SharedPreferences prefs = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE);
            return prefs.getString(key, default_value);
        }
    }

    public static void setSharedPrefs(Context c, String key, int value) {
        if (c != null) {
            SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE).edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isEditTextEmpty(EditText et, String startName) {
        boolean isEmpty = et.getText().toString().trim().equals("");
        if (isEmpty) {
            et.requestFocus();
            et.setError(startName + " cannot be empty");
        } else {
            et.setError(null);
        }
        return isEmpty;
    }
    public static void toastShort(Context c, String msg) {
        if (c != null && msg != null)
            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void addWithoutBackstackFragment(Fragment c, Fragment fragment) {
        FragmentManager fragmentManager = c.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.edit_customer_id, fragment);
        transaction.commit();
    }

    public static void toastLong(Context c, String msg) {
        if (c != null && msg != null)
            Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }

    public static void sop(String msg) {
        System.out.println(msg);
    }

    public static void Log(String msg) {
        Log.d("Hooked", msg);
    }


    public static String convertImageToString(Bitmap bm) {
        //MyFunctions.sop("original size is "+byteSizeOf(bm));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()),
                null, options);
        byte[] photo = baos.toByteArray();

//        MyFunctions.sop("Original dimensions" + bm.getWidth() + " " + bm.getHeight());
//        MyFunctions.sop("Compressed dimensions" + decoded.getWidth() + " " + decoded.getHeight());

        return Base64.encodeToString(photo, Base64.DEFAULT);

    }

    public static String  getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    public static void setSharedPrefs(Context c, String key, Boolean value) {
        if (c != null) {
            SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE).edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static void setSharedPrefs(Context c, String key, String value) {
        if (c != null) {
            SharedPreferences.Editor editor = c.getSharedPreferences(app_name,
                    Context.MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.commit();
        }
    }
}
