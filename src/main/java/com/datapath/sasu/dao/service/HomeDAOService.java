package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.entity.DateInteger;
import com.datapath.sasu.dao.response.HomeDAOResponse;
import com.datapath.sasu.dao.response.HomeDAOResponse.ProcuringEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Component
@AllArgsConstructor
public class HomeDAOService {

    private final JdbcTemplate jdbcTemplate;

    public HomeDAOResponse getResponse() {
        HomeDAOResponse response = new HomeDAOResponse();
        //fixme save date anywhere after recalculate views
        response.setUpdatedDate(LocalDate.now().minusDays(1));

        List<ProcuringEntity> top20ProcuringEntities = getTop20ProcuringEntities();
        List<DateInteger> tendersDynamic = getTendersDynamic();
        List<DateInteger> violationsDynamic = getViolationsDynamic();

        response.setTendersAmount(getTendersAmount());
        response.setProcuringEntitiesCount(getProcuringEntitiesCount());
        response.setProcuringEntitiesWithViolationsCount(getProcuringEntitiesWithViolationsCount());
        response.setTendersCount(getTendersCount());
        response.setViolationsCount(getViolationsCount());

        response.setTop20ProcuringEntity(top20ProcuringEntities);
        response.setTendersDynamic(tendersDynamic);
        response.setViolationsDynamic(violationsDynamic);

        return response;
    }

    private List<ProcuringEntity> getTop20ProcuringEntities() {
        String sql = "SELECT *\n" +
                "FROM (\n" +
                "         SELECT SUBSTR(procuring_entity_outer_id, 7) AS outer_id,\n" +
                "                procuring_entity_name                AS name,\n" +
                "                SUM(tender_expected_value)           AS amount\n" +
                "         FROM home\n" +
                "         WHERE monitoring_result IN ('addressed', 'completed')\n" +
                "         GROUP BY procuring_entity_outer_id, procuring_entity_name\n" +
                "     ) pr\n" +
                "WHERE amount > 10000000\n" +
                "ORDER BY RANDOM()\n" +
                "LIMIT 20";
        return jdbcTemplate.query(sql, newInstance(ProcuringEntity.class));
    }

    private Long getTendersAmount() {
        String sql = "SELECT SUM(tender_expected_value) FROM home";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    private Integer getProcuringEntitiesCount() {
        String sql = "SELECT COUNT(DISTINCT procuring_entity_id) FROM home";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private Integer getProcuringEntitiesWithViolationsCount() {
        String sql = "SELECT COUNT(DISTINCT procuring_entity_id) FROM home WHERE monitoring_result IN ('addressed', 'completed')";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private Integer getTendersCount() {
        String sql = "SELECT COUNT(DISTINCT tender_id) FROM home";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private Integer getViolationsCount() {
        String sql = "SELECT COUNT(DISTINCT tender_id) FROM home " +
                "WHERE monitoring_result IN ('addressed','completed')";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private List<DateInteger> getTendersDynamic() {
        LocalDate endMonitoringDate = LocalDate.now().withDayOfMonth(1);
        LocalDate startMonitoringDate = endMonitoringDate.minusYears(2);

        String sql = "SELECT monitoring_end_month AS date, COUNT(DISTINCT tender_id) AS value \n" +
                "FROM home\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?)\n" +
                "GROUP BY monitoring_end_month\n" +
                "ORDER BY monitoring_end_month";
        return jdbcTemplate.query(sql, newInstance(DateInteger.class), startMonitoringDate, endMonitoringDate);
    }

    private List<DateInteger> getViolationsDynamic() {
        LocalDate endMonitoringDate = LocalDate.now().withDayOfMonth(1);
        LocalDate startMonitoringDate = endMonitoringDate.minusYears(2);

        String sql = "SELECT monitoring_end_month AS date, COUNT(DISTINCT tender_id) AS value \n" +
                "FROM home\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " +
                "AND monitoring_result IN ('addressed','completed')\n" +
                "GROUP BY monitoring_end_month\n" +
                "ORDER BY monitoring_end_month";
        return jdbcTemplate.query(sql, newInstance(DateInteger.class), startMonitoringDate, endMonitoringDate);
    }
}
