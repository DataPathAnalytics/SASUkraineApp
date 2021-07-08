package com.datapath.sasu.integration;

import com.datapath.sasu.integration.prozorro.monitoring.HasMonitoringUpdater;
import com.datapath.sasu.integration.prozorro.monitoring.MonitoringCleaner;
import com.datapath.sasu.integration.prozorro.monitoring.MonitoringDescendingLoader;
import com.datapath.sasu.integration.prozorro.monitoring.MonitoringLoader;
import com.datapath.sasu.integration.prozorro.tendering.FeedLoader;
import com.datapath.sasu.integration.prozorro.tendering.TenderingLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class IntegrationRunner {

    private final TenderingLoader tenderingLoader;
    private final FeedLoader feedLoader;
    private final MonitoringLoader monitoringLoader;
    private final MonitoringDescendingLoader monitoringDescendingLoader;
    private final MonitoringCleaner monitoringCleaner;
    private final HasMonitoringUpdater hasMonitoringUpdater;
    private final ThreadPoolTaskExecutor applicationTaskExecutor;

    @Scheduled(fixedDelay = 43_200_000)
    public void loadOldTenders() {
        int queueSize = applicationTaskExecutor.getThreadPoolExecutor().getQueue().size();
        if (queueSize < 300) {
            feedLoader.load();
            monitoringDescendingLoader.load();
        }
    }

    @Scheduled(fixedDelay = 30_000)
    public void printQueue() {
        log.info("Queue size {}", applicationTaskExecutor.getThreadPoolExecutor().getQueue().size());
    }

    @Scheduled(fixedDelay = 1_200_000)
    public void loadNewTenders() {
        tenderingLoader.load();
        monitoringLoader.loadLastModified();
        monitoringCleaner.clean();
        hasMonitoringUpdater.update();
    }

}
