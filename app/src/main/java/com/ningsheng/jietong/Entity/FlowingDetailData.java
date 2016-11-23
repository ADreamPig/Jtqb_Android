package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * Created by zhushunqing on 2016/3/23.
 */
public class FlowingDetailData implements Serializable{
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FlowingDetailData(String title,String transDate,String transTime) {
        this.title = title;
        this.transDate=transDate;
        this.transTime=transTime;
    }

    private String gServiceFace;
    private String settDate;
    private String cardType;
    private String cardCode1;
    private String cardCode2;
    private String amount;
    private String transDate;
    private String terminalCode;
    private String feeAmount;
    private String transTime;
    private String unitName1;
    private String unitName2;
    private String merchantCode;
    private String transCode;

    @Override
    public String toString() {
        return "FlowingDetailData{" +
                "title='" + title + '\'' +
                ", gServiceFace='" + gServiceFace + '\'' +
                ", settDate='" + settDate + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardCode1='" + cardCode1 + '\'' +
                ", cardCode2='" + cardCode2 + '\'' +
                ", amount='" + amount + '\'' +
                ", transDate='" + transDate + '\'' +
                ", terminalCode='" + terminalCode + '\'' +
                ", feeAmount='" + feeAmount + '\'' +
                ", transTime='" + transTime + '\'' +
                ", unitName1='" + unitName1 + '\'' +
                ", unitName2='" + unitName2 + '\'' +
                ", merchantCode='" + merchantCode + '\'' +
                ", transCode='" + transCode + '\'' +
                '}';
    }

    public String getgServiceFace() {
        return gServiceFace;
    }

    public void setgServiceFace(String gServiceFace) {
        this.gServiceFace = gServiceFace;
    }

    public String getSettDate() {
        return settDate;
    }

    public void setSettDate(String settDate) {
        this.settDate = settDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardCode1() {
        return cardCode1;
    }

    public void setCardCode1(String cardCode1) {
        this.cardCode1 = cardCode1;
    }

    public String getCardCode2() {
        return cardCode2;
    }

    public void setCardCode2(String cardCode2) {
        this.cardCode2 = cardCode2;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getUnitName1() {
        return unitName1;
    }

    public void setUnitName1(String unitName1) {
        this.unitName1 = unitName1;
    }

    public String getUnitName2() {
        return unitName2;
    }

    public void setUnitName2(String unitName2) {
        this.unitName2 = unitName2;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }
}
