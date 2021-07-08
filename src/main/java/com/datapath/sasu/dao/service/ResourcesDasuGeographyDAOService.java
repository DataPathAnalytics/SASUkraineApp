package com.datapath.sasu.dao.service;

import com.datapath.sasu.controllers.response.MonthAuditors;
import com.datapath.sasu.controllers.response.RegionAuditors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component

public class ResourcesDasuGeographyDAOService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getTotalAuditorsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT auditor_id) FROM resources_dasu_geography", Integer.class);
    }

    public Integer getAuditorsCount(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {

        String regionClause = CollectionUtils.isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT auditor_id) FROM resources_dasu_geography " +
                        "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause,
                Integer.class, startDate, endDate);
    }

    public List<RegionAuditors> getAuditorsCountByRegion(LocalDate startDate, LocalDate endDate) {

        return jdbcTemplate.query("SELECT r.id AS region_id, COUNT(DISTINCT g.auditor_id) auditors_count\n" +
                "FROM region r\n" +
                "         LEFT JOIN resources_dasu_geography g ON r.id = g.region_id " +
                "AND (monitoring_end_date >= ? AND monitoring_end_date < ?)\n" +
                "GROUP BY r.id", new BeanPropertyRowMapper<>(RegionAuditors.class), startDate, endDate);
    }

    public List<MonthAuditors> getAuditorsCountByMonth(LocalDate startDate, LocalDate endDate, List<Integer> regionIds) {

        String regionClause = CollectionUtils.isEmpty(regionIds) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(regionIds));

        return jdbcTemplate.query("SELECT region_id,monitoring_end_month AS date,COUNT(DISTINCT auditor_id) auditors_count\n" +
                "FROM resources_dasu_geography\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY region_id,monitoring_end_month\n" +
                "ORDER BY monitoring_end_month DESC", new BeanPropertyRowMapper<>(MonthAuditors.class), startDate, endDate);
    }

}
