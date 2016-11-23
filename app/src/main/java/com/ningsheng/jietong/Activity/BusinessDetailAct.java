package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MerchantInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/19.
 */
public class BusinessDetailAct extends TitleActivity implements View.OnClickListener {
    private String id;
    private TextView m_name, m_discunt, m_address, m_phone;
    private ImageView m_image;

    @Override
    protected void initListener() {
        setContentView(R.layout.activity_business_detail);
        setTitle("详情");
        showBackwardView("", true);
        id = getIntent().getStringExtra("id");


        m_name = (TextView) findViewById(R.id.act_business_detail_name);
        m_discunt = (TextView) findViewById(R.id.act_business_detail_discunt);
        m_address = (TextView) findViewById(R.id.act_business_detail_address);
        m_phone = (TextView) findViewById(R.id.act_business_detail_phone);
        m_image = (ImageView) findViewById(R.id.act_business_detail_image);
        m_image.getLayoutParams().height = AndroidUtil.getScreenSize(this, 1) * 247 / 524;
        m_address.setOnClickListener(this);

    }

    private MerchantInfo merchantInfo;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(Url.merchantInfo, "商户详情", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                merchantInfo = gsonUtil.getInstance().json2Bean(data, MerchantInfo.class);
                m_name.setText(merchantInfo.getMerchantName());
                m_address.setText(merchantInfo.getAddress());
                if (merchantInfo.getMobile() != null && !merchantInfo.getMobile().equals("")) {
                    m_phone.setText(merchantInfo.getMobile());
                }else if(merchantInfo.getTel()!=null&&!merchantInfo.getTel().equals("")){
                    m_phone.setText(merchantInfo.getTel());
                }
                if (Double.parseDouble(merchantInfo.getDiscount()) == 10) {
                    m_discunt.setVisibility(View.GONE);
                } else {
                    m_discunt.setText(merchantInfo.getDiscount() + "折");
                }

                MediaUtil.displayImage(BusinessDetailAct.this, merchantInfo.getImageInfo(), m_image);
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.act_business_detail_address:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("lat", merchantInfo.getLat());
                intent.putExtra("lng", merchantInfo.getLng());
                intent.putExtra("address", merchantInfo.getAddress());
                startActivity(intent);
                break;
        }
    }
}
