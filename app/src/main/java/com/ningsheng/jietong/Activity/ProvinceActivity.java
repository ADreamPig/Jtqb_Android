package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.RegionAdaper;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.CityEntity;
import com.ningsheng.jietong.Entity.DataDictInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ExpandableListViewInSc;
import com.ningsheng.jietong.View.ListViewInScrollView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class ProvinceActivity extends TitleActivity {
    private TextView m_current;
    private ListViewInScrollView m_list;
    private List<DataDictInfo> list = new ArrayList<>();
    private List<String> p_list = new ArrayList<String>();
    private ArrayAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_province);
        setTitle("省市地区");
        showBackwardView("", true);

        m_current = (TextView) findViewById(R.id.act_province_current);
        m_list = (ListViewInScrollView) findViewById(R.id.act_province_list);
        //设置 属性 GroupIndicator 去掉默认向下的箭头
//        m_list.setGroupIndicator(null);
//        adapter=new RegionAdaper(list,this);


//        m_list.setAdapter(adapter);
        adapter = new ArrayAdapter(this, R.layout.linear_image_text, R.id.linear_image_text_text, p_list);
        m_list.setAdapter(adapter);


    }

    @Override
    protected void initListener() {
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
                intent.putExtra("name", p_list.get(position));
                intent.putExtra("data", (Serializable) list);
                intent.putExtra("source", getIntent().getIntExtra("source", 0));
                startActivity(intent);
            }
        });
//        m_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Intent intent=new Intent(ProvinceActivity.this,AreaActivity.class);
//                startActivity(intent);
//                return false;
//            }
//        });

    }

    @Override
    protected void initDate() {
        String Slist_ = SharedPreferencesUtil.getInstance(ProvinceActivity.this).getBeanfromSharedPreferences(SharedPreferencesUtil.PROVINCE, String.class);
        List<DataDictInfo> Slist = (List<DataDictInfo>) gsonUtil.getInstance().json2List(Slist_, new TypeToken<List<DataDictInfo>>() {
        }.getType());
        Log.d("------Slist------", Slist + "");
        if (Slist != null && Slist.size() != 0) {

            if (System.currentTimeMillis() - Slist.get(0).getTime() < 15 * 24 * 60 * 60 * 1000) {
                for (DataDictInfo d : Slist) {
                    p_list.add(d.getProvince());
                }
                list.addAll(Slist);
                adapter.notifyDataSetChanged();
            }
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        HttpSender sender = new HttpSender(Url.getDataDictInfo, "省市", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
//                Map<String, Object> map =  gsonUtil.getInstance().json2Map(data);
                List<DataDictInfo> Mlist = (List<DataDictInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<List<DataDictInfo>>() {
                }.getType());
                if (Mlist != null && Mlist.size() > 0) {
                    Mlist.get(0).setTime(System.currentTimeMillis());
                    SharedPreferencesUtil.getInstance(ProvinceActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.PROVINCE, gsonUtil.getInstance().toJson(Mlist));
                    for (DataDictInfo d : Mlist) {
                        p_list.add(d.getProvince());
                    }
                    list.addAll(Mlist);
                }
//                for (String key : map.keySet()) {
//                    p_list.add(key);
//                    List<CityEntity> entity = (List<CityEntity>) gsonUtil.getInstance().json2List(gsonUtil.getInstance().toJson(map.get(key)), new TypeToken<List<CityEntity>>() {
//                    }.getType());
//
//                    list.add(new DataDictInfo(key, entity));
//                }
                adapter.notifyDataSetChanged();
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);
    }
}
