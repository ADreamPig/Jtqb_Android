package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class ForgetActivity extends TitleActivity implements View.OnClickListener{
    private EditText m_code,m_mobile;
    private TextView m_getCode;
    private String rrid;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_forget);
        setTitle("忘记密码");
        showBackwardView("",true);


        m_mobile=(EditText) findViewById(R.id.act_forget_mobile);
        m_code=(EditText) findViewById(R.id.act_forget_code);
        m_getCode=(TextView)findViewById(R.id.act_forget_getcode);
        rrid=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.RRID_FORGETPWD,null);
    }

    @Override
    protected void initListener() {
        m_getCode.setOnClickListener(this);
        findViewById(R.id.act_forget_next).setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }
    private void next(){
        if(m_code.getText().toString().equals("")){
            toast("请填写验证码！");
            m_code.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if(rrid==null){
            toast("请重新发送验证码！");
            m_code.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("dxyz",m_code.getText().toString());
        map.put("rrid",rrid);
        HttpSender sender=new HttpSender(Url.validateSmsCode, "验证密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    Intent intent=new Intent(ForgetActivity.this,ResetLoginPWDAct.class);
                    intent.putExtra("mobile",m_mobile.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    toast(message);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
//                super.onError(throwable, b);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_forget_getcode:
                Map<String, String> map = new HashMap<String, String>();
                map.put("reqMobile", m_mobile.getText().toString());
                map.put("reqDevice", MyApplication.getOnlyId());
                map.put("reqIp", MyApplication.getLocalIpAddress());
                map.put("reqFlag", "TPL_FORGETPWD");
                HttpSender sender = new HttpSender(Url.sendSms, "发送短信", map, new OnHttpResultListener() {
                    @Override
                    public void onSuccess(String message, String code, String data) {
                        if (code.equals("0000")) {
                            rrid=(String) gsonUtil.getInstance().getValue(data,"rrid");
                            SharedPreferencesUtil.getInstance(ForgetActivity.this).cleanBeantoSharedPreferences(SharedPreferencesUtil.RRID_FORGETPWD,rrid);
                            CountDownTimer timers = new CountDownTimer(120000, 1000) {
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
            case R.id.act_forget_next:
next();
                break;
        }
    }
}
