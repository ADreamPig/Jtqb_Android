package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/3.
 */
public class ShopArea {
    private String id;
    private String areaCode;
    private String areaName;
    private String cityCode;

    public ShopArea(String id, String areaName) {
        this.id = id;
        this.areaName = areaName;
    }

    @Override
    public String toString() {
        return "ShopArea{" +
                "id='" + id + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", areaName='" + areaName + '\'' +
                ", cityCode='" + cityCode + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
