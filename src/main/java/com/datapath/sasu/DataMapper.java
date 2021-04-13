package com.datapath.sasu;

import com.datapath.sasu.controllers.request.*;
import com.datapath.sasu.controllers.response.*;
import com.datapath.sasu.dao.request.*;
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

    ResultsDAORequest map(ResultsRequest request);

    ResultsResponse map(ResultsDAOResponse daoResponse);
}
