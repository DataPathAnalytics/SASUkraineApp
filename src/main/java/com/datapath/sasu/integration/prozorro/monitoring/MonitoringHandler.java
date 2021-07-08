package com.datapath.sasu.integration.prozorro.monitoring;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.Monitoring;
import com.datapath.sasu.integration.Converter;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPI;
import com.datapath.sasu.integration.prozorro.tendering.TenderHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Component
@Slf4j
public class MonitoringHandler {

    public static final String DRAFT = "draft";

    @Autowired
    private Converter converter;

    @Autowired
    private DAO dao;

    @Autowired
    private TenderHandler tenderHandler;

    @Transactional
    public void handle(MonitoringAPI monitoringAPI) {
        if (!DRAFT.equals(monitoringAPI.getStatus())) {
            try {
                tenderHandler.handleBindToMonitoring(monitoringAPI.getTenderId());
                var monitoringEntity = converter.convert(monitoringAPI);
                dao.save(monitoringEntity);
            } catch (EntityNotFoundException ex) {
                log.warn(ex.getMessage());
            }
        }
    }

}
