package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessRegionsResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<RegionProcuringEntity> procuringEntitiesCountByRegion;

    @Data
    public static class RegionProcuringEntity {
        private Integer regionId;
        private Integer procuringEntitiesCount;
    }

}
