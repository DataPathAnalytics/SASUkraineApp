package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsOfficesResponse {

    private Double avgOfficeViolations;
    private Double tendersAmount;
    private List<Office> offices;
    private List<TenderDynamic> tenderDynamics;

    @Data
    public static class Office {
        private Integer id;
        private String name;
        private Integer tendersCount;
        private String date;
    }

    @Data
    public static class TenderDynamic {
        private Integer officeId;
        private String date;
        private Integer tendersCount;
        private Integer procuringEntityCount;
        private Double amount;
    }

}
