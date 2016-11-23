package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.InventedCertificateAdapter;
import com.ningsheng.jietong.App.BaseMeActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.InventedCertificateInfo;
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
 * Created by hasee-pc on 2016/2/23.
 * 我的电子券
 */
public class InventedCertificateActivity extends BaseMeActivity implements View.OnClickListener{
    private ListView listView;
    private InventedCertificateAdapter adapter;
    private List<InventedCertificateInfo> datas;
    private View m_NoData;
    private MaterialRefreshLayout m_pull;
    private TextView m_button;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_invented_certificate);
        setTitle("我的电子券");
        showBackwardView("", true);

        listView = (ListView) findViewById(R.id.listView);
        m_NoData = findViewById(R.id.nodata);
         m_button=(TextView) findViewById(R.id.nodata_next);
        m_button.setText("购买电子券");
        m_pull=(MaterialRefreshLayout)findViewById(R.id.act_invented_certificate_pull) ;
        datas = new ArrayList<>();
        adapter = new InventedCertificateAdapter(this, datas);
        listView.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        m_button.setOnClickListener(this);
        m_pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                datas.clear();
                page=1;
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });
    }

    private int page=1;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("pageIndex", page + "");
        map.put("pageSize", "10");
        HttpSender sender = new HttpSender(Url.ticketList, "电子券列表", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                m_pull.finishRefresh();
                m_pull.finishRefreshLoadMore();
                if (code.equals("0000")) {
                    page++;
                    List<InventedCertificateInfo> infos = (List<InventedCertificateInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<InventedCertificateInfo>>() {
                    }.getType());
                    if (infos != null && infos.size() > 0) {
                        if (infos != null && !infos.isEmpty()) {
                            datas.addAll(infos);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    toast(message);
                }
                if (datas.size() == 0) {
                    m_NoData.setVisibility(View.VISIBLE);
                } else {
                    m_NoData.setVisibility(View.GONE);
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
        switch (v.getId()){
            case R.id.nodata_next:
                Intent intent = new Intent(this, ExTicketActivity.class);
                startActivity(intent);
                break;
        }

    }
}
