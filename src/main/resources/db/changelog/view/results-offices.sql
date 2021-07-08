--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS results_offices;

CREATE MATERIALIZED VIEW results_offices AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       p.id             AS procuring_entity_id,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.result         AS monitoring_result,
       o.id             AS sasu_office_id,
       o.name           AS sasu_office_name,
       v.id             AS violation_id,
       v.name           AS violation_name
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN monitoring AS m ON t.id = m.tender_id
         JOIN sasu_office AS o ON m.sasu_office_id = o.id
         JOIN monitoring_violation AS mv ON mv.monitoring_id = m.id
         JOIN violation AS v ON mv.violation_id = v.id;
