package com.huawei.java.main.common;

public class Node {
    private int cpu;
    private int mem;
    private int cpuUsed;
    private int memUsed;

    public Node(int cpu, int mem) {
        this.cpu = cpu;
        this.mem = mem;
        this.cpuUsed = 0;
        this.memUsed = 0;

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

    public int getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(int cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public int getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(int memUsed) {
        this.memUsed = memUsed;
    }
}
