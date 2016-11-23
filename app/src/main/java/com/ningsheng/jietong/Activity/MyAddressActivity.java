package com.ningsheng.jietong.Activity;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Adapter.MyAddressAdapter;
import com.ningsheng.jietong.App.BaseMeActivity;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.MainActivity;
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
public class MyAddressActivity extends BaseMeActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private MyAddressAdapter adapter;
    private List<MyAddressInfo> datas;
    private int limitStart;
    private int limitEnd = 150;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_myaddress);
        setTitle("常用地址");
        showBackwardView("",true);

        TextView content = (TextView) findViewById(R.id.nodata_content);
        content.setText("你还没有添加常用地址，添加后可在这里查看。");
        TextView next = (TextView) findViewById(R.id.nodata_next);
        next.setText("新增地址");
        listView = (ListView) findViewById(R.id.listView);
        datas = new ArrayList<>();

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setBackgroundColor(getResources().getColor(R.color.background));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, AndroidUtil.dip2px(this, 40));
        params.topMargin = AndroidUtil.dip2px(this, 30);
        params.leftMargin = AndroidUtil.dip2px(this, 20);
        params.rightMargin = AndroidUtil.dip2px(this, 20);
        params.bottomMargin = AndroidUtil.dip2px(this, 20);
        TextView tv = new TextView(this);
        tv.setTextColor(getResources().getColor(R.color.color_while));
        tv.setTextSize(16);
        tv.setText("新增地址");
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.selector_button_next);
        linearLayout.addView(tv, params);
        listView.addFooterView(linearLayout);
        adapter = new MyAddressAdapter(this, datas);
        listView.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initDate() {
        datas.clear();
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountId", MyApplication.user.getId());
        map.put("limitStart", String.valueOf(limitStart));
        map.put("limitEnd", String.valueOf(limitEnd));
        HttpSender sender = new HttpSender(Url.shippingaddressListInfo, "获取收货地址", map, new OnHttpResultListener() {
            @Override
            public void onSuccess(String message, String code, String data) {
//                limitStart += 1;
                if(data!=null&&!data.equals("null")){
                List<MyAddressInfo> MList = (List<MyAddressInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<MyAddressInfo>>() {
                }.getType());
//                if (MList != null && !MList.isEmpty()) {

                    datas.addAll(MList);

//                    listView.setVisibility(View.VISIBLE);
//                    findViewById(R.id.nodata).setVisibility(View.GONE);
//                }
            }
                adapter.notifyDataSetChanged();
                if(datas.size()==0){
                    listView.setVisibility(View.GONE);
                    findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.nodata).setVisibility(View.GONE);
                }
            }
        });
        sender.setContext(this);
        sender.send(Url.Post);


    }

    public void nodataClick(View v) {
        Intent intent = new Intent(this, AddressDetailsActivity.class);
        intent.putExtra("type", AddressDetailsActivity.TYPE_ADD);
        startActivityForResult(intent, 0x100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AddressDetailsActivity.class);
        Log.i("-------position-------",position+"======"+datas.size());
        if (position == datas.size()) {
            intent.putExtra("type", AddressDetailsActivity.TYPE_ADD);
//            startActivityForResult(intent, 0x100);
            startActivity(intent);
        } else {
            intent.putExtra("type", AddressDetailsActivity.TYPE_DETAIL);
            intent.putExtra("address", datas.get(position));
            intent.putExtra("position", position);
//            startActivityForResult(intent, 0x101);
            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11 && resultCode == 12) {//登陆成功后
            initDate();
        }
        if (requestCode == 11 && resultCode == 13) {//点返回键后的返回
            if (!MainActivity.class.isInstance(this)) {
                finish();
            }

        }
        if(resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode){
            case 0x100:
                initDate();
                break;
            case 0x101:
                initDate();
                break;
        }
//super.onActivityResult(requestCode,resultCode,data);

//        if(data==null){
//            toast("空");
//            return;
//        }
//        switch (requestCode) {
//            case 0x100://添加收货地址
//                if (resultCode != RESULT_OK)
//                    return;
////                MyAddressInfo info = (MyAddressInfo) data.getSerializableExtra("address");
////                if (info != null) {
////                    if (datas.size() > 0) {
////                        datas.add(0, info);
////                    } else {
////                        datas.add(0, info);
////                        findViewById(R.id.nodata).setVisibility(View.GONE);
////                        listView.setVisibility(View.VISIBLE);
////                    }
////                }
////                adapter.notifyDataSetChanged();
//                initDate();
//                break;
//            case 0x101:
////                if(resultCode==0x102){
//                Log.i("------地址前------","------地址前------");
//                if (data == null)
//                    return;
//                Log.i("------地址hou------","------地址hou------");
//                initDate();
////                }
////                int position = data.getIntExtra("position", -1);
////                switch (resultCode) {
////                    case 0x100://修改和添加地址
////                        MyAddressInfo addressInfo = (MyAddressInfo) data.getSerializableExtra("address");
////                        datas.remove(position);
////                        datas.add(position, addressInfo);
////                        adapter.notifyDataSetChanged();
////                        break;
////                    case 0x101://删除地址
////                        datas.remove(position);
////                        if (datas.size() > 0) {
////                            adapter.notifyDataSetChanged();
////                        } else {
////                            findViewById(R.id.nodata).setVisibility(View.VISIBLE);
////                            listView.setVisibility(View.GONE);
////                        }
////                        break;
////                }
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
