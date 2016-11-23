package com.ningsheng.jietong.Adapter;

import android.text.Html;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.MerchantSales;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.AndroidUtil;
import com.ningsheng.jietong.Utils.ImageLoaderUtil;
import com.ningsheng.jietong.Utils.MediaUtil;

import java.util.List;

/**
 * Created by zhushunqing on 2016/1/28.
 */
public class ActivityAdapter extends Adapter<MerchantSales> {

    public ActivityAdapter(BaseActivity activity, List<MerchantSales> list) {
        super(activity, list, R.layout.adapter_homeactivity);
    }

    @Override
    public int getCount() {
        return mlist.size()>4?4:mlist.size();
    }

    @Override
    public void getview(ViewHolder vh, int position, MerchantSales T) {
        ImageView imageview=vh.getView(R.id.adapter_homeactivity_image);
        TextView m_content=vh.getView(R.id.adapter_homeactivity_content);

        RelativeLayout. LayoutParams params=(RelativeLayout.LayoutParams)imageview.getLayoutParams();
        params.width= (AndroidUtil.getScreenSize(mactivity,1)-AndroidUtil.dip2px(mactivity,25))/2;
        params.height=(int)(params.width/2.2);

        m_content.setText(Html.fromHtml(T.getTitle()));
//        ImageLoaderUtil.getInstance().setImagebyurl(T.getImageApp(),imageview);

        MediaUtil.displayImage(mactivity,T.getImageApp(),imageview);

    }
}
