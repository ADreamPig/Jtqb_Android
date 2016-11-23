package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/2.
 */
public class OrderDetail {
    private String id;
    private String message;
    private String tradeNo;
    private String money;
    private String notifyUrl;

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                ", money='" + money + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
