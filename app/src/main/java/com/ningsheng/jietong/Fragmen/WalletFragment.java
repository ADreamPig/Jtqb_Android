package com.ningsheng.jietong.Fragmen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.ningsheng.jietong.Activity.BindCardActivity;
import com.ningsheng.jietong.Activity.BuyCardFirstActivity;
import com.ningsheng.jietong.Activity.CardDetailsActivity;
import com.ningsheng.jietong.Activity.LifePayActivity;
import com.ningsheng.jietong.Activity.LoginActivity;
import com.ningsheng.jietong.Activity.MyCardActivity;
import com.ningsheng.jietong.Activity.QueryActivity;
import com.ningsheng.jietong.Activity.RechargePhoneActivity;
import com.ningsheng.jietong.Activity.RecordACtivity;
import com.ningsheng.jietong.Activity.ReportActivity;
import com.ningsheng.jietong.Activity.TopUpActivity;
import com.ningsheng.jietong.Adapter.MyCardAdapter;
import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.MyApplication;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Dialog.PayPasswordDialog;
import com.ningsheng.jietong.Entity.MyCardInfo;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.ListViewInScrollView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/1/29.
 */
@ContentView(R.layout.fragment_wallet)
public class WalletFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PayPasswordDialog dialog;

    private View linearTitle;
    private MaterialRefreshLayout m_pull;
    private ListViewInScrollView listView;
    private MyCardAdapter adapter;
    private List<MyCardInfo> datas;
    private List<MyCardInfo> datas_;
    private ValueAnimator valueAnimatorOpen;
    private ValueAnimator valueAnimatorClose;
    private boolean isOpenMore;
    private View m_CardMain, m_NoData;

    private TextView ll_bg;
    private View m_hasCard, m_noCard;

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ll_bg = (TextView) view.findViewById(R.id.fragment_business_title);
        ll_bg.setBackgroundColor(getResources().getColor(R.color.app_color));
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) ll_bg.getLayoutParams();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            params.topMargin=MyApplication.getStateBarHeight();
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            params.topMargin=MyApplication.getStateBarHeight();
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1){
            params.topMargin=0;
        }

        linearTitle = view.findViewById(R.id.mycard_linear_title);
//        m_CardMain= view.findViewById(R.id.mycard_main);
//        m_noCard=view.findViewById(R.id.nodata);
        view.findViewById(R.id.fragment_wallet_gk).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_kbd).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_kcz).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_yecx).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_jyjl).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_gs).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_sjcz).setOnClickListener(this);
        view.findViewById(R.id.fragment_wallet_shjf).setOnClickListener(this);
        m_pull = (MaterialRefreshLayout) view.findViewById(R.id.fragment_wallet_pull);
        view.findViewById(R.id.act_mycard__rv_add).setVisibility(View.GONE);

        listView = (ListViewInScrollView) view.findViewById(R.id.listView);
        datas = new ArrayList<>();
        datas_ = new ArrayList<>();
        adapter = new MyCardAdapter(activity, datas);
        listView.setAdapter(adapter);
        AndroidUtil.setListViewHeightBasedOnChildren(listView);

        TextView content = (TextView) view.findViewById(R.id.nodata_content);
        content.setText("你还没有绑定任何卡，绑定后可在这里查看。");
        TextView next = (TextView) view.findViewById(R.id.nodata_next);
        next.setText("绑定新卡");
        next.setOnClickListener(this);
        view.findViewById(R.id.mycard_tv_more).setOnClickListener(this);
        listView.setOnItemClickListener(this);
        m_hasCard = view.findViewById(R.id.mycard_main);//.setVisibility(View.VISIBLE);
        m_noCard = view.findViewById(R.id.nodata);//.setVisibility(View.GONE);
        view.findViewById(R.id.mycard_tv_add).setOnClickListener(this);
        m_pull.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
//                datas.clear();
                    if(MyApplication.user.getId()!=null&&!MyApplication.user.getId().equals("")){
                        initData(activity);
                    }else{
                        datas.clear();
                        adapter.notifyDataSetChanged();
                        m_hasCard.setVisibility(View.GONE);
                        m_noCard.setVisibility(View.VISIBLE);
                        m_pull.finishRefresh();
                    }
