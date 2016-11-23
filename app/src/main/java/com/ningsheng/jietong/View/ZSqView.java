package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/3/7.
 */
public class ZSqView extends View {
    private Context context;
    private Paint point;

    public ZSqView(Context context) {
        super(context);
        this.context = context;
    }

    public ZSqView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        point = new Paint();
        point.setColor(getResources().getColor(R.color.app_color));
        point.setStrokeWidth(4);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private float x, y;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.set
//        point.setStyle(Paint.Style.FILL);//实心
        point.setStyle(Paint.Style.STROKE);//空心
        canvas.drawCircle(250, 350, 230, point);
        Log.i("----------sdasdas", Math.sin(Math.PI * 30 / 180) + "------" + Math.cos(30));
        double tempx = 230 * Math.cos(Math.PI * 30 / 180) + 250;
        double tempy = (350 - Math.sin(Math.PI * 30 / 180) * 230);
        y = (float) tempy;
        x = (float) tempx;
        canvas.drawLine(250, 350, x, y, point);
        point.setTextSize(50);
        canvas.drawText("百分比", 350, 350, point);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
