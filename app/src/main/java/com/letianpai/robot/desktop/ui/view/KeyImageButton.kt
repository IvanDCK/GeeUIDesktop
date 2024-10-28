package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import com.letianpai.robot.desktop.R

/**
 * @author liujunbin
 */
open class KeyImageButton : AbstractKeyImageButton {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun setButtonPressed() {
        rlButtonRoot!!.setBackgroundColor(mContext!!.resources.getColor(R.color.image_button_pressed_bg))
        buttonImage!!.setImageResource(pressedImage)
    }

    override fun setButtonUnPressed() {
        rlButtonRoot!!.setBackgroundColor(mContext!!.resources.getColor(R.color.image_button_normal_bg))
        buttonImage!!.setImageResource(unPressedImage)
    }

    // Renamed to avoid conflict with property setters
    fun updatePressedImage(pressedImage: Int) {
        this.pressedImage = pressedImage
    }

    fun updateUnPressedImage(unPressedImage: Int) {
        if (this.unPressedImage == 0) {
            this.unPressedImage = unPressedImage
            setButtonUnPressed()
        } else {
            this.unPressedImage = unPressedImage
        }
    }
}
