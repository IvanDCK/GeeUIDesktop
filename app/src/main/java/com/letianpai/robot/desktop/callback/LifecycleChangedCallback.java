package com.letianpai.robot.desktop.callback;

import java.util.ArrayList;
import java.util.List;

public class LifecycleChangedCallback {
    public static final String ON_PAUSE = "onPause";
    public static final String ON_RESUME = "onResume";
    public static final String ON_DESTROY = "onDestroy";
    public static final String ON_CREATE = "onCreate";

    private List<LifecycleChangedListener> mListenerList = new ArrayList<>();

    private static class LifecycleChangedCallbackHolder {
        private static LifecycleChangedCallback instance = new LifecycleChangedCallback();
    }

    private LifecycleChangedCallback() {

    }

    public static LifecycleChangedCallback getInstance() {
        return LifecycleChangedCallback.LifecycleChangedCallbackHolder.instance;
    }

    public interface LifecycleChangedListener {
        void onLifecycleChange(String lifecycle);
    }

    public void addLifecycleChangedListener (LifecycleChangedListener listener) {
        this.mListenerList.add(listener);
    }
    public void removeLifecycleChangedListener (LifecycleChangedListener listener) {
        this.mListenerList.remove(listener);
    }

    public void setLifecycle(String lifecycle) {
        for (int i = 0;i< mListenerList.size();i++){
            if (mListenerList.get(i)!= null) {
                mListenerList.get(i).onLifecycleChange(lifecycle);
            }
        }

    }
}
