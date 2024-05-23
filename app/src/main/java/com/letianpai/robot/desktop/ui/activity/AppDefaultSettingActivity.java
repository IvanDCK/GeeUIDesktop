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
import com.letianpai.robot.desktop.ui.view.AppSettingsNoticeView;

import java.lang.ref.WeakReference;

public class AppDefaultSettingActivity extends AppCompatActivity {
    private AppSettingsNoticeView appSettingsNoticeView;
    private UpdateViewHandler updateViewHandler;
    private AppMenuInfo appMenuInfo;
    private static final int UPDATE_VIEW = 1;
    private int mode = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appMenuInfo = (AppMenuInfo) getIntent().getSerializableExtra("appInfo");
        mode = getIntent().getIntExtra("mode",0);
        setContentView(R.layout.activity_default);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        initView();
    }

    private void initView() {
        updateViewHandler = new UpdateViewHandler(AppDefaultSettingActivity.this);
        appSettingsNoticeView = findViewById(R.id.appSettingsNoticeView);
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
//        appModeSwitchView.updateIcon(appMenuInfo.getLocalIcon());
//        if (SystemUtil.isInChinese()){
//            appModeSwitchView.updateTitle(appMenuInfo.getName());
//        }else{
//            appModeSwitchView.updateTitle(appMenuInfo.getEn_name());
//        }


    }

    private void updateView() {
        Message message = new Message();
        message.what = UPDATE_VIEW;
        updateViewHandler.sendMessage(message);

    }

    public AppMenuInfo getAppMenuInfo() {
        return appMenuInfo;
    }

    public int getMode() {
        return mode;
    }

}
