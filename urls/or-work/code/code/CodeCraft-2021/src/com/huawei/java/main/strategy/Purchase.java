package com.huawei.java.main.strategy;


import com.huawei.java.main.common.Server;
import com.huawei.java.main.common.VM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Purchase {
    //todo more purchase strategy

    public static int[] purchaseBestServerForVm(VM vm, HashMap<String, int[]> serverInfo,
                                                List<Map.Entry<String, Double>> sortedServerInfo,
                                                ArrayList<Server> servers, HashMap<String, Integer> purchaseList) {
        int[] bestServer = new int[2];
        for (Map.Entry<String, Double> server : sortedServerInfo) {
            String serverType = server.getKey();
            int[] info = serverInfo.get(serverType);

            // 判断可行性
            if (vm.isDoubleDeployed()) {
                // 双节点部署
                if (info[0] >= vm.getCpu() && info[1] >= vm.getMem()) {
                    Server newServer = new Server(serverType, info[0], info[1], info[2], info[3]);

                    // 输出相关：记录购买信息
                    purchaseList.put(serverType, purchaseList.getOrDefault(serverType, 0) + 1);

                    // 加入cache
                    servers.add(newServer);
                    newServer.setId(servers.size() - 1);

                    bestServer[0] = newServer.getId();
                    bestServer[1] = 0;
                    return bestServer;

                }
            } else {
                // 单节点部署
                if (info[0] / 2 >= vm.getCpu() && info[1] / 2 >= vm.getMem()) {
                    Server newServer = new Server(serverType, info[0], info[1], info[2], info[3]);

                    // 输出相关：记录购买信息
                    purchaseList.put(serverType, purchaseList.getOrDefault(serverType, 0) + 1);

                    // 加入cache
                    servers.add(newServer);
                    newServer.setId(servers.size() - 1);

                    bestServer[0] = newServer.getId();
                    bestServer[1] = 1;  // 新的单节点部署的vm优先放在A节点

                    return bestServer;
                }

            }

        }

        bestServer[0] = -1;
        bestServer[1] = -1;
        return bestServer;

    }


}
