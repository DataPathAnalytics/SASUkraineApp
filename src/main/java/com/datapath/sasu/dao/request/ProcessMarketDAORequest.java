package com.datapath.sasu.dao.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProcessMarketDAORequest {

    private String cpv2;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> regions;

}
