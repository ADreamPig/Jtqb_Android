package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * 我的卡
 * Created by hasee-pc on 2016/3/1.
 */
public class MyCardInfo  implements Serializable{


    /**
     * id : 32d630b3f10e4cc291e11b2c2e66c5fb
     * mobile : 13764012001
     * balance : 1
     * handlerBalance : null
     * faceValue : null
     * isRealName : null
     * handlerIsRealName : null
     * cardType : 0
     * handlerCardType : 实体卡
     * accountId : 182b4df3bc43414fb2e19260e9a9a3b1
     * status : 0
     * handlerStatus : 正常
     * payType : null
     * handlerPayType : null
     * cardPwd : 1
     * createTime : 1457954003000
     * cardNo : 131564564561
     */

    private String id;
    private String mobile;
    private String balance;
    private String faceValue;
    private Object isRealName;
    private Object handlerIsRealName;
    private String cardType;//1虚拟卡 0.实体卡
    private String handlerCardType;
    private String accountId;
    private String cardPwd;
    private String cardNo;
    private String status;//0 正常 1.挂失

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "MyCardInfo{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", balance='" + balance + '\'' +
                ", faceValue='" + faceValue + '\'' +
                ", isRealName=" + isRealName +
                ", handlerIsRealName=" + handlerIsRealName +
                ", cardType='" + cardType + '\'' +
                ", handlerCardType='" + handlerCardType + '\'' +
                ", accountId='" + accountId + '\'' +
                ", cardPwd='" + cardPwd + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Object getIsRealName() {
        return isRealName;
    }

    public void setIsRealName(Object isRealName) {
        this.isRealName = isRealName;
    }

    public Object getHandlerIsRealName() {
        return handlerIsRealName;
    }

    public void setHandlerIsRealName(Object handlerIsRealName) {
        this.handlerIsRealName = handlerIsRealName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getHandlerCardType() {
        return handlerCardType;
    }

    public void setHandlerCardType(String handlerCardType) {
        this.handlerCardType = handlerCardType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
