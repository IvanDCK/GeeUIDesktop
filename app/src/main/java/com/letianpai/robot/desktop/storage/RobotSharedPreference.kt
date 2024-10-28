package com.letianpai.robot.desktop.storage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

/**
 * @author liujunbin
 */
class RobotSharedPreference(context: Context?, fileName: String?, action: String?) {
    private var mContext: Context? = null
    private var mFileName: String? = null

    /**
     * The system SharedPreferences object in use
     */
    private var mEditor: SharedPreferences.Editor? = null
    private var mSharedPref: SharedPreferences? = null

    /**
     * The id of the resource for which sharedpreference is used, default -1.
     */
    private var mMode = Context.MODE_PRIVATE or Context.MODE_MULTI_PROCESS

    /**
     * Map of in-memory data
     */
    private var mMap: MutableMap<String, Any?>? = null

    /**
     * Indicates whether the memory data has been changed or not, to avoid unnecessary write file operations
     */
    private var mHasChanged = false

    init {
        mContext = context

        mMode = Context.MODE_PRIVATE or Context.MODE_MULTI_PROCESS

        this.mFileName = fileName
        reloadSharedPref(false)
    }

    /**
     *  File manipulation, reloading configuration files
     *
     * @param syncIPCFlag true will notify all processes all reload, otherwise only load the calling process
     */
    fun reloadSharedPref(syncIPCFlag: Boolean) {
        mSharedPref = mContext!!.getSharedPreferences(mFileName, mMode)
        mEditor = mSharedPref!!.edit()
        mHasChanged = false
        reloadMap()

        if (syncIPCFlag) {
            //sendIPCSyncBroadcast();
            sendSettingChangeBroadcast()
        }
    }

    private fun sendSettingChangeBroadcast() {
        val intent = Intent(ACTION_INTENT_CONFIG_CHANGE)
        mContext!!.sendBroadcast(intent)
    }

    private fun sendMessageDelay(handleid: Int, delay: Long) {
    }

    fun reloadMap() {
        if (mMap != null) {
            mMap!!.clear()
        }
        mMap = mSharedPref!!.all as MutableMap<String, Any?>
    }

    val map: Map<String, Any?>?
        get() = this.mMap

    /**
     * Memory operations, releasing resources occupied by objects, cancelling broadcasts, clearing memory data
     */
    fun terminate() {
        try {
            // mContext.unregisterReceiver(mConfigChangeReceiver);
            // if (mMap != null) {
            // mMap.clear();
            // mMap = null;
            // }
        } catch (e: Exception) {
        }
    }

    /**
     * Determine if a Map contains the specified key
     *
     * @param key
     * @return boolean
     */
    fun contains(key: String): Boolean {
        return mMap!!.containsKey(key)
    }

    /**
     * file operation, submit data to the file, this function for the disk io write file, after the success of the data will notify the use of the file data data reload data
     * @return boolean true write file success; false write file failure
     */
    fun commit(): Boolean {
        if (!mHasChanged) {
            return false
        }
        if (mEditor != null) {
            if (mEditor!!.commit()) {
                mHasChanged = false
                sendMessageDelay(HANDLE_SETTING_CHANGED, DELAY_SEND_BROADCAST)
                //sendSettingChangeBroadcast();
                return true
            }
        }
        return false
    }

    /**
     *  In-memory operations to remove data containing a specific key
     *
     * @param key void
     */
    fun remove(key: String) {
        mEditor = mEditor!!.remove(key)
        mMap!!.remove(key)
        mHasChanged = true
    }

    /**
     *  Memory operations, clearing data
     *
     * @return boolean true success; false failure
     */
    fun clear(): Boolean {
        if (mEditor != null) {
            mEditor!!.clear()
            mMap!!.clear()
            mHasChanged = true
            return true
        }
        return false
    }

    /**
     *  Private public method, add data, value is object
     *
     * @param key
     * @param defValue
     * @return boolean true succeeds, false fails
     */
    private fun setValue(key: String, defValue: Any): Boolean {
        val preValue = mMap!!.put(key, defValue)
        if (preValue == null || preValue != defValue) {
            mHasChanged = true
            return true
        }
        return false
    }

    /**
     *  In-memory operation, add data, value is boolean
     *
     * @param key
     * @param defValue
     */
    fun putBoolean(key: String, defValue: Boolean) {
        if (setValue(key, defValue)) {
            mEditor = mEditor!!.putBoolean(key, defValue)
        }
    }

    /**
     *  In-memory operation, add data, value is Int
     *
     * @param key
     * @param defValue void
     */
    fun putInt(key: String, defValue: Int) {
        if (setValue(key, defValue)) {
            mEditor = mEditor!!.putInt(key, defValue)
        }
    }

    /**
     *  In-memory operation, add data, value is Long
     *
     * @param key
     * @param defValue void
     */
    fun putLong(key: String, defValue: Long) {
        if (setValue(key, defValue)) {
            mEditor = mEditor!!.putLong(key, defValue)
        }
    }

    /**
     *  In-memory operation, add data, value is Float
     *
     * @param key
     * @param defValue void
     */
    fun putFloat(key: String, defValue: Float) {
        if (setValue(key, defValue)) {
            mEditor = mEditor!!.putFloat(key, defValue)
        }
    }

    /**
     *  In-memory operation, add data, value is String
     *
     * @param key
     * @param defValue void
     */
    fun putString(key: String, defValue: String) {
        if (setValue(key, defValue)) {
            mEditor = mEditor!!.putString(key, defValue)
        }
    }

    /**
     *  Memory manipulation to get data of type boolean
     *
     * @param key
     * @param defValue 默认值
     * @return boolean
     */
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val v = mMap!![key] as Boolean?
        return v ?: defValue
    }

    /**
     *  Memory manipulation to get data of type float
     *
     * @param key
     * @param defValue 默认值
     * @return float
     */
    fun getFloat(key: String, defValue: Float): Float {
        val v = mMap!![key] as Float?
        return v ?: defValue
    }

    /**
     *  Memory manipulation to get data of type int
     *
     * @param key
     * @param defValue 默认值
     * @return int
     */
    fun getInt(key: String, defValue: Int): Int {
        val v = mMap!![key] as Int?
        return v ?: defValue
    }

    /**
     *  Memory manipulation to get data of type long
     *
     * @param key
     * @param defValue 默认值
     * @return long
     */
    fun getLong(key: String, defValue: Long): Long {
        val v = mMap!![key] as Long?
        return v ?: defValue
    }

    /**
     *  Memory manipulation to get data of type string
     *
     * @param key
     * @param defValue 默认值
     * @return String
     */
    fun getString(key: String, defValue: String?): String {
        val v = mMap!![key] as String?
        return v ?: defValue!!
    }

    companion object {
        const val UPDATE_TYPE_NONE: Int = 0

        /**
         * Broadcasting-related
         */
        const val ACTION_INTENT_CONFIG_CHANGE: String = "com.letianpai.robot.SETTING_CHANGE"


        const val SHARE_PREFERENCE_NAME: String = "RobotDesktopConfig"

        private const val HANDLE_SETTING_CHANGED = 10
        private const val DELAY_SEND_BROADCAST: Long = 200
    }
}
