package com.letianpai.robot.desktop.storage;

import android.content.Context;



/**
 * 机器人 偏好设置管理器
 * @author liujunbin
 */
public class RobotConfigManager implements RobotConfigConst {

    private static RobotConfigManager mRobotConfigManager;
    private RobotSharedPreference mRobotSharedPreference;
    private Context mContext;


    private RobotConfigManager(Context context) {
        this.mContext = context;
        this.mRobotSharedPreference = new RobotSharedPreference(context,
                RobotSharedPreference.SHARE_PREFERENCE_NAME, RobotSharedPreference.ACTION_INTENT_CONFIG_CHANGE);
    }


    private void initKidSmartConfigState() {

    }

    public static RobotConfigManager getInstance(Context context) {
        if (mRobotConfigManager == null) {
            mRobotConfigManager = new RobotConfigManager(context);
            mRobotConfigManager.initKidSmartConfigState();
            mRobotConfigManager.commit();
        }
        return mRobotConfigManager;

    }

    public boolean commit() {
        return mRobotSharedPreference.commit();
    }



    public void setAppListConfig(String appList){
        mRobotSharedPreference.putString(KEY_APP_LIST_CONFIG,appList);
    }

    public String getAppListConfig(){
        return mRobotSharedPreference.getString(KEY_APP_LIST_CONFIG,VALUE_DEFAULT_APP_LIST_CONFIG);
    }



}
