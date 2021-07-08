package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessRegionsDAORequest;
import com.datapath.sasu.dao.response.ProcessRegionsDAOResponse;
import com.datapath.sasu.dao.response.ProcessRegionsDAOResponse.Cpv;
import com.datapath.sasu.dao.response.ProcessRegionsDAOResponse.RegionProcuringEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ProcessRegionsDAOService {

    private final JdbcTemplate jdbcTemplate;

    public ProcessRegionsDAOResponse getResponse(ProcessRegionsDAORequest request) {
        var response = new ProcessRegionsDAOResponse();
        response.setTotalProcuringEntitiesCount(getTotalProcuringEntitiesCount());
        response.setProcuringEntitiesCount(getProcuringEntitiesCount(request));

        List<RegionProcuringEntity> procuringEntitiesByRegion = getProcuringEntitiesByRegion(request);
        response.setProcuringEntitiesCountByRegion(procuringEntitiesByRegion);

        response.setTopCpv2ByAmount(getTopCpv2ByAmount(request));
        response.setTopCpv2ByTendersCount(getTopCpv2ByTendersCount(request));

        response.setAmount(getTendersAmount(request));
        response.setTendersCount(getTendersCount(request));


        return response;
    }

    private Integer getTotalProcuringEntitiesCount() {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) FROM process_regions";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getProcuringEntitiesCount(ProcessRegionsDAORequest request) {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) " +
                "FROM process_regions " +
                "WHERE has_monitoring IS TRUE AND (monitoring_end_date >= ? AND monitoring_end_date < ?)";
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private List<RegionProcuringEntity> getProcuringEntitiesByRegion(ProcessRegionsDAORequest request) {

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT r.id                                AS region_id,\n" +
                "       COUNT(DISTINCT procuring_entity_id) AS procuring_entities_count\n" +
                "FROM region r\n" +
                "         LEFT JOIN process_regions pr ON pr.procuring_entity_region_id = r.id AND\n" +
                "                                         has_monitoring IS TRUE AND \n" +
                "                                         (monitoring_end_date >= ? AND\n" +
                "                                          monitoring_end_date < ?)" + sasuRegionClause +
                "GROUP BY r.id";
        return jdbcTemplate.query(query, newInstance(RegionProcuringEntity.class),
                request.getStartDate(), request.getEndDate());
    }


    private List<Cpv> getTopCpv2ByTendersCount(ProcessRegionsDAORequest request) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT pr.cpv2                                              AS code,\n" +
                "       (SELECT name FROM cpv_catalogue WHERE cpv = pr.cpv2) AS name,\n" +
                "       COALESCE(COUNT(DISTINCT tender_id),0)                           AS tenders_count\n" +
                "FROM process_regions pr\n" +
                "WHERE has_monitoring IS TRUE AND (monitoring_end_date >= ? AND monitoring_end_date < ?) " + sasuRegionClause + procuringEntityRegionClause +
                "GROUP BY pr.cpv2\n" +
                "ORDER BY tenders_count DESC\n" +
                "LIMIT 5";


        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate());
    }

    private List<Cpv> getTopCpv2ByAmount(ProcessRegionsDAORequest request) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT pr.cpv2                                 AS code,\n" +
                "       (SELECT name FROM cpv_catalogue WHERE cpv = pr.cpv2)        AS name,\n" +
                "       COALESCE(SUM(tender_expected_value), 0) AS amount\n" +
                "FROM process_regions pr\n" +
                "WHERE has_monitoring IS TRUE AND (monitoring_end_date >= ? AND monitoring_end_date < ?)"
                + sasuRegionClause + procuringEntityRegionClause +
                "GROUP BY pr.cpv2\n" +
                "ORDER BY amount DESC\n" +
                "LIMIT 5";
        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate());
    }

    private Double getTendersAmount(ProcessRegionsDAORequest request) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));


        String query = "SELECT COALESCE(SUM(tender_expected_value), 0) FROM process_regions WHERE has_monitoring IS TRUE " + sasuRegionClause + procuringEntityRegionClause;
        return jdbcTemplate.queryForObject(query, Double.class);
    }


    private Integer getTendersCount(ProcessRegionsDAORequest request) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT COALESCE(COUNT(DISTINCT tender_id),0) FROM process_regions WHERE has_monitoring IS TRUE " + sasuRegionClause + procuringEntityRegionClause;
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

}
