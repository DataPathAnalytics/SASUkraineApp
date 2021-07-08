package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessCoverageDAORequest;
import com.datapath.sasu.dao.response.ProcessCoverageDAOResponse;
import com.datapath.sasu.dao.response.ProcessCoverageDAOResponse.TendersDistribution;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ProcessCoverageDAOService {

    private final JdbcTemplate jdbcTemplate;

    private static final String PERIOD_FILTER;

    static {
        PERIOD_FILTER = "( tender_start_date >= ? AND tender_start_date <= ? ) ";
    }

    @Transactional
    public ProcessCoverageDAOResponse getResponse(ProcessCoverageDAORequest request) {
        ProcessCoverageDAOResponse response = new ProcessCoverageDAOResponse();
        response.setTotalAwardsAmount(getTotalAwardsAmount());
        response.setAwardsAmount(getAwardsAmount(request));
        response.setAwardsCount(getAwardsCount(request));
        response.setTendersDistribution(getDistribution(request));
        return response;
    }

    private Double getTotalAwardsAmount() {
        String query = "SELECT SUM(awards_value) FROM process_coverage";
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    private Double getAwardsAmount(ProcessCoverageDAORequest request) {

        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT SUM(awards_value) FROM process_coverage WHERE " + PERIOD_FILTER + regionClause;
        return jdbcTemplate.queryForObject(query, Double.class, request.getStartDate(), request.getEndDate());
    }

    private Integer getAwardsCount(ProcessCoverageDAORequest request) {
        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT COUNT(DISTINCT awards_count) FROM process_coverage WHERE " + PERIOD_FILTER + regionClause;
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private TendersDistribution getDistribution(ProcessCoverageDAORequest request) {
        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT COUNT(DISTINCT tender_id) FILTER ( WHERE tender_status = 'complete' )           AS complete_tenders_count,\n" +
                "       COUNT(DISTINCT tender_id)\n" +
                "       FILTER ( WHERE tender_status IN ('cancelled', 'unsuccessful'))                  AS cancelled_tenders_count,\n" +
                "       COUNT(DISTINCT tender_id)\n" +
                "       FILTER ( WHERE tender_status NOT IN ('complete', 'unsuccessful', 'cancelled') ) AS others_tenders_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER ( WHERE tender_status = 'complete' )                                     AS complete_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER ( WHERE tender_status IN ('cancelled', 'unsuccessful') )                 AS cancelled_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER ( WHERE tender_status NOT IN ('complete', 'unsuccessful', 'cancelled') ) AS others_procuring_entity_count,\n" +
                "       SUM(tender_expected_value) FILTER ( WHERE tender_status = 'complete' )             AS complete_tenders_amount,\n" +
                "       SUM(tender_expected_value)\n" +
                "       FILTER ( WHERE tender_status IN ('cancelled', 'unsuccessful') )                 AS cancelled_tenders_amount,\n" +
                "       SUM(tender_expected_value)\n" +
                "       FILTER ( WHERE tender_status NOT IN ('complete', 'unsuccessful', 'cancelled') ) AS others_tenders_amount,\n" +
                "       COUNT(DISTINCT tender_id)                                                       AS tenders_count,\n" +
                "       SUM(tender_expected_value)                                                      AS tenders_amount,\n" +
                "       COUNT(DISTINCT procuring_entity_id)                                             AS procuring_entity_count,\n" +
                "       COUNT(DISTINCT tender_id) FILTER ( WHERE has_monitoring IS TRUE )               AS monitoring_tenders_count,\n" +
                "       COUNT(DISTINCT tender_id) FILTER ( WHERE has_monitoring IS FALSE )              AS non_monitoring_tenders_count,\n" +
                "       SUM(tender_expected_value) FILTER ( WHERE has_monitoring IS TRUE )              AS monitoring_tenders_amount,\n" +
                "       SUM(tender_expected_value) FILTER ( WHERE has_monitoring IS FALSE )             AS non_monitoring_tenders_amount,\n" +
                "       COUNT(DISTINCT procuring_entity_id) FILTER ( WHERE has_monitoring IS TRUE)      AS monitoring_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id) FILTER ( WHERE has_monitoring IS FALSE)     AS non_monitoring_procuring_entity_count,\n" +
                "       COUNT(tender_id) FILTER ( WHERE monitoring_result IN ('active') )               AS active_monitoring_tenders_count,\n" +
                "       COUNT(tender_id)\n" +
                "       FILTER ( WHERE monitoring_result IN ('addressed', 'completed') )                AS violation_monitoring_tenders_count,\n" +
                "       COUNT(tender_id)\n" +
                "       FILTER ( WHERE monitoring_result IN ('declined', 'closed') )                    AS non_violation_monitoring_tenders_count,\n" +
                "       COUNT(tender_id)\n" +
                "       FILTER ( WHERE monitoring_result IN ('cancelled', 'stopped') )                  AS cancelled_monitoring_tenders_count,\n" +
                "       SUM(tender_expected_value)\n" +
                "       FILTER (WHERE monitoring_result IN ('active') )                                 AS active_monitoring_amount,\n" +
                "       SUM(tender_expected_value)\n" +
                "       FILTER (WHERE monitoring_result IN ('addressed', 'completed') )                 AS violation_monitoring_amount,\n" +
                "       SUM(tender_expected_value) FILTER (\n" +
                "           WHERE monitoring_result IN ('declined', 'closed') )                         AS non_violation_monitoring_amount,\n" +
                "       SUM(tender_expected_value)\n" +
                "       FILTER (WHERE monitoring_result IN ('cancelled', 'stopped') )                   AS cancelled_monitoring_amount,\n" +
                "       COUNT(DISTINCT procuring_entity_id) FILTER (\n" +
                "           WHERE monitoring_result IN ('active') )                                     AS active_monitoring_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER (\n" +
                "           WHERE monitoring_result IN ('addressed', 'completed') )                     AS violation_monitoring_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER (\n" +
                "           WHERE monitoring_result IN ('declined', 'closed') )                         AS non_violation_monitoring_procuring_entity_count,\n" +
                "       COUNT(DISTINCT procuring_entity_id)\n" +
                "       FILTER (WHERE monitoring_result IN ('cancelled', 'stopped') )                   AS cancelled_monitoring_procuring_entity_count\n" +
                "FROM process_coverage WHERE " + PERIOD_FILTER + regionClause;

        return jdbcTemplate.queryForObject(query, newInstance(TendersDistribution.class), request.getStartDate(), request.getEndDate());
    }

}
