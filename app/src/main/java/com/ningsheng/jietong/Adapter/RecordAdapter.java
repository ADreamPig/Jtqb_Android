package com.ningsheng.jietong.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.TransRecor;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/24.
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<TransRecor> list;
    private BaseActivity activity;
    private RecycleOnItemClickLisiener listener;

    public void setListener(RecycleOnItemClickLisiener listener) {
        this.listener = listener;
    }

    public RecordAdapter(BaseActivity activity, List<TransRecor> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("---onCreateViewHolder--", "onCreateViewHolder");
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_record, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("---onBindViewHolder--", "onBindViewHolder");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnItemClickListener(holder.itemView, position);
                }
            }
        });
        holder.m_name.setText(list.get(position).getTransMerchantName());
        holder.m_time.setText(list.get(position).getAddTime());
        if(Double.parseDouble(list.get(position).getTransMoney())>0){
            holder.m_money.setTextColor(Color.parseColor("#10C12B"));
        }else{
            holder.m_money.setTextColor(Color.parseColor("#F72929"));
        }
        holder.m_money.setText(list.get(position).getTransMoney());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView m_name, m_time, m_money;

        public ViewHolder(View itemView) {
            super(itemView);
            m_name = (TextView) itemView.findViewById(R.id.act_buycard_first_name);
            m_time = (TextView) itemView.findViewById(R.id.act_buycard_first_time);
            m_money = (TextView) itemView.findViewById(R.id.act_buycard_first_money);
        }
    }
}
