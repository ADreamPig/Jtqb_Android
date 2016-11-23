package com.ningsheng.jietong.Entity;

import java.util.List;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class AddressInfo {
    private String pageStart;
    private String pageSize;
    private List<AddressEntity> data;

    @Override
    public String toString() {
        return "AddressInfo{" +
                "pageStart='" + pageStart + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", data=" + data +
                '}';
    }

    public String getPageStart() {
        return pageStart;
    }

    public void setPageStart(String pageStart) {
        this.pageStart = pageStart;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<AddressEntity> getData() {
        return data;
    }

    public void setData(List<AddressEntity> data) {
        this.data = data;
    }
}
