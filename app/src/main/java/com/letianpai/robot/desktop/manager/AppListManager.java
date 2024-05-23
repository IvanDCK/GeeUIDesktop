package com.letianpai.robot.desktop.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.letianpai.robot.desktop.utils.PackageConsts;

import java.util.ArrayList;
import java.util.List;

public class AppListManager {
    private ArrayList<String> packageList = new ArrayList<>();

    public static void getInstalledAppsList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);

        for (ApplicationInfo appInfo : installedApps) {
            // 过滤掉系统应用
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = appInfo.loadLabel(packageManager).toString();
                String packageName = appInfo.packageName;
                Log.d("InstalledApp", "App Name: " + appName + ", Package Name: " + packageName);
            }
        }
    }

    public static List<String> getInstalledAppsList2(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);
        List<String> mList = new ArrayList<>();
        for (ApplicationInfo appInfo : installedApps) {
            String appName = appInfo.loadLabel(packageManager).toString();
            String packageName = appInfo.packageName;
            Log.d("InstalledApp", "App Name: " + appName + ", Package Name: " + packageName);
            mList.add(packageName);
        }
        return mList;
    }

    private static final int PERMISSION_REQUEST_CODE = 123;

    private void checkPermissionsAndFetchAppsList(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.QUERY_ALL_PACKAGES)
                == PackageManager.PERMISSION_GRANTED) {
            // 已经获得权限，可以获取应用列表
            getInstalledAppsList(context);
        } else {
            // 未获得权限，请求用户授权
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.QUERY_ALL_PACKAGES},
                    PERMISSION_REQUEST_CODE);
        }
    }






}
