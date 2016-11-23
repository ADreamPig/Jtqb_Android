package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.Entity.CityEntity;
import com.ningsheng.jietong.Entity.DataDictInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.View.ListViewInScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushunqing on 2016/3/1.
 */
public class CityActivity extends TitleActivity {
    private ListViewInScrollView m_list;
    private List<DataDictInfo> list;
    private String province;
    private List<CityEntity> Citylist;
    private ArrayAdapter adapter;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_province);
        setTitle("省市地区");
        showBackwardView("", true);
        list = (List<DataDictInfo>) getIntent().getSerializableExtra("data");
        province = getIntent().getStringExtra("name");

        TextView textView=(TextView) findViewById(R.id.act_province_current);
        textView.setVisibility(View.GONE);
        TextView m_title=(TextView) findViewById(R.id.act_province_title);
        m_title.setVisibility(View.GONE);
        TextView m_name=(TextView) findViewById(R.id.act_province_name);
        m_name.setText(province);
        m_list = (ListViewInScrollView) findViewById(R.id.act_province_list);


        List<String> aList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProvince().equals(province)) {
                Citylist = list.get(i).getList();
                for (CityEntity cn : Citylist) {
                    aList.add(cn.getName());
                }
                break;
            }
        }
        adapter = new ArrayAdapter(this, R.layout.linear_image_text, R.id.linear_image_text_text, aList);
        m_list.setAdapter(adapter);


    }

    @Override
    protected void initListener() {
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CityActivity.this, AreaActivity.class);
                intent.putExtra("id",Citylist.get(position).getId());
                intent.putExtra("province",province);
                intent.putExtra("city",Citylist.get(position).getName());
                intent.putExtra("code",Citylist.get(position).getCode());
                intent.putExtra("source",getIntent().getIntExtra("source",0));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initDate() {

    }
}
