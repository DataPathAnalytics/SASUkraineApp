package com.datapath.sasu.dao.response;

import lombok.Data;

@Data
public class ProcessCoverageDAOResponse {

    private Double totalAwardsAmount;
    private Integer awardsCount;
    private Double awardsAmount;
    private TendersDistribution tendersDistribution;

    @Data
    public static class TendersDistribution {
        private Integer completeTendersCount;
        private Integer cancelledTendersCount;
        private Integer othersTendersCount;

        private Integer completeProcuringEntityCount;
        private Integer cancelledProcuringEntityCount;
        private Integer othersProcuringEntityCount;

        private Double completeTendersAmount;
        private Double cancelledTendersAmount;
        private Double othersTendersAmount;

        private Integer tendersCount;
        private Double tendersAmount;

        private Integer monitoringTendersCount;
        private Double monitoringTendersAmount;

        private Integer nonMonitoringTendersCount;
        private Double nonMonitoringTendersAmount;

        private Integer activeMonitoringTendersCount;
        private Integer violationMonitoringTendersCount;
        private Integer nonViolationMonitoringTendersCount;
        private Integer cancelledMonitoringTendersCount;

        private Double activeMonitoringAmount;
        private Double violationMonitoringAmount;
        private Double nonViolationMonitoringAmount;
        private Double cancelledMonitoringAmount;

    }

}
