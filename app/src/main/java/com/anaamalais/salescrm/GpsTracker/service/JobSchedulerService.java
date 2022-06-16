package com.anaamalais.salescrm.GpsTracker.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.anaamalais.salescrm.GpsTracker.Util;

//import com.example.activityrecognition.service.ForegroundService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    private static final String TAG = "SyncService";
    SchedulerCallback schedulerCallback;

    @Override
    public boolean onStartJob(JobParameters params) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent service = new Intent(getApplicationContext(),
                    ForegroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(service);
            } else {
                getApplicationContext().startService(service);
            }
            Util.scheduleJob(getApplicationContext()); // reschedule the job
        }
        Log.d("wewe", "runioodnslkdnlksndlksndlkskndlsnd");
//new RNBackgroundGeolocationModule().invokeServiceByForground();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public interface SchedulerCallback {
        // void onSchedulerCalled();
    }

}
