package com.letianpai.robot.desktop.callback


/**
 * Application status update callbacks
 */
class AppStatusUpdateCallback private constructor() {
    private val mAppStatusUpdateListener = ArrayList<AppStatusUpdateListener?>()


    private object AppStatusUpdateCallbackHolder {
        val instance: AppStatusUpdateCallback = AppStatusUpdateCallback()
    }

    interface AppStatusUpdateListener {
        fun appInstallSuccess(packageName: String?, status: Int)
        fun appUninstall(packageName: String, status: Int)
    }

    fun addAppStatusUpdateListener(listener: AppStatusUpdateListener?) {
        mAppStatusUpdateListener.add(listener)
    }

    fun removeAppStatusUpdateListener(listener: AppStatusUpdateListener?) {
        if (mAppStatusUpdateListener.size > 1) {
            mAppStatusUpdateListener.remove(listener)
        }
    }

    fun installApkSuccess(packageName: String?, status: Int) {
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appInstallSuccess(packageName, status)
            }
        }
    }

    fun uninstallApk(packageName: String, status: Int) {
        for (i in mAppStatusUpdateListener.indices) {
            if (mAppStatusUpdateListener[i] != null) {
                mAppStatusUpdateListener[i]!!.appUninstall(packageName, status)
            }
        }
    }


    companion object {
        val instance: AppStatusUpdateCallback
            get() = AppStatusUpdateCallbackHolder.instance
    }
}
