package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ningsheng.jietong.Activity.RealNameActivity;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/3/11.
 */
public class RealNameDialog extends Dialog implements View.OnClickListener{
    private BaseActivity context;
    public RealNameDialog(BaseActivity context) {
        super(context, R.style.real_name_dialog);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_realname,null,false);
        view.findViewById(R.id.dialog_confirm).setOnClickListener(this);
        view.findViewById(R.id.dialog_goset).setOnClickListener(this);
        setContentView(view);
//        Window window=getWindow();
//        WindowManager.LayoutParams lp=window.getAttributes();
//        lp.width=WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
//        window.setGravity(Gravity.CENTER);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.alpha = 1f; // 透明度

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_goset:
                Intent intent=new Intent(context, RealNameActivity.class);
                context.startActivityForResult(intent,0x11);
                break;
            case R.id.dialog_confirm:
                dismiss();
                context.finish();
                break;
        }
    }
}
