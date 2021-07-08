package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessMethodsDAORequest;
import com.datapath.sasu.dao.response.ProcessMethodsDAOResponse;
import com.datapath.sasu.dao.response.ProcessMethodsDAOResponse.LocalMethod;
import com.datapath.sasu.dao.response.ProcessMethodsDAOResponse.MonitoringDynamic;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ProcessMethodsDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ProcessMethodsDAOResponse getResponse(ProcessMethodsDAORequest request) {
        ProcessMethodsDAOResponse response = new ProcessMethodsDAOResponse();

        response.setProcedureMonitoringCoverage(getProcedureMonitoringCoverage());
        response.setMonitoringProcuringEntities(getMonitoringProcuringEntities());
        response.setTendersCount(getTendersCount(request));
        response.setTendersAmount(getTendersAmount(request));
        response.setProcuringEntityCount(getProcuringEntityCount(request));
        response.setLocalMethods(getLocalMethods(request));
        response.setMonitoringDynamics(getMonitoringDynamics(request));

        return response;
    }

    private Double getMonitoringProcuringEntities() {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) FILTER ( WHERE has_monitoring ) FROM process_methods";
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    private Double getProcedureMonitoringCoverage() {
        String query = "SELECT SUM(tender_expected_value) FILTER ( WHERE has_monitoring ) /  SUM (tender_expected_value) * 100\n" +
                "FROM process_methods";

        return jdbcTemplate.queryForObject(query, Double.class);
    }


    private Integer getTendersCount(ProcessMethodsDAORequest request) {

        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT COUNT(DISTINCT tender_id) " +
                "FROM process_methods " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause;
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private Double getTendersAmount(ProcessMethodsDAORequest request) {
        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT SUM(tender_expected_value) " +
                "FROM process_methods " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause;
        return jdbcTemplate.queryForObject(query, Double.class, request.getStartDate(), request.getEndDate());
    }

    private Integer getProcuringEntityCount(ProcessMethodsDAORequest request) {
        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));


        String query = "SELECT COUNT(DISTINCT procuring_entity_id) " +
                "FROM process_methods " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause;
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private List<LocalMethod> getLocalMethods(ProcessMethodsDAORequest request) {

        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "WITH methods AS (\n" +
                "    SELECT tender_local_method AS name, COUNT(DISTINCT tender_id) AS tenders_count, SUM(tender_expected_value) AS amount\n" +
                "    FROM process_methods\n" +
                "    WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?)" + regionClause +
                "    GROUP BY tender_local_method\n" +
                ")\n" +
                "SELECT *,\n" +
                "       FLOOR(tenders_count / SUM(tenders_count) OVER ()  * 100) AS tenders_count_percent,\n" +
                "       FLOOR(amount / SUM(amount) OVER ()  * 100) AS amount_percent\n" +
                "FROM methods;";

        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(LocalMethod.class), request.getStartDate(), request.getEndDate());
    }

    private List<MonitoringDynamic> getMonitoringDynamics(ProcessMethodsDAORequest request) {

        String regionClause = CollectionUtils.isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT monitoring_end_month AS date, COUNT(DISTINCT tender_id) AS tenders_count, SUM(tender_expected_value) AS amount\n" +
                "FROM process_methods\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?)" + regionClause +
                "GROUP BY monitoring_end_month\n" +
                "ORDER BY monitoring_end_month";

        return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(MonitoringDynamic.class), request.getStartDate(), request.getEndDate());
    }

}
