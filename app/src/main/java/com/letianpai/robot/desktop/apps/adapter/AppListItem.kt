package com.letianpai.robot.desktop.apps.adapter

import android.graphics.Bitmap
import java.util.Objects

/**
 * @author liujunbin
 */
class AppListItem {
    var appIcon: Int = 0
    var appNameTextColor: Int = 0
    var appName: String? = null
    var appPackageName: String? = null
    var appTag: String? = null
    private val appDescription: String? = null
    var appBitmap: Bitmap? = null
    var appIconSmall: Int = 0
    var appBitmapSmall: Bitmap? = null
    var appBg: Int = 0
    var startType: Int = 0 //Application start-up method
    var startValue: String? = null //Application start-up related values


    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as AppListItem
        return appPackageName == that.appPackageName
    }

    override fun hashCode(): Int {
        return Objects.hash(appPackageName)
    }

    override fun toString(): String {
        return "AppListItem{" +
                "appIcon=" + appIcon +
                ", appNameTextColor=" + appNameTextColor +
                ", appName='" + appName + '\'' +
                ", appPackageName='" + appPackageName + '\'' +
                ", appTag='" + appTag + '\'' +
                ", appDescription='" + appDescription + '\'' +
                ", appBitmap=" + appBitmap +
                ", appIconSmall=" + appIconSmall +
                ", appBitmapSmall=" + appBitmapSmall +
                ", appBg=" + appBg +
                ", startType=" + startType +
                ", startValue='" + startValue + '\'' +
                '}'
    }

    companion object {
        const val APP_START_PACKAGE: Int = 0
        const val APP_START_ACTION: Int = 1
    }
}
