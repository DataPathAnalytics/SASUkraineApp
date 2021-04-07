package com.datapath.sasu.integration;

import com.datapath.sasu.dao.entity.Monitoring;
import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;

public interface Converter {

    Tender convert(TenderAPI tenderAPI);

    Monitoring convert(MonitoringAPI monitoringAPI);
}
