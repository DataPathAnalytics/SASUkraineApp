package com.datapath.sasu.controllers.response;

import com.datapath.sasu.dao.entity.DateInteger;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HomeResponse {

    private LocalDate updatedDate;
    private List<ProcuringEntity> top20ProcuringEntity;
    private Long tendersAmount;
    private Integer procuringEntitiesCount;
    private Integer procuringEntitiesWithViolationsCount;
    private Integer tendersCount;
    private Integer violationsCount;
    private List<DateInteger> tendersDynamic;
    private List<DateInteger> violationsDynamic;

    @Data
    public static class ProcuringEntity {
        private String outerId;
        private String name;
        private Double amount;
    }


}
