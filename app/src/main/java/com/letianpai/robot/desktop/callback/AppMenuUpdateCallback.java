package com.letianpai.robot.desktop.callback;

import com.letianpai.robot.desktop.parser.AppMenuInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujunbin
 */

public class AppMenuUpdateCallback {
    private List<AppMenuUpdateListener> mAppMenuUpdateListener = new ArrayList();

    private static class AppMenuCallbackHolder {
        private static AppMenuUpdateCallback instance = new AppMenuUpdateCallback();
    }

    private AppMenuUpdateCallback() {

    }

    public static AppMenuUpdateCallback getInstance() {
        return AppMenuCallbackHolder.instance;
    }

    public interface AppMenuUpdateListener {
        void onAppMenuUpdated(ArrayList<AppMenuInfo> appMenuInfo);
        void onAppStatusUpdated(String packageName,int status);
    }

    public void registerAppMenuUpdateListener(AppMenuUpdateListener listener) {
        if (mAppMenuUpdateListener != null && !mAppMenuUpdateListener.contains(listener)) {
            mAppMenuUpdateListener.add(listener);
        }
    }

    public void unregisterAppMenuUpdateListener(AppMenuUpdateListener listener) {
        if (mAppMenuUpdateListener != null) {
            mAppMenuUpdateListener.remove(listener);
        }
    }

    //TODO 更改成对象传递
    public void setAppMenuUpdate(ArrayList<AppMenuInfo> appMenuInfo) {
        for (int i = 0; i < mAppMenuUpdateListener.size(); i++) {
            if (mAppMenuUpdateListener.get(i) != null) {
                mAppMenuUpdateListener.get(i).onAppMenuUpdated(appMenuInfo);
            }
        }
    }
    //TODO 更改成对象传递
    public void setAppStatusUpdate(String packageName,int status) {
        for (int i = 0; i < mAppMenuUpdateListener.size(); i++) {
            if (mAppMenuUpdateListener.get(i) != null) {
                mAppMenuUpdateListener.get(i).onAppStatusUpdated(packageName,status);
            }
        }
    }

}
