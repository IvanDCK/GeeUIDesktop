package com.letianpai.robot.desktop.manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AppListManager {
    private val packageList = ArrayList<String>()

    private fun checkPermissionsAndFetchAppsList(context: Activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.QUERY_ALL_PACKAGES)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission has been granted to access the app list
            getInstalledAppsList(context)
        } else {
            // Permission not granted, user authorisation requested
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.QUERY_ALL_PACKAGES),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    companion object {
        fun getInstalledAppsList(context: Context) {
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(0)

            for (appInfo in installedApps) {
                // Filter out system applications
                if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    val appName = appInfo.loadLabel(packageManager).toString()
                    val packageName = appInfo.packageName
                    Log.d(
                        "InstalledApp",
                        "App Name: $appName, Package Name: $packageName"
                    )
                }
            }
        }

        fun getInstalledAppsList2(context: Context): List<String> {
            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(0)
            val mList: MutableList<String> = ArrayList()
            for (appInfo in installedApps) {
                val appName = appInfo.loadLabel(packageManager).toString()
                val packageName = appInfo.packageName
                Log.d("InstalledApp", "App Name: $appName, Package Name: $packageName")
                mList.add(packageName)
            }
            return mList
        }

        private const val PERMISSION_REQUEST_CODE = 123
    }
}
