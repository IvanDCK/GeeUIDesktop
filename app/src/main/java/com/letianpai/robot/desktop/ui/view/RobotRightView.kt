package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.ui.base.RightView

/**
 * @author liujunbin
 */
class RobotRightView : RightView {
    private var watchMainView: RobotMainView? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun updateData() {
    }

    constructor(view: RobotMainView?, context: Context?) : super(view, context) {
        this.watchMainView = view
    }


    override fun initView() {
        super.initView()
        inflate(mContext, R.layout.robot_right_view, this)
    }

    override fun downToUp() {
    }

    override fun upToDown() {
    }
}
