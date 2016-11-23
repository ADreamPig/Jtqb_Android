package com.ningsheng.jietong.Activity;

import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/28.
 */
public class ListViewTestAct extends TitleActivity {
    @Override
    protected void initListener() {
        setContentView(R.layout.act_listview_test);

        MaterialRefreshLayout mPull = (MaterialRefreshLayout) findViewById(R.id.act_listview_pull);
        ListView list = (ListView) findViewById(R.id.act_listview_test);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        textView.setPadding(10, 10, 10, 10);
        textView.setText("q423432");
        textView.setBackgroundColor(getResources().getColor(R.color.app_color));
        list.addHeaderView(textView);
        List<String> plist = new ArrayList<>();
        plist.add("");
        plist.add("");
        plist.add("");
        plist.add("");
        plist.add("");
        plist.add("");

//        list.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_business, plist));

    }

    @Override
    protected void initDate() {

    }
}
