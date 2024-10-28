package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.apps.adapter.AppConfigListAdapter
import java.lang.ref.WeakReference

class DesktopAppListView : RelativeLayout {
    private var mContext: Context? = null
    private var recyclerView: RecyclerView? = null
    private var updateViewHandler: UpdateViewHandler? = null
    private var adapter: AppConfigListAdapter? = null

    constructor(context: Context) : super(context) {
        initViews(context)
    }

    private fun initViews(context: Context) {
        this.mContext = context
        inflate(mContext, R.layout.app_list_vertical_views, this)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initViews(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initViews(context)
    }

    private fun initView() {
        recyclerView = findViewById(R.id.rc_app_list)
        recyclerView!!.setLayoutManager(GridLayoutManager(mContext, 2))
        updateViewHandler = UpdateViewHandler(mContext!!)
        setData()
    }

    private fun setData() {
        adapter = AppConfigListAdapter(mContext, ArrayList())
        recyclerView!!.adapter = adapter
    }


    //    private void setData() {
    //        adapter = new AppConfigListAdapter(mContext,new ArrayList<>());
    //        recyclerView.setAdapter(adapter);
    //    }
    private inner class UpdateViewHandler(context: Context) : Handler() {
        private val context =
            WeakReference(context)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                UPDATE_APP_LIST -> {}
            }
        }
    }

    companion object {
        private const val UPDATE_APP_LIST = 1
    }
}
