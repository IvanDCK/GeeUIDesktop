package com.letianpai.robot.desktop.apps.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.letianpai.robot.components.network.system.SystemUtil;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.parser.AppMenuInfo;

import java.util.List;

public class AppConfigListAdapter extends RecyclerView.Adapter<AppConfigListAdapter.ViewHolder> {

    private List<AppMenuInfo> appList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public AppConfigListAdapter(Context context,List<AppMenuInfo> data) {
        this.mContext = context;
        this.appList = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_view_online, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int icon = appList.get(position).getLocalIcon();
        holder.appIcon.setImageResource(icon);

        String appDisplayName = "";
        if (SystemUtil.isInChinese()){
            appDisplayName = appList.get(position).getName();
        }else{
            appDisplayName = appList.get(position).getEn_name();
        }
        Log.e("letianpai","appDisplayName: "+ appDisplayName);
        holder.appName.setText(appDisplayName);
        holder.appName.setTextColor(mContext.getColor(R.color.white));
        if (position % 2 == 0){
            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));

        }else if (position % 2 == 1){
            holder.leftView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 17.0f));
            holder.rightView.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 113.0f));
        }
        holder.llAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("letianpai","appDisplayName: ");
            }
        });

        holder.llAppIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });


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

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llAppIcon;
        RelativeLayout rlAppIcon;
        ImageView appIcon;
        TextView appName;
        View leftView;
        View rightView;


        public ViewHolder(View itemView) {
            super(itemView);
            llAppIcon = (LinearLayout) itemView.findViewById(R.id.ll_app_icon);
            rlAppIcon = (RelativeLayout) itemView.findViewById(R.id.rl_app_icon);
            appIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            appName = (TextView) itemView.findViewById(R.id.tv_app_title);
            leftView = (View) itemView.findViewById(R.id.left_view);
            rightView = (View)itemView.findViewById(R.id.right_view);
//            resizeView(appIcon,llAppIcon,rlAppIcon);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

}
