package com.letianpai.robot.desktop.manager

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.components.storage.RobotSubConfigManager
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.apps.adapter.AppListItem
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.parser.RobotMenu
import com.letianpai.robot.desktop.storage.RobotConfigManager
import com.letianpai.robot.desktop.utils.AppTagConsts
import com.letianpai.robot.desktop.utils.FunctionUtils
import com.letianpai.robot.desktop.utils.OpenTypeConsts
import com.letianpai.robot.desktop.utils.PackageConsts
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * @author liujunbin
 */
class RobotAppListManager(private val mContext: Context) {
    private val appList: MutableList<AppListItem> = ArrayList()
    var installedApps: MutableList<ApplicationInfo> = ArrayList()
    private val packageManager: PackageManager = mContext.packageManager
    private var appMenuInfoList = ArrayList<AppMenuInfo?>()
    private var tempAppMenuInfoList = ArrayList<AppMenuInfo?>()
    private val fullAppMenuInfoList = ArrayList<AppMenuInfo?>()
    private val uninstallPackageList = ArrayList<String>()

    private val updateTime: Long = 0
    var appInformation: String = ""
    private var hadClosed = false
    private val robotPackageList = ArrayList<String>()
    private var scrollPosition = 0


    init {
        initPackageList()
        getInstalledApps(mContext)
    }

    fun getInstalledApps(context: Context?): List<ApplicationInfo> {
        installedApps.clear()

        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (app in apps) {
            // 这里可以添加过滤条件，例如只获取非系统应用等
            if ((!TextUtils.isEmpty(app.packageName)) && (app.packageName.contains("letianpai") || app.packageName.contains(
                    "geeui"
                )
                        || app.packageName.contains("renhejia")
                        || app.packageName.contains("keepempty")
                        || app.packageName.contains("robot"))
            ) {
                installedApps.add(app)
            }
        }
        return installedApps
    }


    fun getAppList(): List<AppListItem> {
        appList.clear()
        for (i in installedApps.indices) {
            val appListItem = AppListItem()
            appListItem.appName =
                getAppNameFromPackageName(mContext, installedApps[i].packageName)
            appListItem.appPackageName = installedApps[i].packageName
            val icon = getAppIconByPackageName(installedApps[i].packageName)
            if (icon == 0) {
                appListItem.appIcon = R.drawable.default_app_icon
            } else {
                appListItem.appIcon = icon
            }

            //            appList.get(i).setAppName(installedApps.get(i).name);
//            appList.get(i).setAppPackageName(installedApps.get(i).packageName);
//            appList.get(i).setAppIcon(R.drawable.icon_apple);
            appList.add(appListItem)
        }

        return appList
    }

    fun fillAppListIcon(appMenuList: ArrayList<AppMenuInfo?>): ArrayList<AppMenuInfo?> {
        for (i in appMenuList.indices) {
            //            int icon = getAppIconByPackageName(AppMenuList.get(i)!!.packageName);

            val icon = getAppIconByTag(appMenuList[i]!!.icon)
            if (icon == 0) {
                appMenuList[i]!!.localIcon = R.drawable.default_app_icon
            } else {
                appMenuList[i]!!.localIcon = icon
            }
        }

        return appMenuList
    }


    val localAppList: ArrayList<AppMenuInfo?>
        get() {
            val appList =
                RobotSubConfigManager.getInstance(mContext)!!.userPackageList
            val appMenuList =
                ArrayList<AppMenuInfo?>()
            if (appList != null && appList.size > 0) {
                for (i in appList.indices) {
                    if (!getInstance(mContext)
                            .isInThePackageList(appList[i]!!)
                    ) {
                        val appName =
                            getAppNameFromPackageName(mContext, appList[i]!!)
                        val drawable = get3PartAppIconByPackageName(appList[i]!!)

                        val appMenuInfo = AppMenuInfo()
                        appMenuInfo.name = appName
                        appMenuInfo.en_name = appName
                        appMenuInfo.packageName = appList[i]
                        appMenuInfo.openType = OpenTypeConsts.OPEN_APP_USER_APP
                        appMenuInfo.drawableIcon = drawable
                        appMenuList.add(appMenuInfo)
                    }
                }
            }
            return appMenuList
        }

