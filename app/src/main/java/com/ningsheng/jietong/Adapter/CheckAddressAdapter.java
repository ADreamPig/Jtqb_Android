package com.ningsheng.jietong.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/3/23.
 */
public class CheckAddressAdapter extends Adapter<MyAddressInfo> {
    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public CheckAddressAdapter(BaseActivity activity, List<MyAddressInfo> list) {
        super(activity, list, R.layout.adapter_change_address);
    }

    @Override
    public void getview(ViewHolder vh, int position, MyAddressInfo T) {
        TextView m_nameAndPhone = (TextView) vh.getView(R.id.adapter_change_nameAndPhone);
        TextView m_address = (TextView) vh.getView(R.id.adapter_change_address);
        ImageView m_check = (ImageView) vh.getView(R.id.adapter_change_check);
        m_nameAndPhone.setText(T.getConsignee() + "   " + T.getMobile());
        m_address.setText(T.getProv()+T.getCity()+T.getDistrict() + T.getAddress());
        if (position == this.position) {
            m_check.setVisibility(View.VISIBLE);
        } else {
            m_check.setVisibility(View.GONE);
        }
    }
}

