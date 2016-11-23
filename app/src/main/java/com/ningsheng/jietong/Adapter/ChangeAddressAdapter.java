package com.ningsheng.jietong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.MyAddressInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/3/8.
 */
public class ChangeAddressAdapter extends RecyclerView.Adapter<ChangeAddressAdapter.CViewHolder> {
    private List<MyAddressInfo> list;
    private BaseActivity activity;
    private RecycleOnItemClickLisiener onItemClickListenet;

    public void setOnItemClickListenet(RecycleOnItemClickLisiener onItemClickListenet) {
        this.onItemClickListenet = onItemClickListenet;
    }

    public ChangeAddressAdapter(BaseActivity activity, List<MyAddressInfo> list) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public CViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CViewHolder holder = new CViewHolder(LayoutInflater.from(activity).inflate(R.layout.adapter_change_address, parent));
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final CViewHolder holder, final int position) {
        holder.nameAndPhone.setText(list.get(position).getConsignee() + "   " + list.get(position).getMobile());
        holder.address.setText(list.get(position).getProvCityAddr() + list.get(position).getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListenet!=null){
                    onItemClickListenet.OnItemClickListener(holder.itemView,position);
                }
            }
        });
    }

    class CViewHolder extends RecyclerView.ViewHolder {
        private TextView nameAndPhone, address;
        private ImageView m_Check;

        public CViewHolder(View itemView) {
            super(itemView);
            nameAndPhone = (TextView) itemView.findViewById(R.id.adapter_change_nameAndPhone);
            address = (TextView) itemView.findViewById(R.id.adapter_change_address);
            m_Check = (ImageView) itemView.findViewById(R.id.adapter_change_check);

        }
    }
}
