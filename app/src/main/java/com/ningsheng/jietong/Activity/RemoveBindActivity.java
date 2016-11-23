package com.ningsheng.jietong.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/23.
 */
public class RemoveBindActivity extends TitleActivity implements View.OnClickListener {
    private TextView tvPhone;
    private TextView tvYzm;
    private EditText etYzm;
    private TextView tvNext;
    private int time = 60;
    private SmsObserver smsObserver;
    private String cardNo;
    private Handler handelr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x100:
                    if (time != 0) {
                        tvYzm.setText(time + "S");
                    } else {
                        tvYzm.setText("重新获取验证码");
                        tvYzm.setEnabled(true);
                    }
                    break;
                case 0x101:
                    time = 0;
                    tvYzm.setText("重新获取验证码");
                    tvYzm.setEnabled(true);
                    etYzm.setText(msg.obj.toString());

                    break;

            }

        }
    };

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_removebind);
        setTitle("解绑");
        tvPhone = (TextView) findViewById(R.id.removeBind_tv_phone);
        etYzm = (EditText) findViewById(R.id.removeBind_et_yzm);
        tvYzm = (TextView) findViewById(R.id.removeBind_tv_yzm);
        tvNext = (TextView) findViewById(R.id.removeBind_next);
        tvYzm.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        cardNo = getIntent().getStringExtra("cardNo");
        tvPhone.setText(getIntent().getStringExtra("mobile"));
        tvPhone.setText(MyApplication.user.getMobile());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    private void getYzm1() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reqMobile", tvPhone.getText().toString());
        map.put("reqDevice", MyApplication.getOnlyId());
        map.put("reqIp", MyApplication.getLocalIpAddress());
        map.put("reqFlag", "TPL_UNBAND");
        HttpSender sender = new HttpSender(Url.sendSms, "发短信", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("操作成功".equals(message)) {
                    tvYzm.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (time > 0) {
                                time--;
                                handelr.sendEmptyMessage(0x100);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    private void unBind() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("cardNo", cardNo);
        HttpSender sender = new HttpSender(Url.unBind, "解绑", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("9075".equals(code)) {
toast("解绑成功！");
                    Intent intent=new Intent(RemoveBindActivity.this,MyCardActivity.class);
                    intent.putExtra("type","wall");
                    startActivity(intent);
                    finish();
                } else {
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.removeBind_tv_yzm:
                getYzm1();
                break;
            case R.id.removeBind_next:
                if (check()) {
                    unBind();
                }
                break;
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(etYzm.getText().toString())) {
            toast("亲，忘记输短信验证码了");
            etYzm.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etYzm.getText().toString().length() < 6) {
            toast("亲，短信验证码好像不正确喔");
            etYzm.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else {
            return true;
        }
        return false;
    }

    private class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Cursor cursor = getContentResolver().query(
                    Uri.parse("content://sms/inbox"), null, null, null,
                    "date desc");
            if (cursor != null && cursor.moveToNext()) {
                String content = cursor
                        .getString(cursor.getColumnIndex("body"));
                cursor.close();
                if (content.contains("瑞祥支付")) {
                    int index = content.indexOf("：") + 2;
                    String sms = content.substring(index, index + 6);
                    Message msg = handelr.obtainMessage();
                    msg.obj = sms;
                    msg.what = 0x101;
                    msg.sendToTarget();
                }
            }
        }
    }
}
