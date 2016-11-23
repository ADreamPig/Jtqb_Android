package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/5/6.
 */
public class LifePayActivity extends TitleActivity implements View.OnClickListener{

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_life_pay);
        setTitle("生活缴费");
        showBackwardView("",true);
        showForwardView("缴费记录",true);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_life_pay_water).setOnClickListener(this);
        findViewById(R.id.act_life_pay_electricity).setOnClickListener(this);
        findViewById(R.id.act_life_pay_gas).setOnClickListener(this);
        findViewById(R.id.act_life_pay_broadband).setOnClickListener(this);
        findViewById(R.id.act_life_pay_tel).setOnClickListener(this);



    }

    @Override
    protected void initDate() {

    }



    @Override
    protected void onForward() {
//        super.onForward();
        Intent intent=new Intent(this,PayMentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.act_life_pay_water://水费
                intent=new Intent(this,WEGActivity.class);
                intent.putExtra("type",WEGActivity.PAY_TYPE_WATER);
                break;
            case R.id.act_life_pay_electricity://电费
                intent=new Intent(this,WEGActivity.class);
                intent.putExtra("type",WEGActivity.PAY_TYPE_ELE);
                break;
            case R.id.act_life_pay_gas://燃气
                intent=new Intent(this,WEGActivity.class);
                intent.putExtra("type",WEGActivity.PAY_TYPE_GAS);
                break;
            case R.id.act_life_pay_broadband://宽带
                intent=new Intent(this,BroadTelActivity.class);
                intent.putExtra("type",BroadTelActivity.PAY_TYPE_BROADBAND);
                break;
            case R.id.act_life_pay_tel://固定电话
                intent=new Intent(this,BroadTelActivity.class);
                intent.putExtra("type",BroadTelActivity.PAY_TYPE_TEL);
                break;
        }
        startActivity(intent);
    }
}
