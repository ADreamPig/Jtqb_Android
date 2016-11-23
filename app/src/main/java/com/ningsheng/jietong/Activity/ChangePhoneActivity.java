package com.ningsheng.jietong.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.TextFilter;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/25.
 */
public class ChangePhoneActivity extends TitleActivity implements View.OnClickListener {
    private AnimationSet vMain2InAnimationSet;
    private AnimationSet vMain1OutAnimationSet;
    private AnimationSet vMain2OutAnimationSet;
    private AnimationSet vMain1InAnimationSet;
    private View vMain1;
    private TextView etPhone1, etYzm1, tvYzm1, tvNext1;
    private int time1 = 60;
    private View vMain2;
    private TextView etYzm2, tvYzm2, tvNext2;//etPhone2
    private int time2 = 60;
    private boolean isNext;
    private SmsObserver smsObserver;
    private EditText etPhone2;
    private Handler handelr = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x100:
                    if (time1 != 0) {
                        tvYzm1.setText(time1 + "S");
                    } else {
                        tvYzm1.setText("重新获取验证码");
                        tvYzm1.setEnabled(true);
                    }
                    break;
                case 0x101:
                    time1 = 0;
                    tvYzm1.setText("重新获取验证码");
                    tvYzm1.setEnabled(true);
                    etYzm1.setText(msg.obj.toString());

