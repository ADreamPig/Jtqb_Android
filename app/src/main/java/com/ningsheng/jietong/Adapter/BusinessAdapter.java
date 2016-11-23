package com.ningsheng.jietong.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.MediaUtil;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/18.
 */
public class BusinessAdapter extends Adapter<Merchant> {

    public BusinessAdapter(BaseActivity activity, List<Merchant> list) {
        super(activity, list, R.layout.adapter_business);
    }

    @Override
    public void getview(ViewHolder vh, int position, Merchant T) {
        ImageView m_image = vh.getView(R.id.adapter_business_image);
        TextView m_name = vh.getView(R.id.adapter_business_name);
        TextView m_discount = vh.getView(R.id.adapter_business_discount);
        TextView m_type = vh.getView(R.id.adapter_business_type);
        TextView m_distance = vh.getView(R.id.adapter_business_distance);

        MediaUtil.displayImage(mactivity, T.getImageApp(), m_image);
        m_name.setText(T.getMerchantName());
        if(T.getDiscount()!=10){
            m_discount.setText(T.getDiscount()+"折");
            m_discount.setVisibility(View.VISIBLE);
        }else{
            m_discount.setVisibility(View.GONE);
        }

//        m_discount.setText(T.getDiscount() + "折");
//        m_distance.setText(T.getDistance());

        if (T.getDistance() == null || T.getDistance().equals("")) {
            m_distance.setVisibility(View.GONE);
            m_distance.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else {
            m_distance.setVisibility(View.VISIBLE);
//            long s=Long.parseLong(T.getDistance());
//            int a=(int)(s/10);
            m_distance.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_juli, 0, 0, 0);
            double d = Double.parseDouble(T.getDistance());
            int s = (int) d / 100;
            double ss = (double) s;
            m_distance.setText(ss / 10 + "km");
        }
        String s1=T.getDistrictName()==null?"":T.getDistrictName();
        String s2=T.getIndustryName()==null?"":T.getIndustryName();
        m_type.setText(s1+"  "+s2);
//        if (T.getDistrictName() == null || T.getIndustryName() == null) {
//            m_type.setText(" ");
//        } else {
//            m_type.setText(T.getDistrictName() + " " + T.getIndustryName());
//        }


    }
}
