package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ResultsViolationsDAORequest;
import com.datapath.sasu.dao.response.ResultsViolationsDAOResponse;
import com.datapath.sasu.dao.response.ResultsViolationsDAOResponse.Region;
import com.datapath.sasu.dao.response.ResultsViolationsDAOResponse.TenderByViolation;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ResultsViolationsDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ResultsViolationsDAOResponse getResponse(ResultsViolationsDAORequest request) {
        var response = new ResultsViolationsDAOResponse();
        response.setTotalProcuringEntitiesCount(getTotalProcuringEntitiesCount());
        response.setProcuringEntitiesCount(getProcuringEntitiesCount(request));
        response.setTendersByViolation(getTendersByViolation(request));
        response.setRegions(getRegions(request));

        return response;
    }

    private Integer getTotalProcuringEntitiesCount() {
        var query = "SELECT COUNT(DISTINCT procuring_entity_id) FROM results_violations WHERE monitoring_result IN ('addressed', 'completed')";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getProcuringEntitiesCount(ResultsViolationsDAORequest request) {

        String filter = getFilter(request);

        String query = "SELECT COUNT(DISTINCT procuring_entity_id) " +
                "FROM results_violations " +
                "WHERE TRUE " + filter;
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private List<TenderByViolation> getTendersByViolation(ResultsViolationsDAORequest request) {

        String filter = getMonitoringDateFilter(request) + getOfficeFilter(request);

        String query = "WITH tenders AS (\n" +
                "    SELECT COUNT(DISTINCT tender_id) count FROM results_violations\n" +
                ")\n" +
                "SELECT violation_id,\n" +
                "       COUNT(DISTINCT tender_id)                                 AS tenders_count,\n" +
                "       COUNT(DISTINCT tender_id) * 100 / (SELECT * FROM tenders) AS percent\n" +
                "FROM results_violations WHERE TRUE " + filter +
                "GROUP BY violation_id ORDER BY tenders_count DESC";
        return jdbcTemplate.query(query, newInstance(TenderByViolation.class));
    }

    private List<Region> getRegions(ResultsViolationsDAORequest request) {

        String filter = getFilter(request) + getActiveMonitoringFilter();

        String query = "WITH entity_regions AS (\n" +
                "    SELECT procuring_entity_region_id          AS region_id,\n" +
                "           SUM(tender_expected_value)          AS amount,\n" +
                "           COUNT(DISTINCT tender_id)           AS tenders_count,\n" +
                "           COUNT(DISTINCT procuring_entity_id) AS procuring_entities_count\n" +
                "    FROM  (SELECT DISTINCT ON(tender_id) * FROM results_violations\n" +
                "          WHERE procuring_entity_region_id IS NOT NULL " + filter + ") rv " +
                "    GROUP BY procuring_entity_region_id\n" +
                ")\n" +
                "SELECT r.id AS region_id, er.amount, er.tenders_count, er.procuring_entities_count\n" +
                "FROM region r\n" +
                "         LEFT JOIN entity_regions er ON r.id = er.region_id";
        return jdbcTemplate.query(query, newInstance(Region.class));
    }

    private String getFilter(ResultsViolationsDAORequest request) {
        return getMonitoringDateFilter(request)
                + getViolationFilter(request)
                + getOfficeFilter(request);
    }

    private String getMonitoringDateFilter(ResultsViolationsDAORequest request) {
        return String.format(" AND (monitoring_end_date >= '%s' AND monitoring_end_date < '%s')",
                request.getStartDate(), request.getEndDate());
    }

    private String getOfficeFilter(ResultsViolationsDAORequest request) {
        List<Integer> offices = request.getOffices();
        return isEmpty(offices) ? ""
                : String.format(" AND office_id IN (%s) ", collectionToCommaDelimitedString(offices));
    }

    private String getViolationFilter(ResultsViolationsDAORequest request) {
        Integer violationId = request.getViolationId();
        return violationId == null ? "" : "AND violation_id = " + violationId;
    }

    private String getActiveMonitoringFilter() {
        return "AND monitoring_result IN ('addressed','completed') ";
    }

}
