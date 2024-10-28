package com.letianpai.robot.desktop.ui.view

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity

class DeleteConfirmView : LinearLayout {
    private var mContext: Context? = null
    private var ivCancel: ImageView? = null
    private var ivConfirm: ImageView? = null
    private var ivBack: BackButton? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }


    private fun initView(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.app_confirm_view, this)
        initViews()
        addListeners()
    }

    private fun initViews() {
        ivConfirm = findViewById(R.id.iv_confirm)
        ivCancel = findViewById(R.id.iv_cancel)
        ivBack = findViewById(R.id.back_firm_view)
    }

    private fun addListeners() {
        ivConfirm!!.setOnClickListener { responseConfirm() }
        ivCancel!!.setOnClickListener { responseCancel() }
        ivBack!!.setOnClickListener { responseBack() }
    }

    private fun responseBack() {
        responseCancel()
    }

    private fun responseCancel() {
        (mContext as UserAppMainActivity).showUserView()
    }

    protected fun responseConfirm() {
        if ((mContext as UserAppMainActivity).appMenuInfo != null && !TextUtils.isEmpty((mContext as UserAppMainActivity).appMenuInfo!!.packageName)) {
            val packageName = (mContext as UserAppMainActivity).appMenuInfo!!.packageName
            unInstallApp(packageName!!)
            //            RobotAppListManager.getInstance(mContext).updateAppMenuList();
            AppMenuUpdateCallback.instance.setAppStatusUpdate(packageName, 1)
            (mContext as UserAppMainActivity).closeActivity1()
        }
    }

    private fun unInstallApp(packageName: String) {
        val intent = Intent()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val sender = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val mPackageInstaller = mContext!!.packageManager.packageInstaller
        mPackageInstaller.uninstall(packageName, sender.intentSender) // 卸载APK
    }
}
