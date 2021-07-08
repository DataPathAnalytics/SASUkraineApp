--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS process_duration;

CREATE MATERIALIZED VIEW process_duration AS
SELECT t.local_method AS local_method,
       t.competitive  AS competitive,
       m.duration     AS monitoring_duration,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       m.concluded    AS monitoring_cocluded,
       o.region_id    AS region_id
FROM tender AS t
         JOIN monitoring AS m ON t.id = m.tender_id
         JOIN sasu_office AS o ON m.sasu_office_id = o.id
WHERE duration IS NOT NULL;
