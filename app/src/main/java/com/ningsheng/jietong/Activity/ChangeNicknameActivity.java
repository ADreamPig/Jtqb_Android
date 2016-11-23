package com.ningsheng.jietong.Activity;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.TextFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改昵称
 * Created by hasee-pc on 2016/3/1.
 */
public class ChangeNicknameActivity extends TitleActivity {
    private EditText et;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_change_nickname);
        et = (EditText) findViewById(R.id.changeNickname_et);
        et.setText(getIntent().getStringExtra("name"));
        et.setFilters(new InputFilter[]{new TextFilter.UserNameTextFilter()});
        setTitle("修改昵称");
        showForwardView("保存", true);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void onForward() {
        if (check()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("userName", et.getText().toString());
            HttpSender sender = new HttpSender(Url.updateNickName, "修改昵称", map, new OnHttpResultListener() {

                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("操作成功".equals(message)) {
                        setResult(RESULT_OK, getIntent().putExtra("name", et.getText().toString()));
                        MyApplication.user.setUserName(et.getText().toString());
                        SharedPreferencesUtil.getInstance(ChangeNicknameActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER,MyApplication.user);
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

    private boolean check() {
        if (TextUtils.isEmpty(et.getText().toString())) {
            toast("亲，请输入昵称喔");
            et.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (et.getText().length() < 2) {
            toast("亲，昵称最少两个字符喔");
            et.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (et.getText().toString().length() > 8) {
            toast("亲，昵称最多八个字符喔");
            et.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else {
            return true;
        }
        return false;
    }
}
