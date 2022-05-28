package com.huawei.java.main.common;

public class VM {

    private String id;
    private String type;
    private int cpu;
    private int mem;
    private boolean doubleDeployed;
    private int serverId;
    private int deployNode;   // 0 double, 1 nodeA, 2 nodeB
    public int deadDay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getMem() {
        return mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }

    public boolean isDoubleDeployed() {
        return doubleDeployed;
    }

    public void setDoubleDeployed(boolean doubleDeployed) {
        this.doubleDeployed = doubleDeployed;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getDeployNode() {
        return deployNode;
    }

    public void setDeployNode(int deployNode) {
        this.deployNode = deployNode;
    }

    public VM(String id, String type, int cpu, int mem, boolean doubleDeployed) {
        this.id = id;
        this.type = type;
        this.cpu = cpu;
        this.mem = mem;
        this.doubleDeployed = doubleDeployed;
    }
}
