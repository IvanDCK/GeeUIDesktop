package com.letianpai.robot.desktop.parser

import android.graphics.drawable.Drawable
import java.io.Serializable

class AppMenuInfo : Serializable {
    var name: String? = null
    var en_name: String? = null
    var icon: String? = null
    var drawableIcon: Drawable? = null
    var openAddress: String? = null
    var openType: Int = 0
    var packageName: String? = null
    var mode: String? = null
    var version: String? = null
    var settings: String? = null
    var localIcon: Int = 0
    var enDes: String? = null
    var zhDes: String? = null
    var appStatus: Int = 0


    override fun toString(): String {
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
                '}'
    }
}
