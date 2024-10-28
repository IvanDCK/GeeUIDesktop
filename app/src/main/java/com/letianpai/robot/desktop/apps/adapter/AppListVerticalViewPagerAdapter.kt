package com.letianpai.robot.desktop.apps.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Vertical Application List Adapter
 * @author liujunbin
 */
class AppListVerticalViewPagerAdapter(appList: List<View>, context: Context?) :
    PagerAdapter() {
    private val appListViews: MutableList<View> = ArrayList()
    private val mContext: Context?


    init {
        appListViews.addAll(appList)
        this.mContext = context
    }

    override fun getCount(): Int {
        return appListViews.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return if (position < appListViews.size) {
            val view = appListViews[position]
            container.addView(view)
            view
        } else {
            val placeholderView = View(container.context)
            container.addView(placeholderView)
            placeholderView
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if ((appListViews.size > 0) && (position < appListViews.size)) {
            container.removeView(appListViews[position])
        }
    }


    fun refresh(appList: List<View>) {
        appListViews.clear()
        appListViews.addAll(appList)
        //        for (int i = 0; i < appListViews.size(); i++){
//            this.appListViews.set(i,appList.get(i));
//        }
        notifyDataSetChanged()
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}
