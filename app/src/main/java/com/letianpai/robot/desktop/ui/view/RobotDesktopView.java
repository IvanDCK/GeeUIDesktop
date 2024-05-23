package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.letianpai.robot.desktop.MainActivity;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.desktop.callback.OpenAppCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.ui.activity.AppListActivity;
import com.letianpai.robot.desktop.ui.base.DesktopView;
import com.letianpai.robot.desktop.utils.KeyConsts;

public class RobotDesktopView extends DesktopView {
    private Context mContext;
//    private SpineSkinView mSpineSkinView;
//    private RobotExpressionView expressionView;
//    private static String SKIN_PATH = "skin/skin_101";
    private KeyImageButton keyImageButton;
    private AppListVerticalView appListVerticalView;

    public RobotDesktopView(Context context) {
        super(context);
        init(context);
    }

    public RobotDesktopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RobotDesktopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void unregisterActivationReceiver() {

    }

    private void init(Context context) {
        this.mContext = context;
//        inflate(mContext, R.layout.robot_desktop_view, this);
        inflate(mContext, R.layout.robot_control_view, this);
        initView();
        resizeView();
        loadClockView();
        addListeners();

    }

    private void addListeners() {
        keyImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("letianpai_test","============= keyImageButton ===============");
                ((AppListActivity)mContext).finish();
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
        });

        OpenAppCallback.getInstance().setOpenAppListener(new OpenAppCallback.OpenAppCommandListener() {
            @Override
            public void onResponseOpenApp(String command) {
                ((AppListActivity)mContext).finish();
            }
        });
    }

    public void changeRobotMode() {
//        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", "robot");
//        Log.e("letianpai", "changeMode_data: " + data);
//        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        changeMode("robot");
    }

    public void changeSleepMode() {
//        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", "sleep");
//        Log.e("letianpai", "changeMode_data: " + data);
//        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        changeMode("sleep");
    }

    public void changeMode(String mode) {
        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
        Log.e("letianpai", "changeMode_data: " + data);
        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    }

    public void changeStaticMode() {
        String packageName = ((MainActivity)mContext).getPrePackageName();
        if (TextUtils.isEmpty(packageName)){
            return;
        }
        String mode = RobotAppListManager.getInstance(mContext).getModeNameByPackage(packageName);
        if (TextUtils.isEmpty(mode)){
            return;
        }
        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
        Log.e("letianpai_", "changeMode_data: " + data);
        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    }

    private void resizeView() {
    }

    private void initView() {
//        mSpineSkinView = findViewById(R.id.ssv_view);
//        expressionView = findViewById(R.id.expression_view);
        keyImageButton = findViewById(R.id.ivBack);
        keyImageButton.setPressedImage(R.drawable.back_press);
        keyImageButton.setUnPressedImage(R.drawable.back_normal);
        keyImageButton.setImagePosition(KeyConsts.CENTER,0,0,0,0,40,40);
        appListVerticalView = findViewById(R.id.appListVerticalView);
    }

    private void loadClockView() {
//        mSpineSkinView.loadSkin(SKIN_PATH);
    }


    public void onClick(Point pt) {

    }

}
