package com.ningsheng.jietong.Adapter;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.App.BaseActivity;
import com.ningsheng.jietong.Entity.DataDictInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class RegionAdaper extends BaseExpandableListAdapter {
    private List<DataDictInfo> list;
    private BaseActivity activity;

    public RegionAdaper(List<DataDictInfo> list, BaseActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition).getProvince();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(activity).inflate(R.layout.linear_image_text,null,false);
//        view.setBackgroundColor(activity.getResources().getColor(R.color.background));
        TextView m_text=(TextView) view.findViewById(R.id.linear_image_text_text);
        ImageView m_image=(ImageView)view.findViewById(R.id.linear_image_text_image);
         m_text.setText(list.get(groupPosition).getProvince());
        m_text.setPadding(10,12,12,12);
        TextPaint tp = m_text.getPaint();
        tp.setFakeBoldText(true);
        if(isExpanded){
            m_image.setImageResource(R.mipmap.icon_openl);
        }else{
            m_image.setImageResource(R.mipmap.icon_normall);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(activity).inflate(R.layout.app_text,null,false);
            holder.textView=(TextView) convertView.findViewById(R.id.app_text_text);
            holder.textView.setPadding(20,12,10,12);
            holder.textView.setTextColor(activity.getResources().getColor(R.color.font_gray));
            holder.textView.setTextSize(14);
            TextPaint tp =  holder.textView.getPaint();
            tp.setFakeBoldText(false);
//            holder.textView.setText(list.get(groupPosition).getList().get(childPosition).getName());
            convertView.setTag(holder);
        }
        holder=(ViewHolder) convertView.getTag();
        holder.textView.setText(list.get(groupPosition).getList().get(childPosition).getName());

        return convertView;
    }
    class ViewHolder{
        TextView textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
