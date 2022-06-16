package com.anaamalais.salescrm.Adapter;

import static com.anaamalais.salescrm.Utils.Constants.BASE_URL;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.anaamalais.salescrm.FileDownloader;
import com.anaamalais.salescrm.List.ColorDataList;
import com.anaamalais.salescrm.List.CustomerList;
import com.anaamalais.salescrm.List.Model;
import com.anaamalais.salescrm.LoginActivity;
import com.anaamalais.salescrm.ModelDetailsActivity;
import com.anaamalais.salescrm.R;
import com.anaamalais.salescrm.Utils.Constants;
import com.anaamalais.salescrm.Utils.MyFunctions;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GenerateQuotationforAdapter extends RecyclerView.Adapter<GenerateQuotationforAdapter.GenerateQuotationforViewHolder> {

    //this context we will use to inflate the activity_sign_in
    private Context mCtx;
    //we are storing all the products in a list
    private List<CustomerList> modes;

    String followupdate, followuptime;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus, progressBarStatusretail = 0;
    String accessoriesmaster_generatequotation_url = BASE_URL + "accessoriesmaster/generatequotation";
    RequestQueue requestQueue;
    String status_code, msg, token, API_TOKEN, quotation_link, VARIANT_ID, MODEL_ID, COLOR_ID, CONTACT_ID;
    String accessories;
    private String fileName = "salesFile", middleFolderName = "SalesCRM";
    private ProgressDialog progressDialog;

    //getting the context and product list with constructor
    public GenerateQuotationforAdapter(Context mCtx, List<CustomerList> modes) {
        this.mCtx = mCtx;
        this.modes = modes;
    }

    @Override
    public GenerateQuotationforViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.adapter_generatequotationfor, null);
        requestQueue = Volley.newRequestQueue(mCtx);
        API_TOKEN = MyFunctions.getSharedPrefs(mCtx, Constants.TOKEN, "");
        return new GenerateQuotationforViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GenerateQuotationforViewHolder holder, int position) {
        //getting the product of the specified position
        final CustomerList mode = modes.get(position);
        holder.txt_cus_name.setText(mode.getName());
        holder.txt_cus_number.setText(mode.getMobile_number());

        holder.lint_customer_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONTACT_ID = mode.getId();
                MODEL_ID = MyFunctions.getSharedPrefs(mCtx, Constants.MODEL_ID, "");
                COLOR_ID = MyFunctions.getSharedPrefs(mCtx, Constants.COLORID, "");
                VARIANT_ID = MyFunctions.getSharedPrefs(mCtx, Constants.VARIANTID, "");
                if (MyFunctions.getSharedPrefs(mCtx, Constants.ACCESSORIESLIST, "") != null) {
                    accessories = MyFunctions.getSharedPrefs(mCtx, Constants.ACCESSORIESLIST, "");
                } else {
                    accessories = "";
                }
                Generate_quotation();
            }
        });


    }

    @Override
    public int getItemCount() {
        return modes.size();
    }


    class GenerateQuotationforViewHolder extends RecyclerView.ViewHolder {

        TextView txt_cus_name, txt_cus_number;
        LinearLayout lint_customer_id;

        public GenerateQuotationforViewHolder(View itemView) {
            super(itemView);
            txt_cus_name = itemView.findViewById(R.id.txt_cus_name);
            txt_cus_number = itemView.findViewById(R.id.txt_cus_number);
            lint_customer_id = itemView.findViewById(R.id.lint_customer_id);

        }


    }

    public void Generate_quotation() {
        StringRequest requests = new StringRequest(Request.Method.POST, accessoriesmaster_generatequotation_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        System.out.println("Json string is:" + jsonObj);
                        status_code = jsonObj.getString("status");
                        if (status_code.equals("1")) {
                            msg = (String) jsonObj.getString("msg");
                            quotation_link = (String) jsonObj.getString("quotation_link");
                            shareQuotation(quotation_link);

                        } else if (status_code.equals("0")) {
                            msg = (String) jsonObj.getString("msg");

                            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                    if (msg.equals("Invalid API_TOKEN!")) {
                                        MyFunctions.setSharedPrefs(mCtx, Constants.IS_LOGIN, false);
                                        MyFunctions.setSharedPrefs(mCtx, Constants.USER_ID, "");
                                        mCtx.startActivity(new Intent(mCtx, LoginActivity.class).putExtra("DEVICEID", MyFunctions.getSharedPrefs(mCtx, Constants.DEVICEID, "")));
                                        ((Activity) mCtx).finish();
                                    }
                                }
                            }, 3000); // the timer will count 5 seconds....

                        } else {
                            // userMessage = (String) jsonObj.get("userMessage");
                            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                            builder.setTitle("USER MESSAGE");
                            builder.setMessage(msg);
                            builder.setCancelable(true);
                            final AlertDialog closedialog = builder.create();
                            closedialog.show();

                            final Timer timer2 = new Timer();
                            timer2.schedule(new TimerTask() {
                                public void run() {
                                    closedialog.dismiss();
                                    timer2.cancel(); //this will cancel the timer of the system
                                }
                            }, 3000); // the timer will count 5 seconds....

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CONTACT_ID", CONTACT_ID);
                params.put("VARIANT_ID", VARIANT_ID);
                params.put("MODEL_ID", MODEL_ID);
                params.put("COLOR_ID", COLOR_ID);
                params.put("ACCESSORIES", accessories);
                System.out.println("PRINTF" + params);
                return params;
            }

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("API_TOKEN", API_TOKEN);

                return params;
            }

        };
        requestQueue.add(requests);
    }

    private void shareFile(){
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/"+middleFolderName+"/" + fileName);  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        System.out.println("Check statusMessage of Login Activity:" + msg);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(String.valueOf(path));
        shareIntent.putExtra(Intent.EXTRA_STREAM,path);
        mCtx.startActivity(shareIntent);
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        share.setAction(Intent.ACTION_SEND);
//        share.setType("application/pdf");
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        share.setPackage("com.whatsapp");
//
//        activity.startActivity(share);
        //without the below line intent will show error
//        shareIntent.setType("application/pdf");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, path);
//        // Target whatsapp:
//        shareIntent.setPackage("com.whatsapp");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT,
//                "Sharing File...");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

//        try {
//            mCtx.startActivity(shareIntent);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(mCtx,
//                    "Whatsapp have not been installed.",
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void shareQuotation(String quotation_link) {
        {
            fileName = quotation_link.substring(quotation_link.lastIndexOf('/')+1);
            new DownloadFile().execute(quotation_link, fileName);
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, middleFolderName);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mCtx);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            ObjectAnimator anim = ObjectAnimator.ofInt(progressDialog, "progress", 0, 100);
            anim.setDuration(15000);
            anim.setInterpolator(new DecelerateInterpolator());
            progressDialog.show();


            Toast.makeText(mCtx, "Downloading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(mCtx, "Downloading finished", Toast.LENGTH_SHORT).show();
            shareFile();
        }
    }
}
