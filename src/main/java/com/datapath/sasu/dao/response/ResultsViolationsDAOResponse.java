package com.datapath.sasu.dao.response;

import com.datapath.sasu.dao.entity.Office;
import lombok.Data;

import java.util.List;

@Data
public class ResultsViolationsDAOResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<TenderByViolation> tendersByViolation;
    private List<Region> regions;
    private List<Office> offices;

    @Data
    public static class TenderByViolation {
        private Integer violationId;
        private Integer tendersCount;
        private Integer percent;
    }

    @Data
    public static class Region {
        private Integer regionId;
        private Integer tendersCount;
        private Double amount;
        private Integer procuringEntitiesCount;
    }

}
