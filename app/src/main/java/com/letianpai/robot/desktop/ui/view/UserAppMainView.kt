package com.letianpai.robot.desktop.ui.view

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback
import com.letianpai.robot.desktop.listener.AppKiller
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity
import com.letianpai.robot.desktop.utils.KeyConsts

class UserAppMainView : LinearLayout {
    private var mContext: Context? = null
    private var ivAppIcon: ImageView? = null
    private var appTitle: TextView? = null
    private var openUserApp: SettingsButton? = null
    private var unInstall: SettingsButton? = null
    private var backButton: BackButton? = null
    private val tag: String? = null
    private val packageName: String? = null

    constructor(context: Context) : super(context) {
        inits(context)
    }

    private fun inits(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.app_user_main, this)
        initViews()
        addListeners()
    }

    private fun addListeners() {
        openUserApp!!.setOnClickListener {
            if ((mContext as UserAppMainActivity).appMenuInfo != null && !TextUtils.isEmpty((mContext as UserAppMainActivity).appMenuInfo!!.packageName)) {
                val packageName = (mContext as UserAppMainActivity).appMenuInfo!!.packageName
                openUserAppView(packageName)
                (mContext as UserAppMainActivity).closeActivity()
            }
        }

        unInstall!!.setOnClickListener { (mContext as UserAppMainActivity).showConfirmView() }

        backButton!!.setOnClickListener { responseBack() }
    }

    private fun unInstallApp(packageName: String) {
        val intent = Intent()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val sender = PendingIntent.getActivity(mContext, 0, intent, FLAG_IMMUTABLE)
        val mPackageInstaller = mContext!!.packageManager.packageInstaller
        mPackageInstaller.uninstall(packageName, sender.intentSender) // 卸载APK
    }


    private fun unInstallApp1(packageName: String) {
        AppKiller.killApp(mContext!!, packageName, object : AppKiller.OnAppKilledListener {
            override fun onAppKilled(executionTime: Long) {
                Log.e("letianpai_app", "onAppKilled ============ 1 =============")
                unInstallApp(packageName)
            }
        })
    }

    //    private void openUserAppView(String packageName) {
    //        if (TextUtils.isEmpty(packageName)){
    //            return;
    //        }
    //        PackageManager packageManager = mContext.getPackageManager();
    //        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
    //
    //        if (intent != null) {
    //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            mContext.startActivity(intent);
    //        }
    //        OpenAppCallback.getInstance().openApp(packageName);
    //        responseBack();
    //    }
    private fun openUserAppView(packageName: String?) {
        Log.e("letianpai", "openUniversalApp:userAppsConfigModel ======= 1 ======")
        changeMode(packageName)
        OpenAppCallback.instance.openApp(packageName)
        responseBack()
    }

    fun changeMode(mode: String?) {
        if (!TextUtils.isEmpty(mode)) {
            val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
            ModeChangeCmdCallback.instance
                .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
        }
    }


    //    public void changeMode(AppMenuInfo appMenuInfo) {
    //        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.getMode())) {
    //            String mode = appMenuInfo.getMode();
    //            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
    //            Log.e("letianpai", "changeMode_data: " + data);
    //            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    //        }
    //    }
    private fun responseBack() {
        (mContext as UserAppMainActivity).finish()
    }

    private fun initViews() {
        ivAppIcon = findViewById(R.id.ivUserAppIcon)
        appTitle = findViewById(R.id.user_app_title)
        openUserApp = findViewById(R.id.openUserApp)
        unInstall = findViewById(R.id.unInstall)
        backButton = findViewById(R.id.back_user_app_main)
    }

    fun updateIcon(packageName: String?) {
        ivAppIcon!!.setImageDrawable(
            RobotAppListManager.getInstance(mContext!!).getLocalAppIcon(
                packageName!!
            )
        )
    }

    fun updateTitle(title: String?) {
        appTitle!!.text = title
    }

    fun updateUnInstall() {
        unInstall!!.setText(R.string.uninstalling)
        unInstall!!.isEnabled = false
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


    companion object {
        private const val SPLIT = "____"

        const val CHANGE_SHOW_MODE: String = "changeShowModule"
    }
}
