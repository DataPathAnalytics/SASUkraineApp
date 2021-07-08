package com.datapath.sasu.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ViewsUpdateService {

    private final JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "${views.update.cron}")
    public void update() {
        String query = "" +
                "REFRESH MATERIALIZED VIEW home;\n" +
                "REFRESH MATERIALIZED VIEW resources_dasu_geography;\n" +
                "REFRESH MATERIALIZED VIEW resources_dynamics;\n" +
                "REFRESH MATERIALIZED VIEW process_market;\n" +
                "REFRESH MATERIALIZED VIEW process_regions;\n" +
                "REFRESH MATERIALIZED VIEW process_methods;\n" +
                "REFRESH MATERIALIZED VIEW process_coverage;\n" +
                "REFRESH MATERIALIZED VIEW results_results;" +
                "REFRESH MATERIALIZED VIEW results_offices;" +
                "REFRESH MATERIALIZED VIEW results_violations;" +
                "";
        jdbcTemplate.execute(query);
    }
}
