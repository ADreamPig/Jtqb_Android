package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.KeyboardUtil;
import com.ningsheng.jietong.View.SecurityPasswordEditText;

import java.lang.reflect.Method;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class SetPayPassDialog extends Dialog{
    private BaseActivity context;
    private  CallBackTradePwd callBackTradePwd;
    private TextView m_title;
    private View mConfirm;
    private String title;
    private boolean isfirst;
    private View view;
    private SecurityPasswordEditText myEdit;
    private String num;


    public void setCallBackTradePwd(CallBackTradePwd callBackTradePwd) {
        this.callBackTradePwd = callBackTradePwd;
    }
    public interface CallBackTradePwd{
        public void callBaceTradePwd(String pwd);
        public void callClose();
    }

    public void setTitle(String title){
        m_title.setText(title);
    }
    public void setIsfirst(boolean isfirst){
        if(isfirst){
            mConfirm.setVisibility(View.VISIBLE);
        }else {
            mConfirm.setVisibility(View.GONE);
        }
    }


    public SetPayPassDialog(BaseActivity context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context=context;
         view=LayoutInflater.from(getContext()).inflate(R.layout.dialog_set_pay_pass,null,false);

        view.findViewById(R.id.dialog_set_pay_pass_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackTradePwd.callClose();
//                dismiss();
            }
        });
        m_title=(TextView) view.findViewById(R.id.dialog_setPay_title);
        mConfirm=view.findViewById(R.id.dialog_setPay_confirm);
         myEdit = (SecurityPasswordEditText) view.findViewById(R.id.my_edit);
//        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        myEdit.setSecurityEditCompleListener(new SecurityPasswordEditText.SecurityEditCompleListener() {

            @Override
            public void onNumCompleted(String num) {
                SetPayPassDialog.this.num=num;
                if(callBackTradePwd!=null){
                    callBackTradePwd.callBaceTradePwd(num);
                }
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackTradePwd!=null&&SetPayPassDialog.this.num!=null){
                    callBackTradePwd.callBaceTradePwd(num);
                }
            }
        });
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Method setShowSoftInputOnFocus = null;
        try {
            setShowSoftInputOnFocus = myEdit.getClass().getMethod(
                    "setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(myEdit, false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

setCancelable(false);
        new KeyboardUtil(view, context, myEdit).showKeyboard();
        setContentView(view);
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp1 = this.getWindow().getAttributes();
        lp1.width = (int) (display.getWidth()); //设置宽度
        this.getWindow().setAttributes(lp1);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

    }
}
