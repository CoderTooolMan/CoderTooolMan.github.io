package com.huawei.java.main.strategy;

import java.util.*;

import com.huawei.java.main.common.Server;
import com.huawei.java.main.common.VM;
public class Migration {

    public static void mig(HashMap<String, int[]> serverInfo,
                           List<Map.Entry<String, Double>> sortedServerInfo,
                           ArrayList<Server> servers, HashMap<String, int[]> migrationlist)
    {
        int stop;
        int times = servers.size()/200;
        if(times == 0) return;
        ArrayList<VM> vmmig = new ArrayList<VM>();    //需要迁移的信息
        ArrayList<String> vmmigid = new ArrayList<String>();    //虚拟机id
        ArrayList<Server> tmp = new ArrayList<Server>();
        ArrayList<int[]> migList = new ArrayList<>();
        ArrayList<int[]> nomigList = new ArrayList<>();
        for(int i = 0; i <servers.size(); i++)
            tmp.add(servers.get(i));
        Collections.sort(tmp,new top5());
        int move = 0;
        for(int i = 0; i < tmp.size()&& move < 15*times; i++) {//8,60753,50827;15,60538,50865;14,60563,50845;13 60601,50807
            int id = tmp.get(i).getId();
            if(tmp.get(i).getMemUsed() == 0 &&tmp.get(i).getCpuUsed()==0) continue;
            int n = 1;
            for(int VMnum = 0; VMnum < tmp.get(i).getVmNum(); VMnum++) {
                // if(deleteDay[VMID] == day) continue;

                Server j =tmp.get(i);
                if(j.getNodeAVms().size()!=0) {
                    Set<String> set = j.getNodeAVms().keySet();
                    Iterator<String> it = set.iterator();
                    if (it.hasNext()) {
                        vmmigid.add(it.next());
                        vmmig.add(j.getNodeAVms().get(vmmigid.get(move)));
                        move++;
                    }
                }
                else if(j.getNodeBVms().size()!=0)
                {
                    Set<String> set = j.getNodeBVms().keySet();
                    Iterator<String> it = set.iterator();
                    if (it.hasNext()) {
                        vmmigid.add(it.next());
                        vmmig.add(j.getNodeBVms().get(vmmigid.get(move)));
                        move++;
                    }
                }
                else
                {
                    Set<String> set = j.getBothNodeVms().keySet();
                    Iterator<String> it = set.iterator();
                    if (it.hasNext()) {
                        vmmigid.add(it.next());
                        vmmig.add(j.getBothNodeVms().get(vmmigid.get(move)));
                        move++;
                    }
                }
                //vmmig.add(test.get(vmmigid));
                n--;
                if(n <= 0) break;
            }
        }
        int k=0;
        int moved = 0;
        for(int i = 0; i < vmmig.size(); i++)
        {
            if(moved>times)
                return;
            String VMID = vmmigid.get(i);
            int serID = vmmig.get(i).getServerId();
            VM migvm = vmmig.get(i);
            Server server = servers.get(serID);
            int node = migvm.getDeployNode();
            server.unDeployVm(migvm.getDeployNode(),migvm);

            int[] bestServer = Select.selectServerFormigVm(migvm, servers,server);
            Server serverToDeployVm;
            if (bestServer[0] == serID||servers.get(bestServer[0]).getVmNum()==0||server.getmoved()==1) {
                k++;
                server.deployVm(node, migvm, migList);
//                if(server.getNodeBMemLeft()<0||server.getNodeAMemLeft()<0||server.getNodeACpuLeft()<0||server.getNodeBCpuLeft()<0)
//                    stop=1;
            }
            else {
                serverToDeployVm = servers.get(bestServer[0]);
                if(serverToDeployVm.getmoved()==1)
                {
                    k++;
                    server.deployVm(node, migvm, migList);
                }
                else {
                    serverToDeployVm.deployVm(bestServer[1], migvm, migList);
                    migrationlist.put(migvm.getId(), migList.get(i));
                    serverToDeployVm.setmoved(1);
                    server.setmoved(1);
                    moved++;
                }
//                if(migvm.getId()=="862046174")
//                    stop=1;
//                if(serverToDeployVm.getNodeBMemLeft()<0||serverToDeployVm.getNodeAMemLeft()<0||serverToDeployVm.getNodeACpuLeft()<0||serverToDeployVm.getNodeBCpuLeft()<0)
//                        stop=1;
            }
        }
    }
};

