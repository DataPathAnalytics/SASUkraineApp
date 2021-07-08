package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessDurationDAORequest;
import com.datapath.sasu.dao.response.ProcessDurationDAOResponse;
import com.datapath.sasu.dao.response.ProcessDurationDAOResponse.LocalMethod;
import com.datapath.sasu.dao.response.ProcessDurationDAOResponse.Region;
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
public class ProcessDurationDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ProcessDurationDAOResponse getResponse(ProcessDurationDAORequest request) {
        var response = new ProcessDurationDAOResponse();
        response.setTotalDuration(getTotalDuration());
        response.setDuration(getDuration(request));
        response.setRegions(getRegions(request));
        response.setCompetitiveDuration(getCompetitiveDuration(request));
        response.setNonCompetitiveDuration(getNonCompetitiveDuration(request));
        response.setLocalMethods(getLocalMethods(request));
        return response;
    }

    private Integer getTotalDuration() {
        String query = "SELECT SUM(monitoring_duration) FROM process_duration";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getDuration(ProcessDurationDAORequest request) {
        String query = "SELECT SUM(monitoring_duration) FROM process_duration WHERE TRUE " + getFilters(request);
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private List<Region> getRegions(ProcessDurationDAORequest request) {
        String query = "SELECT r.id AS region_id, COALESCE(ROUND(AVG(monitoring_duration)), 0) AS duration\n" +
                "FROM region r\n" +
                "         LEFT JOIN\n" +
                "     process_duration pd ON r.id = pd.region_id " + getMonitoringDateFilter(request) +
                "GROUP BY r.id";
        return jdbcTemplate.query(query, newInstance(Region.class));
    }

    private Integer getCompetitiveDuration(ProcessDurationDAORequest request) {
        String query = "SELECT AVG(monitoring_duration) FROM process_duration WHERE competitive IS TRUE"
                + getFilters(request);
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getNonCompetitiveDuration(ProcessDurationDAORequest request) {
        String query = "SELECT AVG(monitoring_duration) FROM process_duration WHERE competitive IS FALSE"
                + getFilters(request);
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private List<LocalMethod> getLocalMethods(ProcessDurationDAORequest request) {
        String query = "SELECT local_method AS name, " +
                "MIN(monitoring_duration) min_duration, " +
                "MAX(monitoring_duration) max_duration\n" +
                "FROM process_duration\n" +
                "WHERE TRUE " +
                getFilters(request) +
                "GROUP BY local_method;";
        return jdbcTemplate.query(query, newInstance(LocalMethod.class));
    }

    private String getFilters(ProcessDurationDAORequest request) {
        return getMonitoringDateFilter(request) + getRegionFilter(request);
    }

    private String getMonitoringDateFilter(ProcessDurationDAORequest request) {
        return String.format(" AND (monitoring_end_date >= '%s' AND monitoring_end_date < '%s') ",
                request.getStartDate(), request.getEndDate());
    }

    private String getRegionFilter(ProcessDurationDAORequest request) {
        return isEmpty(request.getRegions()) ? ""
                : String.format("AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));
    }
}
