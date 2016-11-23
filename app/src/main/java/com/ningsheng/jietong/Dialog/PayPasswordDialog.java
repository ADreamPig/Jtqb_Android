package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ningsheng.jietong.Activity.ChangePayPwActivity;
import com.ningsheng.jietong.Activity.IdentityValidateActivity;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.KeyboardUtil;
import com.ningsheng.jietong.View.SecurityPasswordEditText;

import java.lang.reflect.Method;

/**
 * Created by zhushunqing on 2016/2/2.
 */
public class PayPasswordDialog extends Dialog {
    private BaseActivity context;
    private  CallBackTradePwd callBackTradePwd;
    private TextView m_money;
    private  View view;


    public void setCallBackTradePwd(CallBackTradePwd callBackTradePwd) {
        this.callBackTradePwd = callBackTradePwd;
    }
    public interface CallBackTradePwd{
        public void callBaceTradePwd(String pwd);
    }
    public PayPasswordDialog(BaseActivity context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context=context;
         view= LayoutInflater.from(context).inflate(R.layout.dialog_pay_pass,null,false);
        m_money=(TextView)view.findViewById(R.id.dialog_pay_money);
    }
    public void setMoney(String money){
        if(m_money!=null){
            m_money.setText("￥"+money);
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View view= LayoutInflater.from(context).inflate(R.layout.dialog_pay_pass,null,false);
        m_money=(TextView)view.findViewById(R.id.dialog_pay_money);
        view.findViewById(R.id.dialog_pay_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.dialog_pay_pass_forgit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,IdentityValidateActivity.class);
                context.startActivity(intent);
            }
        });
        SecurityPasswordEditText myEdit = (SecurityPasswordEditText) view.findViewById(R.id.my_edit);
//        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        myEdit.setSecurityEditCompleListener(new SecurityPasswordEditText.SecurityEditCompleListener() {

            @Override
            public void onNumCompleted(String num) {
                if(callBackTradePwd!=null){
                    callBackTradePwd.callBaceTradePwd(num);
                }
            }
        });
//        tvClose.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                popWindow.dismiss();
//            }
//        });
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
