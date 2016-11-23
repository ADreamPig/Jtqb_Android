package com.ningsheng.jietong.Entity;

import java.util.List;

/**
 * Created by zhushunqing on 2016/3/23.
 */
public class DetailData {
    private List<FlowingDetailData> TransactionFlowingDetailData;

    public List<FlowingDetailData> getTransactionFlowingDetailData() {
        return TransactionFlowingDetailData;
    }

    public void setTransactionFlowingDetailData(List<FlowingDetailData> transactionFlowingDetailData) {
        TransactionFlowingDetailData = transactionFlowingDetailData;
    }

    @Override
    public String toString() {
        return "DetailData{" +
                "TransactionFlowingDetailData=" + TransactionFlowingDetailData +
                '}';
    }
}
