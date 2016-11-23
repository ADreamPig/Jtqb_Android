package com.ningsheng.jietong.Entity;

/**
 * 交易记录实体类
 * Created by hasee-pc on 2016/3/2.
 */
public class ConsumeDetailInfo {
    private String title;
    private String name;

    public ConsumeDetailInfo(String title, String name) {
        this.title = title;
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ConsumeDetailInfo{" +
                "title='" + title + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public void setName(String name) {

        this.name = name;
    }
}

