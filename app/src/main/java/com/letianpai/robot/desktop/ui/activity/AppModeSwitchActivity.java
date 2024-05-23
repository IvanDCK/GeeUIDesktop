package com.letianpai.robot.desktop.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.view.AppModeSwitchView;

import java.lang.ref.WeakReference;

public class AppModeSwitchActivity extends AppCompatActivity {
    private AppModeSwitchView appModeSwitchView;
    private UpdateViewHandler updateViewHandler;
    private AppMenuInfo appMenuInfo;
    private static final int UPDATE_VIEW = 1;
    private static final int CLOSE_ACTIVITY = 2;
    private int mode = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appMenuInfo = (AppMenuInfo) getIntent().getSerializableExtra("appInfo");
        mode = getIntent().getIntExtra("mode",0);
        setContentView(R.layout.activity_switch);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        initView();
        addListeners();
    }

    private void addListeners() {

    }

    private void initView() {
        updateViewHandler = new UpdateViewHandler(AppModeSwitchActivity.this);
        appModeSwitchView = findViewById(R.id.appModeSwitchView);
        updateView();
    }

    private void updateSwitchView() {
    }

    private class UpdateViewHandler extends Handler {
        private final WeakReference<Context> context;

        public UpdateViewHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPDATE_VIEW:
                    responseUpdateViews();
                    break;



            }
        }
    }

    private void responseUpdateViews() {
        appModeSwitchView.updateIcon(appMenuInfo.getLocalIcon());
        if (SystemUtil.isInChinese()){
            appModeSwitchView.updateTitle(appMenuInfo.getName());
        }else{
            appModeSwitchView.updateTitle(appMenuInfo.getEn_name());
        }

    }
    public void closeActivity() {
        Message message = new Message();
        message.what = CLOSE_ACTIVITY;
        updateViewHandler.sendMessageDelayed(message,2000);

    }

    private void updateView() {
        Message message = new Message();
        message.what = UPDATE_VIEW;
        updateViewHandler.sendMessage(message);

    }

//    public void changeMode(AppMenuInfo appMenuInfo) {
//        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.getMode())){
//            String mode = appMenuInfo.getMode();
//            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
//            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE,data);
//        }
//
//    }


    public AppMenuInfo getAppMenuInfo() {
        return appMenuInfo;
    }

    public int getMode() {
        return mode;
    }
}