    fun getLocalAppIcon(packageName: String): Drawable? {
        val drawable = get3PartAppIconByPackageName(packageName)
        return drawable
    }

    val appMenuList: ArrayList<AppMenuInfo?>
        get() {
            for (i in appMenuInfoList.indices) {
//            appMenuInfoList.get(i)!!.localIcon =(getAppIconByPackageName(appMenuInfoList.get(i)!!.packageName));
                appMenuInfoList[i]!!.localIcon =(getAppIconByTag(appMenuInfoList[i]!!.packageName))
            }

            return appMenuInfoList
        }

    fun getModeNameByPackage(packageName: String?): String? {
        if (fullAppMenuInfoList != null) {
            for (i in fullAppMenuInfoList.indices) {
                if (!TextUtils.isEmpty(packageName) && fullAppMenuInfoList[i] != null && !TextUtils.isEmpty(
                        fullAppMenuInfoList[i]!!.packageName
                    )
                    && packageName == fullAppMenuInfoList[i]!!.packageName
                ) {
                    return fullAppMenuInfoList[i]!!.mode
                }
            }
        }
        return null
    }

    private fun getAppIconByPackageName(packageName: String): Int {
        return if (TextUtils.isEmpty(packageName)) {
            0
        } else if (packageName == PackageConsts.PACKAGE_NAME_TIME) {
            R.drawable.icon_m_time
        } else if (packageName == PackageConsts.ROBOT_PACKAGE_NAME) {
            R.drawable.icon_m_pet
        } else if (packageName == PackageConsts.PACKAGE_NAME_ALBUM) {
            R.drawable.icon_m_album
        } else if (packageName == PackageConsts.PACKAGE_NAME_APHORISMS) {
            R.drawable.icon_m_aphorisms
        } else if (packageName == PackageConsts.PACKAGE_NAME_VOICE_MEMO) {
            R.drawable.icon_voice_recorder
        } else if (packageName == PackageConsts.PACKAGE_NAME_AUTO_CHARGING) {
            R.drawable.icon_recharging
        } else if (packageName == PackageConsts.PACKAGE_NAME_IDENT) {
            R.drawable.icon_ai_game
        } else if (packageName == PackageConsts.PACKAGE_NAME_REMINDER) {
            R.drawable.icon_health_notification
        } else if (packageName == PackageConsts.PACKAGE_APP_NAME_SPECTRUM) {
            R.drawable.icon_m_spectrum
        } else if (packageName == PackageConsts.PACKAGE_NAME_EXPRESSION) {
            R.drawable.icon_m_expression
        } else if (packageName == PackageConsts.SPEECH_PACKAGE_NAME) {
            R.drawable.icon_ai_voice_engine
        } else if (packageName == PackageConsts.ALARM_PACKAGE_NAME) {
            R.drawable.icon_alarm
        } else if (packageName == PackageConsts.WEATHER_PACKAGE_NAME) {
            R.drawable.icon_m_weather
        } else if (packageName == PackageConsts.PACKAGE_NAME_COMMEMORATION) {
            R.drawable.icon_m_commemoration
        } else if (packageName == PackageConsts.PACKAGE_NAME_LAMP) {
            R.drawable.icon_m_lamp
        } else if (packageName == PackageConsts.PACKAGE_NAME_CUSTOM) {
            R.drawable.icon_m_custom
        } else if (packageName == PackageConsts.PACKAGE_NAME_NEWS) {
            R.drawable.icon_m_news
        } else if (packageName == PackageConsts.PACKAGE_NAME_WORDS) {
            R.drawable.icon_m_word
        } else if (packageName == PackageConsts.PACKAGE_NAME_MESSAGE) {
            R.drawable.icon_m_message
        } else if (packageName == PackageConsts.STOCK_PACKAGE_NAME) {
            R.drawable.icon_m_stock
        } else if (packageName == PackageConsts.PACKAGE_NAME_FANS) {
            R.drawable.icon_m_fans
        } else if (packageName == PackageConsts.PACKAGE_APP_NAME_MEDITATION) {
            R.drawable.icon_m_meditation
        } else if (packageName == PackageConsts.PACKAGE_NAME_COUNT_DOWN) {
            R.drawable.icon_home_sos
        } else if (packageName == PackageConsts.PACKAGE_APP_NAME_REMINDER) {
            R.drawable.icon_health_notification
        } else if (packageName == PackageConsts.PACKAGE_NAME_APP_STORE) {
            R.drawable.icon_app_store
        } else if (packageName == PackageConsts.PACKAGE_NAME_FIST_PALM_GAME) {
            R.drawable.icon_ai_game
        } else if (packageName == PackageConsts.PACKAGE_APP_NAME_POMO) {
            R.drawable.icon_m_pomodoro
        } else if (packageName == PackageConsts.PACKAGE_APP_NAME_POMO) {
            R.drawable.icon_m_lamp
        } else if (packageName == PackageConsts.PACKAGE_NAME_VIDEO_CALL) {
            R.drawable.icon_video_call
        } else if (packageName == PackageConsts.PACKAGE_NAME_VIDEO_CALL) {
            R.drawable.icon_remote_control
        } else if (packageName == PackageConsts.TAKEPHOTO_PACKAGE_NAME) {
            R.drawable.icon_take_photo
        } else if (packageName == PackageConsts.PACKAGE_NAME_GEEUI_SETTING) {
            R.drawable.icon_geeui_settings
        } else if (packageName == PackageConsts.PACKAGE_NAME_GEEUI_DOWNLOADER) {
            R.drawable.icon_downloader
        } else if (packageName == PackageConsts.PACKAGE_NAME_FANS_SHOW) {
            R.drawable.icon_fans_show
        } else {
            0
        }
    }

