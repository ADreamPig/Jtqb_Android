package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class RegisterSucceedActivity extends TitleActivity {

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_register_succeed);
        setTitle("注册成功");
        showBackwardView("", true);


    }

    @Override
    protected void onBackward() {
//        super.onBackward();
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_register_succeed_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterSucceedActivity.this, UserInfoEditAct.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initDate() {

    }
}
