package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;

import com.ningsheng.jietong.AliPay.PayUtil;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.OrderDetail;
import com.ningsheng.jietong.Entity.SubmitOrder;
import com.ningsheng.jietong.MainActivity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class PayActivity extends TitleActivity implements View.OnClickListener{
    private SubmitOrder order;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_pay);
        setTitle("购卡申请");
        showBackwardView("",true);

         order=(SubmitOrder) getIntent().getSerializableExtra("order");
//        order.set
findViewById(R.id.act_pay_zfb).setOnClickListener(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }

    private void commit(){
        HttpSender sender=new HttpSender(Url.submitOrder, "提交订单", order, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {

                OrderDetail get=gsonUtil.getInstance().json2Bean(data,OrderDetail.class);
                new PayUtil(PayActivity.this, "购卡", "购卡订单", get.getMoney(),get.getTradeNo(),get.getNotifyUrl(), new PayUtil.OnPayCallBack() {
                    @Override
                    public void onSucceed() {
                        toast("支付成功！");
                        Intent intent=new Intent(PayActivity.this,MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void OnPaying() {
                        toast("支付中。。。");
                    }

                    @Override
                    public void OnFailure() {
                        toast("支付失败请重新支付！");
                    }
                }).pay();

            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_pay_zfb:
                order.setPayType("3");
                commit();
                break;
        }
    }
}
