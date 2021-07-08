--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS resources_dynamics;

CREATE MATERIALIZED VIEW resources_dynamics AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       t.has_monitoring AS has_monitoring,
       t.start_date     AS tender_start_date,
       t.status         AS tender_status,
       t.date           AS tender_date,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       a.id             AS auditor_id,
       r.id             AS region_id,
       r.name           AS region_name
FROM tender AS t
         LEFT JOIN monitoring AS m ON t.id = m.tender_id
         LEFT JOIN auditor AS a ON a.id = m.auditor_id
         LEFT JOIN sasu_office AS s ON s.id = m.sasu_office_id
         LEFT JOIN region AS r ON s.region_id = r.id;
