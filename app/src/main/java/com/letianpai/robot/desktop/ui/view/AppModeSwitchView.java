package com.letianpai.robot.desktop.ui.view;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity;
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity;
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity;
import com.letianpai.robot.desktop.utils.KeyConsts;

public class AppModeSwitchView extends LinearLayout {
    private Context mContext;
    private ImageView ivAppIcon;
    private TextView appTitle;
    private SettingsButton setCurrentMode;
    private SettingsButton settings;
    private BackButton backButton;
    private String tag;
    private String packageName;
    private static final String SPLIT = "____";

    public AppModeSwitchView(Context context) {
        super(context);
        inits(context);
    }

    private void inits(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_mode_switch, this);
        initViews();
        addListeners();

    }

    private void addListeners() {
        setCurrentMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentRobotMode();

            }
        });

        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((AppModeSwitchActivity) mContext).getAppMenuInfo() != null && !TextUtils.isEmpty(((AppModeSwitchActivity) mContext).getAppMenuInfo().getSettings())
                        && !TextUtils.isEmpty(((AppModeSwitchActivity) mContext).getAppMenuInfo().getPackageName())) {
                    String settings = ((AppModeSwitchActivity) mContext).getAppMenuInfo().getSettings();
//                    settings = "com.letianpai.robot.downloader_||_com.letianpai.robot.downloader.ui.activity.MainActivity";
                    if (settings.contains(SPLIT)) {
                        String[] sSettings = settings.split(SPLIT);
                        if (sSettings != null && sSettings.length >= 2) {
//                            openAppSettings(sSettings[0], sSettings[1]);
                            changeMode(sSettings[0], sSettings[1]);

                        }else{
                            openDefaultSettings();
                        }
                    } else {
//                        openAppSettings(((AppModeSwitchActivity) mContext).getAppMenuInfo().getPackageName(), ((AppModeSwitchActivity) mContext).getAppMenuInfo().getSettings());
                        changeMode(((AppModeSwitchActivity) mContext).getAppMenuInfo().getPackageName(), ((AppModeSwitchActivity) mContext).getAppMenuInfo().getSettings());
                    }

                } else {
                    openDefaultSettings();
//                    String settings = "com.letianpai.robot.downloader____com.letianpai.robot.downloader.ui.activity.MainActivity";
//                    Log.e("letianpai_settings","sSettings[0]_0"+ settings);
//                    if (settings.contains(SPLIT)){
//                        String [] sSettings = settings.split(SPLIT);
//                        Log.e("letianpai_settings","sSettings[0]:"+ sSettings[0]);
//                        Log.e("letianpai_settings","sSettings[1]:"+ sSettings[1]);
//                        if (sSettings!= null && sSettings.length >=2){
//                            openAppSettings(sSettings[0], sSettings[1]);
//                        }
//                    }
                }

//
//                String settings = "com.letianpai.robot.downloader____com.letianpai.robot.downloader.ui.activity.MainActivity";
//                Log.e("letianpai_settings","sSettings[0]_0"+ settings);
//                if (settings.contains(SPLIT)){
//                    String [] sSettings = settings.split(SPLIT);
//                    Log.e("letianpai_settings","sSettings[0]:"+ sSettings[0]);
//                    Log.e("letianpai_settings","sSettings[1]:"+ sSettings[1]);
//                    if (sSettings!= null && sSettings.length >=2){
//                        openAppSettings(sSettings[0], sSettings[1]);
//                    }
//                }

            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseBack();
            }
        });

    }

    private void setCurrentRobotMode() {
        changeMode(((AppModeSwitchActivity) mContext).getAppMenuInfo());
        OpenAppCallback.getInstance().openApp(((AppModeSwitchActivity) mContext).getAppMenuInfo().getPackageName());
        responseBack();
    }

    public void changeMode(AppMenuInfo appMenuInfo) {
        if (appMenuInfo != null && !TextUtils.isEmpty(appMenuInfo.getMode())) {
            String mode = appMenuInfo.getMode();
            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
            Log.e("letianpai", "changeMode_data: " + data);
            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
        }
    }

    public void changeMode(String packageName, String activityName) {
        String mode = packageName + SPLIT + activityName;
        if (!TextUtils.isEmpty(mode)) {
            String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
            ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
            OpenAppCallback.getInstance().openApp(packageName);
            responseBack();
        }
    }

    private void responseBack() {
        ((AppModeSwitchActivity) mContext).finish();
    }
    private void responseBackDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((AppModeSwitchActivity) mContext).finish();
            }
        },2000);
    }

    private void initViews() {
        ivAppIcon = findViewById(R.id.ivAppIcon);
        appTitle = findViewById(R.id.app_title);
        setCurrentMode = findViewById(R.id.setCurrentMode);
        settings = findViewById(R.id.settings);
        backButton = findViewById(R.id.back);
    }

    public void updateIcon(int icon) {
        ivAppIcon.setImageResource(icon);
    }

    public void updateTitle(String title) {
        appTitle.setText(title);
    }

    public AppModeSwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public AppModeSwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context);
    }

    public AppModeSwitchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inits(context);
    }

    private void openDefaultSettings() {
        Intent intent = new Intent(mContext, AppDefaultSettingActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }

    private void openAppSettings(String packageName, String activityName) {
        OpenAppCallback.getInstance().openApp(packageName);
        responseBackDelay();

        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, activityName));
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
