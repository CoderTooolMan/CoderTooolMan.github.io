package com.huawei.java.main.common;

import com.huawei.java.main.strategy.Purchase;
import com.huawei.java.main.strategy.Select;

import java.util.*;
import java.util.Map.Entry;


import static com.huawei.java.main.strategy.Migration.mig;
import static com.huawei.java.main.strategy.top5.*;


public class Solver {


    /**
     * 按顺序处理每天的请求 (每日输出)
     *
     * @param instance
     * @param cache
     */
    public void initialize(Instance instance, Cache cache) {
        ArrayList<ArrayList<String[]>> requestInfo = instance.getRequestInfo();
        //HashMap<String, int[]> vmInfo = instance.getVmInfo();
        HashMap<String, int[]> serverInfo = instance.getServerInfo();
        List<Entry<String, Double>> sortedServerInfo = instance.getSortedServerInfo();

        HashMap<String, VM> vms = cache.vms;
        ArrayList<Server> servers = cache.servers;
        //quickSort(serverInfo);
        int day=0;
//        int stop;     //测试用
        for (ArrayList<String[]> dayRequest : requestInfo) {
            // 输出的记录
            HashMap<String, Integer> purchaseList = new HashMap<>();
            HashMap<String, int[]> migrationList = new HashMap<>();
            ArrayList<int[]> addList = new ArrayList<>();
            ArrayList<int[]> tmpadd = new ArrayList<>();
            mig(serverInfo, sortedServerInfo, servers, migrationList);
            // 遍历每天的请求
            //ap<String, Double> sortedreq = new HashMap<>();
            ArrayList<String[]> sortedreq = new ArrayList<>();
            HashMap<String,ArrayList<int[]>> tmpaddlist = new HashMap<>();
            //ArrayList<String[]> req = new ArrayList<String[]>();
            int insert = 0;
            int start=0, end = 0;
            int testday;
            if(requestInfo.size()>900)
                testday=day%10;
            else  testday=day%5;//best 5，%=1
            if(testday==1) {
                for (String[] request : dayRequest) {
                    String vmid = request[2];
//               if(vmid.equals("409440991"))
//                    te=0;     //test
                    String re = request[0];
                    if (re.equals("add")) {
                        sortedreq.add(request);
                        end++;
                        insert++;
                    } else {
                        if (insert == 0) {
                            sortedreq.add(request);
                            end++;
                            continue;
                        }
                        start = end - insert;
                        quicksort(sortedreq, start, end, vms);
                        sortedreq.add(request);
                        insert = 0;
                        end++;
                        //start=end+2;
                    }
                }
                start = end - insert;
                quicksort(sortedreq, start, end, vms);
                //对需求排序
                for (String[] request : sortedreq) {
                    if (request[0].equals("add")) {
                        // 添加请求
                        // 从cache中拿一个vm
                        String vmId = request[2];
                        VM vm = vms.get(vmId);
                        // 从cache中选择合适server, 返回在 [cache中的index, 部署的节点]
                        int[] bestServer = Select.selectServerForVm(vm, servers);
//                    Server serverToDeployVm = null;
                        if (bestServer[0] == -1) {
                            // 当前没有适合的server， 需要购买, 并加入cache，返回在 [cache中的index, 部署的节点]
                            bestServer = Purchase.purchaseBestServerForVm(vm, serverInfo, sortedServerInfo, servers, purchaseList);
                        }
                        // 将Vm部署到server上并更新信息
                        Server serverToDeployVm = servers.get(bestServer[0]);
                        serverToDeployVm.deployVm(bestServer[1], vm, tmpadd);
                        cache.vmAlive.add(vm);
                    }
                    else if(request[0].equals("del")) {

                        String vmId = request[2];
                        VM vm = vms.get(vmId);
                        Server server = servers.get(vm.getServerId());
                        server.unDeployVm(vm.getDeployNode(), vm);
                        int[] a = {-1,-1};
                        tmpadd.add(a);
                    }
                }
                    for (int i = 0; i < dayRequest.size(); i++) {
                        {
                            if(!dayRequest.get(i)[0].equals("del")) {
                                for (int j = 0; j < sortedreq.size(); j++) {
//                                    if (day == 2)
//                                        te = 1;       //test
                                    if (dayRequest.get(i)[2].equals(sortedreq.get(j)[2])) {
                                        addList.add(tmpadd.get(j));
                                        break;
                                    }
                                }
                            }
                        }
                    }
            }
            else {
                for (String[] request : dayRequest) {
                    if (request[0].equals("add")) {
                        // 添加请求

                        // 从cache中拿一个vm
                        String vmId = request[2];
                        VM vm = vms.get(vmId);
                        // 从cache中选择合适server, 返回在 [cache中的index, 部署的节点]
                        int[] bestServer = Select.selectServerForVm(vm, servers);
//                    Server serverToDeployVm = null;
                        if (bestServer[0] == -1) {
                            // 当前没有适合的server， 需要购买, 并加入cache，返回在 [cache中的index, 部署的节点]
                            bestServer = Purchase.purchaseBestServerForVm(vm, serverInfo, sortedServerInfo, servers, purchaseList);
                        }
                        // 将Vm部署到server上并更新信息
                        Server serverToDeployVm = servers.get(bestServer[0]);
                        serverToDeployVm.deployVm(bestServer[1], vm, addList);
                        //tmpaddlist.put(request[2],tmpadd);
                        cache.vmAlive.add(vm);
//                    if(serverToDeployVm.getNodeBMemLeft()<0||serverToDeployVm.getNodeAMemLeft()<0||serverToDeployVm.getNodeACpuLeft()<0||serverToDeployVm.getNodeBCpuLeft()<0)
//                        stop=1;
                    } else if (request[0].equals("del")) {
                        // 删除请求
                        // 将vm从服务器上卸载并更新相关信息
                        String vmId = request[2];
                        VM vm = vms.get(vmId);
                        Server server = servers.get(vm.getServerId());
                        server.unDeployVm(vm.getDeployNode(), vm);
                    }
                }
            }
//            for(String[] request : dayRequest) {
//                for (String key : tmpaddlist.keySet()) {
//                    if (Objects.equals(key, request[2])) {
//                        addList.add(tmpaddlist.get(key).get(0));
//                        break;
//                    }
//                }
//            }
            // 输出
            cache.outputDay(purchaseList,migrationList,addList);
            day++;
        }

    }
}

