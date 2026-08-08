package magelan.orders.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Slf4j
@Component
public class MemoryUsageLogger {

    private static final long MB =
            1024L * 1024L;

    @Scheduled(
            initialDelay = 30000,
            fixedDelay = 60000
    )
    public void logMemoryUsage() {

        MemoryMXBean memoryMXBean =
                ManagementFactory
                        .getMemoryMXBean();

        MemoryUsage heap =
                memoryMXBean
                        .getHeapMemoryUsage();

        MemoryUsage nonHeap =
                memoryMXBean
                        .getNonHeapMemoryUsage();

        Runtime runtime =
                Runtime.getRuntime();

        log.info(
                "MEMORY | heap used={} MB, heap committed={} MB, heap max={} MB, "
                        + "non-heap used={} MB, non-heap committed={} MB, "
                        + "processors={}",
                heap.getUsed() / MB,
                heap.getCommitted() / MB,
                heap.getMax() / MB,
                nonHeap.getUsed() / MB,
                nonHeap.getCommitted() / MB,
                runtime.availableProcessors()
        );
    }
}