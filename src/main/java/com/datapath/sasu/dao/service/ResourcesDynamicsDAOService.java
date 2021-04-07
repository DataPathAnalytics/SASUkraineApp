package com.datapath.sasu.dao.service;

import com.datapath.sasu.controllers.response.ResourcesDynamicsResponse.DynamicAuditor;
import com.datapath.sasu.controllers.response.ResourcesDynamicsResponse.DynamicProductivity;
import com.datapath.sasu.controllers.response.ResourcesDynamicsResponse.DynamicTender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
public class ResourcesDynamicsDAOService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getTotalMonitoringTenderPercent() {
        return jdbcTemplate.queryForObject(
                "SELECT\n" +
                        "SUM(tender_expected_value) FILTER ( WHERE has_monitoring ) * 100 / SUM (tender_expected_value)\n" +
                        "FROM resources_dynamics", Integer.class);
    }

    public Integer getMonitoringTenderPercent(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {

        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.queryForObject(
                "SELECT\n" +
                        "SUM(tender_expected_value) FILTER ( WHERE has_monitoring ) * 100 / SUM (tender_expected_value)\n" +
                        "FROM resources_dynamics WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + regionClause,
                Integer.class, startDate, endDate);
    }

    public List<DynamicTender> getDynamicTenders(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_start_month AS date, COUNT(DISTINCT tender_id) AS tendersCount, SUM(tender_expected_value) AS amount\n" +
                "        FROM resources_dynamics\n" +
                "        WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + regionClause +
                "        GROUP BY monitoring_start_month", new BeanPropertyRowMapper<>(DynamicTender.class), startDate, endDate);
    }

    public List<DynamicAuditor> getDynamicAuditors(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_start_month AS date, COUNT(DISTINCT auditor_id) AS count\n" +
                "        FROM resources_dynamics\n" +
                "        WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + regionClause +
                "        GROUP BY monitoring_start_month", new BeanPropertyRowMapper<>(DynamicAuditor.class), startDate, endDate);
    }

    public List<DynamicProductivity> getDynamicProductivity(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_start_month AS date,COUNT(DISTINCT tender_id)::REAL /NULLIF(COUNT(DISTINCT auditor_id),0) tendersCount,\n" +
                "       SUM(tender_expected_value) /NULLIF(COUNT(DISTINCT auditor_id),0) amount\n" +
                "FROM resources_dynamics\n" +
                "        WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?) " + regionClause +
                "GROUP BY monitoring_start_month", new BeanPropertyRowMapper<>(DynamicProductivity.class), startDate, endDate);
    }

}
