package com.letianpai.robot.desktop.callback

import com.letianpai.robot.desktop.parser.AppMenuInfo

/**
 * Created by liujunbin
 */
class AppMenuUpdateCallback private constructor() {
    private val mAppMenuUpdateListener: MutableList<AppMenuUpdateListener?> = ArrayList()

    private object AppMenuCallbackHolder {
        val instance: AppMenuUpdateCallback = AppMenuUpdateCallback()
    }

    interface AppMenuUpdateListener {
        fun onAppMenuUpdated(appMenuInfo: ArrayList<AppMenuInfo?>?)
        fun onAppStatusUpdated(packageName: String?, status: Int)
    }

    fun registerAppMenuUpdateListener(listener: AppMenuUpdateListener?) {
        if (mAppMenuUpdateListener != null && !mAppMenuUpdateListener.contains(listener)) {
            mAppMenuUpdateListener.add(listener)
        }
    }

    fun unregisterAppMenuUpdateListener(listener: AppMenuUpdateListener?) {
        if (mAppMenuUpdateListener != null) {
            mAppMenuUpdateListener.remove(listener)
        }
    }

    //TODO Change to Object Passing
    fun setAppMenuUpdate(appMenuInfo: ArrayList<AppMenuInfo?>?) {
        for (i in mAppMenuUpdateListener.indices) {
            if (mAppMenuUpdateListener[i] != null) {
                mAppMenuUpdateListener[i]!!.onAppMenuUpdated(appMenuInfo)
            }
        }
    }

    //TODO Change to Object Passing
    fun setAppStatusUpdate(packageName: String?, status: Int) {
        for (i in mAppMenuUpdateListener.indices) {
            if (mAppMenuUpdateListener[i] != null) {
                mAppMenuUpdateListener[i]!!.onAppStatusUpdated(packageName, status)
            }
        }
    }

    companion object {
        val instance: AppMenuUpdateCallback
            get() = AppMenuCallbackHolder.instance
    }
}
