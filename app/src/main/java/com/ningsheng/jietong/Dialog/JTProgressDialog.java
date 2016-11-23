package com.ningsheng.jietong.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/3/8.
 */
public class JTProgressDialog extends ProgressDialog {
//    public JTProgressDialog(Context context, int theme) {
//        super(context, theme);
//    }

    public JTProgressDialog(Context context) {
        super(context, R.style.CustomProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
