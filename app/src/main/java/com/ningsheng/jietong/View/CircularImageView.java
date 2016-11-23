package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zhangheng on 2015/12/22.
 */
public class CircularImageView extends ImageView {
    private Paint paint;
    private Bitmap bitmap_;

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap_==null) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                if (drawable instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    int midden = Math.min(bitmap.getWidth(), bitmap.getHeight());
                    int x = bitmap.getWidth() - midden > 0 ? (bitmap.getWidth() - midden) >> 1 : 0;
                    int y = bitmap.getHeight() - midden > 0 ? (bitmap.getHeight() - midden) >> 1 : 0;
                    Bitmap mBitmap = Bitmap.createBitmap(bitmap, x, y, midden, midden);
                    bitmap_ = Bitmap.createBitmap(midden, midden, Bitmap.Config.ARGB_8888);
                    Canvas mCanvas = new Canvas(bitmap_);
                    mCanvas.drawCircle(midden >> 1, midden >> 1, midden >> 1, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    mCanvas.drawBitmap(mBitmap, 0, 0, paint);
                    paint.setColor(0xFF000000);
                    paint.setXfermode(null);
                    bitmap_ = getBitmapFromClass(bitmap_);
                    drawBitmapFromClass(canvas, bitmap_);
                }
            }
        } else {
            drawBitmapFromClass(canvas, bitmap_);
        }

    }

    protected void drawBitmapFromClass(Canvas canvas, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected Bitmap getBitmapFromClass(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, getWidth(), getHeight(), true);
    }

    public void clear() {
        bitmap_ = null;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        clear();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        clear();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        clear();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        clear();
    }
}
