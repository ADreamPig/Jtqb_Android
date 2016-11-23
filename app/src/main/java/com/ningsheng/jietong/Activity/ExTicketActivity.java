package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andexert.library.RippleView;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.PayPasswordDialog;
import com.ningsheng.jietong.Dialog.SetPayPassDialog;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.View.ClearWriteEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/3/1.
 */
public class ExTicketActivity extends TitleActivity {
    private ClearWriteEditText m_money;
    private Button m_commit;
    private SetPayPassDialog dialog, dialog1;
    private String first_pwd;
    private  RippleView rippleView;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_exticket);
        setTitle("兑换电子券");
        showBackwardView("", true);

        m_money = (ClearWriteEditText) findViewById(R.id.act_exticket_money);
        m_commit = (Button) findViewById(R.id.act_exticket_commit);
         rippleView=(RippleView) findViewById(R.id.act_exticket_RippleView);

        if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivityForResult(intent,11);
        }else{
            isTranPw();
        }
//        isTranPw();
    }

    private void isTranPw(){
        if (StringUtil.isBlank(MyApplication.user.getTransactionPwd())) {
            dialog = new SetPayPassDialog(this);
            dialog.setCallBackTradePwd(new SetPayPassDialog.CallBackTradePwd() {
                @Override
                public void callBaceTradePwd(String pwd) {
//                    dialog.setTitle("再次输入密码");
//                    dialog.setIsfirst(true);
                    first_pwd = pwd;
                    dialog.dismiss();
                    dialog1 = new SetPayPassDialog(ExTicketActivity.this);
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
                    SharedPreferencesUtil.getInstance(ExTicketActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11&&resultCode==12){
            isTranPw();
        }

    }


    private void commit() {
        if (m_money.getText().toString().equals("")) {
            toast("请填写兑换金额");
            return;
        }
        final PayPasswordDialog dialog = new PayPasswordDialog(this);
        dialog.setMoney(m_money.getText().toString());
        dialog.setCallBackTradePwd(new PayPasswordDialog.CallBackTradePwd() {
            @Override
            public void callBaceTradePwd(String pwd) {
                dialog.dismiss();
                Map<String, String> map = new HashMap<String, String>();
                map.put("accountId", MyApplication.user.getId());
                map.put("mobile", MyApplication.user.getMobile());
                map.put("transactionPwd", pwd);
                map.put("transMoney", m_money.getText().toString());
                map.put("transType", "5");
                HttpSender sender = new HttpSender(Url.buyTicket, "兑换电子券", map, new OnHttpResultListener() {
                    @Override
                    public void onSuccess(String message, String code, String data) {
                        dialog.dismiss();
                        if (code.equals("0000")) {
                            toast("兑换成功！");
                            finish();
                        } else {
                            error(message);
//                            dialog.dismiss();
//                            toast(message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable, boolean b) {
                        super.onError(throwable, b);
                        error("连接错误，请稍后重试！");
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                        dialog.dismiss();
                    }
                });
                sender.setContext(ExTicketActivity.this);
                sender.send(Url.Post);
            }
        });
        dialog.show();
    }

    private void error(String message){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setMessage(message);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    protected void initListener() {
        m_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }

        });
        m_money.setListener(new ClearWriteEditText.OnEditChangeListener() {
            @Override
            public void change(View view,boolean ischange) {
                if(ischange){
                    m_commit.setBackgroundResource(R.drawable.shape_button);
                    rippleView.setEnabled(true);
                }else{
                    m_commit.setBackgroundResource(R.drawable.shape_messagecode_gary);
                    rippleView.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initDate() {

    }
}
