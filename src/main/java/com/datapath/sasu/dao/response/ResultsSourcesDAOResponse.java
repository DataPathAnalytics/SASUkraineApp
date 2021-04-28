package com.datapath.sasu.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsSourcesDAOResponse {

    private Long tendersAmount;
    private Integer tendersCount;
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
