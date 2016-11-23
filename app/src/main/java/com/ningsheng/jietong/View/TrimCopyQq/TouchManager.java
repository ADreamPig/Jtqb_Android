package com.ningsheng.jietong.View.TrimCopyQq;

import android.view.MotionEvent;

/**
 * Created by zhangheng on 2015/12/3.
 */
public class TouchManager {
    private Vector2D[] prePoint;
    private Vector2D[] postPoint;
    private Vector2D vector2D = new Vector2D();

    public TouchManager() {
        this.prePoint = new Vector2D[2];
        this.postPoint = new Vector2D[2];
    }

    public int getPointCount() {
        int count = 0;
        for (int i = 0; i < prePoint.length; i++) {
            if (prePoint[i] != null)
                count++;
        }
        return count;
    }

    public Vector2D getPreVector2DTouch() {
        return prePoint[0];
    }

    public Vector2D getPreVector2D() {
        return Vector2D.subtract(prePoint[0], prePoint[1]);
    }

    public Vector2D getPostVector2D() {
        return Vector2D.subtract(postPoint[0], prePoint[1]);
    }


    public Vector2D move() {
        if (prePoint[0] != null)
            return vector2D.set(postPoint[0].getX() - prePoint[0].getX(), postPoint[0].getY() - prePoint[0].getY());
        else
            return vector2D.set(0, 0);
    }


    public void update(MotionEvent event) {
        switch (MotionEvent.ACTION_MASK & event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                prePoint[0] = null;
                postPoint[0] = null;
                prePoint[1] = null;
                postPoint[1] = null;
                return;
        }
        switch (event.getPointerCount()) {
            case 1:
                if (prePoint[0] == null)
                    prePoint[0] = new Vector2D(event.getX(), event.getY());
                else
                    prePoint[0].set(postPoint[0]);
                if (postPoint[0] == null)
                    postPoint[0] = new Vector2D(event.getX(), event.getY());
                else
                    postPoint[0].set(event.getX(), event.getY());
                break;
            case 2:
                if (prePoint[0] == null)
                    prePoint[0] = new Vector2D(event.getX(), event.getY());
                else
                    prePoint[0].set(postPoint[0]);
                if (postPoint[0] == null)
                    postPoint[0] = new Vector2D(event.getX(), event.getY());
                else
                    postPoint[0].set(event.getX(), event.getY());

                if (prePoint[1] == null)
                    prePoint[1] = new Vector2D(event.getX(1), event.getY(1));
                else
                    prePoint[1].set(postPoint[1]);
                if (postPoint[1] == null)
                    postPoint[1] = new Vector2D(event.getX(1), event.getY(1));
                else
                    postPoint[1].set(event.getX(1), event.getY(1));
                break;
        }

    }


}
