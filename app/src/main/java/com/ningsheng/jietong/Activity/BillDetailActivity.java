package com.ningsheng.jietong.Activity;

import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.Entity.FlowingDetailData;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.DateUtils;

/**
 * 账单详情
 * Created by hasee-pc on 2016/3/1.
 */
public class BillDetailActivity extends TitleActivity {
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_bill_detail);
        setTitle("消费明细");
        showBackwardView("",true);

        TextView m_busness=(TextView) findViewById(R.id.act_bill_busness);
        TextView _money=(TextView) findViewById(R.id.act_bill_money);
        TextView _type=(TextView) findViewById(R.id.act_bill_type);
        TextView _trantime=(TextView) findViewById(R.id.act_bill_trantime);
        TextView _orderNo=(TextView) findViewById(R.id.act_bill_orderNo);

        FlowingDetailData get=(FlowingDetailData)getIntent().getSerializableExtra("info");
        m_busness.setText(get.getUnitName1());
        _money.setText("￥"+get.getAmount());
//        if(get.get){}
        _type.setText(get.getTransCode());
        String date1= DateUtils.dateToString("yyyy-MM-dd HH:mm:ss",DateUtils.stringToDate("yyyyMMddHHmmss",get.getTransDate()  + get.getTransTime()));
        _trantime.setText(date1);
        _orderNo.setText(get.getCardCode1());

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }
}
