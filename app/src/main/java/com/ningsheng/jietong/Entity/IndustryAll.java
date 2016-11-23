package com.ningsheng.jietong.Entity;

import java.util.List;

/**
 * Created by zhushunqing on 2016/3/11.
 */
public class IndustryAll {
    private List<Industry> types;
    private String all;



    public List<Industry> getIndustry() {
        return types;
    }

    public void setIndustry(List<Industry> industry) {
        this.types = industry;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}
