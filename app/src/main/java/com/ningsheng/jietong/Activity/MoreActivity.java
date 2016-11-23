package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.User;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MateriaDialogUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.StringUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 更多
 * Created by hasee-pc on 2016/2/24.
 */
public class MoreActivity extends TitleActivity implements View.OnClickListener {
    private TextView button;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_more);
        setTitle("更多");
    }

    @Override
    protected void initListener() {
//        if(){}
         button=(TextView)findViewById(R.id.button_login_out);
                button.setOnClickListener(this);
        findViewById(R.id.act_more_tell).setOnClickListener(this);
        if(MyApplication.user.getId()!=null&&!MyApplication.user.getId().equals("")){
            button.setBackgroundResource(R.drawable.shape_button);
            button.setEnabled(true);
        }else{
            button.setBackgroundResource(R.drawable.shape_messagecode_gary);
            button.setEnabled(false);
        }
    }

    @Override
    protected void initDate() {

    }

    public void moreOnClick1(View view) {
        if(MyApplication.user.getId()!=null&&!MyApplication.user.getId().equals("")){
            Intent intent = new Intent(this, FeedBackActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent,11);
        }


    }

    public void moreOnClick2(View view) {
    }

    public void moreOnClick3(View view) {
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(MoreActivity.this, updateInfo);
                        break;
                    case UpdateStatus.No: // has no update
                        toast("没有更新");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(MoreActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(MoreActivity.this, "超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.update(this);
    }

    public void moreOnClick4(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_more_tell:
                Intent intent1 = new Intent(Intent.ACTION_CALL);
                intent1.setData(Uri.parse("tel:4008871380"));
                startActivity(intent1);
                break;
            case R.id.button_login_out:
//                if(!StringUtil.isBlank(MyApplication.user.getId())){
//
//
//                Map<String,String> map=new HashMap<String,String>();
//                map.put("accountId",MyApplication.user.getId());
//                HttpSender sender=new HttpSender(Url.logout, "", map, new OnHttpResultListener() {
//                    @Override
//                    public void onSuccess(String message, String code, String data) {
//                        if(code.equals("0000")){
//                        MyApplication.user=new User();
//                        SharedPreferencesUtil.getInstance(MyApplication.getInstance()).setBeantoSharedPreferences(SharedPreferencesUtil.USER,null);
//                        SharedPreferencesUtil.getInstance(MyApplication.getInstance()).cleanBeantoSharedPreferences(SharedPreferencesUtil.RRID_REGISTER,"");
//                        Intent intent=new Intent(MoreActivity.this, MainActivity.class);
//                        intent.putExtra("action",MainActivity.LOGINOUT);
//                        startActivity(intent);
//                        }else{
//                            toast(message);
//                        }
//                    }
//                });
//                sender.setContext(this);
//                sender.send(Url.Post);
//                }else {
//                    MyApplication.user=new User();
//                    SharedPreferencesUtil.getInstance(MyApplication.getInstance()).setBeantoSharedPreferences(SharedPreferencesUtil.USER,null);
//                    SharedPreferencesUtil.getInstance(MyApplication.getInstance()).cleanBeantoSharedPreferences(SharedPreferencesUtil.RRID_REGISTER,"");
//                SharedPreferencesUtil.getInstance(MyApplication.getInstance()).cleanBeantoSharedPreferences(SharedPreferencesUtil.GESTURE,null);
//                JPushInterface.setAlias(this, "", new TagAliasCallback() {
//                    @Override
//                    public void gotResult(int i, String s, Set<String> set) {
//                        Log.i("---解绑成功----","解绑成功1");
//                    }
//                });
                loginOut();
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                intent.putExtra("action", MainActivity.LOGINOUT);
                startActivity(intent);
//                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 12) {//登陆成功后
            button.setBackgroundResource(R.drawable.shape_button);
            button.setEnabled(true);
            Intent intent = new Intent(this, FeedBackActivity.class);
            startActivity(intent);
        }
        if (requestCode == 11 && resultCode == 13) {//点返回键后的返回
            if (!MainActivity.class.isInstance(this)) {
                finish();
            }

        }

    }
}
