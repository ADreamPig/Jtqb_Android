package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.CityEntity;
import com.ningsheng.jietong.Entity.DataDictInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ListViewInScrollView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/3/3.
 */
public class SelectCityActivity extends TitleActivity {
    private TextView m_current, m_province;
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
        m_province = (TextView) findViewById(R.id.act_province_name);
        m_list = (ListViewInScrollView) findViewById(R.id.act_province_list);
        m_current.setText(MyApplication.city);
        m_province.setText("安徽省");
        adapter = new ArrayAdapter(this, R.layout.linear_image_text, R.id.linear_image_text_text, p_list);
        m_list.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        m_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", MyApplication.city);
                setResult(33, intent);
                finish();
            }
        });
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("name", p_list.get(position));
                setResult(33, intent);
                finish();
            }
        });
    }

    @Override
    protected void initDate() {


        String Slist_ = SharedPreferencesUtil.getInstance(SelectCityActivity.this).getBeanfromSharedPreferences(SharedPreferencesUtil.PROVINCE, String.class);
        List<DataDictInfo> Slist = (List<DataDictInfo>) gsonUtil.getInstance().json2List(Slist_, new TypeToken<List<DataDictInfo>>() {
        }.getType());
        Log.d("------Slist------", Slist + "");
        if (Slist != null && Slist.size() != 0) {
            if (System.currentTimeMillis() - Slist.get(0).getTime() < 15 * 24 * 60 * 60 * 1000) {
                for (DataDictInfo d : Slist) {
                    if (d.getProvince().equals("安徽省")) {
                        for (int i = 0; i < d.getList().size(); i++) {
                            p_list.add(d.getList().get(i).getName());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
//            return;
        } else {
            Map<String, String> map = new HashMap<String, String>();
            HttpSender sender = new HttpSender(Url.getDataDictInfo, "省市", map, new OnHttpResultListener() {
                @Override
                public void onSuccess(String message, String code, String data) {

                    List<DataDictInfo> Mlist = (List<DataDictInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<List<DataDictInfo>>() {
                    }.getType());
                    if (Mlist != null && Mlist.size() > 0) {
                        Mlist.get(0).setTime(System.currentTimeMillis());
                    SharedPreferencesUtil.getInstance(SelectCityActivity.this).setBeantoSharedPreferences(SharedPreferencesUtil.PROVINCE, gsonUtil.getInstance().toJson(Mlist));
                        for (DataDictInfo d : Mlist) {
                            if (d.getProvince().equals("安徽省")) {
                                for (int i = 0; i < d.getList().size(); i++) {
                                    p_list.add(d.getList().get(i).getName());
                                }
                            }

                        }
                        adapter.notifyDataSetChanged();
//                    list.addAll(Mlist);
                    }


//                Map<String, Object> map =  gsonUtil.getInstance().json2Map(data);
//                    List<CityEntity> entity = (List<CityEntity>) gsonUtil.getInstance().json2List(gsonUtil.getInstance().toJson(map.get("安徽省")), new TypeToken<List<CityEntity>>() {
//                    }.getType());
//                for(CityEntity cn:entity){
//                    p_list.add(cn.getName());
//                }
//                adapter.notifyDataSetChanged();
                }
            });
            sender.setContext(this);
            sender.send(Url.Post);
        }
    }
}
