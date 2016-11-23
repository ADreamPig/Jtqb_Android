package com.ningsheng.jietong.Adapter;

import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.HotKey;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class HotSearchAdapter extends Adapter<HotKey> {
    public HotSearchAdapter(BaseActivity activity, List<HotKey> list) {
        super(activity, list, R.layout.adapter_hot_search);

    }

    @Override
    public int getCount() {
        return mlist.size()>6?6:mlist.size();
    }

    @Override
    public void getview(ViewHolder vh, int position, HotKey T) {
        TextView m_name=vh.getView(R.id.adapter_hot_search_name);
        m_name.setText(T.getKeyword());
    }
}
