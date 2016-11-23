package com.ningsheng.jietong.Activity;

import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.MaterialDialog;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.IdcardUtils;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.TextFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * 实名登记
 * Created by hasee-pc on 2016/2/24.
 */
public class RealNameActivity extends TitleActivity {
    private EditText etName;
    private EditText etIdCard;

    @Override
    protected void initView() {
        super.initView();
        setTitle("实名信息");
        setContentView(R.layout.activity_realname);
        etName = (EditText) findViewById(R.id.realName_et_name);
        etName.setFilters(new InputFilter[]{new TextFilter.UserNameTextFilter()});
        etIdCard = (EditText) findViewById(R.id.realName_et_idCard);
        etIdCard.setFilters(new InputFilter[]{new TextFilter.IdCardTextFilter(), new InputFilter.LengthFilter(18)});
        String name = getIntent().getStringExtra("name");
        String identityCard = getIntent().getStringExtra("identityCard");
        if (!TextUtils.isEmpty(MyApplication.user.getName()) && !TextUtils.isEmpty(MyApplication.user.getIdentityCard())) {
            identityCard=MyApplication.user.getIdentityCard();
            etName.setText(name);
            etName.setEnabled(false);
//            Log.i("-----identityCard-----",identityCard.substring(0,3)+"----"+identityCard.substring(identityCard.length()-5,identityCard.length()-1));
            etIdCard.setText(identityCard.substring(0,3)+"********"+identityCard.substring(identityCard.length()-4,identityCard.length()));
            etIdCard.setEnabled(false);
            findViewById(R.id.realName_tv_2).setVisibility(View.GONE);
            findViewById(R.id.next).setVisibility(View.GONE);
        } else {
            findViewById(R.id.realName_tv_1).setVisibility(View.GONE);
        }



    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    public void realNameClick(View view) {
        if (check()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("name", etName.getText().toString());
            map.put("identityCard", etIdCard.getText().toString());//选填
            HttpSender sender = new HttpSender(Url.authentication, "实名登记", map, new OnHttpResultListener() {
                MaterialDialog materialDialog;

                @Override
                public void onSuccess(String message, String code, String data) {
                    if(code.equals("0000")){
                        MyApplication.user.setIdentityCard(etIdCard.getText().toString());
                        MyApplication.user.setName(etName.getText().toString());
                        SharedPreferencesUtil.getInstance(RealNameActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.USER,MyApplication.user);
//                        toast("认证成功！");
                         materialDialog = new MaterialDialog(RealNameActivity.this, new MaterialDialog.MaterialButtonListener() {

                            @Override
                            public void onCancel(TextView v) {
                                finish();
                            }

                            @Override
                            public void onConfirm(TextView v) {
                                finish();
                            }
                        });
                        materialDialog.setContentNorl("认证成功");
                    }else{
                        materialDialog = new MaterialDialog(RealNameActivity.this, new MaterialDialog.MaterialButtonListener() {

                            @Override
                            public void onCancel(TextView v) {
                                materialDialog.cancel();
                            }

                            @Override
                            public void onConfirm(TextView v) {
                                materialDialog.cancel();
                            }
                        });
                        materialDialog.setContentNorl(message);
                    }
                    materialDialog.show();

                }
            });
            sender.setContext(this);
            sender.send(Url.Post);
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            toast("用户名不能为空喔");
            etName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etIdCard.getText().toString().length() < 15) {
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
}
