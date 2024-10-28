package com.letianpai.robot.desktop.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageInstaller
import android.os.IBinder
import android.os.RemoteException
import android.text.TextUtils
import android.util.Log
import com.letianpai.robot.components.network.nets.AppStoreCmdConsts
import com.letianpai.robot.components.parser.appstore.AppStoreInfos
import com.letianpai.robot.components.utils.GeeUILogUtils
import com.letianpai.robot.desktop.broadcast.PackageInstallReceiver
import com.letianpai.robot.desktop.broadcast.PackageUninstallReceiver
import com.letianpai.robot.desktop.callback.AppInstallSuccessCallback
import com.letianpai.robot.desktop.callback.AppInstallSuccessCallback.AppStoreInstallResultListener
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback
import com.letianpai.robot.desktop.callback.ModeChangeCmdCallback.ModeChangeCommandListener
import com.letianpai.robot.letianpaiservice.LtpLongConnectCallback
import com.renhejia.robot.letianpaiservice.ILetianpaiService

/**
 * @author liujunbin
 */
class GeeUIDesktopService : Service() {
    private var iLetianpaiService: ILetianpaiService? = null
    private var mContext: Context? = null
    private var installReceiver: PackageInstallReceiver? = null
    private var uninstallReceiver: PackageUninstallReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this@GeeUIDesktopService
        connectService()
        addModeChangeListeners()
        addInstallSuccessCallback()
        registerAppReceiver()
    }

    private fun addModeChangeListeners() {
        ModeChangeCmdCallback.instance.setModeChangeCommandListener { command, data ->
            if (iLetianpaiService != null && !TextUtils.isEmpty(command) && !TextUtils.isEmpty(
                    data
                )
            ) {
                Log.e("letianpai", "changeMode_data_command: $command")
                Log.e("letianpai", "changeMode_data_data: $data")
                try {
                    iLetianpaiService!!.setLongConnectCommand(command, data)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun addInstallSuccessCallback() {
        AppInstallSuccessCallback.instance.setAppStatusUpdateListener { packageName, appName ->
            try {
                iLetianpaiService!!.setAppCmd(
                    AppStoreCmdConsts.COMMAND_INSTALL_APP_STORE_SUCCESS,
                    (AppStoreInfos(packageName, appName)).toString()
                )
                //TODO command and data
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

    //Link Server
    private fun connectService() {
        val intent = Intent()
        intent.setPackage("com.renhejia.robot.letianpaiservice")
        intent.setAction("android.intent.action.LETIANPAI")
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e("letianpai", "========== GeeUIDesktopService onServiceConnected() ==========")
            Log.d("letianpai", "Menu Completing the AIDLService Binding Service")
            iLetianpaiService = ILetianpaiService.Stub.asInterface(service)
            //            try {
//                iLetianpaiService.registerLCCallback(ltpLongConnectCallback);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.d("", "Lotte Pie Unable to bind AIDLService service of aidlserver")
        }
    }

    private val ltpLongConnectCallback: LtpLongConnectCallback.Stub =
        object : LtpLongConnectCallback.Stub() {
            @Throws(RemoteException::class)
            override fun onLongConnectCommand(command: String, data: String) {
                GeeUILogUtils.logi("onLongConnectCommand", "---command: $command /data: $data")
                //            GeeUILogUtils.logi("onLongConnectCommand" , "---onLongConnectCommand: " + "  当前机器人模式：" + RobotModeManager.getInstance(DispatchService.this).getRobotMode());
//            responseRobotCommand(RobotConsts.ROBOT_COMMAND_TYPE_REMOTE, iLetianpaiService, command, data, false);
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceConnection != null) {
            unbindService(serviceConnection)
        }
        unregisterAppReceiver()
    }

    fun registerAppReceiver() {
        installReceiver = PackageInstallReceiver()
        val installIntentFilter = IntentFilter()
        installIntentFilter.addAction(PackageInstaller.ACTION_SESSION_COMMITTED)
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        installIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        installIntentFilter.addDataScheme("package")
        mContext!!.registerReceiver(installReceiver, installIntentFilter)

        uninstallReceiver = PackageUninstallReceiver()
        val uninstallIntentFilter = IntentFilter()
        uninstallIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        uninstallIntentFilter.addDataScheme("package")
        mContext!!.registerReceiver(uninstallReceiver, uninstallIntentFilter)
    }

    fun unregisterAppReceiver() {
        if (installReceiver != null) {
            mContext!!.unregisterReceiver(installReceiver)
        }

        if (uninstallReceiver != null) {
            mContext!!.unregisterReceiver(uninstallReceiver)
        }
    }
}
