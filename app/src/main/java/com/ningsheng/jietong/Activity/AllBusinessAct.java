package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.BusinessAdapter;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.Entity.PagingEntity;
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
 * Created by zhushunqing on 2016/2/19.
 */
public class AllBusinessAct extends TitleActivity {
    private ListView m_list;
    private MaterialRefreshLayout pull;
    private List<Merchant> list = new ArrayList<Merchant>();
    private BusinessAdapter adapter;

    @Override
    protected void initListener() {
        setContentView(R.layout.activity_allactivity);
        setTitle("商家推荐");
        showBackwardView("返回", true);


        m_list = (ListView) findViewById(R.id.act_allact_list);
        pull = (MaterialRefreshLayout) findViewById(R.id.act_allact_list_pull);
        pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page=0;
                list.clear();
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
            }
        });

        adapter = new BusinessAdapter(this, list);
        m_list.setAdapter(adapter);
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AllBusinessAct.this, BusinessDetailAct.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private int page = 0;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("limitStart", page + "");
        map.put("limitEnd", "20");
//        map.put("city", MyApplication.city);
        map.put("lng", MyApplication.lng + "");
        map.put("lat", MyApplication.lat + "");
//        map.put("area","")
        HttpSender sender = new HttpSender(Url.queryMerchantByLngLat, "商户列表", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    page++;
//                    List<Merchant> MList = (List<Merchant>) gsonUtil.getInstance().json2List(data, new TypeToken<List<Merchant>>() {
//                    }.getType());
                    PagingEntity get = gsonUtil.getInstance().json2Bean(data, PagingEntity.class);
                    List<Merchant> MList = get.getData();

                    if (MList != null && MList.size() != 0) {
                        list.addAll(MList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                pull.finishRefreshLoadMore();
                pull.finishRefresh();
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
