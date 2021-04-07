package com.datapath.sasu.dao.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ProcessRegionsCpvDAOResponse {

    private List<DateCpv> topCpv2ByTendersCount;
    private List<DateCpv> topCpv2ByAmount;

    @Data
    public static class Cpv {
        private String code;
        private String name;
        private Integer tendersCount;
        private Double amount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateCpv {
        private String date;
        private List<Cpv> topCpv;
    }

}
