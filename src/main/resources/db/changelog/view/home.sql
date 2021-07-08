--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS home;

CREATE MATERIALIZED VIEW home AS

SELECT t.id                            AS tender_id,
       t.expected_value                AS tender_expected_value,
       p.name                          AS procuring_entity_name,
       p.id                            AS procuring_entity_id,
       p.outer_id                      AS procuring_entity_outer_id,
       m.result                        AS monitoring_result,
       m.end_month                     AS monitoring_end_month,
       m.end_date                      AS monitoring_end_date,
       (SELECT COUNT(mv.violation_id)
        FROM monitoring_violation mv
        WHERE mv.monitoring_id = m.id) AS violations_count
FROM tender AS t
         JOIN procuring_entity AS p ON p.id = t.procuring_entity_id
         JOIN monitoring AS m ON t.id = m.tender_id
WHERE t.has_monitoring IS TRUE AND p.region_id IS NOT NULL
