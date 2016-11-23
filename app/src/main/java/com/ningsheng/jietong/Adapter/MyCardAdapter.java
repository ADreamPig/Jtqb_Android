package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.MyCardInfo;
import com.ningsheng.jietong.R;

import java.util.List;

/**
 * Created by hasee-pc on 2016/2/22.
 */
public class MyCardAdapter extends BaseAdapter {
    private Context context;
    private List<MyCardInfo> cards;

    public MyCardAdapter(Context context, List<MyCardInfo> cards) {
        this.context = context;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        Log.d("-------size", cards.size() + "");
        return cards.size();
    }

    @Override
    public MyCardInfo getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_mycard, parent, false);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initData(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvType;
        private TextView tvRealName;
        private TextView tvDenomination;
        private TextView tvCardNo;
        private ImageView ivGua;

        private void initView(View convertView) {
            tvType = (TextView) convertView.findViewById(R.id.mycard_item_cardType);
            tvCardNo = (TextView) convertView.findViewById(R.id.mycard_item_cardNo);
            ivGua = (ImageView) convertView.findViewById(R.id.adapter_mycard_guashi);
            tvRealName = (TextView) convertView.findViewById(R.id.mycard_item_realName);
            tvDenomination = (TextView) convertView.findViewById(R.id.mycard_item_denomination);
        }

        public void initData(MyCardInfo info) {

            if ("0".equals(info.getCardType())) {//实体卡
                if ("1".equals(info.getStatus())) {//挂失
                    ivGua.setVisibility(View.VISIBLE);
                    tvRealName.setVisibility(View.GONE);
                } else {//非挂失
                    ivGua.setVisibility(View.GONE);
                    tvRealName.setVisibility(View.VISIBLE);
                }
            } else {//虚拟卡
                ivGua.setVisibility(View.GONE);
                tvRealName.setVisibility(View.GONE);
            }

            if (null == info.getFaceValue() || "0".equals(info.getFaceValue())) {
                tvDenomination.setVisibility(View.INVISIBLE);
            } else {
                tvDenomination.setVisibility(View.VISIBLE);
                tvDenomination.setText(info.getFaceValue());
            }

            if (null != info.getCardNo()) {
                String cardNo = info.getCardNo();
                if (cardNo.length() > 8) {
                    StringBuilder sb = new StringBuilder(cardNo.length() - 8);
                    for (int i = 0; i < cardNo.length() - 8; i++) {
                        if ((i + 1) % 4 == 0)
                            sb.append(" ");
                        sb.append("*");
                    }
                    tvCardNo.setText(cardNo.substring(0, 4) + " " + sb.toString() + " " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
                } else {
                    tvCardNo.setText(cardNo);
                }
            } else {
                tvCardNo.setVisibility(View.INVISIBLE);
            }

        }
    }

}
