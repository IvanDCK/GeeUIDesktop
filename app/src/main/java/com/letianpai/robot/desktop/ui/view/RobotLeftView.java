package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.letianpai.robot.desktop.MainActivity;
import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.ui.base.LeftView;

/**
 * @author liujunbin
 */
public class RobotLeftView extends LeftView {

    private RobotMainView watchMainView;
//    private RobotExpressionView robotExpressionView;
private CommitButton openSettings;

    public RobotLeftView(Context context) {
        super(context);
    }

    public RobotLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotLeftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RobotLeftView(RobotMainView view, Context context) {
        super(view, context);
        this.watchMainView = view;
    }

    @Override
    protected void initView() {
        super.initView();
        inflate(mContext, R.layout.robot_left_view, this);
//        robotExpressionView = findViewById(R.id.expression_view);
        openSettings = findViewById(R.id.openSettings);
        openSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }

    private void exitApp() {
        ((MainActivity)mContext).finish();
//        System.exit(0);
    }


    public void onHide(){
        Log.d("VoiceServiceView", "=== onHide()");

//        if (game.getScreen() != null) {
//            ((BaseScreen) game.getScreen()).setViewShowing(false);
//            ((BaseScreen) game.getScreen()).onHide();
//        }
    }


    public void hideLeftView(){
        watchMainView.onFlitVoiceService();
    }



    @Override
    public void hideView() {
        super.hideView();
        watchMainView.onFlitVoiceService();
    }
}
