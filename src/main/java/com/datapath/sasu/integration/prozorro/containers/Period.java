package com.datapath.sasu.integration.prozorro.containers;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Period {

    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

}
