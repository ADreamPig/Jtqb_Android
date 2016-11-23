package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.ningsheng.jietong.AliPay.PayUtil;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.HintDialog;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.BitmapUtils;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/22.
 */
public class QRCodeActivity extends TitleActivity {
    private ImageView m_QRCode,m_oneDCode;
    private SetPayPassDialog dialog, dialog1;
    private String first_pwd;

    @Override
    protected void initListener() {
        setContentView(R.layout.activity_qrcode);
        setTitle("支付码");
        showBackwardView("", true);

        m_QRCode = (ImageView) findViewById(R.id.act_qr_code);
        m_oneDCode=(ImageView) findViewById(R.id.act_crate_oneDCode);

        if ("".equals(MyApplication.user.getId())) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 11);
        } else {
            isTransPass();
        }


    }

    private void isTransPass() {
        if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
            dialog = new SetPayPassDialog(this);
            dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                @Override
                public void callBaceTradePwd(String pwd) {
                    first_pwd = pwd;
                    dialog.dismiss();
                    dialog1 = new SetPayPassDialog(QRCodeActivity.this);
                    dialog1.setTitle("再次输入密码");
                    dialog1.setIsfirst(true);
                    dialog1.show();
                    dialog1.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                        @Override
                        public void callBaceTradePwd(String pwd) {
                            if (first_pwd.equals(pwd)) {
                                setTransPassWord(pwd);
                            }
                        }

                        @Override
                        public void callClose() {
                            dialog1.dismiss();
                            finish();
                        }
                    });
                }

                @Override
                public void callClose() {
                    dialog.dismiss();
                    finish();
                }
            });

            dialog.show();
        }
    }

    private void setTransPassWord(String num) {
        Map<String, String> map = new HashMap<>();
        map.put("id", MyApplication.user.getId());
        map.put("newPwd", num);
        HttpSender sender = new HttpSender(Url.assignTranPwd, "设置支付密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    dialog1.dismiss();
                    MyApplication.user.setTransactionPwd("true");
                    SharedPreferencesUtil.getInstance(QRCodeActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
                    toast("设置成功！");
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    protected void initDate() {
//        String uri = resultTextView.getText().toString();
//		Bitmap bitmap = BitmapUtil.create2DCoderBitmap(uri, mScreenWidth/2, mScreenWidth/2);
        Bitmap bitmap2,bitmap1;
        try {
            bitmap1=BitmapUtils.CreateOneDCode("zhushunqing");
            bitmap2 = BitmapUtils.createQRCode("zhushunqing", 500);
            if (bitmap2 != null) {
                m_QRCode.setImageBitmap(bitmap2);
                m_oneDCode.setImageBitmap(bitmap1);
            }

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == 12) {//登陆成功后
            isTransPass();
            initDate();
        }
        if (requestCode == 11 && resultCode == 13) {//点返回键后的返回
            finish();
        }

    }

}
