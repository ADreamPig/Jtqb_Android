package com.ningsheng.jietong.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhushunqing on 2016/2/29.
 */
public class DataDictInfo  implements Serializable{
    private String provinceName;
    private List<CityEntity> cityList;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public DataDictInfo(String province, List<CityEntity> list, long time) {
        this.provinceName = province;
        this.cityList = list;
        this.time = time;
    }

    @Override
    public String toString() {
        return "DataDictInfo{" +
                "provinceName='" + provinceName + '\'' +
                ", cityList=" + cityList +
                ", time=" + time +
                '}';
    }

    public String getProvince() {
        return provinceName;
    }

    public void setProvince(String province) {
        this.provinceName = province;
    }

    public List<CityEntity> getList() {
        return cityList;
    }

    public void setList(List<CityEntity> list) {
        this.cityList = list;
    }
}
