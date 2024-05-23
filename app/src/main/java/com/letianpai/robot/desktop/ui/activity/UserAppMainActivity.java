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
import com.letianpai.robot.desktop.callback.AppStatusUpdateCallback;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.view.DeleteConfirmView;
import com.letianpai.robot.desktop.ui.view.UserAppMainView;

import java.lang.ref.WeakReference;

public class UserAppMainActivity extends AppCompatActivity {
    private UserAppMainView userAppMainView;
    private DeleteConfirmView deleteConfirmView;
    private UpdateViewHandler updateViewHandler;
    private AppMenuInfo appMenuInfo;
    private static final int UPDATE_VIEW = 1;
    private static final int CLOSE_ACTIVITY = 2;
    private static final int UPDATE_UNINSTALL = 3;
    private static final int SHOW_CONFIRM_VIEW = 5;
    private static final int SHOW_USER_VIEW = 6;
    private int mode = 0;
    private AppStatusUpdateCallback.AppStatusUpdateListener appStatusUpdateListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appMenuInfo = (AppMenuInfo) getIntent().getSerializableExtra("appInfo");
        mode = getIntent().getIntExtra("mode",0);
        setContentView(R.layout.activity_user);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        initView();
        showUserView();
        addListeners();
    }

    private void addListeners() {
        AppStatusUpdateCallback.getInstance().addAppStatusUpdateListener(appStatusUpdateListener);
    }

    private void initView() {
        updateViewHandler = new UpdateViewHandler(UserAppMainActivity.this);
        userAppMainView = findViewById(R.id.userAppMainView);
        deleteConfirmView = findViewById(R.id.deleteConfirmView);
        updateView();
        appStatusUpdateListener = new AppStatusUpdateCallback.AppStatusUpdateListener() {
            @Override
            public void appInstallSuccess(String packageName, int status) {

            }

            @Override
            public void appUninstall(String packageName, int status) {

                if (appMenuInfo != null && packageName.equals(appMenuInfo.getPackageName())){
                    finish();
                }


            }
        };
    }

    private void updateSwitchView() {
    }

    public void showUserView() {
        Message message = new Message();
        message.what = SHOW_USER_VIEW;
        updateViewHandler.sendMessage(message);
    }
    public void showConfirmView() {
        Message message = new Message();
        message.what = SHOW_CONFIRM_VIEW;
        updateViewHandler.sendMessage(message);
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
                case CLOSE_ACTIVITY:
                    finish();
                    break;
                case UPDATE_UNINSTALL:
                    responseUnInstallView();
                    break;
                case SHOW_CONFIRM_VIEW:
                    responseShowConFirmView();
                    break;
                case SHOW_USER_VIEW:
                    responseShowUserView();
                    break;

            }
        }
    }

    public void responseShowConFirmView() {
        userAppMainView.setVisibility(View.GONE);
        deleteConfirmView.setVisibility(View.VISIBLE);

    }

    public void responseShowUserView() {
        deleteConfirmView.setVisibility(View.GONE);
        userAppMainView.setVisibility(View.VISIBLE);
    }

    private void responseUnInstallView() {
        userAppMainView.updateUnInstall();
    }

    private void responseUpdateViews() {
        userAppMainView.updateIcon(appMenuInfo.getPackageName());
        if (SystemUtil.isInChinese()){
            userAppMainView.updateTitle(appMenuInfo.getName());
        }else{
            userAppMainView.updateTitle(appMenuInfo.getEn_name());
        }

    }

    private void updateView() {
        Message message = new Message();
        message.what = UPDATE_VIEW;
        updateViewHandler.sendMessage(message);

    }
    public void updateUninstallView() {
        Message message = new Message();
        message.what = UPDATE_UNINSTALL;
        updateViewHandler.sendMessage(message);

    }

    public void closeActivity() {
        Message message = new Message();
        message.what = CLOSE_ACTIVITY;
        updateViewHandler.sendMessageDelayed(message,2000);
    }
    public void closeActivity1() {
        Message message = new Message();
        message.what = CLOSE_ACTIVITY;
        updateViewHandler.sendMessageDelayed(message,500);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppStatusUpdateCallback.getInstance().removeAppStatusUpdateListener(appStatusUpdateListener);;
    }
}
