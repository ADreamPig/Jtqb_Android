package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * Created by hasee-pc on 2015/8/21.
 */
public class ToogleButton extends LinearLayout {
    private GradientDrawable drawable,drawableCircle;
    private View circle;
    private Context mContext;
    private int width, height;
    private boolean isCheck = false;
    private int padding;

    public ToogleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        padding = AndroidUtil.dip2px(mContext, 2);
        drawable = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_tooglebutton);
        drawableCircle = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.bg_tooglebutton_circle);
        setBackgroundDrawable(drawable);
        setGravity(Gravity.CENTER_VERTICAL);
        circle = new View(mContext);
        addView(circle);
        setChecked(isCheck);

    }

    public void setChecked( boolean isCheck) {
        this.isCheck = isCheck;
        if (isCheck) {
            drawable.setColor(0xFF4CDA64);
            drawable.setStroke(0, 0xFFC3C3C3);
        } else {
            drawable.setColor(0xFFF5F5F5);
            drawable.setStroke(AndroidUtil.dip2px(getContext(),0.5f),0xFFC2C2C2);
        }
        circle.post(new Runnable() {
            @Override
            public void run() {
                width = getWidth();
                height = getHeight();
                LayoutParams layoutParams = new LayoutParams(height - padding, height - padding);
                if (ToogleButton.this.isCheck) {
                    drawableCircle.setStroke(0,0xFFC3C3C3);
                    layoutParams.leftMargin = width - (height - padding) - padding;
                } else {
                    drawableCircle.setStroke(AndroidUtil.dip2px(getContext(),1),0xFFC3C3C3);
                    layoutParams.leftMargin = padding-AndroidUtil.dip2px(getContext(),1);
                }
                circle.setBackgroundDrawable(drawableCircle);
                circle.setLayoutParams(layoutParams);
            }
        });


    }
    public boolean isCheck(){
        return isCheck;
    }



}
