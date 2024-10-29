package com.letianpai.robot.desktop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.letianpai.robot.components.network.nets.GeeUINetResponseManager
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.service.GeeUIDesktopService
import com.letianpai.robot.desktop.ui.view.LetianpaiMainView
import com.letianpai.robot.desktop.utils.KeyConsts

class MainActivity : AppCompatActivity() {
    var prePackageName: String? = null
        private set
    var robotMode: Int = 0
        private set

    private var main_view: LetianpaiMainView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        prePackageName = intent.getStringExtra("package")
        robotMode = intent.getIntExtra("mode", 0)
        RobotAppListManager.getInstance(this@MainActivity).resetLocalUserPackageList()
        main_view = findViewById(R.id.main_view)
        startService()
        if (robotMode == 11 || robotMode == 3 || robotMode == 5) {
            changeMode()
        }
    }

    override fun onResume() {
        super.onResume()
        RobotAppListManager.getInstance(this@MainActivity).updateAppMenuList()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0) // 禁用关闭动画
    }

    private fun startService() {
        val intent = Intent(this@MainActivity, GeeUIDesktopService::class.java)
        startService(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("ZG", "---getLOGO----")
        main_view!!.imageUrl
    }

    private val lOGO: Unit
        /****
         * 首页切换到桌面页，需要主动请求LOGO
         */
        get() {
            GeeUINetResponseManager.getInstance(this)!!.logoInfo
        }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun changeMode() {
        val mode = this@MainActivity.packageName
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai_", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
    }

    companion object {
        const val COMMAND_VALUE_CHANGE_SLEEP: String = "sleep"
    }
}