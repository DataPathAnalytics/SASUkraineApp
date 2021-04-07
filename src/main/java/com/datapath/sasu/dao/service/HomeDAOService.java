package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.entity.DateInteger;
import com.datapath.sasu.dao.response.HomeDAOResponse;
import com.datapath.sasu.dao.response.HomeDAOResponse.ProcuringEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Component
@AllArgsConstructor
public class HomeDAOService {

    private final JdbcTemplate jdbcTemplate;

    public HomeDAOResponse getResponse() {
        HomeDAOResponse response = new HomeDAOResponse();

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
        String sql = "SELECT procuring_entity_id AS outer_id, procuring_entity_name AS name, SUM(tender_value) AS amount\n" +
                "FROM home\n" +
                "WHERE monitoring_result IN ('addressed', 'completed')\n" +
                "GROUP BY procuring_entity_id,procuring_entity_name\n" +
                "ORDER BY amount DESC\n" +
                "LIMIT 20";
        return jdbcTemplate.query(sql, newInstance(ProcuringEntity.class));
    }

    private List<DateInteger> getTendersDynamic() {
        String sql = "SELECT monitoring_start_month AS date, COUNT(DISTINCT tender_id) AS value \n" +
                "FROM home\n" +
                "GROUP BY monitoring_start_month\n" +
                "ORDER BY monitoring_start_month";
        return jdbcTemplate.query(sql, newInstance(DateInteger.class));
    }

    private List<DateInteger> getViolationsDynamic() {
        String sql = "SELECT monitoring_start_month AS date, COUNT( violation_id) AS value \n" +
                "FROM home\n" +
                "GROUP BY monitoring_start_month\n" +
                "ORDER BY monitoring_start_month";
        return jdbcTemplate.query(sql, newInstance(DateInteger.class));
    }

    private Long getTendersAmount() {
        String sql = "SELECT SUM(tender_value) FROM home";
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
        String sql = "SELECT COUNT(violation_id) FROM home WHERE monitoring_result IN ('addressed', 'completed')";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
