package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.desktop.R

/**
 * Submit button
 * @author liujunbin
 */
class CommitButton : AbstractKeyButton {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun initData() {
//        keyButton.setText("Settings");
    }

    override fun setButtonUnPressed() {
//        keyButton.setTextColor(mContext.getColor(R.color.commit_background_color));

        keyButton!!.setTextColor(mContext!!.getColor(R.color.white))
    }

    override fun setButtonPressed() {
//        keyButton.setTextColor(mContext.getColor(R.color.white));
        keyButton!!.setTextColor(mContext!!.getColor(R.color.commit_background_color))
    }
}
