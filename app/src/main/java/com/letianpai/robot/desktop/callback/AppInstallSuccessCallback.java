package com.letianpai.robot.desktop.callback;

/**
 * 应用状态更新回调
 */

public class AppInstallSuccessCallback {

    private AppStoreInstallResultListener mAppStatusUpdateListener;

    private static class AppStatusUpdateCallbackHolder {
        private static AppInstallSuccessCallback instance = new AppInstallSuccessCallback();
    }

    private AppInstallSuccessCallback() {

    }

    public static AppInstallSuccessCallback getInstance() {
        return AppStatusUpdateCallbackHolder.instance;
    }

    public interface AppStoreInstallResultListener {
        void appInstallSuccess(String packageName,String appName);

    }

    public void setAppStatusUpdateListener(AppStoreInstallResultListener listener) {
        this.mAppStatusUpdateListener = listener;
    }

    public void responseAppInstallSuccess(String packageName,String appName) {
        if (mAppStatusUpdateListener != null) {
            mAppStatusUpdateListener.appInstallSuccess(packageName,appName);
        }
    }

}
