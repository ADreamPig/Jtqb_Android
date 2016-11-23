package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by hasee-pc on 2016/2/24.
 */
public class MyAddressAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddressInfo> datas;
    private LayoutInflater inflater;

    public MyAddressAdapter(Context context, List<MyAddressInfo> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public MyAddressInfo getItem(int position) {
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
            convertView = inflater.inflate(R.layout.layout_item_myaddress, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initData(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvNameAndPhone;
        private TextView tvAddress;

        public void initView(View convertView) {
            tvNameAndPhone = (TextView) convertView.findViewById(R.id.myaddress_tv_nameAndPhone);
            tvAddress = (TextView) convertView.findViewById(R.id.myaddress_tv_address);
        }

        public void initData(MyAddressInfo addressEntity) {
            tvNameAndPhone.setText(addressEntity.getConsignee() + "    " + addressEntity.getMobile());
            tvAddress.setText(addressEntity.getProv() + addressEntity.getCity() + addressEntity.getDistrict() + addressEntity.getAddress());
        }
    }
}
