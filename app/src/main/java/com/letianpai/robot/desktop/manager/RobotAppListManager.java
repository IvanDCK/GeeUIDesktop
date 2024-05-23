package com.letianpai.robot.desktop.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.letianpai.robot.components.network.nets.GeeUiNetManager;
import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.components.parser.appstatus.AppInfo;
import com.letianpai.robot.components.storage.RobotSubConfigManager;
import com.letianpai.robot.desktop.MainActivity;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.apps.adapter.AppListItem;
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.parser.RobotMenu;
import com.letianpai.robot.desktop.storage.RobotConfigManager;
import com.letianpai.robot.desktop.utils.AppTagConsts;
import com.letianpai.robot.desktop.utils.FunctionUtils;
import com.letianpai.robot.desktop.utils.OpenTypeConsts;
import com.letianpai.robot.desktop.utils.PackageConsts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author liujunbin
 */
public class RobotAppListManager {

    private static RobotAppListManager sInstance = null;
    private Context mContext;
    private List<AppListItem> appList = new ArrayList<>();
    List<ApplicationInfo> installedApps = new ArrayList<>();
    private PackageManager packageManager;
    private ArrayList<AppMenuInfo> appMenuInfoList = new ArrayList<>();
    private ArrayList<AppMenuInfo> tempAppMenuInfoList = new ArrayList<>();
    private ArrayList<AppMenuInfo> fullAppMenuInfoList = new ArrayList<>();
    private ArrayList<String> uninstallPackageList = new ArrayList<>();

    private long updateTime = 0;
    public String appInformation = "";
    private boolean hadClosed = false;
    private ArrayList<String> robotPackageList = new ArrayList<>();
    private int scrollPosition = 0;


    public RobotAppListManager(Context context) {
        this.mContext = context;
        packageManager = context.getPackageManager();
        initPackageList();
        getInstalledApps(mContext);
    }

