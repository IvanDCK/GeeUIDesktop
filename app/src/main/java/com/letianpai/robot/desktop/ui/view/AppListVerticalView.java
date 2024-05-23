package com.letianpai.robot.desktop.ui.view;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.apps.adapter.AppListAdapter;
import com.letianpai.robot.desktop.callback.AppMenuUpdateCallback;
import com.letianpai.robot.desktop.callback.LifecycleChangedCallback;
import com.letianpai.robot.desktop.callback.OpenAppCallback;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.ui.activity.AppDefaultSettingActivity;
import com.letianpai.robot.desktop.ui.activity.AppListActivity;
import com.letianpai.robot.desktop.ui.activity.AppModeSwitchActivity;
import com.letianpai.robot.desktop.ui.activity.UserAppMainActivity;
import com.letianpai.robot.desktop.ui.base.AbstractMainView;
import com.letianpai.robot.desktop.utils.OpenTypeConsts;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author liujunbin
 */
public class AppListVerticalView extends RelativeLayout {

    private RecyclerView appListView;
    private Context mContext;
    private AppListAdapter appListAdapter;
    //    private List<AppListItem> appList;
    private ArrayList<AppMenuInfo> appMenuList = new ArrayList<>();
    private ArrayList<AppMenuInfo> tempAppMenuList = new ArrayList<>();
    private int preX = 0;
    private int preY = 0;
    private static final int UPDATE_APP_LIST = 1;
    private static final int UPDATE_APP_STATUS = 2;
    private UpdateViewHandler updateViewHandler;
    private AppMenuUpdateCallback.AppMenuUpdateListener appMenuUpdateListener;
    private View topView;
    private GridLayoutManager gridLayoutManager;
    private int scrollPosition;
    private int tempScrollPosition;

    private static String APP_INFO = "appInfo";
    private static String MODE = "mode";

    public AppListVerticalView(Context context) {
        super(context);
        initView(context);
    }

    public AppListVerticalView(Context context, AbstractMainView mainView) {
        super(context);
        initView(context);
    }

    public AppListVerticalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AppListVerticalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_list_vertical_views, this);
        updateViewHandler = new UpdateViewHandler(mContext);
//        RobotAppListManager.getInstance(mContext).updateAppMenuList();
        appListView = (RecyclerView) findViewById(R.id.rc_app_list);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
         gridLayoutManager = new GridLayoutManager(mContext, 3);
        ArrayList<AppMenuInfo> appMenuInfos = RobotAppListManager.getInstance(mContext).getAppListInfo();

        appMenuInfos = RobotAppListManager.getInstance(mContext).getAppMenuInfoList(appMenuInfos);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.canScrollVertically();
        appListView.setLayoutManager(gridLayoutManager);
//        appListView.setLayoutManager(linearLayoutManager);
        appListAdapter = new AppListAdapter(mContext);
        addListener();
        addLifecycleUpdateListeners();
