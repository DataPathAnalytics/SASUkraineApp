package com.datapath.sasu.integration.prozorro.monitoring.containers;

import com.datapath.sasu.integration.prozorro.containers.Period;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class MonitoringAPI {

    private String id;
    private ZonedDateTime dateModified;
    @JsonProperty("tender_id")
    private String tenderId;
    private String status;
    private Period monitoringPeriod;

    private ConclusionAPI conclusion;

    private List<Party> parties;
    private List<String> reasons;

}
