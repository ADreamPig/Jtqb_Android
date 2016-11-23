package com.ningsheng.jietong.Fragmen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.platform.comapi.map.E;
import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Activity.BusinessDetailAct;
import com.ningsheng.jietong.Activity.SearchActivity;
import com.ningsheng.jietong.Adapter.BusinessAdapter;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.Entity.SearchMerchant;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/2/24.
 */
@ContentView(R.layout.fragment_search_after)
public class SearchAfterFragment extends BaseFragment {
    @ViewInject(R.id.fragment_search_after_list)
    private ListView m_list;
    private BusinessAdapter adapter;
    private List<Merchant> list = new ArrayList<Merchant>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BusinessAdapter(activity, list);

        m_list.setAdapter(adapter);
        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> list2 = SharedPreferencesUtil.getInstance(activity).getBeanfromSharedPreferences(SharedPreferencesUtil.SEARCH, List.class);
                if (list2 == null) {
                    list2 = new ArrayList<>();
                    list2.add(0, key);
                } else {
                    list2.add(0, key);
                }
                SharedPreferencesUtil.getInstance(activity).setBeantoSharedPreferences(SharedPreferencesUtil.SEARCH, list2);
                Intent intent = new Intent(activity, BusinessDetailAct.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }


    private int page;
    private String key;

    public void clean(String key){
        list.clear();
        page=0;
        getData(key);
    }
    public void getData(String key) {
        this.key = key;

        Map<String, String> map = new HashMap<String, String>();
        map.put("keyword", key);
        map.put("limitStart", page + "");
        map.put("limitEnd", "200");
        map.put("city", SearchActivity.city);
        HttpSender sender = new HttpSender(Url.serach, "搜索", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if (code.equals("0000")) {
                    page++;
                    try {
                        JSONObject jo = new JSONObject(data);
                        List<Merchant> mList=(List<Merchant>)gsonUtil.getInstance().json2List(jo.getString("data"),new TypeToken<List<Merchant>>(){}.getType());
//                    SearchMerchant temp=gsonUtil.getInstance().json2List(jo.getString("data"),SearchMerchant.class);
//                        List<Merchant> mList = temp.getMerchant();
//                                (List<Merchant>) gsonUtil.getInstance().json2List(temp, new TypeToken<Merchant>() {
//                        }.getType());
                        list.addAll(mList);
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    activity.toast(message);
                }
            }
        });
        sender.send(Url.Post);

    }
}
