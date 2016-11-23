package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * 收货地址实体类
 * Created by hasee-pc on 2016/3/11.
 */
public class MyAddressInfo implements Serializable {

    /**
     * createTime : 1457668676000
     * prov : null
     * accountId : 182b4df3bc43414fb2e19260e9a9a3b1
     * consignee : 13764012001
     * handlerCreateTime : 2016-03-11 11:57:56
     * city : null
     * id : bd7d9b7502f4423089c6f846b6c9b752
     * address : dddjdjn
     * provCityAddr :
     * district : null
     * shippingAddress : null
     * accountMobile : null
     * commonaddr : 1
     * mobile : 13764012001
     */

    private long createTime;
    private String prov;
    private String accountId;
    private String consignee;
    private String handlerCreateTime;
    private String city;
    private String id;
    private String address;
    private String provCityAddr;
    private String district;
    private Object shippingAddress;
    private Object accountMobile;
    private String commonaddr;
    private String mobile;

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public void setHandlerCreateTime(String handlerCreateTime) {
        this.handlerCreateTime = handlerCreateTime;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvCityAddr(String provCityAddr) {
        this.provCityAddr = provCityAddr;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setShippingAddress(Object shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setAccountMobile(Object accountMobile) {
        this.accountMobile = accountMobile;
    }

    public void setCommonaddr(String commonaddr) {
        this.commonaddr = commonaddr;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Object getProv() {
        return prov;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getConsignee() {
        return consignee;
    }

    public String getHandlerCreateTime() {
        return handlerCreateTime;
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getProvCityAddr() {
        return provCityAddr;
    }

    public String getDistrict() {
        return district;
    }

    public Object getShippingAddress() {
        return shippingAddress;
    }

    public Object getAccountMobile() {
        return accountMobile;
    }

    public String getCommonaddr() {
        return commonaddr;
    }

    public String getMobile() {
        return mobile;
    }
}
