package com.letianpai.robot.desktop.ui.view;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.letianpai.robot.components.network.lexnet.callback.DeviceChannelLogoCallBack;
import com.letianpai.robot.components.network.nets.GeeUINetResponseManager;
import com.letianpai.robot.components.parser.logo.LogoInfo;
import com.letianpai.robot.desktop.MainActivity;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.desktop.callback.OpenAppCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity;
import com.letianpai.robot.desktop.ui.activity.AppListActivity;
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity;
import com.letianpai.robot.desktop.utils.ImageDownloader;
import com.letianpai.robot.desktop.utils.KeyConsts;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class LetianpaiMainView extends LinearLayout {
    private Context mContext;
//    private ImageView ivAppIcon;
//    private TextView appTitle;
    private SettingsButton openAppList;
    private BackButton backButton;
    private Button ivAppIcon;
    private ImageView bg_image;


    private static final int UPDATE_BACKGROUND = 110;

    private  UpdateViewHandler updateViewHandler;

    public LetianpaiMainView(Context context) {
        super(context);
        inits(context);
    }

    private void inits(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.letianpai_main, this);
        initViews();
        addListeners();
        loadPreDownImage();
        getImageUrl();
    }

    public void getImageUrl() {
        Log.d("ZG","getImageUrl");
        GeeUINetResponseManager.getInstance(mContext).getLogoInfo();

        DeviceChannelLogoCallBack.getInstance().setDeviceChannelLogoUpdateListener(new DeviceChannelLogoCallBack.DeviceChannelLogoUpdateListener() {
            @Override
            public void onLogoInfoUpdate(LogoInfo logoInfo) {
                updateViewData(logoInfo);
            }
        });
    }

    /***
     * 更新 背景
     * @param logoInfo
     */
    private void updateViewData(LogoInfo logoInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (logoInfo != null && logoInfo.getData() != null && (!TextUtils.isEmpty(logoInfo.getData().getDesktop_logo()))) {
                    updateBackground(logoInfo.getData().getDesktop_logo());
                    downImage(logoInfo.getData().getDesktop_logo());
                }
            }
        }).start();

    }

    private void downImage(String imageUrl){
        File file = new File(getSDCardDataPath(), "image.jpg");
        try {
            // 尝试删除已存在的同名文件（如果存在）
            if (file.exists()) {
                file.delete();
            }
            // 创建新文件
            boolean created = file.createNewFile();
            if (created) {
                String destinationPath = file.getPath();
                ImageDownloader.downloadImage(imageUrl,destinationPath);
            }} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 加载上次加载的图片
     * @return
     */
    private void loadPreDownImage(){
        File file = new File(getSDCardDataPath(), "image.jpg");
        if(file.exists()){
                // 图片文件存在，加载图片
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bg_image.setImageBitmap(myBitmap);
////
//            ImageView bg_image = findViewById(R.id.bg_image);
//            bg_image.setImageResource(R.drawable.bg17); // 设置占位符图片



        }
    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    private String getSDCardDataPath() {
        File externalFilesDir = mContext.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            return externalFilesDir.getAbsolutePath();
        } else {
            return null;
        }
    }

    private void addListeners() {
        openAppList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppListView();
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                responseBack();
            }
        });

        ivAppIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeeUISettings();
            }
        });
        OpenAppCallback.getInstance().setOpenAppListener(new OpenAppCallback.OpenAppCommandListener() {
            @Override
            public void onResponseOpenApp(String command) {
                ((MainActivity)mContext).finish();
            }
        });
    }

    private void responseBack() {
        Log.e("letianpai_test","============= keyImageButton ===============");
        ((MainActivity)mContext).finish();
        if (((MainActivity) mContext).getRobotMode() == 11) {
            Log.e("letianpai_", "changeMode_data:(MainActivity) mContext).getRobotMode()======== 11 ========: " + ((MainActivity) mContext).getRobotMode());
            changeRobotMode();
        }else if (((MainActivity) mContext).getRobotMode() == 3){
            changeStaticMode();
        }else if (((MainActivity) mContext).getRobotMode() == 5){
            changeSleepMode();
        }
    }

    private void initViews() {
        openAppList = findViewById(R.id.openAppList);
        backButton = findViewById(R.id.back_main);
        ivAppIcon = findViewById(R.id.ivAppIcon);
        bg_image = findViewById(R.id.bg_image);
        updateViewHandler = new  UpdateViewHandler(mContext.getApplicationContext());
     }


    public LetianpaiMainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public LetianpaiMainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inits(context);
    }

    public LetianpaiMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inits(context);
    }

    private void openAppListView() {
        Intent intent = new Intent(mContext, AppListActivity.class);
        String packageName = ((MainActivity)mContext).getPrePackageName();
        int mode = ((MainActivity) mContext).getRobotMode();
        intent.putExtra("package", packageName);
        intent.putExtra("mode", mode);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
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
    public void changeRobotMode() {
        changeMode("robot");
    }

    public void changeSleepMode() {
        changeMode("sleep");
    }

    public void changeMode(String mode) {
        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
        Log.e("letianpai", "changeMode_data: " + data);
        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    }

    public void changeStaticMode() {
        String packageName = ((MainActivity)mContext).getPrePackageName();
        if (TextUtils.isEmpty(packageName)){
            return;
        }
        String mode = RobotAppListManager.getInstance(mContext).getModeNameByPackage(packageName);
        if (TextUtils.isEmpty(mode)){
            return;
        }
        String data = String.format("{\"select_module_tag_list\":[\"%s\"]}", mode);
        Log.e("letianpai_", "changeMode_data: " + data);
        ModeChangeCmdCallback.getInstance().changeRobotMode(KeyConsts.CHANGE_SHOW_MODE, data);
    }

    public void updateBackground(String url) {
        Log.d("ZG","updateBackground");
        Message message = new Message();
        message.what = UPDATE_BACKGROUND;
        message.obj = url;
        updateViewHandler.sendMessage(message);
    }

    private class UpdateViewHandler extends android.os.Handler {
        private final WeakReference<Context> context;

        public UpdateViewHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_BACKGROUND:
                    Log.d("ZG","updateBackground AAA" + (String) msg.obj);
//                    Picasso.with(mContext)
//                            .load((String) msg.obj)
//                            .resize(500, 500)
//                            .centerCrop()
//                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                            .networkPolicy(NetworkPolicy.NO_CACHE)
//                            .into(bg_image);

                    File file = new File(getSDCardDataPath(), "image.jpg");

                    new DownloadImageTask().execute((String) msg.obj, file.getAbsolutePath());
                    break;
            }
        }


    }


    private class DownloadImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String destinationPath = params[1];
            return ImageDownloader.downloadImage(urlString, destinationPath);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                File imgFile = new File(result);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    bg_image.setImageBitmap(myBitmap);
                }
            }
        }
    }


}

