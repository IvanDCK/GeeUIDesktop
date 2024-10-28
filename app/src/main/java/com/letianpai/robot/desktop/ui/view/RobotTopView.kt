package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.ui.base.AbstractMainView
import com.letianpai.robot.desktop.ui.base.BaseView
import com.letianpai.robot.desktop.ui.base.TopView

class RobotTopView : TopView {
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
        setShutDirect(DOWN_TO_UP)
        inflate(mContext, R.layout.robot_top_view, this)
    }
}
