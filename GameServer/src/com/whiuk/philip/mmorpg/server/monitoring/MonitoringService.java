package com.whiuk.philip.mmorpg.server.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.network.NetworkService;

@Service
public class MonitoringService {
    private MemoryMXBean memBean;
    private MemoryUsage heap;
    private MemoryUsage nonHeap;
    private NetworkService networkService;

    public MonitoringService() {
        memBean = ManagementFactory.getMemoryMXBean();
        heap = memBean.getHeapMemoryUsage();
        nonHeap = memBean.getNonHeapMemoryUsage();
    }

    public long getInitHeap() {
        return heap.getInit();
    }

    public long getUsedHeap() {
        return heap.getUsed();
    }

    public long getCommittedHeap() {
        return heap.getUsed();
    }

    public long getMaxHeap() {
        return heap.getUsed();
    }

    public long getInitNonHeap() {
        return nonHeap.getInit();
    }

    public long getUsedNonHeap() {
        return nonHeap.getUsed();
    }

    public long getCommittedNonHeap() {
        return nonHeap.getUsed();
    }

    public long getMaxNonHeap() {
        return nonHeap.getUsed();
    }
    
    public long getConnections() {
        return networkService.getConnectionCount();
    }
}
