package com.ningsheng.jietong.Adapter;

import android.content.Context;
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
 * Created by hasee-pc on 2016/2/24.
 */
public class BalanceAdapter extends BaseAdapter {
    private Context context;
    private List<MyCardInfo> datas;
    private LayoutInflater inflater;

    public BalanceAdapter(Context context, List<MyCardInfo> datas) {
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_item_balance, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initData(datas.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvCard;
        private TextView tvCardStatus;
        private TextView tvBalance,tvRealName;
        private ImageView ivGua;

        private void initView(View convertView) {
            tvCard = (TextView) convertView.findViewById(R.id.balance_tv_card);
            tvCardStatus = (TextView) convertView.findViewById(R.id.balance_tv_cardStatus);
            tvBalance = (TextView) convertView.findViewById(R.id.balance_tv_balance);
            ivGua = (ImageView) convertView.findViewById(R.id.adapter_mycard_guashi);
            tvRealName = (TextView) convertView.findViewById(R.id.mycard_item_realName);
        }

        private void initData(MyCardInfo get) {

            if ("0".equals(get.getCardType())) {//实体卡
                if ("1".equals(get.getStatus())) {//挂失
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

            if (null != get.getCardNo()) {
                String cardNo = get.getCardNo();
                if (cardNo.length() > 8) {
                    StringBuilder sb = new StringBuilder(cardNo.length() - 8);
                    for (int i = 0; i < cardNo.length() - 8; i++) {
                        if ((i + 1) % 4 == 0)
                            sb.append(" ");
                        sb.append("*");
                    }
                    tvCard.setText(cardNo.substring(0, 4) + " " + sb.toString() + " " + cardNo.substring(cardNo.length() - 4, cardNo.length()));
                } else {
                    tvCard.setText(cardNo);
                }
            }
            tvBalance.setText("￥"+get.getBalance());
        }
    }
}
