package com.ningsheng.jietong.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ningsheng.jietong.Entity.BuyCardOrder;
import com.ningsheng.jietong.Entity.OrderDetail;
import com.ningsheng.jietong.R;
import com.ningsheng.jietong.Utils.DateUtils;

import java.util.List;

/**
 * Created by hasee-pc on 2016/2/24.
 */
public class PayCardRecordAdapter extends BaseAdapter {
    private Context context;
    private List<BuyCardOrder> datas;
    private LayoutInflater inflater;

    public PayCardRecordAdapter(Context context, List<BuyCardOrder> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
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
            convertView = inflater.inflate(R.layout.layout_item_paycard_record, null);
            holder = new ViewHolder();
            holder.initView(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.initData( datas.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvTitle;
        private TextView tvType;
        private TextView tvPay;
        private TextView tvAmount;
        private TextView tvPayMode;
        private TextView tvTime;
        private TextView tvNumber;

        public void initView(View convertView) {
            tvTitle = (TextView) convertView.findViewById(R.id.paycard_record_tv_title);
            tvType = (TextView) convertView.findViewById(R.id.paycard_record_tv_type);
            tvPay = (TextView) convertView.findViewById(R.id.paycard_record_tv_pay);
            tvAmount = (TextView) convertView.findViewById(R.id.paycard_record_tv_amount);
            tvPayMode = (TextView) convertView.findViewById(R.id.paycard_record_tv_payMode);
            tvTime = (TextView) convertView.findViewById(R.id.paycard_record_tv_time);
            tvNumber = (TextView) convertView.findViewById(R.id.paycard_record_tv_number);
        }

        public void initData(BuyCardOrder get) {
            if("0".equals(get.getCardType())){
                tvTitle.setText("捷通卡（不记名卡）");
            }else if("1".equals(get.getCardType())){
                tvTitle.setText("捷通卡（记名卡）");
            }else if("2".equals(get.getCardType())){
                tvTitle.setText("捷通卡（记名卡）");
            }

            if("0".equals(get.getCardType())){
                tvType.setText("卡类型："+"面值卡");
//                tvTitle.setText();
            }else if("1".equals(get.getCardType())){
                tvType.setText("卡类型："+"充值卡");
            }else if("2".equals(get.getCardType())){
                tvType.setText("卡类型："+"面值卡");
            }else{
                tvType.setText("卡类型："+"");
            }

            tvPay.setText("实际支付：￥"+get.getMoney());
            tvAmount.setText("购卡数量："+get.getCardNumber());
            if(get.getPayType().equals("3")){
                tvPayMode.setText("支付方式：支付宝");
            }else{
                tvPayMode.setText("支付方式：");
            }
//            tvTime.setText("交易时间："+ DateUtils.timeStamp2Date(get.getAddTime(),"yyyy-MM-dd hh:mm:ss"));
            tvTime.setText("交易时间："+ get.getAddTime());
            tvNumber.setText("交易编号："+get.getTradeNo());
        }
    }
}