//        initAppMenuUpdateCallback();
//        addListUpdateCallback();
//        appListView.setAdapter(appListAdapter);
//        if (appMenuInfos == null || appMenuInfos.size() == 0) {
////            appListView.setAdapter(appListAdapter);
////            addListener();
        if (appMenuInfos == null || appMenuInfos.size() == 0) {
            appListView.setAdapter(appListAdapter);
//            addListener();
        } else {
            appMenuInfos.addAll(RobotAppListManager.getInstance(mContext).getLocalAppList());
            setMenuList(appMenuInfos);
            updateView();

        }
    }

    private void addLifecycleUpdateListeners() {
        LifecycleChangedCallback.getInstance().addLifecycleChangedListener(new LifecycleChangedCallback.LifecycleChangedListener() {
            @Override
            public void onLifecycleChange(String lifecycle) {
                if (lifecycle.equals(LifecycleChangedCallback.ON_DESTROY)){
                    removeListUpdateCallback();
                }else if (lifecycle.equals(LifecycleChangedCallback.ON_RESUME)){
                    addListUpdateCallback();
                }else if (lifecycle.equals(LifecycleChangedCallback.ON_CREATE)){
                    addListUpdateCallback();
                }
            }
        });

    }

    private void initAppMenuUpdateCallback() {
        appMenuUpdateListener = new AppMenuUpdateCallback.AppMenuUpdateListener() {
            @Override
            public void onAppMenuUpdated(ArrayList<AppMenuInfo> appMenuInfo) {
//                setAppList(appMenuInfo);
                setMenuList(appMenuInfo);
                updateView();
//                setAppList(appMenuInfo);
            }

            @Override
            public void onAppStatusUpdated(String packageName, int status) {
                updateAppStatus(packageName,status);
            }
        };
    }

    private void addListUpdateCallback() {
        if (appMenuUpdateListener == null){
            initAppMenuUpdateCallback();
            AppMenuUpdateCallback.getInstance().registerAppMenuUpdateListener(appMenuUpdateListener);
        }

    }
    public void removeListUpdateCallback() {
        AppMenuUpdateCallback.getInstance().unregisterAppMenuUpdateListener(appMenuUpdateListener);
        appMenuUpdateListener = null;
    }

    private void addListener() {

        appListAdapter.setmOnItemClickListener(new AppListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (appMenuList != null && appMenuList.size() > position && (appMenuList.get(position) != null)) {
//                    WatchAppListManager.getInstance(mContext).selectApp(appList.get(position),mContext);
//                    openApp(appMenuList.get(position).getPackageName());
//                    openApp1(appMenuList.get(position));
                    responseAppClick(appMenuList.get(position));
//                    changeMode(appMenuList.get(position));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(mContext, "长按触发", Toast.LENGTH_SHORT).show();
//                WatchFunctionUtils.responseAppLongClick(mContext);
            }
        });

        appListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("letianpai_view_update","letianpai_view_update1: "+ scrollPosition);
