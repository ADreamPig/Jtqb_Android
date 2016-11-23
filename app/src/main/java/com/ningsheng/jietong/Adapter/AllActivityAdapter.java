package com.ningsheng.jietong.Adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.MerchantSales;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.MediaUtil;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/19.
 */
public class AllActivityAdapter extends Adapter<MerchantSales>
{
    public AllActivityAdapter(BaseActivity activity, List<MerchantSales> list) {
        super(activity, list, R.layout.adapter_allactivity);
    }

    @Override
    public void getview(ViewHolder vh, int position, MerchantSales T) {
        ImageView m_image=vh.getView(R.id.adapter_allactivity_image);
        TextView m_title=vh.getView(R.id.adapter_allactivity_title);
        TextView m_time=vh.getView(R.id.adapter_allactivity_time);

        MediaUtil.displayImage(mactivity,T.getImageApp(),m_image);
        m_title.setText(T.getTitle());
        m_time.setText("活动日期："+T.getsTime()+"-"+T.geteTime());
    }
}
