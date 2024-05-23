package com.letianpai.robot.desktop.ui.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity;

public class DeleteConfirmView extends LinearLayout {
    private Context mContext;
    private ImageView ivCancel;
    private ImageView ivConfirm;
    private BackButton ivBack;

    public DeleteConfirmView(Context context) {
        super(context);
    }

    public DeleteConfirmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DeleteConfirmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public DeleteConfirmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_confirm_view,this);
        initViews();
        addListeners();
    }

    private void initViews() {
        ivConfirm = findViewById(R.id.iv_confirm);
        ivCancel = findViewById(R.id.iv_cancel);
        ivBack = findViewById(R.id.back_firm_view);
    }

    private void addListeners() {
        ivConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseConfirm();
            }
        });
        ivCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseCancel();
            }
        });
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseBack();
            }
        });

    }

    private void responseBack() {
        responseCancel();
    }

    private void responseCancel() {
        ((UserAppMainActivity)mContext).showUserView();
    }

    protected void responseConfirm(){
        if (((UserAppMainActivity) mContext).getAppMenuInfo() != null && !TextUtils.isEmpty(((UserAppMainActivity) mContext).getAppMenuInfo().getPackageName())) {
            String packageName = ((UserAppMainActivity) mContext).getAppMenuInfo().getPackageName();
            unInstallApp(packageName);
//            RobotAppListManager.getInstance(mContext).updateAppMenuList();
            AppMenuUpdateCallback.getInstance().setAppStatusUpdate(packageName,1);
            ((UserAppMainActivity)mContext).closeActivity1();
        }
    };

    private void unInstallApp(String packageName) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent sender = PendingIntent.getActivity(mContext, 0, intent, 0);
        PackageInstaller mPackageInstaller = mContext.getPackageManager().getPackageInstaller();
        mPackageInstaller.uninstall(packageName, sender.getIntentSender());// 卸载APK
    }
}
