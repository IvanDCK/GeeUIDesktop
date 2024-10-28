package com.letianpai.robot.desktop.apps.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.letianpai.robot.components.network.system.SystemUtil
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.apps.adapter.AppListAdapter.AppItemHolder
import com.letianpai.robot.desktop.manager.RobotAppListManager
import com.letianpai.robot.desktop.parser.AppMenuInfo
import com.letianpai.robot.desktop.utils.OpenTypeConsts
import com.letianpai.robot.desktop.utils.ViewUtils

//import com.qihoo.kids.launcher.base.manager.LauncherDifferenceUtil;
//import com.qihoo.kids.launcher.base.manager.LauncherInfoManager;
//import com.qihoo.kids.launcher.base.parser.applist.AppListItem;
//import com.qihoo.kids.launcher.base.util.AppListResourceUtil;
//import com.qihoo.kids.launcher.base.util.ViewUtils;
//import com.qihoo.kids.launcher.commonui.R;
//import com.qihoo.kids.launcher.utils.UIConsts;
/**
 * Vertical Application List View
 * @author liujunbin
 */
class AppListAdapter(context: Context?) : RecyclerView.Adapter<AppItemHolder>() {
    private val mLayoutInflater: LayoutInflater
    private val mContext = context!!

    //    private List<AppListItem> appList;
    private var appList: List<AppMenuInfo?>? = null
    private var appListView: View? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    fun setAppList(appList: List<AppMenuInfo?>?) {
        this.appList = appList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemHolder {
        appListView = mLayoutInflater.inflate(R.layout.app_item_view, parent, false)
        return AppItemHolder(appListView!!)
    }

    override fun onBindViewHolder(holder: AppItemHolder, position: Int) {
        val icon = appList!![position]!!.localIcon
        val appMenuInfo = appList!![position]
        if (appList!![position]!!.openType == OpenTypeConsts.OPEN_APP_USER_APP) {
            if (appList!![position]!!.drawableIcon == null) {
                val drawable: Drawable? = RobotAppListManager.getInstance(mContext)
                    .get3PartAppIconByPackageName(
                        appList!![position]!!.packageName.toString()
                    )
                holder.appIcon.setImageDrawable(drawable)
            } else {
                holder.appIcon.setImageDrawable(appList!![position]!!.drawableIcon)
            }
        } else {
            holder.appIcon.setImageResource(icon)
        }

        var appDisplayName: String? = ""
        appDisplayName = if (SystemUtil.isInChinese) {
            appList!![position]!!.name
        } else {
            appList!![position]!!.en_name
        }
        if (appList!![position]!!.appStatus == 1) {
            holder.ivBlackScreen.visibility = View.VISIBLE
        } else {
            holder.ivBlackScreen.visibility = View.GONE
        }

        holder.appName.text = appDisplayName
        if (appList!![position]!!.appStatus == 1) {
            holder.appName.setTextColor(mContext.getColor(R.color.white60))
        } else {
            holder.appName.setTextColor(mContext.getColor(R.color.white))
        }

        //        if (position % 2 == 0){
//            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
//            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));
//
//        }else if (position % 2 == 1){
//            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));
//            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
//        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(View.OnClickListener { v ->
                if (appMenuInfo!!.appStatus == 1) {
                    return@OnClickListener
                }
                val pos = holder.layoutPosition
                mOnItemClickListener!!.onItemClick(v, pos)
            })

            holder.itemView.setOnLongClickListener {
                val pos = holder.layoutPosition
                mOnItemClickListener!!.onItemLongClick(holder.itemView, pos)
                true
            }
        }


        //        if (LauncherDifferenceUtil.isInternationalized(mContext)){
//            return;
//        }


//        if (appDisplayName.length() > 4){
//            holder.appName.setTextSize(11);
//        }else{
//            holder.appName.setTextSize(13);
//        }
    }

    override fun getItemCount(): Int {
        return if (appList == null) {
            0
        } else {
            appList!!.size
        }
    }

    fun setmOnItemClickListener(listener: OnItemClickListener?) {
        this.mOnItemClickListener = listener
    }


    inner class AppItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llAppIcon: LinearLayout =
            itemView.findViewById<View>(R.id.ll_app_icon) as LinearLayout
        var rlAppIcon: RelativeLayout =
            itemView.findViewById<View>(R.id.rl_app_icon) as RelativeLayout
        var appIcon: ImageView =
            itemView.findViewById<View>(R.id.iv_app_icon) as ImageView
        var ivBlackScreen: ImageView =
            itemView.findViewById<View>(R.id.iv_black_screen) as ImageView
        var appName: TextView =
            itemView.findViewById<View>(R.id.tv_app_title) as TextView
        var leftView: View =
            itemView.findViewById(R.id.left_view) as View
        var rightView: View =
            itemView.findViewById(R.id.right_view) as View


        init {
            //            resizeView(appIcon,llAppIcon,rlAppIcon);
        }
    }

    private fun resizeView(appIcon: ImageView, llAppIcon: LinearLayout, rlAppIcon: RelativeLayout) {
        val height = 480
        val width = 480
        val iconSize = 120

        //        if(height != width){
//            ViewUtils.resizeImageViewSize(appIcon,(width * iconSize) / UIConsts.RECTANGLE_WATCH_WIDTH,(width * iconSize) / UIConsts.RECTANGLE_WATCH_WIDTH);
//            if (appList.get(0).getAppBg() != 0){
//                ViewUtils.resizeLinearLayoutViewSize(rlAppIcon,width/2,width * UIConsts.RECTANGLE_WATCH_BG_HEIGHT / UIConsts.RECTANGLE_WATCH_WIDTH);
//            }
//        }else{
//            ViewUtils.resizeImageViewSize(appIcon,(width * iconSize) / UIConsts.SQUARE_WATCH_WIDTH,(width * iconSize) / UIConsts.SQUARE_WATCH_WIDTH);
//            if (appList.get(0).getAppBg() != 0){
//                ViewUtils.resizeLinearLayoutViewSize(rlAppIcon,width/2,width * UIConsts.SQUARE_WATCH_BG_HEIGHT/UIConsts.SQUARE_WATCH_WIDTH);
//            }
//        }
        ViewUtils.resizeGridLayoutManagerViewSize(llAppIcon, width / 2, width / 2)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }


    companion object {
        private const val PIPILU_THEME_NAME = "pipilu"
    }
}