//                topView = gridLayoutManager.getChildAt(0);
                // 获取与该view的顶部的偏移量
                int firstVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                View firstVisibleItem = recyclerView.getLayoutManager().findViewByPosition(firstVisibleItemPosition);
                int offset = firstVisibleItem != null ? firstVisibleItem.getTop() : 0;
                RobotAppListManager.getInstance(mContext).setScrollPosition(firstVisibleItemPosition);

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

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void responseAppClick(AppMenuInfo appMenuInfo) {
        if (appMenuInfo == null) {
            return;
        }
        if (appMenuInfo.getOpenType() == OpenTypeConsts.OPEN_MODE_SWITCH) {
            openApp1(appMenuInfo);

        } else if (appMenuInfo.getOpenType() == OpenTypeConsts.OPEN_APP_MAIN) {
            openMain(appMenuInfo);

        } else if (appMenuInfo.getOpenType() == OpenTypeConsts.OPEN_APP_SETTINGS) {
            openSettings(appMenuInfo);

        } else if (appMenuInfo.getOpenType() == OpenTypeConsts.OPEN_APP_DEFAULT_SETTINGS) {
            openDefaultSettings(appMenuInfo);

        } else if (appMenuInfo.getOpenType() == OpenTypeConsts.OPEN_APP_USER_APP) {
            openUserApp(appMenuInfo);
//            openUserApp(appMenuInfo);
        }

    }

    private void updateView() {
        Message message = new Message();
        message.what = UPDATE_APP_LIST;
        updateViewHandler.sendMessage(message);
    }
    private void updateAppStatus(String packageName,int status) {
        Message message = new Message();
        message.what = UPDATE_APP_STATUS;
        message.obj =packageName;
        message.arg1=status;
        updateViewHandler.sendMessage(message);
    }

    private void openApp1(AppMenuInfo appMenuInfo) {
        try {
            int mode = ((AppListActivity) mContext).getRobotMode();
            Intent intent = new Intent(mContext, AppModeSwitchActivity.class);
            intent.putExtra(APP_INFO, appMenuInfo);
            intent.putExtra(MODE, mode);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openUserApp(AppMenuInfo appMenuInfo) {
        try {
            int mode = ((AppListActivity) mContext).getRobotMode();
            Intent intent = new Intent(mContext, UserAppMainActivity.class);
            AppMenuInfo appMenuInfoNew = appMenuInfo;
            appMenuInfoNew.setDrawableIcon(null);
            intent.putExtra(APP_INFO, appMenuInfoNew);
            intent.putExtra(MODE, mode);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openSettings(AppMenuInfo appMenuInfo) {
        String settings = appMenuInfo.getSettings();
        if (!TextUtils.isEmpty(settings)){
            openApp(appMenuInfo,OpenTypeConsts.OPEN_APP_SETTINGS);
            OpenAppCallback.getInstance().openApp(appMenuInfo.getPackageName());
        }

//        String settings = appMenuInfo.getSettings();
//        String packageName = appMenuInfo.getPackageName();
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName(packageName, settings));
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
//        mContext.startActivity(intent);
    }
    private void openMain(AppMenuInfo appMenuInfo) {
        openApp(appMenuInfo,OpenTypeConsts.OPEN_APP_MAIN);
        OpenAppCallback.getInstance().openApp(appMenuInfo.getPackageName());
    }

    private void openApp(AppMenuInfo appMenuInfo,int openType) {
        try {
            Intent intent = new Intent();
            String packageName = appMenuInfo.getPackageName();
            if (openType == OpenTypeConsts.OPEN_APP_MAIN){
                String main = appMenuInfo.getOpenAddress();
                intent.setComponent(new ComponentName(packageName, main));
            }else if (openType == OpenTypeConsts.OPEN_APP_SETTINGS){
                String settings = appMenuInfo.getSettings();
                intent.setComponent(new ComponentName(packageName, settings));
            }
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openDefaultSettings(AppMenuInfo appMenuInfo) {
        try {
            int mode = ((AppListActivity) mContext).getRobotMode();
            Intent intent = new Intent(mContext, AppDefaultSettingActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(APP_INFO, appMenuInfo);
            intent.putExtra(MODE, mode);
            mContext.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
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

    private void setAppList() {
//        appMenuList.clear();
        appMenuList = RobotAppListManager.getInstance(mContext).fillAppListIcon(appMenuList);
        if (tempAppMenuList == appMenuList){
            return;
        }else{
            tempAppMenuList = appMenuList;
        }
        appListAdapter.setAppList(appMenuList);
        appListView.setAdapter(appListAdapter);
//        gridLayoutManager.scrollToPosition(scrollPosition);
        int scrollPosition1 = RobotAppListManager.getInstance(mContext).getScrollPosition();
        if (scrollPosition1 >= appMenuList.size()){
            scrollPosition1 = appMenuList.size() -1;
        }
//        gridLayoutManager.scrollToPosition(scrollPosition1);
//        gridLayoutManager.scrollToPositionWithOffset(scrollPosition1,0);
        ((GridLayoutManager) appListView.getLayoutManager()).scrollToPositionWithOffset(scrollPosition1, 0);
//        addListener();
    }

    public void setMenuList(ArrayList<AppMenuInfo> appMenuInfo) {
        if (appMenuInfo != null) {
            this.appMenuList = appMenuInfo;
        }
    }

//    public void responseUpdateAppStatus(String packageName,int status) {
    public void responseUpdateAppStatus(Message message) {
        String packageName = (String) message.obj;
        int status = message.arg1;
        for (int i = 0;i< appMenuList.size();i++){
            if (appMenuList.get(i) != null  && !TextUtils.isEmpty(appMenuList.get(i).getPackageName()) && appMenuList.get(i).getPackageName().equals(packageName)){
                appMenuList.get(i).setAppStatus(status);
                appListAdapter.notifyItemChanged(i);
            }
        }
    }

    private void setAppList0() {
//        appMenuList.clear();
        Log.e("letianpai", "AppMenuUpdateCallback.getInstance().registerAppMenuUpdateListener ============ 2000000 =============appMenuList.size(): " + appMenuList.size());
//        appListAdapter.setAppList(appMenuList);
        appListView.setAdapter(appListAdapter);
    }

    public void goTop() {
        appListView.scrollToPosition(0);
    }

    //设定RecyclerView最大滑动速度
    private void setMaxFlingVelocity(RecyclerView recycleview, int velocity) {
        try {
            Field field = recycleview.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recycleview, velocity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class UpdateViewHandler extends Handler {
        private final WeakReference<Context> context;

        public UpdateViewHandler(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case UPDATE_APP_LIST:
                    updateViews();
                    break;

                case UPDATE_APP_STATUS:
                    responseUpdateAppStatus(msg);
                    break;
            }
        }
    }

    private void updateViews() {
        setAppList();
    }

}