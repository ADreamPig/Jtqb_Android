package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * Created by zhushunqing on 2016/3/21.
 */
public class TransRecor implements Serializable{
    private String id;
    private String tradeNo;
    private String addTime;
    private String transMoney;
    private String transMerchantName;
    private String transTypeName;
    private String merchantName;
    private String handlerOperationType;

    public String getHandlerOperationType() {
        return handlerOperationType;
    }

    public void setHandlerOperationType(String handlerOperationType) {
        this.handlerOperationType = handlerOperationType;
    }

    @Override
    public String toString() {
        return "TransRecor{" +
                "id='" + id + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", addTime='" + addTime + '\'' +
                ", transMoney='" + transMoney + '\'' +
                ", transMerchantName='" + transMerchantName + '\'' +
                ", transTypeName='" + transTypeName + '\'' +
                ", merchantName='" + merchantName + '\'' +
                '}';
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(String transMoney) {
        this.transMoney = transMoney;
    }

    public String getTransMerchantName() {
        return transMerchantName;
    }

    public void setTransMerchantName(String transMerchantName) {
        this.transMerchantName = transMerchantName;
    }

    public String getTransTypeName() {
        return transTypeName;
    }

    public void setTransTypeName(String transTypeName) {
        this.transTypeName = transTypeName;
    }
}
