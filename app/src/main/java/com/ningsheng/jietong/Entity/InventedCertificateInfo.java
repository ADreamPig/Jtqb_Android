package com.ningsheng.jietong.Entity;

/**
 * 电子券实体类
 * Created by hasee-pc on 2016/3/11.
 */
public class InventedCertificateInfo {
    /**
     * id : 2015-02-15 16:32:00.0
     * accountId : 9a6178c81c9d42cb8e3927826faf48b0
     * createTime : 1456991068000
     * money : 2
     * merchantId : 10
     * cardNo : 1
     * returnCode : 1
     * startTime : 1423989120000
     * endTime : 1487147520000
     * faceValue : 100
     * buyDate : 2016-03-03 15:44:28
     * mobile : 15288745688
     */

    private int money;
    private String cardNo;
    private String returnCode;
    private String buyDate;
    private String mobile;

    public void setMoney(int money) {
        this.money = money;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMoney() {
        return money;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public String getMobile() {
        return mobile;
    }



}
