package com.ningsheng.jietong.App;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ningsheng.jietong.Activity.GesturePassWordActivity;
import com.ningsheng.jietong.App.StatusBar.StatusBarHelper;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.SwipeBack.SwipeBackActivity;
import com.ningsheng.jietong.Utils.ActUtil;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhushunqing on 2016/1/28.
 */
public abstract class BaseActivity extends SwipeBackActivity {

    protected Toolbar toolbar;
    protected AppBarLayout appBarLayout;

    protected boolean SCREEN_ORIENTATION_PORTRAIT = true;// 是否禁止横竖屏切换

    private ConnectivityManager mConnectivityManager;

    private NetworkInfo netInfo;


    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();

                if(netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                        /////WiFi网络
//                        snackbar.dismiss();
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                        /////有线网络
//                        snackbar.dismiss();
                    }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                        /////////3g网络
//                        snackbar.dismiss();
                    }
                } else {
                    ////////网络断开
                     Snackbar snackbar=null;
                      snackbar = Snackbar.make(findViewById(android.R.id.content), "请检查网络连接！", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("设置", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
//                            snackbar.dismiss();
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);//系统设置界面
                            startActivity(intent);
                        }
                    });
                    snackbar.show();

                }
            }

        }
    };

    protected SystemBarTintManager mTintManager;
    //    private ColorPicker mColorPicker;
    protected StatusBarHelper mStatusBarHelper;

    protected void onTintStatusBar(int color) {
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,
                    StatusBarHelper.LEVEL_21_VIEW);
        }
        mStatusBarHelper.setColor(color);
    }


    protected void immersiveStatusBar() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getAttributes().systemUiVisibility |= (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow()
                    .clearFlags(
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    );
            try {
                Field drawsSysBackgroundsField = WindowManager.LayoutParams.class
                        .getField("FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS");
                getWindow().addFlags(drawsSysBackgroundsField.getInt(null));

                Method setStatusBarColorMethod = Window.class
                        .getDeclaredMethod("setStatusBarColor", int.class);
//					Method setNavigationBarColorMethod = Window.class
//							.getDeclaredMethod("setNavigationBarColor",
//									int.class);
                setStatusBarColorMethod.invoke(getWindow(),
                        Color.TRANSPARENT);
//					setNavigationBarColorMethod.invoke(getWindow(),
//							Color.TRANSPARENT);
            } catch (Exception e) {

            }
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActUtil.getInstance().addActivity(this);
        if (SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 禁止横屏
        }

        /////////动态注册广播 监听网络状态
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);


        initView();
        initListener();
        initDate();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initDate();

    private Toast toast = null;

    public void toast(String msg) {
        if (null == msg || msg.equals("")) {
            return;
        }
        if (null == toast)
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        else
            toast.setText(msg);
        toast.show();
    }

    /**
     * 退出登录
     */
    protected void loginOut(){
        MyApplication.user = new User();
        SharedPreferencesUtil.getInstance(MyApplication.getInstance()).setBeantoSharedPreferences(SharedPreferencesUtil.USER, null);
        SharedPreferencesUtil.getInstance(MyApplication.getInstance()).cleanBeantoSharedPreferences(SharedPreferencesUtil.RRID_REGISTER, "");
        SharedPreferencesUtil.getInstance(MyApplication.getInstance()).cleanBeantoSharedPreferences(SharedPreferencesUtil.GESTURE,null);
        JPushInterface.setAlias(this, "", null);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            MyApplication.isActive = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        if (!MyApplication.isActive) {
            MyApplication.isActive = true;
            String isGesture=SharedPreferencesUtil.getInstance(this).getString(SharedPreferencesUtil.GESTURE,"");
            if(!isGesture.equals("")){
                Intent intent = new Intent(this, GesturePassWordActivity.class);
                intent.putExtra("type", GesturePassWordActivity.TYPE_GESTUREPASSWORD_REPEATEDLY);
                startActivityForResult(intent, 0x100);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 12) {//登陆成功后
            initDate();
        }
        if (requestCode == 11 && resultCode == 13) {//点返回键后的返回
            if (!MainActivity.class.isInstance(this)) {
                finish();
            }

        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }



//    /**
//     * 系统toolbar样式 需要菜单时需重写onCreateOptionsMenu()方法
//     *
//     * @param title
//     */
//    protected void initTitle(String title) {
//        toolbar.setVisibility(View.VISIBLE);
////        getRe
//        setSupportActionBar(toolbar);
//        appBarLayout.setFitsSystemWindows(false);//toolbar是否延伸到状态栏
//        toolbar.setTitle(title);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.color_while));
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(BaseActivity.this, "返回", Toast.LENGTH_SHORT).show();
//                finish();
//
//            }
//        });
//
//    }

//    protected void initTitle(String title, String RightText, int layoutResources) {
//        toolbar.setVisibility(View.VISIBLE);
//        appBarLayout.setFitsSystemWindows(false);//toolbar是否延伸到状态栏
//        setSupportActionBar(toolbar);
//        if (layoutResources == 0) {
//            View view = getLayoutInflater().inflate(R.layout.app_title, toolbar);
//            TextView mTitle = (TextView) view.findViewById(R.id.app_title_custom);
//            TextView mRight = (TextView) view.findViewById(R.id.app_title_right);
//            mTitle.setText(title);
//            mRight.setText(RightText);
//            view.findViewById(R.id.app_title_right).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(BaseActivity.this, "提交", Toast.LENGTH_SHORT).show();
//                }
//            });
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//
//                }
//            });
//        }
//    }

//    /**
//     * 设置activity布局
//     *
//     * @param layoutId
//     */
//    protected void initContentView(int layoutId) {
//        if (layoutId != 0) {
//            RelativeLayout addView = (RelativeLayout) findViewById(R.id.baseactivity_addview);
//            addView.addView(getLayoutInflater().inflate(layoutId, null));
//        }
//    }


}
