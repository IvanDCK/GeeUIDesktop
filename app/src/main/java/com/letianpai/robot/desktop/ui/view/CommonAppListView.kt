package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.view.View
import com.letianpai.robot.desktop.ui.base.AbstractMainView
import com.letianpai.robot.desktop.ui.viewpager.AppListVerticalViewPager

/**
 * 应用列表页面管理类
 *
 * @author liujunbin
 */
class CommonAppListView(private val mContext: Context) {
    private var currentView: View? = null

    fun getAppListView(mainView: AbstractMainView?): View {
        return appListView
    }

    val appListView: View
        get() {
            currentView = AppListVerticalViewPager(mContext)
            return currentView!!
        }

    fun setToFirstPosition() {
        //TODO Add processing logic for the other two types
    }

    fun restoreToOriginalPosition() {
        if (currentView is AppListVerticalViewPager) {
            (currentView as AppListVerticalViewPager).currentItem = 1
        }

        //TODO Add processing logic for the other two types
    }


    companion object {
        private var mCommonAppListView: CommonAppListView? = null

        fun getInstance(context: Context): CommonAppListView {
            if (mCommonAppListView == null) {
                mCommonAppListView = CommonAppListView(context)
            }
            return mCommonAppListView!!
        }
    }
}
