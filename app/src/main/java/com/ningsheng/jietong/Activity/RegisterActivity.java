package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ValidationCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class RegisterActivity extends TitleActivity implements View.OnClickListener {
    private TextView m_getCode;
    private CheckBox m_check;
    private EditText m_pwd, m_mobile, m_code,m_editValicode;
    private  String rrid;
    private ValidationCode m_valicode;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_register);
        setTitle("注册");
        showBackwardView("", true);

        m_getCode = (TextView) findViewById(R.id.act_register_getcode);
        m_check = (CheckBox) findViewById(R.id.act_register_lookpwd);
        m_pwd = (EditText) findViewById(R.id.act_register_pwd);
        m_mobile = (EditText) findViewById(R.id.act_register_mobile);
        m_code = (EditText) findViewById(R.id.act_register_code);
        m_editValicode = (EditText) findViewById(R.id.act_register_valicode);
         m_valicode=(ValidationCode) findViewById(R.id.register_validation_code);

        rrid=SharedPreferencesUtil.getInstance(RegisterActivity.this).getString(SharedPreferencesUtil.RRID_REGISTER,null);
    }

    @Override
    protected void initListener() {
        m_getCode.setOnClickListener(this);
        findViewById(R.id.act_register_xieyi).setOnClickListener(this);
        findViewById(R.id.act_register_commit).setOnClickListener(this);
        m_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //隐藏密码
                    m_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //显示密码
                    m_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    protected void initDate() {

    }

    private void commit() {
        if (!StringUtil.isMobileNo(m_mobile.getText().toString())) {
            toast("请输入正确手机号码");
            return;
        }
        if (m_pwd.getText().toString().equals("")) {
            toast("请输入密码");
            return;
        }
        if (m_code.getText().toString().equals("")) {
            toast("请输入验证码");
            return;
        }
        if(!m_editValicode.getText().toString().equalsIgnoreCase(m_valicode.getCodeString())){
            toast("验证码错误！");
            return;
        }
//        m_valicode.setCodeString(m_valicode.getCharAndNumr(5));
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", m_mobile.getText().toString());
        map.put("password", m_pwd.getText().toString());
        map.put("dxyz", m_code.getText().toString());
        map.put("rrid", rrid);
        map.put("clientType","1");
        HttpSender sender = new HttpSender(Url.register, "注册", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    User user=gsonUtil.getInstance().json2Bean(data,User.class);
                    MyApplication.user=user;
                    SharedPreferencesUtil.getInstance(MyApplication.getInstance()).setBeantoSharedPreferences(SharedPreferencesUtil.USER,user);
//                    setResult(12);
                Intent intent = new Intent(RegisterActivity.this, RegisterSucceedActivity.class);
                startActivity(intent);
                    finish();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_register_getcode:
                if (!StringUtil.isMobileNo(m_mobile.getText().toString())) {
                    toast("请输入正确手机号码");
                    return;
                }
//                if(!m_editValicode.getText().toString().equalsIgnoreCase(m_valicode.getCodeString())){
//                    toast("验证码错误！");
//                    return;
//                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("reqMobile", m_mobile.getText().toString());
                map.put("reqDevice", MyApplication.getOnlyId());
                map.put("reqIp", MyApplication.getLocalIpAddress());
                map.put("reqFlag", "TPL_ZHUCE");
                HttpSender sender = new HttpSender(Url.sendSms, "发送短信", map, new OnHttpResultListener() {
                    @Override
                    public void onSuccess(String message, String code, String data) {
                        if (code.equals("0000")) {
                             rrid=(String)gsonUtil.getInstance().getValue(data,"rrid");
                            SharedPreferencesUtil.getInstance(RegisterActivity.this).cleanBeantoSharedPreferences(SharedPreferencesUtil.RRID_REGISTER,rrid);
                            CountDownTimer timers = new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long arg0) {

                                    m_getCode.setText("剩余" + arg0 / 1000 + "秒");
                                    m_getCode.setEnabled(false);
                                    m_getCode.setBackgroundResource(R.drawable.shape_messagecode_gary);
                                }

                                @Override
                                public void onFinish() {
                                    m_getCode.setText("发送验证码");
                                    m_getCode.setEnabled(true);
                                    m_getCode.setBackgroundResource(R.drawable.shape_messagecode_yellow);
                                }
                            };
                            timers.start();
                        } else {
                            toast(message);
                        }
                    }
                });
                sender.setContext(this);
                sender.send(Url.Post);
                break;
            case R.id.act_register_commit:
                commit();
                break;
            case R.id.act_register_xieyi:
                Intent intent=new Intent(this,RegisterXy.class);
                startActivity(intent);
                break;
        }
    }
}
