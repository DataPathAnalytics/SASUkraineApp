package com.datapath.sasu.integration.prozorro.monitoring;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.Monitoring;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MonitoringCleaner {

    public static final String ACTIVE = "active";
    @Autowired
    private DAO dao;

    public void clean() {
        log.info("Started cleaning duplicated monitoring");
        List<Integer> tenders = dao.getTendersWithDuplicatedMonitoring();
        log.info("Found {} tenders with duplicated monitoring", tenders.size());

        tenders.forEach(tenderId -> {
            List<Monitoring> monitorings = dao.getTenderMonitoring(tenderId);
            if (monitorings.size() != 2) throw new RuntimeException("Tender doesn't have 2 monitoring");

            var active = monitorings.stream().filter(m -> m.getResult().equalsIgnoreCase(ACTIVE)).findFirst();
            var notActive = monitorings.stream().filter(m -> !m.getResult().equalsIgnoreCase(ACTIVE)).findFirst();

            if (active.isPresent() && notActive.isPresent()) {

                if (active.get().getDuration() > 15
                        && notActive.get().getStartDate().plusDays(notActive.get().getDuration()).isAfter(active.get().getStartDate())
                ) {
                    log.info("Delete duplicated monitoring [{}] for tender", active.get().getOuterId());
                    dao.delete(active.get());
                }
            }

        });

    }


}
