package com.huawei.java.main.common;


import com.huawei.java.main.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cache {

    public HashMap<String, VM> vms;
    public ArrayList<VM> vmAlive;
    public ArrayList<Server> servers;
    public ArrayList<Integer> remainVmNum;
    public AlgoParam param;
    public String fName;
    public StringBuffer sb;

    public Cache(AlgoParam param) {
        this.vms = new HashMap<>();
        this.servers = new ArrayList<>();
        this.remainVmNum = new ArrayList<>();
        this.vmAlive = new ArrayList<>();
        this.param=param;
        this.sb=new StringBuffer();
    }

    public void outputDay(HashMap<String, Integer> purchaseList,
                          HashMap<String, int[]> migrationList,
                          ArrayList<int[]> addList) {
        if (param.version.equals("release")) {
            // 1.purchase
            System.out.println("(purchase, " + purchaseList.size() + ")");
            int purchaseNum = 0;
            for (String serverType : purchaseList.keySet()) {
                System.out.println("(" + serverType + ", " + purchaseList.get(serverType) + ")");
                purchaseNum += purchaseList.get(serverType);
            }
            int count = 0;
            for (String serverType : purchaseList.keySet()) {
                for (int i = servers.size() - purchaseNum; i < servers.size(); i++) {
                    if (servers.get(i).getType().equals(serverType)) {
                        servers.get(i).setActualId(servers.size() - purchaseNum + count);
                        count++;
                    }
                }
            }
            if (migrationList.size() > 0) {
                System.out.println("(migration, " + migrationList.size() + ")");
                for (Map.Entry<String, int[]> tmp : migrationList.entrySet()) {
                    String vmId = tmp.getKey();
                    int[] info = tmp.getValue();
                    int serverId = servers.get(info[0]).getActualId();
                    int node = info[1];
                    if (node == 0) {
                        System.out.println("(" + vmId + ", " + serverId + ")");
                    } else if (node == 1) {
                        System.out.println("(" + vmId + ", " + serverId + ", A)");
                    } else if (node == 2) {
                        System.out.println("(" + vmId + ", " + serverId + ", B)");
                    } else {
                        System.err.println(node);
                    }
                }
            } else {
                System.out.println("(migration, 0)");
            }


            // 3.deploy
            for (int i = 0; i < addList.size(); i++) {
                int[] addInfo = addList.get(i);
                if (addInfo[1] == 1) {
                    System.out.println("(" + servers.get(addInfo[0]).getActualId() + ", A)");
                } else if (addInfo[1] == 2) {
                    System.out.println("(" + servers.get(addInfo[0]).getActualId() + ", B)");
                } else {
                    System.out.println("(" + servers.get(addInfo[0]).getActualId() + ")");
                }
            }

        }
        else{
            // 1.purchase
            sb.append("(purchase, " + purchaseList.size() + ")\n");
            int purchaseNum = 0;
            for (String serverType : purchaseList.keySet()) {
                sb.append("(" + serverType + ", " + purchaseList.get(serverType) + ")\n");
                purchaseNum += purchaseList.get(serverType);
            }
            int count = 0;
            for (String serverType : purchaseList.keySet()) {
                for (int i = servers.size() - purchaseNum; i < servers.size(); i++) {
                    if (servers.get(i).getType().equals(serverType)) {
                        servers.get(i).setActualId(servers.size() - purchaseNum + count);
                        count++;
                    }
                }
            }
            if (migrationList.size() > 0) {
                sb.append("(migration, " + migrationList.size() + ")\n");
                for (Map.Entry<String, int[]> tmp : migrationList.entrySet()) {
                    String vmId = tmp.getKey();
                    int[] info = tmp.getValue();
                    int serverId = servers.get(info[0]).getActualId();
                    int node = info[1];
                    if (node == 0) {
                        sb.append("(" + vmId + ", " + serverId + ")\n");
                    } else if (node == 1) {
                        sb.append("(" + vmId + ", " + serverId + ", A)\n");
                    } else if (node == 2) {
                        sb.append("(" + vmId + ", " + serverId + ", B)\n");
                    } else {
                        System.err.println(node);
                    }
                }
            } else {
                sb.append("(migration, 0)\n");
            }
            // 3.deploy
            for (int i = 0; i < addList.size(); i++) {
                int[] addInfo = addList.get(i);
                if (addInfo[1] == 1) {
                    sb.append("(" + servers.get(addInfo[0]).getActualId() + ", A)\n");
                } else if (addInfo[1] == 2) {
                    sb.append("(" + servers.get(addInfo[0]).getActualId() + ", B)\n");
                } else {
                    sb.append("(" + servers.get(addInfo[0]).getActualId() + ")\n");
                }
            }
        }

    }

    public void output(){
        Log.start(new File(param.path_result_sol+"/plot_sol_"+fName), false);
        Log.write(sb.toString());
        Log.end();
    }
}
