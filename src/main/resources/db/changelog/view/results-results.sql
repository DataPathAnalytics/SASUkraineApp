--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS results_results;

CREATE MATERIALIZED VIEW results_results AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       r.id             AS region_id,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.id             AS monitoring_id,
       m.result         AS monitoring_result
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN region AS r ON p.region_id = r.id
         JOIN monitoring AS m ON t.id = m.tender_id
WHERE t.has_monitoring IS TRUE;
