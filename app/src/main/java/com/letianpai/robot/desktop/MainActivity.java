package com.letianpai.robot.desktop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.letianpai.robot.components.network.callback.AppTitleTipCallback;
import com.letianpai.robot.components.network.callback.apptips.AppTips;
import com.letianpai.robot.components.network.nets.GeeUINetResponseManager;
import com.letianpai.robot.components.network.nets.GeeUiNetManager;
import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.parser.RobotMenu;
import com.letianpai.robot.desktop.service.GeeUIDesktopService;
import com.letianpai.robot.desktop.ui.view.LetianpaiMainView;
import com.letianpai.robot.desktop.utils.KeyConsts;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String prePackageName;
    private int robotMode;

    private LetianpaiMainView main_view ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        prePackageName = getIntent().getStringExtra("package");
        robotMode = getIntent().getIntExtra("mode", 0);
        RobotAppListManager.getInstance(MainActivity.this).resetLocalUserPackageList();
        main_view = findViewById(R.id.main_view);
        startService();
        if (robotMode == 11 || robotMode == 3 || robotMode == 5 ) {
            changeMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RobotAppListManager.getInstance(MainActivity.this).updateAppMenuList();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0); // 禁用关闭动画
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, GeeUIDesktopService.class);
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("ZG","---getLOGO----");
        main_view.getImageUrl();
    }

    /****
     * 首页切换到桌面页，需要主动请求LOGO
     */
    private void getLOGO(){
        GeeUINetResponseManager.getInstance(this).getLogoInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void changeMode() {
        String mode = MainActivity.this.getPackageName();
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