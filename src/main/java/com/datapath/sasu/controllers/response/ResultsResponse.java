package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsResponse {

    private Double totalTendersAmount;
    private Double tendersAmount;
    private Integer tendersCount;
    private List<Region> regions;
    private List<Distribution> distributions;
    private List<Dynamic> dynamics;

    @Data
    public static class Region {
        private Integer regionId;
        private Integer tendersCount;
        private Double amount;
    }

    @Data
    public static class Distribution {
        private String monitoringResult;
        private Integer tendersCount;
        private Double amount;
    }

    @Data
    public static class Dynamic {
        private String date;
        private String monitoringResult;
        private Integer tendersCount;
        private Double amount;
    }

}
