--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS process_methods;

CREATE MATERIALIZED VIEW process_methods AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       m.id IS NOT NULL AS has_monitoring,
       t.local_method   AS tender_local_method,
       p.id             AS procuring_entity_id,
       r.id             AS region_id,
       m.start_date     AS monitoring_start_date,
       m.start_month    AS monitoring_start_month
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN region AS r ON p.region_id = r.id
         LEFT JOIN monitoring AS m ON t.id = m.tender_id
