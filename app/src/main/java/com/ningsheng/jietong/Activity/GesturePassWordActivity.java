package com.ningsheng.jietong.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.StatusBar.StatusBarHelper;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.BitmapUtil;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.View.LockPattern;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 张恒 on 2015/9/1.
 */
public class GesturePassWordActivity extends BaseActivity implements LockPattern.OnCompleteListener, OnClickListener {
    private LockPattern lockPattern;
    private ImageView ivHead;
    private Vibrator vibrator;
    private String password;
    private GradientDrawable drawable;
    private TextView m_forget;
    /**
     * 第一次设置手势密码
     */
    public static final int TYPE_GESTUREPASSWORD_FIRST = 0X001;

    /**
     * 进入后台之后,返回解锁手势密码
     */
    public static final int TYPE_GESTUREPASSWORD_REPEATEDLY = 0X002;
    /**
     * 修改手势密码
     */
    public static final int TYPE_GESTUREPASSWORD_CHANGEPASSWORD = 0X003;
    /**
     * 未安全退出之后,解锁手势密码
     */
    public static final int TYPE_GUSTUREPASSWORD_LOGIN = 0x004;
    /**
     * 最多错误的次数
     */
    private int count = 5;
    private int type;
    private TextView tvTitle, tvTitleBottom;
    private ImageView ivOk;


    @Override
//    protected void onTintStatusBar(int color) {
//        if (mStatusBarHelper == null) {
//            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,
//                    StatusBarHelper.LEVEL_21_NORMAL_FULL);
//        }
//        mStatusBarHelper.setActivityRootLayoutFitSystemWindows(false);
//        mStatusBarHelper.setColor(color);
//    }

    protected void initView() {
        onTintStatusBar(0x22EDA618);
        type = getIntent().getIntExtra("type", -1);
        if (type == -1) {
            finish();
            return;
        }
        setContentView(R.layout.activity_gesture_password);
        Bitmap bitmap = BitmapUtil.getBitmapFromResourceId(getResources(), R.mipmap.gesture_bg, MyApplication.getScreenWidth() >> 1, MyApplication.getScreenHeight() >> 1);
        findViewById(R.id.linear).setBackgroundDrawable(new BitmapDrawable(bitmap));
        ivHead = (ImageView) findViewById(R.id.gesture_iv_head);
        lockPattern = (LockPattern) findViewById(R.id.gesture_lockPattern);
        m_forget=(TextView) findViewById(R.id.act_gesture_password_forget);
        lockPattern.setOnCompleteListener(this);
        lockPattern.setType(type);
        tvTitle = (TextView) findViewById(R.id.gesture_tv_title);
        tvTitleBottom = (TextView) findViewById(R.id.gesture_tv_titleBottom);
        ivOk = (ImageView) findViewById(R.id.gesture_iv_ok);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        switch (type) {
            case TYPE_GESTUREPASSWORD_FIRST:
                tvTitle.setText("欢迎您");
                tvTitleBottom.setText("画一个解锁手势");
                break;
            case TYPE_GUSTUREPASSWORD_LOGIN:
            case TYPE_GESTUREPASSWORD_REPEATEDLY:
                tvTitle.setText("欢迎您");
                tvTitleBottom.setText("请输入手势密码");
                m_forget.setVisibility(View.VISIBLE);
                break;
            case TYPE_GESTUREPASSWORD_CHANGEPASSWORD:
                tvTitle.setText("欢迎您");
                tvTitleBottom.setText("画一个新的解锁手势");
                break;

        }
    }

    @Override
    protected void initListener() {
        m_forget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(GesturePassWordActivity.this);
                dialog.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginOut();
                        Intent intent=new Intent(GesturePassWordActivity.this, LoginActivity.class);
//                        intent.putExtra("action",MainActivity.LOGINOUT);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setMessage("忘记手势密码，需重新登录");
                dialog.show();
            }
        });

    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onComplete(final String password) {

        tvTitleBottom.setTextColor(0xFFFFFFFF);
        String isGesture=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE,"");
        Log.i("------密码------",password+"---"+isGesture);
        switch (type) {
            case TYPE_GESTUREPASSWORD_FIRST:
                if (tvTitleBottom.getTag() != null) {
                    ivOk.setVisibility(View.VISIBLE);
                    tvTitleBottom.setText("手势密码设置成功");
                    lockPattern.setTouchEnableLong(false);
                    tvTitle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferencesUtil.getInstance(GesturePassWordActivity.this).cleanBeantoSharedPreferences(SharedPreferencesUtil.GESTURE,password);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 150);
                } else {
                    tvTitleBottom.setText("手势绘制成功,请再确认一次!");
                    tvTitleBottom.setTag(1);
                    lockPattern.setPasswrod(password);
                }
                break;

            case TYPE_GESTUREPASSWORD_REPEATEDLY:
