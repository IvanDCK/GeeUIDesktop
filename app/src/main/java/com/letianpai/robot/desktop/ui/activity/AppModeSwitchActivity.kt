package com.letianpai.robot.desktop.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.ui.view.AppModeSwitchView
import java.lang.ref.WeakReference

class AppModeSwitchActivity : AppCompatActivity() {
    private var appModeSwitchView: AppModeSwitchView? = null
    private var updateViewHandler: UpdateViewHandler? = null

    //    public void changeMode(AppMenuInfo appMenuInfo) {
    //        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.getMode())){
    //            String mode = appMenuInfo.getMode();
    //            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
    //            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE,data);
    //        }
    //
    //    }
    var appMenuInfo: AppMenuInfo? = null
        private set
    var mode: Int = 0
        private set

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appMenuInfo = intent.getSerializableExtra("appInfo") as AppMenuInfo?
        mode = intent.getIntExtra("mode", 0)
        setContentView(R.layout.activity_switch)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        initView()
        addListeners()
    }

    private fun addListeners() {
    }

    private fun initView() {
        updateViewHandler = UpdateViewHandler(
            this@AppModeSwitchActivity
        )
        appModeSwitchView = findViewById(R.id.appModeSwitchView)
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
        appModeSwitchView!!.updateIcon(appMenuInfo!!.localIcon)
        if (SystemUtil.isInChinese) {
            appModeSwitchView!!.updateTitle(appMenuInfo!!.name)
        } else {
            appModeSwitchView!!.updateTitle(appMenuInfo!!.en_name)
        }
    }

    fun closeActivity() {
        val message = Message()
        message.what = CLOSE_ACTIVITY
        updateViewHandler!!.sendMessageDelayed(message, 2000)
    }

    private fun updateView() {
        val message = Message()
        message.what = UPDATE_VIEW
        updateViewHandler!!.sendMessage(message)
    }


    companion object {
        private const val UPDATE_VIEW = 1
        private const val CLOSE_ACTIVITY = 2
    }
}
