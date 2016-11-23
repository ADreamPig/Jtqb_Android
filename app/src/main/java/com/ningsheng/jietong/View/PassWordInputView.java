package com.ningsheng.jietong.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;


/**
 * Created by hasee-pc on 2016/2/26.
 */
public class PassWordInputView extends FrameLayout implements OnClickListener,
        TextWatcher {
    private Context context;
    private String password;
    private boolean isVisible;
    private LinearLayout contentView;
    private TextView[] textViews = new TextView[6];
    private GradientDrawable drawableSelectFalse;
    private GradientDrawable drawableSelectTrue;
    private EditText edit_pwd;
    private InputMethodManager imm;
    private OnFocusChangeListener listener;
    private OnInputSuccessListener onInputSuccessListener;

    public PassWordInputView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public PassWordInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassWordInputView(Context context) {
        this(context, null, 0);
    }

    @SuppressLint("InflateParams")
    private void init() {
        int with = (int) (MyApplication.getScreenWidth() * 0.6);
        int spacing = AndroidUtil.dip2px(getContext(), 5);
        int textWidth = (with - (textViews.length - 1) * spacing) / textViews.length;
        FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(with, textWidth);
        addView(LayoutInflater.from(context).inflate(
                R.layout.password_input_view, null), frameLayoutParams);
        contentView = (LinearLayout) findViewById(R.id.content_view);
        contentView.setOnClickListener(this);
        edit_pwd = (EditText) findViewById(R.id.pwd_edit);
        edit_pwd.addTextChangedListener(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(textWidth, textWidth);
        layoutParams.rightMargin = spacing;
        drawableSelectFalse = new GradientDrawable();
        drawableSelectFalse.setColor(0xFFFFFFFF);
        drawableSelectFalse.setCornerRadius(AndroidUtil.dip2px(getContext(), 4f));
        drawableSelectFalse.setStroke(AndroidUtil.dip2px(getContext(), 0.5f), getResources().getColor(R.color.font_gray));
        drawableSelectTrue = new GradientDrawable();
        drawableSelectTrue.setColor(0xFFFFFFFF);
        drawableSelectTrue.setCornerRadius(AndroidUtil.dip2px(getContext(), 4f));
        drawableSelectTrue.setStroke(AndroidUtil.dip2px(getContext(), 0.5f), getResources().getColor(R.color.app_color));
        for (int i = 0; i < textViews.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setBackgroundDrawable(drawableSelectFalse);
            tv.setGravity(Gravity.CENTER);
            textViews[i] = tv;
            contentView.addView(tv, layoutParams);
        }
        password = "";
        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void addFocus() {
        if (listener != null) {
            listener.onFocusChange(this, true);
        }
        edit_pwd.requestFocus();
        imm.showSoftInput(edit_pwd, 0);
    }

    public void removeFocus() {
        if (listener != null) {
            listener.onFocusChange(this, false);
        }
        edit_pwd.clearFocus();
    }

    public void hideSoftInput() {
        imm.hideSoftInputFromWindow(edit_pwd.getWindowToken(), 0);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        if (isVisible) {
            for (int i = 0; i < password.length(); i++) {
                textViews[i].setText(String.valueOf(password.charAt(i)));
            }
        } else {
            for (int i = 0; i < password.length(); i++) {
                textViews[i].setText("●");
            }
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public void onClick(View view) {
        addFocus();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.listener = listener;
    }

    public void setOnInputSuccessListener(OnInputSuccessListener listener) {
        onInputSuccessListener = listener;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        System.out.println("arg0:" + arg0 + " arg1:" + arg1 + "arg2:" + arg2 + "arg3:" + arg3);
        password = arg0.toString();
        if (password.length() == 6 && onInputSuccessListener != null) {
            Log.i("------view----",password);
            onInputSuccessListener.onInputSuccess(this, password);
        } else if (password.length() == 0 && onInputSuccessListener != null) {
            onInputSuccessListener.onInputClear(this);
        }
        if (!isVisible) {
            TextView tv = textViews[password.length() - 1 < 0 ? 0 : password.length() - 1];
            tv.setText("●");
            tv.setBackgroundDrawable(drawableSelectTrue);
        } else {
            TextView tv = textViews[password.length() - 1 < 0 ? 0 : password.length() - 1];
            tv.setText(password.length() - 1 < 0 ? "" : String.valueOf(password.charAt(password.length() - 1)));
        }
        for (int i = password.length(); i < textViews.length; i++) {
            textViews[i].setText("");
            textViews[i].setBackgroundDrawable(drawableSelectFalse);
        }
    }

    public interface OnInputSuccessListener {
        void onInputSuccess(PassWordInputView passWordInputView,
                            String password);

        void onInputClear(PassWordInputView passWordInputView);
    }

}
