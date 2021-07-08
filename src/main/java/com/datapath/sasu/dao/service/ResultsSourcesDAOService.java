package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ResultsSourcesDAORequest;
import com.datapath.sasu.dao.response.ResultsSourcesDAOResponse;
import com.datapath.sasu.dao.response.ResultsSourcesDAOResponse.ReasonTender;
import com.datapath.sasu.dao.response.ResultsSourcesDAOResponse.Violation;
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
public class ResultsSourcesDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ResultsSourcesDAOResponse getResponse(ResultsSourcesDAORequest request) {
        var response = new ResultsSourcesDAOResponse();
        response.setTotalTendersAmount(getTotalTendersAmount());
        response.setTendersAmount(getTendersAmount(request));
        response.setReasonTenders(getReasonTenders(request));
        response.setViolations(getViolations(request));
        return response;
    }

    private Long getTotalTendersAmount() {
        String query = "SELECT SUM(tender_expected_value) FROM (" +
                "SELECT DISTINCT ON(tender_id,monitoring_id) tender_expected_value FROM results_sources WHERE has_monitoring IS TRUE" +
                ") rs";
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    private Long getTendersAmount(ResultsSourcesDAORequest request) {
        String query = "SELECT SUM(awards_value) FROM (" +
                "SELECT DISTINCT ON(tender_id) awards_value FROM results_sources WHERE TRUE "
                + getFilter(request) +
                ") rs";
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    private List<ReasonTender> getReasonTenders(ResultsSourcesDAORequest request) {
        String query = "SELECT reason_id,\n" +
                "       COUNT(DISTINCT tender_id) FILTER ( WHERE monitoring_result IN ('addressed', 'completed') ) violation_tenders_count,\n" +
                "       COUNT(DISTINCT tender_id) FILTER ( WHERE monitoring_result  IN ('declined', 'closed') ) non_violation_tenders_count\n" +
                "FROM results_sources WHERE reason_id IS NOT NULL " + getFilter(request) +
                "GROUP BY reason_id";
        return jdbcTemplate.query(query, newInstance(ReasonTender.class));
    }

    private List<Violation> getViolations(ResultsSourcesDAORequest request) {
        String query = "SELECT violation_id,\n" +
                "       COUNT(DISTINCT tender_id)           AS tenders_count,\n" +
                "       SUM(tender_expected_value)           AS tenders_amount,\n" +
                "       COUNT(DISTINCT procuring_entity_id) AS procuring_entities_count\n" +
                "FROM results_sources\n" +
                "WHERE violation_id IS NOT NULL " + getFilter(request) + getReasonFilter(request) +
                "GROUP BY violation_id;";
        return jdbcTemplate.query(query, newInstance(Violation.class));
    }

    private String getFilter(ResultsSourcesDAORequest request) {
        return getMonitoringDateFilter(request) + getOfficeFilter(request) + getRegionFilter(request);
    }

    private String getMonitoringDateFilter(ResultsSourcesDAORequest request) {
        return String.format(" AND (monitoring_end_date >= '%s' AND monitoring_end_date < '%s')",
                request.getStartDate(), request.getEndDate());
    }

    private String getOfficeFilter(ResultsSourcesDAORequest request) {
        List<Integer> offices = request.getOffices();
        return isEmpty(offices) ? ""
                : String.format(" AND office_id IN (%s) ", collectionToCommaDelimitedString(offices));
    }

    private String getReasonFilter(ResultsSourcesDAORequest request) {
        List<Integer> reasons = request.getReasons();
        return isEmpty(reasons) ? ""
                : String.format(" AND reason_id IN (%s) ", collectionToCommaDelimitedString(reasons));
    }

    private String getRegionFilter(ResultsSourcesDAORequest request) {
        List<Integer> regions = request.getProcuringEntityRegions();
        return isEmpty(regions) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(regions));
    }

}
