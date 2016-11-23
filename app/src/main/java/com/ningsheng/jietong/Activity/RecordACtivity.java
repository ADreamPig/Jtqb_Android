package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ningsheng.jietong.Adapter.DividerItemDecoration;
import com.ningsheng.jietong.Adapter.ItemDivider;
import com.ningsheng.jietong.Adapter.RecordAdapter;
import com.ningsheng.jietong.Adapter.RecycleOnItemClickLisiener;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.TransRecor;
import com.ningsheng.jietong.Entity.TransRecordEntity;
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
 * Created by zhushunqing on 2016/2/24.
 */
public class RecordACtivity extends TitleActivity {
    private RecyclerView listView;
    private MaterialRefreshLayout pull;
    private RecordAdapter adapter;
    private List<TransRecor> list;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_record);
        setTitle("交易记录");
        showBackwardView("", true);

        pull = (MaterialRefreshLayout) findViewById(R.id.act_record_lsit_pull);
        listView = (RecyclerView) findViewById(R.id.act_record_list);
        list = new ArrayList<>();
        listView.setLayoutManager(new LinearLayoutManager(this));

        //添加分割线
//        listView.addItemDecoration(new DividerItemDecoration(
//               this, DividerItemDecoration.HORIZONTAL_LIST));
        listView.addItemDecoration(new ItemDivider(this, R.drawable.recycler_line));
        adapter = new RecordAdapter(this, list);
        listView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        adapter.setListener(new RecycleOnItemClickLisiener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                Intent intent = new Intent(RecordACtivity.this, RecordDetilActivity.class);
                intent.putExtra("order", list.get(position));
                startActivity(intent);
            }

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Intent intent=new Intent(RecordACtivity.this,RecordDetilActivity.class);
//                int s=10;
//                int sum=0;
//                for(int i=0;i<10;i++){
//                    sum+=s;
//                    s+=2;
//                }
//                toast(sum+"");
//                System.out.print(sum);
//            }
        });
        pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                limitStart = 0;
                limitEnd=20;
                list.clear();
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });
    }

    private int limitStart = 0;
    private int limitEnd=20;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        map.put("limitStart", limitStart + "");
        map.put("limitEnd", limitEnd + "");
        HttpSender sender = new HttpSender(Url.queryTransRecord, "交易记录列表", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    limitStart+=20;
                    limitEnd+=20;
                    TransRecordEntity get = (TransRecordEntity) gsonUtil.getInstance().json2Bean(data, TransRecordEntity.class);
                    List<TransRecor> mlist = get.getData();
                    list.addAll(mlist);
                    adapter.notifyDataSetChanged();
                } else {
                    toast(message);
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                pull.finishRefresh();
                pull.finishRefreshLoadMore();
            }
        });
        sender.setContext(limitStart==0?this:null);
        sender.send(Url.Post);
    }
}
