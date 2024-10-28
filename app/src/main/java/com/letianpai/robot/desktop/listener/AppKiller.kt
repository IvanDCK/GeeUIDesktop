package com.letianpai.robot.desktop.listener

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AppKiller {
    fun killApp(context: Context, packageName: String?, listener: OnAppKilledListener?) {
        CoroutineScope(Dispatchers.IO).launch {
            val startTime = System.currentTimeMillis()
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.killBackgroundProcesses(packageName)
            val executionTime = System.currentTimeMillis() - startTime
            Log.d(
                "letianpai_AppKiller",
                "Execution time: $executionTime milliseconds"
            )
            // Switch to the main thread to call the listener
            withContext(Dispatchers.Main) {
                listener?.onAppKilled(executionTime)
            }
        }
    }

    interface OnAppKilledListener {
        fun onAppKilled(executionTime: Long)
    }
}