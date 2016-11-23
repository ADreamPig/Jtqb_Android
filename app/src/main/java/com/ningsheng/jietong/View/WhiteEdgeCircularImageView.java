package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * Created by zhangheng on 2015/12/22.
 */
public class WhiteEdgeCircularImageView extends CircularImageView {
    private int strokeWidth;
    private Paint paint;

    public WhiteEdgeCircularImageView(Context context) {
        super(context);
        init();
    }

    public WhiteEdgeCircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        strokeWidth = AndroidUtil.dip2px(getContext(),1);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() >> 1, getHeight() >> 1, (getWidth() - strokeWidth) >> 1, paint);
    }

    @Override
    protected Bitmap getBitmapFromClass(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, getWidth() - strokeWidth, getHeight() - strokeWidth, true);
    }

    @Override
    protected void drawBitmapFromClass(Canvas canvas, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, strokeWidth>>1, strokeWidth>>1, null);
    }
}
