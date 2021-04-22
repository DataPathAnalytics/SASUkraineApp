package com.datapath.sasu.dao.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResultsOfficesDAORequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer violationId;

}
