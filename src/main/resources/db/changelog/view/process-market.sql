--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS process_market;

CREATE MATERIALIZED VIEW process_market AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       pc.id            AS category_id,
       pc.name_ua       AS category_name,
       i.cpv_id,
       c.cpv4,
       c.cpv3,
       c.cpv2,
       t.has_monitoring,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date,
       r.id             AS region_id
FROM tender AS t
         JOIN tender_item AS i ON t.id = i.tender_id
         JOIN cpv_catalogue AS c ON i.cpv_id = c.id
         JOIN procurement_category AS pc ON t.procurement_category_id = pc.id
         LEFT JOIN monitoring AS m ON t.id = m.tender_id
         LEFT JOIN sasu_office AS o ON m.sasu_office_id = o.id
         LEFT JOIN region AS r ON o.region_id = r.id
WHERE t.has_monitoring IS TRUE;
