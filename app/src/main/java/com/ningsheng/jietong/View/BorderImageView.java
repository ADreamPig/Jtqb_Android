package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhushunqing on 2016/4/22.
 */
public class BorderImageView extends ImageView {
    private String namespace="http://shadow.com";
    private int color;
    public BorderImageView(Context context,AttributeSet attrs) {
        super(context,attrs);
//        color= Color.parseColor(attrs.getAttributeValue(namespace, "BorderColor"));
        color=0xE6E6E6;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //画边框
        Rect rec=canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint=new Paint();
        paint.setColor(color);
//        paint.
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rec, paint);
    }
}