//                initData(activity);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
            }
        });
        initData(activity);


    }



    private HttpSender sender;




    public void initData(BaseActivity act) {
        if (activity != null) {

            if (MyApplication.user.getId() == null || MyApplication.user.getId().equals("")) {
                 Intent intent=new Intent(activity, LoginActivity.class);
                startActivityForResult(intent,11);
            } else {
                Map<String, String> map = new HashMap<String, String>();
                map.put("accountId", MyApplication.user.getId());
                map.put("id", MyApplication.user.getId());
                HttpSender sender = new HttpSender(Url.myCardList, "我的卡", map, new OnHttpResultListener() {
                    @Override
                    public void onSuccess(String message, String code, String data) {
                        m_pull.finishRefresh();
                        List<MyCardInfo> myCardInfos = (List<MyCardInfo>) gsonUtil.getInstance().json2List(data, new TypeToken<ArrayList<MyCardInfo>>() {
                        }.getType());
                        if (myCardInfos != null && !myCardInfos.isEmpty()) {
                            m_hasCard.setVisibility(View.VISIBLE);
                            m_noCard.setVisibility(View.GONE);
                            datas.clear();
                            if (myCardInfos.size() > 2) {
                                datas.add(myCardInfos.get(0));
                                datas.add(myCardInfos.get(1));
                            } else {
                                datas.addAll(myCardInfos);
                            }
                            Log.i("----data------", datas.toString() + "");
                            adapter.notifyDataSetChanged();
                        } else {
                            m_hasCard.setVisibility(View.GONE);
                            m_noCard.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFinished() {
                        super.onFinished();
                        m_pull.finishRefresh();
                    }
                });
//        }
                sender.setCacheMaxAge(3000l);
                sender.setContext(act);
                sender.send(Url.Post);
            }
        }
    }

    public void refresh() {
//        datas.clear();
        if(m_hasCard!=null) {
            if(MyApplication.user.getId()!=null&&!MyApplication.user.getId().equals("")){
                initData(activity);
            }else{
                datas.clear();
                adapter.notifyDataSetChanged();
                m_hasCard.setVisibility(View.GONE);
                m_noCard.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.mycard_tv_more:
//                isOpenMore = true;
//                linearTitle.setVisibility(View.GONE);
//                valueAnimatorOpen.start();
//                datas.clear();
//                datas.addAll(datas_);
//                adapter.notifyDataSetChanged();
                intent = new Intent(activity, MyCardActivity.class);
                intent.putExtra("type", "wall");
                startActivity(intent);
                break;
            case R.id.fragment_wallet_gk:
                intent = new Intent(activity, BuyCardFirstActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_wallet_kbd:
                intent = new Intent(activity, BindCardActivity.class);
                startActivity(intent);

                break;
            case R.id.fragment_wallet_kcz:
                intent = new Intent(activity, TopUpActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_wallet_yecx:
                intent = new Intent(activity, QueryActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_wallet_jyjl:
                intent = new Intent(activity, RecordACtivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_wallet_gs:
                intent = new Intent(activity, ReportActivity.class);
                startActivity(intent);
                break;
            case R.id.nodata_next:
                intent = new Intent(activity, BindCardActivity.class);
                startActivityForResult(intent, 0x41);
                return;
            case R.id.mycard_tv_add:
                intent = new Intent(activity, BindCardActivity.class);
                startActivityForResult(intent, 0x41);
                break;
            case R.id.fragment_wallet_sjcz:
                intent = new Intent(activity, RechargePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_wallet_shjf:
                intent = new Intent(activity, LifePayActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void nodataClick(View v) {
        Intent intent = new Intent(activity, BindCardActivity.class);
        startActivityForResult(intent, 0x41);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x41&&resultCode==0x41) {
            refresh();
//            datas.clear();
//            adapter.notifyDataSetChanged();
        }else if(requestCode==11&&resultCode==12){
            refresh();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(activity, CardDetailsActivity.class);
        intent.putExtra("cardInfo", datas.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.getTransitionName());
            ActivityCompat.startActivityForResult(activity, intent, 0x101, optionsCompat.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() >> 1, view.getHeight() >> 1, 0, 0);
            ActivityCompat.startActivityForResult(activity, intent, 0x101, optionsCompat.toBundle());
        }
    }

}
