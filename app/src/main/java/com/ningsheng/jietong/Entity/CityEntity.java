package com.ningsheng.jietong.Entity;

import java.io.Serializable;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class CityEntity implements Serializable{
    private String id;
    private String name;
    private String code;
    private String parentid;


    @Override
    public String toString() {
        return "CityEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", parentid='" + parentid + '\'' +
                '}';
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }
}
