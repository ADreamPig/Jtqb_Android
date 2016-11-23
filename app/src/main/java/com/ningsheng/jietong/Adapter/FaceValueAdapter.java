package com.ningsheng.jietong.Adapter;

import android.view.View;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.FaceValue;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/23.
 */
public class FaceValueAdapter extends Adapter<FaceValue> {
    private int select;

    public String getSelect() {
        return mlist.get(select).getFaceValue();
    }
    public void setSelect(int select){
        this.select=select;
        notifyDataSetChanged();
    }

    public FaceValueAdapter(BaseActivity activity, List<FaceValue> list) {
        super(activity, list, R.layout.adapter_facevalue);
    }

    @Override
    public void getview(ViewHolder vh, final int position, FaceValue T) {
        TextView m_text=(TextView) vh.getView(R.id.adapter_facevalue_text);
        m_text.setText("￥"+T.getFaceValue());
        if(select==position){
            m_text.setBackgroundResource(R.drawable.shape_button);
        }else{
            m_text.setBackgroundResource(R.drawable.shape_messagecode_gary);
        }
        m_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=position;
                notifyDataSetChanged();
            }
        });

    }
}
