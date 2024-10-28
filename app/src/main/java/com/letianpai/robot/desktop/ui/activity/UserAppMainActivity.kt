package com.letianpai.robot.desktop.ui.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.AppStatusUpdateCallback
import com.letianpai.robot.desktop.callback.AppStatusUpdateCallback.AppStatusUpdateListener
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.ui.view.DeleteConfirmView
import com.letianpai.robot.desktop.ui.view.UserAppMainView
import java.lang.ref.WeakReference

class UserAppMainActivity : AppCompatActivity() {
    private var userAppMainView: UserAppMainView? = null
    private var deleteConfirmView: DeleteConfirmView? = null
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
    private var appStatusUpdateListener: AppStatusUpdateListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appMenuInfo = intent.getSerializableExtra("appInfo") as AppMenuInfo?
        mode = intent.getIntExtra("mode", 0)
        setContentView(R.layout.activity_user)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        initView()
        showUserView()
        addListeners()
    }

    private fun addListeners() {
        AppStatusUpdateCallback.instance
            .addAppStatusUpdateListener(appStatusUpdateListener)
    }

    private fun initView() {
        updateViewHandler = UpdateViewHandler(
            this@UserAppMainActivity
        )
        userAppMainView = findViewById(R.id.userAppMainView)
        deleteConfirmView = findViewById(R.id.deleteConfirmView)
        updateView()
        appStatusUpdateListener = object : AppStatusUpdateListener {
            override fun appInstallSuccess(packageName: String?, status: Int) {
            }

            override fun appUninstall(packageName: String, status: Int) {
                if (appMenuInfo != null && packageName == appMenuInfo!!.packageName) {
                    finish()
                }
            }
        }
    }

    private fun updateSwitchView() {
    }

    fun showUserView() {
        val message = Message()
        message.what = SHOW_USER_VIEW
        updateViewHandler!!.sendMessage(message)
    }

    fun showConfirmView() {
        val message = Message()
        message.what = SHOW_CONFIRM_VIEW
        updateViewHandler!!.sendMessage(message)
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context =
            WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_VIEW -> responseUpdateViews()
                CLOSE_ACTIVITY -> finish()
                UPDATE_UNINSTALL -> responseUnInstallView()
                SHOW_CONFIRM_VIEW -> responseShowConFirmView()
                SHOW_USER_VIEW -> responseShowUserView()
            }
        }
    }

    fun responseShowConFirmView() {
        userAppMainView!!.visibility = View.GONE
        deleteConfirmView!!.visibility = View.VISIBLE
    }

    fun responseShowUserView() {
        deleteConfirmView!!.visibility = View.GONE
        userAppMainView!!.visibility = View.VISIBLE
    }

    private fun responseUnInstallView() {
        userAppMainView!!.updateUnInstall()
    }

    private fun responseUpdateViews() {
        userAppMainView!!.updateIcon(appMenuInfo!!.packageName)
        if (SystemUtil.isInChinese) {
            userAppMainView!!.updateTitle(appMenuInfo!!.name)
        } else {
            userAppMainView!!.updateTitle(appMenuInfo!!.en_name)
        }
    }

    private fun updateView() {
        val message = Message()
        message.what = UPDATE_VIEW
        updateViewHandler!!.sendMessage(message)
    }

    fun updateUninstallView() {
        val message = Message()
        message.what = UPDATE_UNINSTALL
        updateViewHandler!!.sendMessage(message)
    }

    fun closeActivity() {
        val message = Message()
        message.what = CLOSE_ACTIVITY
        updateViewHandler!!.sendMessageDelayed(message, 2000)
    }

    fun closeActivity1() {
        val message = Message()
        message.what = CLOSE_ACTIVITY
        updateViewHandler!!.sendMessageDelayed(message, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppStatusUpdateCallback.instance
            .removeAppStatusUpdateListener(appStatusUpdateListener)
    }

    companion object {
        private const val UPDATE_VIEW = 1
        private const val CLOSE_ACTIVITY = 2
        private const val UPDATE_UNINSTALL = 3
        private const val SHOW_CONFIRM_VIEW = 5
        private const val SHOW_USER_VIEW = 6
    }
}
