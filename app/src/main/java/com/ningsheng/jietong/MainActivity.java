package com.ningsheng.jietong;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ningsheng.jietong.Activity.ActivityActivity;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.StatusBar.StatusBarHelper;
import com.ningsheng.jietong.Fragmen.BusinessFragment;
import com.ningsheng.jietong.Fragmen.HomeFragment;
import com.ningsheng.jietong.Fragmen.MyFragment;
import com.ningsheng.jietong.Fragmen.WalletFragment;
import com.ningsheng.jietong.SwipeBack.SwipeBackLayout;
import com.ningsheng.jietong.Utils.ActUtil;
import com.ningsheng.jietong.Utils.CheckUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

//biaoji ff
//@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @ViewInject(R.id.act_main_radiogroup)
    private RadioGroup radioGroup;
    private List<BaseFragment> list = new ArrayList<BaseFragment>();

//    private LocationClient mLocClient;
    private SwipeBackLayout mSwipeBackLayout;
    private MyFragment myFragment;
    private WalletFragment walletFragment;
    private String activity_id;


    @Override
    protected void onTintStatusBar(int color) {
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,
                    StatusBarHelper.LEVEL_21_NORMAL_FULL);
        }
        mStatusBarHelper.setActivityRootLayoutFitSystemWindows(false);
        mStatusBarHelper.setColor(color);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.content_main);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEnableGesture(false);
        onTintStatusBar(Color.TRANSPARENT);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setVisibility(View.GONE);
        activity_id = getIntent().getStringExtra("id");
        Uri u=getIntent().getData();
        Log.d("-----MainActivity-----",activity_id+"-----"+ u);

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

//        mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setNavigationBarTintEnabled(true);
//        mTintManager.setTintColor(0x00612599);
//        mColorPicker = (ColorPicker) findViewById(R.id.color_picker);
//        applySelectedColor();

//        mButton = (Button) findViewById(R.id.button);
//        mButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                applySelectedColor();
//            }
//        });


        radioGroup = (RadioGroup) findViewById(R.id.act_main_radiogroup);
        HomeFragment homeFragment = new HomeFragment();
        walletFragment = new WalletFragment();
        BusinessFragment businessFragment = new BusinessFragment();
        myFragment = new MyFragment();
        list.add(homeFragment);
        list.add(walletFragment);
        list.add(businessFragment);
        list.add(myFragment);

        displayFragment(homeFragment);

    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        applySelectedColor();
//    }
//
//    private void applySelectedColor() {
////        int selected = mColorPicker.getColor();
//        int selected=0x008BCAC1;
//        int color = Color.argb(Color.alpha(selected), Color.red(selected), Color.green(selected), Color.blue(selected));
//        mTintManager.setTintColor(color);
//    }

    private void displayFragment(Fragment f) {
        // TODO Auto-generated method stub
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.act_main_frame, f);
        ft.commit();
    }

    public Fragment getVisibleFragment() {
        for (BaseFragment fragment : list) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private Fragment mContent;

    public void switchContent(Fragment from, Fragment to) {
//		  mContent=getVisibleFragment();
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过  to是你要跳转的Fragment from是你当前显示的Fragment
                transaction.hide(from).add(R.id.act_main_frame, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.act_main_radiobutton1:
                        onTintStatusBar(Color.TRANSPARENT);
                        switchContent(getVisibleFragment(), list.get(0));
                        break;
                    case R.id.act_main_radiobutton2:
                        onTintStatusBar(getResources().getColor(R.color.app_color));
//                        onTintStatusBar(Color.TRANSPARENT);
                        switchContent(getVisibleFragment(), list.get(1));
                        walletFragment.refresh();
                        break;
                    case R.id.act_main_radiobutton3:
                        onTintStatusBar(getResources().getColor(R.color.app_color));
                        switchContent(getVisibleFragment(), list.get(2));
                        break;
                    case R.id.act_main_radiobutton4:
                        onTintStatusBar(getResources().getColor(R.color.app_color));
                        switchContent(getVisibleFragment(), list.get(3));
                        myFragment.onRefresh();
                        break;
                }
            }
        });

    }

    @Override
    protected void initDate() {
        if (activity_id != null) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(MyApplication.NOTIFICATION_ID);
            Intent intent = new Intent(this, ActivityActivity.class);
            intent.putExtra("id", activity_id);
            startActivity(intent);
        }
    }

    private long exitTime = 0l;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(this);
                ActUtil.getInstance().AppExit(this);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static int LOGINOUT = 0x101;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra("action", 0) == LOGINOUT) {
            myFragment.onRefresh();//退出登录 刷新我的信息
            walletFragment.refresh();//注册成功！
        }else{
            myFragment.onRefresh();//
            walletFragment.refresh();//
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initUmeng() {
        OnlineConfigAgent.getInstance().updateOnlineConfig(this);
        //获取友盟在线参数
        String update_mode = OnlineConfigAgent.getInstance().getConfigParams(this, "upgrade_mode");
        System.out.println("============友盟在线参数:" + update_mode);
        if (!CheckUtil.isNull(update_mode)) {
            String[] updates = update_mode.split(",");
            int versionCode = 0;
            int uMVersionCode = 0;
            try {
                versionCode = Integer.parseInt("当前版本");
                uMVersionCode = Integer.parseInt(updates[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (versionCode < uMVersionCode) {
                if ("F".equals(updates[1])) {
                    UmengUpdateAgent.forceUpdate(this);
                    //对话框按键的监听，对于强制更新的版本，如果用户未选择更新的行为，关闭app
                    UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {

                        @Override
                        public void onClick(int status) {
                            switch (status) {
                                case UpdateStatus.Update:
                                    break;
                                default:
                                    //友盟自动更新目前还没有提供在代码里面隐藏/显示更新对话框的
                                    //"以后再说"按钮的方式，所以在这里弹个Toast比较合适
                                    Toast.makeText(MainActivity.this,
                                            "非常抱歉，您需要更新应用才能继续使用", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    }, 500);

                            }
                        }
                    });
                } else {
                    //友盟自动更新
                    UmengUpdateAgent.update(this);
                    //是否在非wifi下也更新
                    UmengUpdateAgent.setUpdateOnlyWifi(true);
                }
            }
        } else {
            //友盟自动更新
            UmengUpdateAgent.update(this);
            //是否在非wifi下也更新
            UmengUpdateAgent.setUpdateOnlyWifi(true);
        }


    }

}
