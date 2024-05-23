package com.letianpai.robot.desktop.ui.view;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.ui.base.AbstractMainView;
import com.letianpai.robot.desktop.ui.base.BottomView;


public class RobotBottomView extends BottomView {
    private CommitButton openSettings;
    public RobotBottomView(Context context) {
        super(context);
    }

    public RobotBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RobotBottomView(AbstractMainView view, Context context) {
        super(view, context);
    }

    @Override
    protected void initView() {
        super.initView();
        setShutDirect(UP_TO_DOWN);
        inflate(mContext, R.layout.robot_bottom_view, this);
        openSettings = findViewById(R.id.openSettings);
        openSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeeUISettings();
            }
        });
    }

    private static final String OPEN_FROM = "from";
    private static final String OPEN_FROM_TITLE = "from_title";
    private void openGeeUISettings() {
        String packageName = "com.robot.geeui.setting";
        String activityName = "com.robot.geeui.setting.MainActivity";
        Intent intent = new Intent();
        intent.putExtra(OPEN_FROM,OPEN_FROM_TITLE);
        intent.setComponent(new ComponentName(packageName, activityName));
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }

}
