package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/23.
 */
public class ConsumerDetails {
    private DetailData transList;
    private String isNext;

    public String getIsNext() {
        return isNext;
    }

    public void setIsNext(String isNext) {
        this.isNext = isNext;
    }

    public Object getTransList() {
        return transList;
    }

    public void setTransList(DetailData transList) {
        this.transList = transList;
    }

    @Override
    public String toString() {
        return "ConsumerDetails{" +
                "transList=" + transList +
                ", isNext='" + isNext + '\'' +
                '}';
    }
}
