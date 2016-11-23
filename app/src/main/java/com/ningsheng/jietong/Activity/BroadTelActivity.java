package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/5/9.
 */
public class BroadTelActivity extends TitleActivity implements View.OnClickListener {
    public final static int PAY_TYPE_BROADBAND = 0x04;
    public final static int PAY_TYPE_TEL = 0x05;
    private int type;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_broad_tel);
        type=getIntent().getIntExtra("type",0x04);
        switch (type) {
            case PAY_TYPE_BROADBAND:
                setTitle("宽带");
                break;
            case PAY_TYPE_TEL:
                setTitle("固定电话");
                break;
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_broad_tel_next).setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.act_broad_tel_next:
                intent = new Intent(this, PayAffirmActivity.class);
                break;
        }
        startActivity(intent);
    }
}
