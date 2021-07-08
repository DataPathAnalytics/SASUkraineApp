--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS resources_dasu_geography;

CREATE MATERIALIZED VIEW resources_dasu_geography AS

SELECT m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.auditor_id  AS auditor_id,
       o.region_id   AS region_id
FROM monitoring AS m
         LEFT JOIN sasu_office AS o ON m.sasu_office_id = o.id
