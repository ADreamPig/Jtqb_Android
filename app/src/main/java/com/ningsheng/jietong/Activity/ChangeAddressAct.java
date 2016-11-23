package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.ChangeAddressAdapter;
import com.ningsheng.jietong.Adapter.CheckAddressAdapter;
import com.ningsheng.jietong.Adapter.ItemDivider;
import com.ningsheng.jietong.Adapter.MyAddressAdapter;
import com.ningsheng.jietong.Adapter.RecordAdapter;
import com.ningsheng.jietong.Adapter.RecycleOnItemClickLisiener;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/3/8.
 */
public class ChangeAddressAct extends TitleActivity {
    private ListView m_recyle;
    private List<MyAddressInfo> list = new ArrayList<>();
    private CheckAddressAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_change_address);
        setTitle("选择收货地址");
        showBackwardView("", true);

        m_recyle = (ListView) findViewById(R.id.act_change_address_recyle);
//        m_recyle.setLayoutManager(new LinearLayoutManager(this));

//        adapter = new ChangeAddressAdapter(this, list);
         adapter=new CheckAddressAdapter(this,list);
//        m_recyle.addItemDecoration(new ItemDivider(this, R.drawable.recycler_line));

        m_recyle.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
//        adapter.setOnItemClickListenet(new RecycleOnItemClickLisiener() {
//            @Override
//            public void OnItemClickListener(View view, int position) {
//
//            }
//        });
        m_recyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPosition(position);
                adapter.notifyDataSetChanged();
                Intent intent=getIntent();
                intent.putExtra("address",list.get(position));
                setResult(0x33,intent);
                finish();

            }
        });
    }

    private int limitStart;

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("limitStart", String.valueOf(limitStart));
        map.put("limitEnd", "155");
        HttpSender sender = new HttpSender(Url.shippingaddressListInfo, "获取收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    List<MyAddressInfo> MList = (List<MyAddressInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<MyAddressInfo>>() {
                    }.getType());
                    if (MList != null && !MList.isEmpty()) {
                        list.clear();
                        list.addAll(MList);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    toast(message);
                }

            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
