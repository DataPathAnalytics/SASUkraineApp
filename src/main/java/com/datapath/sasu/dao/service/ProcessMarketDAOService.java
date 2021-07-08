package com.datapath.sasu.dao.service;

import com.datapath.sasu.dao.request.ProcessMarketDAORequest;
import com.datapath.sasu.dao.response.ProcessMarketDAOResponse;
import com.datapath.sasu.dao.response.ProcessMarketDAOResponse.Category;
import com.datapath.sasu.dao.response.ProcessMarketDAOResponse.Cpv;
import com.datapath.sasu.dao.response.ProcessMarketDAOResponse.CpvDynamic;
import com.datapath.sasu.dao.response.ProcessMarketDAOResponse.CpvTenders;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

@Component
@AllArgsConstructor
public class ProcessMarketDAOService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public ProcessMarketDAOResponse getResponse(ProcessMarketDAORequest request) {
        ProcessMarketDAOResponse response = new ProcessMarketDAOResponse();

        response.setCpvCount(getCpvCount());
        response.setCpv2Count(getCpv2Count(request));
        List<CpvTenders> cpvTenders = getCpvTenders(request);
        cpvTenders.forEach(cpvTender -> cpvTender.setCategories(getCpv2Categories(request, cpvTender.getCpv2()).stream()
                .map(Category::getId).collect(toList())));

        response.setCpvTenders(cpvTenders);
        response.setCpvDynamics(getCpvDynamic(request));
        List<Category> categories = getCategories(request);
        for (Category category : categories) {
            List<Cpv> topCpv2ByTendersCount = getTopCpv2ByTendersCount(request, category.getId());
            topCpv2ByTendersCount.forEach(cpv2 -> {
                List<Cpv> topCpv3ByTendersCount = getTopCpv3ByTendersCount(request, cpv2.getCode(), category.getId());
                cpv2.setTopChildCpvByTendersCount(topCpv3ByTendersCount);
            });

            List<Cpv> topCpv2ByAmount = getTopCpv2ByAmount(request, category.getId());
            topCpv2ByAmount.forEach(cpv2 -> {
                List<Cpv> topCpv3ByAmount = getTopCpv3ByAmount(request, cpv2.getCode(), category.getId());
                cpv2.setTopChildCpvByAmount(topCpv3ByAmount);
            });
            category.setTopCpv2ByTendersCount(topCpv2ByTendersCount);
            category.setTopCpv2ByAmount(topCpv2ByAmount);
        }
        response.setCategories(categories);

