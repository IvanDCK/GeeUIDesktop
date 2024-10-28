package com.letianpai.robot.desktop.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.text.TextUtils
import kotlin.math.min

/**
 * Created by liujunbin
 */
object FunctionUtils {
    fun removeLastByte(str: String?): String? {
        if (str == null || str.isEmpty()) {
            return str // Returns the original string if it is null or of length 0.
        }

        // Use substring to remove the last byte
        return str.substring(0, str.length - 1)
    }


    fun getTopActivityName(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = am.getRunningTasks(1)
        if (runningTasks != null && runningTasks.size > 0) {
            val taskInfo = runningTasks[0]
            val componentName = taskInfo.topActivity
            if (componentName != null && componentName.className != null) {
                return componentName.className
            }
        }
        return null
    }

    fun isLauncherOnTheTop(context: Context): Boolean {
        val activityName = getTopActivityName(context)
        return if (activityName != null && activityName.contains(context.packageName)) {
            true
        } else {
            false
        }
    }

    /**
     * Shutdown the robot
     *
     * @param context
     */
    fun shutdownRobot(context: Context) {
        if (!isLauncherOnTheTop(context)) {
            return
        }

        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val clazz: Class<*> = pm.javaClass
        try {
            val shutdown = clazz.getMethod(
                "shutdown",
                Boolean::class.javaPrimitiveType,
                String::class.java,
                Boolean::class.javaPrimitiveType
            )
            shutdown.invoke(pm, false, "shutdown", false)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    val romVersion: String
        get() {
            //Determine the ROM version number
            val displayVersion = Build.DISPLAY
            var localVersion = ""
            if (displayVersion.startsWith("GeeUITest")) {
                localVersion = displayVersion.replace("GeeUITest.", "")
            } else if (displayVersion.startsWith("GeeUI")) {
                localVersion = displayVersion.replace("GeeUI.", "")
            }
            localVersion = if (localVersion.endsWith("d")) {
                localVersion.replace(".d", "")
            } else {
                localVersion.replace(".u", "")
            }
            return localVersion
        }

    fun compareVersion(version1: String, version2: String): Boolean {
        // Cutting point ‘.’ ;
        val versionArray1 =
            version1.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val versionArray2 =
            version2.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (i in versionArray1.indices) {
            if (!TextUtils.isEmpty(versionArray1[i])) {
                versionArray1[i] = versionArray1[i].trim { it <= ' ' }
            }
        }

        for (i in versionArray2.indices) {
            if (!TextUtils.isEmpty(versionArray2[i])) {
                versionArray2[i] = versionArray2[i].trim { it <= ' ' }
            }
        }

        var idx = 0
        // Take the minimum length value
        val minLength =
            min(versionArray1.size.toDouble(), versionArray2.size.toDouble()).toInt()
        var diff = 0
        // Compare lengths first, then characters.
        while (idx < minLength && ((versionArray1[idx].length - versionArray2[idx].length).also {
                diff = it
            }) == 0 && (versionArray1[idx].compareTo(
                versionArray2[idx]
            ).also { diff = it }) == 0
        ) {
            ++idx
        }
        // If the size has been split, it is returned directly, if not, the bits are compared again,
        // and the one with the sub-version is greater
        diff = if ((diff != 0)) diff else versionArray1.size - versionArray2.size
        return diff > 0
    }

    fun isNeedShowApp(context: Context, packageName: String, onlineVersion: String): Boolean {
        val localVersion = getVersionName(context, packageName)
        val result = compareVersion(localVersion!!, onlineVersion)
        return result
    }


    fun isEven(number: Int): Boolean {
        return number % 2 == 0
    }

    fun isOdd(number: Int): Boolean {
        return !isEven(number)
    }

    fun getVersionName(context: Context, packageName: String): String? {
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }
}
