package com.letianpai.robot.desktop.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.letianpai.robot.desktop.R;
import com.letianpai.robot.desktop.utils.KeyConsts;

/**
 * @author liujunbin
 */
public abstract class AbstractKeyImageButton extends RelativeLayout {
    protected AbstractKeyImageButton keyImageButton;
    protected Context mContext;
    protected ImageView buttonImage;
    protected RelativeLayout rlButtonRoot;
    protected int pressedImage;
    protected int unPressedImage;
    protected boolean isEnable = true;

    public AbstractKeyImageButton(Context context) {
        super(context);
        init(context);
    }

    public AbstractKeyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractKeyImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AbstractKeyImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        keyImageButton = AbstractKeyImageButton.this;
        inflate(mContext, R.layout.key_image_button,this);
        initView();
        setButtonUnPressed();

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

    private void initView() {
        rlButtonRoot = findViewById(R.id.rl_button_root);
        buttonImage = findViewById(R.id.button_image);
        keyImageButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isEnable){
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    Log.e("letianpai_test","MotionEvent.ACTION_UP");
                    setButtonUnPressed();

                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){
                    Log.e("letianpai_test","MotionEvent.ACTION_CANCEL");
                    setButtonUnPressed();

                }else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.e("letianpai_test","MotionEvent.ACTION_DOWN");
                    setButtonPressed();
                }
                return false;
            }
        });

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (isEnable){
            super.setOnClickListener(l);
        }
    }

    public void setPressedImage(int pressedImage) {
        this.pressedImage = pressedImage;
    }

    public void setUnPressedImage(int unPressedImage) {
        if (this.unPressedImage == 0){
            this.unPressedImage = unPressedImage;
            setButtonUnPressed();
        }else{
            this.unPressedImage = unPressedImage;
        }

    }

    public abstract void setButtonPressed();
//    private void setButtonPressed() {
//        rlButtonRoot.setBackgroundColor(mContext.getResources().getColor(R.color.keyboard_highlight));
//        buttonImage.setImageResource(pressedImage);
//    }

    public abstract void setButtonUnPressed();
//    private void setButtonUnPressed() {
//        rlButtonRoot.setBackgroundColor(mContext.getResources().getColor(R.color.image_keyboard_normal));
//        buttonImage.setImageResource(unPressedImage);
//
//    }

    public void setImagePosition(int position,int left,int top,int right,int bottom,int imageWidth,int imageHeight) {
        LayoutParams params = new LayoutParams(imageWidth, imageHeight);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (position == KeyConsts.LEFT){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.TOP){
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.RIGHT){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.BOTTOM){
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.LEFT_TOP){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.RIGHT_TOP){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.LEFT_BOTTOM){
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.RIGHT_BOTTOM){
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.CENTER){
            params.addRule(RelativeLayout.CENTER_IN_PARENT);

        }else if(position == KeyConsts.CENTER_VERTICAL){
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.setMargins(left, top, right, bottom);

        }else if(position == KeyConsts.CENTER_HORIZONTAL){
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(left, top, right, bottom);
        }

//        params.setMargins(left, top, right, bottom);
        buttonImage.setLayoutParams(params);
    }



    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
