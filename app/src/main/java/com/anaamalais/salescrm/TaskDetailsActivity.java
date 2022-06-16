package com.anaamalais.salescrm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class TaskDetailsActivity extends AppCompatActivity {
    LinearLayout lin_date_time,lin_date_coment;
    String home;
    EditText txt_date , edt_time;
    int mYear, mDay ,mMonth,mMinute,mHour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        Window window = TaskDetailsActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(TaskDetailsActivity.this, R.color.details));
        lin_date_time = findViewById(R.id.lin_date_time);
        lin_date_coment = findViewById(R.id.lin_date_coment);
        home = getIntent().getStringExtra("home");
        txt_date = findViewById(R.id.txt_date);
        edt_time = findViewById(R.id.edt_time);

        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskDetailsActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edt_time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        txt_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (txt_date.getRight() - txt_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);


                        DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetailsActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {


                                        CharSequence strDate = null;
                                        android.text.format.Time chosenDate = new Time();
                                        chosenDate.set(dayOfMonth, monthOfYear, year);
                                        long dtDob = chosenDate.toMillis(true);

                                        strDate = DateFormat.format("dd/MM/yyyy",dtDob);
                                        txt_date.setText(strDate);
                                        //timesheetdate =  DateFormat.format("yyyy-MM-dd",dtDob).toString();

                                        // startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();

                        return true;
                    }
                }
                return false;
            }
        });

    }

    public void back(View view) {
        if (home.equals("HOME")){
            startActivity(new Intent(TaskDetailsActivity.this,HomeActivity.class));
            finish();
        }else if (home.equals("TASK")){
            startActivity(new Intent(TaskDetailsActivity.this,TaskListActivity.class));
            finish();
        }

    }

    public void followup(View view) {
        lin_date_time.setVisibility(View.VISIBLE);
        lin_date_coment.setVisibility(View.GONE);
    }


    public void lostlesd(View view) {
        lin_date_time.setVisibility(View.GONE);
        lin_date_coment.setVisibility(View.VISIBLE);
    }

    public void closed(View view) {
        lin_date_time.setVisibility(View.GONE);
        lin_date_coment.setVisibility(View.VISIBLE);
    }


    public void phone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.setData(Uri.parse("tel:"+"9923650099"));
        startActivity(intent);
    }


}
