package com.datapath.sasu.integration.prozorro.monitoring.containers;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ConclusionAPI {

    private List<String> violationType;
    private ZonedDateTime datePublished;
    private ZonedDateTime date;

}
