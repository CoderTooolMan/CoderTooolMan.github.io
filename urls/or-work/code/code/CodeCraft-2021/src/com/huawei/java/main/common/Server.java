package com.huawei.java.main.common;


import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private int id;  // 在缓存列表中的位置
    private int actualId;
    private String type;
    private int totalCpu;
    private int totalMem;
    private Node nodeA;
    private Node nodeB;
    private int deviceCost;
    private int energyCost;
    private HashMap<String, VM> nodeAVms;
    private HashMap<String, VM> nodeBVms;
    private HashMap<String, VM> bothNodeVms;
    private int vmNum;
    int moved;
    private boolean status;


    public Server(String type, int totalCpu, int totalMem, int deviceCost, int energyCost) {
        this.type = type;
        this.totalCpu = totalCpu;
        this.totalMem = totalMem;
        this.deviceCost = deviceCost;
        this.energyCost = energyCost;
        this.nodeA = new Node(totalCpu / 2, totalMem / 2);
        this.nodeB = new Node(totalCpu / 2, totalMem / 2);
        this.nodeAVms = new HashMap<String, VM>();
        this.nodeBVms = new HashMap<String, VM>();
        this.bothNodeVms = new HashMap<String, VM>();
        this.moved=0;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public int getmoved() {
        return moved;
    }

    public void setmoved(int moved) {
        this.moved = moved;
    }

    public int getVmNum() {
        return vmNum;
    }

    public void setVmNum(int vmNum) {
        this.vmNum = vmNum;
    }

    public HashMap<String, VM> getNodeAVms() {
        return nodeAVms;
    }

    public void setNodeAVms(HashMap<String, VM> nodeAVms) {
        this.nodeAVms = nodeAVms;
    }

    public HashMap<String, VM> getNodeBVms() {
        return nodeBVms;
    }

    public void setNodeBVms(HashMap<String, VM> nodeBVms) {
        this.nodeBVms = nodeBVms;
    }

    public HashMap<String, VM> getBothNodeVms() {
        return bothNodeVms;
    }

    public void setBothNodeVms(HashMap<String, VM> bothNodeVms) {
        this.bothNodeVms = bothNodeVms;
    }

    public int getActualId() {
        return actualId;
    }

    public void setActualId(int actualId) {
        this.actualId = actualId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalCpu() {
        return totalCpu;
    }

    public void setTotalCpu(int totalCpu) {
        this.totalCpu = totalCpu;
    }

    public int getTotalMem() {
        return totalMem;
    }

    public void setTotalMem(int totalMem) {
        this.totalMem = totalMem;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public void setNodeA(Node nodeA) {
        this.nodeA = nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public void setNodeB(Node nodeB) {
        this.nodeB = nodeB;
    }

    public int getDeviceCost() {
        return deviceCost;
    }

    public void setDeviceCost(int deviceCost) {
        this.deviceCost = deviceCost;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }


    public int getNodeACpuLeft() {
        return nodeA.getCpu() - nodeA.getCpuUsed();
    }

    public int getNodeAMemLeft() {
        return nodeA.getMem() - nodeA.getMemUsed();
    }

    public int getNodeBCpuLeft() {
        return nodeB.getCpu() - nodeB.getCpuUsed();
    }

    public int getNodeBMemLeft() {
        return nodeB.getMem() - nodeB.getMemUsed();
    }

    public int getNodeACpuUsed() {
        return nodeA.getCpuUsed();
    }

    public int getNodeAMemUsed() {
        return nodeA.getMemUsed();
    }

    public int getNodeBCpuUsed() {
        return nodeB.getCpuUsed();
    }

    public int getNodeBMemUsed() {
        return nodeB.getMemUsed();
    }

    public void updateNodeACpuUsed(int change) {
        this.nodeA.setCpuUsed(this.nodeA.getCpuUsed() + change);
    }

    public void updateNodeAMemUsed(int change) {
        this.nodeA.setMemUsed(this.nodeA.getMemUsed() + change);
    }

    public void updateNodeBCpuUsed(int change) {
        this.nodeB.setCpuUsed(this.nodeB.getCpuUsed() + change);
    }

    public void updateNodeBMemUsed(int change) {
        this.nodeB.setMemUsed(this.nodeB.getMemUsed() + change);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceLeft() {
        return getNodeACpuLeft() + getNodeAMemLeft() + getNodeBCpuLeft() + getNodeBMemLeft();
    }

    public int getNodeAResourceUsed() {
        return getNodeACpuUsed() + getNodeAMemUsed();
    }

    public int getNodeBResourceUsed() {
        return getNodeBCpuUsed() + getNodeBMemUsed();
    }

    public int getNodeAResourceLeft() {
        return getNodeACpuLeft() + getNodeAMemLeft();
    }

    public int getNodeBResourceLeft() {
        return getNodeBCpuLeft() + getNodeBMemLeft();
    }

    public int getCpuUsed() {
        return nodeA.getCpuUsed() + nodeB.getCpuUsed();
    }

    public int getMemUsed() {
        return nodeA.getMemUsed() + nodeB.getMemUsed();
    }

    public int getCpuLeft() {
        return getNodeACpuLeft() + getNodeBCpuLeft();
    }

    public int getMemLeft() {
        return getNodeAMemLeft() + getNodeBMemLeft();
    }

    public boolean isFull() {
        return !(getCpuUsed() <= totalCpu && getMemUsed() <= totalMem);
    }

    public boolean availableForDoubleVm(VM vm) {
        if ((getNodeACpuLeft() >= vm.getCpu() / 2 && getNodeAMemLeft() >= vm.getMem() / 2)
                && (getNodeBCpuLeft() >= vm.getCpu() / 2 && getNodeBMemLeft() >= vm.getMem() / 2)) {
            // 两个节点都满足，返回true
            return true;
        }
        return false;
    }

    public int availableForSingleVm(VM vm) {
        boolean A = getNodeACpuLeft() >= vm.getCpu() && getNodeAMemLeft() >= vm.getMem();
        boolean B = getNodeBCpuLeft() >= vm.getCpu() && getNodeBMemLeft() >= vm.getMem();
        // 单节点部署需要判断两个节点, 返回信息【0:两个节点都不可行，1：A节点可行，2: B节点可行；3：两个节点都可行】
        if (A && B) {
            return 3;
        } else if (A) {
            return 1;
        } else if (B) {
            return 2;
        } else {
            return 0;
        }
    }

    public boolean availableForVm(VM vm) {
        return (availableForDoubleVm(vm) || availableForSingleVm(vm) != 0);
    }

    public void deployVm(int node, VM vm, ArrayList<int[]> addList) {
        if (vm.isDoubleDeployed()) {
            // 双节点部署

            // 更新服务器状态
            updateNodeACpuUsed(vm.getCpu() / 2);
            updateNodeAMemUsed(vm.getMem() / 2);
            updateNodeBCpuUsed(vm.getCpu() / 2);
            updateNodeBMemUsed(vm.getMem() / 2);

            // 更新服务器上的vm
            bothNodeVms.put(vm.getId(), vm);
            vmNum += 1;
        } else {
            // 单节点部署
            // 更新服务器状态
            if (node == 1) {
                updateNodeACpuUsed(vm.getCpu());
                updateNodeAMemUsed(vm.getMem());
                // 更新服务器伤的vm
                nodeAVms.put(vm.getId(), vm);
                vmNum += 1;
            } else if (node == 2) {
                updateNodeBCpuUsed(vm.getCpu());
                updateNodeBMemUsed(vm.getMem());

                // 更新服务器上的vm
                nodeBVms.put(vm.getId(), vm);
                vmNum += 1;
            }
        }
        // 更新vm中的信息
        vm.setServerId(getId());
        vm.setDeployNode(node);

        // 输出相关：记录部署信息
        int[] addInfo = new int[2];
        addInfo[0] = getId();
        addInfo[1] = node;
        addList.add(addInfo);
    }

    public void unDeployVm(int node, VM vm) {
        if (vm.isDoubleDeployed()) {
            // 双节点部署
            // 更新服务器状态
            updateNodeACpuUsed(-vm.getCpu() / 2);
            updateNodeAMemUsed(-vm.getMem() / 2);
            updateNodeBCpuUsed(-vm.getCpu() / 2);
            updateNodeBMemUsed(-vm.getMem() / 2);
            // 更新服务器上的vm
            bothNodeVms.remove(vm.getId());
            vmNum -= 1;
        } else {
            // 单节点部署
            // 更新服务器状态
            if (node == 1) {
                updateNodeACpuUsed(-vm.getCpu());
                updateNodeAMemUsed(-vm.getMem());
                // 更新服务器上的vm
                nodeAVms.remove(vm.getId());
                vmNum -= 1;
            } else if (node == 2) {
                updateNodeBCpuUsed(-vm.getCpu());
                updateNodeBMemUsed(-vm.getMem());
                // 更新服务器上的vm
                nodeBVms.remove(vm.getId());
                vmNum -= 1;
            }
        }
    }

    public boolean hasVm(VM vm) {
        if (vm.isDoubleDeployed()) {
            return bothNodeVms.containsKey(vm.getId());
        } else {
            if (nodeAVms.containsKey(vm.getId())) {
                return true;
            }
            return nodeBVms.containsKey(vm.getId());
        }
    }

    public int getResourceUsed() {
        return getNodeAResourceUsed() + getNodeBResourceUsed();
    }

    public float getResourceUseRatio() {
        return (float) (getCpuUsed() / totalCpu + getMemUsed() / totalMem);
    }
}