//                String isGesture=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE,"");
                if(isGesture.equals(password)){
                    ivOk.setVisibility(View.VISIBLE);
                    tvTitleBottom.setText("解锁成功");
                }


                break;
            case TYPE_GUSTUREPASSWORD_LOGIN:
                if(isGesture.equals(password)){
                    ivOk.setVisibility(View.VISIBLE);
                    tvTitleBottom.setText("解锁成功");
                }
//                ivOk.setVisibility(View.VISIBLE);
//                tvTitleBottom.setText("解锁成功");

                break;
            case TYPE_GESTUREPASSWORD_CHANGEPASSWORD:
                if (tvTitleBottom.getTag() != null) {
                    ivOk.setVisibility(View.VISIBLE);
                    tvTitleBottom.setText("手势密码设置成功");
                    lockPattern.setTouchEnableLong(false);
                    tvTitleBottom.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferencesUtil.getInstance(GesturePassWordActivity.this).cleanBeantoSharedPreferences(SharedPreferencesUtil.GESTURE,password);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 300);
                } else {
                    tvTitleBottom.setText("手势绘制成功,请再确认一次!");
                    tvTitleBottom.setTag(1);
                    lockPattern.setPasswrod(password);
                }
                break;

        }


    }

    @Override
    public void onPwdShortOrLong(int pwdLength) {
        Log.d("-------onPwdShortOrLong---------",pwdLength+"---"+password);
        String gesture=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE,"");
//        this.password = null;
        tvTitleBottom.setTextColor(0xFFED2525);
        switch (type) {
            case TYPE_GESTUREPASSWORD_FIRST:
                if (tvTitleBottom.getTag() != null) {
                    lockPattern.setTouchEnableLong(false);
                    tvTitleBottom.setText("两次密码不同,请重新绘制!");
                    lockPattern.setTouchEnableLong(true);
                    lockPattern.setPasswrod(null);
                    tvTitleBottom.setTag(null);
                    ivOk.setVisibility(View.GONE);
                } else {
                    tvTitleBottom.setText("至少需要连接4个点,请重新绘制!");
                }
                break;
            case TYPE_GUSTUREPASSWORD_LOGIN:
            case TYPE_GESTUREPASSWORD_REPEATEDLY:
                if(!this.password.equals(gesture)) {
                    tvTitleBottom.setText("密码错了,还可输入" + --count + "次");
                    if (count == 0) {
                        loginOut();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivityForResult(intent, 11);
                        finish();
                    }
                }else{
                    finish();
                }

                break;
            case TYPE_GESTUREPASSWORD_CHANGEPASSWORD:
                if (tvTitleBottom.getTag() != null) {
                    lockPattern.setTouchEnableLong(false);
                    tvTitleBottom.setText("两次密码不同,请重新绘制!");
                    lockPattern.setTouchEnableLong(true);
                    lockPattern.setPasswrod(null);
                    tvTitleBottom.setTag(null);
                    ivOk.setVisibility(View.GONE);
                } else {
                    tvTitleBottom.setText("至少需要连接4个点,请重新绘制!");
                }
                break;
        }
        tvTitleBottom.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));

    }

    @Override
    public void onSlide(String password) {
        if (type != TYPE_GUSTUREPASSWORD_LOGIN && type != TYPE_GESTUREPASSWORD_REPEATEDLY && !password.equals(this.password)) {
            vibrator.vibrate(30);
        }
        this.password = password;
        tvTitleBottom.setTextColor(0xFFFFFFFF);
        tvTitleBottom.setText("绘制完成后松开手指!");
    }

    @Override
    public void onStartSlide() {
    }

    @Override
    public void onClick(View v) {
    }



    @Override
    public void onBackPressed() {
//        TYPE_GESTUREPASSWORD_REPEATEDLY
        if (type == TYPE_GESTUREPASSWORD_REPEATEDLY || type == TYPE_GUSTUREPASSWORD_LOGIN) {
//            if (type == TYPE_GUSTUREPASSWORD_LOGIN)
                return;
        }
        super.onBackPressed();
    }

    public void gestureBack(View v){
        onBackPressed();
    }

}
