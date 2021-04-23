package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ResourcesDynamicsResponse {

    private long totalMonitoringTenderPercent;
    private int totalMonitoringTenders;
    private long monitoringTenderPercent;

    private List<DynamicTender> dynamicTenders;
    private List<DynamicAuditor> dynamicAuditors;
    private List<DynamicProductivity> dynamicProductivity;

    @Data
    public static class DynamicTender {
        private String date;
        private Integer tendersCount;
        private Long amount;
    }

    @Data
    public static class DynamicAuditor {
        private String date;
        private Integer count;
    }

    @Data
    public static class DynamicProductivity {
        private String date;
        private Double tendersCount;
        private Double amount;
    }

}
