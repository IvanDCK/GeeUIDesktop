package com.letianpai.robot.desktop.ui.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.desktop.callback.OpenAppCallback;
import com.letianpai.robot.desktop.listener.AppKiller;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity;
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity;
import com.letianpai.robot.desktop.utils.KeyConsts;

public class UserAppMainView extends LinearLayout {
    private Context mContext;
    private ImageView ivAppIcon;
    private TextView appTitle;
    private SettingsButton openUserApp;
    private SettingsButton unInstall;
    private BackButton backButton;
    private String tag;
    private String packageName;
    private static final String SPLIT = "____";

    public UserAppMainView(Context context) {
        super(context);
        inits(context);
    }

    private void inits(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_user_main, this);
        initViews();
        addListeners();

    }

    private void addListeners() {
        openUserApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((UserAppMainActivity) mContext).getAppMenuInfo() != null && !TextUtils.isEmpty(((UserAppMainActivity) mContext).getAppMenuInfo().getPackageName())) {
                    String packageName = ((UserAppMainActivity) mContext).getAppMenuInfo().getPackageName();
                    openUserAppView(packageName);
                    ((UserAppMainActivity) mContext).closeActivity();
                }
            }
        });

        unInstall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((UserAppMainActivity)mContext).showConfirmView();

            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseBack();

            }
        });

    }

    private void unInstallApp(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent sender = PendingIntent.getActivity(mContext, 0, intent, 0);
        PackageInstaller mPackageInstaller = mContext.getPackageManager().getPackageInstaller();
        mPackageInstaller.uninstall(packageName, sender.getIntentSender());// 卸载APK
    }


    private void unInstallApp1(String packageName) {
        AppKiller.killApp(mContext, packageName, new AppKiller.OnAppKilledListener() {
            @Override
            public void onAppKilled(long executionTime) {
                Log.e("letianpai_app","onAppKilled ============ 1 =============");
                unInstallApp(packageName);
            }
        });

    }

//    private void openUserAppView(String packageName) {
//        if (TextUtils.isEmpty(packageName)){
//            return;
//        }
//        PackageManager packageManager = mContext.getPackageManager();
//        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
//
//        if (intent != null) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        }
//        OpenAppCallback.getInstance().openApp(packageName);
//        responseBack();
//    }

    private void openUserAppView(String packageName) {
        Log.e("letianpai","openUniversalApp:userAppsConfigModel ======= 1 ======");
        changeMode(packageName);
        OpenAppCallback.getInstance().openApp(packageName);
        responseBack();
    }
    public static final String CHANGE_SHOW_MODE = "changeShowModule";
    public void changeMode(String mode) {
        if (!TextUtils.isEmpty(mode)) {
            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        }
    }


//    public void changeMode(AppMenuInfo appMenuInfo) {
//        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.getMode())) {
//            String mode = appMenuInfo.getMode();
//            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
//            Log.e("letianpai", "changeMode_data: " + data);
//            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
//        }
//    }

    private void responseBack() {
        ((UserAppMainActivity) mContext).finish();
    }

    private void initViews() {
        ivAppIcon = findViewById(R.id.ivUserAppIcon);
        appTitle = findViewById(R.id.user_app_title);
        openUserApp = findViewById(R.id.openUserApp);
        unInstall = findViewById(R.id.unInstall);
        backButton = findViewById(R.id.back_user_app_main);
    }

    public void updateIcon(String packageName) {
        ivAppIcon.setImageDrawable(RobotAppListManager.getInstance(mContext).getLocalAppIcon(packageName));
    }

    public void updateTitle(String title) {
        appTitle.setText(title);
    }
    public void updateUnInstall() {
        unInstall.setText(R.string.uninstalling);
        unInstall.setEnabled(false);
    }

    public UserAppMainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public UserAppMainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context);
    }

    public UserAppMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inits(context);
    }




}
