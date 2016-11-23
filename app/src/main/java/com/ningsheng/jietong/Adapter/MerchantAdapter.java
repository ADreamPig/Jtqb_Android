package com.ningsheng.jietong.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.Merchant;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.ImageLoaderUtil;
import com.ningsheng.jietong.Utils.MediaUtil;
import com.ningsheng.jietong.View.BorderImageView;

import java.util.List;

/**
 * Created by zhushunqing on 2016/1/28.
 */
public class MerchantAdapter extends Adapter<Merchant> {
    public MerchantAdapter(BaseActivity activity, List<Merchant> list) {
        super(activity, list, R.layout.adapter_merchant);
    }

    @Override
    public void getview(ViewHolder vh, int position, Merchant T) {
        BorderImageView imageView=vh.getView(R.id.adapter_merchant_image);
        TextView m_name=vh.getView(R.id.adapter_merchant_name);
        TextView m_discount=vh.getView(R.id.adapter_merchant_discount);
        TextView m_type=vh.getView(R.id.adapter_merchant_type);
        TextView m_distance=vh.getView(R.id.adapter_merchant_distance);

        MediaUtil.displayImage(mactivity,T.getImageApp(),imageView);
        m_name.setText(T.getMerchantName());
//        m_type.setText(T.getDistrictName()+"  "+T.getIndustryName());
//        String n=T.getDistrictName();
        String s1=T.getDistrictName()==null?"":T.getDistrictName();
        String s2=T.getIndustryName()==null?"":T.getIndustryName();
        m_type.setText(s1+"  "+s2);
        if(T.getDistance()==null||T.getDistance().equals("")){
            m_distance.setVisibility(View.INVISIBLE);
            m_distance.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }else{
            m_distance.setVisibility(View.VISIBLE);
//            long s=Long.parseLong(T.getDistance());
//            int a=(int)(s/10);
            m_distance.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_juli,0,0,0);
            double  d=Double.parseDouble(T.getDistance());
            int s=(int)d/100;
            double ss=(double) s;
            m_distance.setText(ss/10+"km");
        }

        if(T.getDiscount()!=10){
            m_discount.setText(T.getDiscount()+"折");
            m_discount.setVisibility(View.VISIBLE);
        }else{
            m_discount.setVisibility(View.INVISIBLE);
        }




    }
}
