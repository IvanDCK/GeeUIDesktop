package com.letianpai.robot.desktop.parser;

/**
 *
 */
public class RobotMenu {
    private int code;
    private String msg;
    private RobotMenuData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RobotMenuData getData() {
        return data;
    }

    public void setData(RobotMenuData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "code:" + code +
                ", msg:'" + msg + '\'' +
                ", data:" + data +
                '}';
    }
}
