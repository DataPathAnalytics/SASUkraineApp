package com.datapath.sasu.integration;

import com.datapath.sasu.integration.prozorro.monitoring.MonitoringLoader;
import com.datapath.sasu.integration.prozorro.tendering.FeedLoader;
import com.datapath.sasu.integration.prozorro.tendering.TenderingLoader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import static com.datapath.sasu.Constants.MONITORING_START;

@Component
@AllArgsConstructor
@Slf4j
public class IntegrationRunner {

    private TenderingLoader tenderingLoader;
    private FeedLoader feedLoader;
    private MonitoringLoader monitoringLoader;

    @Autowired
    private ThreadPoolTaskExecutor applicationTaskExecutor;

//    @Scheduled(fixedDelay = 43_200_000)
//    public void loadOldTenders() {
//
//        int queueSize = applicationTaskExecutor.getThreadPoolExecutor().getQueue().size();
//        if (queueSize < 300) {
//            feedLoader.load();
//            monitoringLoader.load(MONITORING_START.toLocalDateTime());
//        }
//    }

//    @Scheduled(fixedDelay = 20_000)
//    public void printQueue() {
//        log.info("Queue size {}", applicationTaskExecutor.getThreadPoolExecutor().getQueue().size());
//    }

    @Scheduled(fixedDelay = 600_000)
    public void loadNewTenders() {
        tenderingLoader.load();
        monitoringLoader.loadLastModified();
    }

}
