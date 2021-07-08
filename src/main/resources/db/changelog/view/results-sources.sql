--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS results_sources;

CREATE MATERIALIZED VIEW results_sources AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       t.has_monitoring AS has_monitoring,
       p.id             AS procuring_entity_id,
       p.region_id      AS procuring_entity_region_id,
       m.id             AS monitoring_id,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.result         AS monitoring_result,
       mr.reason_id     AS reason_id,
       mv.violation_id  AS violation_id,
       m.sasu_office_id AS office_id,
       (SELECT SUM(a.value) FROM award a  WHERE a.tender_id = t.id) AS awards_value
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN monitoring AS m ON t.id = m.tender_id
         LEFT JOIN monitoring_reason AS mr ON mr.monitoring_id = m.id
         LEFT JOIN monitoring_violation AS mv ON mv.monitoring_id = m.id
WHERE p.region_id IS NOT NULL AND t.has_monitoring IS TRUE;
