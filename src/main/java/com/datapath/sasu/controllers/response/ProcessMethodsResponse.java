package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessMethodsResponse {

    private Double procedureMonitoringCoverage;
    private Double monitoringProcuringEntities;

    private Integer tendersCount;
    private Double tendersAmount;
    private Integer procuringEntityCount;
    private List<LocalMethod> localMethods;
    private List<MonitoringDynamic> monitoringDynamics;

    @Data
    public static class LocalMethod {
        private String name;
        private Integer tendersCount;
        private Integer tendersCountPercent;
        private Double amount;
        private Integer amountPercent;
    }

    @Data
    public static class MonitoringDynamic {
        private String date;
        private Integer tendersCount;
        private Double amount;
    }
}
