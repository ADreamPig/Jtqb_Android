package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/5/9.
 */
public class WEGActivity extends TitleActivity implements View.OnClickListener {
    public final static int PAY_TYPE_WATER = 0x01;
    public final static int PAY_TYPE_ELE = 0x02;
    public final static int PAY_TYPE_GAS = 0x03;
    public final static int PAY_TYPE_BROADBAND = 0x04;

    private int type;
    private String city;
    private TextView mCompany, mStatus;
    private EditText mNumber;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_weg);
        showForwardView("合肥市", true);
        type = getIntent().getIntExtra("type", 0x01);
        mCompany = (TextView) findViewById(R.id.act_weg_company);
        mStatus = (TextView) findViewById(R.id.act_weg_status);
        mNumber = (EditText) findViewById(R.id.act_weg_number);

        switch (type) {
            case PAY_TYPE_WATER:
                setTitle("水费");
                break;
            case PAY_TYPE_ELE:
                setTitle("电费");
                break;
            case PAY_TYPE_GAS:
                setTitle("燃气费");
                break;
            case PAY_TYPE_BROADBAND:
                setTitle("宽带");
                break;
        }
    }

    @Override
    protected void initListener() {
        findViewById(R.id.act_weg_next).setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void onForward() {
//        super.onForward();
        Intent intent1 = new Intent(this, SelectCityActivity.class);
        startActivityForResult(intent1, 44);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.act_weg_next:
                intent = new Intent(this, PayAffirmActivity.class);
                break;
        }
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 44 && resultCode == 33 && data != null) {
            city = data.getStringExtra("name");
        }
    }
}
