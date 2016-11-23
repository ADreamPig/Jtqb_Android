package com.ningsheng.jietong.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.andexert.library.RippleView;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.SwipeBack.SwipeBackLayout;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.PermissionsChecker;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.ningsheng.jietong.Utils.Typefaces;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ClearWriteEditText;
import com.ningsheng.jietong.View.Titanic;
import com.ningsheng.jietong.View.TitanicTextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class LoginActivity extends TitleActivity implements View.OnClickListener ,ClearWriteEditText.OnEditChangeListener{
    private ClearWriteEditText m_mobile, m_password;
    private SwipeBackLayout mSwipeBackLayout;
    private  View m_login;
    private  RippleView rippleView;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_login);
        mPermissionsChecker = new PermissionsChecker(this);
        setTitle("用户登录");
        showBackwardView("", true);
        showForwardView("注册", true);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEnableGesture(false);

//        TitanicTextView text=(TitanicTextView) findViewById(R.id.TitanicTextView);
//        text.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
//        Titanic titanic = new Titanic();
//        titanic.start(text);

        findViewById(R.id.act_login_forget).setOnClickListener(this);
         m_login=findViewById(R.id.act_login_login);
        m_login .setOnClickListener(this);
        m_mobile = (ClearWriteEditText) findViewById(R.id.act_login_mobile);
        m_password = (ClearWriteEditText) findViewById(R.id.act_login_password);
         rippleView=(RippleView)findViewById(R.id.act_login_RippleView);

        LinearLayout line= (LinearLayout) findViewById(R.id.act_login_mobile_line);
        final LinearLayout line_pass= (LinearLayout) findViewById(R.id.act_login_pass_line);
        Animation anim1= AnimationUtils.loadAnimation(this,R.anim.login_line_in);
        line.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                line_pass.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.login_line_in));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onForward() {
        super.onForward();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(this, UserInfoEditAct.class);
//        startActivity(intent);
    }

    @Override
    protected void onBackward() {
        setResult(13);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        setResult(13);
        finish();
//        scrollToFinishActivity();
    }

    @Override
    protected void initListener() {
        m_mobile.setListener(this);
        m_password.setListener(this);
    }

    @Override
    protected void initDate() {
        User u = SharedPreferencesUtil.getInstance(MyApplication.getInstance()).getBeanfromSharedPreferences(SharedPreferencesUtil.USER, User.class);
        if (u != null) {
            m_mobile.setText(u.getMobile());
//            m_password.setText(u.get);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions( PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
//            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.READ_PHONE_STATE
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            setResult(13);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_login_forget:
                Intent intent = new Intent(this, ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.act_login_login:
                Login();
                break;
        }
    }

    private void Login() {
        if (!StringUtil.isMobileNo(m_mobile.getText().toString())) {
            toast("手机号格式有误！");
            m_mobile.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        if (m_password.getText().toString().equals("")) {
            toast("请输入登录密码！");
            m_password.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile", m_mobile.getText().toString());
        map.put("loginPwd", m_password.getText().toString());
        map.put("token", MyApplication.getOnlyId().replace("-", ""));
        map.put("clientType", "1");
//        map.put("token","1234567890");
        HttpSender sender = new HttpSender(Url.login, "登录", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    Set<String> set=new HashSet<>();
                    set.add("JtqbAll");
                    JPushInterface.setTags(LoginActivity.this,set,null);
                    JPushInterface.setAlias(LoginActivity.this, MyApplication.getOnlyId().replace("-", ""), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Log.i("=========", "绑定成功！" + MyApplication.getOnlyId().replace("-", ""));
                        }
                    });
                    User user = gsonUtil.getInstance().json2Bean(data, User.class);
                    MyApplication.user = user;
                    SharedPreferencesUtil.getInstance(MyApplication.getInstance()).setBeantoSharedPreferences(SharedPreferencesUtil.USER, user);
                    setResult(12);
                    finish();

                }
                else if (code.equals("9057")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                    double count = (Double) gsonUtil.getInstance().getValue(data, "loginPwdErrorCount");

                    dialog.setMessage(message + "\n(密码错误五次账户将被冻结)\n您的输错次数为：" + (int) count);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    private boolean name_change,pass_change;
    @Override
    public void change(View view, boolean ischange) {
        switch (view.getId()) {
            case R.id.act_login_mobile:
                name_change = ischange;
                break;
            case R.id.act_login_password:
                pass_change = ischange;
                break;
        }
        if ((name_change) && (pass_change)) {
            m_login.setBackgroundResource(R.drawable.shape_button);
            m_login.setEnabled(true);
            rippleView.setEnabled(true);
        } else {
            m_login.setBackgroundResource(R.drawable.shape_messagecode_gary);
            m_login.setEnabled(false);
            rippleView.setEnabled(false);
        }
    }
}
