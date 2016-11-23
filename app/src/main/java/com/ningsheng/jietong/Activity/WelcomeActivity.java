package com.ningsheng.jietong.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;

public class WelcomeActivity extends Activity {

    ImageView image;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        image = (ImageView) findViewById(R.id.act_welcome_image);

        ll = (LinearLayout) findViewById(R.id.act_welcome_ll);
        initAnim();
//        new Thread(){
//                @Override
//                public void run() {
//                    super.run();
//                    try {
//                        Thread.sleep(2000);
//                        Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }.start();
    }


    //    LinearLayout mLaunchLayout;
    private Animation mFadeIn, mFadeInScale;
    boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";
//    private ImageView iv_ydy;

    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.welcome_fade_in);
        mFadeIn.setDuration(1000);
        mFadeIn.setFillAfter(true);
        mFadeInScale = AnimationUtils.loadAnimation(WelcomeActivity.this,
                R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(1000);
        mFadeInScale.setFillAfter(true);


        String isGes=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE,"");
        if(!"".equals(isGes)&&MyApplication.isActive) {
            Intent intent = new Intent(this, GesturePassWordActivity.class);
            intent.putExtra("type", GesturePassWordActivity.TYPE_GESTUREPASSWORD_REPEATEDLY);
            startActivityForResult(intent, 0x100);
        }else{
            ll.startAnimation(mFadeIn);
        }


        mFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ll.startAnimation(mFadeInScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFadeInScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                String date_begin= SharedPreferencesUtil.getInstance(WelcomeActivity.this).getString("loginout","1970-12-01 00:00:00");
//                String date_end= DateUtils.dateToString("yyyy-MM-dd hh:mm:ss",new Date());
//
//                Log.i("----------------------",date_begin+"----------"+date_end+"-----------"+DateUtils.HourGap(date_begin,date_end));
//                if(DateUtils.HourGap(date_begin,date_end)<24*15*60*60*1000){//登录超时时间为15天
//                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
//                    intent.putExtra("islogin",false);//
//                    startActivity(intent);
//                    finish();
//                }else {

                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);//第一次登录
                startActivity(intent);
                finish();
//                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x100){
            ll.startAnimation(mFadeIn);
        }
    }
}
