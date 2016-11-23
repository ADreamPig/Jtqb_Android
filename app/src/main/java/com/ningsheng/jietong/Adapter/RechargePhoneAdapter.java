package com.ningsheng.jietong.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ningsheng.jietong.App.BaseFragment;

import java.util.List;

/**
 * Created by zhushunqing on 2016/5/5.
 */
//public class RechargePhoneAdapter {
public class RechargePhoneAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> list_fragment;

    private String[] tabs;

    public RechargePhoneAdapter(FragmentManager fm, List<BaseFragment> list_fragment, String[] tabs) {
        super(fm);
        this.tabs = tabs;
        this.list_fragment = list_fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
//}
