package com.letianpai.robot.desktop.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.RelativeLayout
import com.letianpai.robot.desktop.R
import com.letianpai.robot.desktop.utils.KeyConsts

/**
 * @author liujunbin
 */
abstract class AbstractKeyImageButton : RelativeLayout {
    protected var keyImageButton: AbstractKeyImageButton? = null
    protected var mContext: Context? = null
    protected var buttonImage: ImageView? = null
    protected var rlButtonRoot: RelativeLayout? = null
    protected var pressedImage: Int = 0
    protected var unPressedImage: Int = 0
    protected var isEnable: Boolean = true

    constructor(context: Context?) : super(context) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }


    private fun init(context: Context?) {
        this.mContext = context
        keyImageButton = this@AbstractKeyImageButton
        inflate(mContext, R.layout.key_image_button, this)
        initView()
        setButtonUnPressed()
    }

    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        Log.e("letianpai_test","MotionEvent_"+ event.getAction());
    //        if (event.getAction() == MotionEvent.ACTION_UP){
    //            Log.e("letianpai_test","MotionEvent.ACTION_UP");
    //            setButtonUnPressed();
    //
    //        }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
    //            Log.e("letianpai_test","MotionEvent.ACTION_CANCEL");
    //            setButtonUnPressed();
    //
    //        }else if (event.getAction() == MotionEvent.ACTION_DOWN){
    //            Log.e("letianpai_test","MotionEvent.ACTION_DOWN");
    //            setButtonPressed();
    //        }
    //        return false;
    //    }
    private fun initView() {
        rlButtonRoot = findViewById(R.id.rl_button_root)
        buttonImage = findViewById(R.id.button_image)
        keyImageButton!!.setOnTouchListener(OnTouchListener { v, event ->
            if (!isEnable) {
                return@OnTouchListener false
            }
            if (event.action == MotionEvent.ACTION_UP) {
                Log.e("letianpai_test", "MotionEvent.ACTION_UP")
                setButtonUnPressed()
            } else if (event.action == MotionEvent.ACTION_CANCEL) {
                Log.e("letianpai_test", "MotionEvent.ACTION_CANCEL")
                setButtonUnPressed()
            } else if (event.action == MotionEvent.ACTION_DOWN) {
                Log.e("letianpai_test", "MotionEvent.ACTION_DOWN")
                setButtonPressed()
            }
            false
        })
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (isEnable) {
            super.setOnClickListener(l)
        }
    }
    @JvmName("setPressedImageResource")
    fun setPressedImage(pressedImage: Int) {
        this.pressedImage = pressedImage
    }
    @JvmName("setUnpressedImageResource")
    fun setUnPressedImage(unPressedImage: Int) {
        if (this.unPressedImage == 0) {
            this.unPressedImage = unPressedImage
            setButtonUnPressed()
        } else {
            this.unPressedImage = unPressedImage
        }
    }
    

    abstract fun setButtonPressed()

    //    private void setButtonPressed() {
    //        rlButtonRoot.setBackgroundColor(mContext.getResources().getColor(R.color.keyboard_highlight));
    //        buttonImage.setImageResource(pressedImage);
    //    }
    abstract fun setButtonUnPressed()

    //    private void setButtonUnPressed() {
    //        rlButtonRoot.setBackgroundColor(mContext.getResources().getColor(R.color.image_keyboard_normal));
    //        buttonImage.setImageResource(unPressedImage);
    //
    //    }
    fun setImagePosition(
        position: Int,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        imageWidth: Int,
        imageHeight: Int
    ) {
        val params = LayoutParams(imageWidth, imageHeight)
        //        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (position == KeyConsts.LEFT) {
            params.addRule(ALIGN_PARENT_LEFT)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.TOP) {
            params.addRule(ALIGN_PARENT_TOP)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.RIGHT) {
            params.addRule(ALIGN_PARENT_RIGHT)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.BOTTOM) {
            params.addRule(ALIGN_PARENT_BOTTOM)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.LEFT_TOP) {
            params.addRule(ALIGN_PARENT_LEFT)
            params.addRule(ALIGN_PARENT_TOP)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.RIGHT_TOP) {
            params.addRule(ALIGN_PARENT_RIGHT)
            params.addRule(ALIGN_PARENT_TOP)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.LEFT_BOTTOM) {
            params.addRule(ALIGN_PARENT_LEFT)
            params.addRule(ALIGN_PARENT_BOTTOM)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.RIGHT_BOTTOM) {
            params.addRule(ALIGN_PARENT_RIGHT)
            params.addRule(ALIGN_PARENT_BOTTOM)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.CENTER) {
            params.addRule(CENTER_IN_PARENT)
        } else if (position == KeyConsts.CENTER_VERTICAL) {
            params.addRule(CENTER_VERTICAL)
            params.setMargins(left, top, right, bottom)
        } else if (position == KeyConsts.CENTER_HORIZONTAL) {
            params.addRule(CENTER_HORIZONTAL)
            params.setMargins(left, top, right, bottom)
        }

        //        params.setMargins(left, top, right, bottom);
        buttonImage!!.layoutParams = params
    }


    fun enableButton(enable: Boolean) {
        isEnable = enable
    }
}
