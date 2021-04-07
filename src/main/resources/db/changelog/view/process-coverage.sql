--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS process_coverage;

CREATE MATERIALIZED VIEW process_coverage AS
SELECT t.id             AS tender_id,
       t.start_date     AS tender_start_date,
       t.date           AS tender_date,
       t.expected_value AS tender_expected_value,
       t.status         AS tender_status,
       m.id IS NOT NULL AS has_monitoring,
       p.id             AS procuring_entity_id,
       r.id             AS region_id,
       a.id             AS award_id,
       a.value          AS award_value,
       m.start_date     AS monitoring_start_date,
       m.result         AS monitoring_result
FROM tender AS t
         JOIN procuring_entity AS p ON p.id = t.procuring_entity_id
         JOIN region AS r ON r.id = p.region_id
         LEFT JOIN award AS a ON t.id = a.tender_id
         LEFT JOIN monitoring AS m ON t.id = m.tender_id;
