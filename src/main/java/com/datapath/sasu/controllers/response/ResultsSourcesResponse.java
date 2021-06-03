package com.datapath.sasu.controllers.response;


import lombok.Data;

import java.util.List;

@Data
public class ResultsSourcesResponse {

    private Long tendersAmount;
    private Long totalTendersAmount;
    private List<ReasonTender> reasonTenders;
    private List<Violation> violations;

    @Data
    public static class ReasonTender {
        private int reasonId;
        private int violationTendersCount;
        private int nonViolationTendersCount;
    }

    @Data
    public static class Violation {
        private int violationId;
        private int tendersCount;
        private double tendersAmount;
        private int procuringEntitiesCount;
    }

}
