package com.letianpai.robot.desktop.parser;

import java.util.Arrays;

public class RobotMenuData {
    private AppMenuInfo[] config_data;
    private String config_key;
    private String config_title;

    public AppMenuInfo[] getConfig_data() {
        return config_data;
    }

    public void setConfig_data(AppMenuInfo[] config_data) {
        this.config_data = config_data;
    }

    public String getConfig_key() {
        return config_key;
    }

    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    public String getConfig_title() {
        return config_title;
    }

    public void setConfig_title(String config_title) {
        this.config_title = config_title;
    }

    @Override
    public String toString() {
        return "{" +
                "config_data:" + Arrays.toString(config_data) +
                ", config_key:'" + config_key + '\'' +
                ", config_title:'" + config_title + '\'' +
                '}';
    }

    //
//    public AppMenuInfo[] getAppMenuInfo() {
//        return appMenuInfo;
//    }
//
//    public void setAppMenuInfo(AppMenuInfo[] appMenuInfo) {
//        this.appMenuInfo = appMenuInfo;
//    }
//
//    @Override
//    public String toString() {
//        return "RobotMenuData{" +
//                "appMenuInfo=" + Arrays.toString(appMenuInfo) +
//                '}';
//    }
}
