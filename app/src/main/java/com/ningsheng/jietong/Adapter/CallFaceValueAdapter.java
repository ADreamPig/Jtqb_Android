package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/5/6.
 */
public class CallFaceValueAdapter extends RecyclerView.Adapter<CallViewHolder> {
    private List<String> list;
    private Context context;

    public CallFaceValueAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }


    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CallViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_call_facevalue,null));
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
class CallViewHolder extends RecyclerView.ViewHolder {
    private TextView value,price;

    public CallViewHolder(View itemView) {
        super(itemView);
        value=(TextView) itemView.findViewById(R.id.adapter_call_facevalue_value);
    }
}
