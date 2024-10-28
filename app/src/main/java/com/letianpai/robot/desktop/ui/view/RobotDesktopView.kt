package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.graphics.Point
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import com.letianpai.robot.desktop.MainActivity
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback
import com.letianpai.robot.desktop.callback.OpenAppCallback.OpenAppCommandListener
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.ui.activity.AppListActivity
import com.letianpai.robot.desktop.ui.base.DesktopView
import com.letianpai.robot.desktop.utils.KeyConsts

class RobotDesktopView : DesktopView {
    private var mContext: Context? = null

    //    private SpineSkinView mSpineSkinView;
    //    private RobotExpressionView expressionView;
    //    private static String SKIN_PATH = "skin/skin_101";
    private var keyImageButton: KeyImageButton? = null
    private var appListVerticalView: AppListVerticalView? = null

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    override fun unregisterActivationReceiver() {
    }

    private fun init(context: Context?) {
        this.mContext = context
        //        inflate(mContext, R.layout.robot_desktop_view, this);
        inflate(mContext, R.layout.robot_control_view, this)
        initView()
        resizeView()
        loadClockView()
        addListeners()
    }

    private fun addListeners() {
        keyImageButton!!.setOnClickListener {
            Log.e("letianpai_test", "============= keyImageButton ===============")
            (mContext as AppListActivity).finish()
            //                if (((MainActivity) mContext).getRobotMode() == 11) {
            //                    Log.e("letianpai_", "changeMode_data:(MainActivity) mContext).getRobotMode()======== 11 ========: " + ((MainActivity) mContext).getRobotMode());
            //                    changeRobotMode();
            //                }else if (((MainActivity) mContext).getRobotMode() == 3){
            //                    changeStaticMode();
            //                }else if (((MainActivity) mContext).getRobotMode() == 5){
            //                    changeSleepMode();
            //                }
            //                System.exit(0);
        }

        OpenAppCallback.instance
            .setOpenAppListener { (mContext as AppListActivity).finish() }
    }

    fun changeRobotMode() {
//        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", "robot");
//        Log.e("letianpai", "changeMode_data: " + data);
//        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        changeMode("robot")
    }

    fun changeSleepMode() {
//        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", "sleep");
//        Log.e("letianpai", "changeMode_data: " + data);
//        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        changeMode("sleep")
    }

    fun changeMode(mode: String) {
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data)
    }

    fun changeStaticMode() {
        val packageName = (mContext as MainActivity).prePackageName
        if (TextUtils.isEmpty(packageName)) {
            return
        }
        val mode: String =
            RobotAppListManager.getInstance(mContext!!).getModeNameByPackage(packageName).toString()
        if (TextUtils.isEmpty(mode)) {
            return
        }
        val data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode)
        Log.e("letianpai_", "changeMode_data: $data")
        ModeChangeCmdCallback.instance
            .changeRobotMode(KeyConsts.Companion.CHANGE_SHOW_MODE, data)
    }

    private fun resizeView() {
    }

    private fun initView() {
//        mSpineSkinView = findViewById(R.id.ssv_view);
//        expressionView = findViewById(R.id.expression_view);
        keyImageButton = findViewById(R.id.ivBack)
        keyImageButton!!.setPressedImage(R.drawable.back_press)
        keyImageButton!!.setUnPressedImage(R.drawable.back_normal)
        keyImageButton!!.setImagePosition(KeyConsts.Companion.CENTER, 0, 0, 0, 0, 40, 40)
        appListVerticalView = findViewById(R.id.appListVerticalView)
    }

    private fun loadClockView() {
//        mSpineSkinView.loadSkin(SKIN_PATH);
    }


    fun onClick(pt: Point?) {
    }
}
