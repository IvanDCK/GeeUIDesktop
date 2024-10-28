package com.letianpai.robot.desktop.ui.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.apps.adapter.AppListAdapter
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback.AppMenuUpdateListener
import com.letianpai.robot.desktop.callback.LifecycleChangedCallback
import com.letianpai.robot.desktop.callback.LifecycleChangedCallback.LifecycleChangedListener
import com.letianpai.robot.desktop.callback.OpenAppCallback
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity
import com.letianpai.robot.desktop.ui.activity.AppListActivity
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity
import com.letianpai.robot.desktop.ui.base.AbstractMainView
import com.letianpai.robot.desktop.utils.OpenTypeConsts
import java.lang.ref.WeakReference

/**
 * @author liujunbin
 */
class AppListVerticalView : RelativeLayout {
    private var appListView: RecyclerView? = null
    private var mContext: Context? = null
    private var appListAdapter: AppListAdapter? = null

    //    private List<AppListItem> appList;
    private var appMenuList = ArrayList<AppMenuInfo?>()
    private var tempAppMenuList = ArrayList<AppMenuInfo?>()
    private val preX = 0
    private val preY = 0
    private var updateViewHandler: UpdateViewHandler? = null
    private var appMenuUpdateListener: AppMenuUpdateListener? = null
    private val topView: View? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private val scrollPosition = 0
    private val tempScrollPosition = 0

    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, mainView: AbstractMainView?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context?) {
        this.mContext = context
        inflate(mContext, R.layout.app_list_vertical_views, this)
        updateViewHandler = UpdateViewHandler(mContext!!)
        //        RobotAppListManager.getInstance(mContext).updateAppMenuList();
        appListView = findViewById<View>(R.id.rc_app_list) as RecyclerView
        //        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager = GridLayoutManager(mContext, 3)
        var appMenuInfos: ArrayList<AppMenuInfo?>? = RobotAppListManager.getInstance(
            mContext!!
        ).appListInfo

        appMenuInfos =
            RobotAppListManager.getInstance(mContext!!).getAppMenuInfoList(appMenuInfos)
        //        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.canScrollVertically();
        appListView!!.layoutManager = gridLayoutManager
        //        appListView.setLayoutManager(linearLayoutManager);
        appListAdapter = AppListAdapter(mContext)
        addListener()
        addLifecycleUpdateListeners()
        //        initAppMenuUpdateCallback();
//        addListUpdateCallback();
//        appListView.setAdapter(appListAdapter);
//        if (appMenuInfos == null || appMenuInfos.size() == 0) {
////            appListView.setAdapter(appListAdapter);
////            addListener();
        if (appMenuInfos == null || appMenuInfos.size == 0) {
            appListView!!.adapter = appListAdapter
            //            addListener();
        } else {
            appMenuInfos.addAll(
                RobotAppListManager.getInstance(mContext!!).localAppList
            )
            setMenuList(appMenuInfos)
            updateView()
        }
    }

    private fun addLifecycleUpdateListeners() {
        LifecycleChangedCallback.instance.addLifecycleChangedListener { lifecycle ->
            when (lifecycle) {
                LifecycleChangedCallback.ON_DESTROY -> {
                    removeListUpdateCallback()
                }

                LifecycleChangedCallback.ON_RESUME -> {
                    addListUpdateCallback()
                }

                LifecycleChangedCallback.ON_CREATE -> {
                    addListUpdateCallback()
                }
            }
        }
    }

    private fun initAppMenuUpdateCallback() {
        appMenuUpdateListener = object : AppMenuUpdateListener {
            override fun onAppMenuUpdated(appMenuInfo: ArrayList<AppMenuInfo?>?) {
//                setAppList(appMenuInfo);
                setMenuList(appMenuInfo)
                updateView()
                //                setAppList(appMenuInfo);
            }

            override fun onAppStatusUpdated(packageName: String?, status: Int) {
                updateAppStatus(packageName, status)
            }
        }
    }

    private fun addListUpdateCallback() {
        if (appMenuUpdateListener == null) {
            initAppMenuUpdateCallback()
            AppMenuUpdateCallback.instance
                .registerAppMenuUpdateListener(appMenuUpdateListener)
        }
    }

    fun removeListUpdateCallback() {
        AppMenuUpdateCallback.instance
            .unregisterAppMenuUpdateListener(appMenuUpdateListener)
        appMenuUpdateListener = null
    }

    private fun addListener() {
        appListAdapter!!.setmOnItemClickListener(object : AppListAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (appMenuList != null && appMenuList.size > position && (appMenuList[position] != null)) {
//                    WatchAppListManager.getInstance(mContext).selectApp(appList.get(position),mContext);
//                    openApp(appMenuList.get(position).getPackageName());
//                    openApp1(appMenuList.get(position));
                    responseAppClick(appMenuList[position])
                    //                    changeMode(appMenuList.get(position));
                }
            }

            override fun onItemLongClick(view: View?, position: Int) {
//                Toast.makeText(mContext, "长按触发", Toast.LENGTH_SHORT).show();
//                WatchFunctionUtils.responseAppLongClick(mContext);
            }
        })

        appListView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e(
                    "letianpai_view_update",
                    "letianpai_view_update1: $scrollPosition"
                )
                //                topView = gridLayoutManager.getChildAt(0);
                // 获取与该view的顶部的偏移量
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                val firstVisibleItem =
                    recyclerView.layoutManager!!.findViewByPosition(firstVisibleItemPosition)
                val offset = firstVisibleItem?.top ?: 0
                RobotAppListManager.getInstance(mContext!!)
                    .setScrollPosition(firstVisibleItemPosition)

                //                if (topView != null){
