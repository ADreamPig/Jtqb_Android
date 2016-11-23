package com.ningsheng.jietong.Activity;

import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.TransRecor;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class RecordDetilActivity extends TitleActivity {
    private TransRecor get;
    private TextView m_orderNo;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_record_detail);
        setTitle("购单详情");
        showBackwardView("",true);

         get=(TransRecor)getIntent().getSerializableExtra("order");

        TextView m_name=(TextView) findViewById(R.id.act_record_detail_name);
        TextView m_money=(TextView) findViewById(R.id.act_record_detail_money);
        TextView m_time=(TextView) findViewById(R.id.act_record_detail_time);
        TextView m_type=(TextView) findViewById(R.id.act_record_detail_type);
         m_orderNo=(TextView) findViewById(R.id.act_record_detail_orderNo);
        m_name.setText(get.getTransMerchantName());
        m_money.setText(get.getTransMoney());
        m_time.setText(get.getAddTime());
        m_orderNo.setText(get.getTradeNo()+"");
        m_type.setText(get.getHandlerOperationType());

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {
        Map<String,String> map=new HashMap<String,String>();
        map.put("id",get.getId());
        HttpSender sender=new HttpSender(Url.transRecordInfo, "消费记录详情", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                String tradeNo=(String)gsonUtil.getInstance().getValue(data,"tradeNo");
                m_orderNo.setText(tradeNo);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
