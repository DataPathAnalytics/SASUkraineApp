package com.datapath.sasu;

import com.datapath.sasu.controllers.request.ProcessCoverageRequest;
import com.datapath.sasu.controllers.request.ProcessMarketRequest;
import com.datapath.sasu.controllers.request.ProcessMethodsRequest;
import com.datapath.sasu.controllers.request.ProcessRegionsRequest;
import com.datapath.sasu.controllers.response.*;
import com.datapath.sasu.dao.request.ProcessCoverageDAORequest;
import com.datapath.sasu.dao.request.ProcessMarketDAORequest;
import com.datapath.sasu.dao.request.ProcessMethodsDAORequest;
import com.datapath.sasu.dao.request.ProcessRegionsDAORequest;
import com.datapath.sasu.dao.response.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DataMapper {

    ProcessMarketDAORequest map(ProcessMarketRequest api);

    ProcessMarketResponse map(ProcessMarketDAOResponse dao);

    ProcessMarketResponse.Cpv map(ProcessMarketDAOResponse.Cpv daoCpv);

    ProcessRegionsResponse map(ProcessRegionsDAOResponse dao);

    ProcessRegionsDAORequest map(ProcessRegionsRequest request);

    ProcessRegionsCpvResponse map(ProcessRegionsCpvDAOResponse dao);

    ProcessMethodsDAORequest map(ProcessMethodsRequest request);

    ProcessMethodsResponse map(ProcessMethodsDAOResponse daoResponse);

    HomeResponse map(HomeDAOResponse daoResponse);

    ProcessCoverageDAORequest map(ProcessCoverageRequest request);

    ProcessCoverageResponse map(ProcessCoverageDAOResponse daoResponse);
}
