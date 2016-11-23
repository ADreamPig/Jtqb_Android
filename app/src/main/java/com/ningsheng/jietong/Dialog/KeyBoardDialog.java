package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.View.SecurityPasswordEditText;

/**
 * Created by zhushunqing on 2016/3/11.
 */
public class KeyBoardDialog extends Dialog {
    private BaseActivity context;



    public KeyBoardDialog(BaseActivity context,SecurityPasswordEditText myEdit,View view) {
        super(context, R.style.ActionSheetDialogStyle1);
        this.context=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view= LayoutInflater.from(context).inflate(R.layout.dialog_keybord,null,false);

    }
}
