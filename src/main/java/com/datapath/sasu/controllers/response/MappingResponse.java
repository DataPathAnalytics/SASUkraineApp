package com.datapath.sasu.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class MappingResponse {
    private List<OfficeDTO> offices;
    private List<ViolationDTO> violations;
    private List<ReasonDTO> reasons;
}
