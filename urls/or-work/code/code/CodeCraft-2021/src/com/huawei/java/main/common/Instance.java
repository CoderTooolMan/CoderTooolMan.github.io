package com.huawei.java.main.common;

import com.huawei.java.main.Main;
import com.huawei.java.main.strategy.bestserver;
import com.huawei.java.main.strategy.top5;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Instance {

    private HashMap<String, int[]> serverInfo;
    private HashMap<String, int[]> vmInfo;
    private ArrayList<ArrayList<String[]>> requestInfo;
    private List<Map.Entry<String, Double>> sortedServerInfo;

    // --------------------------------------
    // -------- Getters and Setters ---------
    // --------------------------------------

    public List<Map.Entry<String, Double>> getSortedServerInfo() {
        return sortedServerInfo;
    }

    public void setSortedServerInfo(List<Map.Entry<String, Double>> sortedServerInfo) {
        this.sortedServerInfo = sortedServerInfo;

    }
    public void setServerInfo(HashMap<String, int[]> serverInfo) {
        this.serverInfo = serverInfo;
    }

    public HashMap<String, int[]> getServerInfo() {
        return serverInfo;
    }

    public HashMap<String, int[]> getVmInfo() {
        return vmInfo;
    }

    public void setVmInfo(HashMap<String, int[]> vmInfo) {
        this.vmInfo = vmInfo;
    }

    public ArrayList<ArrayList<String[]>> getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(ArrayList<ArrayList<String[]>> requestInfo) {
        this.requestInfo = requestInfo;
    }

    // ----------------------------------------
    // -------------- Methods -----------------
    // ----------------------------------------

    public void readFile(Cache cache,File file) throws IOException {
        Scanner sc;
        if (cache.param.version.equals("release")) {
            sc = new Scanner(System.in);
        } else {
            sc = new Scanner(file);
        }

        // 解析服务器信息
        int N = Integer.parseInt(sc.nextLine());
        HashMap<String, int[]> serverInfo = new HashMap<>();
        for (int i = 0; i < N; i++) {
            String server = sc.nextLine();
            server = server.substring(1, server.length() - 1);
            String[] sp = server.split(", ");

            int[] info = new int[5];
            info[0] = Integer.parseInt(sp[1]);
            info[1] = Integer.parseInt(sp[2]);
            info[2] = Integer.parseInt(sp[3]);
            info[3] = Integer.parseInt(sp[4]);
            //info[4] = info[2]/(info[1]+info[0]);
            serverInfo.put(sp[0], info);
        }
        setServerInfo(serverInfo);


        // 按照服务器固定成本对其进行排序
        Map<String, Double> tmpServer = new HashMap<>();
        for (String key : serverInfo.keySet()) {
            double cost = (double) serverInfo.get(key)[2];
//            double pe = serverInfo.get(key)[0]+serverInfo.get(key)[1];
            tmpServer.put(key, cost);
//            tmpServer.put(key, pe/cost);
        }
        List<Map.Entry<String, Double>> sortedServerInfo = new ArrayList<>(tmpServer.entrySet());
        Collections.sort(sortedServerInfo, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        //Collections.sort(sortedServerInfo,new bestserver());
        setSortedServerInfo(sortedServerInfo);


        // 解析VM信息
        int M = Integer.parseInt(sc.nextLine());
        HashMap<String, int[]> vmInfo = new HashMap<>();
        for (int i = 0; i < M; i++) {
            String VM = sc.nextLine();
            VM = VM.substring(1, VM.length() - 1);
            String[] sp = VM.split(", ");

            int[] info = new int[3];
            info[0] = Integer.parseInt(sp[1]);
            info[1] = Integer.parseInt(sp[2]);
            info[2] = Integer.parseInt(sp[3]);
            vmInfo.put(sp[0], info);
        }
        setVmInfo(vmInfo);


        // 解析请求信息
        HashMap<String, VM> vms = new HashMap<>();
        int T = Integer.parseInt(sc.nextLine());
        int remainVms = 0;

        ArrayList<ArrayList<String[]>> requestInfo = new ArrayList<>();
        for (int i = 0; i < T; i++) {
            int R = Integer.parseInt(sc.nextLine());
            ArrayList<String[]> day = new ArrayList<>();
            for (int j = 0; j < R; j++) {
                String request = sc.nextLine();
                request = request.substring(1, request.length() - 1);
                String[] sp = request.split(", ");

                if (sp[0].equals("add")) {
                    day.add(sp);
                    int doubleDeploy = vmInfo.get(sp[1])[2];
                    boolean vmDoubleDeploy = false;
                    if (doubleDeploy == 1) {
                        vmDoubleDeploy = true;
                    }
                    VM vm = new VM(sp[2], sp[1], vmInfo.get(sp[1])[0], vmInfo.get(sp[1])[1], vmDoubleDeploy);
                    cache.vms.put(sp[2], vm);
                    remainVms += 1;

                } else {
                    String[] req = new String[3];
                    req[0] = sp[0];
                    req[1] = null;
                    req[2] = sp[1];
                    day.add(req);
                    cache.vms.get(sp[1]).deadDay = i;  // 设置vm的del时间
                    remainVms -= 1;
                }
            }
            requestInfo.add(day);
            cache.remainVmNum.add(remainVms);
        }
        setRequestInfo(requestInfo);

    }
}
