package com.ningsheng.jietong.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;

/**
 * @author 张恒
 */
public class ClearEditText extends EditText implements TextWatcher,
        OnFocusChangeListener {
    private Drawable drawable;
    private boolean hasFocus;
    private Context context;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        drawable = getCompoundDrawables()[2];
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.selector_delete);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
        }
        setClearIcoVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);

    }

    /**
     * 设置图标是否显示
     *
     * @param isVisible
     */
    private void setClearIcoVisible(boolean isVisible) {
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], isVisible ? drawable
                : null, drawables[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore,
                              int lengthAfter) {
        if (hasFocus && text.length() > 0 && isEnabled()) {
            setClearIcoVisible(true);
        } else {
            setClearIcoVisible(false);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus && getText().length() > 0) {
            setClearIcoVisible(true);
        } else {
            setClearIcoVisible(false);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {

            if (event.getX() > getWidth() - getTotalPaddingRight() - AndroidUtil.dip2px(context, 20)
                    && event.getX() < getWidth() - getPaddingRight() + AndroidUtil.dip2px(context, 20)) {
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }
}
