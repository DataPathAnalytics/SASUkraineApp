package com.datapath.sasu.controllers.response;

import lombok.Data;

@Data
public class ProcessCoverageResponse {


    private Double totalAwardsAmount;
    private Integer awardsCount;
    private Double awardsAmount;
    private TendersDistribution tendersDistribution;

    @Data
    public static class TendersDistribution {
        private int completeTendersCount;
        private int cancelledTendersCount;
        private int othersTendersCount;

        private int completeProcuringEntityCount;
        private int cancelledProcuringEntityCount;
        private int othersProcuringEntityCount;

        private double completeTendersAmount;
        private double cancelledTendersAmount;
        private double othersTendersAmount;

        private int tendersCount;
        private double tendersAmount;
        private double procuringEntityCount;

        private int monitoringTendersCount;
        private double monitoringTendersAmount;
        private int monitoringProcuringEntityCount;

        private int nonMonitoringTendersCount;
        private double nonMonitoringTendersAmount;
        private int nonMonitoringProcuringEntityCount;

        private int activeMonitoringTendersCount;
        private int violationMonitoringTendersCount;
        private int nonViolationMonitoringTendersCount;
        private int cancelledMonitoringTendersCount;

        private double activeMonitoringAmount;
        private double violationMonitoringAmount;
        private double nonViolationMonitoringAmount;
        private double cancelledMonitoringAmount;

        private int activeMonitoringProcuringEntityCount;
        private int violationMonitoringProcuringEntityCount;
        private int nonViolationMonitoringProcuringEntityCount;
        private int cancelledMonitoringProcuringEntityCount;

    }

}
