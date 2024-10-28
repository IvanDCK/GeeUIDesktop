package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import com.letianpai.robot.desktop.ui.base.AbstractMainView

/**
 * @author liujunbin
 */
class RobotMainView : AbstractMainView {
    private var robotDesktopView: RobotDesktopView? = null
    private var robotTopView: RobotTopView? = null
    private var robotBottomView: RobotBottomView? = null
    private var robotLeftView: RobotLeftView? = null
    private var robotRightView: RobotRightView? = null


    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun responseChangedSpineSkin(skinPath: String?) {
    }

    override fun fillWatchViews() {
        robotDesktopView = RobotDesktopView(mContext)
        robotTopView = RobotTopView(mContext)
        robotLeftView = RobotLeftView(mContext)
        robotRightView = RobotRightView(mContext)
        robotBottomView = RobotBottomView(mContext)

        setDesktopView(robotDesktopView)
        setTopView(robotTopView)
        setLeftView(robotLeftView)
        //        setRightView(CommonAppListView.getInstance(mContext).getAppListView());
        setRightView(robotRightView)
        setBottomView(robotBottomView)

        setBottomViewDisable(true)
        setLeftViewDisable(true)
        setRightViewDisable(true)
        setTopViewDisable(true)
    }

    override fun responseOnClick(pt: Point?) {
        robotDesktopView!!.onClick(pt)
    }

    override fun responseLongPressOnWatchView() {
    }

    override fun updateData() {
    }
}
