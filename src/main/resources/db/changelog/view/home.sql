--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS home;

CREATE MATERIALIZED VIEW home AS

SELECT t.id          AS tender_id,
       t.value       AS tender_value,
       p.name        AS procuring_entity_name,
       p.id          AS procuring_entity_id,
       p.outer_id    AS procuring_entity_outer_id,
       m.result      AS monitoring_result,
       m.start_month AS monitoring_start_month,
       mv.violation_id
FROM tender AS t
         JOIN procuring_entity AS p ON p.id = t.procuring_entity_id
         JOIN monitoring AS m ON t.id = m.tender_id
         JOIN monitoring_violation AS mv ON m.id = mv.monitoring_id
