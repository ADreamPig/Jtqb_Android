package com.ningsheng.jietong.View.TrimCopyQq;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.ningsheng.jietong.Activity.TrimCopyQqActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * Created by zhangheng on 2015/12/2.
 */
public class TrimCopyQqImageView extends ImageView implements GestureDetector.OnGestureListener {
    private Matrix matrix;
    private Bitmap mBitmap;
    private boolean isInitialise;
    private int defaultWidth, defaultHeight;
    private float translateX, translateY;
    private float paddingLeft, paddingTop;
    private float scale = 1.0f, firstScale, maxScale = 10;
    private TouchManager touchManager;
    private Vector2D vector2D;
    private GestureDetector gestureDetector;

    public TrimCopyQqImageView(Context context) {
        super(context);
        init();
    }

    public TrimCopyQqImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        matrix = new Matrix();
        touchManager = new TouchManager();
        vector2D = new Vector2D();
        defaultHeight = (MyApplication.getScreenHeight() - AndroidUtil.dip2px(getContext(),52));
        paddingLeft = (MyApplication.getScreenWidth() - TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2) >> 1;
        paddingTop = (defaultHeight - TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2) >> 1;
        gestureDetector = new GestureDetector(getContext(), this);

        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                System.out.println("onSingleTapConfirmed");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                System.out.println("onDoubleTap");
                scale *= 1.5f;
                if (scale > 10)
                    scale = firstScale;
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                System.out.println("onDoubleTapEvent");
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInitialise) {

        } else {
            isInitialise = true;
            Drawable drawable = getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                mBitmap = ((BitmapDrawable) drawable).getBitmap();
                int midden = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
                scale = TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2.0f / midden;
                scale = scale <= 2.5f ? 2.5f : scale;
                firstScale = scale;
            } else {
                super.onDraw(canvas);
                return;
            }
        }

        if (scale * mBitmap.getHeight() < TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 || scale * mBitmap.getWidth() < TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2) {
            int midden = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
            scale = TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2.0f / midden;
        }
        translateX = (getWidth() - mBitmap.getWidth() * scale) / 2;
        translateY = (defaultHeight - mBitmap.getHeight() * scale) / 2;
        /*
        控制移动范围
         */
        if (vector2D.getX() > paddingLeft - translateX) {
            vector2D.setX(paddingLeft - translateX);
        }
        if (vector2D.getY() > paddingTop - translateY) {
            vector2D.setY(paddingTop - translateY);
        }
        if (vector2D.getX() < translateX - paddingLeft) {
            vector2D.setX(translateX - paddingLeft);
        }
        if (vector2D.getY() < translateY - paddingTop) {
            vector2D.setY(translateY - paddingTop);
        }
//        bitmapWidth = bitmapWidth == 0 ? mBitmap.getWidth() : bitmapWidth;
//        bitmapHeight = bitmapHeight == 0 ? mBitmap.getHeight() : bitmapHeight;
        System.out.println(vector2D.toString());
        System.out.println("bitmap.Width:" + mBitmap.getWidth() + "bitmap.Height:" + mBitmap.getHeight() + "x:" + (int) ((paddingLeft - Math.abs(translateX)) / 2 - Math.abs(vector2D.getX()) / 2) + "y:" + (int) ((paddingTop - Math.abs(translateY)) / 2 - Math.abs(vector2D.getY()) / 2) + "width:" + TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 / scale);
        System.out.println("scale:" + scale + " paddingLeft:" + paddingLeft + " paddingTop:" + paddingTop + " translateX:" + translateX + " translateY:" + translateY);
        matrix.reset();
        matrix.preScale(scale, scale);
        matrix.preTranslate(translateX / scale, translateY / scale);
        matrix.postTranslate(vector2D.getX(), vector2D.getY());
        canvas.drawBitmap(mBitmap, matrix, null);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        touchManager.update(event);
        switch (touchManager.getPointCount()) {
            case 1:
                vector2D.add(touchManager.move());
                break;
            case 2:
                Vector2D vector2D1 = touchManager.getPostVector2D();
                Vector2D vector2D2 = touchManager.getPreVector2D();
                scale *= vector2D1.length() / vector2D2.length();
                if (scale > 10)
                    scale = 10;
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 计算裁剪区域
     *
     * @return
     */
    public Bitmap trim() {
        int width = (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 / (mBitmap.getWidth() * scale) * mBitmap.getWidth());
        int height = (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 / (mBitmap.getHeight() * scale) * mBitmap.getHeight());
        int x = 0;
        int y = 0;
        if (vector2D.getX() > 0) {
            x = (int) (((((mBitmap.getWidth() - width) * 1.0f) / mBitmap.getWidth()) * mBitmap.getWidth() * scale / 2 - vector2D.getX()) / (mBitmap.getWidth() * scale) * mBitmap.getWidth());
        } else if (vector2D.getX() < 0) {
            x = (int) (((((mBitmap.getWidth() - width) * 1.0f) / mBitmap.getWidth()) * mBitmap.getWidth() * scale / 2 - vector2D.getX()) / (mBitmap.getWidth() * scale) * mBitmap.getWidth());
        } else {
            x = (mBitmap.getWidth() - width) >> 1;
        }
        if (vector2D.getY() > 0) {
            y = (int) (((mBitmap.getHeight() - height) * 1.0f / mBitmap.getHeight() * mBitmap.getHeight() * scale / 2 - vector2D.getY()) / (mBitmap.getHeight() * scale) * mBitmap.getHeight());
        } else if (vector2D.getY() < 0) {
            y = (int) (((mBitmap.getHeight() - height) * 1.0f / mBitmap.getHeight() * mBitmap.getHeight() * scale / 2 - vector2D.getY()) / (mBitmap.getHeight() * scale) * mBitmap.getHeight());
        } else {
            y = (mBitmap.getHeight() - height) >> 1;
        }
        System.out.println("x:" + x + "y:" + y + "width:" + width + "height:" + height);
//        return Bitmap.createBitmap(mBitmap, (int) ((paddingLeft - Math.abs(translateX)) / 2 - Math.abs(vector2D.getX()) / 2), (int) ((paddingTop - Math.abs(translateY)) / 2 - Math.abs(vector2D.getY()) / 2), (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 / scale), (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH * 2 / scale));
//        return Bitmap.createBitmap(mBitmap, (int) ((paddingLeft - Math.abs(translateX)) / 2 - Math.abs(vector2D.getX()) / 2), (int) (Math.abs(vector2D.getY())), (int) (mBitmap.getWidth() - Math.abs(vector2D.getX())), (int) (mBitmap.getHeight() - Math.abs(vector2D.getY())));
        return Bitmap.createBitmap(mBitmap, x, y, width, height);
//        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        draw(canvas);
//        return Bitmap.createBitmap(bitmap, (int)Math.ceil((getWidth() - paddingLeft) / 2f), (int)Math.ceil((getHeight() - paddingTop) / 2f), (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH*2/scale), (int) (TrimCopyQqActivity.TRIMCOPYQQPADDINGWIDTH*2/scale));
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
