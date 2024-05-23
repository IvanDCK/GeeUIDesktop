package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.apps.adapter.AppConfigListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DesktopAppListView extends RelativeLayout {
    private Context mContext;
    private RecyclerView recyclerView;
    private static final int UPDATE_APP_LIST = 1;
    private UpdateViewHandler updateViewHandler;
    private AppConfigListAdapter adapter;

    public DesktopAppListView(Context context) {
        super(context);
        initViews(context);
        
    }

    private void initViews(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.app_list_vertical_views, this);
        initView();

    }

    public DesktopAppListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public DesktopAppListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public DesktopAppListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context);
    }

    private void initView() {
        recyclerView = findViewById(R.id.rc_app_list);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        updateViewHandler = new UpdateViewHandler(mContext);
        setData();
    }

    private void setData() {
        adapter = new AppConfigListAdapter(mContext,new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

//    private void setData() {
//        adapter = new AppConfigListAdapter(mContext,new ArrayList<>());
//        recyclerView.setAdapter(adapter);
//    }


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

                    break;



            }
        }
    }

}
