package com.letianpai.robot.desktop.apps.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.qihoo.kids.launcher.base.manager.LauncherDifferenceUtil;
//import com.qihoo.kids.launcher.base.manager.LauncherInfoManager;
//import com.qihoo.kids.launcher.base.parser.applist.AppListItem;
//import com.qihoo.kids.launcher.base.util.AppListResourceUtil;
//import com.qihoo.kids.launcher.base.util.ViewUtils;
//import com.qihoo.kids.launcher.commonui.R;
//import com.qihoo.kids.launcher.utils.UIConsts;

import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.manager.RobotAppListManager;
import com.letianpai.robot.desktop.parser.AppMenuInfo;
import com.letianpai.robot.desktop.utils.OpenTypeConsts;
import com.letianpai.robot.desktop.utils.RoundImageHelper;
import com.letianpai.robot.desktop.utils.ViewUtils;

import java.util.List;

/**
 * 纵向应用列表View
 * @author liujunbin
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppItemHolder>{

    private LayoutInflater mLayoutInflater;
    private Context mContext;
//    private List<AppListItem> appList;
    private List<AppMenuInfo> appList;
    private View appListView;
    private OnItemClickListener mOnItemClickListener;
    private static final String PIPILU_THEME_NAME = "pipilu";

    public AppListAdapter(Context context){
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    public void setAppList(List<AppMenuInfo> appList) {
        this.appList = appList;
        this.notifyDataSetChanged();

    }

    @Override
    public AppItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        appListView = mLayoutInflater.inflate(R.layout.app_item_view,parent,false);
        return new AppItemHolder(appListView);
    }

    @Override
    public void onBindViewHolder(final AppItemHolder holder, final int position) {
        int icon = appList.get(position).getLocalIcon();
        AppMenuInfo appMenuInfo = appList.get(position);
        if (appList.get(position).getOpenType() == OpenTypeConsts.OPEN_APP_USER_APP ){
            if (appList.get(position).getDrawableIcon() == null){
                Drawable drawable = RobotAppListManager.getInstance(mContext).get3PartAppIconByPackageName(appList.get(position).getPackageName());
                holder.appIcon.setImageDrawable(drawable);
            }else{
                holder.appIcon.setImageDrawable(appList.get(position).getDrawableIcon());
            }

        }else{
            holder.appIcon.setImageResource(icon);
        }

        String appDisplayName = "";
        if (SystemUtil.isInChinese()){
            appDisplayName = appList.get(position).getName();
        }else{
            appDisplayName = appList.get(position).getEn_name();
        }
        if (appList.get(position).getAppStatus() == 1){
            holder.ivBlackScreen.setVisibility(View.VISIBLE);
        }else{
            holder.ivBlackScreen.setVisibility(View.GONE);
        }

        holder.appName.setText(appDisplayName);
        if (appList.get(position).getAppStatus() == 1){
            holder.appName.setTextColor(mContext.getColor(R.color.white60));
        }else{
            holder.appName.setTextColor(mContext.getColor(R.color.white));
        }

//        if (position % 2 == 0){
//            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
//            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));
//
//        }else if (position % 2 == 1){
//            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));
//            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
//        }

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appMenuInfo.getAppStatus() == 1){
                        return;
                    }
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                    return true;
                }
            });
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

    @Override
    public int getItemCount() {
        if (appList == null){
            return 0;
        }else{
            return appList.size();
        }
    }

    public void setmOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }


    class AppItemHolder extends RecyclerView.ViewHolder{
        LinearLayout llAppIcon;
        RelativeLayout rlAppIcon;
        ImageView appIcon;
        ImageView ivBlackScreen;
        TextView appName;
        View leftView;
        View rightView;


        public AppItemHolder(View itemView) {
            super(itemView);
            llAppIcon = (LinearLayout) itemView.findViewById(R.id.ll_app_icon);
            rlAppIcon = (RelativeLayout) itemView.findViewById(R.id.rl_app_icon);
            appIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            ivBlackScreen = (ImageView) itemView.findViewById(R.id.iv_black_screen);
            appName = (TextView) itemView.findViewById(R.id.tv_app_title);
            leftView = (View) itemView.findViewById(R.id.left_view);
            rightView = (View)itemView.findViewById(R.id.right_view);
//            resizeView(appIcon,llAppIcon,rlAppIcon);

        }
    }

    private void resizeView(ImageView appIcon,LinearLayout llAppIcon,RelativeLayout rlAppIcon) {
        int height = 480;
        int width = 480;
        int iconSize = 120;

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
        ViewUtils.resizeGridLayoutManagerViewSize(llAppIcon,width/2,width/2);

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


}
