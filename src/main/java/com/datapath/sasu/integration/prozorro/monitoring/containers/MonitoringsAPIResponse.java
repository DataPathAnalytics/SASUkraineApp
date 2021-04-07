package com.datapath.sasu.integration.prozorro.monitoring.containers;

import com.datapath.sasu.integration.prozorro.containers.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MonitoringsAPIResponse {

    private List<MonitoringAPI> data;
    @JsonProperty("next_page")
    private Page nextPage;

}
