package com.datapath.sasu.controllers;

import com.datapath.sasu.controllers.request.*;
import com.datapath.sasu.controllers.response.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin
public class SASUController {

    private SasuWebService service;

    @RequestMapping("/home")
    public HomeResponse home() {
        return service.getHome();
    }

    @RequestMapping("/resources-dasu-geography")
    public ResourcesDASUGeographyResponse resourcesDASUGeography(@Validated ResourcesDASUGeographyRequest request) {
        return service.getResourcesDASUGeography(request);
    }

    @RequestMapping("/resources-dynamics")
    public ResourcesDynamicsResponse resourcesDynamics(@Validated ResourcesDynamicsRequest request) {
        return service.getResourcesDynamics(request);
    }

    @RequestMapping("/process-market")
    public ProcessMarketResponse processMarket(@Validated ProcessMarketRequest request) {
        return service.getProcessMarket(request);
    }

    @RequestMapping("/process-market/cpv2")
    public ProcessMarketResponse processMarketCpv2(@Validated ProcessMarketRequest request) {
        return service.getProcessMarketCpv2(request);
    }

    @RequestMapping("/process-regions")
    public ProcessRegionsResponse processRegions(@Validated ProcessRegionsRequest request) {
        return service.getProcessRegions(request);
    }

    @RequestMapping("/process-methods")
    public ProcessMethodsResponse processMethods(@Validated ProcessMethodsRequest request) {
        return service.getProcessMethods(request);
    }

    @RequestMapping("/process-coverage")
    public ProcessCoverageResponse processCoverage(@Validated ProcessCoverageRequest request) {
        return service.getProcessCoverage(request);
    }

    @RequestMapping("/results-results")
    public ResultsResponse results(@Validated ResultsRequest request) {
        return service.getResults(request);
    }

    @RequestMapping("/results-offices")
    public ResultsOfficesResponse resultsOffices(@Validated ResultsOfficesRequest request) {
        return service.getResultsOffices(request);
    }


}
