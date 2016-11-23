package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;

/**
 * Created by hasee-pc on 2016/3/17.
 */
public class CircularUpLoadView extends UpLoadView {
    public CircularUpLoadView(Context context) {
        super(context);
    }

    public CircularUpLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint1().setTextSize(30);
        canvas.drawText((int) (getProgress() * 100) + "%", (getMeasureWidth() - getPaint1().measureText((int) (getProgress() * 100) + "%")) / 2, (getMeasureHeight() - getTextHeight()) / 2, getPaint1());
        Paint paint = getPaint();
        Bitmap bitmap = Bitmap.createBitmap(getMeasureWidth(), getMeasureHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap);
        paint.setColor(0x55FFFFFF);
        canvas1.drawCircle(getMeasureWidth() >> 1, getMeasureHeight() >> 1, getMeasureWidth() >> 1, paint);
        paint.setColor(0xFF000000);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas1.drawRect(0, 0, getMeasureWidth(), getMeasureHeight() * (1 - getProgress()), paint);
        paint.setXfermode(null);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}
