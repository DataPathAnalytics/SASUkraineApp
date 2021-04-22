package com.datapath.sasu.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessRegionsDAOResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<RegionProcuringEntity> procuringEntitiesCountByRegion;
    private List<Cpv> topCpv2ByAmount;
    private List<Cpv> topCpv2ByTendersCount;
    private Integer tendersCount;
    private Double amount;

    @Data
    public static class RegionProcuringEntity {
        private Integer regionId;
        private Integer procuringEntitiesCount;
    }

    @Data
    public static class Cpv {
        private String code;
        private String name;
        private Integer tendersCount;
        private Double amount;
    }

}
