package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.repository.ViolationRepository;
import com.datapath.sasu.dao.request.ResultsOfficesDAORequest;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse.Office;
import com.datapath.sasu.dao.response.ResultsOfficesDAOResponse.TenderDynamic;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Component
@AllArgsConstructor
public class ResultsOfficesDAOService {

    private final JdbcTemplate jdbcTemplate;
    private final ViolationRepository violationRepository;

    @Transactional
    public ResultsOfficesDAOResponse getResponse(ResultsOfficesDAORequest request) {
        ResultsOfficesDAOResponse response = new ResultsOfficesDAOResponse();

        response.setAvgOfficeViolations(getAvgOfficeViolations());
        response.setTendersAmount(getTendersAmount(request));
        response.setOffices(getOffices(request));
        response.setTenderDynamics(getTenderDynamic(request));
        response.setViolations(violationRepository.findAll());

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
                "         FROM results_offices WHERE TRUE " + violationFilter +
                "     ) rf";

        return jdbcTemplate.queryForObject(query, Double.class);
    }

    private List<Office> getOffices(ResultsOfficesDAORequest request) {
        String violationFilter = request.getViolationId() == null ? "" : "AND violation_id = " + request.getViolationId();

        String query = "SELECT sasu_office_id AS id, sasu_office_name AS name, " +
                "monitoring_start_month AS date, COUNT(DISTINCT tender_id) AS tenders_count\n" +
                "FROM results_offices " +
                "WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + violationFilter +
                "GROUP BY sasu_office_id, sasu_office_name, monitoring_start_month;";

        return jdbcTemplate.query(query, newInstance(Office.class), request.getStartDate(), request.getEndDate());
    }

    private List<TenderDynamic> getTenderDynamic(ResultsOfficesDAORequest request) {
        String violationFilter = request.getViolationId() == null ? "" : "AND violation_id = " + request.getViolationId();

        String query = "SELECT sasu_office_id                      AS office_id,\n" +
                "       monitoring_start_month              AS date,\n" +
                "       COUNT(DISTINCT tender_id)           AS tenders_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id) AS procuring_entity_count,\n" +
                "       SUM(tender_expected_value) AS amount\n" +
                "FROM results_offices ro\n" +
                "WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + violationFilter +
                "GROUP BY sasu_office_id, monitoring_start_month";

        return jdbcTemplate.query(query, newInstance(TenderDynamic.class), request.getStartDate(), request.getEndDate());
    }

}
