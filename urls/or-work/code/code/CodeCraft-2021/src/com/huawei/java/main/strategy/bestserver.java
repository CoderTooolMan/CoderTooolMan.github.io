package com.huawei.java.main.strategy;

import com.huawei.java.main.common.Server;

import java.util.Comparator;

public class bestserver implements Comparator{
    public int compare(Object o1, Object o2) {
        Server s1= (Server) o1;
        Server s2= (Server) o2;
        s1.setmoved(0);
        double ratio1 = 1.0*(s1.getTotalCpu()+s1.getTotalMem())/s1.getDeviceCost();
        double ratio2 = 1.0*(s2.getTotalCpu()+s2.getTotalMem())/s2.getDeviceCost();
        if (ratio1 < ratio2) return 1;
        else if(ratio1==ratio2)
            return 0;
        else return -1;
    }
}
