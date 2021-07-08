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

import static com.datapath.sasu.Constants.MONITORING_START;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
public class ResourcesDynamicsDAOService {

    private static final String PERIOD_FILTER;

    static {
        PERIOD_FILTER = "( tender_start_date >= ? AND tender_start_date <= ? ) ";
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Double getTotalMonitoringTenderPercent() {
        LocalDate startDate = MONITORING_START.toLocalDate();
        LocalDate endDate = LocalDate.now().withDayOfMonth(1);
        return jdbcTemplate.queryForObject(
                "SELECT\n" +
                        "SUM(tender_expected_value) FILTER ( WHERE has_monitoring IS TRUE) * 100 / SUM (tender_expected_value)\n" +
                        "FROM resources_dynamics WHERE " + PERIOD_FILTER,
                Double.class, startDate, endDate);
    }

    public Double getMonitoringTenderPercent(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {

        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.queryForObject(
                "SELECT\n" +
                        "SUM(tender_expected_value) FILTER ( WHERE has_monitoring " + regionClause + ") * 100 / SUM(tender_expected_value)\n" +
                        "FROM resources_dynamics WHERE " + PERIOD_FILTER,
                Double.class, startDate, endDate);
    }

    public List<DynamicTender> getDynamicTenders(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_end_month AS date, COUNT(DISTINCT tender_id) AS tendersCount, SUM(tender_expected_value) AS amount\n" +
                "        FROM resources_dynamics\n" +
                "        WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "        GROUP BY monitoring_end_month", new BeanPropertyRowMapper<>(DynamicTender.class), startDate, endDate);
    }

    public List<DynamicAuditor> getDynamicAuditors(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_end_month AS date, COUNT(DISTINCT auditor_id) AS count\n" +
                "        FROM resources_dynamics\n" +
                "        WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "        GROUP BY monitoring_end_month", new BeanPropertyRowMapper<>(DynamicAuditor.class), startDate, endDate);
    }

    public List<DynamicProductivity> getDynamicProductivity(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {
        String regionClause = isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT monitoring_end_month AS date,COUNT(DISTINCT tender_id)::REAL /NULLIF(COUNT(DISTINCT auditor_id),0) tendersCount,\n" +
                "       SUM(tender_expected_value) /NULLIF(COUNT(DISTINCT auditor_id),0) amount\n" +
                "FROM resources_dynamics\n" +
                "        WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY monitoring_end_month", new BeanPropertyRowMapper<>(DynamicProductivity.class), startDate, endDate);
    }

    public Integer getTotalMonitoringTenders() {
        String query = "SELECT COUNT(DISTINCT tender_id) FROM resources_dynamics WHERE has_monitoring IS TRUE";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

}
