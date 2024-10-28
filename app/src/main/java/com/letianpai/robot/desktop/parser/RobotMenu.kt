package com.letianpai.robot.desktop.parser

/**
 *
 */
class RobotMenu {
    var code: Int = 0
    var msg: String? = null
    var data: RobotMenuData? = null

    override fun toString(): String {
        return "{" +
                "code:" + code +
                ", msg:'" + msg + '\'' +
                ", data:" + data +
                '}'
    }
}
