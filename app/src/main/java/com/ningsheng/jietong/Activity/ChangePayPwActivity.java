package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.View.PassWordInputView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/26.
 */
public class ChangePayPwActivity extends TitleActivity {
    private PassWordInputView inputView1;
    private PassWordInputView inputView2;
    public static final int TYPE_UPDATE = 0;
    public static final int TYPE_ASSIGN = 1;
    public int type;
    private SetPayPassDialog dialog, dialog1;
    private String first_pwd;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_change_paypw);

        if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
            dialog = new SetPayPassDialog(this);
            dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                @Override
                public void callBaceTradePwd(String pwd) {
//                    dialog.setTitle("再次输入密码");
//                    dialog.setIsfirst(true);
                    first_pwd = pwd;
                    dialog.dismiss();
                    dialog1 = new SetPayPassDialog(ChangePayPwActivity.this);
                    dialog1.setTitle("再次输入密码");
                    dialog1.setIsfirst(true);
                    dialog1.show();
                    dialog1.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                        @Override
                        public void callBaceTradePwd(String pwd) {
                            if (first_pwd.equals(pwd)) {
                                setTransPassWord(pwd);
                            }else{
                                toast("两次输入不一致");
                                dialog.show();
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


        inputView2 = (PassWordInputView) findViewById(R.id.inputView2);
        inputView2.setOnInputSuccessListener(new PassWordInputView.OnInputSuccessListener() {
            @Override
            public void onInputSuccess(PassWordInputView passWordInputView, String password) {
                inputView2.hideSoftInput();
            }

            @Override
            public void onInputClear(PassWordInputView passWordInputView) {
                if(inputView1!=null){
                    inputView1.addFocus();
                }
            }
        });

        type = getIntent().getIntExtra("type", TYPE_UPDATE);
        switch (type) {
            case TYPE_ASSIGN:
                setTitle("重置支付密码");
                findViewById(R.id.changePayPw_update_linear).setVisibility(View.GONE);
//                inputView1 = (PassWordInputView) findViewById(R.id.inputView1);
//                inputView1.setOnInputSuccessListener(new PassWordInputView.OnInputSuccessListener() {
//                    @Override
//                    public void onInputSuccess(PassWordInputView passWordInputView, String password) {
////                        inputView1.hideSoftInput();
////                        inputView2.addFocus();
//                        Log.i("-------password--------",password);
//                    }
//
//                    @Override
//                    public void onInputClear(PassWordInputView passWordInputView) {
////                        Log.i("-------password--------",password);
//                    }
//                });

                break;
            case TYPE_UPDATE:
                setTitle("更改支付密码");
                inputView1 = (PassWordInputView) findViewById(R.id.inputView1);
                inputView1.setOnInputSuccessListener(new PassWordInputView.OnInputSuccessListener() {
                    @Override
                    public void onInputSuccess(PassWordInputView passWordInputView, String password) {
                        inputView1.removeFocus();
                        inputView2.addFocus();
                    }

                    @Override
                    public void onInputClear(PassWordInputView passWordInputView) {

                    }
                });
                break;


        }
    }


    private void setTransPassWord(String num) {
        Map<String, String> map = new HashMap<>();
        map.put("accountId", MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        map.put("newPwd", num);
        HttpSender sender = new HttpSender(Url.assignTranPwd, "设置支付密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    dialog1.dismiss();
                    MyApplication.user.setTransactionPwd("true");
                    SharedPreferencesUtil.getInstance(ChangePayPwActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
                    toast("设置成功！");
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    public void updateTranPwd() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", MyApplication.user.getId());
        map.put("transactionPwd", inputView1.getPassword());
        map.put("newPwd", inputView2.getPassword());
        HttpSender sender = new HttpSender(Url.updateTranPwd, "修改支付密码", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    toast("更改成功！");
                    finish();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    public void assignTranPwd() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId",MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        map.put("newPwd", inputView2.getPassword());
        HttpSender sender = new HttpSender(Url.assignTranPwd, "重置支付密码", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    toast("更改成功！");
                    finish();
                }else{
                    toast(message);
                }
            }

        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    public void changePayPwClick1(View view) {
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage("是否确定修改支付密码？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type) {
                    case TYPE_UPDATE:
                        updateTranPwd();
                        break;
                    case TYPE_ASSIGN:
                        assignTranPwd();
                        break;
                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    public void changePayPwClick2(View view) {
        Intent intent = new Intent(this, IdentityValidateActivity.class);
        startActivity(intent);
    }

    public void changePayPwClick3(View view) {
        inputView1.setVisible(!inputView1.isVisible());
        ImageView iv = (ImageView) view;
        iv.setImageResource(inputView1.isVisible() ? R.mipmap.eye_kaiqi : R.mipmap.eye_guanbi);
    }

    public void changePayPwClick4(View view) {
        inputView2.setVisible(!inputView2.isVisible());
        ImageView iv = (ImageView) view;
        iv.setImageResource(inputView2.isVisible() ? R.mipmap.eye_kaiqi : R.mipmap.eye_guanbi);
    }
}
