package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ResultsDAORequest;
import com.datapath.sasu.dao.response.ResultsDAOResponse;
import com.datapath.sasu.dao.response.ResultsDAOResponse.Distribution;
import com.datapath.sasu.dao.response.ResultsDAOResponse.Dynamic;
import com.datapath.sasu.dao.response.ResultsDAOResponse.Region;
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
public class ResultsDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ResultsDAOResponse getResponse(ResultsDAORequest request) {
        ResultsDAOResponse response = new ResultsDAOResponse();
        response.setTotalTendersAmount(getTotalTendersAmount());
        response.setTendersAmount(getTendersAmount(request));
        response.setTendersCount(getTendersCount(request));
        response.setRegions(getRegions(request));
        response.setDistributions(getMonitoringDistribution(request));
        response.setDynamics(getMonitoringDynamics(request));
        return response;
    }

    private Double getTotalTendersAmount() {
        String query = "SELECT SUM(tender_expected_value) FROM results_results";
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    private Double getTendersAmount(ResultsDAORequest request) {
        String query = "SELECT SUM(tender_expected_value) " +
                "FROM results_results " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?)";
        return jdbcTemplate.queryForObject(query, Double.class, request.getStartDate(), request.getEndDate());
    }

    private Integer getTendersCount(ResultsDAORequest request) {
        String query = "SELECT COUNT(DISTINCT tender_id) FROM results_results " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?)";
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private List<Region> getRegions(ResultsDAORequest request) {
        String query = "SELECT r.id                                    AS region_id,\n" +
                "       COUNT(DISTINCT tender_id)               AS tenders_count,\n" +
                "       COALESCE(SUM(tender_expected_value), 0) AS amount\n" +
                "FROM region r\n" +
                " LEFT JOIN results_results rr ON r.id = rr.region_id AND (monitoring_end_date >= ? AND monitoring_end_date < ?) " +
                "GROUP BY r.id;";

        return jdbcTemplate.query(query, newInstance(Region.class), request.getStartDate(), request.getEndDate());
    }

    private List<Distribution> getMonitoringDistribution(ResultsDAORequest request) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT monitoring_result,\n" +
                "       COUNT(DISTINCT monitoring_id)               AS tenders_count,\n" +
                "       COALESCE(SUM(tender_expected_value), 0) AS amount\n" +
                "FROM results_results " +
                "WHERE TRUE " +
                "AND (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY monitoring_result";

        return jdbcTemplate.query(query, newInstance(Distribution.class), request.getStartDate(), request.getEndDate());
    }

    private List<Dynamic> getMonitoringDynamics(ResultsDAORequest request) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT monitoring_end_month                  AS date,\n" +
                "       monitoring_result,\n" +
                "       COUNT(DISTINCT tender_id)               AS tenders_count,\n" +
                "       COALESCE(SUM(tender_expected_value), 0) AS amount\n" +
                "FROM results_results \n" +
                "WHERE monitoring_result IN ('addressed', 'complete', 'declined', 'closed') " +
                "AND (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY monitoring_end_month, monitoring_result";

        return jdbcTemplate.query(query, newInstance(Dynamic.class), request.getStartDate(), request.getEndDate());
    }


}
