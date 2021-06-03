package com.datapath.sasu.dao.response;

import com.datapath.sasu.controllers.response.LocalMethod;
import com.datapath.sasu.controllers.response.RegionDTO;
import com.datapath.sasu.dao.entity.Office;
import com.datapath.sasu.dao.entity.Reason;
import com.datapath.sasu.dao.entity.Violation;
import lombok.Data;

import java.util.List;

@Data
public class MappingDAOResponse {

    private List<Office> offices;
    private List<Violation> violations;
    private List<Reason> reasons;
    private List<LocalMethod> localMethods;
    private List<RegionDTO> regions;


}
