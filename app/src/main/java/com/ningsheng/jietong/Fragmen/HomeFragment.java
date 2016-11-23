package com.ningsheng.jietong.Fragmen;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ningsheng.jietong.Activity.ActivityActivity;
import com.ningsheng.jietong.Activity.AllBusinessAct;
import com.ningsheng.jietong.Activity.BindCardActivity;
import com.ningsheng.jietong.Activity.BlankActivityAct;
import com.ningsheng.jietong.Activity.BusinessDetailAct;
import com.ningsheng.jietong.Activity.BuyCardFirstActivity;
import com.ningsheng.jietong.Activity.ListViewTestAct;
import com.ningsheng.jietong.Activity.QueryActivity;
import com.ningsheng.jietong.Activity.TicketActivity;
import com.ningsheng.jietong.Activity.TopUpActivity;
import com.ningsheng.jietong.Adapter.ActivityAdapter;
import com.ningsheng.jietong.Adapter.BannerPagerAdapter;
import com.ningsheng.jietong.Adapter.MerchantAdapter;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.Url;
import com.ningsheng.jietong.Entity.Banner;
import com.ningsheng.jietong.Entity.HomeEntity;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.Entity.MerchantSales;
import com.ningsheng.jietong.Pull.MaterialRefreshLayout;
import com.ningsheng.jietong.Pull.MaterialRefreshListener;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.HttpSender;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.Utils.OnHttpResultListener;
import com.ningsheng.jietong.Utils.SharedPreferencesUtil;
import com.ningsheng.jietong.Utils.gsonUtil;
import com.ningsheng.jietong.View.GridViewInScorllView;
import com.ningsheng.jietong.View.ListViewInScrollView;
import com.ningsheng.jietong.View.ReboundScrollView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/1/28.
 */

@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.fragment_home_viewpager)
    private ViewPager viewpager;
    @ViewInject(R.id.fragment_home_gv)
    private GridViewInScorllView m_gv;
    @ViewInject(R.id.fragment_home_lv)
    private ListViewInScrollView m_lv;
    @ViewInject(R.id.fragment_home_viewpager_ll)
    private LinearLayout indicator;
    @ViewInject(R.id.fragment_home_srcollview)
    private ReboundScrollView m_scrollview;
    @ViewInject(R.id.fragment_home_relative)
    private RelativeLayout m_rl;
    @ViewInject(R.id.fragment_home_title)
    private TextView m_title;
    @ViewInject(R.id.fragmen_business_recommend)
    private View m_recommend;
    @ViewInject(R.id.fragmen_business_activity)
    private View m_activity;
    @ViewInject(R.id.fragment_home_dzq)
    private View m_dzq;
    @ViewInject(R.id.fragment_home_fkm)
    private View m_fkm;
    @ViewInject(R.id.fragment_home_gk)
    private View m_gk;
    @ViewInject(R.id.fragment_home_kcz)
    private View m_kcz;
    @ViewInject(R.id.fragment_home_kbd)
    private View m_kbd;
    @ViewInject(R.id.fragment_home_yecx)
    private View m_yecx;
    @ViewInject(R.id.fragment_home_refresh)
    private MaterialRefreshLayout m_refresh;


    private boolean order = true;//左到右
    private List<View> views = new ArrayList<View>();
    private int[] images = {R.mipmap.image1, R.mipmap.image2, R.mipmap.image3, R.mipmap.image4};
    private boolean isDragging;
    private int aplh;

    private ActivityAdapter activity_adapter;
    private MerchantAdapter merchantAdapter;
    private List<Merchant> list1 = new ArrayList<>();
    private List<MerchantSales> list2 = new ArrayList<MerchantSales>();
    private List<Banner> bannerList = new ArrayList<>();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LodingDialog mPd=new LodingDialog(activity);
//        mPd.show();
        m_recommend.setOnClickListener(this);
        m_activity.setOnClickListener(this);
        m_dzq.setOnClickListener(this);
        m_fkm.setOnClickListener(this);
        m_gk.setOnClickListener(this);
        m_kcz.setOnClickListener(this);
        m_kbd.setOnClickListener(this);
        m_yecx.setOnClickListener(this);
        viewpager.getLayoutParams().width = ViewPager.LayoutParams.MATCH_PARENT;
        viewpager.getLayoutParams().height = (int) (AndroidUtil.getScreenSize(getActivity(), 1) * 0.511);
//        m_refresh.setSunStyle(true);
        m_refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
            }

            @Override
            public void onfinish() {
                super.onfinish();
            }
        });

        initData();
        initList();

    }

    private void initList() {
        activity_adapter = new ActivityAdapter(activity, list2);
        m_gv.setAdapter(activity_adapter);

        merchantAdapter = new MerchantAdapter(activity, list1);
        m_lv.setAdapter(merchantAdapter);
        m_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(activity, BusinessDetailAct.class);
                intent.putExtra("id", list1.get(position).getId());
                startActivity(intent);
            }
        });
        m_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ActivityActivity.class);
                intent.putExtra("id", list2.get(position).getId());
                startActivity(intent);
            }
        });
        m_title.getBackground().setAlpha(aplh);
        m_scrollview.setScrollViewListener(new ReboundScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(int x, int y, int oldx, int oldy) {
//
                int h = m_rl.getHeight();
                if (y < h) {
                    Log.d("---------------", "aaaaaaaaaaaaaa");
                    aplh = 255 * y / h;
                    m_title.getBackground().setAlpha(aplh);
                } else {
                    aplh = 255;
                    m_title.getBackground().setAlpha(aplh);
                }
            }
        });
        HomeEntity home_entity = SharedPreferencesUtil.getInstance(activity).getBeanfromSharedPreferences("HOME_DATA", HomeEntity.class);
