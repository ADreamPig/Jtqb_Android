package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.R;

/**
 * Created by zhushunqing on 2016/2/22.
 */
public class TicketActivity extends TitleActivity implements View.OnClickListener {
    private Button m_commit;
    private TextView m_content;

    @Override
    protected void initListener() {
        setContentView(R.layout.activity_ticket);
        setTitle("移动电子券");
        showBackwardView("", true);

        m_commit = (Button) findViewById(R.id.act_ticket_commit);
        m_commit.setOnClickListener(this);
        m_content=(TextView)findViewById(R.id.act_ticket_content);
        m_content.setText(Html.fromHtml("<font color=#eda618>移动电子券是是什么？</font><br>移动电子券全名叫做“移动和包电子券”是中国移动赠送的电子礼金可在合作商家购物消费。<br><br><font color=#eda618>移动电子券的支付场景有哪些？</font><br>线上Web端通过电子券网站<a href='http://www.cmpay.com'>www.cmpay.com</a>页面输入移动和包电子券支付密码完成支付线下消费在pos机输入手机号，会获得电子券验证码，输入验证码完成支付<br><br><font color=#eda618>有哪些商家支持移动电子券？</font><br>线上支持唯品会、京东商城、万达、百盛集团线下支持较多的快餐、超市、加油站等。"));
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_ticket_commit:
                Intent intent = new Intent(this, ExTicketActivity.class);
                startActivity(intent);
                break;
        }
    }
}
