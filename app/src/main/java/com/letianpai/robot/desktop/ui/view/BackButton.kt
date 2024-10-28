package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.utils.KeyConsts

class BackButton : KeyImageButton {
    constructor(context: Context?) : super(context) {
        initData()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initData()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initData()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initData()
    }

    private fun initData() {
        keyImageButton!!.setPressedImage(R.drawable.back_press)
        keyImageButton!!.setUnPressedImage(R.drawable.back_normal)
        keyImageButton!!.setImagePosition(KeyConsts.Companion.CENTER, 0, 0, 0, 0, 40, 40)
    }
}
