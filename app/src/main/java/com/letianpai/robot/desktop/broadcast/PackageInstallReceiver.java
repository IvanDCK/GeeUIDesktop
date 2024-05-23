package com.letianpai.robot.desktop.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.letianpai.robot.components.storage.RobotSubConfigManager;
import com.letianpai.robot.desktop.callback.AppInstallSuccessCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;


public class PackageInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取安装的包名
//            String packageName = intent.getStringExtra(PackageInstaller.EXTRA_PACKAGE_NAME);
        String packageName = intent.getData().getSchemeSpecificPart();
        Log.e("letianpai_auto_install", "packageName: " + packageName);
        if (!TextUtils.isEmpty(packageName) && !RobotAppListManager.getInstance(context).isInThePackageList(packageName)){
            RobotSubConfigManager.getInstance(context).addUserPackage(packageName);
            RobotSubConfigManager.getInstance(context).commit();
            Log.e("letianpai_auto_install", "update ~~~~~~~~: " + packageName);
            RobotAppListManager.getInstance(context).updateAppMenuList();
            responseAppInstallSuccess(context,packageName);
        }

    }


    public static void launchApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            // 如果找不到对应的应用程序
            // 可以进行一些错误处理操作，或者显示提示信息给用户
        }
    }

    public static String getAppNameFromPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        String appName = "";

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            appName = (String) packageManager.getApplicationLabel(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    private void responseAppInstallSuccess(Context context, String packageName) {

        String displayName = getLocalAppName(context, packageName);
        AppInstallSuccessCallback.getInstance().responseAppInstallSuccess(packageName, displayName);


    }

    private String getLocalAppName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        String localAppName = "";

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            CharSequence appName = packageManager.getApplicationLabel(appInfo);
            if (appName != null) {
                localAppName = appName.toString();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return localAppName;

    }
}