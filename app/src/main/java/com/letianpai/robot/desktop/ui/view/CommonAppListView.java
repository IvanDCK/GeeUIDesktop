package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.view.View;

import com.letianpai.robot.desktop.ui.base.AbstractMainView;
import com.letianpai.robot.desktop.ui.viewpager.AppListVerticalViewPager;

/**
 * 应用列表页面管理类
 *
 * @author liujunbin
 */
public class CommonAppListView {
    private View currentView;
    private Context mContext;
    private static CommonAppListView mCommonAppListView;

    public CommonAppListView(Context context) {
        this.mContext = context;
    }

    public static CommonAppListView getInstance(Context context) {
        if (mCommonAppListView == null) {
            mCommonAppListView = new CommonAppListView(context);
        }
        return mCommonAppListView;
    }

    public View getAppListView(AbstractMainView mainView) {
        return getAppListView();
    }

    public View getAppListView() {

        currentView = null;

        currentView = new AppListVerticalViewPager(mContext);

        return currentView;
    }

    public void setToFirstPosition() {


        //TODO 添加为其他两种类型的处理逻辑
    }

    public void restoreToOriginalPosition() {
         if (currentView instanceof AppListVerticalViewPager) {
            ((AppListVerticalViewPager) currentView).setCurrentItem(1);
        }

        //TODO 添加为其他两种类型的处理逻辑
    }




}
