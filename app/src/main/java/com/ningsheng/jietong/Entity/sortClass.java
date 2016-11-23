package com.ningsheng.jietong.Entity;

import java.util.Comparator;

/**
 * Created by zhushunqing on 2016/3/23.
 */
public class sortClass implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        FlowingDetailData fd0 = (FlowingDetailData)lhs;
        FlowingDetailData fd1 = (FlowingDetailData)rhs;
        int flag = (fd0.getTransDate()+fd0.getTransTime()).compareTo(fd1.getTransDate()+fd1.getTransTime());
        return flag;
    }
}
