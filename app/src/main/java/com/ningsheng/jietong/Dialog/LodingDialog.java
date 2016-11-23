package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mingle.widget.LoadingView;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/3/24.
 */
public class LodingDialog extends Dialog {
    private Context context;
    public LodingDialog(Context context) {
        super(context,R.style.loading_dialog);
        this.context=context;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_loading,null,false);
        LoadingView m_load=(LoadingView)view.findViewById(R.id.loadView);
        m_load.getLayoutParams().width=m_load.getLayoutParams().height;
        this.setContentView(view);
//        getWindow().getWindowManager()
//        WindowManager windowManager = getWindow().getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        WindowManager.LayoutParams lp1 = this.getWindow().getAttributes();
////        lp1.width = (int) (display.getWidth()); //设置宽度
////        lp1.width = 400; //设置宽度
//
//        this.getWindow().setAttributes(lp1);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;

//        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
//        if (tvMsg != null) {
//            tvMsg.setText(strMessage);
//        }
//
    }
}
