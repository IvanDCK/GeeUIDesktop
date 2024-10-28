package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity

class AppSettingsNoticeView : RelativeLayout {
    private var mContext: Context? = null
    private var backButton: BackButton? = null
    private var enDes: TextView? = null
    private var zhDes: TextView? = null

    constructor(context: Context) : super(context) {
        inits(context)
    }

    private fun inits(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.app_setting_notice_view, this)
        initViews()
        setData()
        addListeners()
    }

    private fun setData() {
        if ((mContext as AppDefaultSettingActivity).appMenuInfo != null && !TextUtils.isEmpty((mContext as AppDefaultSettingActivity).appMenuInfo!!.enDes)
            && !TextUtils.isEmpty((mContext as AppDefaultSettingActivity).appMenuInfo!!.enDes)
        ) {
            enDes!!.text = (mContext as AppDefaultSettingActivity).appMenuInfo!!.enDes
            zhDes!!.text = (mContext as AppDefaultSettingActivity).appMenuInfo!!.zhDes
        } else {
            enDes!!.text = mContext!!.getText(R.string.default_en)
            zhDes!!.text = mContext!!.getText(R.string.default_zh)
        }
    }

    private fun addListeners() {
        backButton!!.setOnClickListener { responseBack() }
    }

    private fun setCurrentRobotMode() {
    }

    private fun openSettings() {
    }

    private fun responseBack() {
        (mContext as AppDefaultSettingActivity).finish()
    }

    private fun initViews() {
        backButton = findViewById(R.id.back)
        enDes = findViewById(R.id.en_des)
        zhDes = findViewById(R.id.zh_des)
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
}
