package com.datapath.sasu.integration.prozorro.monitoring;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HasMonitoringUpdater {

    private JdbcTemplate jdbcTemplate;


    public void update() {
        String query = "UPDATE tender SET has_monitoring = FALSE;\n" +
                "UPDATE tender\n" +
                "SET has_monitoring = TRUE\n" +
                "WHERE id IN (\n" +
                "    SELECT tender_id\n" +
                "    FROM monitoring\n" +
                "    WHERE concluded IS TRUE\n" +
                ")";
        jdbcTemplate.execute(query);
    }

}
