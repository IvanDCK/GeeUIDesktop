package com.letianpai.robot.desktop.parser;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppMenuInfo implements Serializable {
    private String name;
    private String en_name;
    private String icon;
    private Drawable drawableIcon;
    private String openAddress;
    private int openType;
    private String packageName;
    private String mode;
    private String version;
    private String settings;
    private int localIcon;
    private String enDes;
    private String zhDes;
    private int appStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenAddress() {
        return openAddress;
    }

    public void setOpenAddress(String openAddress) {
        this.openAddress = openAddress;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public int getLocalIcon() {
        return localIcon;
    }

    public void setLocalIcon(int localIcon) {
        this.localIcon = localIcon;
    }


    public String getEnDes() {
        return enDes;
    }

    public void setEnDes(String enDes) {
        this.enDes = enDes;
    }

    public String getZhDes() {
        return zhDes;
    }

    public void setZhDes(String zhDes) {
        this.zhDes = zhDes;
    }

    public Drawable getDrawableIcon() {
        return drawableIcon;
    }

    public void setDrawableIcon(Drawable drawableIcon) {
        this.drawableIcon = drawableIcon;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }

    @Override
    public String toString() {
        return "AppMenuInfo{" +
                "name='" + name + '\'' +
                ", en_name='" + en_name + '\'' +
                ", icon='" + icon + '\'' +
                ", drawableIcon=" + drawableIcon +
                ", openAddress='" + openAddress + '\'' +
                ", openType=" + openType +
                ", packageName='" + packageName + '\'' +
                ", mode='" + mode + '\'' +
                ", version='" + version + '\'' +
                ", settings='" + settings + '\'' +
                ", localIcon=" + localIcon +
                ", enDes='" + enDes + '\'' +
                ", zhDes='" + zhDes + '\'' +
                ", appStatus=" + appStatus +
                '}';
    }
}
