package com.letianpai.robot.desktop.callback

/**
 * Application status update callbacks
 */
class AppInstallSuccessCallback private constructor() {
    private var mAppStatusUpdateListener: AppStoreInstallResultListener? = null

    private object AppStatusUpdateCallbackHolder {
        val instance: AppInstallSuccessCallback = AppInstallSuccessCallback()
    }

    fun interface AppStoreInstallResultListener {
        fun appInstallSuccess(packageName: String?, appName: String?)
    }

    fun setAppStatusUpdateListener(listener: AppStoreInstallResultListener?) {
        this.mAppStatusUpdateListener = listener
    }

    fun responseAppInstallSuccess(packageName: String?, appName: String?) {
        if (mAppStatusUpdateListener != null) {
            mAppStatusUpdateListener!!.appInstallSuccess(packageName, appName)
        }
    }

    companion object {
        val instance: AppInstallSuccessCallback
            get() = AppStatusUpdateCallbackHolder.instance
    }
}
