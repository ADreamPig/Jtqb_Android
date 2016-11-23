package com.ningsheng.jietong.Entity;

/**
 * Created by zhushunqing on 2016/3/3.
 */
public class Industry {
    private String id;
    private String name;

    public Industry(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
