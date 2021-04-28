package com.datapath.sasu.dao.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResultsSourcesDAORequest {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> offices;
    private List<Integer> procuringEntityRegions;

}
