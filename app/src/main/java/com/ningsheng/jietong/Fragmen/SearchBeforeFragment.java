package com.ningsheng.jietong.Fragmen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Activity.SearchActivity;
import com.ningsheng.jietong.Adapter.HotSearchAdapter;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.HotKey;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.GridViewInScorllView;
import com.ningsheng.jietong.View.ListViewInScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushunqing on 2016/2/24.
 */
@ContentView(R.layout.fragment_search_before)
public class SearchBeforeFragment extends BaseFragment {
    @ViewInject(R.id.fragment_search_before_gv)
    private GridViewInScorllView m_gv;
    @ViewInject(R.id.fragment_search_before_lv)
    private ListViewInScrollView m_lv;
    private View view;

    private  HotSearchAdapter adapter1;
    private ArrayAdapter adapter;
    private  List<HotKey> list1=new ArrayList<HotKey>();
    private List<String> list;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         list= SharedPreferencesUtil.getInstance(activity).getBeanfromSharedPreferences(SharedPreferencesUtil.SEARCH,List.class);
        m_gv=(GridViewInScorllView)view.findViewById(R.id.fragment_search_before_gv);
        list1.clear();
         adapter1=new HotSearchAdapter(activity,list1);
        if(list!=null){
         adapter=new ArrayAdapter(activity,R.layout.app_text,R.id.app_text_text,list);
            m_lv.setAdapter(adapter);
        }
        m_gv.setAdapter(adapter1);
        view.findViewById(R.id.fragment_search_before_clen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
                dialog.setCancelable(true);
                dialog.setMessage("确定要清空搜索历史？");
                dialog.setPositiveButton("确定",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferencesUtil.getInstance(activity).setBeantoSharedPreferences(SharedPreferencesUtil.SEARCH, null);
                        list.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("" +
                        "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
//                dialog.(WINDOWS_NO_TITLE);

            }
        });
        m_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                (SearchActivity)activity.;
                SearchActivity a=(SearchActivity)activity;
                a.setText(list1.get(position).getKeyword());
            }
        });
        initData();

    }

    private void initData(){

        HttpSender sender=new HttpSender(Url.HotKey, "热门搜索", null, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
                if(code.equals("0000")){
                    List<HotKey> mlist=(List<HotKey>)gsonUtil.getInstance().json2List(data,new TypeToken<List<HotKey>>(){}.getType());
                    list1.addAll(mlist);
                    adapter1.notifyDataSetChanged();
                }
            }
        });
        sender.setContext(activity);
        sender.send(Url.Post);
    }
}
