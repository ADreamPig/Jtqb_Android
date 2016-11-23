package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/2/25.
 */
public class Merchant{
    private String id;
    private String industryName;
    private String districtName;
    private String merchantName;
    private String image;
    private double discount;
    private String distance;
    private String imageApp;

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id='" + id + '\'' +
                ", industryName='" + industryName + '\'' +
                ", districtName='" + districtName + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", image='" + image + '\'' +
                ", discount='" + discount + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
