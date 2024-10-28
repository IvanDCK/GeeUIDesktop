package com.letianpai.robot.desktop.storage

import android.content.Context

/**
 * Robotics Preference Manager
 * @author liujunbin
 */
class RobotConfigManager private constructor(private val mContext: Context) : RobotConfigConst {
    private val mRobotSharedPreference =
        RobotSharedPreference(
            mContext,
            RobotSharedPreference.SHARE_PREFERENCE_NAME,
            RobotSharedPreference.ACTION_INTENT_CONFIG_CHANGE
        )


    private fun initKidSmartConfigState() {
    }

    fun commit(): Boolean {
        return mRobotSharedPreference.commit()
    }


    var appListConfig: String?
        get() = mRobotSharedPreference.getString(
            RobotConfigConst.KEY_APP_LIST_CONFIG,
            RobotConfigConst.VALUE_DEFAULT_APP_LIST_CONFIG
        )
        set(appList) {
            mRobotSharedPreference.putString(
                RobotConfigConst.KEY_APP_LIST_CONFIG,
                appList!!
            )
        }


    companion object {
        private var mRobotConfigManager: RobotConfigManager? = null
        fun getInstance(context: Context): RobotConfigManager? {
            if (mRobotConfigManager == null) {
                mRobotConfigManager = RobotConfigManager(context)
                mRobotConfigManager!!.initKidSmartConfigState()
                mRobotConfigManager!!.commit()
            }
            return mRobotConfigManager
        }
    }
}
