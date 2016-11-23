package com.ningsheng.jietong.Entity;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/25.
 */
public class HomeEntity {
    private List<Merchant> merchant;
    private List<MerchantSales> merchantSales;
    private List<Banner> banner;

    @Override
    public String toString() {
        return "HomeEntity{" +
                "merchant=" + merchant +
                ", merchantSales=" + merchantSales +
                ", banner=" + banner +
                '}';
    }

    public List<Merchant> getMerchant() {
        return merchant;
    }

    public void setMerchant(List<Merchant> merchant) {
        this.merchant = merchant;
    }

    public List<MerchantSales> getMerchantSales() {
        return merchantSales;
    }

    public void setMerchantSales(List<MerchantSales> merchantSales) {
        this.merchantSales = merchantSales;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }
}
