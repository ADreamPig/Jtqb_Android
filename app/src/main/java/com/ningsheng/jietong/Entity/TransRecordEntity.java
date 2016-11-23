package com.ningsheng.jietong.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhushunqing on 2016/3/21.
 */
public class TransRecordEntity implements Serializable{
    //    private String addTime;
//    private String id;

    private String pageStart;
    private String pageSize;
    private List<TransRecor> data;
    private String totalCount;
    private String totalPages;
    private String hasNext;
    private String nextPage;
    private String hasPre;
    private String prePage;


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

    public List<TransRecor> getData() {
        return data;
    }

    public void setData(List<TransRecor> data) {
        this.data = data;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getHasNext() {
        return hasNext;
    }

    public void setHasNext(String hasNext) {
        this.hasNext = hasNext;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getHasPre() {
        return hasPre;
    }

    public void setHasPre(String hasPre) {
        this.hasPre = hasPre;
    }

    public String getPrePage() {
        return prePage;
    }

    public void setPrePage(String prePage) {
        this.prePage = prePage;
    }
}
