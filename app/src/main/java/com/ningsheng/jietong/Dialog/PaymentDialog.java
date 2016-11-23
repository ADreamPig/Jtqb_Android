package com.ningsheng.jietong.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class PaymentDialog extends Dialog {
    private BaseActivity context;
    private onPaymentBack  onPaymentBack;

    public PaymentDialog(BaseActivity context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context=context;
    }

    public void setOnPaymentBack(PaymentDialog.onPaymentBack onPaymentBack) {
        this.onPaymentBack = onPaymentBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_payment,null,false);
        view.findViewById(R.id.dialog_payment_zfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onPaymentBack!=null){
                onPaymentBack.onBack();
                }
            }
        });

        setContentView(view);
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp1 = this.getWindow().getAttributes();
        lp1.width = (int) (display.getWidth()); //设置宽度
        this.getWindow().setAttributes(lp1);
        getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    public interface onPaymentBack{
        public void onBack();
    }
}
