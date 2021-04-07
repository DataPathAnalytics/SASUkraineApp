package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ResourcesDASUGeographyResponse {

    private Integer totalAuditorsCount;
    private Integer auditorsCount;
    private List<RegionAuditors> auditorsCountByRegion;
    private List<MonthAuditors> auditorsCountByMonth;

}
