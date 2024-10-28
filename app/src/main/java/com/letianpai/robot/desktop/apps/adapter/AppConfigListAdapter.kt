package com.letianpai.robot.desktop.apps.adapter

import android.content.Context
import android.util.Log
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
import com.letianpai.robot.desktop.parser.AppMenuInfo

class AppConfigListAdapter(private val mContext: Context?, private val appList: List<AppMenuInfo>) :
    RecyclerView.Adapter<AppConfigListAdapter.ViewHolder>() {
    private val mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.app_item_view_online, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = appList[position].localIcon
        holder.appIcon.setImageResource(icon)

        var appDisplayName: String? = ""
        appDisplayName = if (SystemUtil.isInChinese) {
            appList[position].name
        } else {
            appList[position].en_name
        }
        Log.e("letianpai", "appDisplayName: $appDisplayName")
        holder.appName.text = appDisplayName
        holder.appName.setTextColor(mContext!!.getColor(R.color.white))
        if (position % 2 == 0) {
            holder.leftView.layoutParams = LinearLayout.LayoutParams(0, 0, 113.0f)
            holder.rightView.layoutParams = LinearLayout.LayoutParams(0, 0, 17.0f)
        } else if (position % 2 == 1) {
            holder.leftView.layoutParams = LinearLayout.LayoutParams(0, 0, 17.0f)
            holder.rightView.layoutParams = LinearLayout.LayoutParams(0, 0, 113.0f)
        }
        holder.llAppIcon.setOnClickListener {
            Log.e(
                "letianpai",
                "appDisplayName: "
            )
        }

        holder.llAppIcon.setOnLongClickListener { false }


        //        if (mOnItemClickListener != null){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickListener.onItemClick(v,pos);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
//                    return true;
//                }
//            });
//        }
    }

    override fun getItemCount(): Int {
        return 0
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llAppIcon: LinearLayout =
            itemView.findViewById<View>(R.id.ll_app_icon) as LinearLayout
        var rlAppIcon: RelativeLayout =
            itemView.findViewById<View>(R.id.rl_app_icon) as RelativeLayout
        var appIcon: ImageView =
            itemView.findViewById<View>(R.id.iv_app_icon) as ImageView
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

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)

        fun onItemLongClick(view: View?, position: Int)
    }
}
