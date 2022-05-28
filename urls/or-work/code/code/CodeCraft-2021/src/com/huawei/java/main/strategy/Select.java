package com.huawei.java.main.strategy;

import com.huawei.java.main.common.Cache;
import com.huawei.java.main.common.Server;
import com.huawei.java.main.common.VM;

import java.util.ArrayList;

public class Select {

    public static float cosine(int[] v1, int[] v2) {
        int top = 0;
        float bottom1 = 0;
        float bottom2 = 0;
        for (int i = 0; i < v1.length; i++) {
            top += v1[i] * v2[i];
            bottom1 += v1[i] * v1[i];
            bottom2 += v2[i] * v2[i];
        }

        return (top) / (float) (Math.sqrt(bottom1) * Math.sqrt(bottom2));
    }

    public static int[] selectServerForVm(VM vm, ArrayList<Server> servers) {
        int[] bestServer = new int[]{-1, -1};
        if (vm.isDoubleDeployed()) {
            return getBestServerForDoubleVm_1(vm, servers, bestServer);
        } else {
            return getBestServerForSingleVm_4(vm, servers, bestServer);
        }
    }
    public static int[] selectServerFormigVm(VM vm, ArrayList<Server> servers,Server server) {
        int[] bestServer = new int[]{-1, -1};
        if (vm.isDoubleDeployed()) {
            return getBestServerFormigDoubleVm_1(vm, servers, bestServer,server);
        } else {
            return getBestServerFormigSingleVm_4(vm, servers, bestServer,server);
        }
    }

    // ==================================================双节点==========================================================

    /**
     * 双节点：使服务器的剩余资源最小
     *
     * @param vm
     * @param servers
     * @param bestServer
     * @return
     */
    public static int[] getBestServerForDoubleVm_1(VM vm, ArrayList<Server> servers, int[] bestServer) {
        int resourceLeft = Integer.MAX_VALUE;
        for (Server server : servers) {

            boolean available = server.availableForDoubleVm(vm);
            if (available) {
                // 可以部署得下
                if ((server.getNodeACpuLeft() == vm.getCpu() / 2 && server.getNodeAMemLeft() == vm.getMem() / 2)
                        && (server.getNodeBCpuLeft() == vm.getCpu() / 2 && server.getNodeBMemLeft() == vm.getMem() / 2)) {
                    // 正好利用完服务器所有资源，是最优的
                    bestServer[0] = server.getId();
                    bestServer[1] = 0;
                    return bestServer;
                } else {
                    // 不能正好利用完服务器所有资源，计算部署后的剩余资源, 选剩余资源小的
                    if (server.getResourceLeft() - vm.getCpu() - vm.getMem() < resourceLeft) {
                        resourceLeft = server.getResourceLeft() -vm.getCpu() - vm.getMem();
                        bestServer[0] = server.getId();
                        bestServer[1] = 0;
                    }
                }
            }
        }
        return bestServer;
    }
    public static int[] getBestServerFormigDoubleVm_1(VM vm, ArrayList<Server> servers, int[] bestServer,Server old) {
        int resourceLeft = Integer.MAX_VALUE;
        for (Server server : servers) {
            if(old.getId()!=server.getId()) {
                if (!server.getType().equals(old.getType()))
                    continue;
            }
            boolean available = server.availableForDoubleVm(vm);
            if (available) {
                // 可以部署得下
                if ((server.getNodeACpuLeft() == vm.getCpu() / 2 && server.getNodeAMemLeft() == vm.getMem() / 2)
                        && (server.getNodeBCpuLeft() == vm.getCpu() / 2 && server.getNodeBMemLeft() == vm.getMem() / 2)) {
                    // 正好利用完服务器所有资源，是最优的
                    bestServer[0] = server.getId();
                    bestServer[1] = 0;
                    return bestServer;
                } else {
                    // 不能正好利用完服务器所有资源，计算部署后的剩余资源, 选剩余资源小的
                    if (server.getResourceLeft() - vm.getCpu() - vm.getMem() < resourceLeft) {
                        resourceLeft = server.getResourceLeft() -vm.getCpu() - vm.getMem();
                        bestServer[0] = server.getId();
                        bestServer[1] = 0;
                    }
                }
            }
        }
        return bestServer;
    }

    // ==================================================双节点==========================================================


    // ==================================================单节点==========================================================


    /**
     * 单节点：使单节点资源剩余最小
     *
     * @param vm
     * @param servers
     * @param bestServer
     * @return
     */
    public static int[] getBestServerForSingleVm_4(VM vm, ArrayList<Server> servers, int[] bestServer) {
        int resourceLeft = Integer.MAX_VALUE;
        for (Server server : servers) {

            int available = server.availableForSingleVm(vm);
            if (available == 1) {
                // 只有A节点可行
                if (server.getNodeACpuLeft() == vm.getCpu() && server.getNodeAMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                    return bestServer;
                }
                int tmpLeft = server.getNodeACpuLeft() - vm.getCpu() + server.getNodeAMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                }
            } else if (available == 2) {
                // 只有B节点可行
                if (server.getNodeBCpuLeft() == vm.getCpu() && server.getNodeBMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                    return bestServer;
                }
                int tmpLeft = server.getNodeBCpuLeft() - vm.getCpu() + server.getNodeBMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                }
            } else if (available == 3) {
                // 两节点都可行
                // 先尝试A点
                if (server.getNodeACpuLeft() == vm.getCpu() && server.getNodeAMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                    return bestServer;
                }
                int tmpLeft = server.getNodeACpuLeft() - vm.getCpu() + server.getNodeAMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                }
                if (server.getNodeBCpuLeft() == vm.getCpu() && server.getNodeBMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                    return bestServer;
                }
                tmpLeft = server.getNodeBCpuLeft() - vm.getCpu() + server.getNodeBMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                }
            }
        }
        return bestServer;
    }
    public static int[] getBestServerFormigSingleVm_4(VM vm, ArrayList<Server> servers, int[] bestServer,Server old) {
        int resourceLeft = Integer.MAX_VALUE;
        for (Server server : servers) {
            if(old.getId()!=server.getId()) {
                if (!old.getType().equals(server.getType()))
                    continue;
            }
            int available = server.availableForSingleVm(vm);
            if (available == 1) {
                // 只有A节点可行
                if (server.getNodeACpuLeft() == vm.getCpu() && server.getNodeAMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                    return bestServer;
                }
                int tmpLeft = server.getNodeACpuLeft() - vm.getCpu() + server.getNodeAMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                }
            } else if (available == 2) {
                // 只有B节点可行
                if (server.getNodeBCpuLeft() == vm.getCpu() && server.getNodeBMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                    return bestServer;
                }
                int tmpLeft = server.getNodeBCpuLeft() - vm.getCpu() + server.getNodeBMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                }
            } else if (available == 3) {
                // 两节点都可行
                // 先尝试A点
                if (server.getNodeACpuLeft() == vm.getCpu() && server.getNodeAMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                    return bestServer;
                }
                int tmpLeft = server.getNodeACpuLeft() - vm.getCpu() + server.getNodeAMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 1;
                }
                if (server.getNodeBCpuLeft() == vm.getCpu() && server.getNodeBMemLeft() == vm.getMem()) {
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                    return bestServer;
                }
                tmpLeft = server.getNodeBCpuLeft() - vm.getCpu() + server.getNodeBMemLeft() - vm.getMem();
                if (tmpLeft < resourceLeft) {
                    resourceLeft = tmpLeft;
                    bestServer[0] = server.getId();
                    bestServer[1] = 2;
                }
            }
        }
        return bestServer;
    }
    // ==================================================单节点==========================================================

}
