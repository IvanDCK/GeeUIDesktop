package com.letianpai.robot.desktop.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.letianpai.robot.components.storage.RobotSubConfigManager
import com.letianpai.robot.desktop.callback.AppStatusUpdateCallback
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.utils.AppStoreConsts

class PackageUninstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Get the name of the uninstalled package
        val packageName = intent.data!!.schemeSpecificPart
        Log.e(
            "letianpai_appstore",
            "2_PackageUninstallReceiver_packageName: $packageName"
        )
        AppStatusUpdateCallback.instance
            .uninstallApk(packageName, AppStoreConsts.APP_STORE_STATUS_UNINSTALL)
        RobotSubConfigManager.getInstance(context)!!.removeUserPackage(packageName)
        RobotSubConfigManager.getInstance(context)!!.commit()
        RobotAppListManager.getInstance(context).updateAppMenuList()
        //        Log.e("letianpai_appstore","2_PackageUninstallReceiver_packageName: "+ packageName);
//            if (PACKAGE_NAME.equals(packageName)) {
//                // Handling APK uninstall events
                  // Here you can perform the appropriate actions
//            }
    }
}
