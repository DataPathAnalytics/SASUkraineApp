package com.datapath.sasu.dao.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResultsOfficesDAORequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer violationId;
    private List<Integer> offices;

}