    private fun getAppIconByTag(tag: String?): Int {
        return if (TextUtils.isEmpty(tag)) {
            0
        } else if (tag == AppTagConsts.TAG_NAME_TIME) {
            R.drawable.icon_m_time
        } else if (tag == AppTagConsts.TAG_NAME_PET) {
            R.drawable.icon_m_pet
        } else if (tag == AppTagConsts.TAG_NAME_ALBUM) {
            R.drawable.icon_m_album
        } else if (tag == AppTagConsts.TAG_NAME_APHORISMS) {
            R.drawable.icon_m_aphorisms
        } else if (tag == AppTagConsts.TAG_NAME_VOICE_MEMO) {
            R.drawable.icon_voice_recorder
            //        }else if (tag.equals(PackageConsts.PACKAGE_NAME_AUTO_CHARGING)){
            //            return R.drawable.icon_recharging;
            //        }else if (tag.equals(PackageConsts.PACKAGE_NAME_IDENT)){
            //            return R.drawable.icon_ai_game;
        } else if (tag == AppTagConsts.TAG_NAME_REMINDER) {
            R.drawable.icon_health_notification
        } else if (tag == AppTagConsts.PACKAGE_APP_NAME_SPECTRUM) {
            R.drawable.icon_m_spectrum
        } else if (tag == AppTagConsts.PACKAGE_NAME_EXPRESSION) {
            R.drawable.icon_m_expression
        } else if (tag == AppTagConsts.TAG_AI_PACKAGE_NAME) {
            R.drawable.icon_ai_voice_engine
        } else if (tag == AppTagConsts.ALARM_PACKAGE_NAME) {
            R.drawable.icon_alarm
        } else if (tag == AppTagConsts.WEATHER_PACKAGE_NAME) {
            R.drawable.icon_m_weather
        } else if (tag == AppTagConsts.PACKAGE_NAME_COMMEMORATION) {
            R.drawable.icon_m_commemoration
        } else if (tag == AppTagConsts.PACKAGE_NAME_LAMP) {
            R.drawable.icon_m_lamp
        } else if (tag == AppTagConsts.PACKAGE_NAME_CUSTOM) {
            R.drawable.icon_m_custom
        } else if (tag == AppTagConsts.PACKAGE_NAME_NEWS) {
            R.drawable.icon_m_news
        } else if (tag == AppTagConsts.PACKAGE_NAME_WORDS) {
            R.drawable.icon_m_word
        } else if (tag == AppTagConsts.PACKAGE_NAME_MESSAGE) {
            R.drawable.icon_m_message
        } else if (tag == AppTagConsts.STOCK_PACKAGE_NAME) {
            R.drawable.icon_m_stock
        } else if (tag == AppTagConsts.PACKAGE_NAME_FANS) {
            R.drawable.icon_m_fans
        } else if (tag == AppTagConsts.PACKAGE_APP_NAME_MEDITATION) {
            R.drawable.icon_m_meditation
        } else if (tag == AppTagConsts.TAG_NAME_EVENT_COUNT_DOWN) {
            R.drawable.icon_m_todo
        } else if (tag == AppTagConsts.PACKAGE_NAME_APP_STORE) {
            R.drawable.icon_app_store
        } else if (tag == AppTagConsts.PACKAGE_NAME_FIST_PALM_GAME) {
            R.drawable.icon_ai_game
        } else if (tag == AppTagConsts.PACKAGE_APP_NAME_POMO) {
            R.drawable.icon_m_pomodoro
        } else if (tag == AppTagConsts.PACKAGE_APP_NAME_POMO) {
            R.drawable.icon_m_lamp
        } else if (tag == AppTagConsts.PACKAGE_NAME_VIDEO_CALL) {
            R.drawable.icon_video_call
        } else if (tag == AppTagConsts.PACKAGE_NAME_REMOTE) {
            R.drawable.icon_remote_view
        } else if (tag == AppTagConsts.PACKAGE_NAME_REMOTE_CONTROL) {
            R.drawable.icon_remote_control
        } else if (tag == AppTagConsts.TAKEPHOTO_PACKAGE_NAME) {
            R.drawable.icon_take_photo
        } else if (tag == AppTagConsts.TAG_NAME_GEEUI_SETTINGS) {
            R.drawable.icon_geeui_settings
        } else if (tag == AppTagConsts.PACKAGE_NAME_GEEUI_DOWNLOADER) {
            R.drawable.icon_downloader
        } else if (tag == AppTagConsts.PACKAGE_NAME_GEEUI_LOCAL_DOWNLOADER) {
            R.drawable.icon_app_installer
        } else if (tag == AppTagConsts.TAG_NAME_VIDEO_PLAYER) {
            R.drawable.icon_small_player
        } else if (tag == AppTagConsts.TAG_NAME_BLUETOOTH_SPEAKER) {
            R.drawable.icon_bt
        } else if (tag == AppTagConsts.TAG_NAME_TIME_COUNTDOWN) {
            R.drawable.icon_count_down_timer
        } else if (tag == AppTagConsts.TAG_NAME_HOME_NOTIFICATION) {
            R.drawable.icon_home_sos
        } else if (tag == AppTagConsts.TAG_NAME_HOME_AI_SKILL) {
            R.drawable.icon_skill
        } else if (tag == AppTagConsts.TAG_NAME_ALARM) {
            R.drawable.icon_alarm
        } else if (tag == AppTagConsts.TAG_NAME_MY_MUSIC) {
            R.drawable.icon_my_music
        } else if (tag == AppTagConsts.TAG_NAME_GUIDE) {
            R.drawable.icon_guide
        } else if (tag == AppTagConsts.TAG_NAME_FANS_SHOW) {
            R.drawable.icon_fans_show
        } else {
            0
        }
    }

