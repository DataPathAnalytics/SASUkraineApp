package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessRegionsDAORequest;
import com.datapath.sasu.dao.response.ProcessRegionsCpvDAOResponse;
import com.datapath.sasu.dao.response.ProcessRegionsCpvDAOResponse.DateCpv;
import com.datapath.sasu.dao.response.ProcessRegionsDAOResponse;
import com.datapath.sasu.dao.response.ProcessRegionsDAOResponse.RegionProcuringEntity;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.datapath.sasu.dao.response.ProcessRegionsCpvDAOResponse.Cpv;
import static java.util.stream.Collectors.toList;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ProcessRegionsDAOService {

    private final JdbcTemplate jdbcTemplate;

    public ProcessRegionsDAOResponse getResponse(ProcessRegionsDAORequest request) {
        ProcessRegionsDAOResponse response = new ProcessRegionsDAOResponse();
        response.setTotalProcuringEntitiesCount(getTotalProcuringEntitiesCount());
        response.setProcuringEntitiesCount(getProcuringEntitiesCount(request));

        List<RegionProcuringEntity> procuringEntitiesByRegion = getProcuringEntitiesByRegion(request);
        response.setProcuringEntitiesCountByRegion(procuringEntitiesByRegion);
        return response;
    }

    private Integer getTotalProcuringEntitiesCount() {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) FROM process_regions";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    private Integer getProcuringEntitiesCount(ProcessRegionsDAORequest request) {
        String query = "SELECT COUNT(DISTINCT procuring_entity_id) " +
                "FROM process_regions " +
                "WHERE (monitoring_start_date >= ? AND monitoring_start_date < ?)";
        return jdbcTemplate.queryForObject(query, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private List<RegionProcuringEntity> getProcuringEntitiesByRegion(ProcessRegionsDAORequest request) {

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT r.id                                AS region_id,\n" +
                "       COUNT(DISTINCT procuring_entity_id) AS procuring_entities_count\n" +
                "FROM region r\n" +
                "         LEFT JOIN process_regions pr ON pr.procuring_entity_region_id = r.id AND\n" +
                "                                         (monitoring_start_date >= ? AND\n" +
                "                                          monitoring_start_date < ?)" + sasuRegionClause +
                "GROUP BY r.id";
        return jdbcTemplate.query(query, newInstance(RegionProcuringEntity.class),
                request.getStartDate(), request.getEndDate());
    }


    public ProcessRegionsCpvDAOResponse getTopCpv2(ProcessRegionsDAORequest request) {

        List<DateCpv> topCpvByTendersCount = new ArrayList<>();
        List<DateCpv> topCpvByAmount = new ArrayList<>();

        List<LocalDate> months = request.getStartDate().datesUntil(request.getEndDate(), Period.ofMonths(1)).collect(toList());
        for (LocalDate month : months) {
            String date = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<Cpv> topByTendersCount = getTopCpv2TendersCountByMonth(request, date);
            List<Cpv> topByAmount = getTopCpv2AmountByMonth(request, date);

            topCpvByTendersCount.add(new DateCpv(date, topByTendersCount));
            topCpvByAmount.add(new DateCpv(date, topByAmount));
        }

        ProcessRegionsCpvDAOResponse response = new ProcessRegionsCpvDAOResponse();
        response.setTopCpv2ByTendersCount(topCpvByTendersCount);
        response.setTopCpv2ByAmount(topCpvByAmount);
        return response;
    }

    private List<Cpv> getTopCpv2TendersCountByMonth(ProcessRegionsDAORequest request, String month) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT cc.cpv_code                                          AS code,\n" +
                "cc.name AS name,\n" +
                "       COALESCE(COUNT(DISTINCT tender_id),0)                            AS tenders_count\n" +
                "FROM cpv_catalogue cc\n" +
                "         LEFT JOIN process_regions pr ON cc.cpv = pr.cpv2 AND monitoring_start_month = ? " + sasuRegionClause + procuringEntityRegionClause +
                "WHERE cc.cpv = cc.cpv2 " +
                "GROUP BY cc.cpv_code,cc.name";

        return jdbcTemplate.query(query, newInstance(Cpv.class), month);
    }

    private List<Cpv> getTopCpv2AmountByMonth(ProcessRegionsDAORequest request, String month) {

        String procuringEntityRegionClause = isEmpty(request.getProcuringEntityRegions()) ? ""
                : String.format(" AND procuring_entity_region_id IN (%s) ", collectionToCommaDelimitedString(request.getProcuringEntityRegions()));

        String sasuRegionClause = isEmpty(request.getSasuRegions()) ? ""
                : String.format(" AND sasu_region_id IN (%s) ", collectionToCommaDelimitedString(request.getSasuRegions()));

        String query = "SELECT cc.cpv_code                                          AS code,\n" +
                "cc.name AS name,\n" +
                " COALESCE(SUM(tender_expected_value),0) AS amount " +
                "FROM cpv_catalogue cc\n" +
                "         LEFT JOIN process_regions pr ON cc.cpv = pr.cpv2 AND monitoring_start_month = ?" + sasuRegionClause + procuringEntityRegionClause +
                "WHERE cc.cpv = cc.cpv2  " +
                "GROUP BY cc.cpv_code,cc.name";
        return jdbcTemplate.query(query, newInstance(Cpv.class), month);
    }

}
