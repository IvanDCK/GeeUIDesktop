package com.letianpai.robot.desktop.apps.adapter;

import android.graphics.Bitmap;

import java.util.Objects;

/**
 * @author liujunbin
 */
public class AppListItem {
    public static final int APP_START_PACKAGE = 0;
    public static final int APP_START_ACTION = 1;

    private int appIcon;            //图标；
    private int appNameTextColor;   //生成文字颜色；
    private String appName;         //应用名字；
    private String appPackageName;  //包名；
    private String appTag;          //程序Tag；
    private String appDescription;  //程序描述
    private Bitmap appBitmap;       //
    private int appIconSmall;            //图标；
    private Bitmap appBitmapSmall;       //
    private int appBg;
    private int startType;          //应用启动的方式
    private String startValue;         //应用启动的相关值

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public String getAppTag() {
        return appTag;
    }

    public void setAppTag(String appTag) {
        this.appTag = appTag;
    }

    public void setAppNameTextColor(int appNameTextColor) {
        this.appNameTextColor = appNameTextColor;
    }

    public int getAppNameTextColor() {
        return appNameTextColor;
    }


    public Bitmap getAppBitmap() {
        return appBitmap;
    }

    public void setAppBitmap(Bitmap appBitmap) {
        this.appBitmap = appBitmap;
    }

    public void setAppIconSmall(int appIconSmall) {
        this.appIconSmall = appIconSmall;
    }

    public int getAppIconSmall() {
        return appIconSmall;
    }

    public Bitmap getAppBitmapSmall() {
        return appBitmapSmall;
    }

    public void setAppBitmapSmall(Bitmap appBitmapSmall) {
        this.appBitmapSmall = appBitmapSmall;
    }

    public int getAppBg() {
        return appBg;
    }

    public void setAppBg(int appBg) {
        this.appBg = appBg;
    }

    public int getStartType() {
        return startType;
    }

    public void setStartType(int startType) {
        this.startType = startType;
    }

    public String getStartValue() {
        return startValue;
    }

    public void setStartValue(String startValue) {
        this.startValue = startValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppListItem that = (AppListItem) o;
        return appPackageName.equals(that.appPackageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appPackageName);
    }

    @Override
    public String toString() {
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
                '}';
    }
}