                    break;
                case 0x102:
                    if (time2 != 0) {
                        tvYzm2.setText(time2 + "S");
                    } else {
                        tvYzm2.setText("重新获取验证码");
                        tvYzm2.setEnabled(true);
                    }
                    break;
                case 0x103:
                    time2 = 0;
                    tvYzm2.setText("重新获取验证码");
                    tvYzm2.setEnabled(true);
                    etYzm2.setText(msg.obj.toString());
                    break;
            }

        }
    };

    @Override
    protected void initView() {
        super.initView();
        setTitle("更改手机号码");
        setContentView(R.layout.activity_change_phone);
        vMain1 = findViewById(R.id.changePhone_linear_main1);
        etPhone1 = (EditText) findViewById(R.id.changePhone_et_phone1);
        String mobile = getIntent().getStringExtra("mobile");
        if (!TextUtils.isEmpty(mobile))
            etPhone1.setEnabled(false);
        etPhone1.setText(mobile);
        etYzm1 = (EditText) findViewById(R.id.changePhone_et_yzm1);
        tvYzm1 = (TextView) findViewById(R.id.changePhone_tv_yzm1);
        tvNext1 = (TextView) findViewById(R.id.changePhone_tv_next1);
        etPhone2=(EditText) findViewById(R.id.changePhone_et_phone2);
        etPhone1.setFilters(new InputFilter[]{new TextFilter.MobileTextFilter(), new InputFilter.LengthFilter(11)});
        tvYzm1.setOnClickListener(this);
        tvNext1.setOnClickListener(this);
        vMain2InAnimationSet = new AnimationSet(true);
        vMain2InAnimationSet.setDuration(400);
        vMain2InAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        AlphaAnimation inAlphaAnimation = new AlphaAnimation(0, 1);
        TranslateAnimation inTranslateAnimation = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0f);
        vMain2InAnimationSet.addAnimation(inAlphaAnimation);
        vMain2InAnimationSet.addAnimation(inTranslateAnimation);
        vMain1OutAnimationSet = new AnimationSet(true);
        vMain1OutAnimationSet.setDuration(400);
        vMain1OutAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        AlphaAnimation outAlphaAnimation = new AlphaAnimation(1, 0);
        TranslateAnimation outTranslateAnimation = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.2f);
        vMain1OutAnimationSet.addAnimation(outAlphaAnimation);
        vMain1OutAnimationSet.addAnimation(outTranslateAnimation);

        vMain2OutAnimationSet = new AnimationSet(true);
        vMain2OutAnimationSet.setDuration(400);
        vMain2OutAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        AlphaAnimation outAlphaAnimation1 = new AlphaAnimation(1, 0);
        TranslateAnimation outTranslateAnimation1 = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, -1f);
        vMain2OutAnimationSet.addAnimation(outAlphaAnimation1);
        vMain2OutAnimationSet.addAnimation(outTranslateAnimation1);
        vMain1InAnimationSet = new AnimationSet(true);
        vMain1InAnimationSet.setDuration(400);
        vMain1InAnimationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        AlphaAnimation inAlphaAnimation1 = new AlphaAnimation(0, 1);
        TranslateAnimation inTranslateAnimation1 = new TranslateAnimation(0, 0, 0, 0, Animation.RELATIVE_TO_PARENT, 0.2f, Animation.RELATIVE_TO_PARENT, 0f);
        vMain1InAnimationSet.addAnimation(inAlphaAnimation1);
        vMain1InAnimationSet.addAnimation(inTranslateAnimation1);

        smsObserver = new SmsObserver(this, null);
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true,
                smsObserver);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }
   private  String rrid1;
    private void SmSCode() {
        if (check1()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("dxyz", etYzm1.getText().toString());
            map.put("rrid", rrid1);
            HttpSender sender = new HttpSender(Url.validateSmsCode, "", map, new OnHttpResultListener() {
                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("0000".equals(code)) {
//                         rrid=(String)gsonUtil.getInstance().getValue(data,"rrid");
//                         rrid1=(String)gsonUtil.getInstance().getValue(data,"rrid");
//                        if(check1()){
                        oldMobileValidation();
//                        }
                    }else{
                        toast(message);
                    }
                }
            });
        sender.setContext(this);
        sender.send(Url.Post);
        }
    }
    private  String rrid2;
    private void SmSCode2() {
        if (check2()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("dxyz", etYzm2.getText().toString());
            map.put("rrid", rrid2);
            HttpSender sender = new HttpSender(Url.validateSmsCode, "", map, new OnHttpResultListener() {
                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("0000".equals(code)) {
//                        rrid2=(String)gsonUtil.getInstance().getValue(data,"rrid");
//                        if(check2()) {
                            updateNewMobile();
//                        }
                    }else{
                        toast(message);
                    }
                }
            });
        sender.setContext(this);
        sender.send(Url.Post);
        }
    }

    private void oldMobileValidation() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("mobile", etPhone1.getText().toString());
            HttpSender sender = new HttpSender(Url.oldMobileValidation, "验证原手机", map, new OnHttpResultListener() {

                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("9030".equals(code)) {
                        vMain1.startAnimation(vMain1OutAnimationSet);
                        vMain2.startAnimation(vMain2InAnimationSet);
                        vMain1.setVisibility(View.GONE);
                        vMain2.setVisibility(View.VISIBLE);
                        isNext = true;
                    } else {
                        toast(message);
                    }
                }
            });
            sender.setContext(this);
            sender.send(Url.Post);
        }


    private void updateNewMobile() {
//        if (check2()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", MyApplication.user.getId());
            map.put("mobile", etPhone2.getText().toString());
            HttpSender sender = new HttpSender(Url.updateNewMobile, "重置手机号", map, new OnHttpResultListener() {

                @Override
                public void onSuccess(String message, String code, String data) {
                    if ("9081".equals(code)) {
                        toast("重置手机号成功");
                        finish();
                    } else {
                        toast(message);
                    }
                }
            });
            sender.setContext(this);
            sender.send(Url.Post);
//        }
    }

    private boolean check1() {
        if (TextUtils.isEmpty(etPhone1.getText().toString())) {
            toast("亲，忘记输手机号了");
            etPhone1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etPhone1.getText().toString().length() < 11) {
            toast("亲，手机号好像不正确喔");
            etPhone1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (TextUtils.isEmpty(etYzm1.getText().toString())) {
            toast("亲，忘记输短信验证码了");
            etYzm1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etYzm1.getText().toString().length() < 6) {
            toast("亲，短信验证码好像不正确喔");
            etYzm1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if(rrid1==null){
            toast("亲，请重新获取验证码！");
        }else{
            return true;
        }
        return false;
    }

    private boolean check2() {
        if (TextUtils.isEmpty(etPhone2.getText().toString())) {
            toast("亲，忘记输手机号了");
            etPhone2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etPhone2.getText().toString().length() < 11) {
            toast("亲，手机号好像不正确喔");
            etPhone2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (TextUtils.isEmpty(etYzm2.getText().toString())) {
            toast("亲，忘记输短信验证码了");
            etYzm2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if (etYzm2.getText().toString().length() < 6) {
            toast("亲，短信验证码好像不正确喔");
            etYzm2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        } else if(rrid2==null){
            toast("亲，请重新获取验证码！");
        }else{
            return true;
        }
        return false;
    }

    public void tel(View view){
        Intent intent1 = new Intent(Intent.ACTION_CALL);
        intent1.setData(Uri.parse("tel:4008871380"));
        startActivity(intent1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePhone_tv_next1://验证原手机
                if (vMain2 == null) {
                    vMain2 = findViewById(R.id.changePhone_linear_main2);
                    etYzm2 = (EditText) findViewById(R.id.changePhone_et_yzm2);
                    tvYzm2 = (TextView) findViewById(R.id.changePhone_tv_yzm2);
                    tvNext2 = (TextView) findViewById(R.id.changePhone_tv_next2);
                    etPhone2.setFilters(new InputFilter[]{new TextFilter.MobileTextFilter(), new InputFilter.LengthFilter(11)});
                    tvNext2.setEnabled(true);
                    tvYzm2.setOnClickListener(this);
                    tvNext2.setOnClickListener(this);
                }
//                oldMobileValidation();
                SmSCode();
                break;
            case R.id.changePhone_tv_next2:
//                updateNewMobile();
                SmSCode2();
                break;
            case R.id.changePhone_tv_yzm1:
                getYzm1();
                break;
            case R.id.changePhone_tv_yzm2:
                getYzm2();
                break;
        }
    }

    private void getYzm1() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reqMobile", etPhone1.getText().toString());
        map.put("reqDevice", MyApplication.getOnlyId());
        map.put("reqIp", MyApplication.getLocalIpAddress());
        map.put("reqFlag", "TPL_UPDATETEL");
        HttpSender sender = new HttpSender(Url.sendSms, "发短信", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("操作成功".equals(message)) {
                    rrid1=(String)gsonUtil.getInstance().getValue(data,"rrid");
                    tvYzm1.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (time1 > 0) {
                                time1--;
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

    private void getYzm2() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("reqMobile", etPhone2.getText().toString());
        map.put("reqDevice", MyApplication.getOnlyId());
        map.put("reqIp", MyApplication.getLocalIpAddress());
        map.put("reqFlag", "TPL_UPDATETEL");
        HttpSender sender = new HttpSender(Url.sendSms, "发短信", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                if ("操作成功".equals(message)) {
                    rrid2=(String)gsonUtil.getInstance().getValue(data,"rrid");
                    tvYzm2.setEnabled(false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (time2 > 0) {
                                time2--;
                                handelr.sendEmptyMessage(0x102);
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

    @Override
    public void onBackPressed() {
        if (isNext) {
            isNext = false;
            vMain1.startAnimation(vMain1InAnimationSet);
            vMain2.startAnimation(vMain2OutAnimationSet);
            vMain1.setVisibility(View.VISIBLE);
            vMain2.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(smsObserver);
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
                    if (isNext)
                        msg.what = 0x101;
                    else
                        msg.what = 0x103;
                    msg.sendToTarget();
                }
            }
        }
    }
}
