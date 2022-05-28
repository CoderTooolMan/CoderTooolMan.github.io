package com.huawei.java.main.strategy;

import com.huawei.java.main.common.Server;
import com.huawei.java.main.common.VM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class top5 implements Comparator {
    //使用率排序
    public int compare(Object o1, Object o2) {
        Server s1= (Server) o1;
        Server s2= (Server) o2;
        s1.setmoved(0);
        double ratio1 = 1-1.0*s1.getCpuUsed()/s1.getTotalCpu()+1-1.0*s1.getMemUsed()/s1.getTotalMem();
        double ratio2 = 1-1.0*s2.getCpuUsed()/s2.getTotalCpu()+1-1.0*s2.getMemUsed()/s2.getTotalMem();
//        double ratio1 =s1.getVmNum();
//        double ratio2 = s2.getVmNum();
        if (ratio1 < ratio2) return 1;
        else if(ratio1==ratio2)
            return 0;
        else return -1;
    }
    //num，cost排序
//    public int compare(Object o1, Object o2) {
//        Server s1= (Server) o1;
//        Server s2= (Server) o2;
//        Integer num1 = s1.getVmNum();
//        Integer num2 = s2.getVmNum();
//        s1.setmoved(0);
//        int scomp = num1.compareTo(num2);
//        if(scomp!=0)
//        {
//            return scomp;
//        }
//        else{
//            Integer x1=((Server) o1).getEnergyCost();
//            Integer x2=((Server) o2).getEnergyCost();
//            return x2.compareTo(x1);
//        }
////        double ratio1 = 1-1.0*s1.getCpuUsed()/s1.getTotalCpu()+1-1.0*s1.getMemUsed()/s1.getTotalMem();
////        double ratio2 = 1-1.0*s2.getCpuUsed()/s2.getTotalCpu()+1-1.0*s2.getMemUsed()/s2.getTotalMem();
//
//    }

    private static void swap(ArrayList<String[]> servers, int i, int j) {
        String[] temp = servers.get(i);
        servers.set(i, servers.get(j));
        servers.set(j, temp);
    }
    private static double pe(String[] request,HashMap<String, VM> vms)
    {
        String vmid = request[2];
        String re = request[0];
        int te=0;
        VM tmp = null;
        if(re.equals("add")) {
            for (String key : vms.keySet()) {
                if (Objects.equals(key, vmid)) {
                    tmp = vms.get(key);
                    //1.0 * (tmp.getCpu() + tmp.getMem())
                    break;
                }
            }
        }
        else
            te=0;
        double p= 1.0* (tmp.getCpu()+tmp.getMem());
        return p;
    }
    public static void quicksort(ArrayList<String[]> req, int start, int end, HashMap<String, VM> vms) {
        sort(req,start,end-1,vms);
//        double[] a = new double[end-start];
//        for(int i=start;i<end;i++)
//            a[i]=pe(req.get(i),vms);
//        int i=0;
    }

    private static void sort(ArrayList<String[]> req,int start,int end, HashMap<String, VM> vms) {
        if (start >= end) {
            return;
        }
        int i = start, j = end;
        String[] index = req.get(i);
        while (i < j) {
            while (i < j && pe(req.get(j),vms) <= pe(index,vms)) {
                j--;
            }
            if (i < j) {
                req.set(i++, req.get(j));
            }
            while (i < j && pe(req.get(i),vms) >pe(index,vms)) {
                i++;
            }
            if (i < j) {
                req.set(j--, req.get(i));
            }
        }
        req.set(i, index);
        sort(req, start, i - 1,vms);
        sort(req, i + 1,end,vms);
    }


    public static void bubblesort(ArrayList<String[]> a, int start, int end, HashMap<String, VM> vms) {
        int count=0;
        for (int i = start; i < end-1; i++) {
            boolean flag=true;
            for (int j = start; j < end-i-1; j++) {
                if (pe(a.get(j),vms)>pe(a.get(j + 1),vms)) {
                    swap(a,j,j+1);
                    flag=false;
                }
                count++;
            }
            if (flag) {
                break;
            }
        }
    }

}
