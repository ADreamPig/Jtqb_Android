package com.ningsheng.jietong.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ningsheng.jietong.Adapter.RechargePhoneAdapter;
import com.ningsheng.jietong.App.BaseFragment;
import com.ningsheng.jietong.App.TitleActivity;
import com.ningsheng.jietong.Fragmen.KeepCallFragment;
import com.ningsheng.jietong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhushunqing on 2016/5/5.
 */
public class RechargePhoneActivity extends TitleActivity {
    private TabLayout m_tab;
    private ViewPager viewPager;
    private List<BaseFragment> list_fragment;
    String[] tabs = {"话费充值", "流量充值"};

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.act_recharge_phone);
        setTitle("手机充值");
        m_tab = (TabLayout) findViewById(R.id.act_recharge_phone_tab);
        viewPager = (ViewPager) findViewById(R.id.act_recharge_phone_viewpager);
        list_fragment = new ArrayList<>();
        KeepCallFragment fragment1 = new KeepCallFragment();
        KeepCallFragment fragment2 = new KeepCallFragment();
        list_fragment.add(fragment1);
        list_fragment.add(fragment2);
        RechargePhoneAdapter adapter = new RechargePhoneAdapter(getSupportFragmentManager(),list_fragment,tabs);
        viewPager.setAdapter(adapter);
        m_tab.setupWithViewPager(viewPager);
        m_tab.setTabMode(TabLayout.MODE_FIXED);


//        m_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//         TabLayout.TabLayoutOnPageChangeListener listener =
//                new TabLayout.TabLayoutOnPageChangeListener(m_tab);
//        viewPager.addOnPageChangeListener(listener);
//        viewPager.setAdapter(adapter);
    }

    @Override
    protected void initListener() {


    }

    @Override
    protected void initDate() {

    }


}
