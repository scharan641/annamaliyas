package com.anaamalais.salescrm.GpsTracker;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.anaamalais.salescrm.GpsTracker.service.JobSchedulerService;

public class Util {
    static ComponentName serviceComponent = null;
    static JobInfo.Builder builder = null;

    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (serviceComponent == null) {
                serviceComponent = new ComponentName(context, JobSchedulerService.class);
            }
            if (builder == null) {
                builder = new JobInfo.Builder(0, serviceComponent);
            }
            //builder.setMinimumLatency(1 * 1000); // wait at least
            builder.setOverrideDeadline(1 * 60 * 1000); // maximum delay
            builder.setPeriodic(6 * 1000);

            //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
            //builder.setRequiresDeviceIdle(true); // device should be idle
            //builder.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }
    }
}
