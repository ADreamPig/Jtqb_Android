package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hasee-pc on 2016/3/14.
 */
public class UpLoadView extends View {
    private int measureHeight;
    private int measureWidth;
    private float textHeight;
    private Paint paint;
    private Paint paint1;
    private float progress = 0;

    public UpLoadView(Context context) {
        super(context);
        init();
    }

    public UpLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(0x66000000);
        paint1 = new Paint();
        paint1.setColor(0xFFFFFFFF);
        paint1.setTextSize(40);
        Paint.FontMetrics fontMetrics = paint1.getFontMetrics();
        textHeight = fontMetrics.ascent - fontMetrics.bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = getMeasuredWidth();
        measureHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText((int) (progress * 100) + "%", (measureWidth - paint1.measureText((int) (progress * 100) + "%")) / 2, (measureHeight - textHeight) / 2, paint1);
        canvas.drawRect(0, 0, measureWidth, measureHeight * (1 - progress), paint);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public Paint getPaint() {
        return paint;
    }

    public Paint getPaint1() {
        return paint1;
    }

    public float getTextHeight() {
        return textHeight;
    }

    public int getMeasureHeight() {
        return measureHeight;
    }

    public int getMeasureWidth() {
        return measureWidth;
    }
}
