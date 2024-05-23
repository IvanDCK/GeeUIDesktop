package com.letianpai.robot.desktop.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.letianpai.robot.components.storage.RobotSubConfigManager;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.callback.LifecycleChangedCallback;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.storage.RobotConfigManager;
import com.letianpai.robot.desktop.utils.KeyConsts;

public class AppListActivity extends AppCompatActivity {
    private String prePackageName;
    private int robotMode;
    private int userAppListSize;
    private long updateTime = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        userAppListSize = RobotSubConfigManager.getInstance(AppListActivity.this).getUserPackageListSize();
        prePackageName = getIntent().getStringExtra("package");
        robotMode = getIntent().getIntExtra("mode", 0);
        LifecycleChangedCallback.getInstance().setLifecycle(LifecycleChangedCallback.ON_CREATE);
        Log.e("letianpai_list_view_update","============ ON_CREATE ============");

//        if (robotMode == 11 || robotMode == 3 || robotMode == 5 ) {
//            changeMode();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LifecycleChangedCallback.getInstance().setLifecycle(LifecycleChangedCallback.ON_RESUME);
        Log.e("letianpai_list_view_update","============ ON_RESUME ============");
//        if (RobotConfigManager.getInstance(AppListActivity.this).getAppListConfig().equals(RobotConfigManager.VALUE_DEFAULT_APP_LIST_CONFIG) || isNeedUpdate()){
        if (RobotConfigManager.getInstance(AppListActivity.this).getAppListConfig().equals(RobotConfigManager.VALUE_DEFAULT_APP_LIST_CONFIG) ){
            Log.e("letianpai_list_view_update","============ ON_RESUME AAAAA ============");
            RobotAppListManager.getInstance(AppListActivity.this).updateAppMenuList();
            Log.e("letianpai_list_view_update","============ ON_RESUME BBBBB ============");
        }

    }

    private boolean isNeedUpdate() {
        if ((System.currentTimeMillis() - updateTime)> 1000 * 60 * 30){
            updateTime = System.currentTimeMillis();
            return true;
        }else{
            return false;
        }
    }

//    private void startService() {
//        Intent intent = new Intent(AppListActivity.this, GeeUIDesktopService.class);
//        startService(intent);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LifecycleChangedCallback.getInstance().setLifecycle(LifecycleChangedCallback.ON_DESTROY);
        RobotAppListManager.getInstance(AppListActivity.this).cleanScrollPosition();
        Log.e("letianpai_list_view_update","============ ON_DESTROY ============");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LifecycleChangedCallback.getInstance().setLifecycle(LifecycleChangedCallback.ON_PAUSE);
        Log.e("letianpai_list_view_update","============ ON_PAUSE ============");
    }

    public void changeMode() {
        String mode = AppListActivity.this.getPackageName();
        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
        Log.e("letianpai_", "changeMode_data: " + data);
        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    }
    public static final  String COMMAND_VALUE_CHANGE_SLEEP = "sleep" ;

    public int getRobotMode() {
        return robotMode;
    }

    public String getPrePackageName() {
        return prePackageName;
    }

}