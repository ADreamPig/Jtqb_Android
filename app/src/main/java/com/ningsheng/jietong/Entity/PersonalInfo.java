package com.ningsheng.jietong.Entity;

/**
 * Created by hasee-pc on 2016/3/11.
 */
public class PersonalInfo {

    /**
     * createTime : 1456991068000
     * sex :
     * status : 1
     * dividend : null
     * loginLockTime : null
     * image : http://hyjf.shizitegong.com:8080/image/2016/03/11/f327ef8c53ce0d1820a0dd2c4bc34578.png
     * loginLockTimeNull : false
     * identityCard :
     * transLockTimeNull : false
     * transactionPwd :
     * integral : null
     * id : 182b4df3bc43414fb2e19260e9a9a3b1
     * handlerstatus : 正常
     * address :
     * transLockTime : null
     * name : 王帅
     * loginPwdErrorCount : 0
     * dividendAccount : null
     * userName : 12312
     * realCreateTime : 2016-03-03 15:44:28
     * loginPwd : 1000:6adf80493511fbab8bd73a8da7b1a219630d2fbc670daefe:d3c924b4981940abab0c543783a1b35b671cdb31c97d762c
     * singleLogin :
     * transPwdErrorCount : null
     * cardNum : null
     * mobile : 18017016827
     */

    private String status;
    private String image;
    private String identityCard;
    private String id;
    private String address;
    private String name;
    private String userName;
    private String mobile;
    private String accountBalence;
    private String elecVoucherCount;
    private String cardNum;
    public void setStatus(String status) {
        this.status = status;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }


    public String getImage() {
        return image;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAccountBalence() {
        return accountBalence;
    }

    public String getElecVoucherCount() {
        return elecVoucherCount;
    }

    public void setElecVoucherCount(String elecVoucherCount) {
        this.elecVoucherCount = elecVoucherCount;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public void setAccountBalence(String accountBalence) {

        this.accountBalence = accountBalence;
    }
}
