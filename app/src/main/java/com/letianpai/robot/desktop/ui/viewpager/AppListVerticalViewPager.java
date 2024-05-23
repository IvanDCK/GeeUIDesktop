package com.letianpai.robot.desktop.ui.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.letianpai.robot.desktop.apps.adapter.AppListVerticalViewPagerAdapter;
import com.letianpai.robot.desktop.ui.view.AppListVerticalView;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向应用列表
 *
 * @author liujunbin
 */
public class AppListVerticalViewPager extends ViewPager {

    private int preX,preY;
    private Context mContext;
    private AppListVerticalViewPager mViewPager;
    private AppListVerticalViewPagerAdapter adapter;
    private List<View> appShortcutViews = new ArrayList<>();
    private AppListVerticalView appListVerticalView;

    public AppListVerticalViewPager(Context context) {
        super(context);
        init(context);
    }

    public AppListVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.setOverScrollMode(OVER_SCROLL_NEVER);// 去除ViewPager边缘效果
        this.mViewPager = this;
        fillAppShortcutViews();
        adapter = new AppListVerticalViewPagerAdapter(appShortcutViews,mContext);
        this.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        addListeners();
    }

    private void addListeners() {
        addViewPagerChangeListener();

    }

    private void fillAppShortcutViews() {
        appShortcutViews.clear();
        appShortcutViews.add(new View(mContext));
        appListVerticalView= new AppListVerticalView(mContext);
        appShortcutViews.add(appListVerticalView);

    }


    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    private void addViewPagerChangeListener() {
        this.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mViewPager.setCurrentItem(1);
                    mViewPager.setX(mViewPager.getWidth());
                }

            }

            @Override
            public void onPageScrollStateChanged(int status) {

            }
        });
    }

    /**
     *
     */
    public void resetAppListView(boolean updateByOrder){
        Log.e("theme_change_1","======= 4 =======");
        fillAppShortcutViews();
        //TODO
        adapter.refresh(appShortcutViews);

    }

    @Override
    public void setCurrentItem(int item) {

        if (item == 1){
            if (appListVerticalView != null){
                appListVerticalView.goTop();
            }
        }
        super.setCurrentItem(item);
    }


}
