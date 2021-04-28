package com.datapath.sasu.controllers.response;


import lombok.Data;

import java.util.List;

@Data
public class ResultsViolationsResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<TenderByViolation> tendersByViolation;
    private List<Region> regions;

    @Data
    public static class TenderByViolation {
        private Integer violationId;
        private Integer tendersCount;
        private Integer percent;
    }

    @Data
    public static class Region {
        private Integer regionId;
        private int tendersCount;
        private double amount;
        private int procuringEntitiesCount;
    }

}
