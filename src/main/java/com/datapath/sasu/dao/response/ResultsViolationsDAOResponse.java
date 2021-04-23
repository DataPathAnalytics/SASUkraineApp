package com.datapath.sasu.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class ResultsViolationsDAOResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<TenderByViolation> tendersByViolation;

    @Data
    public static class TenderByViolation {
        private Integer violationId;
        private Integer tendersCount;
        private Integer percent;
    }

}
