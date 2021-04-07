package com.datapath.sasu.dao.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ProcessRegionsDAOResponse {

    private Integer totalProcuringEntitiesCount;
    private Integer procuringEntitiesCount;
    private List<RegionProcuringEntity> procuringEntitiesCountByRegion;

    @Data
    public static class RegionProcuringEntity {
        private Integer regionId;
        private Integer procuringEntitiesCount;
    }

}
