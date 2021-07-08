package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ResultsOfficesDAORequest;
import com.datapath.sasu.dao.request.ResultsViolationsDAORequest;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse.Office;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse.TenderDynamic;
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
public class ResultsOfficesDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ResultsOfficesDAOResponse getResponse(ResultsOfficesDAORequest request) {
        var response = new ResultsOfficesDAOResponse();

        response.setAvgOfficeViolations(getAvgOfficeViolations());
        response.setTendersAmount(getTendersAmount(request));
        response.setOffices(getOffices(request));
        response.setTenderDynamics(getTenderDynamic(request));

        return response;
    }

    private Double getAvgOfficeViolations() {
        String query = "SELECT AVG(o.vioaltions)\n" +
                "FROM (\n" +
                "         SELECT COUNT(violation_id) vioaltions\n" +
                "         FROM results_offices\n" +
                "         GROUP BY sasu_office_id) o";
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    private Double getTendersAmount(ResultsOfficesDAORequest request) {

        String violationFilter = request.getViolationId() == null ? "" : "AND violation_id = " + request.getViolationId();

        String query = "SELECT SUM(tender_expected_value)\n" +
                "FROM (\n" +
                "         SELECT DISTINCT ON (tender_id) tender_expected_value\n" +
                "         FROM results_offices WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + violationFilter +
                "     ) rf";

        return jdbcTemplate.queryForObject(query, Double.class,request.getStartDate(),request.getEndDate());
    }

    private List<Office> getOffices(ResultsOfficesDAORequest request) {
        String violationFilter = request.getViolationId() == null ? "" : "AND violation_id = " + request.getViolationId();

        String query = "SELECT sasu_office_id AS id, sasu_office_name AS name, " +
                "monitoring_end_month AS date, COUNT(DISTINCT tender_id) AS tenders_count\n" +
                "FROM results_offices " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + violationFilter +
                "GROUP BY sasu_office_id, sasu_office_name, monitoring_end_month;";

        return jdbcTemplate.query(query, newInstance(Office.class), request.getStartDate(), request.getEndDate());
    }

    private List<TenderDynamic> getTenderDynamic(ResultsOfficesDAORequest request) {
        String violationFilter = request.getViolationId() == null ? "" : "AND violation_id = " + request.getViolationId();

        String query = "SELECT sasu_office_id                      AS office_id,\n" +
                "       monitoring_end_month              AS date,\n" +
                "       COUNT(DISTINCT tender_id)           AS tenders_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id) AS procuring_entity_count,\n" +
                "       SUM(tender_expected_value) AS amount\n" +
                "FROM results_offices ro\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + violationFilter + getOfficeFilter(request) +
                "GROUP BY sasu_office_id, monitoring_end_month";

        return jdbcTemplate.query(query, newInstance(TenderDynamic.class), request.getStartDate(), request.getEndDate());
    }

    private String getOfficeFilter(ResultsOfficesDAORequest request) {
        List<Integer> offices = request.getOffices();
        return isEmpty(offices) ? ""
                : String.format(" AND sasu_office_id IN (%s) ", collectionToCommaDelimitedString(offices));
    }

}
