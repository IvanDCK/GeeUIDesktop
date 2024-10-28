package com.letianpai.robot.desktop.ui.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity
import com.letianpai.robot.desktop.utils.KeyConsts

class AppModeSwitchView : LinearLayout {
    private var mContext: Context? = null
    private var ivAppIcon: ImageView? = null
    private var appTitle: TextView? = null
    private var setCurrentMode: SettingsButton? = null
    private var settings: SettingsButton? = null
    private var backButton: BackButton? = null
    private val tag: String? = null
    private val packageName: String? = null

    constructor(context: Context) : super(context) {
        inits(context)
    }

    private fun inits(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.app_mode_switch, this)
        initViews()
        addListeners()
    }

    private fun addListeners() {
        setCurrentMode!!.setOnClickListener { setCurrentRobotMode() }

        settings!!.setOnClickListener {
            if ((mContext as AppModeSwitchActivity).appMenuInfo != null && !TextUtils.isEmpty((mContext as AppModeSwitchActivity).appMenuInfo!!.settings)
                && !TextUtils.isEmpty((mContext as AppModeSwitchActivity).appMenuInfo!!.packageName)
            ) {
                val settings = (mContext as AppModeSwitchActivity).appMenuInfo!!.settings
                //                    settings = "com.letianpai.robot.downloader_||_com.letianpai.robot.downloader.ui.activity.MainActivity";
                if (settings!!.contains(SPLIT)) {
                    val sSettings =
                        settings.split(SPLIT.toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (sSettings != null && sSettings.size >= 2) {
                        //                            openAppSettings(sSettings[0], sSettings[1]);
                        changeMode(sSettings[0], sSettings[1])
                    } else {
                        openDefaultSettings()
                    }
                } else {
                    //                        openAppSettings(((AppModeSwitchActivity) mContext).getAppMenuInfo().getPackageName(), ((AppModeSwitchActivity) mContext).getAppMenuInfo().getSettings());
                    changeMode(
                        (mContext as AppModeSwitchActivity).appMenuInfo!!.packageName,
                        (mContext as AppModeSwitchActivity).appMenuInfo!!.settings
                    )
                }
            } else {
                openDefaultSettings()
                //                    String settings = "com.letianpai.robot.downloader____com.letianpai.robot.downloader.ui.activity.MainActivity";
                //                    Log.e("letianpai_settings","sSettings[0]_0"+ settings);
                //                    if (settings.contains(SPLIT)){
                //                        String [] sSettings = settings.split(SPLIT);
                //                        Log.e("letianpai_settings","sSettings[0]:"+ sSettings[0]);
                //                        Log.e("letianpai_settings","sSettings[1]:"+ sSettings[1]);
                //                        if (sSettings!= null && sSettings.length >=2){
                //                            openAppSettings(sSettings[0], sSettings[1]);
                //                        }
                //                    }
            }
            //
            //                String settings = "com.letianpai.robot.downloader____com.letianpai.robot.downloader.ui.activity.MainActivity";
            //                Log.e("letianpai_settings","sSettings[0]_0"+ settings);
            //                if (settings.contains(SPLIT)){
            //                    String [] sSettings = settings.split(SPLIT);
            //                    Log.e("letianpai_settings","sSettings[0]:"+ sSettings[0]);
            //                    Log.e("letianpai_settings","sSettings[1]:"+ sSettings[1]);
            //                    if (sSettings!= null && sSettings.length >=2){
            //                        openAppSettings(sSettings[0], sSettings[1]);
            //                    }
            //                }
        }

        backButton!!.setOnClickListener { responseBack() }
    }

    private fun setCurrentRobotMode() {
        changeMode((mContext as AppModeSwitchActivity).appMenuInfo)
        OpenAppCallback.instance
            .openApp((mContext as AppModeSwitchActivity).appMenuInfo!!.packageName)
        responseBack()
    }

    fun changeMode(appMenuInfo: AppMenuInfo?) {
        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.mode)) {
            val mode = appMenuInfo.mode
            val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
            Log.e("letianpai", "changeMode_data: $data")
            ModeChangeCmdCallback.instance
                .changeRobotMode(KeyConsts.Companion.CHANGE_SHOW_MODE, data)
        }
    }

    fun changeMode(packageName: String?, activityName: String?) {
        val mode = packageName + SPLIT + activityName
        if (!TextUtils.isEmpty(mode)) {
            val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
            ModeChangeCmdCallback.instance
                .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
            OpenAppCallback.instance.openApp(packageName)
            responseBack()
        }
    }

    private fun responseBack() {
        (mContext as AppModeSwitchActivity).finish()
    }

    private fun responseBackDelay() {
        Handler().postDelayed({ (mContext as AppModeSwitchActivity).finish() }, 2000)
    }

    private fun initViews() {
        ivAppIcon = findViewById(R.id.ivAppIcon)
        appTitle = findViewById(R.id.app_title)
        setCurrentMode = findViewById(R.id.setCurrentMode)
        settings = findViewById(R.id.settings)
        backButton = findViewById(R.id.back)
    }

    fun updateIcon(icon: Int) {
        ivAppIcon!!.setImageResource(icon)
    }

    fun updateTitle(title: String?) {
        appTitle!!.text = title
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inits(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        inits(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        inits(context)
    }

    private fun openDefaultSettings() {
        val intent = Intent(mContext, AppDefaultSettingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mContext!!.startActivity(intent)
    }

    private fun openAppSettings(packageName: String, activityName: String) {
        OpenAppCallback.instance.openApp(packageName)
        responseBackDelay()

        try {
            val intent = Intent()
            intent.setComponent(ComponentName(packageName, activityName))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            mContext!!.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {
        private const val SPLIT = "____"
    }
}
