package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View.OnClickListener
import com.letianpai.robot.desktop.MainActivity
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.ui.base.LeftView

/**
 * @author liujunbin
 */
class RobotLeftView : LeftView {
    private var watchMainView: RobotMainView? = null

    //    private RobotExpressionView robotExpressionView;
    private var openSettings: CommitButton? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(view: RobotMainView?, context: Context?) : super(view, context) {
        this.watchMainView = view
    }

    override fun initView() {
        super.initView()
        inflate(mContext, R.layout.robot_left_view, this)
        //        robotExpressionView = findViewById(R.id.expression_view);
        openSettings = findViewById(R.id.openSettings)
        openSettings!!.setOnClickListener(OnClickListener { exitApp() })
    }

    private fun exitApp() {
        (mContext as MainActivity).finish()
        //        System.exit(0);
    }


    fun onHide() {
        Log.d("VoiceServiceView", "=== onHide()")

        //        if (game.getScreen() != null) {
//            ((BaseScreen) game.getScreen()).setViewShowing(false);
//            ((BaseScreen) game.getScreen()).onHide();
//        }
    }


    fun hideLeftView() {
        watchMainView!!.onFlitVoiceService()
    }


    override fun hideView() {
        super.hideView()
        watchMainView!!.onFlitVoiceService()
    }
}
