package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.RealNameDialog;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.IdcardUtils;
import com.ningsheng.jietong.Utils.OnHttpResultListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 身份验证
 * Created by hasee-pc on 2016/3/3.
 */
public class IdentityValidateActivity extends TitleActivity {
    private EditText etIdCard;
    private RealNameDialog dialog;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_identity_validate);
        setTitle("身份验证");

        etIdCard=(EditText) findViewById(R.id.identity_et_idcarc);
        if("".equals(MyApplication.user.getIdentityCard())){
             dialog=new RealNameDialog(this);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x11&&!"".equals(MyApplication.user.getIdentityCard())){
            dialog.dismiss();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    public void identityClick(View view) {
        if (check()) {
            identityAuth();
        }
    }

    private boolean check() {
        if (etIdCard.getText().toString().length() < 15) {
            toast("身份证号码最少15位喔");
            etIdCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (!IdcardUtils.validateCard(etIdCard.getText().toString())) {
            toast("身份证格式不正确喔");
            etIdCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else {
            return true;
        }
        return false;
    }

    private void identityAuth() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", MyApplication.user.getId());
        map.put("identityCard", etIdCard.getText().toString());
        HttpSender sender = new HttpSender(Url.identityAuth, "验证身份证", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("9039".equals(code)) {
                    Intent intent = new Intent(IdentityValidateActivity.this, ChangePayPwActivity.class);
                    intent.putExtra("type", ChangePayPwActivity.TYPE_ASSIGN);
                    startActivity(intent);
                    finish();
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