//                    // 得到该View的数组位置
//                    tempScrollPosition = gridLayoutManager.getPosition(topView);
//                    RobotAppListManager.getInstance(mContext).setScrollPosition(tempScrollPosition);
////                    if (tempScrollPosition != 0){
////                        scrollPosition = tempScrollPosition;
////                    }
//                    Log.e("letianpai_view_update","letianpai_view_update2: "+ scrollPosition);
//                    Log.e("letianpai_view_update","scrollPosition2: "+ scrollPosition);
//                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun responseAppClick(appMenuInfo: AppMenuInfo?) {
        if (appMenuInfo == null) {
            return
        }
        if (appMenuInfo.openType == OpenTypeConsts.OPEN_MODE_SWITCH) {
            openApp1(appMenuInfo)
        } else if (appMenuInfo.openType == OpenTypeConsts.OPEN_APP_MAIN) {
            openMain(appMenuInfo)
        } else if (appMenuInfo.openType == OpenTypeConsts.OPEN_APP_SETTINGS) {
            openSettings(appMenuInfo)
        } else if (appMenuInfo.openType == OpenTypeConsts.OPEN_APP_DEFAULT_SETTINGS) {
            openDefaultSettings(appMenuInfo)
        } else if (appMenuInfo.openType == OpenTypeConsts.OPEN_APP_USER_APP) {
            openUserApp(appMenuInfo)
            //            openUserApp(appMenuInfo);
        }
    }

    private fun updateView() {
        val message = Message()
        message.what = UPDATE_APP_LIST
        updateViewHandler!!.sendMessage(message)
    }

    private fun updateAppStatus(packageName: String?, status: Int) {
        val message = Message()
        message.what = UPDATE_APP_STATUS
        message.obj = packageName
        message.arg1 = status
        updateViewHandler!!.sendMessage(message)
    }

    private fun openApp1(appMenuInfo: AppMenuInfo) {
        try {
            val mode = (mContext as AppListActivity).robotMode
            val intent = Intent(mContext, AppModeSwitchActivity::class.java)
            intent.putExtra(APP_INFO, appMenuInfo)
            intent.putExtra(MODE, mode)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            mContext!!.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openUserApp(appMenuInfo: AppMenuInfo) {
        try {
            val mode = (mContext as AppListActivity).robotMode
            val intent = Intent(mContext, UserAppMainActivity::class.java)
            val appMenuInfoNew = appMenuInfo
            appMenuInfoNew.drawableIcon = null
            intent.putExtra(APP_INFO, appMenuInfoNew)
            intent.putExtra(MODE, mode)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            mContext!!.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openSettings(appMenuInfo: AppMenuInfo) {
        val settings = appMenuInfo.settings
        if (!TextUtils.isEmpty(settings)) {
            openApp(appMenuInfo, OpenTypeConsts.OPEN_APP_SETTINGS)
            OpenAppCallback.instance.openApp(appMenuInfo.packageName)
        }

        //        String settings = appMenuInfo.getSettings();
//        String packageName = appMenuInfo.getPackageName();
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName(packageName, settings));
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
//        mContext.startActivity(intent);
    }

    private fun openMain(appMenuInfo: AppMenuInfo) {
        openApp(appMenuInfo, OpenTypeConsts.OPEN_APP_MAIN)
        OpenAppCallback.instance.openApp(appMenuInfo.packageName)
    }

    private fun openApp(appMenuInfo: AppMenuInfo, openType: Int) {
        try {
            val intent = Intent()
            val packageName = appMenuInfo.packageName
            if (openType == OpenTypeConsts.OPEN_APP_MAIN) {
                val main = appMenuInfo.openAddress
                intent.setComponent(ComponentName(packageName!!, main!!))
            } else if (openType == OpenTypeConsts.OPEN_APP_SETTINGS) {
                val settings = appMenuInfo.settings
                intent.setComponent(ComponentName(packageName!!, settings!!))
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            mContext!!.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openDefaultSettings(appMenuInfo: AppMenuInfo) {
        try {
            val mode = (mContext as AppListActivity).robotMode
            val intent = Intent(mContext, AppDefaultSettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra(APP_INFO, appMenuInfo)
            intent.putExtra(MODE, mode)
            mContext!!.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //    private void openUserApp(AppMenuInfo appMenuInfo) {
    //        PackageManager packageManager = mContext.getPackageManager();
    //        Intent intent = packageManager.getLaunchIntentForPackage(appMenuInfo.getPackageName());
    //
    //        if (intent != null) {
    //            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            mContext.startActivity(intent);
    //        }
    //    }
    //    private void setAppList() {
    ////        appList = RobotAppListManager.getInstance(mContext).getAppList();
    ////        appMenuList = RobotAppListManager.getInstance(mContext).getAppList();
    //        appListAdapter.setAppList( appMenuList);
    //        appListView.setAdapter(appListAdapter);
    //    }
    private fun setAppList() {
//        appMenuList.clear();
        appMenuList =
            RobotAppListManager.getInstance(mContext!!).fillAppListIcon(appMenuList)
        if (tempAppMenuList === appMenuList) {
            return
        } else {
            tempAppMenuList = appMenuList
        }
        appListAdapter!!.setAppList(appMenuList)
        appListView!!.adapter = appListAdapter
        //        gridLayoutManager.scrollToPosition(scrollPosition);
        var scrollPosition1: Int =
            RobotAppListManager.getInstance(mContext!!).getScrollPosition()
        if (scrollPosition1 >= appMenuList.size) {
            scrollPosition1 = appMenuList.size - 1
        }
        //        gridLayoutManager.scrollToPosition(scrollPosition1);
//        gridLayoutManager.scrollToPositionWithOffset(scrollPosition1,0);
        (appListView!!.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
            scrollPosition1,
            0
        )
        //        addListener();
    }

    fun setMenuList(appMenuInfo: ArrayList<AppMenuInfo?>?) {
        if (appMenuInfo != null) {
            this.appMenuList = appMenuInfo
        }
    }

    //    public void responseUpdateAppStatus(String packageName,int status) {
    fun responseUpdateAppStatus(message: Message) {
        val packageName = message.obj as String
        val status = message.arg1
        for (i in appMenuList.indices) {
            if (appMenuList[i] != null && !TextUtils.isEmpty(appMenuList[i]!!.packageName) && appMenuList[i]!!.packageName == packageName) {
                appMenuList[i]!!.appStatus = (status)
                appListAdapter!!.notifyItemChanged(i)
            }
        }
    }

    private fun setAppList0() {
//        appMenuList.clear();
        Log.e(
            "letianpai",
            "AppMenuUpdateCallback.getInstance().registerAppMenuUpdateListener ============ 2000000 =============appMenuList.size(): " + appMenuList.size
        )
        //        appListAdapter.setAppList(appMenuList);
        appListView!!.adapter = appListAdapter
    }

    fun goTop() {
        appListView!!.scrollToPosition(0)
    }

    //设定RecyclerView最大滑动速度
    private fun setMaxFlingVelocity(recycleview: RecyclerView, velocity: Int) {
        try {
            val field = recycleview.javaClass.getDeclaredField("mMaxFlingVelocity")
            field.isAccessible = true
            field[recycleview] = velocity
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context =
            WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_APP_LIST -> updateViews()
                UPDATE_APP_STATUS -> responseUpdateAppStatus(msg)
            }
        }
    }

    private fun updateViews() {
        setAppList()
    }

    companion object {
        private const val UPDATE_APP_LIST = 1
        private const val UPDATE_APP_STATUS = 2
        private const val APP_INFO = "appInfo"
        private const val MODE = "mode"
    }
}