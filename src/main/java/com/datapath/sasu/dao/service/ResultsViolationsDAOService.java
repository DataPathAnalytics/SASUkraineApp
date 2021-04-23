package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ResultsViolationsDAORequest;
import com.datapath.sasu.dao.response.ResultsViolationsDAOResponse;
import com.datapath.sasu.dao.response.ResultsViolationsDAOResponse.TenderByViolation;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;

@Component
@AllArgsConstructor
public class ResultsViolationsDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ResultsViolationsDAOResponse getResponse(ResultsViolationsDAORequest request) {
        ResultsViolationsDAOResponse response = new ResultsViolationsDAOResponse();
        response.setTotalProcuringEntitiesCount(getTotalProcuringEntitiesCount());
        response.setProcuringEntitiesCount(getProcuringEntitiesCount(request));
        response.setTendersByViolation(getTendersByViolation(request));

        return response;
    }

    private Integer getTotalProcuringEntitiesCount() {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) FROM results_violations";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getProcuringEntitiesCount(ResultsViolationsDAORequest request) {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) FROM results_violations";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private List<TenderByViolation> getTendersByViolation(ResultsViolationsDAORequest request) {
        String query = "WITH tenders AS (\n" +
                "    SELECT COUNT(DISTINCT tender_id) count FROM results_violations\n" +
                ")\n" +
                "SELECT violation_id,\n" +
                "       COUNT(DISTINCT tender_id)                                 AS tenders_count,\n" +
                "       COUNT(DISTINCT tender_id) * 100 / (SELECT * FROM tenders) AS percent\n" +
                "FROM results_violations\n" +
                "GROUP BY violation_id";
        return jdbcTemplate.query(query, newInstance(TenderByViolation.class));
    }



}
