package com.letianpai.robot.desktop.listener;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AppKiller {

    public interface OnAppKilledListener {
        void onAppKilled(long executionTime);
    }

    public static void killApp(final Context context, final String packageName, final OnAppKilledListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                if (activityManager != null) {
                    activityManager.killBackgroundProcesses(packageName);
                }
                long executionTime = System.currentTimeMillis() - startTime;
                Log.d("letianpai_AppKiller", "Execution time: " + executionTime + " milliseconds");
                if (listener != null) {
                    listener.onAppKilled(executionTime);
                }
            }
        });
    }
}