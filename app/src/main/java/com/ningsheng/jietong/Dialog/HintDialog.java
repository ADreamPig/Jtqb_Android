package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class HintDialog extends Dialog {
    private BaseActivity context;
    public HintDialog(BaseActivity context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_hint,null,false);
        view.findViewById(R.id.dialog_hini_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);
        Window window=getWindow();
//        WindowManager.LayoutParams lp=window.getAttributes();
//        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);

    }
}
