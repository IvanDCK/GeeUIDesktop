package com.letianpai.robot.desktop.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.ui.view.AppSettingsNoticeView
import java.lang.ref.WeakReference

class AppDefaultSettingActivity : AppCompatActivity() {
    private var appSettingsNoticeView: AppSettingsNoticeView? = null
    private var updateViewHandler: UpdateViewHandler? = null
    var appMenuInfo: AppMenuInfo? = null
        private set
    var mode: Int = 0
        private set


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appMenuInfo = intent.getSerializableExtra("appInfo") as AppMenuInfo?
        mode = intent.getIntExtra("mode", 0)
        setContentView(R.layout.activity_default)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        initView()
    }

    private fun initView() {
        updateViewHandler = UpdateViewHandler(
            this@AppDefaultSettingActivity
        )
        appSettingsNoticeView = findViewById(R.id.appSettingsNoticeView)
        updateView()
    }

    private fun updateSwitchView() {
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context =
            WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_VIEW -> responseUpdateViews()
            }
        }
    }

    private fun responseUpdateViews() {
//        appModeSwitchView.updateIcon(appMenuInfo.getLocalIcon());
//        if (SystemUtil.isInChinese()){
//            appModeSwitchView.updateTitle(appMenuInfo.getName());
//        }else{
//            appModeSwitchView.updateTitle(appMenuInfo.getEn_name());
//        }
    }

    private fun updateView() {
        val message = Message()
        message.what = UPDATE_VIEW
        updateViewHandler!!.sendMessage(message)
    }

    companion object {
        private const val UPDATE_VIEW = 1
    }
}
