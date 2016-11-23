package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class AddressEntity implements Serializable {
    private String id;
    private String accountId;
    private String address;
    private String provCityAddr;
    private String consignee;
    private String createTime;
    private String mobile;
    private String commonaddr;
    private String prov;
    private String city;
    private String district;

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "AddressEntity{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", address='" + address + '\'' +
                ", provCityAddr='" + provCityAddr + '\'' +
                ", consignee='" + consignee + '\'' +
                ", createTime='" + createTime + '\'' +
                ", mobile='" + mobile + '\'' +
                ", commonaddr='" + commonaddr + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvCityAddr() {
        return provCityAddr;
    }

    public void setProvCityAddr(String provCityAddr) {
        this.provCityAddr = provCityAddr;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCommonaddr() {
        return commonaddr;
    }

    public void setCommonaddr(String commonaddr) {
        this.commonaddr = commonaddr;
    }
}
