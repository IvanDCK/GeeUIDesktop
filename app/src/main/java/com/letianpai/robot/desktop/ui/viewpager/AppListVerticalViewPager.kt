package com.letianpai.robot.desktop.ui.viewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.letianpai.robot.desktop.apps.adapter.AppListVerticalViewPagerAdapter
import com.letianpai.robot.desktop.ui.view.AppListVerticalView

/**
 * Vertical Application List
 *
 * @author liujunbin
 */
class AppListVerticalViewPager : ViewPager {
    private val preX = 0
    private val preY = 0
    private var mContext: Context? = null
    private var mViewPager: AppListVerticalViewPager? = null
    private var adapter: AppListVerticalViewPagerAdapter? = null
    private val appShortcutViews: MutableList<View> = ArrayList()
    private var appListVerticalView: AppListVerticalView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        this.mContext = context
        this.overScrollMode = OVER_SCROLL_NEVER // Remove ViewPager edge effect
        this.mViewPager = this
        fillAppShortcutViews()
        adapter = AppListVerticalViewPagerAdapter(appShortcutViews, mContext)
        this.setAdapter(adapter)
        mViewPager!!.currentItem = 1
        addListeners()
    }

    private fun addListeners() {
        addViewPagerChangeListener()
    }

    private fun fillAppShortcutViews() {
        appShortcutViews.clear()
        appShortcutViews.add(View(mContext))
        appListVerticalView = AppListVerticalView(mContext)
        appShortcutViews.add(appListVerticalView!!)
    }


    override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) {
        super.onPageScrolled(position, offset, offsetPixels)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    private fun addViewPagerChangeListener() {
        this.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(i: Int) {
                if (i == 0) {
                    mViewPager!!.currentItem = 1
                    mViewPager!!.x = mViewPager!!.width.toFloat()
                }
            }

            override fun onPageScrollStateChanged(status: Int) {
            }
        })
    }

    fun resetAppListView(updateByOrder: Boolean) {
        Log.e("theme_change_1", "======= 4 =======")
        fillAppShortcutViews()
        //TODO
        adapter!!.refresh(appShortcutViews)
    }

    override fun setCurrentItem(item: Int) {
        if (item == 1) {
            if (appListVerticalView != null) {
                appListVerticalView!!.goTop()
            }
        }
        super.setCurrentItem(item)
    }
}
