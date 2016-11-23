package com.ningsheng.jietong.Entity;

import java.util.List;

/**
 * Created by zhushunqing on 2016/4/1.
 */
public class ActivityInfo {

    private List<MerchantSales> data;

    public List<MerchantSales> getData() {
        return data;
    }

    public void setData(List<MerchantSales> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ActivityInfo{" +
                "data=" + data +
                '}';
    }
}
