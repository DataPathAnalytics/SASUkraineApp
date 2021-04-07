package com.datapath.sasu.dao.response;

import lombok.Data;

import java.util.List;

@Data
public class ProcessMarketDAOResponse {

    private Integer cpvCount;
    private Integer cpv2Count;
    private List<CpvTenders> cpvTenders;
    private List<CpvDynamic> cpvDynamics;
    private List<Category> categories;
    private List<Cpv> cpv2Tree;

    @Data
    public static class CpvTenders {
        private String cpv2;
        private String cpvName;
        private List<Integer> categories;
        private Integer tendersCount;
        private Long amount;
    }

    @Data
    public static class CpvDynamic {
        private String cpv2;
        private String date;
        private Integer tendersCount;
        private Long amount;
    }

    @Data
    public static class Category {
        private Integer id;
        private String name;
        private Integer tendersCount;
        private Double amount;

        private List<Cpv> topCpv2ByTendersCount;
        private List<Cpv> topCpv2ByAmount;
    }

    @Data
    public static class Cpv {
        private String code;
        private String name;
        private Integer tendersCount;
        private Double amount;
        private Integer categoryId;

        private List<Cpv> topChildCpvByTendersCount;
        private List<Cpv> topChildCpvByAmount;


    }
}
