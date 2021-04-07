package com.datapath.sasu.controllers.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class ResourcesDASUGeographyRequest {

    @NotNull
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = ISO.DATE)
    @NotNull
    private LocalDate endDate;
    private List<Integer> regions;
}
