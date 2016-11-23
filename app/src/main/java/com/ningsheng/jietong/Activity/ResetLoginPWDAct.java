package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/3/11.
 */
public class ResetLoginPWDAct extends TitleActivity implements View.OnClickListener {
    private EditText m_pass1, m_pass;
    private String mobile;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_resetlogin_pwd);
        setTitle("重置密码");
        showBackwardView("", true);
        mobile=getIntent().getStringExtra("mobile");

        m_pass = (EditText) findViewById(R.id.act_reset_newpass);
        m_pass1 = (EditText) findViewById(R.id.act_reset_newpass1);

    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_reset_next).setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        if(!m_pass.getText().toString().equals(m_pass1.getText().toString())){
            toast("两次密码输入不一致！");
            return;
        }
        Map<String,String> map=new HashMap<String,String>();
        map.put("mobile",mobile);
        map.put("newPwd",m_pass.getText().toString());
        HttpSender sender=new HttpSender(Url.assignUserPwd, "重置密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    toast("重置成功！");
                    finish();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
