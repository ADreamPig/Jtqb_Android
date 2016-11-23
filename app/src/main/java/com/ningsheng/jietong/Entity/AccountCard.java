package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/2.
 */
public class AccountCard {
    private String id;
    private String balance;//余额
    private String faceValue;
    private String handlerIsRealName;
    private String cardNo;
    private String cardType;
    private String status;//0 正常 1.挂失

    @Override
    public String toString() {
        return "AccountCard{" +
                "id='" + id + '\'' +
                ", balance='" + balance + '\'' +
                ", faceValue='" + faceValue + '\'' +
                ", handlerIsRealName='" + handlerIsRealName + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", cardType='" + cardType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getHandlerIsRealName() {
        return handlerIsRealName;
    }

    public void setHandlerIsRealName(String handlerIsRealName) {
        this.handlerIsRealName = handlerIsRealName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
