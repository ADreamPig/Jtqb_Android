package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.App.BaseMeActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.View.ToogleButton;

/**
 * 安全设置
 * Created by hasee-pc on 2016/2/24.
 */
public class SafeSetupActivity extends BaseMeActivity {
    private ToogleButton button;
    private String isGesture;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_safe_setup);
        setTitle("安全设置");
        button = (ToogleButton) findViewById(R.id.toogleButton);

//        if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivityForResult(intent,11);
//        }

        isGesture = SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE, "");
        if (isGesture.equals("")) {
            button.setChecked(false);
        } else {
            button.setChecked(true);
        }


    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initDate() {

    }

    /**
     * 实名登记
     *
     * @param view
     */
    public void safeOnClick1(View view) {
        Intent intent = new Intent(this, RealNameActivity.class);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("identityCard", getIntent().getStringExtra("identityCard"));
        startActivity(intent);
    }

    /**
     * 更改手机号码
     *
     * @param view
     */
    public void safeOnClick2(View view) {
        Intent intent = new Intent(this, ChangePhoneActivity.class);
        intent.putExtra("mobile", getIntent().getStringExtra("mobile"));
        startActivity(intent);
    }

    /**
     * 更改登录密码
     *
     * @param view
     */
    public void safeOnClick3(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 更改支付密码
     *
     * @param view
     */
    public void safeOnClick4(View view) {
        Intent intent = new Intent(this, ChangePayPwActivity.class);
        intent.putExtra("type", ChangePayPwActivity.TYPE_UPDATE);
        startActivity(intent);
    }

    /**
     * 手势密码
     *
     * @param view
     */
    public void safeOnClick5(View view) {
        if (isGesture.equals("")) {
            Intent intent = new Intent(this, GesturePassWordActivity.class);
            intent.putExtra("type", GesturePassWordActivity.TYPE_GESTUREPASSWORD_FIRST);
            startActivityForResult(intent, 0x100);
        } else {
            SharedPreferencesUtil.getInstance(this).cleanBeantoSharedPreferences(SharedPreferencesUtil.GESTURE, null);
            button.setChecked(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 0x100:
                button.setChecked(true);
                break;
            default:
                break;
        }
    }
}
