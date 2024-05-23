package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import com.letianpai.robot.desktop.ui.base.AbstractMainView;

/**
 * @author liujunbin
 */
public class RobotMainView extends AbstractMainView {

    private RobotDesktopView robotDesktopView;
    private RobotTopView robotTopView;
    private RobotBottomView robotBottomView;
    private RobotLeftView robotLeftView;
    private RobotRightView robotRightView;


    public RobotMainView(Context context) {
        super(context);
    }

    public RobotMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RobotMainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void responseChangedSpineSkin(String skinPath) {

    }

    @Override
    protected void fillWatchViews() {
        robotDesktopView = new RobotDesktopView(mContext);
        robotTopView = new RobotTopView(mContext);
        robotLeftView = new RobotLeftView(mContext);
        robotRightView = new RobotRightView(mContext);
        robotBottomView = new RobotBottomView(mContext);

        setDesktopView(robotDesktopView);
        setTopView(robotTopView);
        setLeftView(robotLeftView);
//        setRightView(CommonAppListView.getInstance(mContext).getAppListView());
        setRightView(robotRightView);
        setBottomView(robotBottomView);

        setBottomViewDisable(true);
        setLeftViewDisable(true);
        setRightViewDisable(true);
        setTopViewDisable(true);
    }

    @Override
    protected void responseOnClick(Point pt) {
        robotDesktopView.onClick(pt);

    }

    @Override
    protected void responseLongPressOnWatchView() {

    }

    @Override
    protected void updateData() {

    }
}
