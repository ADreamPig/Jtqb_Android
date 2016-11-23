package com.ningsheng.jietong.Entity;

import java.util.List;
import java.util.Map;

/**
 * Created by zhushunqing on 2016/3/3.
 */
public class MerchantDatas {
//    private SortType sortType;
//    private Map<String,String> sortType;
//    private List<Industry> industry;
//    private List<ShopArea> shopArea;
//    private String merchant;

    private ShopAreaAll shopArea;
    private IndustryAll industry;
    private Map<String,String> sortType;

    @Override
    public String toString() {
        return "MerchantDatas{" +
                "shopArea=" + shopArea +
                ", industry=" + industry +
                ", sortType=" + sortType +
                '}';
    }

    public ShopAreaAll getShopArea() {
        return shopArea;
    }

    public void setShopArea(ShopAreaAll shopArea) {
        this.shopArea = shopArea;
    }

    public IndustryAll getIndustry() {
        return industry;
    }

    public void setIndustry(IndustryAll industry) {
        this.industry = industry;
    }

    public Map<String, String> getSortType() {
        return sortType;
    }

    public void setSortType(Map<String, String> sortType) {
        this.sortType = sortType;
    }
}
