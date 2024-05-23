package com.letianpai.robot.desktop.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInstaller;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.letianpai.robot.components.network.nets.AppStoreCmdConsts;
import com.letianpai.robot.components.parser.appstore.AppStoreInfos;
import com.letianpai.robot.components.utils.GeeUILogUtils;
import com.letianpai.robot.desktop.broadcast.PackageInstallReceiver;
import com.letianpai.robot.desktop.broadcast.PackageUninstallReceiver;
import com.letianpai.robot.desktop.callback.AppInstallSuccessCallback;
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback;
import com.letianpai.robot.letianpaiservice.LtpLongConnectCallback;
import com.renhejia.robot.letianpaiservice.ILetianpaiService;

/**
 * @author liujunbin
 */
public class GeeUIDesktopService extends Service {

    private ILetianpaiService iLetianpaiService;
    private Context mContext;
    private PackageInstallReceiver installReceiver;
    private PackageUninstallReceiver uninstallReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = GeeUIDesktopService.this;
        connectService();
        addModeChangeListeners();
        addInstallSuccessCallback();
        registerAppReceiver();
    }

    private void addModeChangeListeners() {
        ModeChangeCmdCallback.getInstance().setModeChangeCommandListener(new ModeChangeCmdCallback.ModeChangeCommandListener() {
            @Override
            public void onRobotModeChange(String command, String data) {
                if (iLetianpaiService!= null && !TextUtils.isEmpty(command) && !TextUtils.isEmpty(data)){
                    Log.e("letianpai","changeMode_data_command: "+ command);
                    Log.e("letianpai","changeMode_data_data: "+ data);
                    try {
                        iLetianpaiService.setLongConnectCommand(command,data);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void addInstallSuccessCallback() {
        AppInstallSuccessCallback.getInstance().setAppStatusUpdateListener(new AppInstallSuccessCallback.AppStoreInstallResultListener() {
            @Override
            public void appInstallSuccess(String packageName, String appName) {
                try {
                    iLetianpaiService.setAppCmd(AppStoreCmdConsts.COMMAND_INSTALL_APP_STORE_SUCCESS, (new AppStoreInfos(packageName, appName)).toString());
                    //TODO command 和data
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //链接服务端
    private void connectService() {
        Intent intent = new Intent();
        intent.setPackage("com.renhejia.robot.letianpaiservice");
        intent.setAction("android.intent.action.LETIANPAI");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("letianpai","========== GeeUIDesktopService onServiceConnected() ==========");
            Log.d("letianpai", "Menu 完成AIDLService绑定服务");
            iLetianpaiService = ILetianpaiService.Stub.asInterface(service);
//            try {
//                iLetianpaiService.registerLCCallback(ltpLongConnectCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("", "乐天派 无法绑定aidlserver的AIDLService服务");
        }
    };

    private final LtpLongConnectCallback.Stub ltpLongConnectCallback = new LtpLongConnectCallback.Stub() {
        @Override
        public void onLongConnectCommand(String command, String data) throws RemoteException {
            GeeUILogUtils.logi("onLongConnectCommand" , "---command: " + command + " /data: " + data);
//            GeeUILogUtils.logi("onLongConnectCommand" , "---onLongConnectCommand: " + "  当前机器人模式：" + RobotModeManager.getInstance(DispatchService.this).getRobotMode());
//            responseRobotCommand(RobotConsts.ROBOT_COMMAND_TYPE_REMOTE, iLetianpaiService, command, data, false);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null){
            unbindService(serviceConnection);
        }
        unregisterAppReceiver();

    }

    public void registerAppReceiver() {
        installReceiver = new PackageInstallReceiver();
        IntentFilter installIntentFilter = new IntentFilter();
        installIntentFilter.addAction(PackageInstaller.ACTION_SESSION_COMMITTED);
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        installIntentFilter.addDataScheme("package");
        mContext.registerReceiver(installReceiver, installIntentFilter);

        uninstallReceiver = new PackageUninstallReceiver();
        IntentFilter uninstallIntentFilter = new IntentFilter();
        uninstallIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        uninstallIntentFilter.addDataScheme("package");
        mContext.registerReceiver(uninstallReceiver, uninstallIntentFilter);

    }

    public void unregisterAppReceiver() {
        if (installReceiver != null) {
            mContext.unregisterReceiver(installReceiver);
        }

        if (uninstallReceiver != null) {
            mContext.unregisterReceiver(uninstallReceiver);
        }

    }
}
