package com.letianpai.robot.desktop.parser

class RobotMenuData {
    lateinit var config_data: Array<AppMenuInfo>
    var config_key: String? = null
    var config_title: String? = null

    @JvmName("getConfigurationData")
    fun getConfig_data(): Array<AppMenuInfo> {
        return config_data
    }

    @JvmName("setConfigurationData")
    fun setConfig_data(config_data: Array<AppMenuInfo>) {
        this.config_data = config_data
    }

    override fun toString(): String {
        return "{" +
                "config_data:" + config_data.contentToString() +
                ", config_key:'" + config_key + '\'' +
                ", config_title:'" + config_title + '\'' +
                '}'
    } //
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
