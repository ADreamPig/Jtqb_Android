package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ningsheng.jietong.Adapter.ConsumeDetailAdapter;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.ConsumeDetailInfo;
import com.ningsheng.jietong.Entity.ConsumerDetails;
import com.ningsheng.jietong.Entity.DetailData;
import com.ningsheng.jietong.Entity.FlowingDetailData;
import com.ningsheng.jietong.Entity.sortClass;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.DateUtils;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.PinnedHeaderListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 消费明细
 * Created by hasee-pc on 2016/3/1.
 */
public class ConsumeDetailActivity extends TitleActivity {
    private PinnedHeaderListView listView;
    private List<FlowingDetailData> datas;
    private ConsumeDetailAdapter adapter;
    private String cardNo;
    private MaterialRefreshLayout m_pull;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_consume_detail);
        setTitle("消费明细");
        showBackwardView("", true);
        cardNo = getIntent().getStringExtra("cardNo");

        listView = (PinnedHeaderListView) findViewById(R.id.listView);
        m_pull=(MaterialRefreshLayout)findViewById(R.id.act_consume_detail_pull) ;
        m_pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                page=1;datas.clear();
                initDate();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                initDate();
            }
        });
        datas = new ArrayList<>();
//        datas.add(new ConsumeDetailInfo("1", "1.0"));
//        datas.add(new ConsumeDetailInfo("1", "1.1"));
//        datas.add(new ConsumeDetailInfo("1", "1.2"));
//        datas.add(new ConsumeDetailInfo("1", "1.3"));
//        datas.add(new ConsumeDetailInfo("1", "1.4"));
//        datas.add(new ConsumeDetailInfo("2", "2.0"));
//        datas.add(new ConsumeDetailInfo("2", "2.1"));
//        datas.add(new ConsumeDetailInfo("2", "2.2"));
//        datas.add(new ConsumeDetailInfo("2", "2.3"));
//        datas.add(new ConsumeDetailInfo("3", "3.0"));
//        datas.add(new ConsumeDetailInfo("3", "3.1"));
//        datas.add(new ConsumeDetailInfo("3", "3.2"));
//        datas.add(new ConsumeDetailInfo("4", "4.0"));
//        datas.add(new ConsumeDetailInfo("4", "4.1"));
//        datas.add(new ConsumeDetailInfo("5", "5.0"));
//        datas.add(new ConsumeDetailInfo("5", "5.1"));
//        datas.add(new ConsumeDetailInfo("5", "5.2"));
//        datas.add(new ConsumeDetailInfo("5", "5.3"));
//        datas.add(new ConsumeDetailInfo("5", "5.4"));
//        datas.add(new ConsumeDetailInfo("5", "5.5"));
//        datas.add(new ConsumeDetailInfo("5", "5.6"));
//        datas.add(new ConsumeDetailInfo("5", "5.7"));
//        datas.add(new ConsumeDetailInfo("5", "5.8"));
//        datas.add(new ConsumeDetailInfo("5", "5.9"));
        adapter = new ConsumeDetailAdapter(datas, this);
//        View HeaderView = getLayoutInflater().inflate(R.layout.layout_item_consumedetail_1_title, listView, false);
//        listView.setPinnedHeader(HeaderView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(adapter);
    }

    @Override
    protected void initListener() {
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            private boolean bool = false;
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                System.out.println("firstVisibleItem: " + firstVisibleItem + " visibleItemCount: " + visibleItemCount + " totalItemCount: " + totalItemCount);
//                View v = view.getChildAt(0);
//                if (v == null)
//                    return;
//                int type = adapter.getItemViewType(firstVisibleItem);
//                switch (type) {
//                    case ConsumeDetailAdapter.TYPE_TITLE:
//                        if (firstVisibleItem == 0) {
//                            viewTitle.setVisibility(View.VISIBLE);
//                        } else {
//                            if (bool) {
//                                viewTitle.setY(v.getTop());
//                            }
//                        }
//                        break;
//                    case ConsumeDetailAdapter.TYPE_ITEM:
//                        if (adapter.getItemViewType(firstVisibleItem + 1) == ConsumeDetailAdapter.TYPE_TITLE) {
//                            bool = true;
//                        } else {
//                            bool = false;
//                        }
//                        viewTitle.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.getItemViewType(position) == ConsumeDetailAdapter.TYPE_TITLE) {
                    return;
                }
                Intent intent = new Intent(ConsumeDetailActivity.this, BillDetailActivity.class);
                intent.putExtra("info",datas.get(position));
                startActivity(intent);
            }
        });


    }

    private int page = 1;
    private String month1,month2;

    @Override
    protected void initDate() {
        month1 = null; month2 = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("cardNo", cardNo);
        map.put("startIndex", page + "");
        map.put("pageSize",20+ "");
        map.put("lastMonths", "6");
        map.put("endDate", DateUtils.getCurrentDate());
        HttpSender sender = new HttpSender(Url.consumerDetails, "卡消费明细", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("9083")) {
                    page++;
                    String isNext = (String) gsonUtil.getInstance().getValue(data, "isNext");
                    if (Integer.parseInt(isNext)>=0) {
                        ConsumerDetails get = gsonUtil.getInstance().json2Bean(data, ConsumerDetails.class);
                        Log.i("-----get----", get.toString() + "");
//                        if (get.getTransList().equals("1")) {
                            DetailData d = (DetailData) get.getTransList();
                            List<FlowingDetailData> Mlist = d.getTransactionFlowingDetailData();
                            if (Mlist != null && Mlist.size() > 0) {
                                datas.addAll(Mlist);
//                    sortClass sort = new sortClass();
//                    Collections.sort(datas, sort);
//                    Comparator descComparator = Collections.reverseOrder(sort);
//                    Collections.sort(datas, descComparator);

                                for (int i = 0; i < datas.size(); i++) {
                                    datas.get(i).setTitle(datas.get(i).getTransDate().substring(0, 8));
                                    String s = datas.get(i).getTransDate();
                                    month1 = datas.get(i).getTransDate().substring(0, 8);
                                    if (i != 0) {
                                        month2 = datas.get(i - 1).getTransDate().substring(0, 8);
                                    }
                                    if (!month1.equals(month2)&&datas.get(0).getAmount()!=null) {
                                        datas.add(i, new FlowingDetailData(month1, datas.get(i).getTransDate(), datas.get(i).getTransTime()));
i++;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                Log.i("-----list-----", datas.toString());
                            } else {
//                                toast("没有数据了1");
                            }
//                        } else {
//                            toast("没有数据了2");
//                        }

                    } else {
                        toast(message);
                    }
                }
            }

            @Override
            public void onFinished() {
                super.onFinished();
                m_pull.finishRefresh();
                m_pull.finishRefreshLoadMore();
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
