package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/9.
 */
public class HotKey {
    private String id;
    private String keyword;

    @Override
    public String toString() {
        return "HotKey{" +
                "id='" + id + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
