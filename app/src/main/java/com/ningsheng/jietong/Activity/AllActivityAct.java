package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.AllActivityAdapter;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.ActivityInfo;
import com.ningsheng.jietong.Entity.AddressInfo;
import com.ningsheng.jietong.Entity.MerchantSales;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/19.
 */
public class AllActivityAct extends TitleActivity {
    @ViewInject(R.id.act_allact_list)
    private ListView m_list;
    private int limitStart = 0;
    private int limitEnd = 20;
    private List<MerchantSales> list = new ArrayList<MerchantSales>();
    private AllActivityAdapter adapter;
    private MaterialRefreshLayout pull;
    private   Map<String, String> map = new HashMap<String, String>();


    @Override
    protected void initListener() {
        setContentView(R.layout.activity_allactivity);
        setTitle("优惠活动");
        showBackwardView("", true);

        m_list = (ListView) findViewById(R.id.act_allact_list);
        pull = (MaterialRefreshLayout) findViewById(R.id.act_allact_list_pull);
        adapter = new AllActivityAdapter(this, list);
        m_list.setAdapter(adapter);
//        pull.setSunStyle(true);
        pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                list.clear();
                limitStart = 0;
                limitEnd=20;
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });

        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllActivityAct.this, ActivityActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initDate() {
        map.clear();
        map.put("limitStart", limitStart + "");
        map.put("limitEnd", limitEnd + "");
        HttpSender sender = new HttpSender(Url.merchantSalesList, "所有活动", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    limitStart+=20;
                    limitEnd+=20;
                    ActivityInfo info = gsonUtil.getInstance().json2Bean(data, ActivityInfo.class);
                    List<MerchantSales> Mlist = info.getData();
//                List<MerchantSales> Mlist = (List<MerchantSales>) gsonUtil.getInstance().json2List(data, new TypeToken<List<MerchantSales>>() {
//                }.getType());
                    if (Mlist != null && Mlist.size() > 0) {
                        list.addAll(Mlist);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onFinished() {
                super.onFinished();
                pull.finishRefresh();
                pull.finishRefreshLoadMore();
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
