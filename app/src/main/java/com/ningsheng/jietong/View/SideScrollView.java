package com.ningsheng.jietong.View;

import android.content.Context;
import android.widget.ScrollView;

/**
 * Created by zhushunqing on 2016/2/4.
 */
public class SideScrollView extends ScrollView {
    public SideScrollView(Context context) {
        super(context);
    }

    public interface ScrollViewListener {

        void onScrollChanged(int x, int y, int oldx, int oldy);

    }
    private ScrollViewListener scrollViewListener;
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // TODO Auto-generated method stub
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
