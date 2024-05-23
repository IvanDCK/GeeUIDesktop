package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity;
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity;

public class AppSettingsNoticeView extends RelativeLayout {
    private Context mContext;
    private BackButton backButton;
    private TextView enDes;
    private TextView zhDes;

    public AppSettingsNoticeView(Context context) {
        super(context);
        inits(context);
    }

    private void inits(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_setting_notice_view,this);
        initViews();
        setData();
        addListeners();

    }

    private void setData() {
        if (((AppDefaultSettingActivity) mContext).getAppMenuInfo() != null && !TextUtils.isEmpty(((AppDefaultSettingActivity) mContext).getAppMenuInfo().getEnDes())
                &&  !TextUtils.isEmpty(((AppDefaultSettingActivity) mContext).getAppMenuInfo().getEnDes())) {
            enDes.setText( ((AppDefaultSettingActivity) mContext).getAppMenuInfo().getEnDes());
            zhDes.setText( ((AppDefaultSettingActivity) mContext).getAppMenuInfo().getZhDes());

        } else {
            enDes.setText( mContext.getText(R.string.default_en));
            zhDes.setText( mContext.getText(R.string.default_zh));
        }
    }

    private void addListeners() {

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseBack();
            }
        });

    }

    private void setCurrentRobotMode() {

    }

    private void openSettings() {

    }

    private void responseBack() {
        ((AppDefaultSettingActivity)mContext).finish();
    }

    private void initViews() {
        backButton = findViewById(R.id.back);
        enDes = findViewById(R.id.en_des);
        zhDes = findViewById(R.id.zh_des);
    }

    public AppSettingsNoticeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public AppSettingsNoticeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context);
    }

    public AppSettingsNoticeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inits(context);
    }
}
