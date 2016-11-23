package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.InventedCertificateInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by hasee-pc on 2016/2/23.
 */
public class InventedCertificateAdapter extends BaseAdapter {
    private Context context;
    private List<InventedCertificateInfo> datas;
    private LayoutInflater inflater;

    public InventedCertificateAdapter(Context context, List<InventedCertificateInfo> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public InventedCertificateInfo getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_item_invented_certificate, null);
            convertView.setTag(holder);
            holder.initView(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initData(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvMobile;
        private TextView tvMoney;
        private TextView tvTimeResult;

        private void initView(View convertView) {
            tvMobile = (TextView) convertView.findViewById(R.id.inventedCentificate_tv_mobile);
            tvMoney = (TextView) convertView.findViewById(R.id.inventedCentificate_tv_money);
            tvTimeResult = (TextView) convertView.findViewById(R.id.inventedCentificate_tv_time_result);
        }

        private void initData(InventedCertificateInfo info) {
            tvMobile.setText("支付手机号：" + info.getMobile());
            tvMoney.setText("金额：" + info.getMoney() + "元");
            tvTimeResult.setText("发放时间：" + info.getBuyDate() + "       发放结果：" + ("0000".equals(info.getReturnCode()) ? "成功" : "失败"));
        }

    }
}
