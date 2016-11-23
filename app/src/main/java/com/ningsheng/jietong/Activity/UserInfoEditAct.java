package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.KeyboardUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.View.ClearWriteEditText;
import com.ningsheng.jietong.View.SecurityPasswordEditText;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class UserInfoEditAct extends TitleActivity implements View.OnClickListener ,ClearWriteEditText.OnEditChangeListener{
    private SecurityPasswordEditText myEdit;
    private View view;
    private KeyboardUtil keyboardUtil;
    private View m_scroll, m_ll;
    private ClearWriteEditText m_name,m_num;
    private String pwd;
    private  Button button;

    @Override
    protected void initView() {
        super.initView();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.act_userinfo_edit);
        setTitle("完善账户信息");
        showBackwardView("", true);

         m_name=(ClearWriteEditText) findViewById(R.id.act_userinfo_edit_name);
         m_num=(ClearWriteEditText) findViewById(R.id.act_userinfo_edit_num);
//        m_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                if(hasFocus){
//                    keyboardUtil.hideKeyboard();
////                }
//
//            }
//        });
        m_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.hideKeyboard();
                return false;
            }
        });
        m_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                keyboardUtil.hideKeyboard();
            }
        });

        view = getLayoutInflater().inflate(R.layout.act_userinfo_edit, null);
        m_scroll = findViewById(R.id.act_userinfo_edit_scroll);
        m_ll = findViewById(R.id.act_userinfo_edit_ll);
        myEdit = (SecurityPasswordEditText) findViewById(R.id.my_edit);
        keyboardUtil = new KeyboardUtil(UserInfoEditAct.this, UserInfoEditAct.this, myEdit);
//        TextView tvClose = (TextView) view.findViewById(R.id.tv_close);
        myEdit.setSecurityEditCompleListener(new SecurityPasswordEditText.SecurityEditCompleListener() {

            @Override
            public void onNumCompleted(String num) {
                pwd=num;
//                keyboardUtil.hideKeyboard();
            }
        });

        myEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardUtil != null) {
                    keyboardUtil.showKeyboard();
                    scrollToBottom(m_scroll, m_ll);
                }
            }
        });
//        tvClose.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                popWindow.dismiss();
//            }
//        });

        Method setShowSoftInputOnFocus = null;
        try {
            setShowSoftInputOnFocus = myEdit.getClass().getMethod(
                    "setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(myEdit, false);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new KeyboardUtil(this, UserInfoEditAct.this, myEdit).showKeyboard();

    }

    @Override
    protected void initListener() {
        button=(Button) findViewById(R.id.act_userinfo_edit_commit);
        button.setOnClickListener(this);
        m_name.setListener(this);
        m_num.setListener(this);
    }

    @Override
    protected void initDate() {

    }

    private void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }

                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }
    private void identityCard(){
        Map<String,String> map=new HashMap<String,String>();
        map.put("id", MyApplication.user.getId());
        map.put("name",m_name.getText().toString());
        map.put("identityCard",m_num.getText().toString());
        HttpSender sender=new HttpSender(Url.authentication, "验证身份证", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    commit();
                }else{
                    toast(message);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
    private void commit(){
        Map<String,String> map=new HashMap<>();
        map.put("id",MyApplication.user.getId());
        map.put("newPwd",pwd);
        HttpSender sender=new HttpSender(Url.assignTranPwd, "设置（重置）支付密码", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    toast("设置成功！");
                    Intent intent = new Intent(UserInfoEditAct.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
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
            case R.id.act_userinfo_edit_commit:
                if(m_name.getText().toString().equals("")){
                    m_name.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                    toast("");
                    return;


                }
                identityCard();


//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
                break;
        }

    }
    private boolean name_change,pass_change;
    @Override
    public void change(View view, boolean ischange) {
        switch (view.getId()) {
            case R.id.act_userinfo_edit_name:
                name_change = ischange;
                break;
            case R.id.act_userinfo_edit_num:
                pass_change = ischange;
                break;
        }
        if ((name_change) && (pass_change)) {
            button.setBackgroundResource(R.drawable.shape_button);
            button.setEnabled(true);
//            rippleView.setEnabled(true);
        } else {
            button.setBackgroundResource(R.drawable.shape_messagecode_gary);
            button.setEnabled(false);
//            rippleView.setEnabled(false);
        }
    }
}
