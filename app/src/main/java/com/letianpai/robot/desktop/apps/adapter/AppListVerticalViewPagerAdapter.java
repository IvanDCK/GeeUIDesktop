package com.letianpai.robot.desktop.apps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 纵向应用列表Adapter
 * @author liujunbin
 */
public class AppListVerticalViewPagerAdapter extends PagerAdapter {
    private List<View> appListViews = new ArrayList<>();
    private Context mContext;


    public AppListVerticalViewPagerAdapter(List<View> appList, Context context){
        this.appListViews.addAll(appList);
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return appListViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position < appListViews.size()){
            View view = appListViews.get(position);
            container.addView(view);
            return view;
        }
        return null;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if ((appListViews.size()>0)&&(position < appListViews.size())){
            container.removeView(appListViews.get(position));
        }
    }


    public void refresh(List<View> appList) {
        this.appListViews.clear();
        this.appListViews.addAll(appList);
//        for (int i = 0; i < appListViews.size(); i++){
//            this.appListViews.set(i,appList.get(i));
//        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
