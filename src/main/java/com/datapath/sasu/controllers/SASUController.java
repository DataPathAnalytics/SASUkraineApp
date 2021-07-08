package com.datapath.sasu.controllers;

import com.datapath.sasu.controllers.request.*;
import com.datapath.sasu.controllers.response.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
public class SASUController {

    private SasuWebService service;

    @GetMapping("/mappings")
    public MappingResponse mappings() {
        return service.getMappings();
    }

    @GetMapping("/home")
    public HomeResponse home() {
        return service.getHome();
    }

    @GetMapping("/resources-dasu-geography")
    public ResourcesDASUGeographyResponse resourcesDASUGeography(@Validated ResourcesDASUGeographyRequest request) {
        return service.getResourcesDASUGeography(request);
    }

    @GetMapping("/resources-dynamics")
    public ResourcesDynamicsResponse resourcesDynamics(@Validated ResourcesDynamicsRequest request) {
        return service.getResourcesDynamics(request);
    }

    @GetMapping("/process-market")
    public ProcessMarketResponse processMarket(@Validated ProcessMarketRequest request) {
        return service.getProcessMarket(request);
    }

    @GetMapping("/process-market/cpv2")
    public ProcessMarketResponse processMarketCpv2(@Validated ProcessMarketRequest request) {
        return service.getProcessMarketCpv2(request);
    }

    @GetMapping("/process-regions")
    public ProcessRegionsResponse processRegions(@Validated ProcessRegionsRequest request) {
        return service.getProcessRegions(request);
    }

    @GetMapping("/process-methods")
    public ProcessMethodsResponse processMethods(@Validated ProcessMethodsRequest request) {
        return service.getProcessMethods(request);
    }

    @GetMapping("/process-coverage")
    public ProcessCoverageResponse processCoverage(@Validated ProcessCoverageRequest request) {
        return service.getProcessCoverage(request);
    }

    @GetMapping("/process-duration")
    public ProcessDurationResponse processDuration(@Validated ProcessDurationRequest request) {
        return service.getProcessDuration(request);
    }

    @GetMapping("/results-results")
    public ResultsResponse results(@Validated ResultsRequest request) {
        return service.getResults(request);
    }

    @GetMapping("/results-offices")
    public ResultsOfficesResponse resultsOffices(@Validated ResultsOfficesRequest request) {
        return service.getResultsOffices(request);
    }

    @RequestMapping("/results-violations")
    public ResultsViolationsResponse resultsViolations(@Validated ResultsViolationsRequest request) {
        return service.getResultsViolations(request);
    }

    @RequestMapping("/results-sources")
    public ResultsSourcesResponse resultsSources(@Validated ResultsSourcesRequest request) {
        return service.getResultsSources(request);
    }

    @PostMapping("support/email")
    public void sendSupportEmail(@RequestBody @Validated SupportEmailRequest request) {
        service.sendSupportEmail(request);
    }

}
