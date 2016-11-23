package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.ActUtil;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by zhushunqing on 2016/3/18.
 */
public class LoginOutActivit extends BaseActivity implements View.OnClickListener {
    private TextView m_content;
    private String content;

    @Override
    protected void initView() {
        setContentView(R.layout.act_loginout);
        content = getIntent().getStringExtra("content");
        m_content = (TextView) findViewById(R.id.act_login_out_content);
        m_content.setText(content + "");
        loginOut();
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_login_out_exit).setOnClickListener(this);
        findViewById(R.id.act_login_out_relogin).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_login_out_exit:
                MobclickAgent.onKillProcess(this);
                ActUtil.getInstance().AppExit(this);
                break;
            case R.id.act_login_out_relogin:

                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent,22);
//                ActUtil.getInstance().killAllActivity();
//                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        Log.i("--finish---","finish");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==22&&resultCode==12){
            finish();
//            onDestroy();
        }else if(requestCode==22&&resultCode==13){
//            toast("没有操作");
        }
    }
}
