package com.datapath.sasu.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessDurationDAOResponse {

    private Integer totalDuration;
    private Integer duration;
    private Integer competitiveDuration;
    private Integer nonCompetitiveDuration;

    private List<Region> regions;
    private List<LocalMethod> localMethods;

    @Data
    public static class Region {
        private Integer regionId;
        private Integer duration;
    }

    @Data
    public static class LocalMethod {
        private String name;
        private Integer minDuration;
        private Integer maxDuration;
    }

}
