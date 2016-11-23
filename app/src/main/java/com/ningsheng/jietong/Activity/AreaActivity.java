package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.AreaEntity;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class AreaActivity extends TitleActivity {
    private ListView m_list;
    private List<String> list = new ArrayList<>();
    private String id, province, city, code;
    private ArrayAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_area);
        setTitle("省市地区");
        showBackwardView("", true);
        id = getIntent().getStringExtra("id");//区id
        province = getIntent().getStringExtra("province");
        city = getIntent().getStringExtra("city");
        code = getIntent().getStringExtra("code");

        m_list = (ListView) findViewById(R.id.act_area_lsit);
        TextView textView = new TextView(this);
        int padding = AndroidUtil.dip2px(this, 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextColor(getResources().getColor(R.color.app_color));
        textView.setText(province + " " + city);
        textView.setTextSize(16);
        m_list.addHeaderView(textView);
        adapter = new ArrayAdapter<String>(this, R.layout.app_text, R.id.app_text_text, list);
        m_list.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                if(position!=0) {
                    if (getIntent().getIntExtra("source", 0) == 1) {//个人中心新增收货地址
                        intent = new Intent(AreaActivity.this, AddressDetailsActivity.class);
                    } else {//购卡新增收货地址
                        intent = new Intent(AreaActivity.this, BuyCardFirstActivity.class);
                    }
                    intent.putExtra("province", province);
                    intent.putExtra("city", city);
                    intent.putExtra("area", list.get(position - 1));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", city);
        map.put("code", code);
        HttpSender sender = new HttpSender(Url.getAreaData, "获取区", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                try {
                    JSONObject jo = new JSONObject(data);
                    List<AreaEntity> Mlist = (List<AreaEntity>) gsonUtil.getInstance().json2List(jo.getString(city), new TypeToken<List<AreaEntity>>() {
                    }.getType());
                    for (AreaEntity a : Mlist) {
                        list.add(a.getName());
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
