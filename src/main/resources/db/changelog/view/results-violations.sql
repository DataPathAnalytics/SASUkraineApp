--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS results_violations;

CREATE MATERIALIZED VIEW results_violations AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       p.id             AS procuring_entity_id,
       p.region_id      AS procuring_entity_region_id,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.result         AS monitoring_result,
       m.sasu_office_id AS office_id,
       v.id             AS violation_id
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN monitoring AS m ON t.id = m.tender_id
         JOIN monitoring_violation AS mv ON m.id = mv.monitoring_id
         JOIN violation AS v ON mv.violation_id = v.id
WHERE p.region_id IS NOT NULL;
