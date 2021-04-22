--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS results_violations;

CREATE MATERIALIZED VIEW results_violations AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       p.id             AS procuring_entity_id,
       r.id             AS procuring_entity_region_id,
       m.start_date     AS monitoring_start_date,
       m.result         AS monitoring_result,
       s.sasu_region_id AS sasu_region_id,
       v.id             AS violation_id
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN region AS r ON p.region_id = r.id
         JOIN monitoring AS m ON t.id = m.tender_id
         JOIN (
    SELECT m.id AS monitoring_id, r.id AS sasu_region_id
    FROM monitoring AS m
             JOIN sasu_office AS o ON m.sasu_office_id = o.id
             JOIN region AS r ON o.region_id = r.id
) AS s ON m.id = s.monitoring_id
         JOIN monitoring_violation AS mv ON m.id = mv.monitoring_id
         JOIN violation AS v ON mv.violation_id = v.id;
