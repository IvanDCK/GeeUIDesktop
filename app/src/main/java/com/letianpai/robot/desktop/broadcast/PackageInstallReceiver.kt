package com.letianpai.robot.desktop.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.letianpai.robot.components.storage.RobotSubConfigManager
import com.letianpai.robot.desktop.callback.AppInstallSuccessCallback
import com.letianpai.robot.desktop.manager.RobotAppListManager

class PackageInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Get the name of the installed package
//            String packageName = intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
        val packageName = intent.data!!.schemeSpecificPart
        Log.e("letianpai_auto_install", "packageName: $packageName")
        if (!TextUtils.isEmpty(packageName) && !RobotAppListManager.getInstance(context)
                .isInThePackageList(packageName)
        ) {
            RobotSubConfigManager.getInstance(context).addUserPackage(packageName)
            RobotSubConfigManager.getInstance(context).commit()
            Log.e("letianpai_auto_install", "update ~~~~~~~~: $packageName")
            RobotAppListManager.getInstance(context).updateAppMenuList()
            responseAppInstallSuccess(context, packageName)
        }
    }


    private fun responseAppInstallSuccess(context: Context, packageName: String) {
        val displayName = getLocalAppName(context, packageName)
        AppInstallSuccessCallback.instance
            .responseAppInstallSuccess(packageName, displayName)
    }

    private fun getLocalAppName(context: Context, packageName: String): String {
        val packageManager = context.packageManager
        var localAppName = ""

        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val appName = packageManager.getApplicationLabel(appInfo)
            if (appName != null) {
                localAppName = appName.toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        return localAppName
    }

    companion object {
        fun launchApp(context: Context, packageName: String) {
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(packageName)

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // If the corresponding application is not found
                // You can do some error handling or display a message to the user.
            }
        }

        fun getAppNameFromPackageName(context: Context, packageName: String): String {
            val packageManager = context.packageManager
            var appName = ""

            try {
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                appName = packageManager.getApplicationLabel(appInfo) as String
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return appName
        }
    }
}