    public List<ApplicationInfo> getInstalledApps(Context context) {
        installedApps.clear();

        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            // 这里可以添加过滤条件，例如只获取非系统应用等
            if ((!TextUtils.isEmpty(app.packageName)) && (app.packageName.contains("letianpai") || app.packageName.contains("geeui")
                    || app.packageName.contains("renhejia")
                    || app.packageName.contains("keepempty")
                    || app.packageName.contains("robot"))) {
                installedApps.add(app);
            }

        }
        return installedApps;
    }


    public static RobotAppListManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (RobotAppListManager.class) {
                if (sInstance == null) {
                    sInstance = new RobotAppListManager(context);
                }
            }
        }
        return sInstance;
    }


    public List<AppListItem> getAppList() {
        appList.clear();
        for (int i = 0; i < installedApps.size(); i++) {
            AppListItem appListItem = new AppListItem();
            appListItem.setAppName(getAppNameFromPackageName(mContext, installedApps.get(i).packageName));
            appListItem.setAppPackageName(installedApps.get(i).packageName);
            int icon = getAppIconByPackageName(installedApps.get(i).packageName);
            if (icon == 0) {
                appListItem.setAppIcon(R.drawable.default_app_icon);
            } else {
                appListItem.setAppIcon(icon);
            }

//            appList.get(i).setAppName(installedApps.get(i).name);
//            appList.get(i).setAppPackageName(installedApps.get(i).packageName);
//            appList.get(i).setAppIcon(R.drawable.icon_apple);
            appList.add(appListItem);
        }

        return appList;
    }

    public ArrayList<AppMenuInfo> fillAppListIcon(ArrayList<AppMenuInfo> appMenuList) {
        for (int i = 0; i < appMenuList.size(); i++) {

//            int icon = getAppIconByPackageName(AppMenuList.get(i).getPackageName());
            int icon = getAppIconByTag(appMenuList.get(i).getIcon());
            if (icon == 0) {
                appMenuList.get(i).setLocalIcon(R.drawable.default_app_icon);
            } else {
                appMenuList.get(i).setLocalIcon(icon);
            }
        }

        return appMenuList;
    }


    public ArrayList<AppMenuInfo> getLocalAppList() {
        List<String> appList = RobotSubConfigManager.getInstance(mContext).getUserPackageList();
        ArrayList<AppMenuInfo> appMenuList = new ArrayList<>();
        if (appList != null && appList.size() > 0) {
            for (int i = 0; i < appList.size(); i++) {
                if (!RobotAppListManager.getInstance(mContext).isInThePackageList(appList.get(i))){
                    String appName = getAppNameFromPackageName(mContext, appList.get(i));
                    Drawable drawable = get3PartAppIconByPackageName(appList.get(i));

                    AppMenuInfo appMenuInfo = new AppMenuInfo();
                    appMenuInfo.setName(appName);
                    appMenuInfo.setEn_name(appName);
                    appMenuInfo.setPackageName(appList.get(i));
                    appMenuInfo.setOpenType(OpenTypeConsts.OPEN_APP_USER_APP);
                    appMenuInfo.setDrawableIcon(drawable);
                    appMenuList.add(appMenuInfo);

                }

            }
        }
        return appMenuList;
    }

    public Drawable getLocalAppIcon(String packageName) {
        Drawable drawable = get3PartAppIconByPackageName(packageName);
        return drawable;
    }

    public ArrayList<AppMenuInfo> getAppMenuList() {
        for (int i = 0; i < appMenuInfoList.size(); i++) {
//            appMenuInfoList.get(i).setLocalIcon(getAppIconByPackageName(appMenuInfoList.get(i).getPackageName()));
            appMenuInfoList.get(i).setLocalIcon(getAppIconByTag(appMenuInfoList.get(i).getPackageName()));
        }

        return appMenuInfoList;
    }

    public String getModeNameByPackage(String packageName) {
        if (fullAppMenuInfoList != null) {
            for (int i = 0; i < fullAppMenuInfoList.size(); i++) {
                if (!TextUtils.isEmpty(packageName) && fullAppMenuInfoList.get(i) != null && !TextUtils.isEmpty(fullAppMenuInfoList.get(i).getPackageName())
                        && packageName.equals(fullAppMenuInfoList.get(i).getPackageName())) {

                    return fullAppMenuInfoList.get(i).getMode();

                }
            }
        }
        return null;
    }

    private int getAppIconByPackageName(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return 0;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_TIME)) {
            return R.drawable.icon_m_time;
        } else if (packageName.equals(PackageConsts.ROBOT_PACKAGE_NAME)) {
            return R.drawable.icon_m_pet;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_ALBUM)) {
            return R.drawable.icon_m_album;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_APHORISMS)) {
            return R.drawable.icon_m_aphorisms;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_VOICE_MEMO)) {
            return R.drawable.icon_voice_recorder;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_AUTO_CHARGING)) {
            return R.drawable.icon_recharging;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_IDENT)) {
            return R.drawable.icon_ai_game;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_REMINDER)) {
            return R.drawable.icon_health_notification;
        } else if (packageName.equals(PackageConsts.PACKAGE_APP_NAME_SPECTRUM)) {
            return R.drawable.icon_m_spectrum;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_EXPRESSION)) {
            return R.drawable.icon_m_expression;
        } else if (packageName.equals(PackageConsts.SPEECH_PACKAGE_NAME)) {
            return R.drawable.icon_ai_voice_engine;
        } else if (packageName.equals(PackageConsts.ALARM_PACKAGE_NAME)) {
            return R.drawable.icon_alarm;
        } else if (packageName.equals(PackageConsts.WEATHER_PACKAGE_NAME)) {
            return R.drawable.icon_m_weather;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_COMMEMORATION)) {
            return R.drawable.icon_m_commemoration;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_LAMP)) {
            return R.drawable.icon_m_lamp;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_CUSTOM)) {
            return R.drawable.icon_m_custom;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_NEWS)) {
            return R.drawable.icon_m_news;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_WORDS)) {
            return R.drawable.icon_m_word;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_MESSAGE)) {
            return R.drawable.icon_m_message;
        } else if (packageName.equals(PackageConsts.STOCK_PACKAGE_NAME)) {
            return R.drawable.icon_m_stock;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_FANS)) {
            return R.drawable.icon_m_fans;
        } else if (packageName.equals(PackageConsts.PACKAGE_APP_NAME_MEDITATION)) {
            return R.drawable.icon_m_meditation;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_COUNT_DOWN)) {
            return R.drawable.icon_home_sos;
        } else if (packageName.equals(PackageConsts.PACKAGE_APP_NAME_REMINDER)) {
            return R.drawable.icon_health_notification;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_APP_STORE)) {
            return R.drawable.icon_app_store;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_FIST_PALM_GAME)) {
            return R.drawable.icon_ai_game;
        } else if (packageName.equals(PackageConsts.PACKAGE_APP_NAME_POMO)) {
            return R.drawable.icon_m_pomodoro;
        } else if (packageName.equals(PackageConsts.PACKAGE_APP_NAME_POMO)) {
            return R.drawable.icon_m_lamp;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_VIDEO_CALL)) {
            return R.drawable.icon_video_call;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_VIDEO_CALL)) {
            return R.drawable.icon_remote_control;
        } else if (packageName.equals(PackageConsts.TAKEPHOTO_PACKAGE_NAME)) {
            return R.drawable.icon_take_photo;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_GEEUI_SETTING)) {
            return R.drawable.icon_geeui_settings;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_GEEUI_DOWNLOADER)) {
            return R.drawable.icon_downloader;
        } else if (packageName.equals(PackageConsts.PACKAGE_NAME_FANS_SHOW)) {
            return R.drawable.icon_fans_show;
        } else {
            return 0;
        }
    }

    private int getAppIconByTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return 0;
        } else if (tag.equals(AppTagConsts.TAG_NAME_TIME)) {
            return R.drawable.icon_m_time;
        } else if (tag.equals(AppTagConsts.TAG_NAME_PET)) {
            return R.drawable.icon_m_pet;
        } else if (tag.equals(AppTagConsts.TAG_NAME_ALBUM)) {
            return R.drawable.icon_m_album;
        } else if (tag.equals(AppTagConsts.TAG_NAME_APHORISMS)) {
            return R.drawable.icon_m_aphorisms;
        } else if (tag.equals(AppTagConsts.TAG_NAME_VOICE_MEMO)) {
            return R.drawable.icon_voice_recorder;
//        }else if (tag.equals(PackageConsts.PACKAGE_NAME_AUTO_CHARGING)){
//            return R.drawable.icon_recharging;
//        }else if (tag.equals(PackageConsts.PACKAGE_NAME_IDENT)){
//            return R.drawable.icon_ai_game;
        } else if (tag.equals(AppTagConsts.TAG_NAME_REMINDER)) {
            return R.drawable.icon_health_notification;
        } else if (tag.equals(AppTagConsts.PACKAGE_APP_NAME_SPECTRUM)) {
            return R.drawable.icon_m_spectrum;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_EXPRESSION)) {
            return R.drawable.icon_m_expression;
        } else if (tag.equals(AppTagConsts.TAG_AI_PACKAGE_NAME)) {
            return R.drawable.icon_ai_voice_engine;
        } else if (tag.equals(AppTagConsts.ALARM_PACKAGE_NAME)) {
            return R.drawable.icon_alarm;
        } else if (tag.equals(AppTagConsts.WEATHER_PACKAGE_NAME)) {
            return R.drawable.icon_m_weather;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_COMMEMORATION)) {
            return R.drawable.icon_m_commemoration;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_LAMP)) {
            return R.drawable.icon_m_lamp;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_CUSTOM)) {
            return R.drawable.icon_m_custom;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_NEWS)) {
            return R.drawable.icon_m_news;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_WORDS)) {
            return R.drawable.icon_m_word;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_MESSAGE)) {
            return R.drawable.icon_m_message;
        } else if (tag.equals(AppTagConsts.STOCK_PACKAGE_NAME)) {
            return R.drawable.icon_m_stock;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_FANS)) {
            return R.drawable.icon_m_fans;
        } else if (tag.equals(AppTagConsts.PACKAGE_APP_NAME_MEDITATION)) {
            return R.drawable.icon_m_meditation;
        } else if (tag.equals(AppTagConsts.TAG_NAME_EVENT_COUNT_DOWN)) {
            return R.drawable.icon_m_todo;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_APP_STORE)) {
            return R.drawable.icon_app_store;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_FIST_PALM_GAME)) {
            return R.drawable.icon_ai_game;
        } else if (tag.equals(AppTagConsts.PACKAGE_APP_NAME_POMO)) {
            return R.drawable.icon_m_pomodoro;
        } else if (tag.equals(AppTagConsts.PACKAGE_APP_NAME_POMO)) {
            return R.drawable.icon_m_lamp;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_VIDEO_CALL)) {
            return R.drawable.icon_video_call;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_REMOTE)) {
            return R.drawable.icon_remote_view;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_REMOTE_CONTROL)) {
            return R.drawable.icon_remote_control;
        } else if (tag.equals(AppTagConsts.TAKEPHOTO_PACKAGE_NAME)) {
            return R.drawable.icon_take_photo;
        } else if (tag.equals(AppTagConsts.TAG_NAME_GEEUI_SETTINGS)) {
            return R.drawable.icon_geeui_settings;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_GEEUI_DOWNLOADER)) {
            return R.drawable.icon_downloader;
        } else if (tag.equals(AppTagConsts.PACKAGE_NAME_GEEUI_LOCAL_DOWNLOADER)) {
            return R.drawable.icon_app_installer;
        } else if (tag.equals(AppTagConsts.TAG_NAME_VIDEO_PLAYER)) {
            return R.drawable.icon_small_player;
        } else if (tag.equals(AppTagConsts.TAG_NAME_BLUETOOTH_SPEAKER)) {
            return R.drawable.icon_bt;
        } else if (tag.equals(AppTagConsts.TAG_NAME_TIME_COUNTDOWN)) {
            return R.drawable.icon_count_down_timer;
        } else if (tag.equals(AppTagConsts.TAG_NAME_HOME_NOTIFICATION)) {
            return R.drawable.icon_home_sos;
        } else if (tag.equals(AppTagConsts.TAG_NAME_HOME_AI_SKILL)) {
            return R.drawable.icon_skill;
        } else if (tag.equals(AppTagConsts.TAG_NAME_ALARM)) {
            return R.drawable.icon_alarm;
        } else if (tag.equals(AppTagConsts.TAG_NAME_MY_MUSIC)) {
            return R.drawable.icon_my_music;
        } else if (tag.equals(AppTagConsts.TAG_NAME_GUIDE)) {
            return R.drawable.icon_guide;
        } else if (tag.equals(AppTagConsts.TAG_NAME_FANS_SHOW)) {
            return R.drawable.icon_fans_show;
        } else {
            return 0;
        }
    }

    public String getAppNameFromPackageName(Context context, String packageName) {
        String appName = "";

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            appName = (String) packageManager.getApplicationLabel(appInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    public Drawable get3PartAppIconByPackageName(String packageName) {
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            Drawable icon = appInfo.loadIcon(packageManager);
//            return appInfo.loadIcon(packageManager);
            // 在这里尝试从 ConstantState 获取高分辨率图标
            Drawable.ConstantState constantState = icon.getConstantState();
            if (constantState != null) {
                Drawable highResIcon = constantState.newDrawable().mutate();
                return highResIcon;
            } else {
                return icon;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
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
    public List<AppMenuInfo> getAppMenuInfoList() {
        return appMenuInfoList;
    }

    public void updateAppMenuList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDesktopConfig();
            }
        }).start();

    }

    private void getDesktopConfig() {
        GeeUiNetManager.getMenuConfig(mContext, SystemUtil.isInChinese(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response != null && response.body() != null) {

                    String info = "";
                    info = response.body().string();
//                    Log.e("letianpai_update", "menu: " + info);
                    RobotMenu robotMenu;
                    try {
                        if (info != null) {
                            if (TextUtils.isEmpty(info) ) {
                                return;
                            }
                            if (!appInformation.equals(info)){
                                appInformation = info;
                            }

                            robotMenu = new Gson().fromJson(info, RobotMenu.class);


                            if (robotMenu != null && robotMenu.getData() != null && robotMenu.getData().getConfig_data() != null && robotMenu.getData().getConfig_data().length > 0) {
                                updateAppMenuInfo(robotMenu.getData().getConfig_data());

                                appMenuInfoList = getAppMenuInfoList(appMenuInfoList);
                                appMenuInfoList.addAll(RobotAppListManager.getInstance(mContext).getLocalAppList());
                                RobotConfigManager.getInstance(mContext).setAppListConfig(info);
                                RobotConfigManager.getInstance(mContext).commit();
                                if (!TextUtils.isEmpty(appInformation) && appInformation.equals(info) && tempAppMenuInfoList == appMenuInfoList) {
                                    return;
                                }
                                tempAppMenuInfoList = appMenuInfoList;
                                AppMenuUpdateCallback.getInstance().setAppMenuUpdate(appMenuInfoList);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private ArrayList<AppMenuInfo> getAppListInfo(String info) {

        ArrayList<AppMenuInfo> appMenuList = new ArrayList<>();
        try {
            RobotMenu robotMenu = new Gson().fromJson(info, RobotMenu.class);
            if (robotMenu != null && robotMenu.getData() != null && robotMenu.getData().getConfig_data() != null && robotMenu.getData().getConfig_data().length > 0) {

                appMenuList = getAppMenuInfoList(robotMenu.getData().getConfig_data());
                appMenuList = fillAppListIcon(appMenuList);
            } else {

            }

        } catch (Exception e) {

        }

        return appMenuList;

    }

    public ArrayList<AppMenuInfo> getAppListInfo() {
        String appMenuList = RobotConfigManager.getInstance(mContext).getAppListConfig();
        if (TextUtils.isEmpty(appMenuList)) {
            return null;
        } else {
            return getAppListInfo(appMenuList);
        }

    }

    private void updateAppMenuInfo(AppMenuInfo[] appMenuInfo) {
        if (appMenuInfo != null && appMenuInfo.length > 0) {
            appMenuInfoList.clear();
            fullAppMenuInfoList.clear();
            for (int i = 0; i < appMenuInfo.length; i++) {
                appMenuInfoList.add(appMenuInfo[i]);
                fullAppMenuInfoList.add(appMenuInfo[i]);
            }
        }
    }
    private void updateAppMenuInfoToUninstallStatus(AppMenuInfo[] appMenuInfo) {
        if (appMenuInfo != null && appMenuInfo.length > 0) {
            appMenuInfoList.clear();
            fullAppMenuInfoList.clear();
            for (int i = 0; i < appMenuInfo.length; i++) {
                appMenuInfoList.add(appMenuInfo[i]);
                fullAppMenuInfoList.add(appMenuInfo[i]);
            }
        }
    }

    private ArrayList<AppMenuInfo> getAppMenuInfoList(AppMenuInfo[] appMenuInfo) {
        ArrayList<AppMenuInfo> appMenuList = new ArrayList<>();
        if (appMenuInfo != null && appMenuInfo.length > 0) {
            for (int i = 0; i < appMenuInfo.length; i++) {
                if (isAppInLocal(appMenuInfo[i].getPackageName())) {
                    appMenuList.add(appMenuInfo[i]);
                }
            }
        }
        return appMenuList;
    }

    public ArrayList<AppMenuInfo> getAppMenuInfoList(ArrayList<AppMenuInfo> appMenuInfo) {
        ArrayList<AppMenuInfo> appMenuList = new ArrayList<>();
        if (appMenuInfo != null && appMenuInfo.size() > 0) {
            for (int i = 0; i < appMenuInfo.size(); i++) {
                if (appMenuInfo.get(i) != null && isAppInLocal(appMenuInfo.get(i).getPackageName()) && FunctionUtils.isNeedShowApp(mContext, appMenuInfo.get(i).getPackageName(), appMenuInfo.get(i).getVersion())) {
                    appMenuList.add(appMenuInfo.get(i));
                }
            }
        }
        return appMenuList;
    }

    private boolean isAppInLocal(String packageName) {

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return true;
            } else {
                return false;
            }

        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
            return false;
        }
    }

    public void setHadClosed(boolean hadClosed) {
        this.hadClosed = hadClosed;
    }

    private ApplicationInfo getLocalAppInfo( String packageName) {

        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return appInfo;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void resetLocalUserPackageList() {
        RobotSubConfigManager.getInstance(mContext).resetUserPackageList();
        List<String> localAppList = RobotSubConfigManager.getInstance(mContext).getUserPackageList();
        if (localAppList != null && localAppList.size()>0){
            for (int i = 0;i< localAppList.size();i++){
                ApplicationInfo applicationInfo = getLocalAppInfo(localAppList.get(i));
                if (applicationInfo == null){
                    RobotSubConfigManager.getInstance(mContext).removeUserPackage(localAppList.get(i));

                }
                if (isInThePackageList(localAppList.get(i))){
                    RobotSubConfigManager.getInstance(mContext).removeUserPackage(localAppList.get(i));
                }
            }

            RobotSubConfigManager.getInstance(mContext).commit();
        }

    }

    private void initPackageList() {
        robotPackageList.clear();
        robotPackageList.add(PackageConsts.TAKEPHOTO_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.ROBOT_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.LAUNCHER_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.AUTO_APP_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.LEX_CLASS_PACKAGE);
        robotPackageList.add(PackageConsts.SPEECH_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.ALARM_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.STOCK_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.WEATHER_PACKAGE_NAME);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_COUNT_DOWN);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_COMMEMORATION);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_WORDS);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_NEWS);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_MESSAGE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FANS);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_IDENT);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_CUSTOM);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_VIDEO_CALL);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_LAMP);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_REMINDER);
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_REMINDER);
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_SPECTRUM);
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_POMO);
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_OTA);
        robotPackageList.add(PackageConsts.PACKAGE_APP_NAME_MEDITATION);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_TIME);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_EXPRESSION);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_ALBUM);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_WIFI_CONNECTOR);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_SETTINGS);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_APP_STORE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FIST_PALM_GAME);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_VOICE_MEMO);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_MCU_SERVICE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_APHORISMS);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_RESOURCE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_VIDEO_PLAYER);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_DOWNLOADER);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GEEUI_MY_MUSIC);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_TASK_SERVICE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_DESKTOP);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FACTORY);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_LOCAL_DOWNLOADER);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_INSTALLER);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_GUIDE);
        robotPackageList.add(PackageConsts.PACKAGE_NAME_FANS_SHOW);
    }

    public boolean isInThePackageList(String packageName) {
        if (robotPackageList == null || robotPackageList.size() == 0){
            return false;
        }
        if (robotPackageList.contains(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isInUninstallPackageList(String packageName) {
        if (uninstallPackageList.contains(packageName)){
            return true;
        }else{
            return false;
        }
    }

    public void setScrollPosition(int scrollPosition) {
        if (scrollPosition != 0){
            this.scrollPosition = scrollPosition;
            Log.e("letianpai_list_view_update","============ RobotAppListManager_setScrollPosition_: "+ scrollPosition);
        }

    }

    public int getScrollPosition() {
        Log.e("letianpai_list_view_update","============ RobotAppListManager_setScrollPosition_: "+ scrollPosition);
        return scrollPosition;
    }

    public void cleanScrollPosition() {
        scrollPosition = 0;
    }
}
