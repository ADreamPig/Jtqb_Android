package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * Created by zhushunqing on 2016/3/2.
 */
public class User implements Serializable{
    private String id="";
    private String sex;
    private String identityCard;//身份证号码
    private String status;//状态（0已删除 1 正常  2 禁用 3 冻结）
    private String handlerstatus;
    private String image;
    private String userName;
    private String address;
    private  String mobile;
    private String transactionPwd;
    private String accountBalence;//余额
    private String cardNum;
    private String elecVoucherCount;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getElecVoucherCount() {
        return elecVoucherCount;
    }

    public void setElecVoucherCount(String elecVoucherCount) {
        this.elecVoucherCount = elecVoucherCount;
    }

    public String getAccountBalence() {
        return accountBalence;
    }

    public void setAccountBalence(String accountBalence) {
        this.accountBalence = accountBalence;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", sex='" + sex + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", status='" + status + '\'' +
                ", handlerstatus='" + handlerstatus + '\'' +
                ", image='" + image + '\'' +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", transactionPwd='" + transactionPwd + '\'' +
                '}';
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHandlerstatus() {
        return handlerstatus;
    }

    public void setHandlerstatus(String handlerstatus) {
        this.handlerstatus = handlerstatus;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTransactionPwd() {
        return transactionPwd;
    }

    public void setTransactionPwd(String transactionPwd) {
        this.transactionPwd = transactionPwd;
    }
}