    fun getAppNameFromPackageName(context: Context?, packageName: String): String {
        var appName = ""

        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            appName = packageManager.getApplicationLabel(appInfo) as String
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return appName
    }

    fun get3PartAppIconByPackageName(packageName: String): Drawable? {
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            val icon = appInfo.loadIcon(packageManager)
            //            return appInfo.loadIcon(packageManager);
            // 在这里尝试从 ConstantState 获取高分辨率图标
            val constantState = icon.constantState
            if (constantState != null) {
                val highResIcon = constantState.newDrawable().mutate()
                return highResIcon
            } else {
                return icon
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }

    //    public static Drawable getAppIconByPackageName(Context context, String packageName) {
    //        try {
    //            PackageManager packageManager = context.getPackageManager();
    //            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
    //            Drawable icon = appInfo.loadIcon(packageManager);
    //
    //            // 在这里尝试从 ConstantState 获取高分辨率图标
    //            Drawable.ConstantState constantState = icon.getConstantState();
    //            if (constantState != null) {
    //                Drawable highResIcon = constantState.newDrawable().mutate();
    //                return highResIcon;
    //            } else {
    //                return icon;
    //            }
    //        } catch (PackageManager.NameNotFoundException e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //    }
    fun getAppMenuInfoList(): List<AppMenuInfo?> {
        return appMenuInfoList
    }

    fun updateAppMenuList() {
        Thread { desktopConfig }.start()
    }

    private val desktopConfig: Unit
        get() {
            GeeUiNetManager.getMenuConfig(
                mContext,
                SystemUtil.isInChinese,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        if (response.body != null) {
                            var info = ""
                            info = response.body!!.string()
                            //                    Log.e("letianpai_update", "menu: " + info);
                            val robotMenu: RobotMenu?
                            try {
                                if (info != null) {
                                    if (TextUtils.isEmpty(info)) {
                                        return
                                    }
                                    if (appInformation != info) {
                                        appInformation = info
                                    }

                                    robotMenu =
                                        Gson().fromJson(info, RobotMenu::class.java)


                                    if (robotMenu?.data != null && robotMenu.data!!.config_data.isNotEmpty()
                                    ) {
                                        updateAppMenuInfo(robotMenu.data!!.config_data)

                                        appMenuInfoList = getAppMenuInfoList(appMenuInfoList)
                                        appMenuInfoList.addAll(
                                            getInstance(
                                                mContext
                                            ).localAppList
                                        )
                                        RobotConfigManager.getInstance(mContext)!!
                                            .appListConfig = (info)
                                        RobotConfigManager.getInstance(mContext)!!
                                            .commit()
                                        if (!TextUtils.isEmpty(appInformation) && appInformation == info && tempAppMenuInfoList === appMenuInfoList) {
                                            return
                                        }
                                        tempAppMenuInfoList = appMenuInfoList
                                        AppMenuUpdateCallback.instance
                                            .setAppMenuUpdate(appMenuInfoList)
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                })
        }

    fun getAppListInfo(info: String?): ArrayList<AppMenuInfo?> {
        var appMenuList = ArrayList<AppMenuInfo?>()
        try {
            val robotMenu = Gson().fromJson(info, RobotMenu::class.java)
            if (robotMenu?.data != null && robotMenu.data!!.config_data != null && robotMenu.data!!.config_data.isNotEmpty()) {
                appMenuList = getAppMenuInfoList(robotMenu.data!!.config_data as? Array<AppMenuInfo?>)
                appMenuList = fillAppListIcon(appMenuList)
            } else {
            }
        } catch (e: Exception) {
        }

        return appMenuList
    }

    val appListInfo: ArrayList<AppMenuInfo?>?
        get() {
            val appMenuList: String =
                RobotConfigManager.getInstance(mContext)!!.appListConfig.toString()
            return if (TextUtils.isEmpty(appMenuList)) {
                null
            } else {
                getAppListInfo(appMenuList)
            }
        }

    private fun updateAppMenuInfo(appMenuInfo: Array<AppMenuInfo>) {
        if (appMenuInfo != null && appMenuInfo.size > 0) {
            appMenuInfoList.clear()
            fullAppMenuInfoList.clear()
            for (i in appMenuInfo.indices) {
                appMenuInfoList.add(appMenuInfo[i])
                fullAppMenuInfoList.add(appMenuInfo[i])
            }
        }
    }

    private fun updateAppMenuInfoToUninstallStatus(appMenuInfo: Array<AppMenuInfo>?) {
        if (appMenuInfo != null && appMenuInfo.size > 0) {
            appMenuInfoList.clear()
            fullAppMenuInfoList.clear()
            for (i in appMenuInfo.indices) {
                appMenuInfoList.add(appMenuInfo[i])
                fullAppMenuInfoList.add(appMenuInfo[i])
            }
        }
    }

    private fun getAppMenuInfoList(appMenuInfo: Array<AppMenuInfo?>?): ArrayList<AppMenuInfo?> {
        val appMenuList = ArrayList<AppMenuInfo?>()
        if (appMenuInfo != null && appMenuInfo.size > 0) {
            for (i in appMenuInfo.indices) {
                if (isAppInLocal(appMenuInfo[i]!!.packageName.toString())) {
                    appMenuList.add(appMenuInfo[i])
                }
            }
        }
        return appMenuList
    }

    fun getAppMenuInfoList(appMenuInfo: ArrayList<AppMenuInfo?>?): ArrayList<AppMenuInfo?> {
        val appMenuList = ArrayList<AppMenuInfo?>()
        if (appMenuInfo != null && appMenuInfo.size > 0) {
            for (i in appMenuInfo.indices) {
                if (appMenuInfo[i] != null && isAppInLocal(appMenuInfo[i]!!.packageName.toString()) && FunctionUtils.isNeedShowApp(
                        mContext,
                        appMenuInfo[i]!!.packageName.toString(),
                        appMenuInfo[i]!!.version.toString()
                    )
                ) {
                    appMenuList.add(appMenuInfo[i])
                }
            }
        }
        return appMenuList
    }

    private fun isAppInLocal(packageName: String): Boolean {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return if (packageInfo != null) {
                true
            } else {
                false
            }
        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace();
            return false
        }
    }

    fun setHadClosed(hadClosed: Boolean) {
        this.hadClosed = hadClosed
    }

    private fun getLocalAppInfo(packageName: String): ApplicationInfo? {
        try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            return appInfo
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return null
        }
    }

    fun resetLocalUserPackageList() {
        RobotSubConfigManager.getInstance(mContext)!!.resetUserPackageList()
        val localAppList = RobotSubConfigManager.getInstance(mContext)!!.userPackageList
        if (localAppList != null && localAppList.size > 0) {
            for (i in localAppList.indices) {
                val applicationInfo = getLocalAppInfo(localAppList[i]!!)
                if (applicationInfo == null) {
                    RobotSubConfigManager.getInstance(mContext)!!.removeUserPackage(localAppList[i]!!)
                }
                if (isInThePackageList(localAppList[i]!!)) {
                    RobotSubConfigManager.getInstance(mContext)!!.removeUserPackage(localAppList[i]!!)
                }
            }

            RobotSubConfigManager.getInstance(mContext)!!.commit()
        }
    }

    private fun initPackageList() {
        robotPackageList.clear()
        robotPackageList.add(PackageConsts.TAKEPHOTO_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.ROBOT_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.LAUNCHER_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.AUTO_APP_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.LEX_CLASS_PACKAGE)
        robotPackageList.add(PackageConsts.SPEECH_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.ALARM_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.STOCK_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.WEATHER_PACKAGE_NAME)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_COUNT_DOWN)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_COMMEMORATION)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_WORDS)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_NEWS)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_MESSAGE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FANS)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_IDENT)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_CUSTOM)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_VIDEO_CALL)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_LAMP)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_REMINDER)
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_REMINDER)
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_SPECTRUM)
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_POMO)
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_OTA)
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_MEDITATION)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_TIME)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_EXPRESSION)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_ALBUM)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_WIFI_CONNECTOR)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_SETTINGS)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_APP_STORE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FIST_PALM_GAME)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_VOICE_MEMO)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_MCU_SERVICE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_APHORISMS)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_RESOURCE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_VIDEO_PLAYER)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_DOWNLOADER)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_MY_MUSIC)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_TASK_SERVICE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_DESKTOP)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FACTORY)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_LOCAL_DOWNLOADER)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_INSTALLER)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GUIDE)
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FANS_SHOW)
    }

    fun isInThePackageList(packageName: String): Boolean {
        if (robotPackageList == null || robotPackageList.size == 0) {
            return false
        }
        return if (robotPackageList.contains(packageName)) {
            true
        } else {
            false
        }
    }

    private fun isInUninstallPackageList(packageName: String): Boolean {
        return if (uninstallPackageList.contains(packageName)) {
            true
        } else {
            false
        }
    }

    fun setScrollPosition(scrollPosition: Int) {
        if (scrollPosition != 0) {
            this.scrollPosition = scrollPosition
            Log.e(
                "letianpai_list_view_update",
                "============ RobotAppListManager_setScrollPosition_: $scrollPosition"
            )
        }
    }

    fun getScrollPosition(): Int {
        Log.e(
            "letianpai_list_view_update",
            "============ RobotAppListManager_setScrollPosition_: $scrollPosition"
        )
        return scrollPosition
    }

    fun cleanScrollPosition() {
        scrollPosition = 0
    }

    companion object {
        private var sInstance: RobotAppListManager? = null
        fun getInstance(context: Context): RobotAppListManager {
            if (sInstance == null) {
                synchronized(RobotAppListManager::class.java) {
                    if (sInstance == null) {
                        sInstance = RobotAppListManager(context)
                    }
                }
            }
            return sInstance!!
        }
    }
}
