package com.ningsheng.jietong.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.BalanceAdapter;
import com.ningsheng.jietong.Adapter.MyCardAdapter;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MyCardInfo;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hasee-pc on 2016/2/24.
 */
public class BalanceActivity extends TitleActivity {
    private ListView listView;
    private BalanceAdapter adapter;
    private List<MyCardInfo> datas;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_balance);
        setTitle("余额");
        showBackwardView("", true);
        String balance=(String)getIntent().getStringExtra("balance");



        listView = (ListView) findViewById(R.id.listView);
        View headView = View.inflate(this, R.layout.layout_content_balance, null);
        listView.addHeaderView(headView);
        TextView m_balance=(TextView)headView. findViewById(R.id.act_balance_balance);
        m_balance.setText("￥"+balance);
        datas = new ArrayList<>();
        adapter = new BalanceAdapter(this, datas);
        listView.setAdapter(adapter);
    }



    @Override
    protected void initListener() {

    }


    @Override
    protected void initDate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("id", MyApplication.user.getId());
        HttpSender sender = new HttpSender(Url.myCardList, "我的卡", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                List<MyCardInfo> myCardInfos = (List<MyCardInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<MyCardInfo>>() {
                }.getType());
                if (myCardInfos != null && !myCardInfos.isEmpty()) {

                    datas.clear();
                    datas.addAll(myCardInfos);
                    adapter.notifyDataSetChanged();

                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);

    }
}
