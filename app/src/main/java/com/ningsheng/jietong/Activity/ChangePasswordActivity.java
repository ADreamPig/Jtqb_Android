package com.ningsheng.jietong.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.TextFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/26.
 */
public class ChangePasswordActivity extends TitleActivity {
    private EditText etOld;
    private EditText etNew1;
    private EditText etNew2;

    @Override
    protected void initView() {
        super.initView();
        setTitle("修改密码");
        setContentView(R.layout.activity_change_password);
        InputFilter[] filters = {new TextFilter.PasswordTextFilter(), new InputFilter.LengthFilter(16)};
        etOld = (EditText) findViewById(R.id.changePassword_et_pass1);
        etNew1 = (EditText) findViewById(R.id.changePassword_et_pass2);
        etNew2 = (EditText) findViewById(R.id.changePassword_et_pass3);
        etNew1.setFilters(filters);
        etNew2.setFilters(filters);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    public void changePwClick1(View view) {
        if (check()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("password", etOld.getText().toString());
            map.put("newPwd", etNew1.getText().toString());
            HttpSender sender = new HttpSender(Url.updateUserPwd, "修改登录密码", map, new OnHttpResultListener() {

                @Override
                public void onSuccess(String message, String code, String data) {

                    AlertDialog.Builder dialog=new AlertDialog.Builder(ChangePasswordActivity.this);
                    dialog.setMessage("密码修改成功！");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            });
            sender.setContext(this);
            sender.send(Url.Post);
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(etOld.getText().toString())) {
            toast("亲，忘记输旧密码了");
            etOld.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etOld.requestFocus();
        } else if (TextUtils.isEmpty(etNew1.getText().toString())) {
            toast("亲，忘记输新密码了");
            etNew1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew1.requestFocus();
        } else if (etNew1.getText().toString().length() < 6) {
            toast("亲，为了保障您的账户安全，密码最少6位喔");
            etNew1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew1.requestFocus();
        } else if (TextFilter.isLoginPassword(etNew1.getText().toString())) {
            toast("亲，密码需要包含字母，数字或字符至少两种组合");
            etNew1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew1.requestFocus();
        } else if (TextUtils.isEmpty(etNew2.getText().toString())) {
            toast("亲，再输入一遍新密码");
            etNew2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew2.requestFocus();
        } else if (etNew2.getText().toString().length() < 6) {
            toast("亲，为了保障您的账户安全，密码最少6位喔");
            etNew2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew2.requestFocus();
        } else if (TextFilter.isLoginPassword(etNew2.getText().toString())) {
            toast("亲，密码需要包含字母，数字或字符至少两种组合");
            etNew2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew2.requestFocus();
        } else if (!etNew1.getText().toString().equals(etNew2.getText().toString())) {
            toast("亲，两次输入不一致！");
            etNew1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            etNew2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else {
            return true;
        }
        return false;
    }

    public void changePwClick2(View view) {
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:4008871380"));
        startActivity(intent1);
    }
}