//        List<Merchant> merchantList=home_entity.getMerchant();
//        List<MerchantSales> salesList=home_entity.getMerchantSales();
        if (home_entity != null) {
            bannerList = home_entity.getBanner();

            list1.addAll(home_entity.getMerchant());
            list2.addAll(home_entity.getMerchantSales());

            activity_adapter.notifyDataSetChanged();
            merchantAdapter.notifyDataSetChanged();
            initViewPager();
        }

    }

    private void initViewPager() {
        indicator.removeAllViews();
        views.clear();
        for (int i = 0; i < bannerList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.width = ViewPager.LayoutParams.MATCH_PARENT;
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            MediaUtil.displayImage(activity, bannerList.get(i).getImage(), imageView);
//            ImageLoaderUtil.getInstance().setImagebyurl(bannerList.get(i).getImage(), imageView);
//            imageView.setImageResource(images[i]);
            views.add(imageView);

            ImageView imageView1 = new ImageView(getContext());
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(-2, -2);
            imageView1.setLayoutParams(params1);
            imageView1.setPadding(3, 3, 3, 3);
            imageView1.setImageResource(R.mipmap.icon_indicator_on);
            indicator.addView(imageView1);
        }
        initAdapter();

    }

    BannerPagerAdapter adapter;

    private void initAdapter() {
        if (adapter == null) {
            adapter = new BannerPagerAdapter(views);
            viewpager.setAdapter(adapter);
            selectIndicator(0);
            viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    selectIndicator(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 用户拖拽
                            isDragging = true;
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 空闲状态
                            isDragging = false;
                            break;
                        case ViewPager.SCROLL_STATE_SETTLING:
                            // 被释放时
                            isDragging = false;
                            break;

                        default:
                            break;
                    }
                }
            });

            viewpager.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // 若用户没有拖拽，则自动滚动
                    if (!isDragging) {
                        //
                        if (order) {
                            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                            if (viewpager.getCurrentItem() == views.size() - 1) {
                                order = false;
                            }
                        } else {
                            viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
                            if (viewpager.getCurrentItem() == 0) {
                                order = true;
                            }
                        }
                    }
                    viewpager.postDelayed(this, 3000);
                }
            }, 3000);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    private void selectIndicator(int position) {
        for (int i = 0; i < views.size(); i++) {
            ImageView count = (ImageView) indicator.getChildAt(i);
            if (i == position) {
                count.setImageResource(R.mipmap.icon_indicator_on);
            } else {
                count.setImageResource(R.mipmap.icon_indicator);
            }
        }
    }

    private void initData() {

        Map<String, String> map = new HashMap<String, String>();
//        map.put("lng", "121.571028");
//        map.put("lat", "31.189672");
//        map.put("area", "1234");//选填
//        map.put("industryId", "123456");//选填
        HttpSender sender = new HttpSender(Url.indext, "首页", map, new OnHttpResultListener() {

            @Override
            public void onSuccess(String message, String code, String data) {
                list2.clear();
                list1.clear();
                HomeEntity homeentity = gsonUtil.getInstance().json2Bean(data, HomeEntity.class);
                SharedPreferencesUtil.getInstance(activity).setBeantoSharedPreferences("HOME_DATA", homeentity);
                List<Merchant> merchantList = homeentity.getMerchant();
                List<MerchantSales> salesList = homeentity.getMerchantSales();
                bannerList = homeentity.getBanner();

                list1.addAll(merchantList);
                list2.addAll(salesList);

                activity_adapter.notifyDataSetChanged();
                merchantAdapter.notifyDataSetChanged();
                initViewPager();
            }

            @Override
            public void onFinished() {
                super.onFinished();
                m_refresh.finishRefresh();
            }
        });
        sender.setContext(activity);
        sender.send(Url.Post);
    }


    private Notification notification;
    private NotificationManager nManager;
    private Intent intent;
    private PendingIntent pIntent;

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.fragmen_business_activity:
//                intent = new Intent(activity, AllActivityAct.class);
                intent = new Intent(activity, BlankActivityAct.class);
                break;
            case R.id.fragmen_business_recommend:
                intent = new Intent(activity, AllBusinessAct.class);
                break;
            case R.id.fragment_home_dzq:
                intent = new Intent(activity, TicketActivity.class);
                break;
            case R.id.fragment_home_fkm:
//                intent = new Intent(activity, FinanceActivity.class);
//                intent = new Intent(activity, QRCodeActivity.class);
                intent = new Intent(activity, ListViewTestAct.class);


                break;
            case R.id.fragment_home_gk:
                intent = new Intent(activity, BuyCardFirstActivity.class);
                break;
            case R.id.fragment_home_kcz:
                intent = new Intent(activity, BindCardActivity.class);
                break;
            case R.id.fragment_home_kbd:
                intent = new Intent(activity, TopUpActivity.class);
                break;
            case R.id.fragment_home_yecx:
                intent = new Intent(activity, QueryActivity.class);
                break;
//            case R.id.
        }
        startActivity(intent);
    }

//    private void da(){
//        // 单击通知后会跳转到NotificationResult类
//        intent = new Intent(activity,
//                LoginActivity.class);
//        // 获取PendingIntent,点击时发送该Intent
//        pIntent = PendingIntent.getActivity(activity, 0,
//                intent, 0);
//        // 设置通知的标题和内容
//        notification.setLatestEventInfo(activity, "标题",
//                "内容", pIntent);
//        // 发出通知
//        nManager.notify(ID, notification);
//    }
}
