package com.ningsheng.jietong.View.TrimCopyQq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ningsheng.jietong.Activity.TrimCopyQqActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * Created by zhangheng on 2015/12/1.
 */
public class TrimCopyQqSrcView extends View {
    private Bitmap mBitmap;
    private Paint paint;
    private Paint alphaPaint;
    private Path path;
    private int width, height, lineWidth;
    private Canvas canvas;

    public TrimCopyQqSrcView(Context context) {
        super(context);
        init();
    }

    public TrimCopyQqSrcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, null);

        super.onDraw(canvas);
    }

    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
                canvas = new Canvas(mBitmap);
                canvas.drawColor(0x88000000);
                canvas.drawCircle(width, height, TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH, paint);
                canvas.drawPath(path, alphaPaint);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        lineWidth = AndroidUtil.dip2px(getContext(), 1);
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(lineWidth);
        alphaPaint = new Paint(paint);
        alphaPaint.setAlpha(0);
        alphaPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFFFFFFFF);
        width = MyApplication.getScreenWidth() >> 1;
        height = (MyApplication.getScreenHeight() - AndroidUtil.dip2px(getContext(),52)) >> 1;
        path.addCircle(width, height, TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH - lineWidth, Path.Direction.CCW);


    }

}
