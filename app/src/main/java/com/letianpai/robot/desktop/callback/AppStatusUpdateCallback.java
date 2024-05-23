package com.letianpai.robot.desktop.callback;


import java.util.ArrayList;

/**
 * 应用状态更新回调
 */

public class AppStatusUpdateCallback {

    private ArrayList<AppStatusUpdateListener> mAppStatusUpdateListener = new ArrayList<>();


    private static class AppStatusUpdateCallbackHolder {
        private static AppStatusUpdateCallback instance = new AppStatusUpdateCallback();
    }

    private AppStatusUpdateCallback() {

    }

    public static AppStatusUpdateCallback getInstance() {
        return AppStatusUpdateCallbackHolder.instance;
    }

    public interface AppStatusUpdateListener {
        void appInstallSuccess(String packageName, int status);
        void appUninstall(String packageName, int status);
    }

    public void addAppStatusUpdateListener(AppStatusUpdateListener listener) {
        this.mAppStatusUpdateListener.add(listener);
    }

    public void removeAppStatusUpdateListener(AppStatusUpdateListener listener) {
        if (mAppStatusUpdateListener.size() > 1){
            this.mAppStatusUpdateListener.remove(listener);
        }
    }

    public void installApkSuccess(String packageName, int status) {
        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appInstallSuccess(packageName, status);
            }
        }
    }
    public void uninstallApk(String packageName, int status) {
        for (int i = 0;i< mAppStatusUpdateListener.size();i++){
            if (mAppStatusUpdateListener.get(i) != null) {
                mAppStatusUpdateListener.get(i).appUninstall(packageName, status);
            }
        }
    }





}
