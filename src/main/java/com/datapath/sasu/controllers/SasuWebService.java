package com.datapath.sasu.controllers;

import com.datapath.sasu.DataMapper;
import com.datapath.sasu.controllers.request.*;
import com.datapath.sasu.controllers.response.*;
import com.datapath.sasu.dao.request.*;
import com.datapath.sasu.dao.response.*;
import com.datapath.sasu.dao.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SasuWebService {

    @Autowired
    private HomeDAOService homeDAOService;
    @Autowired
    private ResourcesDasuGeographyDAOService geographyDAOService;
    @Autowired
    private ResourcesDynamicsDAOService resourcesDynamicsDAOService;
    @Autowired
    private ProcessMarketDAOService processMarketDAOService;
    @Autowired
    private ProcessRegionsDAOService processRegionsDAOService;
    @Autowired
    private ProcessMethodsDAOService processMethodsDAOService;
    @Autowired
    private ProcessCoverageDAOService processCoverageDAOService;
    @Autowired
    private ResultsDAOService resultsDAOService;
    @Autowired
    private ResultsOfficesDAOService resultsOfficesDAOService;
    @Autowired
    private ResultsViolationsDAOService resultsViolationsDAOService;

    @Autowired
    private DataMapper mapper;

    public HomeResponse getHome() {
        HomeDAOResponse daoResponse = homeDAOService.getResponse();
        return mapper.map(daoResponse);
    }

    public ResourcesDASUGeographyResponse getResourcesDASUGeography(ResourcesDASUGeographyRequest request) {
        ResourcesDASUGeographyResponse response = new ResourcesDASUGeographyResponse();

        response.setTotalAuditorsCount(geographyDAOService.getTotalAuditorsCount());

        response.setAuditorsCount(geographyDAOService.getAuditorsCount(
                request.getStartDate(), request.getEndDate(), request.getRegions())
        );

        response.setAuditorsCountByRegion(geographyDAOService.getAuditorsCountByRegion(
                request.getStartDate(), request.getEndDate())
        );
        response.setAuditorsCountByMonth(geographyDAOService.getAuditorsCountByMonth(request.getStartDate(), request.getEndDate(), request.getRegions()));
        return response;
    }

    public ResourcesDynamicsResponse getResourcesDynamics(ResourcesDynamicsRequest request) {
        ResourcesDynamicsResponse response = new ResourcesDynamicsResponse();

        response.setTotalMonitoringTenderPercent(Math.round(resourcesDynamicsDAOService.getTotalMonitoringTenderPercent()));
        response.setTotalMonitoringTenders(resourcesDynamicsDAOService.getTotalMonitoringTenders());

        response.setMonitoringTenderPercent(Math.round(resourcesDynamicsDAOService.getMonitoringTenderPercent(
                request.getStartDate(), request.getEndDate(), request.getRegions()
        )));

        response.setDynamicTenders(resourcesDynamicsDAOService.getDynamicTenders(
                request.getStartDate(), request.getEndDate(), request.getRegions())
        );

        response.setDynamicAuditors(resourcesDynamicsDAOService.getDynamicAuditors(
                request.getStartDate(), request.getEndDate(), request.getRegions())
        );

        response.setDynamicProductivity(resourcesDynamicsDAOService.getDynamicProductivity(
                request.getStartDate(), request.getEndDate(), request.getRegions())
        );

        return response;
    }

    public ProcessMarketResponse getProcessMarket(ProcessMarketRequest request) {
        ProcessMarketDAORequest daoRequest = mapper.map(request);
        ProcessMarketDAOResponse daoResponse = processMarketDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ProcessMarketResponse getProcessMarketCpv2(ProcessMarketRequest request) {
        ProcessMarketDAORequest daoRequest = mapper.map(request);
        ProcessMarketDAOResponse daoResponse = processMarketDAOService.getCpv2Tree(daoRequest);
        return mapper.map(daoResponse);
    }

    public ProcessRegionsResponse getProcessRegions(ProcessRegionsRequest request) {
        ProcessRegionsDAORequest daoRequest = mapper.map(request);
        ProcessRegionsDAOResponse daoResponse = processRegionsDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ProcessMethodsResponse getProcessMethods(ProcessMethodsRequest request) {
        ProcessMethodsDAORequest daoRequest = mapper.map(request);
        ProcessMethodsDAOResponse daoResponse = processMethodsDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ProcessCoverageResponse getProcessCoverage(ProcessCoverageRequest request) {
        ProcessCoverageDAORequest daoRequest = mapper.map(request);
        ProcessCoverageDAOResponse daoResponse = processCoverageDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ResultsResponse getResults(ResultsRequest request) {
        ResultsDAORequest daoRequest = mapper.map(request);
        ResultsDAOResponse daoResponse = resultsDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ResultsOfficesResponse getResultsOffices(ResultsOfficesRequest request) {
        ResultsOfficesDAORequest daoRequest = mapper.map(request);
        ResultsOfficesDAOResponse daoResponse = resultsOfficesDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

    public ResultsViolationsResponse getResultsViolations(ResultsViolationsRequest request) {
        ResultsViolationsDAORequest daoRequest = mapper.map(request);
        ResultsViolationsDAOResponse daoResponse = resultsViolationsDAOService.getResponse(daoRequest);
        return mapper.map(daoResponse);
    }

}
