package com.datapath.sasu.dao.response;

import com.datapath.sasu.dao.entity.Violation;
import lombok.Data;

import java.util.List;

@Data
public class ResultsOfficesDAOResponse {

    private Double avgOfficeViolations;
    private Double tendersAmount;
    private List<Office> offices;
    private List<TenderDynamic> tenderDynamics;
    private List<Violation> violations;

    @Data
    public static class Office {
        private Integer id;
        private String name;
        private String date;
        private Integer tendersCount;
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