        return response;
    }

    private List<Cpv> getTopCpv2ByTendersCount(ProcessMarketDAORequest request, Integer categoryId) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv2 AS code, " +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv2), " +
                " COUNT(DISTINCT tender_id) AS tenders_count " +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND category_id = ?" + regionClause +
                "GROUP BY  cpv2 " +
                "ORDER BY tenders_count DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), categoryId);
    }

    private List<Cpv> getTopCpv2ByAmount(ProcessMarketDAORequest request, Integer categoryId) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv2 AS code," +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv2)," +
                " SUM(tender_expected_value) AS amount " +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND category_id = ?" + regionClause +
                "GROUP BY  cpv2 " +
                "ORDER BY amount DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), categoryId);
    }

    private List<Cpv> getTopCpv3ByTendersCount(ProcessMarketDAORequest request, String cpv2, Integer categoryId) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv3 AS code," +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv3),\n" +
                " COUNT(DISTINCT tender_id) AS tenders_count\n" +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv2 = ? AND category_id = ? AND cpv3 <> 'undefined' " + regionClause +
                "GROUP BY  cpv3 " +
                "ORDER BY tenders_count DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), cpv2, categoryId);
    }

    private List<Cpv> getTopCpv3ByAmount(ProcessMarketDAORequest request, String cpv2, Integer categoryId) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv3 AS code," +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv3),\n" +
                " SUM(tender_expected_value) AS amount " +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv2 = ? AND category_id = ?  AND cpv3 <> 'undefined' " + regionClause +
                "GROUP BY cpv3 " +
                "ORDER BY amount DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), cpv2, categoryId);
    }

    private List<Cpv> getTopCpv4ByTendersCount(ProcessMarketDAORequest request, String cpv3, Integer categoryId) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv4 AS code," +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv4),\n" +
                " COUNT(DISTINCT tender_id) AS tenders_count\n" +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv3 = ? AND category_id = ?" + regionClause +
                "GROUP BY  cpv4 " +
                "ORDER BY tenders_count DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), cpv3, categoryId);
    }

    private List<Cpv> getTopCpv4ByAmount(ProcessMarketDAORequest request, String cpv3, Integer categoryId) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT  cpv4 AS code," +
                "(SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv4),\n" +
                " SUM(tender_expected_value) AS amount " +
                "FROM process_market pm " +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv3 = ? AND category_id = ?" + regionClause +
                "GROUP BY  cpv4 " +
                "ORDER BY amount DESC LIMIT 5";

        return jdbcTemplate.query(query, newInstance(Cpv.class), request.getStartDate(), request.getEndDate(), cpv3, categoryId);
    }

    private Integer getCpvCount() {
        String sql = "SELECT COUNT(DISTINCT cpv_id) FROM process_market WHERE has_monitoring IS TRUE";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private Integer getCpv2Count(ProcessMarketDAORequest request) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));


        String sql = "SELECT COUNT(DISTINCT cpv2) FROM process_market WHERE (process_market.monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause;
        return jdbcTemplate.queryForObject(sql, Integer.class, request.getStartDate(), request.getEndDate());
    }

    private List<CpvTenders> getCpvTenders(ProcessMarketDAORequest request) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT pm.cpv2, COUNT(DISTINCT tender_id) AS tendersCount, SUM(tender_expected_value) AS amount,\n" +
                "       (SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv2) as cpv_name\n" +
                "FROM process_market pm " +
                "WHERE (pm.monitoring_end_date >= ? AND pm.monitoring_end_date < ?) " + regionClause +
                "GROUP BY pm.cpv2";

        return jdbcTemplate.query(query, newInstance(CpvTenders.class), request.getStartDate(), request.getEndDate());
    }

    private List<Category> getCpv2Categories(ProcessMarketDAORequest request, String cpv2) {
        String query = "SELECT category_id AS id, category_name AS name " +
                "FROM process_market\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv2 = ? " +
                "GROUP BY category_id,category_name";

        return jdbcTemplate.query(query, newInstance(Category.class), request.getStartDate(), request.getEndDate(), cpv2);
    }

    private List<Category> getCpv2Categories(ProcessMarketDAORequest request) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT category_id AS id, category_name AS name,COUNT(DISTINCT tender_id) AS tendersCount, SUM(tender_expected_value) AS amount\n " +
                "FROM process_market\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) AND cpv2 = ? " + regionClause +
                "GROUP BY category_id,category_name";

        return jdbcTemplate.query(query, newInstance(Category.class), request.getStartDate(), request.getEndDate(), request.getCpv2());
    }

    private List<CpvDynamic> getCpvDynamic(ProcessMarketDAORequest request) {
        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT cpv2,monitoring_end_month     AS date,\n" +
                "       COUNT(DISTINCT tender_id)           AS tendersCount,\n" +
                "       SUM(tender_expected_value) AS amount\n" +
                "FROM process_market\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY cpv2,monitoring_end_month";

        return jdbcTemplate.query(query, newInstance(CpvDynamic.class), request.getStartDate(), request.getEndDate());
    }

    private List<Category> getCategories(ProcessMarketDAORequest request) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        String query = "SELECT category_id AS id, category_name AS name, COUNT(DISTINCT tender_id) AS tendersCount, SUM(tender_expected_value) AS amount\n" +
                "FROM process_market\n" +
                "WHERE (monitoring_end_date >= ? AND monitoring_end_date < ?) " + regionClause +
                "GROUP BY category_id,category_name";

        return jdbcTemplate.query(query, newInstance(Category.class), request.getStartDate(), request.getEndDate());

    }

    public Cpv getCpv2(ProcessMarketDAORequest request, Integer categoryId) {

        String regionClause = isEmpty(request.getRegions()) ? ""
                : String.format(" AND region_id IN (%s) ", collectionToCommaDelimitedString(request.getRegions()));

        return jdbcTemplate.queryForObject("SELECT cpv2 AS code, " +
                "       (SELECT name FROM cpv_catalogue WHERE cpv = pm.cpv2) AS name, " +
                "       category_id, " +
                "       COUNT(DISTINCT tender_id)                               tenders_count, " +
                "       SUM(tender_expected_value)                              amount " +
                "FROM process_market pm " +
                "WHERE cpv2 = ? AND (monitoring_end_date >= ? AND monitoring_end_date < ?) AND category_id = ?" + regionClause +
                "GROUP BY cpv2,category_id", newInstance(Cpv.class), request.getCpv2(), request.getStartDate(), request.getEndDate(), categoryId);
    }

    public ProcessMarketDAOResponse getCpv2Tree(ProcessMarketDAORequest request) {

        List<Cpv> cpv2Tree = new ArrayList<>();

        List<Category> categories = getCpv2Categories(request);

        categories.forEach(category -> {
            Cpv cpv2 = getCpv2(request, category.getId());

            List<Cpv> topCpv3ByAmount = getTopCpv3ByAmount(request, cpv2.getCode(), category.getId());
            topCpv3ByAmount.forEach(cpv3 -> cpv3.setTopChildCpvByAmount(getTopCpv4ByAmount(request, cpv3.getCode(), category.getId())));
            cpv2.setTopChildCpvByAmount(topCpv3ByAmount);

            List<Cpv> topCpv3ByTendersCount = getTopCpv3ByTendersCount(request, cpv2.getCode(), category.getId());
            topCpv3ByTendersCount.forEach(cpv3 -> cpv3.setTopChildCpvByTendersCount(getTopCpv4ByTendersCount(request, cpv3.getCode(), category.getId())));
            cpv2.setTopChildCpvByTendersCount(topCpv3ByTendersCount);

            cpv2Tree.add(cpv2);

        });

        ProcessMarketDAOResponse response = new ProcessMarketDAOResponse();
        response.setCpv2Tree(cpv2Tree);
        return response;
    }


}
