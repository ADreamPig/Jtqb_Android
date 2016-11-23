package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.PayCardRecordAdapter;
import com.ningsheng.jietong.App.BaseMeActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.BuyCardOrder;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购卡记录
 * Created by hasee-pc on 2016/2/23.
 */
public class PayCardRecordActivity extends BaseMeActivity implements View.OnClickListener {
    private ListView listView;
    private PayCardRecordAdapter adapter;
    private List<BuyCardOrder> datas;
    private MaterialRefreshLayout m_pull;
    private View m_nodata;
    private TextView m_nodata_next;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_invented_certificate);
        setTitle("购卡记录");

        m_pull = (MaterialRefreshLayout) findViewById(R.id.act_invented_certificate_pull);
        listView = (ListView) findViewById(R.id.listView);
        m_nodata = findViewById(R.id.nodata);
        m_nodata_next = (TextView) findViewById(R.id.nodata_next);
        m_nodata_next.setText("去购卡");

        datas = new ArrayList<>();
        adapter = new PayCardRecordAdapter(this, datas);
        listView.setAdapter(adapter);
        m_pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                datas.clear();
                page = 1;
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });

    }

    @Override
    protected void initListener() {
        super.initListener();
        m_nodata_next.setOnClickListener(this);
    }

    private int page=1;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("pageIndex", page + "");
        map.put("pageSize", "10");//选填
        HttpSender sender = new HttpSender(Url.payCardList, "购卡列表", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                m_pull.finishRefresh();
                m_pull.finishRefreshLoadMore();
                if (code.equals("0000")) {
                    page++;
                    List<BuyCardOrder> list = (List<BuyCardOrder>) gsonUtil.getInstance().json2List(data, new TypeToken<List<BuyCardOrder>>() {
                    }.getType());
                    datas.addAll(list);
                    adapter.notifyDataSetChanged();
                } else {
                    toast(message);
                }
                if (datas == null || datas.size() == 0) {
                    m_nodata.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                } else {
                    m_nodata.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFinished() {
                super.onFinished();
                m_pull.finishRefresh();
                m_pull.finishRefreshLoadMore();
            }
        });
        sender.setContext(page == 0 ? this : null);
        sender.send(Url.Post);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nodata_next:
                Intent intent = new Intent(this, BuyCardFirstActivity.class);
                startActivity(intent);
                break;
        }
    }
}
