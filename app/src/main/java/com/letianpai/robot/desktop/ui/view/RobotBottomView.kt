package com.letianpai.robot.desktop.ui.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View.OnClickListener
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.ui.base.AbstractMainView
import com.letianpai.robot.desktop.ui.base.BaseView
import com.letianpai.robot.desktop.ui.base.BottomView

class RobotBottomView : BottomView {
    private var openSettings: CommitButton? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(view: AbstractMainView?, context: Context?) : super(view, context)

    override fun initView() {
        super.initView()
        setShutDirect(BaseView.Companion.UP_TO_DOWN)
        inflate(mContext, R.layout.robot_bottom_view, this)
        openSettings = findViewById(R.id.openSettings)
        openSettings!!.setOnClickListener(OnClickListener { openGeeUISettings() })
    }

    private fun openGeeUISettings() {
        val packageName = "com.robot.geeui.setting"
        val activityName = "com.robot.geeui.setting.MainActivity"
        val intent = Intent()
        intent.putExtra(OPEN_FROM, OPEN_FROM_TITLE)
        intent.setComponent(ComponentName(packageName, activityName))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mContext!!.startActivity(intent)
    }

    companion object {
        private const val OPEN_FROM = "from"
        private const val OPEN_FROM_TITLE = "from_title"
    }
}
