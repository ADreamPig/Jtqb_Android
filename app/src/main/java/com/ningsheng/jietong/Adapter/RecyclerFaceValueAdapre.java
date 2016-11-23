package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.FaceValue;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/4/15.
 */
public class RecyclerFaceValueAdapre extends RecyclerView.Adapter<FVViewHolder> {
    private List<FaceValue> list;
    private Context context;
    private RecycleOnItemClickLisiener onItemClickListenet;
    private int select;
    public String getSelect() {
        return list.get(select).getFaceValue();
    }
    public void setSelect(int select){
        this.select=select;
        notifyDataSetChanged();
    }

    public void setOnItemClickListenet(RecycleOnItemClickLisiener onItemClickListenet) {
        this.onItemClickListenet = onItemClickListenet;
    }

    public RecyclerFaceValueAdapre(Context context,List<FaceValue> list){
        this.list=list;
        this.context=context;

    }


    @Override
    public FVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FVViewHolder viewholder=new FVViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_facevalue,null));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(FVViewHolder holder, final int position) {
        holder.m_text.setText("￥"+list.get(position).getFaceValue());
        holder.m_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=position;
                notifyDataSetChanged();
                onItemClickListenet.OnItemClickListener(v,position);
            }
        });

        if(select==position){
            holder.m_text.setBackgroundResource(R.drawable.shape_button);
        }else{
            holder.m_text.setBackgroundResource(R.drawable.shape_messagecode_gary);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
class FVViewHolder extends RecyclerView.ViewHolder{
    TextView m_text;

    public FVViewHolder(View itemView) {
        super(itemView);
        m_text=(TextView) itemView.findViewById(R.id.adapter_facevalue_text);
    }
}
