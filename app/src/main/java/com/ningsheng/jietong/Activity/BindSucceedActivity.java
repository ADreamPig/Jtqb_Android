package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/3/2.
 */
public class BindSucceedActivity extends TitleActivity {
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_bindsucceed);
        showBackwardView("",true);
    }

    @Override
    protected void initListener() {
findViewById(R.id.act_bindsucceed_complete).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(BindSucceedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
});
    }

    @Override
    protected void initDate() {

    }
}
