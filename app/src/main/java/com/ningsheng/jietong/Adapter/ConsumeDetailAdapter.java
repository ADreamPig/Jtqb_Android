package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.ConsumeDetailInfo;
import com.ningsheng.jietong.Entity.FlowingDetailData;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.DateUtils;
import com.ningsheng.jietong.View.PinnedHeaderListView;

import java.util.List;

/**
 * Created by hasee-pc on 2016/3/1.
 */
public class ConsumeDetailAdapter extends BaseAdapter implements AbsListView.OnScrollListener, PinnedHeaderListView.PinnedHeaderAdapter {
    private List<FlowingDetailData> datas;
    private Context context;
    private LayoutInflater inflater;
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ITEM_BOTTOM = 2;

    public ConsumeDetailAdapter(List<FlowingDetailData> datas, Context context) {
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public FlowingDetailData getItem(int position) {
        if (position >= datas.size())
            return null;
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_TITLE;
        if (position >= getCount() - 1)
            return TYPE_ITEM_BOTTOM;
        FlowingDetailData info1 = getItem(position - 1);
        FlowingDetailData info2 = getItem(position);
        FlowingDetailData info3 = getItem(position + 1);
        if (!info2.getTitle().equals(info3.getTitle()))
            return TYPE_ITEM_BOTTOM;
        if (info1.getTitle().equals(info2.getTitle()))
            return TYPE_ITEM;
        return TYPE_TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConsumeDetailHolder2 holder3 = null;
        ConsumeDetailHolder2 holder2 = null;
        ConsumeDetailHolder1 holder1 = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    convertView = inflater.inflate(R.layout.layout_item_consumedetail_1, null);
                    holder1 = new ConsumeDetailHolder1();
                    holder1.m_title = (TextView) convertView.findViewById(R.id.adapter_consumedetail_holder1_name);
//                    String s=datas.get(position).getTitle();
//                    holder1.m_title.setText(datas.get(position).getTitle());
                    convertView.setTag(holder1);
                    break;
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.layout_item_consumedetail_2, null);
                    holder2 = new ConsumeDetailHolder2();
                    holder2.m_name = (TextView) convertView.findViewById(R.id.consumeDetail_tv_title);
                    holder2.m_time = (TextView) convertView.findViewById(R.id.consumeDetail_tv_time);
                    holder2.m_money = (TextView) convertView.findViewById(R.id.consumeDetail_tv_money);
                    convertView.setTag(holder2);
                    break;
                case TYPE_ITEM_BOTTOM:
                    convertView = inflater.inflate(R.layout.layout_item_consumedetail_2_bottom, null);
                    holder3 = new ConsumeDetailHolder2();
                    holder3.m_name = (TextView) convertView.findViewById(R.id.consumeDetail_tv_title);
                    holder3.m_time = (TextView) convertView.findViewById(R.id.consumeDetail_tv_time);
                    holder3.m_money = (TextView) convertView.findViewById(R.id.consumeDetail_tv_money);
                    convertView.setTag(holder3);
                    break;
            }

        } else {
            switch (getItemViewType(position)) {
                case TYPE_TITLE:
                    holder1 = (ConsumeDetailHolder1) convertView.getTag();
                    break;
                case TYPE_ITEM:
                    holder2 = (ConsumeDetailHolder2) convertView.getTag();
                    break;
                case TYPE_ITEM_BOTTOM:
                    holder3 = (ConsumeDetailHolder2) convertView.getTag();
                    break;
            }

        }

        switch (getItemViewType(position)) {
            case TYPE_TITLE:
                String s = datas.get(position).getTitle();
                int a = holder1.m_title.getId();
                holder1.m_title.setText(datas.get(position).getTitle().substring(0,4)+"年"+datas.get(position).getTitle().substring(4,6)+"月");
                break;
            case TYPE_ITEM:
               String date= DateUtils.dateToString("yyyy-MM-dd HH:mm:ss",DateUtils.stringToDate("yyyyMMddHHmmss",datas.get(position).getTransDate()  + datas.get(position).getTransTime()));
                holder2.m_name.setText(datas.get(position).getUnitName1());
//                holder2.m_time.setText(datas.get(position).getTransDate() + "  " + datas.get(position).getTransTime());
                holder2.m_time.setText(date);
                holder2.m_money.setText("￥"+datas.get(position).getAmount());
                break;
            case TYPE_ITEM_BOTTOM:
                String date1= DateUtils.dateToString("yyyy-MM-dd HH:mm:ss",DateUtils.stringToDate("yyyyMMddHHmmss",datas.get(position).getTransDate()  + datas.get(position).getTransTime()));
                holder3.m_name.setText(datas.get(position).getUnitName1());
                holder3.m_time.setText(date1);
                holder3.m_money.setText("￥"+datas.get(position).getAmount());
                break;
        }
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof PinnedHeaderListView) {
            ((PinnedHeaderListView) view).controlPinnedHeader(firstVisibleItem);
        }

    }

    @Override
    public int getPinnedHeaderState(int position) {
        if (getCount() == 0 || position < 0) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_GONE;
        }

        if (isMove(position)) {
            return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_PUSHED_UP;
        }

        return PinnedHeaderListView.PinnedHeaderAdapter.PINNED_HEADER_VISIBLE;

    }

    @Override
    public void configurePinnedHeader(View headerView, int position, int alpaha) {

    }

    private boolean isMove(int position) {
        // 获取当前与下一项
        FlowingDetailData currentEntity = getItem(position);
        FlowingDetailData nextEntity = getItem(position + 1);
        System.out.println("position: " + position);
        System.out.println("currentEntity: " + currentEntity.toString());
        System.out.println("nextEntity: " + nextEntity.toString());
        if (null == currentEntity || null == nextEntity) {
            return false;
        }

        // 获取两项header内容
        String currentTitle = currentEntity.getTitle();
        String nextTitle = nextEntity.getTitle();
        if (null == currentTitle || null == nextTitle) {
            return false;
        }

        // 当前不等于下一项header，当前项需要移动了
        if (!currentTitle.equals(nextTitle)) {
            return true;
        }
        return false;
    }

    public class ConsumeDetailHolder1 {
        TextView m_title;

    }

    public class ConsumeDetailHolder2 {
        TextView m_name, m_time, m_money;


    }
}
