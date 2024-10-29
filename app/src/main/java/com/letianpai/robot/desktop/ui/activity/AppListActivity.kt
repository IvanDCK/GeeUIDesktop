package com.letianpai.robot.desktop.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.letianpai.robot.components.storage.RobotSubConfigManager
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.LifecycleChangedCallback
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.storage.RobotConfigConst
import com.letianpai.robot.desktop.storage.RobotConfigManager
import com.letianpai.robot.desktop.utils.KeyConsts

class AppListActivity : AppCompatActivity() {
    var prePackageName: String? = null
        private set
    var robotMode: Int = 0
        private set
    private var userAppListSize = 0
    private var updateTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
        val decorView = window.decorView
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        userAppListSize =
            RobotSubConfigManager.getInstance(this@AppListActivity)!!.userPackageListSize
        prePackageName = intent.getStringExtra("package")
        robotMode = intent.getIntExtra("mode", 0)
        LifecycleChangedCallback.instance
            .setLifecycle(LifecycleChangedCallback.ON_CREATE)
        Log.e("letianpai_list_view_update", "============ ON_CREATE ============")

        //        if (robotMode == 11 || robotMode == 3 || robotMode == 5 ) {
//            changeMode();
//        }
    }

    override fun onResume() {
        super.onResume()
        LifecycleChangedCallback.instance
            .setLifecycle(LifecycleChangedCallback.ON_RESUME)
        Log.e("letianpai_list_view_update", "============ ON_RESUME ============")
        //        if (RobotConfigManager.getInstance(AppListActivity.this).getAppListConfig().equals(RobotConfigManager.VALUE_DEFAULT_APP_LIST_CONFIG) || isNeedUpdate()){
        if (RobotConfigManager.getInstance(this@AppListActivity)
                !!.appListConfig == RobotConfigConst.VALUE_DEFAULT_APP_LIST_CONFIG
        ) {
            Log.e("letianpai_list_view_update", "============ ON_RESUME AAAAA ============")
            RobotAppListManager.Companion.getInstance(this@AppListActivity).updateAppMenuList()
            Log.e("letianpai_list_view_update", "============ ON_RESUME BBBBB ============")
        }
    }

    private val isNeedUpdate: Boolean
        get() {
            if ((System.currentTimeMillis() - updateTime) > 1000 * 60 * 30) {
                updateTime = System.currentTimeMillis()
                return true
            } else {
                return false
            }
        }

    //    private void startService() {
    //        Intent intent = new Intent(AppListActivity.this, GeeUIDesktopService.class);
    //        startService(intent);
    //    }
    override fun onDestroy() {
        super.onDestroy()
        LifecycleChangedCallback.instance
            .setLifecycle(LifecycleChangedCallback.ON_DESTROY)
        RobotAppListManager.getInstance(this@AppListActivity).cleanScrollPosition()
        Log.e("letianpai_list_view_update", "============ ON_DESTROY ============")
    }

    override fun onPause() {
        super.onPause()
        LifecycleChangedCallback.instance
            .setLifecycle(LifecycleChangedCallback.ON_PAUSE)
        Log.e("letianpai_list_view_update", "============ ON_PAUSE ============")
    }

    fun changeMode() {
        val mode = this@AppListActivity.packageName
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai_", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
    }

    companion object {
        const val COMMAND_VALUE_CHANGE_SLEEP: String = "sleep"
    }
}