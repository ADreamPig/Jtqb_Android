package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andexert.library.RippleView;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
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
 * Created by zhushunqing on 2016/2/23.
 */
public class BindCardActivity extends TitleActivity implements View.OnClickListener, ClearWriteEditText.OnEditChangeListener {
    private ClearWriteEditText m_number, m_pass;
    private boolean number_change, pass_change;
    private SetPayPassDialog dialog, dialog1;
    private String first_pwd;
    private Button m_next;
    private  RippleView rippleView;

    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_bindcard);
        setTitle("卡绑定");
        showBackwardView("", true);

        m_number = (ClearWriteEditText) findViewById(R.id.act_bindcard_number);
        m_pass = (ClearWriteEditText) findViewById(R.id.act_bindcard_password);
        m_next = (Button) findViewById(R.id.act_bindcard_next);
         rippleView=(RippleView) findViewById(R.id.act_bind_RippleView);

        if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")){
            Intent intent=new Intent(this,LoginActivity.class);
            startActivityForResult(intent,11);
        }else if("".equals(MyApplication.user.getTransactionPwd())){
            isTranPw();
        }

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
                    dialog1 = new SetPayPassDialog(BindCardActivity.this);
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
                    SharedPreferencesUtil.getInstance(BindCardActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER, MyApplication.user);
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

        findViewById(R.id.act_bindcard_next).setOnClickListener(this);
        m_number.setListener(this);
        m_pass.setListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==11&&resultCode==12){
            isTranPw();
        }else if(requestCode==11&&resultCode==13){
            finish();
        }

    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_bindcard_next:
                if (m_number.getText().toString().equals("")) {
                    toast("请输入卡号");
                    Intent intent = new Intent(BindCardActivity.this, BindSucceedActivity.class);
                    startActivity(intent);
                    setResult(0x41);
                    finish();
                    return;
                }
                if (m_pass.getText().toString().equals("")) {
                    toast("请输入密码");
                    return;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("accountId", MyApplication.user.getId());
                map.put("cardNo", m_number.getText().toString());
                map.put("cardPwd", m_pass.getText().toString());
                HttpSender sender = new HttpSender(Url.bindCard, "", map, new OnHttpResultListener() {
                    @Override
                    public void onSuccess(String message, String code, String data) {
                        if (code.equals("9079")) {
                            Intent intent = new Intent(BindCardActivity.this, BindSucceedActivity.class);
                            startActivity(intent);
                            setResult(0x41);
                            finish();
                        } else {
                            toast(message);
                        }
                    }

                });
                sender.setContext(this);
                sender.send(Url.Post);

                break;
        }
    }

    @Override
    public void change(View view, boolean ischange) {
        switch (view.getId()) {
            case R.id.act_bindcard_number:
                number_change = ischange;
                break;
            case R.id.act_bindcard_password:
                pass_change = ischange;
                break;
        }
        if ((number_change) && (pass_change)) {
            m_next.setBackgroundResource(R.drawable.shape_button);
            m_next.setEnabled(true);
            rippleView.setEnabled(true);
        } else {
            m_next.setBackgroundResource(R.drawable.shape_messagecode_gary);
            m_next.setEnabled(false);
            rippleView.setEnabled(false);
        }

    }
}
